package open.dolphin.impl.scheam.stateeditor;

import javafx.scene.input.MouseEvent;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.SchemaLayer;
import open.dolphin.impl.scheam.ShapeHolder;
import open.dolphin.impl.scheam.shapeholder.OvalHolder;

/**
 * Oval を作る StateEditor.
 * Draft は OvalHolder で描いて，
 * 完成品は PolygonHolder に変換してから返す.
 * @author pns
 */
public class OvalEditor extends StateEditorBase {
    // 途中経過を描く Layer と Holder
    private final SchemaLayer draftLayer;
    private OvalHolder draftHolder;

    public OvalEditor(SchemaEditorImpl context) {
        draftLayer = context.getDraftLayer();
    }

    @Override
    public ShapeHolder getHolder() {
        if (draftHolder.getStartX() == draftHolder.getEndX()
                && draftHolder.getStartY() == draftHolder.getEndY()) {
            return null;
        } else {
            draftHolder.unbind();
            // PolygonHolder に変換してから返す
            return draftHolder.getPolygonHolder();
        }
    }

    @Override
    public void mouseDown(MouseEvent e) {
        draftHolder = new OvalHolder();
        // 初期値設定
        draftHolder.setStartX(e.getX()); draftHolder.setStartY(e.getY());
        draftHolder.setEndX(e.getX()); draftHolder.setEndY(e.getY());
        // setHolder すると描画される
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
        draftHolder.setEndX(e.getX()); draftHolder.setEndY(e.getY());
        draftLayer.redraw();
    }

    @Override
    public void mouseUp(MouseEvent e) {
        draftHolder.setEndX(e.getX()); draftHolder.setEndY(e.getY());
        draftLayer.clear();
    }
}
