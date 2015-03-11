package open.dolphin.impl.scheam.undoevent;

import javafx.scene.layout.StackPane;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.SchemaLayer;
import open.dolphin.impl.scheam.UndoEvent;

/**
 *
 * @author pns
 */
public class UndoClipEvent implements UndoEvent {
    private final StackPane canvasPane;
    private final SchemaLayer baseLayer;
    private double width, height, dx, dy;

    public UndoClipEvent(SchemaEditorImpl context, double w, double h, double x, double y) {
        canvasPane = context.getCanvasPane();
        baseLayer = context.getBaseLayer();

        // 変更前の width, height
        width = w;
        height = h;
        // translate は反転して保存
        dx = -x;
        dy = -y;
    }

    @Override
    public void rollback() {
        double prevWidth = baseLayer.getWidth();
        double prevHeight = baseLayer.getHeight();

        baseLayer.setWidth(width);
        baseLayer.setHeight(height);
        baseLayer.getHolder().translate(dx, dy);
        baseLayer.redraw();

        // DrawLayers
        canvasPane.getChildren().forEach(node -> {
            SchemaLayer layer = (SchemaLayer) node;
            layer.getHolder().translate(dx, dy);
            layer.redraw();
        });

        width = prevWidth;
        height = prevHeight;
        dx = -dx;
        dy = -dy;
    }
}
