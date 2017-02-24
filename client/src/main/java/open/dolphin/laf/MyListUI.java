package open.dolphin.laf;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicListUI;

/**
 * ストライプな ListUI.
 *
 * @author pns
 */
public class MyListUI extends BasicListUI {

    private UIHelper helper;

    public static ComponentUI createUI(JComponent list) {
        return new MyListUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);

        helper = new UIHelper(c);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        System.out.println("-list " + g.getClipBounds());
        Object property = list.getClientProperty("Quaqua.List.style");
        boolean isStriped = property != null && property.equals("striped");
        helper.setRendererColors(list);

        if (isStriped) {
            Rectangle clip = g.getClipBounds();
            int[] top = getTopY(clip.y);
            // １行前から開始
            int currentRow = top[1]-1;
            int topY = top[0]-getCellHeight(currentRow);

            while (topY < clip.y + clip.height) {
                int bottomY = topY + getCellHeight(currentRow);
                g.setColor(UIHelper.ROW_COLORS[currentRow & 1]);
                g.fillRect(clip.x, topY, clip.width, bottomY);
                topY = bottomY;
                currentRow++;
            }
        }
        super.paint(g, c);
    }

    /**
     * Focus に応じて selction foreground / background を変える.
     * @param g
     * @param row
     * @param rowBounds
     * @param renderer
     * @param dataModel
     * @param selModel
     * @param leadIndex
     */
    @Override
    protected void paintCell( Graphics g, int row, Rectangle rowBounds, ListCellRenderer renderer,
            ListModel dataModel, ListSelectionModel selModel, int leadIndex) {

        boolean hasFocus = list.isFocusOwner();
        Color origFore = list.getSelectionForeground();
        Color origBack = list.getSelectionBackground();
        Color fore = helper.getForeground(true, hasFocus);
        Color back = helper.getBackground(true, hasFocus);

        // Focus に応じて foreground 色も変える
        if (! origFore.equals(fore)) { list.setSelectionForeground(helper.getForeground(true, hasFocus)); }
        if (! origBack.equals(back)) { list.setSelectionBackground(helper.getBackground(true, hasFocus)); }

        super.paintCell(g, row, rowBounds, renderer, dataModel, selModel, leadIndex);

        // 戻す
        if (! origFore.equals(fore)) { list.setSelectionForeground(origFore); }
        if (! origBack.equals(back)) { list.setSelectionBackground(origBack); }
    }

   /**
     * ClipBound.y を越えた初めての Cell の Y 座標とその行数.
     *
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
            return new int[]{y, row};

        } else {
            return new int[]{0, 0};
        }
    }

    /**
     * row の高さを得る.
     *
     * @param row
     * @return
     */
    private int getCellHeight(int row) {
        int fixed = list.getFixedCellHeight();
        if (fixed > 0) {
            return fixed;
        }

        int listSize = list.getModel().getSize();
        if (listSize == 0) {
            // 空のリスト
            return UIHelper.DEFAULT_ROW_HEIGHT;

        } else {
            return (row < listSize - 1)
                    ? list.getCellBounds(row, row).height
                    : list.getCellBounds(listSize - 1, listSize - 1).height;
        }
    }
}
