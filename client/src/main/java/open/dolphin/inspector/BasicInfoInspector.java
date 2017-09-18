package open.dolphin.inspector;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import open.dolphin.client.ChartImpl;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.SimpleAddressModel;
import open.dolphin.util.MMLDate;
import open.dolphin.util.StringTool;

/**
 * BasicInfoInspector.
 * 名前，年齢，生年月日と住所. 3行バージョン.
 * @author pns
 */
public class BasicInfoInspector implements IInspector {
    public static final String NAME = "患者情報";

    private static final Color FONT_COLOR = new Color(20,20,140); // 濃い青
    private static final Color MALE_COLOR = new Color(230,243,243);
    private static final Color FEMALE_COLOR = new Color(254,221,242);
    private static final Color INDETERMINATE_COLOR = Color.LIGHT_GRAY;

    private final Border MALE_BORDER = new NameBorder(MALE_COLOR);
    private final Border FEMALE_BORDER = new NameBorder(FEMALE_COLOR);
    private final Border UNKNOWN_BORDER = new NameBorder(INDETERMINATE_COLOR);

    private JLabel nameLabel;
    private JLabel kanaLabel;
    private JLabel ageLabel;
    private JLabel birthdayLabel;
    private JLabel addressLabel;

    private JPanel namePanel;
    private JPanel panel;

    // Context このインスペクタの親コンテキスト
    private final ChartImpl context;

    /**
     * BasicInfoInspector オブジェクトを生成する.
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
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
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

        birthdayLabel = new JLabel(" ");
        birthdayLabel.setForeground(FONT_COLOR);
        birthdayLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        birthdayLabel.setHorizontalAlignment(SwingConstants.LEFT);
        birthdayLabel.setOpaque(false);

        addressLabel = new JLabel("　");
        addressLabel.setHorizontalAlignment(SwingConstants.LEFT);
        addressLabel.setForeground(FONT_COLOR);
        addressLabel.setOpaque(false);
        addressLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));

        // 名前，年齢を表示するパネル
        namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        namePanel.setOpaque(false);
        namePanel.add(nameLabel);
        namePanel.add(kanaLabel);
        namePanel.add(Box.createHorizontalGlue());
        namePanel.add(ageLabel);

        JPanel birthdayPanel = new JPanel();
        birthdayPanel.setOpaque(true);
        birthdayPanel.setBackground(IInspector.BACKGROUND);
        birthdayPanel.setLayout(new BoxLayout(birthdayPanel, BoxLayout.X_AXIS));
        birthdayPanel.add(birthdayLabel);
        birthdayPanel.add(Box.createHorizontalGlue());

        JPanel addressPanel = new JPanel();
        addressPanel.setOpaque(true);
        addressPanel.setBackground(IInspector.BACKGROUND);
        addressPanel.setLayout(new BoxLayout(addressPanel, BoxLayout.X_AXIS));
        addressPanel.add(addressLabel);
        addressPanel.add(Box.createHorizontalGlue());

        // 生年月日，住所を入れたパネル
        JPanel ptInfoPanel = new JPanel();
        ptInfoPanel.setOpaque(true);
        ptInfoPanel.setBackground(IInspector.BACKGROUND);
        ptInfoPanel.setLayout(new BoxLayout(ptInfoPanel, BoxLayout.Y_AXIS));
        ptInfoPanel.add(Box.createVerticalStrut(3));
        ptInfoPanel.add(birthdayPanel);
        ptInfoPanel.add(addressPanel);
        ptInfoPanel.add(Box.createVerticalStrut(5));

        // 名前パネルに頭のインデントを入れたもの
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
        upperPanel.add(Box.createHorizontalStrut(5));
        upperPanel.add(namePanel);

        // 生年月日，住所パネルにインデントを入れたもの
        JPanel lowerPanel = new JPanel();
        lowerPanel.setOpaque(true);
        lowerPanel.setBackground(IInspector.BACKGROUND);
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));
        lowerPanel.add(Box.createHorizontalStrut(15));
        lowerPanel.add(ptInfoPanel);
        lowerPanel.add(Box.createHorizontalStrut(10));

        // 境界線つき
        panel = new JPanel() {
            private static final long serialVersionUID = 1L;
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.setColor(IInspector.BORDER_COLOR);
                //g.drawLine(0, 30, getWidth()-1, 30);
                g.drawLine(10, getHeight()-1, getWidth()-11, getHeight()-1);

            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setName(NAME);

        panel.add(upperPanel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lowerPanel);
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

        // kanjiName が全てカタカナの場合以外は kanaName を表示する (13863, 14642, 20205, 15660, 5035)
        int maxLength = 14;
        String test = kanjiName.replaceAll("[　,\\s]", ""); // スペースを除去

        if (StringTool.isAllKatakana(test)) {
            // 全部がカナ文字の場合
            // 13文字以上の場合圧縮
            if (kanjiName.length() > maxLength) {
                float sy = (float) maxLength / (float) kanjiName.length();
                Font font = nameLabel.getFont().deriveFont(AffineTransform.getScaleInstance(sy, 1));
                nameLabel.setFont(font);
            }

        } else {
            kanaName = "（" + patient.getKanaName() + "）";

            // 名前が長い場合は，カナ名を圧縮する
            if (kanjiName.length() + kanaName.length() > maxLength) {
                float sy = (float) (maxLength - kanjiName.length() + 1) / (float) kanaName.length();
                Font font = nameLabel.getFont().deriveFont(AffineTransform.getScaleInstance(sy, 1));
                kanaLabel.setFont(font);
            }
        }

        nameLabel.setText(kanjiName);
        kanaLabel.setText(kanaName);

        String birthday = patient.getBirthday();
        String nengoBirthday = MMLDate.toFullNengo(birthday);
        birthdayLabel.setText(nengoBirthday + "生");

        String age = ModelUtils.getAge(birthday);
        ageLabel.setText(String.format("%s 歳", age));

        SimpleAddressModel address = patient.getAddress();
        if (address != null) {
            String addr = StringTool.toZenkakuNumber(address.getAddress());
            addressLabel.setText(addr.replaceAll("北海道", "")); // 北海道は省略
        } else {
            addressLabel.setText("　");
        }

        switch (patient.getGenderDesc()) {
            case IInfoModel.MALE_DISP:
                namePanel.setBorder(MALE_BORDER);
                break;
            case IInfoModel.FEMALE_DISP:
                namePanel.setBorder(FEMALE_BORDER);
                break;
            default:
                namePanel.setBorder(UNKNOWN_BORDER);
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

    private class NameBorder extends AbstractBorder {
        private static final long serialVersionUID = 1L;

        private Insets borderInsets = new Insets(4,8,5,4);
        private Color color;

        public NameBorder(Color c) {
            color = c;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(color);
            g.fillRoundRect(0, 0, width-1, height-1, 8, 8);
            g.setColor(IInspector.BORDER_COLOR);
            g.drawRoundRect(0, 0, width-1, height-1, 8, 8);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return borderInsets;
        }
    }
}
