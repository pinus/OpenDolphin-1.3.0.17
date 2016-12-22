package open.dolphin.inspector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import open.dolphin.client.ChartImpl;
import open.dolphin.client.ClientContext;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.SimpleAddressModel;
import open.dolphin.ui.MyBorderFactory;

/**
 * modified by pns
 * @author kazm
 */
public class BasicInfoInspector {

    private JPanel basePanel; // このクラスのパネル
    private JPanel aquaPanel;
    private JLabel nameLabel;
    private JLabel addressLabel;

    private Border maleBorder = MyBorderFactory.createTitleBorderLightBlue(new Insets(0,0,0,0));
    private Border femaleBorder = MyBorderFactory.createTitleBorderPink(new Insets(0,0,0,0));
    private Border unknownBorder = MyBorderFactory.createTitleBorderGray(new Insets(0,0,0,0));


    // Context このインスペクタの親コンテキスト
    private ChartImpl context;


    /**
     * BasicInfoInspectorオブジェクトを生成する.
     */
    public BasicInfoInspector(ChartImpl context) {
        this.context = context;
        initComponent();
        update();
    }

    /**
     * レイウアトのためにこのインスペクタのコンテナパネルを返す.
     * @return コンテナパネル
     */
    public JPanel getPanel() {
        //return basePanel;
        return aquaPanel;
    }

    /**
     * 患者の基本情報を表示する.
     */
    private void update() {

        StringBuilder sb = new StringBuilder();
        sb.append(context.getPatient().getFullName());
        sb.append("  ");
        sb.append(context.getPatient().getAgeBirthday());
        nameLabel.setText(sb.toString());

        SimpleAddressModel address = context.getPatient().getAddress();
        if (address != null) {
            addressLabel.setText(address.getAddress());
        } else {
            addressLabel.setText("　");
        }

        String gender = context.getPatient().getGenderDesc();

        if (gender.equals(IInfoModel.MALE_DISP)) {
            aquaPanel.setBorder(maleBorder);

        } else if (gender.equals(IInfoModel.FEMALE_DISP)) {
            aquaPanel.setBorder(femaleBorder);

        } else {
            aquaPanel.setBorder(unknownBorder);
        }
    }

    /**
     * GUI コンポーネントを初期化する.
     */
    private void initComponent() {

        // 性別によって変えるパネルのバックグランドカラー
        Color foreground = ClientContext.getColor("patientInspector.basicInspector.foreground"); // 濃い青
        int[] size = ClientContext.getIntArray("patientInspector.basicInspector.size");

        nameLabel = new JLabel("　");
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        nameLabel.setForeground(foreground);
        nameLabel.setOpaque(false);
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        namePanel.setOpaque(false);
        namePanel.add(nameLabel);

        addressLabel = new JLabel("　");
        addressLabel.setHorizontalAlignment(SwingConstants.LEFT);
        addressLabel.setForeground(foreground);
        addressLabel.setOpaque(false);
        addressLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        JPanel addressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        addressPanel.setOpaque(false);
        addressPanel.add(addressLabel);

        aquaPanel = new JPanel(new BorderLayout());
        aquaPanel.add(addressPanel, BorderLayout.CENTER);
        aquaPanel.add(namePanel, BorderLayout.NORTH);
        aquaPanel.setPreferredSize(new Dimension(size[0], 38));

        //basePanel = new JPanel(new BorderLayout());
        //basePanel.setOpaque(false);
        //basePanel.add(aquaPanel, BorderLayout.CENTER);

        //basePanel.setBorder(MyBorderFactory.createGroupBoxBorder(new Insets(3,3,3,3)));

//        Dimension dim = new Dimension(size[0], size[1]);
//        basePanel.setMinimumSize(dim);
//        basePanel.setMaximumSize(dim);
//        basePanel.setPreferredSize(dim);
    }
}
