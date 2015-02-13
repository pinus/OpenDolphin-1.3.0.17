package open.dolphin.client;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.ui.MyJScrollPane;
import open.dolphin.ui.StatusPanel;

/**
 * StampTreePanel
 *
 * @author  Kazushi Minagawa
 */
public class StampTreePanel extends JPanel implements TreeSelectionListener {
    private static final long serialVersionUID = -268963413379453444L;

    protected StampTree stampTree;
    //protected JTextArea infoArea;
    private StatusPanel statusPanel;

    public StampTreePanel(StampTree tree) {

        this.stampTree = tree;
//pns   JScrollPane scroller = new JScrollPane(stampTree);
        JScrollPane scroller = new MyJScrollPane(stampTree);

        this.setLayout(new BorderLayout());
        this.add(scroller, BorderLayout.CENTER);

        String treeEntity = stampTree.getEntity();
//pns   if (treeEntity != null && (!treeEntity.equals(IInfoModel.ENTITY_TEXT))) {
        if (treeEntity != null) {
//          infoArea = new JTextArea();
//pns^
//          infoArea.setBackground(new Color(234,234,234));
//          infoArea.setMargin(new Insets(3, 5, 3, 5));
//pns$      infoArea.setMargin(new Insets(3, 2, 3, 2));
//          infoArea.setLineWrap(true);
//          infoArea.setPreferredSize(new Dimension(250, 18));
//          Font font = GUIFactory.createSmallFont();
//          infoArea.setFont(font);

            statusPanel = new StatusPanel();
            statusPanel.add("", "infoArea");
            statusPanel.setPanelHeight(18);
            statusPanel.setFontSize(8);
            statusPanel.setBackgroundColor(Color.BLACK, 0f, 0.05f);
            statusPanel.setTopLineAlpha(0.4f);

            this.add(statusPanel, BorderLayout.SOUTH);

            //this.add(infoArea, BorderLayout.SOUTH);
            tree.addTreeSelectionListener(this);
        }
    }

    /**
     * このパネルのStampTreeを返す。
     * @return StampTree
     */
    public StampTree getTree() {
        return stampTree;
    }

    /**
     * スタンプツリーで選択されたスタンプの情報を表示する。
     */
    public void valueChanged(TreeSelectionEvent e) {
        StampTree tree = (StampTree) e.getSource();
        StampTreeNode node = (StampTreeNode) tree.getLastSelectedPathComponent();
        if (node != null) {
            if (node.getUserObject() instanceof ModuleInfoBean) {
                ModuleInfoBean info = (ModuleInfoBean) node.getUserObject();
                //infoArea.setText(info.getStampMemo());
                statusPanel.setText(info.getStampMemo(), "infoArea");
            } else {
                //infoArea.setText("");
                statusPanel.setText("", "infoArea");
            }
        } else {
            //infoArea.setText("");
            statusPanel.setText("", "infoArea");
        }
    }
}
