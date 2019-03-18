package open.dolphin.impl.scheam;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Bounds of ShapeHolders.
 * 開始座標(startx, starty), 終了座標(endx, endy), ぼかしの程度 blur (0.0-1.0) をセットして使う.
 *
 * @author pns
 */
public class ShapeHolderBounds {
    // 設定可能なプロパティ
    private final DoubleProperty startx = new SimpleDoubleProperty();
    private final DoubleProperty starty = new SimpleDoubleProperty();
    private final DoubleProperty endx = new SimpleDoubleProperty();
    private final DoubleProperty endy = new SimpleDoubleProperty();
    private final DoubleProperty blur = new SimpleDoubleProperty();
    // 読み出し専用のプロパティ
    private final ReadOnlyDoubleWrapper minx = new ReadOnlyDoubleWrapper();
    private final ReadOnlyDoubleWrapper miny = new ReadOnlyDoubleWrapper();
    private final ReadOnlyDoubleWrapper maxx = new ReadOnlyDoubleWrapper();
    private final ReadOnlyDoubleWrapper maxy = new ReadOnlyDoubleWrapper();
    private final ReadOnlyDoubleWrapper width = new ReadOnlyDoubleWrapper();
    private final ReadOnlyDoubleWrapper height = new ReadOnlyDoubleWrapper();
    private final ReadOnlyDoubleWrapper minSpan = new ReadOnlyDoubleWrapper();
    private final ReadOnlyDoubleWrapper maxSpan = new ReadOnlyDoubleWrapper();
    private final ReadOnlyDoubleWrapper blurRadius = new ReadOnlyDoubleWrapper();

    // binds
    {
        minx.bind(new DoubleBinding() {
            {
                super.bind(startx, endx);
            }

            @Override
            protected double computeValue() {
                return Math.min(startx.get(), endx.get());
            }
        });
        miny.bind(new DoubleBinding() {
            {
                super.bind(starty, endy);
            }

            @Override
            protected double computeValue() {
                return Math.min(starty.get(), endy.get());
            }
        });
        maxx.bind(new DoubleBinding() {
            {
                super.bind(startx, endx);
            }

            @Override
            protected double computeValue() {
                return Math.max(startx.get(), endx.get());
            }
        });
        maxy.bind(new DoubleBinding() {
            {
                super.bind(starty, endy);
            }

            @Override
            protected double computeValue() {
                return Math.max(starty.get(), endy.get());
            }
        });
        width.bind(maxx.subtract(minx));
        height.bind(maxy.subtract(miny));
        minSpan.bind(new DoubleBinding() {
            {
                super.bind(width, height);
            }

            @Override
            protected double computeValue() {
                return Math.min(width.get(), height.get());
            }
        });
        maxSpan.bind(new DoubleBinding() {
            {
                super.bind(width, height);
            }

            @Override
            protected double computeValue() {
                return Math.max(width.get(), height.get());
            }
        });
        blurRadius.bind(minSpan.multiply(blur));
    }

    /**
     * 開始点の X 座標.
     *
     * @param x
     */
    public void setStartX(double x) {
        startx.set(x);
    }

    /**
     * 開始点の Y 座標.
     *
     * @param y
     */
    public void setStartY(double y) {
        starty.set(y);
    }

    /**
     * 終了点の X 座標.
     *
     * @param x
     */
    public void setEndX(double x) {
        endx.set(x);
    }

    /**
     * 終了点の Y 座標.
     *
     * @param y
     */
    public void setEndY(double y) {
        endy.set(y);
    }

    /**
     * ぼかしの程度をセットする.
     *
     * @param b
     */
    public void setBlur(double b) {
        blur.set(b);
    }

    /**
     * 開始点と終了点のうち，小さい方の X 座標.
     *
     * @return
     */
    public double getMinX() {
        return minx.get();
    }

    /**
     * 開始点と終了点のうち，小さい方の Y 座標.
     *
     * @return
     */
    public double getMinY() {
        return miny.get();
    }

    /**
     * 開始点と終了点のうち，大きい方の X 座標.
     *
     * @return
     */
    public double getMaxX() {
        return maxx.get();
    }

    /**
     * 開始点と終了点のうち，大きい方の Y 座標.
     *
     * @return
     */
    public double getMaxY() {
        return maxy.get();
    }

    /**
     * 開始点と終了点から幅を計算.
     *
     * @return
     */
    public double getWidth() {
        return width.get();
    }

    /**
     * 開始点と終了点から高さを計算.
     *
     * @return
     */
    public double getHeight() {
        return height.get();
    }

    /**
     * 幅と高さの小さい方を返す.
     *
     * @return
     */
    public double getMinSpan() {
        return minSpan.get();
    }

    /**
     * 幅と高さの大きい方を返す.
     *
     * @return
     */
    public double getMaxSpan() {
        return maxSpan.get();
    }

    /**
     * ぼやけの程度 blur から blur radius を計算して返す.
     * 幅と高さの小さい方に blur 係数を掛けたものを blur radius としている.
     *
     * @return
     */
    public double getBlurRadius() {
        return blurRadius.get();
    }

    /**
     * 点 (x,y) がこの Bounds に含まれるかどうか.
     * Bounds の幅や高さが 6 未満の場合は，外側 6 ドットの近傍なら含まれると判定する.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean contains(double x, double y) {
        double minX = getMinX();
        double maxX = getMaxX();
        double minY = getMinY();
        double maxY = getMaxY();
        // あまり小さいとつかめないので，近ければ contains とする
        minX -= 6;
        maxX += 6;
        minY -= 6;
        maxY += 6;

        return (minX < x) && (x < maxX) && (minY < y) && (y < maxY);
    }

    // 外からの bind 用
    public DoubleProperty startXProperty() {
        return startx;
    }

    public DoubleProperty startYProperty() {
        return starty;
    }

    public DoubleProperty endXProperty() {
        return endx;
    }

    public DoubleProperty endYProperty() {
        return endy;
    }

    public DoubleProperty blurProperty() {
        return blur;
    }

    public ReadOnlyDoubleWrapper minXProperty() {
        return minx;
    }

    public ReadOnlyDoubleWrapper minYProperty() {
        return miny;
    }

    public ReadOnlyDoubleWrapper maxXProperty() {
        return maxx;
    }

    public ReadOnlyDoubleWrapper maxYProperty() {
        return maxy;
    }

    public ReadOnlyDoubleWrapper widthProperty() {
        return width;
    }

    public ReadOnlyDoubleWrapper heightProperty() {
        return height;
    }

    public ReadOnlyDoubleWrapper minSpanProperty() {
        return minSpan;
    }

    public ReadOnlyDoubleWrapper maxSpanProperty() {
        return maxSpan;
    }

    public ReadOnlyDoubleWrapper blurRadiusProperty() {
        return blurRadius;
    }
}
