package open.dolphin.inspector;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import open.dolphin.ui.MyJScrollPane;
import open.dolphin.ui.StatusPanel;

/**
 *
 * @author pns
 */
public class DocumentHistoryPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JLabel cntLbl;
    private JComboBox docTypeCombo;
    private JComboBox periodCombo;
    private JTable table;

    public DocumentHistoryPanel() {
        initComponents();
        // connect(); // ほとんど使わないのでやめた
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(0,0));

        table = new JTable();
        MyJScrollPane scroller = new MyJScrollPane(table);
        scroller.setBorder(BorderFactory.createEmptyBorder());
        scroller.putClientProperty("JComponent.sizeVariant", "small");

        cntLbl = new JLabel("0件");
        cntLbl.setHorizontalAlignment(SwingConstants.RIGHT);

        docTypeCombo = new JComboBox();
        docTypeCombo.setModel(new DefaultComboBoxModel(new String[] { "カルテ" }));

        // 文書抽出期間の項目は DocumentHistory で管理
        periodCombo = new JComboBox();
        String [] periodLabels = new String[DocumentHistory.extractionObjects.length];
        for (int i=0; i<periodLabels.length; i++) {
            periodLabels[i] = DocumentHistory.extractionObjects[i].getName();
        }
        periodCombo.setModel(new DefaultComboBoxModel(periodLabels));
        periodCombo.setPreferredSize(new Dimension(76,24));
        periodCombo.setMaximumSize(new Dimension(76,24));
        periodCombo.setMinimumSize(new Dimension(76,24));

        StatusPanel statusPanel = new StatusPanel();
        statusPanel.setPanelHeight(26);
        statusPanel.add(periodCombo);
        statusPanel.addGlue();
        statusPanel.add(cntLbl);
        statusPanel.setMargin(4);

        this.add(scroller, BorderLayout.CENTER);
        this.add(statusPanel, BorderLayout.SOUTH);
    }

    private void connect() {
        // ショートカット command-1~6 で文書抽出期間選択
        InputMap im = table.getInputMap();
        ActionMap am = table.getActionMap();
        for (int i=0; i<periodCombo.getItemCount(); i++) {
            String name = "history" + String.valueOf(i);
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_1 + i, KeyEvent.META_MASK), name);
            am.put(name, new SelectPeriodAction(i));
        }
    }

    private class SelectPeriodAction extends AbstractAction {

        private static final long serialVersionUID = 1L;
        private final int index;
        public SelectPeriodAction(int i) {
            index = i;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            periodCombo.setSelectedIndex(index);
        }
    }

    public JLabel getCntLbl() {
        return cntLbl;
    }

    public JComboBox getDocTypeCombo() {
        return docTypeCombo;
    }

    public JComboBox getExtractCombo() {
        return periodCombo;
    }

    public JTable getTable() {
        return table;
    }
}
