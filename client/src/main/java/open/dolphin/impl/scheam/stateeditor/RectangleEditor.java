package open.dolphin.impl.scheam.stateeditor;

import javafx.scene.input.MouseEvent;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.SchemaLayer;
import open.dolphin.impl.scheam.ShapeHolder;
import open.dolphin.impl.scheam.shapeholder.PolygonHolder;

/**
 * 四角形を描く StateEditor
 * PolygonHolder の点を４つ使って長方形を描く
 * @author pns
 */
public class RectangleEditor extends StateEditorBase {
    // 途中経過を描く Layer と Holder
    private final SchemaLayer draftLayer;
    private PolygonHolder draftHolder;

    public RectangleEditor(SchemaEditorImpl context) {
        draftLayer = context.getDraftLayer();
    }

    /**
     * StateManager から呼ばれる
     * draftHolder を unbind してから StateManager に渡す
     * @return
     */
    @Override
    public ShapeHolder getHolder() {
        if (draftHolder.getPathX(0) == draftHolder.getPathX(2)
                && draftHolder.getPathY(0) == draftHolder.getPathY(2)) {
            return null;
        } else {
            draftHolder.unbind();

            return draftHolder;
        }
    }

    @Override
    public void mouseDown(MouseEvent e) {
        draftHolder = new PolygonHolder();
        // 初期値設定
        // 左上 index = 0
        draftHolder.addPathX(e.getX());
        draftHolder.addPathY(e.getY());
        // 右上 index = 1
        draftHolder.addPathX(e.getX());
        draftHolder.addPathY(e.getY());
        // 右下 index = 2
        draftHolder.addPathX(e.getX());
        draftHolder.addPathY(e.getY());
        // 左下 index = 3
        draftHolder.addPathX(e.getX());
        draftHolder.addPathY(e.getY());
        // setHolder
        draftLayer.setHolder(draftHolder);
        // SchemaEditorProperties と bind
        draftHolder.bind();
        draftLayer.draw();
    }
    /**
     * 途中経過を draftHolder に入れて draftLayer に描く
     * @param e
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        draftHolder.setPathX(1, e.getX());
        draftHolder.setPathX(2, e.getX());
        draftHolder.setPathY(2, e.getY());
        draftHolder.setPathY(3, e.getY());

        draftLayer.redraw();
    }

    @Override
    public void mouseUp(MouseEvent e) {
        draftLayer.clear();
    }
}
