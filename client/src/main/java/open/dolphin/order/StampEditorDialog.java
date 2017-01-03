package open.dolphin.order;

import open.dolphin.order.stampeditor.StampEditor;
import ch.randelshofer.quaqua.JSheet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.*;
import open.dolphin.client.BlockGlass;
import open.dolphin.client.ClientContext;
import open.dolphin.event.ProxyAction;
import open.dolphin.helper.ComponentBoundsManager;
import open.dolphin.ui.HorizontalPanel;
import org.apache.log4j.Logger;

/**
 * Stamp 編集用の外枠を提供する Dialog.
 * カルテの StampHolder から呼ばれる.
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class StampEditorDialog implements PropertyChangeListener {

    public static final String VALID_DATA_PROP = "validData";
    public static final String VALUE_PROP = "value";
    private static final Point DEFAULT_LOC = new Point(159,67);
    private static final Dimension DEFAULT_SIZE = new Dimension(924,616);

    /** command buttons */
    private JButton okButton;
    private JButton cancelButton;

    /** target editor */
    private StampEditor editor;
    private final PropertyChangeSupport boundSupport;

    private JFrame dialog;
    private final String entity;
    private Object value;
    private BlockGlass glass;

    private final Logger logger;
    private final boolean isNew; // value が null なら new

    public StampEditorDialog(String entity, Object value)  {
        this.entity = entity;
        this.value = value;
        isNew = (value == null);
        boundSupport = new PropertyChangeSupport(this);
        logger = ClientContext.getBootLogger();
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

        dialog = new JFrame();
        dialog.setAlwaysOnTop(true);

        okButton = new JButton("カルテに展開");
        okButton.setEnabled(false);
        okButton.addActionListener(e -> {
            value = getValue();
            close();
        });

        cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(e -> {
            value = null;
            close();
        });

        // BlockGlass を生成し dialog に設定する
        glass = new BlockGlass();
        dialog.setGlassPane(glass);

        editor = new StampEditor(this.entity);
        editor.start();
        //editor.addValidListener(this::setValid);
        editor.addPropertyChangeListener(VALID_DATA_PROP, this);
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

        dialog.setTitle(editor.getTitle());
        ComponentBoundsManager cm = new ComponentBoundsManager(dialog, DEFAULT_LOC, DEFAULT_SIZE, this);
        cm.revertToPreferenceBounds();

        //ESC で編集内容破棄してクローズ
        InputMap im = dialog.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        im.put(key, "cancel");
        dialog.getRootPane().getActionMap().put("cancel", new ProxyAction(cancelButton::doClick));

        // commnad-w で，保存ダイアログを出してから終了
        key = KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.META_DOWN_MASK);
        im.put(key, "close-window");
        dialog.getRootPane().getActionMap().put("close-window", new ProxyAction(() -> {
            JSheet.showConfirmSheet(dialog, "カルテに展開しますか？", sheetEvent -> {
                // 0=はい, 1=いいえ, 2=キャンセル -1=エスケープキー
                if (sheetEvent.getOption() == 0) {
                    okButton.doClick();
                } else if (sheetEvent.getOption() == 1) {
                    value = null;
                    close();
                }
            });
        }));

        // Command + ENTER で入力
        key = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.META_DOWN_MASK);
        im.put(key, "done");
        dialog.getRootPane().getActionMap().put("done", new ProxyAction(okButton::doClick));

        dialog.setVisible(true);
        editor.enter(); // フォーカスとる
    }

    /**
     * 編集した Stamp を返す.
     * @return
     */
    public Object getValue() {
        return editor.getValue();
    }

    /**
     * プロパティチェンジリスナを登録する.
     * @param prop プロパティ名
     * @param listener プロパティチェンジリスナ
     */
    public void addPropertyChangeListener(String prop, PropertyChangeListener listener) {
        boundSupport.addPropertyChangeListener(prop, listener);
    }

    /**
     * プロパティチェンジリスナを削除する.
     * @param prop プロパティ名
     * @param listener プロパティチェンジリスナ
     */
    public void remopvePropertyChangeListener(String prop, PropertyChangeListener listener) {
        boundSupport.removePropertyChangeListener(prop, listener);
    }

    /**
     * 編集中のモデル値が有効な値かどうかの通知を受け，
     * カルテに展開ボタンを enable/disable にする.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        okButton.setEnabled((Boolean) evt.getNewValue());
    }

    public void setValid(boolean valid) {
        okButton.setEnabled(valid);
    }

    /**
     * ダイアログを閉じる
     */
    public void close() {
        glass.block();
        editor.dispose();
        dialog.setVisible(false);
        dialog.dispose();
        boundSupport.firePropertyChange(VALUE_PROP, isNew, value);
        glass.unblock();
    }
}
