package open.dolphin.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

/**
 * グラデーションをもった枠.
 * @author pns
 */
public class PNSBorder {

    // selected border color
    protected static final Color INSIDE_COLOR = new Color(217,231,244);
    protected static final Color INSIDE_MIDDLE_COLOR = new Color(161,189,215);
    protected static final Color MIDDLE_COLOR = new Color(134,177,218);
    protected static final Color OUTSIDE_MIDDLE_COLOR = new Color(161,192,222);
    protected static final Color OUTSIDE_COLOR = new Color(197,213,227);

    // gray selected color
    protected static final Color INSIDE_GRAY_COLOR = new Color(255,255,255);
    protected static final Color INSIDE_MIDDLE_GRAY_COLOR = new Color(255,255,255);
    protected static final Color MIDDLE_GRAY_COLOR = new Color(200,200,200);

    protected static final Color APEX_GRAY_COLOR = new Color(235,235,235);

    // blue selected color
    protected static final Color INSIDE_BLUE_COLOR = new Color(134,177,218);
    protected static final Color INSIDE_MIDDLE_BLUE_COLOR = new Color(56,117,215);
    protected static final Color MIDDLE_BLUE_COLOR = new Color(56,117,215);
    protected static final Color OUTSIDE_MIDDLE_BLUE_COLOR = new Color(56,117,215);
    protected static final Color OUTSIDE_BLUE_COLOR = new Color(134,177,218);

    // red selected color
    protected static final Color INSIDE_RED_COLOR = new Color(216,89,93);
    protected static final Color INSIDE_MIDDLE_RED_COLOR = new Color(234,33,50);
    protected static final Color MIDDLE_RED_COLOR = new Color(255,37,51);
    protected static final Color OUTSIDE_MIDDLE_RED_COLOR = new Color(222,27,47);
    protected static final Color OUTSIDE_RED_COLOR = new Color(226,162,161);

    // insets
    protected static final Insets NULL_MARGIN = new Insets(0,0,0,0);
    protected static final Insets TEXT_FIELD_MARGIN = new Insets(0,4,0,0);
    protected static final Insets DEFAULT_MARGIN = new Insets(3,3,3,3);

    /**
     * ボーダーを重なった四角形で描画
     * @param level 一番外側が 0
     * @param color
     * @param g
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public static void drawRect(int level, Color color, Graphics g, int x, int y, int width, int height) {
        g.setColor(color);
        g.drawRect(x+level, y+level, width-level*2, height-level*2);
    }

    /**
     * ボーダーを重なった角丸四角形で描画
     * @param level 一番外側が 0
     * @param color
     * @param g
     * @param x
     * @param y
     * @param width
     * @param height
     * @param arcx
     * @param arcy
     */
    public static void drawRoundRect(int level, Color color, Graphics g, int x, int y, int width, int height, int arcx, int arcy) {
        g.setColor(color);
        g.drawRoundRect(x+level, y+level, width-level*2, height-level*2, arcx, arcy);
    }

