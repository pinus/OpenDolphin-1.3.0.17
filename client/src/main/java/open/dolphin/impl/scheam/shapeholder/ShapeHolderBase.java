package open.dolphin.impl.scheam.shapeholder;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.transform.Affine;
import open.dolphin.impl.scheam.*;
import open.dolphin.impl.scheam.helper.SchemaUtils;

import java.util.Objects;

/**
 * ShapeHolder の Base となる abstract で，変数と Bind を管理する.
 * 各 ShapeHolder はこれを extend して，各種パラメータを取得しつつ，draw を Override して描画する.
 * @author pns
 */
public abstract class ShapeHolderBase implements ShapeHolder {
    private final SchemaEditorProperties properties = SchemaEditorImpl.getProperties();
    // When true, indicates the current value of this Holder is changing.
    private final BooleanProperty valueChanging = new SimpleBooleanProperty();
    // 親の SchemaLayer
    private SchemaLayer parentLayer;
    // この Holder を redraw させるべきプロパティー
    private final Property[] propertiesToRedraw = {
        properties.lineWidthProperty(),
        properties.lineColorProperty(),
        properties.fillColorProperty(),
        properties.fillBlurProperty(),
        properties.fillModeProperty()
    };
    // redraw させる Listener
    private final RedrawListener redrawListener = new RedrawListener();

    private final ShapeHolderBounds bounds = new ShapeHolderBounds();
    private final GaussianBlur gaussianBlur = new GaussianBlur();
    private final Affine invert = new Affine();
    // この Holder の State を保持
    private State state = properties.getState();

    // properties
    // 開始点
    private final DoubleProperty startx = new SimpleDoubleProperty();
    private final DoubleProperty starty = new SimpleDoubleProperty();
    // 開始点の変換されたもの（実表示される開始点）
    private final DoubleProperty dispstartx = new SimpleDoubleProperty();
    private final DoubleProperty dispstarty = new SimpleDoubleProperty();
    private final DoubleProperty endx = new SimpleDoubleProperty();
    private final DoubleProperty endy = new SimpleDoubleProperty();
    private final DoubleProperty dispendx = new SimpleDoubleProperty();
    private final DoubleProperty dispendy = new SimpleDoubleProperty();
    private final ObservableList<Double> pathx = FXCollections.observableArrayList();
    private final ObservableList<Double> pathy = FXCollections.observableArrayList();
    private final DoubleProperty lineWidth = new SimpleDoubleProperty();
    private final ObjectProperty<Color> lineColor = new SimpleObjectProperty<>();
    private final ObjectProperty<Color> fillColor = new SimpleObjectProperty<>();
    private final DoubleProperty fillBlur = new SimpleDoubleProperty();
    private final ObjectProperty<FillMode> fillMode = new SimpleObjectProperty<>();
    private final ObjectProperty<GraphicsContext> graphicsContext = new SimpleObjectProperty<>();

    public DoubleProperty startXProperty() { return startx; }
    public DoubleProperty startYProperty() { return starty; }
    public DoubleProperty endXProperty() { return endx; }
    public DoubleProperty endYProperty() { return endy; }
    public ObservableList<Double> pathXList() { return pathx; }
    public ObservableList<Double> pathYList() { return pathy; }
    public DoubleProperty lineWidthProperty() { return lineWidth; }
    public ObjectProperty<Color> lineColorProperty() { return lineColor; }
    public ObjectProperty<Color> fillColorProperty() { return fillColor; }
    public DoubleProperty fillBlurProperty() { return fillBlur; }
    public ObjectProperty<FillMode> fillModeProperty() { return fillMode; }
    public ObjectProperty<GraphicsContext> graphicsContextProperty() { return graphicsContext; }

    private final DoubleProperty translatex = new SimpleDoubleProperty();
    private final DoubleProperty translatey = new SimpleDoubleProperty();
    private final DoubleProperty scalex = new SimpleDoubleProperty();
    private final DoubleProperty scaley = new SimpleDoubleProperty();

