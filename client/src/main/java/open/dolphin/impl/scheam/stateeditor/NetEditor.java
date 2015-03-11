package open.dolphin.impl.scheam.stateeditor;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.ShapeHolder;
import open.dolphin.impl.scheam.ShapeHolderBounds;
import open.dolphin.impl.scheam.StateManager;
import open.dolphin.impl.scheam.shapeholder.NetHolder;
import open.dolphin.impl.scheam.shapeholder.PenHolder;

/**
 * 指定された範囲に網を描く StateEditor.
 * @author pns
 */
public class NetEditor extends PolygonEditorBase {
    private static final double LINES_PER_WIDTH = 3;
    // 網の間隔 = 指定範囲に LINES_PER_WIDHT 本 / intervalFactor
    private double interval;
    private double intervalFactor = 1;

    public NetEditor(SchemaEditorImpl context) {
        super(context);
    }

    /**
     * PolygonEditorBase から呼ばれて画面に FeedBack を表示する.
     * @param e
     */
    @Override
    public void drawDragFeedback(MouseEvent e) {
        ShapeHolderBounds b = getDraftHolder().getBounds();
        GraphicsContext gc = getDraftHolder().getGraphicsContext();
        double width = b.getWidth();
        double height = b.getHeight();
        interval = Math.abs((width + height) / 2) / LINES_PER_WIDTH / intervalFactor;

        List<Point2D> list = getNet();
        for (int n=0; n<list.size(); n+=2) {
            Point2D p1 = list.get(n);
            Point2D p2 = list.get(n+1);

            gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
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
     * Option, Shift キーで網を増やす処理.
     * @param e
     */
    private void adjustInterval(KeyEvent e) {
        intervalFactor = e.isAltDown()? (e.isShiftDown()? 0.8: 1.5): 1;
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
            NetHolder h = new NetHolder();
            // 網をセット
            List<Point2D> netData = getNet();
            netData.forEach(p -> {
                h.addPathX(p.getX());
                h.addPathY(p.getY());
            });
            h.setNetDataSize(netData.size());
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

    /**
     * 網の座標を作る.
     * Path(0) - line - Path(1)
     * Path(2) - line - Path(3)
     *   :
     * Path(2n) - line - Path(2n+1)
     * という line の集合体として表す
     *
     * @return
     */
    private List<Point2D> getNet() {
        List<Point2D> list = new ArrayList<>();
        ShapeHolderBounds b = getDraftHolder().getBounds();
        GraphicsContext gc = getDraftHolder().getGraphicsContext();

        double minx = b.getMinX();
        double maxx = b.getMaxX();
        double miny = b.getMinY();
        double maxy = b.getMaxY();

        // 右斜め下方向
        for (double x = minx; x < maxx; x += interval) {
            double y = miny;
            double d = 0;

            boolean found = false;
            while(x + d < maxx && y + d < maxy) {
                if (! found && gc.isPointInPath(x + d, y + d)) {
                    found = true;
                    list.add(new Point2D(x + d, y + d));
                }
                if (found && ! gc.isPointInPath(x + d, y + d)) {
                    found = false;
                    list.add(new Point2D(x + d, y + d));
                }
                d++;
            }
            if (found) {
                list.add(new Point2D(x + d, y + d));
            }
        }

        for (double y = miny + interval; y < maxy; y += interval) {
            double x = minx;
            double d = 0;

            boolean found = false;
            while(x + d < maxx && y + d < maxy) {
                if (! found && gc.isPointInPath(x + d, y + d)) {
                    found = true;
                    list.add(new Point2D(x + d, y + d));
                }
                if (found && ! gc.isPointInPath(x + d, y + d)) {
                    found = false;
                    list.add(new Point2D(x + d, y + d));
                }
                d++;
            }
            if (found) {
                list.add(new Point2D(x + d, y + d));
            }
        }

        // 左斜め下方向
        for (double x = maxx; x > minx; x -= interval) {
            double y = miny;
            double d = 0;

            boolean found = false;
            while(x - d > minx && y + d < maxy) {
                if (! found && gc.isPointInPath(x - d, y + d)) {
                    found = true;
                    list.add(new Point2D(x - d, y + d));
                }
                if (found && ! gc.isPointInPath(x - d, y + d)) {
                    found = false;
                    list.add(new Point2D(x - d, y + d));
                }
                d++;
            }
            if (found) {
                list.add(new Point2D(x - d, y + d));
            }
        }

        for (double y = miny + interval; y < maxy; y += interval) {
            double x = maxx;
            double d = 0;

            boolean found = false;
            while(x - d > minx && y + d < maxy) {
                if (! found && gc.isPointInPath(x - d, y + d)) {
                    found = true;
                    list.add(new Point2D(x - d, y + d));
                }
                if (found && ! gc.isPointInPath(x - d, y + d)) {
                    found = false;
                    list.add(new Point2D(x - d, y + d));
                }
                d++;
            }
            if (found) {
                list.add(new Point2D(x - d, y + d));
            }
        }

        return list;
    }
}
