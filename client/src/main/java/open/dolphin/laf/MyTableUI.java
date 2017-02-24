package open.dolphin.laf;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;

/**
 * ストライプなテーブルUI.
 *
 * @autho pns
 */
public class MyTableUI extends BasicTableUI {

    private UIHelper helper;

    public static ComponentUI createUI(JComponent c) {
        return new MyTableUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);

        JTable t = (JTable) c;
        helper = new UIHelper(c);

        // hack rendererPane to control foreground/background
        // paintCell が private なので苦肉の策
        t.remove(rendererPane);
        rendererPane = new PatchedCellRendererPane();
        t.add(rendererPane);

        // ここで初期値を読み込んでいるので，後から変更できない
        helper.setRendererColors(table);

        // データのないところもストライプで埋める
        t.setFillsViewportHeight(true);

        // データのないところをクリックしたら選択を外す
        t.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = t.rowAtPoint(e.getPoint());
                if (row == -1) { t.clearSelection(); }
            }
        });

        //キー入力によるセル編集開始を禁止する
        t.putClientProperty("JTable.autoStartsEdit", false);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setRowHeight(UIHelper.DEFAULT_ROW_HEIGHT);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Object property = table.getClientProperty("Quaqua.Table.style");
        boolean isStriped = property != null && property.equals("striped");

        if (isStriped) {
            Rectangle clip = g.getClipBounds();

            int[] top = getTopY(clip.y);
            // １行前から開始
            int topY = top[0]-table.getRowHeight();
            int currentRow = top[1]-1;

            while (topY < clip.y + clip.height) {
                int bottomY = topY + table.getRowHeight();
                g.setColor(UIHelper.ROW_COLORS[currentRow & 1]);
                g.fillRect(clip.x, topY, clip.width, bottomY);
                topY = bottomY;
                currentRow++;
            }
        }
        super.paint(g, c);
    }

    /**
     * ClipBound.y を越えた初めての Cell の Y 座標とその行数.
     *
     * @param clipY
     * @return
     */
    private int[] getTopY(int clipY) {

        if (table.getRowCount() > 0) {
            int rowHeight = table.getRowHeight();
            int row = 0;
            int ｙ = table.getCellRect(0, 0, true).y;

            while (ｙ < clipY) {
                ｙ += rowHeight;
                row++;
            }
            return new int[]{ｙ, row};

        } else {
            return new int[]{0, 0};
        }
    }

    /**
     * 色を Focus に応じてコントロールする CellRendererPane.
     */
    private class PatchedCellRendererPane extends CellRendererPane {
        private static final long serialVersionUID = 1L;

        @Override
        public void paintComponent(Graphics g, Component c, Container p, int x, int y, int w, int h, boolean shouldValidate) {
            boolean hasFocus = table.isFocusOwner();

            // focus で selection 色を変える
            table.setSelectionForeground(helper.getForeground(true, hasFocus));
            table.setSelectionBackground(helper.getBackground(true, hasFocus));

            super.paintComponent(g, c, p, x, y, w, h, shouldValidate);
        }
    }
}
