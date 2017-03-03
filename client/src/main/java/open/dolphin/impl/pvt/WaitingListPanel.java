package open.dolphin.impl.pvt;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import open.dolphin.client.ClientContext;
import open.dolphin.client.GUIConst;
import open.dolphin.client.MainComponentPanel;
import open.dolphin.ui.MyJScrollPane;
import open.dolphin.ui.StatusPanel;

/**
 * WaitingListView 互換 JPanel
 * @author pns
 */
public class WaitingListPanel extends MainComponentPanel {
    private static final long serialVersionUID = 1L;

    private static final ImageIcon KUTU_ICON = GUIConst.ICON_ARROW_CIRCULAR_ALT1_16;

    // command panel
    private JButton kutuBtn;
    private JLabel legendLbl;
    private JLabel atokLbl;
    // main panel
    private JTable table;
    // status panel
    private JLabel checkedTimeLbl;
    private JLabel countLbl;
    private JLabel dateLbl;
    private JLabel intervalLbl;

    public WaitingListPanel() {
        initComponents();
    }

    private void initComponents() {

        CommandPanel comPanel = getCommandPanel();
        remove(comPanel);

        kutuBtn = new JButton(KUTU_ICON);
        if (ClientContext.isWin()) {
            kutuBtn.setBorder(null);
            kutuBtn.setContentAreaFilled(false);
        } else {
            kutuBtn.setBorderPainted(false);
        }
        kutuBtn.setFocusable(false);
        legendLbl = new JLabel();
        //atokLbl = new JLabel("ATOKメモリ");
        //comPanel.add(kutuBtn);
        //comPanel.addGlue();
        //comPanel.add(atokLbl);
        //comPanel.add(legendLbl);
        //comPanel.setMargin(4);

        MainPanel mainPanel = getMainPanel();
        table = new RowTipsTable();
        table.putClientProperty("Quaqua.Table.style", "striped");
        MyJScrollPane scroller = new MyJScrollPane(table);
        scroller.isPermanentScrollBar = true;
        mainPanel.add(scroller);

        StatusPanel statusPanel = getStatusPanel();
        checkedTimeLbl = new JLabel("00:00");
        countLbl = new JLabel("来院数10人，待ち10人，待ち時間 00:00");
        dateLbl = new JLabel("2011-11-11(土)");
        intervalLbl = new JLabel("チェック間隔：30秒");
        statusPanel.add(kutuBtn);
        if (ClientContext.isWin()) { statusPanel.addSpace(5); }
        statusPanel.add(checkedTimeLbl);
        statusPanel.addSeparator();
        statusPanel.add(intervalLbl);
        statusPanel.addGlue();
        statusPanel.add(dateLbl);
        statusPanel.addSeparator();
        statusPanel.add(countLbl);
        statusPanel.setMargin(4);
    }

    public JLabel getCheckedTimeLbl() {
        return checkedTimeLbl;
    }

    public JLabel getCountLbl() {
        return countLbl;
    }

    public JLabel getDateLbl() {
        return dateLbl;
    }

    public JLabel getIntervalLbl() {
        return intervalLbl;
    }

    public JButton getKutuBtn() {
        return kutuBtn;
    }

    public JLabel getLegendLbl() {
        return legendLbl;
    }

    public JLabel getAtokLbl() {
        return atokLbl;
    }

    public JTable getTable() {
        return table;
    }
}
