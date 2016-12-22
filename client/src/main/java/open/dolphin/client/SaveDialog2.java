package open.dolphin.client;

import ch.randelshofer.quaqua.SheetEvent;
import ch.randelshofer.quaqua.SheetListener;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import open.dolphin.helper.ProxyDocumentListener;
import open.dolphin.ui.MyJSheet;

/**
 * SaveDialog2
 * SaveDialog の JSheet version
 * @author  pns
 */
public class SaveDialog2 {

    private static final String[] PRINT_COUNT = { "0", "1",  "2",  "3",  "4", "5" };
    private static final String[] TITLE_LIST = {"経過記録", "処方", "処置", "検査", "画像", "指導"};
    private static final String TITLE = "ドキュメント保存";
    private static final String[] BUTTON_NAME = { "保 存", "仮保存", "破 棄", "キャンセル" };
    // result code
    public static final int SAVE = 0;
    public static final int TMP_SAVE = 1;
    public static final int DISPOSE = 2;
    public static final int CANCEL = 3;

    private Window parent;
    private JOptionPane pane;
    private MyJSheet dialog;

    private JTextField titleField;
    private JComboBox titleCombo;
    private JComboBox printCombo;
    private JLabel departmentLabel;
    private JCheckBox sendClaim;
    private JButton okButton;
    private JButton tmpButton;
    private JButton disposeButton;
    private JButton cancelButton;

    // 戻り値のSaveParams/
    private SaveParams value;

    /**
     * カルテ保存時のダイアログ JSheet 対応 by pns
     * @param parent
     */
    public SaveDialog2(Window parent) {
        this.parent = parent;
        initComponent();
    }

    public void start () {
        dialog.addSheetListener(new SheetListener(){
            @Override
            public void optionSelected(SheetEvent se) {
                // 戻り値のSaveparamsを生成する
                value = new SaveParams();

                int result = se.getOption();
                if (result == SAVE) doOk();
                else if(result == TMP_SAVE) doTemp();
                else if(result == DISPOSE) doDispose();
                else if(result == CANCEL) doCancel();
            }
        });
        dialog.show();
    }

    public SaveParams getValue() {
        return value;
    }

    public void setValue(SaveParams params) {

        // Titleを表示する
        String val = params.getTitle();
        if (val != null && (!val.equals("") &&(!val.equals("経過記録")))) {
            titleCombo.insertItemAt(val, 0);
        }
        titleCombo.setSelectedIndex(0);

        //
        // 診療科を表示する
        // 受付情報からの診療科を設定する
        val = params.getDepartment();
        if (val != null) {
            String[] depts = val.split("\\s*,\\s*");
            if (depts[0] != null) {
                departmentLabel.setText(depts[0]);
            } else {
                departmentLabel.setText(val);
            }
        }

        // 印刷部数選択
        int count = params.getPrintCount();
        if (count != -1) {
            printCombo.setSelectedItem(String.valueOf(count));

        } else {
            // いつのまにか preferences: open.dolphin.client.plist
            // karte.print.count が -1 になっていてどうにも直せなくなったことがあった
            // printCombo.setEnabled(false);
            printCombo.setSelectedItem(0);
        }

        //
        // CLAIM 送信をチェックする
        //
        if (params.isDisableSendClaim()) {
            // シングルカルテで CLAIM 送信自体を行わない場合
            sendClaim.setEnabled(false);
        } else {
            sendClaim.setSelected(params.isSendClaim());
        }

        checkTitle();
    }


