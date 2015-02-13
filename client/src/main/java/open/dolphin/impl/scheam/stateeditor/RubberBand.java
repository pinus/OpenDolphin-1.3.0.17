package open.dolphin.impl.scheam.stateeditor;

import javafx.scene.canvas.GraphicsContext;
import open.dolphin.impl.scheam.SchemaLayer;
import open.dolphin.impl.scheam.State;
import open.dolphin.impl.scheam.helper.SchemaUtils;
import open.dolphin.impl.scheam.shapeholder.ShapeHolderBase;

/**
 * Translate Editor で選択した ShapeHolder を囲む RubberBand
 * Bounds を線で囲んで，四隅に小さい四角形を出す
 *
 * @author pns
 */
public class RubberBand {
    private final SchemaLayer draftLayer;
    private ShapeHolderBase holder;

    private boolean isLine;
    /**
     * Band の位置
     * xSyS +--------+ xEyS
     *      |        |
     * xSyE +--------+ xEyE
     * StartX, StartY... の座標で対応するので四隅がこの順番とは限らない
     */
    public enum Pos { xSyS, xSyE, xEyS, xEyE }

    public RubberBand(SchemaLayer d) {
        draftLayer = d;
    }

    /**
     * 対象となる ShemaLayer をセットする
     * 選択された対象がない場合は null をセットする
     * @param h
     */
    public void setHolder(ShapeHolderBase h) {
        holder =h;
        if (h != null) {
            isLine = holder.getState().equals(State.Line);
        }
    }

    public ShapeHolderBase getHolder() { return holder; }

    public void redraw() {
        draftLayer.clear();
        GraphicsContext gc = draftLayer.getGraphicsContext2D();

        if (isLine) {
            // line の場合は anchor の小さい四角だけ出す
            drawAnchorRect(gc, holder.getPathX(0), holder.getPathY(0));
            drawAnchorRect(gc, holder.getPathX(1), holder.getPathY(1));

        } else {
            // line 以外では外周に四角を表示
            // hairline 表示のためには 0.5 ドットずらす必要がある
            gc.beginPath();
            gc.moveTo(holder.getStartX()-0.5, holder.getStartY()-0.5);
            gc.lineTo(holder.getEndX()+0.5, holder.getStartY()-0.5);
            gc.lineTo(holder.getEndX()+0.5, holder.getEndY()+0.5);
            gc.lineTo(holder.getStartX()-0.5, holder.getEndY()+0.5);
            gc.closePath();
            gc.stroke();

            drawAnchorRect(gc, holder.getStartX(), holder.getStartY());
            drawAnchorRect(gc, holder.getStartX(), holder.getEndY());
            drawAnchorRect(gc, holder.getEndX(), holder.getStartY());
            drawAnchorRect(gc, holder.getEndX(), holder.getEndY());
        }
    }

    /**
     * 小さい Rectangle を描く
     * @param gc
     */
    private void drawAnchorRect(GraphicsContext gc, double x, double y) {
        gc.strokeRect(x-2.5, y-2.5, 5, 5);
    }

    public boolean isLine() { return isLine; }

    /**
     * x, y 座標が RubberBand のどこを指しているかを返す
     * @param x
     * @param y
     * @return
     */
    public Pos getPos(double x, double y) {
        if (isLine) {
            if (SchemaUtils.isNear(x, y, holder.getPathX(0), holder.getPathY(0))) { return Pos.xSyS; }
            if (SchemaUtils.isNear(x, y, holder.getPathX(1), holder.getPathY(1))) { return Pos.xEyE; }
        }
        if (SchemaUtils.isNear(x, y, holder.getStartX(), holder.getStartY())) { return Pos.xSyS; }
        if (SchemaUtils.isNear(x, y, holder.getEndX(), holder.getEndY())) { return Pos.xEyE; }
        if (SchemaUtils.isNear(x, y, holder.getStartX(), holder.getEndY())) { return Pos.xSyE; }
        if (SchemaUtils.isNear(x, y, holder.getEndX(), holder.getStartY())) { return Pos.xEyS; }
        return null;
    }

    /**
     * band 内に点が含まれているかどうか
     * band がかかっていない場合（holder == null）なら false
     * band がかかっていて line 以外の時：bound に入っていれば true
     * band がかかっていて line の時：line の contains で判定
     * @param x
     * @param y
     * @return
     */
    public boolean contains(double x, double y) {
        if (holder == null) { return false; }

        if (isLine) {
            return holder.contains(x, y);
        } else {
            return holder.getBounds().contains(x, y);
        }
    }
}
