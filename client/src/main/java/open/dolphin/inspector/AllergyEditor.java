package open.dolphin.inspector;

import open.dolphin.calendar.CalendarPanel;
import open.dolphin.client.GUIConst;
import open.dolphin.event.ProxyAction;
import open.dolphin.event.ProxyDocumentListener;
import open.dolphin.infomodel.AllergyModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.SimpleDate;
import open.dolphin.ui.Focuser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * アレルギデータを編集するエディタクラス.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public class AllergyEditor {
    private Logger logger = LoggerFactory.getLogger(AllergyEditor.class);

    private final AllergyInspector inspector;
    private AllergyModel model;
    private final JDialog dialog;
    private final JButton addBtn;
    private final JButton clearBtn;
    private AllergyEditorView view;
    private boolean ok;
    private String todayString;

    public AllergyEditor(AllergyInspector inspector) {
        this.inspector = inspector;

        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(IInfoModel.DATE_WITHOUT_TIME);
        todayString = sdf.format(today);

        if (Objects.isNull(model = inspector.getSelectedModel())) {
            model = new AllergyModel();
        }

        // init components
        view = new AllergyEditorView();
        view.getFactorFld().getDocument().addDocumentListener((ProxyDocumentListener) e -> checkBtn());
        view.getIdentifiedFld().addMouseListener(new PopupListener());
        view.getIdentifiedFld().putClientProperty("Quaqua.TextComponent.showPopup", false);

        addBtn = new JButton("OK");
        addBtn.addActionListener(e -> add());
        addBtn.setEnabled(false);

        clearBtn = new JButton("クリア");
        clearBtn.addActionListener(e -> clear());
        clearBtn.setEnabled(false);

        JOptionPane pane = new JOptionPane(view,
            JOptionPane.PLAIN_MESSAGE,
            JOptionPane.DEFAULT_OPTION,
            null,
            new Object[]{addBtn, clearBtn, "キャンセル"}, addBtn);
        dialog = pane.createDialog(inspector.getContext().getFrame(), "アレルギー登録");
        dialog.setIconImage(GUIConst.ICON_DOLPHIN.getImage());

        // dialog が開いたら FactorFld にフォーカスを当てる
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                Focuser.requestFocus(view.getFactorFld());
            }
        });

        // command-w でウインドウクローズ
        InputMap im = dialog.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke key = KeyStroke.getKeyStroke("meta W");
        im.put(key, "close-window");
        dialog.getRootPane().getActionMap().put("close-window", new ProxyAction(dialog::dispose));

        // model to view
        view.getFactorFld().setText(model.getFactor());
        view.getMemoFld().setText(model.getMemo());
        if (StringUtils.isEmpty(model.getSeverity())) {
            model.setSeverity(view.getReactionCombo().getItemAt(0));
        }
        view.getReactionCombo().setSelectedItem(model.getSeverity());
        if (StringUtils.isEmpty(model.getIdentifiedDate())) {
            model.setIdentifiedDate(todayString);
        }
        view.getIdentifiedFld().setText(model.getIdentifiedDate());

        // show dialogs
        dialog.setVisible(true);
    }

    public static void show(AllergyInspector inspector) {
        AllergyEditor editor = new AllergyEditor(inspector);
    }

    private void checkBtn() {

        String factor = view.getFactorFld().getText().trim();
        String date = view.getIdentifiedFld().getText().trim();

        boolean newOk = !factor.equals("") && !date.equals("");

        if (ok != newOk) {
            ok = newOk;
            addBtn.setEnabled(ok);
            clearBtn.setEnabled(ok);
        }
    }

    private void add() {
        // view to model
        model.setFactor(view.getFactorFld().getText().trim());
        model.setSeverity((String) view.getReactionCombo().getSelectedItem());
        String memo = view.getMemoFld().getText().trim();
        if (!memo.equals("")) {
            model.setMemo(memo);
        }
        String dateStr = view.getIdentifiedFld().getText().trim();
        if (!dateStr.equals("")) {
            model.setIdentifiedDate(dateStr);
        }
        clear();

        addBtn.setEnabled(false);
        clearBtn.setEnabled(false);
        inspector.add(model);
        // renewal
        model = new AllergyModel();
        model.setIdentifiedDate(todayString);
    }

    private void clear() {
        view.getFactorFld().setText("");
        view.getMemoFld().setText("");
    }

    private class PopupListener extends MouseAdapter {

        private JPopupMenu popup;

        public PopupListener() {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // windows
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup = new JPopupMenu();
                CalendarPanel cp = new CalendarPanel();
                cp.getTable().addCalendarListener(date -> {
                    view.getIdentifiedFld().setText(SimpleDate.simpleDateToMmldate(date));
                    popup.setVisible(false);
                    popup = null;
                });

                popup.insert(cp, 0);
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
}
