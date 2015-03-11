package open.dolphin.impl.scheam.stateeditor;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.SchemaLayer;
import open.dolphin.impl.scheam.ShapeHolder;
import open.dolphin.impl.scheam.StateManager;
import open.dolphin.impl.scheam.shapeholder.LineHolder;

/**
 * 線を描く StateEditor.
 * Shift を押すと水平線 or 垂直線になる.
 * Option を押すと矢印になる.
 * @author pns
 */
public class LineEditor extends StateEditorBase {
    // DraftLayer
    private final SchemaLayer draftLayer;
    private LineHolder draftHolder;
    // Horizontal, Virtical adjustment する前の座標
    private double origX, origY;

    public LineEditor(SchemaEditorImpl context) {
        draftLayer = context.getDraftLayer();
    }
    /**
     * Option，Shift キーを押した場合の処理.
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (! StateManager.isMousePressed()) { return; }
        // Option キーを押したら矢印を描く
        if (e.isAltDown()) { draftHolder.setDrawArrow(true); }
        else { draftHolder.setDrawArrow(false); }
        // Shift キーを押したら垂直 or 水平に adjust
        if (e.isShiftDown()) { adjustHorizontalOrVirtical(); }
        draftLayer.redraw();
    }
    /**
     * キーを放したら元に戻す処理.
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (! StateManager.isMousePressed()) { return; }
        draftHolder.setDrawArrow(false);
        draftHolder.setPathX(1, origX);
        draftHolder.setPathY(1, origY);
        draftLayer.redraw();
    }

    @Override
    public void mouseDown(MouseEvent e) {
        draftHolder = new LineHolder();
        draftHolder.addPathX(e.getX()); draftHolder.addPathY(e.getY());
        draftHolder.addPathX(e.getX()); draftHolder.addPathY(e.getY());
        draftLayer.setHolder(draftHolder);
        draftHolder.bind();
        draftLayer.draw();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        draftHolder.setPathX(1, e.getX()); draftHolder.setPathY(1, e.getY());
        // 水平 or 垂直 adjust 後，もとに戻す処理のためにオリジナルを保存
        origX = e.getX(); origY = e.getY();
        // シフトキーが押されていたら垂直線か水平線にする
        if (e.isShiftDown()) { adjustHorizontalOrVirtical(); }
        if (e.isAltDown()) { draftHolder.setDrawArrow(true); }
        else { draftHolder.setDrawArrow(false); }
        draftLayer.redraw();
    }

    @Override
    public void mouseUp(MouseEvent e) {
        draftLayer.clear();
    }

    @Override
    public ShapeHolder getHolder() {
        if (draftHolder.getPathX(0) == draftHolder.getPathX(1)
                && draftHolder.getPathY(0) == draftHolder.getPathY(1)) {
            return null;
        }
        draftHolder.unbind();
        return draftHolder;
    }

    /**
     * 線を水平か垂直に adjust する.
     */
    private void adjustHorizontalOrVirtical() {
        double dx = Math.abs(draftHolder.getPathX(1) - draftHolder.getPathX(0));
        double dy = Math.abs(draftHolder.getPathY(1) - draftHolder.getPathY(0));
        if (dx > dy) {
            draftHolder.setPathY(1, draftHolder.getPathY(0));
        } else {
            draftHolder.setPathX(1, draftHolder.getPathX(0));
        }
    }
}
