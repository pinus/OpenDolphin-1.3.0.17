package open.dolphin.client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.EventHandler;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import open.dolphin.helper.ProxyDocumentListener;
import open.dolphin.ui.IMEControl;

/**
 * SaveDialog
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class SaveDialog {

    private static final String[] PRINT_COUNT = {
        "0", "1",  "2",  "3",  "4", "5"
    };

    private static final String[] TITLE_LIST = {"経過記録", "処方", "処置", "検査", "画像", "指導"};

    private static final String TITLE = "ドキュメント保存";
    private static final String SAVE = "保存";
    private static final String TMP_SAVE = "仮保存";

    private JCheckBox patientCheck;
    private JCheckBox clinicCheck;

    // 保存ボタン
    private JButton okButton;

    // キャンセルボタン
    private JButton cancelButton;

    // 仮保存ボタン
    private JButton tmpButton;

    private JTextField titleField;
    private JComboBox titleCombo;
    //private JLabel sendMmlLabel;
    private JComboBox printCombo;
    private JLabel departmentLabel;
    //private Frame parent;

    // CLAIM 送信
    private JCheckBox sendClaim;

    // 戻り値のSaveParams/
    private SaveParams value;

    // ダイアログ
    private JDialog dialog;
    private Window parent;
//pns ダイアログ → JSheet で置き換える
//  Quaqua 6.5 から，JSheet に focus がとれなくなった
//  後ろの textPane にカーソルがうつってしまうのだが，focus 自体は JSheet にあることになっている。
//
//    private MyJSheet dialog;

    /**
     * Creates new OpenKarteDialog
     */
    public SaveDialog(Window parent) {
        this.parent = parent;

        JPanel contentPanel = createComponent();

        Object[] options = new Object[]{okButton, tmpButton, cancelButton};

        JOptionPane jop = new JOptionPane(
                contentPanel,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                options,
                okButton);

//pns^  すでに JSheet が出ている場合は，toFront してリターン
//        if (MyJSheet.isAlreadyShown(parent)) {
//            parent.toFront();
//            return;
//        }
//pns$
        dialog = jop.createDialog(parent, ClientContext.getFrameTitle(TITLE));
//        dialog = MyJSheet.createDialog(jop, parent);

//pns^  ショートカット登録
        InputMap im = okButton.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap am = dialog.getRootPane().getActionMap();

        // SPACE で CLAIM 送信のチェックボックスの ON/OFF をする
        // もともと，SPACE には okButton 'pressed' が割り当てられているので，これを削除する
        KeyStroke spaceKey = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);
        while (im != null ){
            im.remove(spaceKey);
            im = im.getParent();
        }
        im = dialog.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        im.put(spaceKey, "toggle-claim");
        am.put("toggle-claim", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                sendClaim.doClick();
            }
        });

        // ESC でキャンセル
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
        am.put("escape", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                cancelButton.doClick();
            }
        });

//pns$
    }

    public void start() {
        // JSheet と位置あわせ
        Rectangle r = new Rectangle(dialog.getBounds());
        r.y = parent.getBounds().y;
        dialog.setBounds(r);

        dialog.setVisible(true);
    }

    public SaveParams getValue() {
        return value;
    }

    /**
     * コンポーネントにSaveParamsの値を設定する。
     */
    public void setValue(SaveParams params) {

        // Titleを表示する
//pns   String val = params.getTitle();
//pns   if (val != null && (!val.equals("") &&(!val.equals("経過記録")))) {
//pns       titleCombo.insertItemAt(val, 0);
//pns   }
        titleCombo.setSelectedIndex(0);

        //
        // 診療科を表示する
        // 受付情報からの診療科を設定する
        String val = params.getDepartment();
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
            printCombo.setEnabled(false);
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


        // アクセス権を設定する
        if (params.getSendMML()) {
            // 患者への参照と診療歴のある施設の参照許可を設定する
            boolean permit = params.isAllowPatientRef();
            patientCheck.setSelected(permit);
            permit = params.isAllowClinicRef();
            clinicCheck.setSelected(permit);

        } else {
            // MML 送信をしないときdiasbleにする
            patientCheck.setEnabled(false);
            clinicCheck.setEnabled(false);
        }

        checkTitle();
    }


    /**
     * GUIコンポーネントを初期化する。
     */
    private JPanel createComponent() {

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

        //
        // ComboBox のエディタコンポーネントへリスナを設定する
        //
        titleField = (JTextField) titleCombo.getEditor().getEditorComponent();
//pns   titleField.addFocusListener(AutoKanjiListener.getInstance());
        IMEControl.setImeOnIfFocused(titleField);
        titleField.getDocument().addDocumentListener(ProxyDocumentListener.create(this, "checkTitle"));

        // 診療科、印刷部数を表示するラベルとパネルを生成する
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


        // AccessRightを設定するボタンとパネルを生成する
        patientCheck = new JCheckBox("患者に参照を許可する");
        clinicCheck = new JCheckBox("診療歴のある病院に参照を許可する");

        //
        // CLAIM 送信ありなし
        //
        sendClaim = new JCheckBox("診療行為を送信する (仮保存の場合は送信しない)");
        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p5.add(sendClaim);
        content.add(p5);

        // OK button
        okButton = new JButton(SAVE);
        okButton.setToolTipText("診療行為の送信はチェックボックスに従います。");
        okButton.addActionListener((ActionListener) EventHandler.create(ActionListener.class, this, "doOk"));
        okButton.setEnabled(false);

        // Cancel Button
        String buttonText =  (String)UIManager.get("OptionPane.cancelButtonText");
        cancelButton = new JButton(buttonText);
        cancelButton.addActionListener((ActionListener) EventHandler.create(ActionListener.class, this, "doCancel"));

        // 仮保存 button
        tmpButton = new JButton(TMP_SAVE);
        tmpButton.setToolTipText("診療行為は送信しません。");
        tmpButton.addActionListener((ActionListener) EventHandler.create(ActionListener.class, this, "doTemp"));
        tmpButton.setEnabled(false);
        return content;
    }

    /**
     * タイトルフィールドの有効性をチェックする。
     */
    public void checkTitle() {
        boolean enabled = titleField.getText().trim().equals("") ? false : true;
        okButton.setEnabled(enabled);
        tmpButton.setEnabled(enabled);
    }


    /**
     * GUIコンポーネントから値を取得し、saveparamsに設定する。
     */
    public void doOk() {

        // 戻り値のSaveparamsを生成する
        value = new SaveParams();

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
        boolean b = patientCheck.isSelected();
        value.setAllowPatientRef(b);

        // 診療歴のある施設への参照許可を設定する
        b = clinicCheck.isSelected();
        value.setAllowClinicRef(b);

        close();
    }


    /**
     * 仮保存の場合のパラメータを設定する。
     */
    public void doTemp() {

        // 戻り値のSaveparamsを生成する
        value = new SaveParams();

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

        close();
    }

    /**
     * キャンセルしたことを設定する。
     */
    public void doCancel() {
        value = null;
        close();
    }

    private void close() {
        dialog.setVisible(false);
        dialog.dispose();
    }
}
