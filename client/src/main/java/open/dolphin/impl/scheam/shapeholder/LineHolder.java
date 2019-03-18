package open.dolphin.impl.scheam.shapeholder;

import javafx.scene.canvas.GraphicsContext;
import open.dolphin.impl.scheam.helper.SchemaUtils;

/**
 * 線を保持する ShapeHolder.
 * Path(0) と Path(1) を使う.
 * DrawArrow を true にセットすると矢印を書く.
 *
 * @author pns
 */
public class LineHolder extends ShapeHolderBase {

    private boolean drawArrow;

    public boolean getDrawArrow() {
        return drawArrow;
    }

    public void setDrawArrow(boolean b) {
        drawArrow = b;
    }

    @Override
    public void draw() {
        super.draw();

        GraphicsContext gc = getGraphicsContext();
        gc.beginPath();
        gc.moveTo(getPathX(0), getPathY(0));
        gc.lineTo(getPathX(1), getPathY(1));
        if (drawArrow) {
            drawArrow(gc);
        }
        gc.stroke();
    }

    private void drawArrow(GraphicsContext gc) {
        double w = 5; // 矢印の開き方
        double h = 10; // 矢印部分の長さ
        double x = getPathX(1) - getPathX(0);
        double y = getPathY(1) - getPathY(0);
        double len = Math.sqrt(x * x + y * y);
        double lx = h * x / len + w * y / len;
        double ly = h * y / len - w * x / len;
        double rx = h * x / len - w * y / len;
        double ry = h * y / len + w * x / len;

        gc.moveTo(getPathX(0), getPathY(0));
        gc.lineTo(getPathX(0) + lx, getPathY(0) + ly);
        gc.moveTo(getPathX(0), getPathY(0));
        gc.lineTo(getPathX(0) + rx, getPathY(0) + ry);
    }

    @Override
    public void scale(double dx, double dy) {
        setPathX(1, pathXList().get(1) + dx);
        setPathY(1, pathYList().get(1) + dy);
    }

    @Override
    public void rotate(double r) {
    }

    @Override
    public boolean contains(double x, double y) {
        // bound の範囲内にあるかどうか
        if (getBounds().contains(x, y)) {
            // bound の範囲内にあれば，直線との距離が 10 未満なら contains とする
            double ux = getPathX(1) - getPathX(0);
            double uy = getPathY(1) - getPathY(0);
            double vx = x - getPathX(0);
            double vy = y - getPathY(0);
            if (SchemaUtils.getDistance(ux, uy, vx, vy) < 10) {
                return true;
            }
        }

        return false;
    }
}
