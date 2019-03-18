package open.dolphin.impl.scheam.shapeholder;

/**
 * Polygon の Draft を描くときに MultiClickMode で使う ShapeHolder.
 * 通常モードは Draft のために PenHolder を使うが，
 * MultiClickMode では MouseMove に合わせてガイドラインを表示する.
 *
 * @author pns
 */
public class PolygonDraftHolder extends PenHolder {
    // Write a guide line hovering with mouse move
    private double hoverX = -1, hoverY = -1;

    @Override
    public void draw() {
        super.draw();

        if (hoverX >= 0) {
            int size = getPathSize();
            double lastx = getPathX(size - 1);
            double lasty = getPathY(size - 1);
            getGraphicsContext().strokeLine(lastx, lasty, hoverX, hoverY);
        }
    }

    public void setHoverX(double x) {
        hoverX = x;
    }

    public void setHoverY(double y) {
        hoverY = y;
    }
}
