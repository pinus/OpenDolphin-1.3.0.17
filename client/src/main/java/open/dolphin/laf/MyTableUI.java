package open.dolphin.laf;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;
import open.dolphin.client.ClientContext;

/**
 * ストライプなテーブルUI.
 * @autho pns
 */
public class MyTableUI extends BasicTableUI {

    private static final Color DEFAULT_ODD_COLOR = ClientContext.getColor("color.odd");
    private static final Color DEFAULT_EVEN_COLOR = ClientContext.getColor("color.even");
    private static final Color[] ROW_COLORS = { DEFAULT_EVEN_COLOR, DEFAULT_ODD_COLOR };

    public static ComponentUI createUI(JComponent c) {
        return new MyTableUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);

        JTable t = (JTable) c;
        t.setFillsViewportHeight(true);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Object property = table.getClientProperty("Quaqua.Table.style");
        boolean isStriped = property != null && property.equals("striped");

        if (isStriped) {
            Rectangle r = g.getClipBounds();

            int[] top = getTopY(r.y);
            int topY = top[0];
            int currentRow = top[1]-1;

            // ClipBounds.y から topY まで塗る
            g.setColor(ROW_COLORS[currentRow & 1]);
            g.fillRect(r.x, r.y, r.width, topY);
            currentRow ++;

            // 続きを塗る
            while (topY < r.y + r.height) {
                int bottomY = topY + table.getRowHeight();
                g.setColor(ROW_COLORS[currentRow & 1]);
                g.fillRect(r.x, topY, r.width, bottomY);
                topY = bottomY;
                currentRow++;
            }
        }
        super.paint(g, c);
    }

    /**
     * ClipBound.y を越えた初めての Cell の Y 座標とその行数.
     * @param clipY
     * @return
     */
    private int[] getTopY(int clipY) {
        int rowHeight = table.getRowHeight();
        if (table.getRowCount() > 0) {
            int row = 0;
            int ｙ = table.getCellRect(0, 0, true).y;
            while (ｙ < clipY) {
                ｙ += rowHeight;
                row++;
            }
            return new int[]{ ｙ, row };
        } else {
            return new int[]{ 0, 0 };
        }
    }
}
