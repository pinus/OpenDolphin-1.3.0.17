package open.dolphin.impl.scheam.stateeditor;

import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.SchemaLayer;
import open.dolphin.impl.scheam.ShapeHolder;
import open.dolphin.impl.scheam.ShapeHolderBounds;
import open.dolphin.impl.scheam.UndoManager;
import open.dolphin.impl.scheam.shapeholder.PolygonHolder;

/**
 * 選択範囲を切り抜く StateEditor
 * 親の SchemaLayer を選択範囲の大きさにして，そこに選択範囲が入るように translate する
 * @author pns
 */
public class ClipEditor extends StateEditorBase {
    private final SchemaLayer draftLayer;
    private final SchemaLayer baseLayer;
    private final StackPane canvasPane;
    private final UndoManager undoManager;

    private PolygonHolder draftHolder;

    public ClipEditor(SchemaEditorImpl context) {
        draftLayer = context.getDraftLayer();
        baseLayer = context.getBaseLayer();
        canvasPane = context.getCanvasPane();
        undoManager = context.getUndoManager();
    }

    @Override
    public void mouseDown(MouseEvent e) {
        draftHolder = new PolygonHolder();
        draftHolder.setLineColor(Color.WHITE);
        draftHolder.setFillColor(Color.TRANSPARENT);
        draftHolder.setLineWidth(1.0);
        // setHolder の前に座標を決めなければならない
        for (int i=0; i<4; i++) {
            draftHolder.addPathX(e.getX());
            draftHolder.addPathY(e.getY());
        }

        draftLayer.setBlendMode(BlendMode.DIFFERENCE);
        draftLayer.setHolder(draftHolder);
        draftLayer.draw();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // draftHolder の範囲外には移動させない
        double x = (e.getX() < 0)? 0 : (e.getX() >= draftLayer.getWidth())? draftLayer.getWidth()-1 : e.getX();
        double y = (e.getY() < 0)? 0 : (e.getY() >= draftLayer.getHeight())? draftLayer.getHeight()-1 : e.getY();

        draftHolder.setPathX(1, x);
        draftHolder.setPathX(2, x); draftHolder.setPathY(2, y);
        draftHolder.setPathY(3, y);
        draftLayer.redraw();
    }

    @Override
    public void mouseUp(MouseEvent e) {
        draftLayer.clear();
        draftLayer.setBlendMode(null);

        ShapeHolderBounds b = draftHolder.getBounds();
        double w = b.getWidth();
        double h = b.getHeight();
        if (w == 0 || h == 0) { return; }

        double dx = - b.getMinX();
        double dy = - b.getMinY();

        // UndoManager に登録
        undoManager.offerClip(baseLayer.getWidth(), baseLayer.getHeight(), dx, dy);

        // baseLayer
        baseLayer.setWidth(w);
        baseLayer.setHeight(h);
        baseLayer.getHolder().translate(dx, dy);
        baseLayer.redraw();

        // DrawLayers
        for (Node n : canvasPane.getChildren()) {
            SchemaLayer layer = (SchemaLayer) n;
            layer.getHolder().translate(dx, dy);
            layer.redraw();
        }
    }

    @Override
    public ShapeHolder getHolder() {
        return null;
    }
}
