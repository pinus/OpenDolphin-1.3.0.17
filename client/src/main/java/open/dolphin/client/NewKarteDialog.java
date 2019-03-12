package open.dolphin.client;

import open.dolphin.infomodel.PVTHealthInsuranceModel;
import open.dolphin.project.Project;
import open.dolphin.ui.Focuser;
import open.dolphin.ui.PNSBorderFactory;
import open.dolphin.ui.sheet.JSheet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

/**
 * 新規カルテ作成のダイアログ.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 * @author pns
 */
public final class NewKarteDialog {

    private static final String OPEN_ANOTHER        = "別ウィンドウで編集";
    private static final String ADD_TO_TAB          = "タブパネルへ追加";
    private static final String EMPTY_NEW           = "空白の新規カルテ";
    private static final String APPLY_RP            = "前回処方を適用";
    private static final String ALL_COPY            = "全てコピー";
    private static final String DEPARTMENT          =  "診療科:";
    private static final String SELECT_INS          =  "保険選択";
    private static final String LAST_CREATE_MODE    = "newKarteDialog.lastCreateMode";
    private static final String FRAME_MEMORY        = "newKarteDialog.openFrame";

    private NewKarteParams params;

    // GUI components
    private JButton okButton;
    private JButton cancelButton;
    private JRadioButton emptyNew;
    private JRadioButton applyRp;       // 前回処方を適用
    private JRadioButton allCopy;	// 全てコピー
    private JList<PVTHealthInsuranceModel> insuranceList;
    private JLabel departmentLabel;
    private JRadioButton addToTab;	// タブパネルへ追加
    private JRadioButton openAnother;	// 別 Window へ表示

    private final Preferences prefs;

    private final Frame parentFrame;
    private final String title;
    private final JPanel content;
    private JSheet dialog;
    private Object value;

    /**
     * Creates new NewKarteDialog.
     * @param parentFrame
     * @param title
     */
    public NewKarteDialog(Frame parentFrame, String title) {
        prefs = Preferences.userNodeForPackage(this.getClass());
        this.parentFrame = parentFrame;
        this.title = title;
        content = createComponent();
    }

    private JPanel createComponent() {

        // 診療科情報ラベル
        departmentLabel = new JLabel();
        JPanel dp = new JPanel(new FlowLayout(FlowLayout.CENTER, 11, 0));
        dp.add(new JLabel(DEPARTMENT));
        dp.add(departmentLabel);

        // 保険選択リスト
        insuranceList = new JList<>();
        insuranceList.setFixedCellWidth(200);
        insuranceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        insuranceList.addListSelectionListener(e -> insuranceSelectionChanged(e.getValueIsAdjusting()));

        JPanel ip = new JPanel(new BorderLayout(9, 0));
        ip.setBorder(PNSBorderFactory.createTitledBorder(SELECT_INS));
        ip.add(insuranceList, BorderLayout.CENTER);
        ip.add(new JLabel(GUIConst.ICON_INSURANCE_CARD_32), BorderLayout.WEST);

        // 前回処方適用 / 全コピー / 空白
        emptyNew = new JRadioButton(EMPTY_NEW);
        applyRp = new JRadioButton(APPLY_RP);
        allCopy = new JRadioButton(ALL_COPY);
        ActionListener memory = e -> memoryMode();
        emptyNew.addActionListener(memory);
        applyRp.addActionListener(memory);
        allCopy.addActionListener(memory);
        JPanel rpPanel = new JPanel();
        rpPanel.setLayout(new BoxLayout(rpPanel, BoxLayout.X_AXIS));
        rpPanel.add(applyRp);
        rpPanel.add(Box.createRigidArea(new Dimension(5,0)));
        rpPanel.add(allCopy);
        rpPanel.add(Box.createRigidArea(new Dimension(5,0)));
        rpPanel.add(emptyNew);
        rpPanel.add(Box.createHorizontalGlue());

        // タブパネルへ追加/別ウィンドウ
        openAnother = new JRadioButton(OPEN_ANOTHER);
        addToTab = new JRadioButton(ADD_TO_TAB);
        openAnother.addActionListener(e -> memoryFrame());
        addToTab.addActionListener(e -> memoryFrame());
        JPanel openPanel = new JPanel();
        openPanel.setLayout(new BoxLayout(openPanel, BoxLayout.X_AXIS));
        openPanel.add(openAnother);
        openPanel.add(Box.createRigidArea(new Dimension(5,0)));
        openPanel.add(addToTab);
        openPanel.add(Box.createHorizontalGlue());

        // ok
        String buttonText =  (String) UIManager.get("OptionPane.okButtonText");
        okButton = new JButton(buttonText);
        okButton.addActionListener(e -> doOk());
        okButton.setEnabled(false);

        // Cancel Button
        buttonText =  (String)UIManager.get("OptionPane.cancelButtonText");
        cancelButton = new JButton(buttonText);
        cancelButton.addActionListener(e -> doCancel());

        // 全体を配置
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(dp);
        panel.add(Box.createVerticalStrut(11));
        panel.add(ip);
        panel.add(Box.createVerticalStrut(11));
        panel.add(GUIFactory.createTitledPanel(rpPanel, "カルテ作成方法"));
        panel.add(Box.createVerticalStrut(11));
        panel.add(GUIFactory.createTitledPanel(openPanel, "カルテ編集ウインドウ"));
        panel.add(Box.createVerticalStrut(11));

        return panel;
    }

