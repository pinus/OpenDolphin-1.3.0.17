package open.dolphin.impl.scheam.undoevent;

import javafx.scene.layout.StackPane;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.SchemaLayer;
import open.dolphin.impl.scheam.UndoEvent;

/**
 * @author pns
 */
public class UndoRotateEvent implements UndoEvent {
    private final StackPane canvasPane;
    private final SchemaLayer baseLayer;
    private double rotate;

    public UndoRotateEvent(SchemaEditorImpl context, double r) {
        canvasPane = context.getCanvasPane();
        baseLayer = context.getBaseLayer();
        // 逆回転にして保存しておく
        rotate = -r;
    }

    @Override
    public void rollback() {
        // RotateEditor#rotateLayers のコピペ
        double w = baseLayer.getWidth();
        double h = baseLayer.getHeight();

        // BaseLayer の大きさを変えると全部変わる
        baseLayer.setWidth(h);
        baseLayer.setHeight(w);

        // Base Image Rotation
        baseLayer.getHolder().rotate(rotate);
        baseLayer.redraw();

        // DrawLayers Rotation
        canvasPane.getChildren().forEach(node -> {
            SchemaLayer layer = (SchemaLayer) node;
            layer.getHolder().rotate(rotate);
            layer.redraw();
        });

        // 逆回転にして保存
        rotate = -rotate;
    }
}
