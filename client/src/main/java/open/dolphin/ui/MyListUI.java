package open.dolphin.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicListUI;
import open.dolphin.client.ClientContext;

/**
 *
 * @author pns
 */
public class MyListUI extends BasicListUI {

    private static final Color DEFAULT_ODD_COLOR = ClientContext.getColor("color.odd");
    private static final Color DEFAULT_EVEN_COLOR = ClientContext.getColor("color.even");
    private static final Color[] ROW_COLORS = {DEFAULT_EVEN_COLOR, DEFAULT_ODD_COLOR};

    private int lastCellHeight = 16;

    public static ComponentUI createUI(JComponent list) {
        return new MyListUI();
    }

    /**
     * ストライプな ListUI.
     * @author pns
     */
    @Override
    public void paint(Graphics g, JComponent c) {

        // get the row index at the top of the clip bounds (the first row to paint).
        int rowAtPoint = list.locationToIndex(g.getClipBounds().getLocation());
        // get the y coordinate of the first row to paint. if there are no rows in the table, start
        // painting at the top of the supplied clipping bounds.
        int topY = rowAtPoint < 0 ? g.getClipBounds().y : list.getCellBounds(rowAtPoint, rowAtPoint).y;

        // create a counter variable to hold the current row. if there are no rows in the table,
        // start the counter at 0.
        int currentRow = rowAtPoint < 0 ? 0 : rowAtPoint;

        while (topY < g.getClipBounds().y + g.getClipBounds().height) {
            Rectangle r = list.getCellBounds(currentRow, currentRow);
            // if currentRow is out of range, r == null
            // if FixedCellHeight is not set, getFixedHeight returns -1
            if (r != null) {
                lastCellHeight = r.height;
            } else {
                int h = list.getFixedCellHeight();
                if (h > 0) { lastCellHeight = h; }
            }
            int bottomY = topY + lastCellHeight;
            g.setColor(ROW_COLORS[currentRow & 1]);
            g.fillRect(g.getClipBounds().x, topY, g.getClipBounds().width, bottomY);
            topY = bottomY;
            currentRow++;
        }
        super.paint(g, c);
    }
}
