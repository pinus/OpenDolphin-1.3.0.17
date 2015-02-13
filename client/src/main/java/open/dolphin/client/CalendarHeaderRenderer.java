package open.dolphin.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author pns
 */
public class CalendarHeaderRenderer extends DefaultTableCellRenderer {
    // バックグランドのグラデーションカラー
    private static int[] BG = {240,255,255,255,255,255,255,251,247,244,241,231,231,231,231,231,231,241,234};
    private static Color[] BGCOL = new Color[BG.length];
    static {
        for (int i=0; i<BG.length; i++) {
            BGCOL[i] = new Color(BG[i], BG[i], BG[i]);
        }
    }

    public CalendarHeaderRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setOpaque(false);
        setBackground(new Color(0,true));
    }

    @Override
    public Component getTableCellRendererComponent(JTable t, Object v, boolean isSelected, boolean hasFocus, int row, int col) {
        super.getTableCellRendererComponent(t, v, isSelected, hasFocus, row, col);

        switch (col) {
            case 0:
                setForeground(Color.red);
                break;
            case 6:
                setForeground(Color.blue);
                break;
            default:
                setForeground(Color.black);
        }

        return this;
    }

    // バックグランドにグラデーションをつける
    @Override
    public void paintComponent(Graphics g) {
        for (int y=0; y<getHeight();y++) {
            g.setColor(BGCOL[y]);
            g.drawLine(0, y, getWidth()-1, y);
        }
        super.paintComponent(g);
    }
}
