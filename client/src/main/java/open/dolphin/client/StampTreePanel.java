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
    private StatusPanel statusPanel;

    public StampTreePanel(StampTree tree) {

        this.stampTree = tree;
        JScrollPane scroller = new MyJScrollPane(stampTree);

        this.setLayout(new BorderLayout());
        this.add(scroller, BorderLayout.CENTER);

        String treeEntity = stampTree.getEntity();
        if (treeEntity != null) {
            statusPanel = new StatusPanel();
            statusPanel.add("", "infoArea");
            statusPanel.setPanelHeight(18);
            statusPanel.setFontSize(8);

            this.add(statusPanel, BorderLayout.SOUTH);

            //this.add(infoArea, BorderLayout.SOUTH);
            tree.addTreeSelectionListener(this);
        }
    }

    /**
     * このパネルのStampTreeを返す.
     * @return StampTree
     */
    public StampTree getTree() {
        return stampTree;
    }

    /**
     * スタンプツリーで選択されたスタンプの情報を表示する.
     */
    @Override
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
