package open.dolphin.laf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author pns
 */
public class MyTreeUI extends BasicTreeUI {

    private static final Color DEFAULT_ODD_COLOR = Color.WHITE;
    private static final Color DEFAULT_EVEN_COLOR = new Color(237,243,254);
    private static final Color[] ROW_COLORS = {DEFAULT_EVEN_COLOR, DEFAULT_ODD_COLOR};

    public static ComponentUI createUI(JComponent c) {
        final JTree t = (JTree) c;
        // tree が展開されたときにバックグラウンドがみだれるのの workaround
        //t.addTreeSelectionListener(e -> t.repaint());

        return new MyTreeUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        JTree t = (JTree) c;
        t.setCellRenderer(new CellRenderer());
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        // get the row index at the top of the clip bounds (the first row to paint).
        Point p = g.getClipBounds().getLocation();
        int rowAtPoint = tree.getRowForLocation(p.x, p.y);
        // get the y coordinate of the first row to paint. if there are no rows in the table, start
        // painting at the top of the supplied clipping bounds.
        int topY = rowAtPoint < 0 ? g.getClipBounds().y : tree.getRowBounds(rowAtPoint).y;

        // create a counter variable to hold the current row. if there are no rows in the table,
        // start the counter at 0.
        int currentRow = rowAtPoint < 0 ? 0 : rowAtPoint;
        while (topY < g.getClipBounds().y + g.getClipBounds().height) {
            int bottomY = topY + tree.getRowHeight();
            int row = tree.getRowForLocation(g.getClipBounds().width-1, topY);

            if (tree.isRowSelected(row)) {
                g.setColor(((DefaultTreeCellRenderer)tree.getCellRenderer()).getBackgroundSelectionColor());
            } else {
                g.setColor(ROW_COLORS[currentRow & 1]);
            }

            g.fillRect(g.getClipBounds().x, topY, g.getClipBounds().width, bottomY);
            topY = bottomY;
            currentRow++;
        }

        super.paint(g, c);
    }

    /**
     * 行選択.
     * @param tree
     * @param path
     * @return
     */
    @Override
    public Rectangle getPathBounds(JTree tree, TreePath path) {
        if(tree != null && treeState != null) {
            return getPathBounds(path, tree.getInsets(), new Rectangle());
        }
        return null;
    }

    private Rectangle getPathBounds(TreePath path, Insets insets, Rectangle bounds) {
        Rectangle b = treeState.getBounds(path, bounds);
        if(b != null) {
            b.width = tree.getWidth();
            b.y += insets.top;
        }
        return b;
    }

    private class CellRenderer extends DefaultTreeCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
            boolean expanded, boolean leaf, int row, boolean hasFocus) {
            JLabel l = (JLabel) super.getTreeCellRendererComponent(tree, value, leaf, expanded, leaf, row, hasFocus);

            l.setToolTipText("value = " + value);
            return l;
        }
    }


    public static void main(String[] arg) {
        UIManager.put("TreeUI", MyTreeUI.class.getName());

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTree tree = new JTree();
        ToolTipManager.sharedInstance().registerComponent(tree);
        tree.setPreferredSize(new Dimension(300,400));
        tree.setRootVisible(false);

        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        f.add(tree, BorderLayout.CENTER);
        f.pack();
        f.setLocation(300, 100);
        f.setVisible(true);
    }
}
