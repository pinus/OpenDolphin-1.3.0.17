package open.dolphin.impl.scheam.shapeholder;

import javafx.scene.canvas.GraphicsContext;
import open.dolphin.impl.scheam.FillMode;
import open.dolphin.impl.scheam.helper.SchemaUtils;

/**
 * 網を保持する ShapeHolder.
 * データ形式
 * Path(0) - line - Path(1)
 * Path(2) - line - Path(3)
 *   :
 * Path(2n) - line - Path(2n+1)
 * ２つの Path Pair で線を表し，2n+1 = netDataSize とする.
 * netDataSize の後に，輪郭の Path Data が入る.
 * @author pns
 */
public class NetHolder extends ShapeHolderBase {
    private int netDataSize;

    public void setNetDataSize(int s) { netDataSize = s; }
    public int getNetDataSize() { return netDataSize; }

    @Override
    public void draw() {
        super.draw();

        GraphicsContext gc = getGraphicsContext();

        // FillMode に応じて描画
        if (getFillMode() != FillMode.Line) {
            gc.setStroke(getFillColor());
            for (int n=0; n<netDataSize; n+=2) {
                gc.strokeLine(getPathX(n), getPathY(n), getPathX(n+1), getPathY(n+1));
            }
        }
        // NetData の後に輪郭データを載せてある
        if (getFillMode() != FillMode.Fill) {
            gc.setStroke(getLineColor());
            gc.beginPath();
            gc.moveTo(getPathX(netDataSize), getPathY(netDataSize));
            for (int i=netDataSize+1; i<getPathSize(); i++) {
                gc.lineTo(getPathX(i), getPathY(i));
            }
            gc.closePath();
            gc.stroke();
        }
    }

    @Override
    public boolean contains(double x, double y) {
        // bound 内でかつどれかの線と近ければ contains と判断
        if (getBounds().contains(x, y)) {
            for (int n=0; n<getPathSize(); n+=2) {
                double ux = getPathX(n+1) - getPathX(n);
                double uy = getPathY(n+1) - getPathY(n);
                double vx = x - getPathX(n);
                double vy = y - getPathY(n);
                if (SchemaUtils.getDistance(ux, uy, vx, vy) < 10) { return true; }
            }
        }
        return false;
    }
}
