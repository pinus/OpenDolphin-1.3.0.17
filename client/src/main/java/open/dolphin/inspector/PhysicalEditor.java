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
import javax.swing.KeyStroke;
import open.dolphin.client.CalendarCardPanel;
import open.dolphin.client.ClientContext;
import open.dolphin.event.ProxyAction;
import open.dolphin.event.ProxyDocumentListener;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.PhysicalModel;
import open.dolphin.infomodel.SimpleDate;
import open.dolphin.ui.Focuser;
import open.dolphin.ui.IMEControl;
import open.dolphin.ui.MyJPopupMenu;

/**
 * 身長体重データを編集するエディタクラス.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public class PhysicalEditor {

    private final PhysicalInspector inspector;
    private PhysicalEditorView view;
    private final JDialog dialog;
    private final JButton addBtn;
    private final JButton clearBtn;
    private boolean ok;

    public static void show(PhysicalInspector ins) {
        new PhysicalEditor(ins);
    }

    public PhysicalEditor(PhysicalInspector inspector) {
        this.inspector = inspector;

        view = new PhysicalEditorView();

        ProxyDocumentListener dl = e -> checkBtn();

        view.getHeightFld().getDocument().addDocumentListener(dl);
        view.getWeightFld().getDocument().addDocumentListener(dl);
        view.getIdentifiedDateFld().getDocument().addDocumentListener(dl);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(IInfoModel.DATE_WITHOUT_TIME);
        String todayString = sdf.format(date);
        view.getIdentifiedDateFld().setText(todayString);
        view.getIdentifiedDateFld().addMouseListener(new PopupListener());

        IMEControl.setImeOffIfFocused(view.getHeightFld());
        IMEControl.setImeOffIfFocused(view.getWeightFld());
        IMEControl.setImeOffIfFocused(view.getIdentifiedDateFld());

        addBtn = new JButton("追加");
        addBtn.addActionListener(e -> add());
        addBtn.setEnabled(false);

        clearBtn = new JButton("クリア");
        clearBtn.addActionListener(e -> clear());
        clearBtn.setEnabled(false);

        Object[] options = new Object[]{addBtn,clearBtn};

        JOptionPane pane = new JOptionPane(view,
                                           JOptionPane.PLAIN_MESSAGE,
                                           JOptionPane.DEFAULT_OPTION,
                                           null,
                                           options, addBtn);
        dialog = pane.createDialog(inspector.getContext().getFrame(), ClientContext.getFrameTitle("身長体重登録"));

        // dialog が開いたら WeightFld にフォーカスを当てる
        dialog.addWindowListener(new WindowAdapter(){
            @Override
            public void windowOpened(WindowEvent e) {
                Focuser.requestFocus(view.getWeightFld());
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

        String height = view.getHeightFld().getText().trim();
        String weight = view.getWeightFld().getText().trim();
        String dateStr = view.getIdentifiedDateFld().getText().trim();

        boolean newOk = !(height.equals("") && weight.equals("")) && ! dateStr.equals("");

        if (ok != newOk) {
            ok = newOk;
            addBtn.setEnabled(ok);
            clearBtn.setEnabled(ok);
        }
    }

    private void add() {

        String h = view.getHeightFld().getText().trim();
        String w = view.getWeightFld().getText().trim();
        final PhysicalModel model = new PhysicalModel();

        if (!h.equals("")) { model.setHeight(h); }
        if (!w.equals("")) { model.setWeight(w); }

        // 同定日
        String confirmedStr = view.getIdentifiedDateFld().getText().trim();
        model.setIdentifiedDate(confirmedStr);

        addBtn.setEnabled(false);
        clearBtn.setEnabled(false);
        inspector.add(model);
    }

    private void clear() {
        view.getHeightFld().setText("");
        view.getWeightFld().setText("");
        view.getIdentifiedDateFld().setText("");
    }

    private class PopupListener extends MouseAdapter {

        private MyJPopupMenu popup;

        public PopupListener() {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                maybeShowPopup(e);
            }
        }

        //@Override
        //public void mouseReleased(MouseEvent e) {
        //    maybeShowPopup(e);
        //}

        private void maybeShowPopup(MouseEvent e) {

            popup = new MyJPopupMenu();
            CalendarCardPanel cc = new CalendarCardPanel(ClientContext.getEventColorTable());
            cc.addPropertyChangeListener(CalendarCardPanel.PICKED_DATE, evt -> {
                if (evt.getPropertyName().equals(CalendarCardPanel.PICKED_DATE)) {
                    SimpleDate sd = (SimpleDate) evt.getNewValue();
                    view.getIdentifiedDateFld().setText(SimpleDate.simpleDateToMmldate(sd));
                    popup.setVisible(false);
                    popup = null;
                }
            });
            cc.setCalendarRange(new int[]{-12, 0});
            popup.insert(cc, 0);
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
