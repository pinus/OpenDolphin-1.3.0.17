package open.dolphin.inspector;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import open.dolphin.ui.ComboBoxFactory;
import open.dolphin.ui.MyJScrollPane;
import open.dolphin.ui.StatusPanel;
import open.dolphin.util.PNSPair;

/**
 *
 * @author pns
 */
public class DocumentHistoryPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JLabel cntLbl;
    private JComboBox<PNSPair<String,Integer>> extractCombo;
    private JTable table;

    public DocumentHistoryPanel() {
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(0,0));

        table = new JTable();
        MyJScrollPane scroller = new MyJScrollPane(table);
        scroller.setBorder(BorderFactory.createEmptyBorder());
        scroller.putClientProperty("JComponent.sizeVariant", "small");

        cntLbl = new JLabel("0件");
        cntLbl.setHorizontalAlignment(SwingConstants.RIGHT);

        // 文書抽出期間の項目は DocumentHistory で管理
        extractCombo = ComboBoxFactory.getDocumentExtractionPeriodCombo();
        extractCombo.setPreferredSize(new Dimension(76,24));
        extractCombo.setMaximumSize(new Dimension(76,24));
        extractCombo.setMinimumSize(new Dimension(76,24));

        StatusPanel statusPanel = new StatusPanel();
        statusPanel.setPanelHeight(26);
        statusPanel.add(extractCombo);
        statusPanel.addGlue();
        statusPanel.add(cntLbl);
        statusPanel.setMargin(4);

        this.add(scroller, BorderLayout.CENTER);
        this.add(statusPanel, BorderLayout.SOUTH);
    }

    public JLabel getCntLbl() {
        return cntLbl;
    }

    public JComboBox getExtractCombo() {
        return extractCombo;
    }

    public JTable getTable() {
        return table;
    }
}
