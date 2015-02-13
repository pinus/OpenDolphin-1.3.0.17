package open.dolphin.impl.scheam.stateeditor;

import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.ShapeHolder;
import open.dolphin.impl.scheam.shapeholder.PenHolder;
import open.dolphin.impl.scheam.shapeholder.PolygonHolder;

/**
 * ドラッグのとおりに描画し，最後に Path を Close する StateEditor
 * @author pns
 */
public class PolygonEditor extends PolygonEditorBase {

    public PolygonEditor(SchemaEditorImpl context) {
        super(context);
    }

    @Override
    public ShapeHolder getHolder() {
        PenHolder draftHolder = getDraftHolder();

        // ３点以上なければ無視
        if (isMultiClickMode() || draftHolder.getPathSize() < 3) {
            return null;

        } else {
            PolygonHolder h = new PolygonHolder();
            // path のコピー
            for (int i=0; i<draftHolder.getPathSize(); i++) {
                h.addPathX(draftHolder.getPathX(i));
                h.addPathY(draftHolder.getPathY(i));
            }
            // properties をコピー
            h.setProperties();

            return h;
        }
    }
}
