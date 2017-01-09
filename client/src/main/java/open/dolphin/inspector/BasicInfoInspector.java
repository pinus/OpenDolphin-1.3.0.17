package open.dolphin.inspector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import open.dolphin.client.ChartImpl;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.SimpleAddressModel;
import open.dolphin.ui.PNSBorderFactory;

/**
 * BasicInfoInspector.
 * 名前，年齢，生年月日と住所.
 * このインスペクタだけは ChartImpl の ToolPanel に入る.
 * @author kazm
 * @author pns
 */
public class BasicInfoInspector implements IInspector {

    private static final Color FONT_COLOR = new Color(20,20,140); // 濃い青
    private static final Border MALE_BORDER = PNSBorderFactory.createTitleBarBorderLightBlue(new Insets(0,0,0,0));
    private static final Border FEMALE_BORDER = PNSBorderFactory.createTitleBarBorderPink(new Insets(0,0,0,0));
    private static final Border UNKNOWN_BORDER = PNSBorderFactory.createTitleBarBorderGray(new Insets(0,0,0,0));
    private static final int WIDTH_EXTENSION = 50;

    private JPanel aquaPanel;
    private JLabel nameLabel;
    private JLabel addressLabel;

    // Context このインスペクタの親コンテキスト
    private final ChartImpl context;

    /**
     * BasicInfoInspectorオブジェクトを生成する.
     * @param context
     */
    public BasicInfoInspector(ChartImpl context) {
        this.context = context;
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
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        namePanel.setOpaque(false);
        namePanel.add(nameLabel);

        addressLabel = new JLabel("　");
        addressLabel.setHorizontalAlignment(SwingConstants.LEFT);
        addressLabel.setForeground(FONT_COLOR);
        addressLabel.setOpaque(false);
        addressLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        JPanel addressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        addressPanel.setOpaque(false);
        addressPanel.add(addressLabel);

        aquaPanel = new JPanel(new BorderLayout());
        aquaPanel.add(addressPanel, BorderLayout.CENTER);
        aquaPanel.add(namePanel, BorderLayout.NORTH);

        // サイズ調節
        aquaPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH + WIDTH_EXTENSION, 42));
        aquaPanel.setMaximumSize(new Dimension(DEFAULT_WIDTH + WIDTH_EXTENSION, 42));
        aquaPanel.setMinimumSize(new Dimension(DEFAULT_WIDTH + WIDTH_EXTENSION, 42));
    }

    /**
     * レイウアトのためにこのインスペクタのコンテナパネルを返す.
     * @return コンテナパネル
     */
    @Override
    public JPanel getPanel() {
        return aquaPanel;
    }

    /**
     * 患者の基本情報を表示する.
     */
    @Override
    public void update() {
        PatientModel patient = context.getPatient();

        String name = String.format("%s (%s)   %s", patient.getFullName(), patient.getKanaName(), patient.getAgeBirthday());
        nameLabel.setText(name);

        SimpleAddressModel address = patient.getAddress();
        if (address != null) {
            addressLabel.setText(address.getAddress());
        } else {
            addressLabel.setText("　");
        }

        switch (patient.getGenderDesc()) {
            case IInfoModel.MALE_DISP:
                aquaPanel.setBorder(MALE_BORDER);
                break;
            case IInfoModel.FEMALE_DISP:
                aquaPanel.setBorder(FEMALE_BORDER);
                break;
            default:
                aquaPanel.setBorder(UNKNOWN_BORDER);
                break;
        }
    }
}
