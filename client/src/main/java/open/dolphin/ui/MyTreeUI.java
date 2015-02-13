package open.dolphin.ui;

import com.sun.java.swing.plaf.windows.WindowsTreeUI;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.ComponentUI;
import open.dolphin.client.ClientContext;

/**
 *
 * @author pns
 */
public class MyTreeUI extends WindowsTreeUI {

    private static final Color DEFAULT_ODD_COLOR = ClientContext.getColor("color.odd");
    private static final Color DEFAULT_EVEN_COLOR = ClientContext.getColor("color.even");
    private static final Color[] ROW_COLORS = {DEFAULT_EVEN_COLOR, DEFAULT_ODD_COLOR};

    public static ComponentUI createUI(JComponent tree) {
        final JTree t = (JTree) tree;
        // tree が展開されたときにバックグラウンドがみだれるのの workaround
        t.addTreeSelectionListener(new TreeSelectionListener(){
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                t.repaint();
            }
        });
        return new MyTreeUI();
    }

    /**
     * ストライプな TreeUI
     * @author pns
     */
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
            g.setColor(ROW_COLORS[currentRow & 1]);
            g.fillRect(g.getClipBounds().x, topY, g.getClipBounds().width, bottomY);
            topY = bottomY;
            currentRow++;
        }

        super.paint(g, c);
    }
}
