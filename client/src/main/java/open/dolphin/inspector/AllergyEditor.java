package open.dolphin.inspector;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import open.dolphin.calendar.CalendarPanel;
import open.dolphin.event.ProxyAction;
import open.dolphin.event.ProxyDocumentListener;
import open.dolphin.infomodel.AllergyModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.SimpleDate;
import open.dolphin.ui.Focuser;
import open.dolphin.ui.IMEControl;

/**
 * アレルギデータを編集するエディタクラス.
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public class AllergyEditor {

    private final AllergyInspector inspector;
    private AllergyEditorView view;
    private final JDialog dialog;
    private final JButton addBtn;
    private final JButton clearBtn;
    private boolean ok;

    public static void show(AllergyInspector inspector) {
        AllergyEditor editor = new AllergyEditor(inspector);
    }

    public AllergyEditor(AllergyInspector inspector) {

        this.inspector = inspector;
        view = new AllergyEditorView();

        // factor field
        IMEControl.setImeOnIfFocused(view.getFactorFld());
        view.getFactorFld().getDocument().addDocumentListener((ProxyDocumentListener) e -> checkBtn());

        // memo field
        IMEControl.setImeOnIfFocused(view.getMemoFld());

        // identified field
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(IInfoModel.DATE_WITHOUT_TIME);
        String todayString = sdf.format(date);
        view.getIdentifiedFld().setText(todayString);
        view.getIdentifiedFld().addMouseListener(new PopupListener());
        view.getIdentifiedFld().putClientProperty("Quaqua.TextComponent.showPopup", false);
        IMEControl.setImeOffIfFocused(view.getIdentifiedFld());

        addBtn = new JButton("追加");
        addBtn.addActionListener(e -> add());
        addBtn.setEnabled(false);

        clearBtn = new JButton("クリア");
        clearBtn.addActionListener(e -> clear());
        clearBtn.setEnabled(false);

        Object[] options = new Object[]{addBtn,clearBtn, "閉じる"};

        JOptionPane pane = new JOptionPane(view,
                                           JOptionPane.PLAIN_MESSAGE,
                                           JOptionPane.DEFAULT_OPTION,
                                           null,
                                           options, addBtn);
        dialog = pane.createDialog(inspector.getContext().getFrame(), "アレルギー登録");

        // dialog が開いたら FactorFld にフォーカスを当てる
        dialog.addWindowListener(new WindowAdapter(){
            @Override
            public void windowOpened(WindowEvent e) {
                Focuser.requestFocus(view.getFactorFld());
            }
        });

        // command-w でウインドウクローズ
        InputMap im = dialog.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.META_MASK);
        im.put(key, "close-window");
        dialog.getRootPane().getActionMap().put("close-window", new ProxyAction(dialog::dispose));

        dialog.setVisible(true);
    }

    private void checkBtn() {

        String factor = view.getFactorFld().getText().trim();
        String date = view.getIdentifiedFld().getText().trim();

        boolean newOk = ! factor.equals("") && ! date.equals("");

        if (ok != newOk) {
            ok = newOk;
            addBtn.setEnabled(ok);
            clearBtn.setEnabled(ok);
        }
    }

    private void add() {

        final AllergyModel model = new AllergyModel();
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
