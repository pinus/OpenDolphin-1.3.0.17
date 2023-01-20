package open.dolphin.setting;

import open.dolphin.client.Dolphin;
import open.dolphin.client.GUIConst;
import open.dolphin.client.GUIFactory;
import open.dolphin.helper.GridBagBuilder;
import open.dolphin.project.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
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

    // コンソールのログ出力
    private JCheckBox redirectBox;

    // ATOK 文字種切換キー登録
    private JComboBox<String> toHiraganaCombo;
    private JComboBox<String> toEijiCombo;

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

        // コンソールのログ出力
        redirectBox = new JCheckBox("<html>コンソールをファイルに出力する<br>" +
            "・user.home/Library/Application Support/OpenDolphin/console.log<br>" +
            "・user.home\\AppData\\Local\\OpenDolphin\\console.log</html>");

        gbb = new GridBagBuilder("コンソールのログ出力 [設定反映に再起動が必要]");
        gbb.add(redirectBox, 0, 0, 1, 1, GridBagConstraints.EAST);
        JPanel redirectPanel = gbb.getProduct();

        // ATOK の【ひらがな入力文字種(あ)】と【英字入力(A)】に割り当てたキーを設定
        String[] key = { "F13", "F14", "F15"};
        toEijiCombo = new JComboBox<>(key);
        toHiraganaCombo = new JComboBox<>(key);
        JLabel toEijiLabel = new JLabel("英字入力(A)", SwingConstants.RIGHT);
        JLabel toHiraganaLabel = new JLabel("ひらがな入力文字種(あ)", SwingConstants.RIGHT);

        gbb = new GridBagBuilder("ATOK の文字種切換キー [設定反映に再起動が必要]");
        gbb.add(toEijiLabel, 0, 0, 1, 1, GridBagConstraints.EAST);
        gbb.add(toEijiCombo, 1, 0, 1, 1, GridBagConstraints.WEST);
        gbb.add(toHiraganaLabel, 0, 1, 1, 1, GridBagConstraints.EAST);
        gbb.add(toHiraganaCombo, 1, 1, 1, 1, GridBagConstraints.WEST);
        JPanel atokPanel = gbb.getProduct();

        // 全体のレイアウト
        getUI().setLayout(new BoxLayout(getUI(), BoxLayout.Y_AXIS));
        getUI().add(scrollUnitPanel);
        getUI().add(redirectPanel);
        getUI().add(atokPanel);
        getUI().add(Box.createVerticalStrut(100));
        getUI().add(Box.createVerticalGlue());
    }

    private static int toKeyCode(String key) {
        return switch (key) {
            case "F13" -> KeyEvent.VK_F13;
            case "F14" -> KeyEvent.VK_F14;
            default -> KeyEvent.VK_F15;
        };
    }

    private static String toKeyCodeString(int key) {
        return switch (key) {
            case KeyEvent.VK_F13 -> "F13";
            case KeyEvent.VK_F14 -> "F14";
            default -> "F15";
        };
    }

    private void bindModelToView() {
        scrollUnitKarteSpinner.setValue(prefs.getInt(Project.SCROLL_UNIT_KARTE, 15));
        scrollUnitTableSpinner.setValue(prefs.getInt(Project.SCROLL_UNIT_TABLE, 15));
        scrollUnitStampSpinner.setValue(prefs.getInt(Project.SCROLL_UNIT_STAMP, 15));
        redirectBox.setSelected(Preferences.userNodeForPackage(Dolphin.class).getBoolean(Project.REDIRECT_CONSOLE, false));

        int toEijiCode = Preferences.userNodeForPackage(Dolphin.class).getInt(Project.ATOK_TO_EIJI_KEY, KeyEvent.VK_F15);
        int toHiraganaCode = Preferences.userNodeForPackage(Dolphin.class).getInt(Project.ATOK_TO_HIRAGANA_KEY, KeyEvent.VK_F14);
        toEijiCombo.getModel().setSelectedItem(toKeyCodeString(toEijiCode));
        toHiraganaCombo.getModel().setSelectedItem(toKeyCodeString(toHiraganaCode));
    }

    @Override
    public void save() {
        bindViewToModel();
    }

    private void bindViewToModel() {
        prefs.putInt(Project.SCROLL_UNIT_KARTE, (int) scrollUnitKarteSpinner.getValue());
        prefs.putInt(Project.SCROLL_UNIT_TABLE, (int) scrollUnitTableSpinner.getValue());
        prefs.putInt(Project.SCROLL_UNIT_STAMP, (int) scrollUnitStampSpinner.getValue());
        Preferences.userNodeForPackage(Dolphin.class).putBoolean(Project.REDIRECT_CONSOLE, redirectBox.isSelected());

        String toEijiString = (String) toEijiCombo.getSelectedItem();
        String toHiraganaString = (String) toHiraganaCombo.getSelectedItem();
        Preferences.userNodeForPackage(Dolphin.class).putInt(Project.ATOK_TO_EIJI_KEY, toKeyCode(toEijiString));
        Preferences.userNodeForPackage(Dolphin.class).putInt(Project.ATOK_TO_HIRAGANA_KEY, toKeyCode(toHiraganaString));
    }
}