    public DoubleProperty translateXProperty() { return translatex; }
    public DoubleProperty translateYProperty() { return translatey; }
    public DoubleProperty scaleXProperty() { return scalex; }
    public DoubleProperty scaleYProperty() { return scaley; }

    public BooleanProperty valueChangingProperty() { return valueChanging; }

    @Override
    public void setGraphicsContext(GraphicsContext gc) {
        graphicsContext.set(gc);
        parentLayer = (SchemaLayer) gc.getCanvas();
    }

    /**
     * 各 ShapeHolder はこれを Override して描画する.
     * そうすると super.draw() で色や線などの基本的なパラメータをセットすることができる.
     */
    @Override
    public void draw() {
        GraphicsContext gc = graphicsContext.get();
        if (gc == null) { return; }

        if (fillColor.get() != null) { gc.setFill(fillColor.get()); }
        if (lineColor.get() != null) { gc.setStroke(lineColor.get()); }
        gc.setLineWidth(lineWidth.get());
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);
    }

    /**
     * BlurRadius セット済みの GaussianBlur を返す.
     * @return
     */
    public GaussianBlur getBlurEffect() { return gaussianBlur; }

    /**
     * dx, dy ドット分だけ移動.
     * 実データは書き換えないで Translate 要素を設定して，値を読み出すときに Modify する.
     * @param dx
     * @param dy
     */
    @Override
    public void translate(double dx, double dy) {
        translatex.set(translatex.get() + dx);
        translatey.set(translatey.get() + dy);
    }

    /**
     * StartX, StartY を基点に EndX, EndY 側を dx, dy ドット分拡大・縮小.
     * 実データを書き換えると Path の値が不可逆になってしまうので，
     * 実データは書き換えないで Scale 要素を設定して，値を読み出すときに Modify する.
     * @param dx
     * @param dy
     */
    @Override
    public void scale(double dx, double dy) {
        scalex.set(scalex.get() + dx);
        scaley.set(scaley.get() + dy);
    }

    /**
     * 回転は実座標を変換している.
     * TranslateEditor から呼ばれる通常回転では Shape の中心を中心に回転.
     * 拡大縮小が入ると変形してしまうが，面倒くさいので無視.
     * RotateEditor から呼ばれる 90度回転の時は Canvas の中心を中心に回転.
     * これは変形すると困るので別処理する.
     * @param r
     */
    @Override
    public void rotate(double r) {
        ObservableList<Double> rx = FXCollections.observableArrayList();
        ObservableList<Double> ry = FXCollections.observableArrayList();

        if (r == Math.PI/2) {
            // RotateEditor から呼ばれる 90度回転の場合
            for (int i=0; i<pathx.size(); i++) {
                double x = graphicsContext.get().getCanvas().getWidth() - pathy.get(i);
                double y = pathx.get(i);

                rx.add(x);
                ry.add(y);
            }
            // 一気にセットすると ListChangeListener が呼ばれるのが１回ですむ
            pathx.setAll(rx);
            pathy.setAll(ry);

            double prevx = scalex.get();
            scalex.set(-scaley.get());
            scaley.set(prevx);
            prevx = translatex.get();
            translatex.set(-translatey.get());
            translatey.set(prevx);

        } else if (r == -Math.PI/2) {
            // RotateEditor から呼ばれる -90度回転の場合
            for (int i=0; i<pathx.size(); i++) {
                double x = pathy.get(i);
                double y = graphicsContext.get().getCanvas().getHeight() - pathx.get(i);
                rx.add(x);
                ry.add(y);
            }
            // 一気にセットすると ListChangeListener が呼ばれるのが１回ですむ
            pathx.setAll(rx);
            pathy.setAll(ry);

            double prevx = scalex.get();
            scalex.set(scaley.get());
            scaley.set(-prevx);
            prevx = translatex.get();
            translatex.set(translatey.get());
            translatey.set(-prevx);

        } else {
            // 通常の回転は図形の中心で回転
            double cx = getPivotX();
            double cy = getPivotY();
            Affine a = SchemaUtils.createRotate(r, cx, cy);

            for (int i=0; i<pathx.size(); i++) {
                Point2D p = SchemaUtils.affineTransform(a, pathx.get(i), pathy.get(i));
                rx.add(p.getX());
                ry.add(p.getY());
            }
            // path(0) の位置により start, end が変化する可能性があるので保存しておく
            double prevStartX = startx.get();
            double prevStartY = starty.get();
            double prevEndX = endx.get();
            double prevEndY = endy.get();

            // 一気にセットすると ListChangeListener が呼ばれるのが１回ですむ
            pathx.setAll(rx);
            pathy.setAll(ry);

            // 変換の結果，start, end の変化があれば，scale, translate を調節する
            if ((endx.get() - startx.get()) * (prevEndX - prevStartX) < 0) {
                translatex.set(translatex.get() + scalex.get());
                scalex.set(-scalex.get());
            }
            if ((endy.get() - starty.get()) * (prevEndY - prevStartY) < 0) {
                translatey.set(translatey.get() + scaley.get());
                scaley.set(-scaley.get());
            }
        }
    }

    /**
     * 回転中心の X 座標を返す.
     * @return
     */
    public double getPivotX() { return (startx.get() + endx.get()) / 2; }

    /**
     * 回転中心の Y.
     * @return
     */
    public double getPivotY() { return (starty.get() + endy.get()) / 2; }

    /**
     * ShapeHolder の Shape が指定された点を含んでいるかどうか.
     * 矩形領域以外は Override してコーディング必要.
     * @param x
     * @param y
     * @return
     */
    @Override
    public boolean contains(double x, double y) {
        return bounds.contains(x, y);
    }

    /**
     * この ShapeHolder の Bounds を返す.
     * @return
     */
    public ShapeHolderBounds getBounds() { return bounds; }

    /**
     * Transform の逆行列を返す.
     * Property ではないので，必要な時にその都度 getInvertAffine して取り直す必要あり.
     * [ mxx mxy tx ]       1  [  myy -mxy  mxy*ty-myy*tx ]
     * [ myx myy ty ] ->   --- [ -myx  mxx  myx*tx-mxx*ty ]
     * [  0   0   1 ]      det [   0    0         det     ]
     * @return
     */
    public Affine getInvertAffine() {
        Affine a = getGraphicsContext().getTransform();
        double det = a.getMxx() * a.getMyy() - a.getMxy() * a.getMyx();
        invert.setMxx(a.getMyy()/det);
        invert.setMxy(-a.getMxy()/det);
        invert.setTx((a.getMxy()*a.getTy() - a.getMyy()*a.getTx())/det);
        invert.setMyx(-a.getMyx()/det);
        invert.setMyy(a.getMxx()/det);
        invert.setTy((a.getMyx()*a.getTx() - a.getMxx()*a.getTy())/det);
        invert.setTz(1);
        return invert;
    }

    /**
     * SchemaEditorProperties との bind.
     */
    public void bind() {
        valueChanging.bind(properties.valueChangingProperty());
        lineWidth.bind(properties.lineWidthProperty());
        lineColor.bind(properties.lineColorProperty());
        fillColor.bind(properties.fillColorProperty());
        fillBlur.bind(properties.fillBlurProperty());
        fillMode.bind(properties.fillModeProperty());

        redrawListener.add();
    }

    /**
     * SchemaEditorProperties との bind を切る.
     */
    public void unbind() {
        valueChanging.unbind();
        lineWidth.unbind();
        lineColor.unbind();
        fillColor.unbind();
        fillBlur.unbind();
        fillMode.unbind();

        redrawListener.remove();
    }

    /**
     * SchemaEditorProperties の値をこの Holder にセット.
     */
    public void setProperties() {
        lineWidth.set(properties.getLineWidth());
        lineColor.set(properties.getLineColor());
        fillColor.set(properties.getFillColor());
        fillBlur.set(properties.getFillBlur());
        fillMode.set(properties.getFillMode());
    }

    // StartX
    public double getStartX() { return dispstartx.get(); }
    public void setStartX(double x) { startx.set(x); }
    // StartY
    public double getStartY() { return dispstarty.get(); }
    public void setStartY(double y) { starty.set(y); }
    // endX
    public double getEndX() { return dispendx.get(); }
    public void setEndX(double x) { endx.set(x); }
    // endY
    public double getEndY() { return dispendy.get(); }
    public void setEndY(double x) { endy.set(x); }
    // path
    public void addPathX(double x) { pathx.add(x); }
    public void addPathY(double y) { pathy.add(y); }
    public void setPathX(int i, double x) { pathx.set(i, x); }
    public void setPathY(int i, double y) { pathy.set(i, y); }
    public double getPathX(int i) {
        double diff = endx.get() - startx.get();
        double scaleFactor = (diff == 0)? 0 : (pathx.get(i) - startx.get()) / diff;
        return pathx.get(i) + translatex.get() + scaleFactor * scalex.get(); }
    public double getPathY(int i) {
        double diff = endy.get() - starty.get();
        double scaleFactor = (diff == 0)? 0 : (pathy.get(i) - starty.get()) / diff;
        return pathy.get(i) + translatey.get() + scaleFactor * scaley.get(); }
    public int getPathSize() { return pathx.size(); }
    // lineWidth
    public double getLineWidth() { return lineWidth.get(); }
    public void setLineWidth(double w) { lineWidth.set(w); }
    // lineColor
    public Color getLineColor() { return lineColor.get(); }
    public void setLineColor(Color c) { lineColor.set(c); }
    // fillColor
    public Color getFillColor() { return fillColor.get(); }
    public void setFillColor(Color c) { fillColor.set(c); }
    // blur rate 0.0-1.0
    public double getFillBlur() { return fillBlur.get(); }
    public void setFillBlur(double r) { fillBlur.set(r); }
    // FillMode
    public FillMode getFillMode() { return fillMode.get(); }
    public void setFillMode(FillMode f) { fillMode.set(f); }
    // GraphicsContext
    public GraphicsContext getGraphicsContext() { return graphicsContext.get(); }
    // 親の Canvas (SchemaLayer) の大きさ
    public double getCanvasWidth() { return graphicsContext.get().getCanvas().getWidth(); }
    public double getCanvasHeight() { return graphicsContext.get().getCanvas().getHeight(); }
    // State は作られた時の State Property の値が自動的にセットされるが clone を作るときは自分でセットする必要あり
    public State getState() { return state; }
    public void setState(State s) { state = s; }

    // binds
    {
        dispstartx.bind(startx.add(translatex));
        dispstarty.bind(starty.add(translatey));
        dispendx.bind(endx.add(translatex).add(scalex));
        dispendy.bind(endy.add(translatey).add(scaley));

        // path が変化したら startx, starty, endx, endy を計算し直す
        pathx.addListener(new PathListChangeListener(startx, endx));
        pathy.addListener(new PathListChangeListener(starty, endy));

        bounds.startXProperty().bind(dispstartx);
        bounds.startYProperty().bind(dispstarty);
        bounds.endXProperty().bind(dispendx);
        bounds.endYProperty().bind(dispendy);
        bounds.blurProperty().bind(fillBlur);

        gaussianBlur.radiusProperty().bind(bounds.blurRadiusProperty());
    }

    /**
     * path が変化したら startx, starty, endx, endy を計算しなおす Listener.
     * path 中の x, y の最大値，最小値を調べて start, end の値とする.
     * path をセットするとき，一つ一つセットすると，そのたび呼ばれて遅くなるので，setAll でセットすること.
     */
    private class PathListChangeListener implements ListChangeListener<Double> {
        private final DoubleProperty start, end;

        public PathListChangeListener(DoubleProperty st, DoubleProperty ed) {
            start = st; end = ed;
        }

        @Override
        public void onChanged(Change<? extends Double> change) {
            ObservableList<? extends Double> path = change.getList();
            if (path.isEmpty()) { return; }

            if (path.size() == 1) {
                start.set(path.get(0));
                end.set(path.get(0));

            } else if (path.size() == 2) {
                // Line の場合
                end.set(path.get(1));

            } else {
                double d0 = change.getList().get(0);
                double min = d0;
                double max = d0;
                // max, min を調べる
                for (int i=1; i<path.size(); i++) {
                    double d = path.get(i);
                    if (d < min) { min = d; }
                    if (d > max) { max = d; }
                }
                // path(0) が近い方を start とする
                // path(0) が中点だったら path(1)を，path(1)も中点だったら path(2)が近い方を start とする
                // それ以上は諦める
                double ref = d0;
                for (int i=1; i<3; i++) {
                    if (max + min != 2 * ref) { break; }
                    ref = change.getList().get(i);
                }

                if (max + min > 2 * ref) {
                    start.set(min);
                    end.set(max);
                } else {
                    start.set(max);
                    end.set(min);
                }
            }
        }
    }

    /**
     * プロパティーの変化により親の SchemaLayer を redraw するリスナ.
     */
    private class RedrawListener implements InvalidationListener {
        public void add() {
            for (Property p : propertiesToRedraw) { p.addListener(this); }
        }
        public void remove() {
            for (Property p : propertiesToRedraw) { p.removeListener(this); }
        }

        @Override
        public void invalidated(Observable o) {
            if (parentLayer != null) { parentLayer.redraw(); }
        }
    }

    /**
     * ShapeHolderBase の同等性はパラメータの一致で判定する.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof ShapeHolderBase)) { return false; }
        ShapeHolderBase dist = (ShapeHolderBase) obj;

        boolean bpathx = pathx.size() == dist.pathXList().size();
        if (bpathx) {
            for (int i=0; i<pathx.size(); i++) {
                if (! Objects.equals(pathx.get(i), dist.pathXList().get(i))) {
                    bpathx = false;
                    break;
                }
            }
        }
        boolean bpathy = pathy.size() == dist.pathYList().size();
        if (bpathy) {
            for (int i=0; i<pathy.size(); i++) {
                if (! Objects.equals(pathy.get(i), dist.pathYList().get(i))) {
                    bpathy = false;
                    break;
                }
            }
        }

        return lineWidth.get() == dist.lineWidthProperty().get()
                && lineColor.get().equals(dist.lineColorProperty().get())
                && fillColor.get().equals(dist.fillColorProperty().get())
                && fillBlur.get() == dist.fillBlurProperty().get()
                && fillMode.get().equals(dist.fillModeProperty().get())
                && startx.get() == dist.startXProperty().get()
                && starty.get() == dist.startYProperty().get()
                && endx.get() == dist.endXProperty().get()
                && endy.get() == dist.endYProperty().get()
                && translatex.get() == dist.translateXProperty().get()
                && translatey.get() == dist.translateYProperty().get()
                && scalex.get() == dist.scaleXProperty().get()
                && scaley.get() == dist.scaleYProperty().get()
                && bpathx && bpathy
                ;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return String.format(
                "class = %s\n"
              + "(startx, starty) = (%f, %f)\n"
              + "(getStartX, getStartY) = (%f, %f)\n"
              + "(endx, endy) = (%f, %f)\n"
              + "(getEndX, getEndY) = (%f, %f)\n"
              + "(tx, ty) = (%f, %f)\n"
              + "(sx, sy) = (%f, %f)\n",
                getClass().getName(), startx.get(), starty.get(), getStartX(), getStartY(),
                endx.get(), endy.get(), getEndX(), getEndY(),
                translatex.get(), translatey.get(), scalex.get(), scaley.get()
        );
    }
}