    public void setValue(Object o) {

        this.params = (NewKarteParams) o;
        setDepartment(params.getDepartment());
        setInsurance(params.getInsurances());

        int lastCreateMode = prefs.getInt(LAST_CREATE_MODE, 0);
        boolean frameMemory = prefs.getBoolean(FRAME_MEMORY, true);

        switch (params.getOption()) {

            case BROWSER_NEW:
                applyRp.setEnabled(false);
                allCopy.setEnabled(false);
                emptyNew.setSelected(true);
                openAnother.setSelected(frameMemory);
                addToTab.setSelected(!frameMemory);
                ButtonGroup bg = new ButtonGroup();
                bg.add(openAnother);
                bg.add(addToTab);
                break;

            case BROWSER_COPY_NEW:
                selectCreateMode(lastCreateMode);
                bg = new ButtonGroup();
                bg.add(emptyNew);
                bg.add(applyRp);
                bg.add(allCopy);

                openAnother.setSelected(frameMemory);
                addToTab.setSelected(!frameMemory);
                bg = new ButtonGroup();
                bg.add(openAnother);
                bg.add(addToTab);
                break;

            case BROWSER_MODIFY:
                insuranceList.setEnabled(false);
                applyRp.setEnabled(false);
                allCopy.setEnabled(false);
                emptyNew.setEnabled(false);
                openAnother.setSelected(frameMemory);
                addToTab.setSelected(!frameMemory);
                bg = new ButtonGroup();
                bg.add(openAnother);
                bg.add(addToTab);
                // OK Button
                okButton.setEnabled(true);
                break;

            case EDITOR_NEW:
                applyRp.setEnabled(false);
                allCopy.setEnabled(false);
                emptyNew.setSelected(true);
                openAnother.setSelected(true);
                openAnother.setEnabled(false);
                addToTab.setEnabled(false);
                break;

            case EDITOR_COPY_NEW:
                selectCreateMode(lastCreateMode);
                bg = new ButtonGroup();
                bg.add(applyRp);
                bg.add(allCopy);
                bg.add(emptyNew);
                openAnother.setSelected(true);
                openAnother.setEnabled(false);
                addToTab.setEnabled(false);
                break;

            case EDITOR_MODIFY:
                insuranceList.setEnabled(false);
                applyRp.setEnabled(false);
                allCopy.setEnabled(false);
                emptyNew.setEnabled(false);
                openAnother.setSelected(true);
                openAnother.setEnabled(false);
                addToTab.setEnabled(false);
                break;
        }
    }

