package open.dolphin.impl.scheam;

import javafx.scene.canvas.GraphicsContext;

/**
 * SchemaLayer にセットする Holder の Interface
 * @author pns
 */
public interface ShapeHolder {
    /**
     * GraphicsContext をセットする
     * @param gc
     */
    public void setGraphicsContext(GraphicsContext gc);
    /**
     * Shape を描く
     */
    public void draw();
    /**
     * Shape が点 (x,y) を含んでいるかどうか
     * @param x
     * @param y
     * @return
     */
    public boolean contains(double x, double y);
    /**
     * Shape を (dx,dy) だけ移動する
     * @param dx
     * @param dy
     */
    public void translate(double dx, double dy);
    /**
     * Shape を theta 度だけ回転する
     * @param theta
     */
    public void rotate(double theta);
    /**
     * Shape を (dx,dy) だけ拡大・縮小する
     * @param dx
     * @param dy
     */
    public void scale(double dx, double dy);
}
