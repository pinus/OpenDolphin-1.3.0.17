package open.dolphin.impl.scheam.stateeditor;

import javafx.scene.ImageCursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.SchemaLayer;
import open.dolphin.impl.scheam.ShapeHolder;
import open.dolphin.impl.scheam.UndoManager;
import open.dolphin.impl.scheam.constant.ShapeIcon;
import open.dolphin.impl.scheam.shapeholder.ShapeHolderBase;

/**
 * Canvas 全部を拡大／縮小する StateEditor.
 * 普通は縮小モード，Option を押すと拡大モードになる.
 * モードに合わせてカーソルを変化させる.
 *
 * @author pns
 */
public class ScaleEditor extends StateEditorBase {
    private final SchemaLayer draftLayer;
    private final SchemaLayer baseLayer;
    private final StackPane canvasPane;
    private final UndoManager undoManager;

    private final ImageCursor cursorPlus;
    private final ImageCursor cursorMinus;
    // 増分
    private double diffX, diffY;

    public ScaleEditor(SchemaEditorImpl context) {
        draftLayer = context.getDraftLayer();
        baseLayer = context.getBaseLayer();
        canvasPane = context.getCanvasPane();
        undoManager = context.getUndoManager();

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        Image plusImage = ShapeIcon.getLoupePlus().snapshot(parameters, null);
        Image minusImage = ShapeIcon.getLoupeMinus().snapshot(parameters, null);
        cursorPlus = new ImageCursor(plusImage, plusImage.getWidth() / 2, plusImage.getHeight() / 2);
        cursorMinus = new ImageCursor(minusImage, minusImage.getWidth() / 2, minusImage.getHeight() / 2);
    }

    @Override
    public void start() {
        draftLayer.setCursor(cursorMinus);
        // 10% ずつ増減
        diffX = draftLayer.getWidth() / 10;
        diffY = draftLayer.getHeight() / 10;
    }

    @Override
    public void end() {
        draftLayer.setCursor(null);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isAltDown()) {
            draftLayer.setCursor(cursorPlus);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        draftLayer.setCursor(cursorMinus);
    }

    @Override
    public void mouseDown(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseUp(MouseEvent e) {
        double dx;
        double dy;

        // Option が押されていない場合は縮小とする
        if (e.isAltDown()) {
            dx = diffX;
            dy = diffY;
        } else {
            dx = -diffX;
            dy = -diffY;
        }

        double w = baseLayer.getWidth();
        double h = baseLayer.getHeight();
        // 50 ドット以下にはしない
        if (w + dx < 50 && h + dy < 50) {
            return;
        }

        // bind されているので，BaseLayer の大きさを変更すると全ての Layer が変更される
        baseLayer.setWidth(w + dx);
        baseLayer.setHeight(h + dy);

        // BaseLayer
        scaleLayers(baseLayer, dx, dy, w, h);

        // DrawLayers
        canvasPane.getChildren().forEach(node -> {
            scaleLayers((SchemaLayer) node, dx, dy, w, h);
        });

        // UndoManager に登録
        undoManager.offerScale(dx, dy);
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

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public ShapeHolder getHolder() {
        return null;
    }
}
