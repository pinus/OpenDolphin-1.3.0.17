package open.dolphin.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;
import open.dolphin.client.ClientContext;

public class MyTableUI extends BasicTableUI {

    private static final Color DEFAULT_ODD_COLOR = ClientContext.getColor("color.odd");
    private static final Color DEFAULT_EVEN_COLOR = ClientContext.getColor("color.even");
    private static final Color[] ROW_COLORS = {DEFAULT_EVEN_COLOR, DEFAULT_ODD_COLOR};

    public static ComponentUI createUI(JComponent c) {
        return new MyTableUI();
    }
    
    /**
     *  ストライプなテーブルUI
     * @author masuda, Masuda Naika
     * http://explodingpixels.wordpress.com/2008/10/05/making-a-jtable-fill-the-view-without-extension/
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        if ( c.getParent() instanceof JViewport) {
            // get the row index at the top of the clip bounds (the first row to paint).
            int rowAtPoint = table.rowAtPoint(g.getClipBounds().getLocation());
            // get the y coordinate of the first row to paint. if there are no rows in the table, start
            // painting at the top of the supplied clipping bounds.
            int topY = rowAtPoint < 0 ? g.getClipBounds().y : table.getCellRect(rowAtPoint, 0, true).y;

            // create a counter variable to hold the current row. if there are no rows in the table,
            // start the counter at 0.
            int currentRow = rowAtPoint < 0 ? 0 : rowAtPoint;
            while (topY < g.getClipBounds().y + g.getClipBounds().height) {
                int bottomY = topY + table.getRowHeight();
                g.setColor(ROW_COLORS[currentRow & 1]);
                g.fillRect(g.getClipBounds().x, topY, g.getClipBounds().width, bottomY);
                topY = bottomY;
                currentRow++;
            }
        }
        super.paint(g, c);
    }
}
