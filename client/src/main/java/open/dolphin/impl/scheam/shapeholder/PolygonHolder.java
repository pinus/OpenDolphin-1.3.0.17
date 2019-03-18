package open.dolphin.impl.scheam.shapeholder;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.FillRule;
import open.dolphin.impl.scheam.FillMode;
import open.dolphin.impl.scheam.helper.SchemaUtils;

/**
 * Polygon を保持する ShapeHolder.
 *
 * @author pns
 */
public class PolygonHolder extends ShapeHolderBase {

    protected void drawPath(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(getPathX(0), getPathY(0));
        for (int i = 1; i < getPathSize(); i++) {
            gc.lineTo(getPathX(i), getPathY(i));
        }
    }

    @Override
    public void draw() {
        super.draw();

        GraphicsContext gc = getGraphicsContext();
        gc.setFillRule(FillRule.NON_ZERO);

        // FillMode に応じて描画
        if (getFillMode() != FillMode.Line) {
            gc.setEffect(getBlurEffect());

            drawPath(gc);
            gc.closePath();
            gc.fill();
            gc.setEffect(null);
        }
        if (getFillMode() != FillMode.Fill) {
            drawPath(gc);
            gc.closePath();
            gc.stroke();
        }
    }

    @Override
    public boolean contains(double x, double y) {
        // どれかの点と近ければ contains と判断
        for (int i = 0; i < getPathSize(); i++) {
            if (SchemaUtils.isNear(
                    x, y,
                    getPathX(i), getPathY(i))) {
                return true;
            }
        }
        // または，Path の内部であれば true
        return getGraphicsContext().isPointInPath(x, y);
    }
}
