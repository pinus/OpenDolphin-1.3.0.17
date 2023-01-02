package open.dolphin.stampbox;

import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.ui.PNSScrollPane;
import open.dolphin.ui.StatusPanel;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;

/**
 * StampTreePanel.
 *
 * @author Kazushi Minagawa
 * @author pns
 */
public class StampTreePanel extends JPanel implements TreeSelectionListener {

    private final StampTree stampTree;
    private StatusPanel statusPanel;

    public StampTreePanel(StampTree tree) {
        stampTree = tree;
        initComponents();
    }

    private void initComponents() {
        JScrollPane scroller = new PNSScrollPane(stampTree);

        setLayout(new BorderLayout());
        add(scroller, BorderLayout.CENTER);

        String treeEntity = stampTree.getEntity();
        if (treeEntity != null) {
            statusPanel = new StatusPanel();
            statusPanel.add("", "infoArea");
            statusPanel.setPanelHeight(18);
            statusPanel.setFontSize(8);

            this.add(statusPanel, BorderLayout.SOUTH);

            //this.add(infoArea, BorderLayout.SOUTH);
            stampTree.addTreeSelectionListener(this);
        }
    }

    /**
     * このパネルのStampTreeを返す.
     *
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
