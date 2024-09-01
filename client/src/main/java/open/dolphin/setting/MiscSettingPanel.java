package open.dolphin.setting;

import open.dolphin.client.Dolphin;
import open.dolphin.client.GUIConst;
import open.dolphin.client.GUIFactory;
import open.dolphin.helper.GridBagBuilder;
import open.dolphin.helper.WindowSupport;
import open.dolphin.project.Project;

import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

/**
 * Miscellaneous Setting Panel.
 *
 * @author pns
 */
public class MiscSettingPanel extends AbstractSettingPanel {
    private static final String ID = "miscSetting";
    private static final String TITLE = "詳細";
    private static final ImageIcon ICON = GUIConst.ICON_EMBLEM_SYSTEM_32;

    private final Preferences prefs = Project.getPreferences();

    // スクロール速度
    private JSpinner scrollUnitKarteSpinner;
    private JSpinner scrollUnitTableSpinner;
    private JSpinner scrollUnitStampSpinner;

    // ウインドウ整列 in WindowSupport のパラメータ
    private JTextField initX, initY, diffX, diffY;

    // コンソールのログ出力
    private JCheckBox redirectBox;

    // im-select の inputSourceID 設定
    private JTextField japaneseId;
    private JTextField romanId;

    public MiscSettingPanel() {
        init();
    }

    private void init() {
        this.setId(ID);
        this.setTitle(TITLE);
        this.setIcon(ICON);
    }

    @Override
    public void start() {

        initComponents();
        bindModelToView();

    }

    /**
     * init components
     */
    private void initComponents() {
        // スクロール速度設定
        JPanel scrollUnitPanel = new JPanel();
        scrollUnitPanel.setLayout(new BoxLayout(scrollUnitPanel, BoxLayout.Y_AXIS));
        int row = 0;
        GridBagBuilder gbb = new GridBagBuilder("スクロール速度");

        // カルテスクロール速度
        JLabel label = new JLabel("カルテ :", SwingConstants.RIGHT);
        JPanel slider = GUIFactory.createSliderPanel(1, 32, 15);
        scrollUnitKarteSpinner = (JSpinner) slider.getComponent(1);
        gbb.add(label, 0, row, 1, 1, GridBagConstraints.EAST);
        gbb.add(slider, 1, row, 1, 1, GridBagConstraints.WEST);
        row++;

        // テーブルのスクロール速度
        label = new JLabel("テーブル :", SwingConstants.RIGHT);
        slider = GUIFactory.createSliderPanel(1, 32, 15);
        scrollUnitTableSpinner = (JSpinner) slider.getComponent(1);
        gbb.add(label, 0, row, 1, 1, GridBagConstraints.EAST);
        gbb.add(slider, 1, row, 1, 1, GridBagConstraints.WEST);
        row++;

        // スタンプのスクロール速度
        label = new JLabel("スタンプ :", SwingConstants.RIGHT);
        slider = GUIFactory.createSliderPanel(1, 32, 15);
        scrollUnitStampSpinner = (JSpinner) slider.getComponent(1);
        gbb.add(label, 0, row, 1, 1, GridBagConstraints.EAST);
        gbb.add(slider, 1, row, 1, 1, GridBagConstraints.WEST);

        scrollUnitPanel.add(gbb.getProduct());
        //scrollUnitPanel.add(Box.createVerticalStrut(500));
        //scrollUnitPanel.add(Box.createVerticalGlue());

        // ウインドウ整列 in WindwoSupport のパラメータ
        gbb = new GridBagBuilder("ウインドウ整列のパラメータ");
        initX = new JTextField(String.valueOf(WindowSupport.INITIAL_X), 3);
        initY = new JTextField(String.valueOf(WindowSupport.INITIAL_Y), 3);
        diffX = new JTextField(String.valueOf(WindowSupport.INITIAL_DX), 3);
        diffY = new JTextField(String.valueOf(WindowSupport.INITIAL_DY), 3);
        label = new JLabel("開始X:", SwingConstants.RIGHT);
        gbb.add(label, 0, 0, 1, 1, GridBagConstraints.EAST);
        gbb.add(initX, 1, 0, 1, 1, GridBagConstraints.WEST);
        label = new JLabel("開始Y:", SwingConstants.RIGHT);
        gbb.add(label, 2, 0, 1, 1, GridBagConstraints.EAST);
        gbb.add(initY, 3, 0, 1, 1, GridBagConstraints.WEST);
        label = new JLabel("X差分:", SwingConstants.RIGHT);
        gbb.add(label, 4, 0, 1, 1, GridBagConstraints.EAST);
        gbb.add(diffX, 5, 0, 1, 1, GridBagConstraints.WEST);
        label = new JLabel("Y差分:", SwingConstants.RIGHT);
        gbb.add(label, 6, 0, 1, 1, GridBagConstraints.EAST);
        gbb.add(diffY, 7, 0, 1, 1, GridBagConstraints.WEST);

        scrollUnitPanel.add(gbb.getProduct());

        // コンソールのログ出力
        redirectBox = new JCheckBox("<html>コンソールをファイルに出力する<br>" +
            "・user.home/Library/Application Support/OpenDolphin/console.log<br>" +
            "・user.home\\AppData\\Local\\OpenDolphin\\console.log</html>");

        gbb = new GridBagBuilder("コンソールのログ出力 [設定反映に再起動が必要]");
        gbb.add(redirectBox, 0, 0, 1, 1, GridBagConstraints.EAST);
        JPanel redirectPanel = gbb.getProduct();

        // im-select の inputSourceID の設定
        gbb = new GridBagBuilder("inputSourceID for im-select");
        JLabel japaneseLbl = new JLabel("ひらがな");
        JLabel romanLbl = new JLabel("英字");
        japaneseId = new JTextField(40);
        romanId = new JTextField(40);
        gbb.add(japaneseLbl, 0,0,1,1, GridBagConstraints.WEST);
        gbb.add(japaneseId,0,1,1,1, GridBagConstraints.WEST);
        gbb.add(romanLbl, 0,2,1,1, GridBagConstraints.WEST);
        gbb.add(romanId,0,3,1,1, GridBagConstraints.WEST);
        JPanel atokPanel = gbb.getProduct();

        // 全体のレイアウト
        getUI().setLayout(new BoxLayout(getUI(), BoxLayout.Y_AXIS));
        getUI().add(scrollUnitPanel);
        getUI().add(redirectPanel);
        getUI().add(atokPanel);
        getUI().add(Box.createVerticalStrut(100));
        getUI().add(Box.createVerticalGlue());
    }

