package open.dolphin.impl.scheam.shapeholder;

import javafx.scene.canvas.GraphicsContext;
import open.dolphin.impl.scheam.FillMode;
import open.dolphin.impl.scheam.ShapeHolderBounds;

import java.util.ArrayList;
import java.util.List;

/**
 * Oval を保持する ShapeHolder.
 * 回転が必要ないものは OvalHolder で保持して良いが，
 * 回転が必要な場合は Polygon に変換して PolygonHolder として保持しなくてはならない.
 * @author pns
 */
public class OvalHolder extends ShapeHolderBase {

    @Override
    public void draw() {
        super.draw();

        GraphicsContext gc = getGraphicsContext();
        ShapeHolderBounds bounds = getBounds();

        // FillMode に応じて描画
        if (getFillMode() != FillMode.Line) {
            gc.setEffect(getBlurEffect());
            gc.fillOval(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
            gc.setEffect(null);
        }
        if (getFillMode() != FillMode.Fill) {
            gc.strokeOval(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
        }
    }
    /**
     * oval -> polygon 変換.
     * 回転させるために Path に変換しておく必要がある.
     * @return
     */
    public PolygonHolder getPolygonHolder() {
        ShapeHolderBounds bounds = getBounds();
        double a = bounds.getWidth() / 2;
        double b = bounds.getHeight() / 2;
        List<Double> px = new ArrayList<>();
        List<Double> py = new ArrayList<>();

        final int FR = 120;
        for (double f=0; f<FR; f++) {
            px.add(a * Math.cos(2 * Math.PI * f/FR) + bounds.getMinX() + a);
            py.add(b * Math.sin(2 * Math.PI * f/FR) + bounds.getMinY() + b);
        }

        PolygonHolder p = new PolygonHolder();
        p.pathXList().addAll(px);
        p.pathYList().addAll(py);
        p.setProperties();
        return p;
    }
}
