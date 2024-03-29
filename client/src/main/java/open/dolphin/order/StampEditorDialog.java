package open.dolphin.order;

import open.dolphin.client.GUIConst;
import open.dolphin.event.ProxyAction;
import open.dolphin.helper.WindowSupport;
import open.dolphin.order.stampeditor.StampEditor;
import open.dolphin.order.tablepanel.ItemTablePanel;
import open.dolphin.ui.BlockGlass2;
import open.dolphin.ui.HorizontalPanel;
import open.dolphin.ui.sheet.JSheet;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Stamp 編集用の外枠を提供する Dialog.
 * StampHolder, DiagnosisDocument, KartePane から呼ばれる.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public class StampEditorDialog {

    public static final String VALUE_PROP = "value";
    private final PropertyChangeSupport boundSupport;
    private final String entity;
    private final Logger logger;
    private final boolean isNew; // value が null なら new
    /**
     * command buttons
     */
    private JButton okButton;
    private JButton cancelButton;
    /**
     * target editor
     */
    private StampEditor editor;
    private WindowSupport<StampEditorDialog> windowSupport;
    private Object value;
    private BlockGlass2 glass;

    public StampEditorDialog(String entity, Object value) {
        this.entity = entity;
        this.value = value;
        isNew = (value == null);
        boundSupport = new PropertyChangeSupport(this);
        logger = LoggerFactory.getLogger(StampEditorDialog.class);
    }

    /**
     * エディタを開始する.
     */
    public void start() {
        initialize();
    }

    /**
     * GUIコンポーネントを初期化する.
     */
    private void initialize() {
        editor = new StampEditor(this.entity);
        editor.start();

        windowSupport = new WindowSupport<>(editor.getTitle(), this);
        JFrame dialog = windowSupport.getFrame();
        dialog.setIconImage(GUIConst.ICON_DOLPHIN.getImage());
        dialog.setAlwaysOnTop(true);

        okButton = new JButton("カルテに展開");
        okButton.setEnabled(false);
        okButton.addActionListener(e -> {
            value = editor.getValue();
            close();
        });

        cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(e -> {
            value = null;
            close();
        });

        editor.addValidListener(okButton::setEnabled);
        editor.setValue(value);

        // レアイウトする
        HorizontalPanel lowerPanel = new HorizontalPanel();
        lowerPanel.setPanelHeight(32);
        lowerPanel.addGlue();
        lowerPanel.add(cancelButton);
        lowerPanel.add(okButton);

        dialog.add(editor, BorderLayout.CENTER);
        dialog.add(lowerPanel, BorderLayout.SOUTH);

        // CloseBox 処理を登録する
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // CloseBox がクリックされた場合はキャンセルとする
                value = null;
                close();
            }
        });

        //ESC で編集内容破棄してクローズ
        InputMap im = dialog.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "cancel");
        dialog.getRootPane().getActionMap().put("cancel", new ProxyAction(this::escape));

        // command-w で，保存ダイアログを出してから終了
        im.put(KeyStroke.getKeyStroke("meta W"), "close-window");
        dialog.getRootPane().getActionMap().put("close-window", new ProxyAction(() -> {
            int ans = JSheet.showOptionDialog(dialog, "カルテに展開しますか？", "",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null,
                    new String[]{"はい", "いいえ", "キャンセル"}, "はい");
            if (ans == 0) {
                okButton.doClick();
            } else if (ans == 1) {
                cancelButton.doClick();
            }
        }));

        // Command + ENTER で入力
        im.put(KeyStroke.getKeyStroke("meta ENTER"), "done");
        dialog.getRootPane().getActionMap().put("done", new ProxyAction(okButton::doClick));

        // Undo/Redo
        ItemTablePanel tablePanel = editor.getTablePanel();
        im.put(KeyStroke.getKeyStroke("meta Z"), "undo");
        dialog.getRootPane().getActionMap().put("undo", new ProxyAction(tablePanel::undo));
        im.put(KeyStroke.getKeyStroke("shift meta Z"), "redo");
        dialog.getRootPane().getActionMap().put("redo", new ProxyAction(tablePanel::redo));

        dialog.setVisible(true);
        editor.enter(); // フォーカスとる
    }

    /**
     * ESCAPE を押したときの動作. キーワード入力があればクリア，無ければダイアログを閉じる.
     */
    public void escape() {
        MasterSearchPanel p = editor.getMasterSearchPanel();
        JTextField tf = p.getKeywordField();
        if (StringUtils.isEmpty(tf.getText())) {
            cancelButton.doClick();
        } else {
            tf.setText("");
            p.requestFocusOnTextField();
        }
    }

    /**
     * プロパティチェンジリスナを登録する.
     *
     * @param listener プロパティチェンジリスナ
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (!Arrays.asList(boundSupport.getPropertyChangeListeners(StampEditorDialog.VALUE_PROP)).contains(listener)) {
            boundSupport.addPropertyChangeListener(StampEditorDialog.VALUE_PROP, listener);
        }
    }

    /**
     * プロパティチェンジリスナを削除する.
     *
     * @param listener プロパティチェンジリスナ
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        boundSupport.removePropertyChangeListener(StampEditorDialog.VALUE_PROP, listener);
    }

    /**
     * ダイアログを閉じる.
     * 閉じるときにリスナに通知する.
     */
    public void close() {
        editor.dispose();
        boundSupport.firePropertyChange(VALUE_PROP, isNew, value);

        Stream.of(boundSupport.getPropertyChangeListeners()).
            forEach(boundSupport::removePropertyChangeListener);

        windowSupport.dispose();
    }
}