    /**
     * 四角形のスミを別に描画（角丸にみせるため）
     * @param level　一番外側が 0
     * @param color
     * @param g
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public static void drawAngle(int level, Color color, Graphics g, int x, int y, int width, int height) {
        int x1 = x + level; int x2 = width - 1 - level;
        int y1 = y + level; int y2 = height - 1 - level;
        g.setColor(color);
        g.drawLine(x1,y1,x1,y1);
        g.drawLine(x1,y2,x1,y2);
        g.drawLine(x2,y1,x2,y1);
        g.drawLine(x2,y2,x2,y2);
    }

    public static void drawSelectedRect(Component c, Graphics g, int x, int y, int width, int height) {
        drawRect (0, MIDDLE_COLOR,         g, x, y, width, height);
        drawRect (1, INSIDE_MIDDLE_COLOR,  g, x, y, width, height);
        drawRect (2, INSIDE_COLOR,         g, x, y, width, height);

        drawAngle(0, APEX_GRAY_COLOR,      g, x, y, width, height);
        drawAngle(1, MIDDLE_COLOR,         g, x, y, width, height);
        drawAngle(2, INSIDE_MIDDLE_COLOR,  g, x, y, width, height);
    }

    public static void drawSelectedRoundRect(Component c, Graphics g, int x, int y, int width, int height, int arcx, int arcy) {
        drawRoundRect (0, INSIDE_COLOR,         g, x, y, width, height, arcx, arcy);
        drawRoundRect (1, INSIDE_MIDDLE_COLOR,  g, x, y, width, height, arcx, arcy);
        drawRoundRect (2, MIDDLE_COLOR,         g, x, y, width, height, arcx, arcy);
        drawRoundRect (3, OUTSIDE_MIDDLE_COLOR, g, x, y, width, height, arcx, arcy);
        drawRoundRect (4, OUTSIDE_COLOR,        g, x, y, width, height, arcx, arcy);
    }

    public static void drawSelectedBlueRoundRect(Component c, Graphics g, int x, int y, int width, int height, int arcx, int arcy) {
        drawRoundRect (2, INSIDE_BLUE_COLOR,         g, x, y, width, height, arcx, arcy);
        drawRoundRect (1, INSIDE_MIDDLE_BLUE_COLOR,  g, x, y, width, height, arcx, arcy);
        drawRoundRect (0, MIDDLE_BLUE_COLOR,         g, x, y, width, height, arcx, arcy);
        //drawRoundRect (3, OUTSIDE_MIDDLE_BLUE_COLOR, g, x, y, width, height, arcx, arcy);
        //drawRoundRect (4, OUTSIDE_BLUE_COLOR,        g, x, y, width, height, arcx, arcy);
    }

    public static void drawSelectedRedRoundRect(Component c, Graphics g, int x, int y, int width, int height, int arcx, int arcy) {
        drawRoundRect (2, INSIDE_RED_COLOR,         g, x, y, width, height, arcx, arcy);
        drawRoundRect (1, INSIDE_MIDDLE_RED_COLOR,  g, x, y, width, height, arcx, arcy);
        drawRoundRect (0, MIDDLE_RED_COLOR,         g, x, y, width, height, arcx, arcy);
        //drawRoundRect (3, OUTSIDE_MIDDLE_RED_COLOR, g, x, y, width, height, arcx, arcy);
        //drawRoundRect (4, OUTSIDE_RED_COLOR,        g, x, y, width, height, arcx, arcy);
    }
    public static void drawSelectedBlueRect(Component c, Graphics g, int x, int y, int width, int height) {
        drawRect (0, MIDDLE_BLUE_COLOR,         g, x, y, width, height);
        drawRect (1, INSIDE_MIDDLE_BLUE_COLOR,  g, x, y, width, height);
        drawRect (2, INSIDE_BLUE_COLOR,         g, x, y, width, height);

        drawAngle(0, INSIDE_BLUE_COLOR,         g, x, y, width, height);
        drawAngle(1, MIDDLE_BLUE_COLOR,         g, x, y, width, height);
        drawAngle(2, INSIDE_MIDDLE_BLUE_COLOR,  g, x, y, width, height);
    }

    public static void drawSelectedGrayRect(Component c, Graphics g, int x, int y, int width, int height) {
        drawRect (0, MIDDLE_GRAY_COLOR,         g, x, y, width, height);
        drawRect (1, INSIDE_MIDDLE_GRAY_COLOR,  g, x, y, width, height);
        drawRect (2, INSIDE_GRAY_COLOR,         g, x, y, width, height);

        drawAngle(0, APEX_GRAY_COLOR,           g, x, y, width, height);
        drawAngle(1, MIDDLE_GRAY_COLOR,         g, x, y, width, height);
        drawAngle(2, INSIDE_MIDDLE_GRAY_COLOR,  g, x, y, width, height);
    }

    public static void clearRect(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(c.getBackground());
        g.drawRect(x, y, width-1, height-1);
        g.drawRect(x+1, y+1, width-3, height-3);
        g.drawRect(x+2, y+2, width-5, height-5);
    }

    public static void drawSelectedLine(Component c, Graphics g, int x, int y, int width, int height){
        int level = y;
        if (y < 2) { level = 2; }
        g.setColor(INSIDE_COLOR); g.drawLine(x, level, width, level);
        g.setColor(INSIDE_MIDDLE_COLOR); g.drawLine(x, level-1, width, level-1);
        g.setColor(MIDDLE_COLOR); g.drawLine(x, level-2, width, level-2);
    }

    public static void drawSelectedBlueLine(Component c, Graphics g, int x, int y, int width, int height){
        int level = y;
        if (y < 2) { level = 2; }
        g.setColor(INSIDE_BLUE_COLOR); g.drawLine(x, level, width, level);
        g.setColor(INSIDE_MIDDLE_BLUE_COLOR); g.drawLine(x, level-1, width, level-1);
        g.setColor(MIDDLE_BLUE_COLOR); g.drawLine(x, level-2, width, level-2);
    }

    public static void drawSelectedGrayLine(Component c, Graphics g, int x, int y, int width, int height){
        int level = y;
        if (y < 2) { level = 2; }
        g.setColor(INSIDE_GRAY_COLOR); g.drawLine(x, level, width, level);
        g.setColor(INSIDE_MIDDLE_GRAY_COLOR); g.drawLine(x, level-1, width, level-1);
        g.setColor(MIDDLE_GRAY_COLOR); g.drawLine(x, level-2, width, level-2);
    }
}
