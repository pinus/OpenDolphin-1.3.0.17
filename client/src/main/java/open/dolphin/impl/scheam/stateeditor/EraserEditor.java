package open.dolphin.impl.scheam.stateeditor;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.SnapshotParametersBuilder;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.EllipseBuilder;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.SchemaEditorProperties;
import open.dolphin.impl.scheam.SchemaLayer;
import open.dolphin.impl.scheam.ShapeHolder;
import open.dolphin.impl.scheam.constant.Const;
import open.dolphin.impl.scheam.shapeholder.EraserHolder;
import open.dolphin.impl.scheam.shapeholder.OvalHolder;

/**
 * 不透明の白で上書きする StateEditor
 * 直径は lineWidth の ERASER_WIDTH_FACTOR 倍
 * @author pns
 */
public class EraserEditor extends StateEditorBase {
    private final SchemaEditorProperties properties;
    private final SchemaLayer draftLayer;
    // Eraser カーソルの直径
    private final DoubleProperty diameter;
    // Eraser カーソルの色
    private final Color eraserFillColor;
    private final Color eraserStrokeColor;

    private EraserHolder draftHolder;
    // Drag されたのか Click されたのか判定フラグ
    private boolean dragged = false;

    public EraserEditor(SchemaEditorImpl context) {
        properties = context.getProperties();
        draftLayer = context.getDraftLayer();
        diameter = new SimpleDoubleProperty();
        diameter.bind(properties.lineWidthProperty().multiply(Const.ERASER_WIDTH_FACTOR));
        eraserFillColor = Color.color(1.0, 1.0, 1.0);
        eraserStrokeColor = Color.color(0, 0, 0, 0.7);
    }

    @Override
    public void start() {
        // カーソル設定
        draftLayer.cursorProperty().bind(new ObjectBinding<Cursor>(){
            { super.bind(diameter); }
            @Override
            protected Cursor computeValue() {
                double r = diameter.get()/2;
                Ellipse e = EllipseBuilder.create().radiusX(r).radiusY(r).centerX(r).centerY(r).
                    fill(eraserFillColor).stroke(eraserStrokeColor).build();
                SnapshotParameters parameters = SnapshotParametersBuilder.create().fill(Color.TRANSPARENT).build();
                Image img = e.snapshot(parameters, null);

                return new ImageCursor(img, r+1, r+1);
            }
        });
    }

    @Override
    public void end() {
        draftLayer.cursorProperty().unbind();
        draftLayer.setCursor(null);
    }

    @Override
    public void mouseDown(MouseEvent e) {
        dragged = false;

        draftHolder = new EraserHolder();
        draftHolder.setProperties();
        // 色を不透明白で上書き
        draftHolder.setLineColor(Color.WHITE);
        draftHolder.setFillColor(Color.WHITE);
        draftHolder.setFillBlur(0);

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

        if (size == 0) { return null; }

        // 点が１つの時は　LineWidth 大の Oval を返す
        // 色は fillColor になる
        if (size == 1) {
            double r = diameter.get()/2;
            double x = draftHolder.getPathX(0);
            double y = draftHolder.getPathY(0);
            OvalHolder o = new OvalHolder();
            o.setStartX(x-r); o.setStartY(y-r);
            o.setEndX(x+r); o.setEndY(y+r);
            o.setProperties();
            // 色を上書き
            o.setLineColor(Color.WHITE);
            o.setFillColor(Color.WHITE);
            o.setFillBlur(0);
            return o;
        }

        // ２点というのは普通あり得ない
        if (size == 2) {
            return null;
        }

        // ３点以上からなる Path の場合
        return draftHolder;
    }
}
