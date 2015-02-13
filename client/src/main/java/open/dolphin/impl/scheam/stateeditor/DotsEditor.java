package open.dolphin.impl.scheam.stateeditor;

import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.ShapeHolder;
import open.dolphin.impl.scheam.ShapeHolderBounds;
import open.dolphin.impl.scheam.StateManager;
import open.dolphin.impl.scheam.helper.SchemaUtils;
import open.dolphin.impl.scheam.shapeholder.DotsHolder;
import open.dolphin.impl.scheam.shapeholder.PenHolder;

/**
 * 指定した範囲内に点をたくさん打つ StateEditor
 * Option キー，Shit キーで点を増やす
 * @author pns
 */
public class DotsEditor extends PolygonEditorBase {
    // 範囲内に点をいくつ打つか
    private static final double DOTS_PER_WIDTH = 5;
    // 点の間隔 = 指定範囲に DOTS_PER_WIDHT 個の点 / intervalFactor
    private double interval;
    private double intervalFactor = 1;

    public DotsEditor(SchemaEditorImpl context) {
        super(context);
    }
    /**
     * PolygonEditorBase から呼ばれて FeedBack を画面に表示する
     * @param e
     */
    @Override
    public void drawDragFeedback(MouseEvent e) {
        double dotSize = SchemaEditorImpl.getProperties().getLineWidth();
        PenHolder h = getDraftHolder();
        GraphicsContext gc = getDraftHolder().getGraphicsContext();

        ShapeHolderBounds b = getDraftHolder().getBounds();
        double x = b.getMinX();
        double y = b.getMinY();
        double width = b.getWidth();
        double height = b.getHeight();

        //interval = Math.abs((width + height) / 2) / DOTS_PER_WIDTH / intervalFactor;
        interval = 10 / intervalFactor;

        List<Point2D> points = SchemaUtils.getRandomPoints(x, y, width, height, interval);

        for (Point2D p : points) {
            if (gc.isPointInPath(p.getX(), p.getY())) {
                gc.fillOval(p.getX()-dotSize/2, p.getY()-dotSize/2, dotSize, dotSize);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        adjustInterval(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        adjustInterval(e);
    }

    /**
     * Option, Shift キーで点を増やす処理
     * @param e
     */
    private void adjustInterval(KeyEvent e) {
        //intervalFactor = e.isAltDown()? (e.isShiftDown()? 0.5: 2): 1;

        // option key を押すたびにドットが増えて，shift-option なら減る
        if (e.isAltDown()) {
            if (e.isShiftDown()) { intervalFactor *= 0.8; }
            else { intervalFactor *= 1.2; }
        }

        if (StateManager.isMousePressed() || isMultiClickMode()) {
            getDraftLayer().redraw();
            drawDragFeedback(null);
        }
    }

    @Override
    public ShapeHolder getHolder() {
        PenHolder draftHolder = getDraftHolder();
        // ３点以上なければ無視
        if (isMultiClickMode() || draftHolder.getPathSize() < 3) {
            return null;

        } else {
            DotsHolder h = new DotsHolder();

            // random dots の取得
            ShapeHolderBounds b = getDraftHolder().getBounds();
            List<Point2D> points = SchemaUtils.getRandomPoints(b.getMinX(), b.getMinY(), b.getWidth(), b.getHeight(), interval);

            for (Point2D p : points) {
                if (draftHolder.getGraphicsContext().isPointInPath(p.getX(), p.getY())) {
                    h.addPathX(p.getX());
                    h.addPathY(p.getY());
                }
            }
            h.setDotDataSize(h.getPathSize());
            // 輪郭をセット
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
