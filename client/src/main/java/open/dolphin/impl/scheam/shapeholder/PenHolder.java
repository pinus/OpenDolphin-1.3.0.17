package open.dolphin.impl.scheam.shapeholder;

import javafx.scene.canvas.GraphicsContext;
import open.dolphin.impl.scheam.helper.SchemaUtils;

/**
 * 自由描画の Path を線で保持する ShapeHolder.
 *
 * @author pns
 */
public class PenHolder extends ShapeHolderBase {

    @Override
    public void draw() {
        super.draw();

        GraphicsContext gc = getGraphicsContext();
        gc.beginPath();
        gc.moveTo(getPathX(0), getPathY(0));
        for (int i = 1; i < getPathSize(); i++) {
            gc.lineTo(getPathX(i), getPathY(i));
        }
        gc.stroke();
    }

    @Override
    public boolean contains(double x, double y) {
        // どれかの点と 6 ドット未満の近さなら contains と判断
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
