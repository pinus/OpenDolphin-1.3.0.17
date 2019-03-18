package open.dolphin.impl.scheam.undoevent;

import open.dolphin.impl.scheam.SchemaLayer;
import open.dolphin.impl.scheam.UndoEvent;
import open.dolphin.impl.scheam.shapeholder.ShapeHolderBase;

/**
 * @author pns
 */
public class UndoLineWidthEvent implements UndoEvent {
    private final SchemaLayer layer;
    private final ShapeHolderBase holder;
    private double lineWidth;

    public UndoLineWidthEvent(ShapeHolderBase h) {
        layer = (SchemaLayer) h.getGraphicsContext().getCanvas();
        holder = h;
    }

    public void setPreviousLineWidth(double d) {
        lineWidth = d;
    }

    @Override
    public void rollback() {
        double tmp = holder.getLineWidth();
        holder.setLineWidth(lineWidth);
        lineWidth = tmp;

        // redraw まで管理
        layer.redraw();
    }
}
