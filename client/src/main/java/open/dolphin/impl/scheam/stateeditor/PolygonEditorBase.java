package open.dolphin.impl.scheam.stateeditor;

import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.SchemaLayer;
import open.dolphin.impl.scheam.ShapeHolder;
import open.dolphin.impl.scheam.helper.SchemaUtils;
import open.dolphin.impl.scheam.shapeholder.PolygonDraftHolder;

/**
 * ドラッグの軌跡を DraftLayer に描画する StateEditor の abstract.
 * PolygonEditor, DotsEditor, NetEditor で使う.
 * クリックすれば多角形の頂点を次々とセットできる (MultiClick Mode）.
 * 同じ点をクリックするか，最初の点に戻れば MultiClick Mode 終了.
 * ドラッグ中は drawDragFeedback を呼ぶ.
 *
 * @author pns
 */
public abstract class PolygonEditorBase extends StateEditorBase {
    // DraftLayer
    private final SchemaLayer draftLayer;
    // 下書きは PolygonDraftHolder に描く
    private PolygonDraftHolder draftHolder;
    // Multiclick Mode
    private boolean multiClickMode = false;

    public PolygonEditorBase(SchemaEditorImpl context) {
        draftLayer = context.getDraftLayer();
    }

    public SchemaLayer getDraftLayer() {
        return draftLayer;
    }

    public PolygonDraftHolder getDraftHolder() {
        return draftHolder;
    }

    public boolean isMultiClickMode() {
        return multiClickMode;
    }

    @Override
    public void mouseDown(MouseEvent e) {
        if (!multiClickMode) {
            // 最初に入るときは Multiclick mode ではない
            // 下書き用の設定
            draftHolder = new PolygonDraftHolder();
            draftHolder.setLineColor(Color.WHITE);
            draftHolder.setFillColor(Color.WHITE);
            draftHolder.setLineWidth(1.0);
            // setHolder の前に座標を決めなければならない
            draftHolder.addPathX(e.getX());
            draftHolder.addPathY(e.getY());

            draftLayer.setBlendMode(BlendMode.DIFFERENCE);
            draftLayer.setHolder(draftHolder);
            draftLayer.draw();

            multiClickMode = true;

        } else {
            draftHolder.addPathX(e.getX());
            draftHolder.addPathY(e.getY());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // drag されれば，MulticlickMode ではない
        multiClickMode = false;
        // mouse move で描く hover 座標はリセット
        draftHolder.setHoverX(-1);
        draftHolder.setHoverY(-1);
        draftHolder.addPathX(e.getX());
        draftHolder.addPathY(e.getY());
        // これは外枠を描く
        draftLayer.redraw();
        // ドラッグ中の中身の Feedback を描く
        drawDragFeedback(e);
    }

    public void drawDragFeedback(MouseEvent e) {
    }

    @Override
    public void mouseUp(MouseEvent e) {
        if (multiClickMode) {
            int size = draftHolder.getPathSize();
            if (size > 1) {
                double firstx = draftHolder.getPathX(0);
                double firsty = draftHolder.getPathY(0);
                double lastx = draftHolder.getPathX(size - 2);
                double lasty = draftHolder.getPathY(size - 2);
                // 最初の点か，最後の点の近くなら終了とする
                if (SchemaUtils.isNear(lastx, lasty, e.getX(), e.getY())
                        || SchemaUtils.isNear(firstx, firsty, e.getX(), e.getY())) {
                    draftLayer.clear();
                    multiClickMode = false;
                }
            }

        } else {
            draftHolder.addPathX(e.getX());
            draftHolder.addPathY(e.getY());
            draftLayer.clear();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // multi click mode では，次にクリックされるまで，マウスの動きに合わせてガイドラインを出す
        if (multiClickMode) {
            // ガイドラインを描くための hover 座標
            draftHolder.setHoverX(e.getX());
            draftHolder.setHoverY(e.getY());
            // 外枠を描く
            draftLayer.redraw();
            // MouseMove 中の Feedback を描く
            drawDragFeedback(e);

        } else {
            if (draftHolder != null) {
                draftHolder.setHoverX(-1);
                draftHolder.setHoverY(-1);
            }
        }
    }

    @Override
    public void end() {
        draftLayer.clear();
        draftLayer.setBlendMode(null);
        multiClickMode = false;
    }

    @Override
    public abstract ShapeHolder getHolder();
}
