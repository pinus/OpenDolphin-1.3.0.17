package open.dolphin.laf;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicListUI;
import open.dolphin.client.ClientContext;

/**
 * ストライプな ListUI.
 * @author pns
 */
public class MyListUI extends BasicListUI {

    private static final Color DEFAULT_ODD_COLOR = ClientContext.getColor("color.odd");
    private static final Color DEFAULT_EVEN_COLOR = ClientContext.getColor("color.even");
    private static final Color[] ROW_COLORS = {DEFAULT_EVEN_COLOR, DEFAULT_ODD_COLOR};
    private static final int DEFAULT_ROW_HEIGHT = 16;

    public static ComponentUI createUI(JComponent list) {
        return new MyListUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Object property = list.getClientProperty("Quaqua.List.style");
        boolean isStriped = property != null && property.equals("striped");

        if (isStriped) {
            Rectangle clip = g.getClipBounds();
            int[] top = getTopY(clip.y);
            int topY = top[0];
            int currentRow = top[1]-1;

            // ClipBounds.y から topY まで塗る
            g.setColor(ROW_COLORS[currentRow & 1]);
            g.fillRect(clip.x, clip.y, clip.width, topY);
            currentRow ++;

            // 続きを塗る
            while (topY < clip.y + clip.height) {
                int bottomY = topY + getCellHeight(currentRow);
                g.setColor(ROW_COLORS[currentRow & 1]);
                g.fillRect(clip.x, topY, clip.width, bottomY);
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

        int listSize = list.getModel().getSize();

        if (listSize != 0) {
            int row = 0;
            int y = list.getCellBounds(row, row).y;
            
            while (y < clipY) {
                y += getCellHeight(row);
                row++;
            }
            return new int[] { y, row };

        } else  {
            return new int[] { 0, 0 };
        }
    }

    /**
     * row の高さを得る.
     * @param row
     * @return
     */
    private int getCellHeight(int row) {
        int fixed = list.getFixedCellHeight();
        if (fixed > 0) { return fixed; }

        int listSize = list.getModel().getSize();
        if (listSize == 0) {
            // 空のリスト
            return DEFAULT_ROW_HEIGHT;

        } else {
            return (row < listSize-1)?
                    list.getCellBounds(row, row).height:
                    list.getCellBounds(listSize-1, listSize-1).height;
        }
    }
}
