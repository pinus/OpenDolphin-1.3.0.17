package open.dolphin.impl.scheam.shapeholder;

import javafx.scene.canvas.GraphicsContext;
import open.dolphin.impl.scheam.constant.Const;

/**
 * EraserEditor で作られる ShapeHolder.
 * 実体は太さ LineWidth * ERASER_WIDTH_FACTOR の PenHolder.
 *
 * @author pns
 */
public class EraserHolder extends PenHolder {

    @Override
    public void draw() {
        super.draw();

        GraphicsContext gc = getGraphicsContext();
        gc.setLineWidth(getLineWidth() * Const.ERASER_WIDTH_FACTOR);

        gc.beginPath();
        gc.moveTo(getPathX(0), getPathY(0));
        for (int i = 1; i < getPathSize(); i++) {
            gc.lineTo(getPathX(i), getPathY(i));
        }
        gc.stroke();
    }
}
