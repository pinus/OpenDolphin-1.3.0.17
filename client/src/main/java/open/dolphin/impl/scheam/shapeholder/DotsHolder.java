package open.dolphin.impl.scheam.shapeholder;

import javafx.scene.canvas.GraphicsContext;
import open.dolphin.impl.scheam.FillMode;
import open.dolphin.impl.scheam.helper.SchemaUtils;

/**
 * 複数の点を保持する ShapeHolder.
 *
 * @author pns
 */
public class DotsHolder extends ShapeHolderBase {
    private int dotDataSize;

    public int getDotDataSize() {
        return dotDataSize;
    }

    public void setDotDataSize(int s) {
        dotDataSize = s;
    }

    @Override
    public void draw() {
        super.draw();

        double dotSize = getLineWidth();
        GraphicsContext gc = getGraphicsContext();

        // FillMode に応じて描画
        if (getFillMode() != FillMode.Line) {
            for (int i = 0; i < dotDataSize; i++) {
                gc.fillOval(getPathX(i) - dotSize / 2, getPathY(i) - dotSize / 2, dotSize, dotSize);
            }
        }
        if (getFillMode() != FillMode.Fill) {
            gc.beginPath();
            gc.moveTo(getPathX(dotDataSize), getPathY(dotDataSize));
            for (int i = dotDataSize + 1; i < getPathSize(); i++) {
                gc.lineTo(getPathX(i), getPathY(i));
            }
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
        return false;
    }
}
