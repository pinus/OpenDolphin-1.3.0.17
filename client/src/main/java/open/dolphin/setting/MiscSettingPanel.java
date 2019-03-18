package open.dolphin.setting;

import open.dolphin.client.Dolphin;
import open.dolphin.client.GUIConst;
import open.dolphin.client.GUIFactory;
import open.dolphin.helper.GridBagBuilder;
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

    // コンソールのログ出力
    private JCheckBox redirectBox;

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
        row++;

        scrollUnitPanel.add(gbb.getProduct());
        //scrollUnitPanel.add(Box.createVerticalStrut(500));
        //scrollUnitPanel.add(Box.createVerticalGlue());

        // コンソールのログ出力
        redirectBox = new JCheckBox("コンソールをファイルに出力する [設定反映に再起動が必要]");

        gbb = new GridBagBuilder("コンソールのログ出力");
        gbb.add(redirectBox, 0, 1, 1, 1, GridBagConstraints.EAST);
        JPanel redirectPanel = gbb.getProduct();

        getUI().setLayout(new BoxLayout(getUI(), BoxLayout.Y_AXIS));
        getUI().add(scrollUnitPanel);
        getUI().add(redirectPanel);
        getUI().add(Box.createVerticalStrut(300));
        getUI().add(Box.createVerticalGlue());

    }

    private void bindModelToView() {
        scrollUnitKarteSpinner.setValue(prefs.getInt(Project.SCROLL_UNIT_KARTE, 15));
        scrollUnitTableSpinner.setValue(prefs.getInt(Project.SCROLL_UNIT_TABLE, 15));
        scrollUnitStampSpinner.setValue(prefs.getInt(Project.SCROLL_UNIT_STAMP, 15));

        redirectBox.setSelected(Preferences.userNodeForPackage(Dolphin.class).getBoolean(Project.REDIRECT_CONSOLE, false));
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
    }
}