    private void bindModelToView() {
        scrollUnitKarteSpinner.setValue(prefs.getInt(Project.SCROLL_UNIT_KARTE, 15));
        scrollUnitTableSpinner.setValue(prefs.getInt(Project.SCROLL_UNIT_TABLE, 15));
        scrollUnitStampSpinner.setValue(prefs.getInt(Project.SCROLL_UNIT_STAMP, 15));

        initX.setText(String.valueOf(prefs.getInt(Project.ARRANGE_INSPECTOR_X, WindowSupport.INITIAL_X)));
        initY.setText(String.valueOf(prefs.getInt(Project.ARRANGE_INSPECTOR_Y, WindowSupport.INITIAL_Y)));
        diffX.setText(String.valueOf(prefs.getInt(Project.ARRANGE_INSPECTOR_DX, WindowSupport.INITIAL_DX)));
        diffY.setText(String.valueOf(prefs.getInt(Project.ARRANGE_INSPECTOR_DY, WindowSupport.INITIAL_DY)));

        redirectBox.setSelected(Preferences.userNodeForPackage(Dolphin.class).getBoolean(Project.REDIRECT_CONSOLE, false));

        String japanese = Preferences.userNodeForPackage(Dolphin.class).get(Project.ATOK_JAPANESE_KEY, "com.justsystems.inputmethod.atok34.Japanese");
        String roman = Preferences.userNodeForPackage(Dolphin.class).get(Project.ATOK_ROMAN_KEY, "com.justsystems.inputmethod.atok34.Roman");
        japaneseId.setText(japanese);
        romanId.setText(roman);
    }

    @Override
    public void save() {
        bindViewToModel();
    }

    private void bindViewToModel() {
        prefs.putInt(Project.SCROLL_UNIT_KARTE, (int) scrollUnitKarteSpinner.getValue());
        prefs.putInt(Project.SCROLL_UNIT_TABLE, (int) scrollUnitTableSpinner.getValue());
        prefs.putInt(Project.SCROLL_UNIT_STAMP, (int) scrollUnitStampSpinner.getValue());

        prefs.putInt(Project.ARRANGE_INSPECTOR_X, Integer.parseInt(initX.getText()));
        prefs.putInt(Project.ARRANGE_INSPECTOR_Y, Integer.parseInt(initY.getText()));
        prefs.putInt(Project.ARRANGE_INSPECTOR_DX, Integer.parseInt(diffX.getText()));
        prefs.putInt(Project.ARRANGE_INSPECTOR_DY, Integer.parseInt(diffY.getText()));

        Preferences.userNodeForPackage(Dolphin.class).putBoolean(Project.REDIRECT_CONSOLE, redirectBox.isSelected());
        Preferences.userNodeForPackage(Dolphin.class).put(Project.ATOK_JAPANESE_KEY, japaneseId.getText());
        Preferences.userNodeForPackage(Dolphin.class).put(Project.ATOK_ROMAN_KEY, romanId.getText());
    }
}
