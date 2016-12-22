package open.dolphin.inspector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
// import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import open.dolphin.client.CalendarCardPanel;
import open.dolphin.client.ClientContext;
import open.dolphin.infomodel.AllergyModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.SimpleDate;
import open.dolphin.ui.IMEControl;
import open.dolphin.ui.MyJPopupMenu;

/**
 * アレルギデータを編集するエディタクラス.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class AllergyEditor {

    private AllergyInspector inspector;
    private AllergyEditorView view;
    private JDialog dialog;
    private JButton addBtn;
    private JButton clearBtn;
    private boolean ok;

    private void checkBtn() {

        String factor = view.getFactorFld().getText().trim();
        String date = view.getIdentifiedFld().getText().trim();

        boolean newOk = true;
        if (factor.equals("") || date.equals("")) {
            newOk = false;
        }

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
//pns
        clear();

        addBtn.setEnabled(false);
        clearBtn.setEnabled(false);
        inspector.add(model);
    }

    private void clear() {
        view.getFactorFld().setText("");
        view.getMemoFld().setText("");
//pns   view.getIdentifiedFld().setText("");
    }

    class PopupListener extends MouseAdapter implements PropertyChangeListener {

        private MyJPopupMenu popup;
        private JTextField tf;

        // private LiteCalendarPanel calendar;
        public PopupListener(JTextField tf) {
            this.tf = tf;
            tf.addMouseListener(this);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {

            if (e.isPopupTrigger()) {
                popup = new MyJPopupMenu();
                CalendarCardPanel cc = new CalendarCardPanel(ClientContext.getEventColorTable());
                cc.addPropertyChangeListener(CalendarCardPanel.PICKED_DATE, this);
                cc.setCalendarRange(new int[]{-12, 0});
                popup.insert(cc, 0);
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }

        public void propertyChange(PropertyChangeEvent e) {
            if (e.getPropertyName().equals(CalendarCardPanel.PICKED_DATE)) {
                SimpleDate sd = (SimpleDate) e.getNewValue();
                tf.setText(SimpleDate.simpleDateToMmldate(sd));
                popup.setVisible(false);
                popup = null;
            }
        }
    }

    public AllergyEditor(AllergyInspector inspector) {

        this.inspector = inspector;
        view = new AllergyEditorView();
//pns   view.getFactorFld().addFocusListener(AutoKanjiListener.getInstance());
        IMEControl.setImeOnIfFocused(view.getFactorFld());
        view.getFactorFld().getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                checkBtn();
            }

            public void removeUpdate(DocumentEvent e) {
                checkBtn();
            }

            public void changedUpdate(DocumentEvent e) {
                checkBtn();
            }
        });

//pns   view.getMemoFld().addFocusListener(AutoKanjiListener.getInstance());
        IMEControl.setImeOnIfFocused(view.getMemoFld());

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(IInfoModel.DATE_WITHOUT_TIME);
        String todayString = sdf.format(date);
        view.getIdentifiedFld().setText(todayString);
        new PopupListener(view.getIdentifiedFld());
//pns   view.getIdentifiedFld().addFocusListener(AutoRomanListener.getInstance());
        IMEControl.setImeOffIfFocused(view.getIdentifiedFld());

        addBtn = new JButton("追加");
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                add();
            }
        });
        addBtn.setEnabled(false);

        clearBtn = new JButton("クリア");
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        clearBtn.setEnabled(false);

//pns   閉じるボタン追加
//pns   Object[] options = new Object[]{addBtn,clearBtn};
        Object[] options = new Object[]{addBtn,clearBtn, "閉じる"};

        JOptionPane pane = new JOptionPane(view,
                                           JOptionPane.PLAIN_MESSAGE,
                                           JOptionPane.DEFAULT_OPTION,
                                           null,
                                           options, addBtn);
        dialog = pane.createDialog(inspector.getContext().getFrame(), ClientContext.getFrameTitle("アレルギー登録"));

//pns^  dialog が開いたら FactorFld にフォーカスを当てる
        dialog.addWindowListener(new WindowAdapter(){
            @Override
            public void windowOpened(WindowEvent e) {
                // need to invokeLater in java 7
                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run() {
                        view.requestFocusInWindow();
                        view.getFactorFld().requestFocusInWindow();
                    }
                });
            }
        });
//pns$

//pns^  command-w でウインドウクローズ
        InputMap im = dialog.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.META_MASK);
        im.put(key, "close-window");
        dialog.getRootPane().getActionMap().put("close-window", new AbstractAction() {
            private static final long serialVersionUID = 1L;
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
//pns$
        dialog.setVisible(true);
    }
}