    /**
     * GUIコンポーネントを初期化する.
     */
    private void initComponent() {

        // content
        JPanel content = new JPanel();
        content.setLayout(new GridLayout(0, 1));

        // 文書Title
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleCombo = new JComboBox(TITLE_LIST);
        titleCombo.setPreferredSize(new Dimension(220, titleCombo.getPreferredSize().height));
        titleCombo.setMaximumSize(titleCombo.getPreferredSize());
        titleCombo.setEditable(true);
        p.add(new JLabel("タイトル:"));
        p.add(titleCombo);
        content.add(p);

        // ComboBox のエディタコンポーネントへリスナを設定する
        titleField = (JTextField) titleCombo.getEditor().getEditorComponent();
        titleField.getDocument().addDocumentListener((ProxyDocumentListener) e -> checkTitle());

        // 診療科，印刷部数を表示するラベルとパネルを生成する
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        departmentLabel = new JLabel();
        p1.add(new JLabel("診療科:"));
        p1.add(departmentLabel);
        p1.add(Box.createRigidArea(new Dimension(11, 0)));

        // Print
        printCombo = new JComboBox(PRINT_COUNT);
        printCombo.setSelectedIndex(1);
        p1.add(new JLabel("印刷部数:"));
        p1.add(printCombo);
        content.add(p1);

        // CLAIM 送信ありなし
        sendClaim = new JCheckBox("診療行為を送信する (仮保存の場合は送信しない)");
        // quaqua-7.4.2 にしたら，なぜか診察室の iMac １台だけ isFocusable = false になる. 何で？
        sendClaim.setFocusable(true);

        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p5.add(sendClaim);
        content.add(p5);

        // JOptionPane
        pane = new JOptionPane(content, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, BUTTON_NAME, BUTTON_NAME[0]);
        pane.putClientProperty("Quaqua.OptionPane.destructiveOption", 3);

        // JOptionPane から component を再帰検索して okButton と tmpButton を取り出す
        Component[] components = pane.getComponents();
        List<Component> cc = java.util.Arrays.asList(components);

        while (!cc.isEmpty()) {
            List<Component> stack = new ArrayList<Component>();
            for (Component c: cc) {
                if (c instanceof JButton) {
                    JButton button = (JButton) c;
                    String name = button.getText();
                    if (BUTTON_NAME[SAVE].equals(name)) okButton = button;
                    else if (BUTTON_NAME[TMP_SAVE].equals(name)) tmpButton = button;
                    else if (BUTTON_NAME[DISPOSE].equals(name)) disposeButton = button;
                    else if (BUTTON_NAME[CANCEL].equals(name)) cancelButton = button;

                } else if (c instanceof JComponent) {
                    components = ((JComponent)c).getComponents();
                    stack.addAll(java.util.Arrays.asList(components));
                }
            }
            cc = stack;
        }
        okButton.setEnabled(false);
        okButton.setToolTipText("診療行為の送信はチェックボックスに従います。");
        tmpButton.setEnabled(false);
        tmpButton.setToolTipText("診療行為は送信しません。");

        dialog = MyJSheet.createDialog(pane, parent);
        dialog.setFocusTraversalPolicy(new LayoutFocusTraversalPolicy(){
            private static final long serialVersionUID = 1L;
            @Override
            public Component getInitialComponent(Window w) {
                // System.out.println("Is sendClaim checkbox focusable? " + sendClaim.isFocusable());
                return sendClaim;
            }
        });

        // ショートカット登録
        ActionMap am = dialog.getRootPane().getActionMap();
        InputMap im = dialog.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // SPACE で CLAIM 送信のチェックボックスの ON/OFF をする
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "toggle-claim");
        am.put("toggle-claim", new AbstractAction() {
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(ActionEvent e) {
                sendClaim.doClick();
            }
        });

        // Cmd-T で一時保存
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.META_DOWN_MASK), "tmpSave");
        am.put("tmpSave", new AbstractAction() {
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(ActionEvent e) {
                tmpButton.doClick();
            }
        });

        // ESC でキャンセル
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
        am.put("cancel", new AbstractAction() {
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButton.doClick();
            }
        });

        // Cmd-ESC で破棄
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, InputEvent.META_DOWN_MASK), "dispose");
        am.put("dispose", new AbstractAction() {
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeButton.doClick();
            }
        });
    }

    /**
     * タイトルフィールドの有効性をチェックする.
     */
    public void checkTitle() {
        boolean enabled = titleField.getText().trim().equals("") ? false : true;
        okButton.setEnabled(enabled);
        tmpButton.setEnabled(enabled);
    }

    /**
     * GUIコンポーネントから値を取得し，saveparamsに設定する.
     */
    private void doOk() {

        // 文書タイトルを取得する
        String val = (String) titleCombo.getSelectedItem();
        if (! val.equals("")) {
            value.setTitle(val);
        } else {
            value.setTitle("経過記録");
        }

        // Department
        val = departmentLabel.getText();
        value.setDepartment(val);

        // 印刷部数を取得する
        int count = Integer.parseInt((String)printCombo.getSelectedItem());
        value.setPrintCount(count);

        //
        // CLAIM 送信
        //
        value.setSendClaim(sendClaim.isSelected());

        // 患者への参照許可を取得する
        value.setAllowPatientRef(false);
        // 診療歴のある施設への参照許可を設定する
        value.setAllowClinicRef(false);

        value.setSelection(SAVE);
        close();
    }

    /**
     * 仮保存の場合のパラメータを設定する.
     */
    private void doTemp() {
        //
        // 仮保存であることを設定する
        //
        value.setTmpSave(true);

        // 文書タイトルを取得する
        String val = (String) titleCombo.getSelectedItem();
        if (! val.equals("")) {
            value.setTitle(val);
        }

        // Department
        val = departmentLabel.getText();
        value.setDepartment(val);

        //
        // 印刷部数を取得する
        // 仮保存でも印刷するかも知れない
        //
        int count = Integer.parseInt((String)printCombo.getSelectedItem());
        value.setPrintCount(count);

        //
        // CLAIM 送信
        //
        value.setSendClaim(false);

        // 患者への参照許可を取得する
        boolean b = false;
        value.setAllowPatientRef(b);

        // 診療歴のある施設への参照許可を設定する
        b = false;
        value.setAllowClinicRef(b);

        // 患者への参照許可を取得する
        value.setAllowPatientRef(false);
        // 診療歴のある施設への参照許可を設定する
        value.setAllowClinicRef(false);

        value.setSelection(TMP_SAVE);
        close();
    }

    private void doDispose() {
        value.setSelection(DISPOSE);
        close();
    }

    private void doCancel() {
        value.setSelection(CANCEL);
        close();
    }

    private void close() {
        dialog.setVisible(false);
        dialog.dispose();
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        try {
            UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
        } catch (Exception e) { System.out.println("Dolphin.java: " + e);}

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(500, 200);
        f.setVisible(true);

        SaveDialog2 sd = new SaveDialog2(f);
        SaveParams param = new SaveParams();
        sd.setValue(param);
        sd.start();
        param = sd.getValue();
        System.out.println("----selection = " + param.getSelection());
    }
}
