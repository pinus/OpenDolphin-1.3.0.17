package open.dolphin.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * CalendarPanel ヘッダーの曜日部分のレンダラ.
 * @author pns
 */
public class CalendarHeaderRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;

    // バックグランドのグラデーションカラー
    private static final int[] BG = {240,255,255,255,255,255,255,251,247,244,241,231,231,231,231,231,231,241,234};
    private static final Color[] BGCOL = new Color[BG.length];
    static {
        for (int i=0; i<BG.length; i++) {
            BGCOL[i] = new Color(BG[i], BG[i], BG[i]);
        }
    }

    public CalendarHeaderRenderer() {
        init();
    }

    private void init() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setOpaque(false);
        setBackground(new Color(0,true));
    }

    @Override
    public Component getTableCellRendererComponent(JTable t, Object v, boolean isSelected, boolean hasFocus, int row, int col) {
        super.getTableCellRendererComponent(t, v, isSelected, hasFocus, row, col);

        switch (col) {
            case 0:
                // 日曜
                setForeground(Color.red);
                break;
            case 6:
                // 土曜
                setForeground(Color.blue);
                break;
            default:
                // 平日
                setForeground(Color.black);
        }

        return this;
    }

    // バックグランドにグラデーションをつける
    @Override
    public void paintComponent(Graphics graphics) {
        for (int y=0; y<getHeight();y++) {
            Graphics g = graphics.create();
            g.setColor(BGCOL[y]);
            g.drawLine(0, y, getWidth()-1, y);
            g.dispose();
        }
        super.paintComponent(graphics);
    }
}
