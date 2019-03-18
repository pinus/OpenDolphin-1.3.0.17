package open.dolphin.impl.scheam.helper;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.stage.Window;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author pns
 */
public class SchemaUtils {

    /**
     * Image から BufferedImage に変換.
     *
     * @param src
     * @return
     */
    public static Image toFXImage(java.awt.Image src) {
        int width = 0;
        int height = 0;

        if (src != null) {
            width = src.getWidth(null);
            height = src.getHeight(null);
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(src, 0, 0, null);
        g.dispose();

        return SwingFXUtils.toFXImage(image, null);
    }

    /**
     * Color -> WebString "#ffffff" 変換.
     *
     * @param color
     * @return string
     */
    public static String colorToString(Color color) {
        StringBuilder buf = new StringBuilder();
        int r = (int) (255 * color.getRed());
        int g = (int) (255 * color.getGreen());
        int b = (int) (255 * color.getBlue());
        int a = (int) (255 * color.getOpacity());
        return String.format("#%02x%02x%02x%02x", r, g, b, a);
    }

    /**
     * 与えられた opacity を設定した色を返す.
     *
     * @param c
     * @param opacity
     * @return
     */
    public static Color mergeOpacity(Color c, double opacity) {
        return Color.color(c.getRed(), c.getGreen(), c.getBlue(), opacity);
    }

    /**
     * opacity 以外一致しているかどうかを返す.
     *
     * @param c
     * @param c1
     * @return
     */
    public static boolean equalsExceptOpacity(Color c, Color c1) {
        return c.getRed() == c1.getRed()
                && c.getGreen() == c1.getGreen()
                && c.getBlue() == c1.getBlue();
    }

    /**
     * Node の左上隅のスクリーン座標での位置を返す.
     * On Mac, top-left corner of the screen is (0,22) because of the menu bar.
     * In case JFXPanel (com.sun.javafx.stage.EmbeddedWindow) it becomes (0,44) due to title bar.
     * (javafx.stage.Stage includes title bar, while com.sun.javafx.stage.EmbededWindow does not)
     * This can be adjusted using scene.getX() and scene.getY().
     * Node#localToScene(0,0) points node's top-left corner position in the scene.
     * Node's size includes it's drop shadow.
     * (x,y) in show(window, x, y) are screen coordinates.
     *
     * @param node
     * @return
     */
    public static Point2D getScreenLocation(Node node) {
        Scene s = node.getScene();
        Window w = s.getWindow();
        Point2D n = node.localToScene(0, 0);

        double x = w.getX() + s.getX() + n.getX();
        double y = w.getY() + s.getY() + n.getY();

        return new Point2D(x, y);
    }

    /**
     * 逆行列を作る.
     * [ mxx mxy tx ]       1  [  myy -mxy  mxy*ty-myy*tx ]
     * [ myx myy ty ] ->   --- [ -myx  mxx  myx*tx-mxx*ty ]
     * [  0   0   1 ]      det [   0    0         det     ]
     *
     * @param a
     * @return
     */
    public static Affine createInvert(Affine a) {
        Affine invert = new Affine();
        double det = a.getMxx() * a.getMyy() - a.getMxy() * a.getMyx();
        invert.setMxx(a.getMyy() / det);
        invert.setMxy(-a.getMxy() / det);
        invert.setTx((a.getMxy() * a.getTy() - a.getMyy() * a.getTx()) / det);
        invert.setMyx(-a.getMyx() / det);
        invert.setMyy(a.getMxx() / det);
        invert.setTy((a.getMyx() * a.getTx() - a.getMxx() * a.getTy()) / det);
        invert.setTz(1);

        return invert;
    }

    /**
     * 点 (x,y) を Affin Transform した Point を返す.
     * [ mxx mxy tx ]   [ x ]   [ x*mxx + y*mxy + tx ]
     * [ myx myy ty ] * [ y ] = [ x*myx + y*myy + ty ]
     * [  0   0   1 ]   [ 1 ]   [          1         ]
     *
     * @param a
     * @param x
     * @param y
     * @return
     */
    public static Point2D affineTransform(Affine a, double x, double y) {
        return new Point2D(
                x * a.getMxx() + y * a.getMxy() + a.getTx(),
                x * a.getMyx() + y * a.getMyy() + a.getTy());
    }

    /**
     * (x1,y1) と (x2,y2) が近いかどうか.
     * 10ドット未満なら近いと判断
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static boolean isNear(double x1, double y1, double x2, double y2) {
        return Math.abs(x1 - x2) < 10 && Math.abs(y1 - y2) < 10;
    }

    /**
     * (0,0)と(ux,uy)を通る直線と点(vx,vy)の距離を返す.
     * u = (ux,uy), v = (vx,vy) とすると，L = u x v / |u|
     *
     * @param ux
     * @param uy
     * @param vx
     * @param vy
     * @return
     */
    public static double getDistance(double ux, double uy, double vx, double vy) {
        return Math.abs((ux * vy - vx * uy) / Math.sqrt(ux * ux + uy * uy));
    }

    /**
     * (x,y) を中心とした theta 回転を表す Affine を返す.
     * [ cosθ -sinθ x(1-cosθ)+ysinθ ]
     * [ sinθ  cosθ y(1-cosθ)-xsinθ ]
     * [   0    0            1      ]
     *
     * @param theta
     * @param x
     * @param y
     * @return
     */
    public static Affine createRotate(double theta, double x, double y) {
        Affine a = new Affine();
        double sin = Math.sin(theta);
        double cos = Math.cos(theta);
        a.setMxx(cos);
        a.setMxy(-sin);
        a.setMyx(sin);
        a.setMyy(cos);
        a.setTx(x * (1 - cos) + y * sin);
        a.setTy(y * (1 - cos) - x * sin);
        a.setTz(1);

        return a;
    }

    /**
     * 与えられた範囲内の random dots の点列を返す.
     *
     * @param x
     * @param y
     * @param h
     * @param w
     * @param interval
     * @return
     */
    public static List<Point2D> getRandomPoints(double x, double y, double w, double h, double interval) {
        List<Point2D> p = new ArrayList<>();
        Random r = new Random(System.currentTimeMillis());
        for (double px = x; px < x + w; px += interval) {
            for (double py = y; py < y + h; py += interval) {
                double dx = r.nextDouble() * interval / 2.0;
                double dy = r.nextDouble() * interval / 2.0;
                p.add(new Point2D(px + dx, py + dy));
            }
        }
        return p;
    }

    /**
     * 与えられた範囲内の網を表す点列を返す.
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param interval
     * @return
     */
    public static List<Point2D> getNet(double x, double y, double width, double height, double interval) {
        List<Point2D> p = new ArrayList<>();
        // x 方向に１ドットずつスキャン
        for (double px = x; px < x + width; px++) {
            // y 方向は interval 毎にとっていく
            for (double py = y - interval; py < y + height + interval; py += interval) {
                // ずらす量
                double d = (px - x) % interval;
                // プラスにずらした場合
                if (py + d >= y && py + d < y + height) {
                    p.add(new Point2D(px, py + d));
                }
                // マイナスにずらした場合
                if (d != 0 && py - d >= y && py - d < y + height) {
                    p.add(new Point2D(px, py - d));
                }
            }
        }
        return p;
    }
}
