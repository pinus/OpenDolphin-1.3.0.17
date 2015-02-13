package open.dolphin.impl.scheam.stateeditor;

import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.SnapshotParametersBuilder;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.SchemaLayer;
import open.dolphin.impl.scheam.ShapeHolder;
import open.dolphin.impl.scheam.UndoManager;
import open.dolphin.impl.scheam.helper.ShapeIcon;

/**
 * canvas全体を 右に 90度回転する StateEditor
 * Option キーで左 90度回転
 * @author pns
 */
public class RotateEditor extends StateEditorBase {
    private final SchemaLayer draftLayer;
    private final SchemaLayer baseLayer;
    private final StackPane canvasPane;
    private final UndoManager undoManager;

    private final ImageCursor cursor;
    private final ImageCursor cursorReverse;

    public RotateEditor(SchemaEditorImpl context) {
        draftLayer = context.getDraftLayer();
        baseLayer = context.getBaseLayer();
        canvasPane = context.getCanvasPane();
        undoManager = context.getUndoManager();

        SnapshotParameters parameters = SnapshotParametersBuilder.create().fill(Color.TRANSPARENT).build();
        Image img = ShapeIcon.getRotate().snapshot(parameters, null);
        cursor = new ImageCursor(img, img.getWidth()/2, img.getHeight()/2);
        Transform t = Transform.scale(-1, 1);
        parameters.setTransform(t);
        img = ShapeIcon.getRotate().snapshot(parameters, null);
        cursorReverse = new ImageCursor(img, img.getWidth()/2, img.getHeight()/2);
    }

    @Override
    public void start() {
        draftLayer.setCursor(cursor);
        clearDraftLayer();
    }

    @Override
    public void end() {
        draftLayer.setCursor(null);
        clearDraftLayer();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isAltDown()) {
            draftLayer.setCursor(cursorReverse);
            clearDraftLayer();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        draftLayer.setCursor(cursor);
        clearDraftLayer();
    }

    @Override
    public void mouseDown(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseUp(MouseEvent e) {
        double r;
        if (e.isAltDown()) {
            r = -Math.PI/2;
        } else {
            r = Math.PI/2;
        }
        rotateLayers(r);
        // UndoManager に操作を登録
        undoManager.offerRotate(r);
    }

    /**
     * 各 Layer を 90度回転する
     * @param dx
     * @param dy
     */
    private void rotateLayers(double r) {
        double w = baseLayer.getWidth();
        double h = baseLayer.getHeight();
        // double hdiff = stage.getHeight() - baseLayer.getHeight();

        // BaseLayer の大きさを変えると全部変わる
        baseLayer.setWidth(h);
        baseLayer.setHeight(w);

        // Base Image Rotation
        baseLayer.getHolder().rotate(r);
        baseLayer.redraw();

        // DrawLayers Rotation
        for (Node n : canvasPane.getChildren()) {
            SchemaLayer layer = (SchemaLayer) n;
            layer.getHolder().rotate(r);
            layer.redraw();
        }

        // this sets cursor to default
        // stage.setHeight(baseLayer.getHeight() + hdiff);
        // clearDraftLayer();
    }

    @Override
    public ShapeHolder getHolder() {
        return null;
    }

    /**
     * Workaround for bug RT-33412:
     * The mouse cursor needs to move in order for a setCursor(Cursor) to take effect
     */
    private void clearDraftLayer() {
        draftLayer.getGraphicsContext2D().clearRect(0, 0, draftLayer.getWidth(), draftLayer.getHeight());
    }
}
