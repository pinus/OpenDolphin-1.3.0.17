package open.dolphin.impl.scheam.shapeholder;

import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

/**
 * Image を保持する ShapeHolder.
 * 直接 Image をセットしないで，Image の元となる Node をセットする.
 * ShapeHolderBase の translate, scale, rotate は使わずに独自に実装.
 *
 * @author pns
 */
public class ImageHolder extends ShapeHolderBase {
    private Node node;
    private Image image;
    private SnapshotParameters snapshotParameters;
    private double rotate;

    @Override
    public void draw() {
        super.draw();

        double x = getStartX();
        double y = getStartY();
        double w = getEndX() - getStartX();
        double h = getEndY() - getStartY();

        getGraphicsContext().drawImage(image, x, y, w, h);
    }

    public Node getNode() {
        return node;
    }

    /**
     * Image の元になる Node をセットする.
     * StartX と StartY はあらかじめセットしておく必要あり.
     * Node から Snapshot を取って描画のための Image とする.
     *
     * @param n
     */
    public void setNode(Node n) {
        node = n;
        snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        image = node.snapshot(snapshotParameters, null);
        setEndX(getStartX() + image.getWidth());
        setEndY(getStartY() + image.getHeight());
    }

    @Override
    public void translate(double dx, double dy) {
        setStartX(getStartX() + dx);
        setStartY(getStartY() + dy);
        setEndX(getEndX() + dx);
        setEndY(getEndY() + dy);
    }

    @Override
    public void scale(double dx, double dy) {
        setEndX(getEndX() + dx);
        setEndY(getEndY() + dy);
    }

    /**
     * 回転させる場合はその都度 Image を作り直す.
     *
     * @param r
     */
    @Override
    public void rotate(double r) {
        rotate += Math.toDegrees(r);
        Transform t = Transform.rotate(rotate, image.getWidth() / 2, image.getHeight() / 2);
        snapshotParameters.setTransform(t);
        image = node.snapshot(snapshotParameters, null);

        if (r == Math.PI / 2) {
            // RotateEditor から呼ばれる 90度回転
            double lw = getCanvasWidth();

            double startx = lw - getEndY();
            double starty = getStartX();
            double endx = lw - getStartY();
            double endy = getEndX();

            setStartX(startx);
            setStartY(starty);
            setEndX(endx);
            setEndY(endy);

        } else if (r == -Math.PI / 2) {
            // RotateEditor から呼ばれる -90度回転
            double lh = getCanvasHeight();

            double startx = getStartY();
            double starty = lh - getEndX();
            double endx = getEndY();
            double endy = lh - getStartX();

            setStartX(startx);
            setStartY(starty);
            setEndX(endx);
            setEndY(endy);

        } else {
            // 通常回転

        }
    }
}