    public void start() {

        Object[] options = new Object[]{okButton, cancelButton};

        JOptionPane jop = new JOptionPane(
                content,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                options,
                okButton);

        // すでに JSheet が出ている場合は，toFront してリターン
        if (JSheet.isAlreadyShown(parentFrame)) {
            parentFrame.toFront();
            return;
        }

        dialog = JSheet.createDialog(jop, parentFrame);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                Focuser.requestFocus(insuranceList);
            }
        });
        dialog.addSheetListener(se -> {
            // Escape キーを押したときだけここに入る
            if (se.getOption() == JOptionPane.CLOSED_OPTION) { cancelButton.doClick(); }
        });
        dialog.setVisible(true);
    }

    public Object getValue() {
        return value;
    }

    private void setDepartment(String dept) {
        if (dept != null) {
            String[] depts = dept.split("\\s*,\\s*");
            departmentLabel.setText(depts[0]);
        }
    }

    private void setInsurance(PVTHealthInsuranceModel[] o) {

        insuranceList.setListData(o);

        //
        // 保険が一つしかない場合はそれを選択する
        //
        if (o != null && o.length > 0) {
            int index = params.getInitialSelectedInsurance();
            if (index >=0 && index < o.length) {
                insuranceList.getSelectionModel().setSelectionInterval(index,index);
            }
        }
    }

    private Chart.NewKarteMode getCreateMode() {
        if (emptyNew.isSelected()) {
            return Chart.NewKarteMode.EMPTY_NEW;
        } else if (applyRp.isSelected()) {
            return Chart.NewKarteMode.APPLY_RP;
        } else if (allCopy.isSelected()) {
            return Chart.NewKarteMode.ALL_COPY;
        }
        return Chart.NewKarteMode.EMPTY_NEW;
    }

    private void selectCreateMode(int mode) {
        emptyNew.setSelected(false);
        applyRp.setSelected(false);
        allCopy.setSelected(false);
        switch (mode) {
            case 0:
                emptyNew.setSelected(true);
                break;
            case 1:
                applyRp.setSelected(true);
                break;
            case 2:
                allCopy.setSelected(true);
                break;
            default:
                break;
        }
    }

    /**
     * 保険選択に応じてボタン処理をする.
     * @param adjusting
     */
    public void insuranceSelectionChanged(boolean adjusting) {
        if (adjusting == false) {
            Object o = insuranceList.getSelectedValue();
            okButton.setEnabled(o != null);
        }
    }

    /**
     * カルテの作成方法をプレファレンスに記録する.
     */
    public void memoryMode() {

        if (emptyNew.isSelected()) {
            prefs.putInt(LAST_CREATE_MODE, 0);
        } else if (applyRp.isSelected()) {
            prefs.putInt(LAST_CREATE_MODE, 1);
        } else if (allCopy.isSelected()) {
            prefs.putInt(LAST_CREATE_MODE, 2);
        }
    }

    /**
     * カルテフレーム(ウインドウ)の作成方法をプレファレンスに記録する.
     */
    public void memoryFrame() {
        boolean openFrame = openAnother.isSelected();
        prefs.putBoolean(FRAME_MEMORY,openFrame);
        Preferences gpref = Project.getPreferences();
        gpref.putBoolean(Project.KARTE_PLACE_MODE, openFrame);
    }

    /**
     * パラーメータを取得しダイアログの値に設定する.
     */
    public void doOk() {
        params.setDepartment(departmentLabel.getText());
        params.setPVTHealthInsurance(insuranceList.getSelectedValue());
        params.setCreateMode(getCreateMode());
        params.setOpenFrame(openAnother.isSelected());
        value = params;
        dialog.setVisible(false);
        dialog.dispose();
    }

    /**
     * キャンセルする. ダイアログを閉じる.
     */
    public void doCancel() {
        value = null;
        dialog.setVisible(false);
        dialog.dispose();
    }
}
