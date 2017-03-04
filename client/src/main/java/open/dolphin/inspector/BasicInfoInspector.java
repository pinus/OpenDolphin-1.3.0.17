package open.dolphin.inspector;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import open.dolphin.client.ChartImpl;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.SimpleAddressModel;
import open.dolphin.ui.PNSBorderFactory;
import open.dolphin.util.StringTool;

/**
 * BasicInfoInspector.
 * 名前，年齢，生年月日と住所.
 * このインスペクタだけは ChartImpl の ToolPanel に入る.
 * @author kazm
 * @author pns
 */
public class BasicInfoInspector implements IInspector {
    public static final String NAME = "患者情報";

    private static final Color FONT_COLOR = new Color(20,20,140); // 濃い青
    private static final Border MALE_BORDER = PNSBorderFactory.createTitleBarBorderLightBlue(new Insets(0,0,0,0));
    private static final Border FEMALE_BORDER = PNSBorderFactory.createTitleBarBorderPink(new Insets(0,0,0,0));
    private static final Border UNKNOWN_BORDER = PNSBorderFactory.createTitleBarBorderGray(new Insets(0,0,0,0));
    private static final int WIDTH = 340;
    private static final int INDENT = 8;

    private JPanel panel;
    private JLabel nameLabel;
    private JLabel kanaLabel;
    private JLabel ageLabel;
    private JLabel addressLabel;

    // Context このインスペクタの親コンテキスト
    private final ChartImpl context;

    /**
     * BasicInfoInspectorオブジェクトを生成する.
     * @param parent
     */
    public BasicInfoInspector(PatientInspector parent) {
        context = parent.getContext();
        initComponent();
    }

    /**
     * GUI コンポーネントを初期化する.
     */
    private void initComponent() {

        nameLabel = new JLabel("　");
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        nameLabel.setForeground(FONT_COLOR);
        nameLabel.setOpaque(false);

        kanaLabel = new JLabel(" ");
        kanaLabel.setHorizontalAlignment(SwingConstants.LEFT);
        kanaLabel.setForeground(FONT_COLOR);
        kanaLabel.setOpaque(false);

        ageLabel = new JLabel(" ");
        ageLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        ageLabel.setForeground(FONT_COLOR);
        ageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        ageLabel.setOpaque(false);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));

        namePanel.setOpaque(false);
        namePanel.add(Box.createHorizontalStrut(INDENT));
        namePanel.add(nameLabel);
        namePanel.add(kanaLabel);
        namePanel.add(Box.createGlue());
        namePanel.add(ageLabel);
        namePanel.add(Box.createHorizontalStrut(INDENT));

        addressLabel = new JLabel("　");
        addressLabel.setHorizontalAlignment(SwingConstants.LEFT);
        addressLabel.setForeground(FONT_COLOR);
        addressLabel.setOpaque(false);
        addressLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        JPanel addressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, INDENT, 0));
        addressPanel.setOpaque(false);
        addressPanel.add(addressLabel);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setName(NAME);
        panel.add(namePanel);
        panel.add(Box.createVerticalGlue());
        panel.add(addressPanel);

        // サイズ調節
        panel.setPreferredSize(new Dimension(WIDTH, 42));
        panel.setMaximumSize(new Dimension(WIDTH, 42));
        panel.setMinimumSize(new Dimension(WIDTH, 42));
    }

    /**
     * レイウアトのためにこのインスペクタのコンテナパネルを返す.
     * @return コンテナパネル
     */
    @Override
    public JPanel getPanel() {
        return panel;
    }

    /**
     * 患者の基本情報を表示する.
     */
    @Override
    public void update() {
        PatientModel patient = context.getPatient();
        String kanjiName = patient.getFullName();

        String kanaName = "";

        // kanjiName が全てカタカナの場合以外は kanaName を表示する
        String test = kanjiName.replaceAll("[　,\\s]", ""); // スペースを除去

        if (! StringTool.isAllKatakana(test)) {
            kanaName = "（" + patient.getKanaName() + "）";

            // 名前が長い場合は，カナ名を圧縮する
            if (kanjiName.length() + kanaName.length() > 15) {
                float sy = (float) (15.5 - kanjiName.length()) / kanaName.length();
                Font font = nameLabel.getFont().deriveFont(AffineTransform.getScaleInstance(sy, 1));
                kanaLabel.setFont(font);
            }
        }

        nameLabel.setText(kanjiName);
        kanaLabel.setText(kanaName);
        String birthday = patient.getBirthday();
        String age = ModelUtils.getAge(birthday);
        ageLabel.setText(String.format("%s 歳 %s", age, ModelUtils.toNengo(birthday)));

        SimpleAddressModel address = patient.getAddress();
        if (address != null) {
            addressLabel.setText(StringTool.toZenkakuNumber(address.getAddress()));
        } else {
            addressLabel.setText("　");
        }

        switch (patient.getGenderDesc()) {
            case IInfoModel.MALE_DISP:
                panel.setBorder(MALE_BORDER);
                break;
            case IInfoModel.FEMALE_DISP:
                panel.setBorder(FEMALE_BORDER);
                break;
            default:
                panel.setBorder(UNKNOWN_BORDER);
                break;
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return NAME;
    }
}
