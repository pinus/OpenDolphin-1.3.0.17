package open.dolphin.impl.scheam.stateeditor;

import javafx.scene.input.MouseEvent;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.SchemaLayer;
import open.dolphin.impl.scheam.ShapeHolder;
import open.dolphin.impl.scheam.shapeholder.OvalHolder;
import open.dolphin.impl.scheam.shapeholder.PenHolder;

/**
 * マウスドラッグで自由に線を引く StateEditor.
 * クリックされた場合は点を打つ.
 *
 * @author pns
 */
public class PenEditor extends StateEditorBase {
    private final SchemaLayer draftLayer;
    private PenHolder draftHolder;
    // Drag されたのか Click されたのか判定フラグ
    private boolean dragged = false;

    public PenEditor(SchemaEditorImpl context) {
        draftLayer = context.getDraftLayer();
    }

    @Override
    public void mouseDown(MouseEvent e) {
        dragged = false;

        draftHolder = new PenHolder();
        draftHolder.bind();
        draftHolder.addPathX(e.getX());
        draftHolder.addPathY(e.getY());
        draftLayer.setHolder(draftHolder);
        draftLayer.draw();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        dragged = true;

        draftHolder.addPathX(e.getX());
        draftHolder.addPathY(e.getY());
        draftLayer.redraw();
    }

    @Override
    public void mouseUp(MouseEvent e) {
        // dragged でなければ click されたということなのでそのまま終了
        if (dragged) {
            draftHolder.addPathX(e.getX());
            draftHolder.addPathY(e.getY());
        }
        draftLayer.clear();
    }

    @Override
    public ShapeHolder getHolder() {
        int size = draftHolder.getPathSize();

        if (size == 0) {
            return null;
        }

        // 点が１つの時は　LineWidth 大の Oval を返す
        // 色は fillColor になる
        if (size == 1) {
            double d = draftHolder.getLineWidth() / 2.0;
            double x = draftHolder.getPathX(0);
            double y = draftHolder.getPathY(0);
            OvalHolder o = new OvalHolder();
            o.setStartX(x - d);
            o.setStartY(y - d);
            o.setEndX(x + d);
            o.setEndY(y + d);
            o.setProperties();
            //o.setFillColor(draftHolder.getLineColor());
            //o.setLineColor(Color.TRANSPARENT);

            return o;
        }

        // ２点というのは普通あり得ない
        if (size == 2) {
            return null;
        }

        // ３点以上からなる Path の場合
        draftHolder.unbind();

        return draftHolder;
    }
}
