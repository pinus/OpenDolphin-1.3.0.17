package open.dolphin.calendar;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * CalendarPanel ヘッダーの曜日部分のレンダラ.
 *
 * @author pns
 */
public class CalendarHeaderRenderer extends DefaultTableCellRenderer {

    private static final Color BACKGROUND = new Color(240, 240, 240);
    private static final Color BORDER = Color.LIGHT_GRAY;

    // バックグランドのグラデーションカラー
    //private static final int[] BG = {240,255,255,255,255,255,255,251,247,244,241,231,231,231,231,231,231,241,234};
    //private static final Color[] BGCOL = new Color[BG.length];
    //static {
    //    for (int i=0; i<BG.length; i++) {
    //        BGCOL[i] = new Color(BG[i], BG[i], BG[i]);
    //    }
    //}

    public CalendarHeaderRenderer() {
        init();
    }

    private void init() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setOpaque(false);
        setBackground(new Color(0, true));
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
    public void paintComponent(Graphics g) {
        // フラットなバックグランド
        g.setColor(BACKGROUND);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(BORDER);
        g.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1);

        super.paintComponent(g);
    }
}
