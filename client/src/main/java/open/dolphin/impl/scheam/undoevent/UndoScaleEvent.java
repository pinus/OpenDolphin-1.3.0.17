package open.dolphin.impl.scheam.undoevent;

import javafx.scene.layout.StackPane;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.SchemaLayer;
import open.dolphin.impl.scheam.UndoEvent;
import open.dolphin.impl.scheam.shapeholder.ShapeHolderBase;

/**
 * @author pns
 */
public class UndoScaleEvent implements UndoEvent {
    private final StackPane canvasPane;
    private final SchemaLayer baseLayer;
    private double dx, dy;

    public UndoScaleEvent(SchemaEditorImpl context, double x, double y) {
        canvasPane = context.getCanvasPane();
        baseLayer = context.getBaseLayer();
        // 拡大縮小反転
        dx = -x;
        dy = -y;
    }

    @Override
    public void rollback() {
        double w = baseLayer.getWidth();
        double h = baseLayer.getHeight();

        // bind されているので，BaseLayer の大きさを変更すると全ての Layer が変更される
        baseLayer.setWidth(w + dx);
        baseLayer.setHeight(h + dy);
        baseLayer.redraw();

        // BaseLayer
        scaleLayers(baseLayer, dx, dy, w, h);

        // DrawLayers
        canvasPane.getChildren().forEach(node -> {
            scaleLayers((SchemaLayer) node, dx, dy, w, h);
        });

        // 拡大縮小反転して保存
        dx = -dx;
        dy = -dy;
    }

    /**
     * 各 Layer を拡大／縮小する.
     *
     * @param dx
     * @param dy
     * @param w
     * @param h
     */
    private void scaleLayers(SchemaLayer layer, double dx, double dy, double w, double h) {
        ShapeHolderBase holder = (ShapeHolderBase) layer.getHolder();

        double holderx = holder.getStartX();
        double holdery = holder.getStartY();
        double holderw = holder.getEndX() - holder.getStartX();
        double holderh = holder.getEndY() - holder.getStartY();

        holder.translate(holderx * dx / w, holdery * dy / h);
        holder.scale(holderw * dx / w, holderh * dy / h);

        layer.redraw();
    }
}
