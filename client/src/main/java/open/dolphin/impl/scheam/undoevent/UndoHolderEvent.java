package open.dolphin.impl.scheam.undoevent;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import open.dolphin.impl.scheam.FillMode;
import open.dolphin.impl.scheam.SchemaLayer;
import open.dolphin.impl.scheam.UndoEvent;
import open.dolphin.impl.scheam.shapeholder.ShapeHolderBase;

/**
 *
 * @author pns
 */
public final class UndoHolderEvent implements UndoEvent {
    private final SchemaLayer layer;
    private final ShapeHolderBase holder;

    private double startx, starty, endx, endy;
    private final List<Double> pathx, pathy;
    private double translatex, translatey, scalex, scaley;
    private Color lineColor, fillColor;
    private double lineWidth, fillBlur;
    private FillMode fillMode;

    public UndoHolderEvent (ShapeHolderBase h) {
        holder = h;
        layer = (SchemaLayer) holder.getGraphicsContext().getCanvas();
        pathx = new ArrayList<>();
        pathy = new ArrayList<>();

        // Holder からパラメータを吸い取って保存する
        copyFrom(h);
    }

    /**
     * パラメータを入れ変えて描画する.
     */
    @Override
    public void rollback() {
        // 現在の Holder のパラメータを保存
        ShapeHolderBase tmpHolder = new ShapeHolderBase() {};
        UndoHolderEvent tmp = new UndoHolderEvent(holder);
        tmp.copyTo(tmpHolder);
        // Holder のパラメータを保存していたもので置き換える → rollback
        copyTo(holder);
        // tmp に入れておいた現在の Holder のパラメータを保存する
        copyFrom(tmpHolder);

        // 必要な描画までする
        layer.redraw();
    }

    /**
     * Holder からパラメータを吸い取る.
     * @param src
     */
    public void copyFrom(ShapeHolderBase src) {
        lineWidth = src.lineWidthProperty().get();
        lineColor = src.lineColorProperty().get();
        fillColor = src.fillColorProperty().get();
        fillBlur = src.fillBlurProperty().get();
        fillMode = src.fillModeProperty().get();

        startx = src.startXProperty().get();
        starty = src.startYProperty().get();
        endx = src.endXProperty().get();
        endy = src.endYProperty().get();

        pathx.clear();
        pathx.addAll(src.pathXList());
        pathy.clear();
        pathy.addAll(src.pathYList());

        translatex = src.translateXProperty().get();
        translatey = src.translateYProperty().get();
        scalex = src.scaleXProperty().get();
        scaley = src.scaleYProperty().get();
    }

    /**
     * dist Holder にパラメータをコピーする.
     * @param dist
     */
    public void copyTo(ShapeHolderBase dist) {
        dist.lineWidthProperty().set(lineWidth);
        dist.lineColorProperty().set(lineColor);
        dist.fillColorProperty().set(fillColor);
        dist.fillBlurProperty().set(fillBlur);
        dist.fillModeProperty().set(fillMode);

        dist.startXProperty().set(startx);
        dist.startYProperty().set(starty);
        dist.endXProperty().set(endx);
        dist.endYProperty().set(endy);

        dist.pathXList().clear();
        dist.pathXList().addAll(pathx);
        dist.pathYList().clear();
        dist.pathYList().addAll(pathy);

        dist.translateXProperty().set(translatex);
        dist.translateYProperty().set(translatey);
        dist.scaleXProperty().set(scalex);
        dist.scaleYProperty().set(scaley);
    }
}
