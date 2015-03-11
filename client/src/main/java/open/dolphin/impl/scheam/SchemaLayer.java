package open.dolphin.impl.scheam;

import javafx.scene.canvas.Canvas;

/**
 * SchemaHolder を保持する Canvas.
 * 現在のところ，保持できる Holder は１つのみ.
 * @author pns
 */
public class SchemaLayer extends Canvas {
    /** この Layer が保持する ShapeHolder */
    private ShapeHolder holder;

    public SchemaLayer() {
    }

    /**
     * 保持している Holder を draw する.
     */
    public void draw() { holder.draw(); }
    /**
     * このキャンバス上の全ての描画をクリアする.
     */
    public void clear() {
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
    }
    /**
     * 再描画 = clear + draw.
     */
    public void redraw() {
        clear(); draw();
    }

    public ShapeHolder getHolder() { return holder; }

    /**
     * Holder をセットする.
     * @param h
     */
    public void setHolder(final ShapeHolder h) {
        holder = h;
        holder.setGraphicsContext(getGraphicsContext2D());
    }
}
