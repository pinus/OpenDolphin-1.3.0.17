package open.dolphin.impl.scheam.helper;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.EllipseBuilder;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineBuilder;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.shape.SVGPathBuilder;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import open.dolphin.impl.scheam.constant.Const;
import open.dolphin.impl.scheam.constant.StyleClass;

/**
 * ShapeIcons
 * @author pns
 */
public class ShapeIcon {

    /**
     * チェックマーク
     * @return
     */
    public static Shape getCheckMark() {
        return SVGPathBuilder.create().fill(Const.PNS_BLACK)
                .content("M 0 5 L 3 9 L 4 9 L 9 0 L 7 0  L 3 6 L 2 5 z").build();
    }
    /**
     * ComboBox の右側の三角矢印
     * @return
     */
    public static Shape getComboBoxArrow() {
        return SVGPathBuilder.create().fill(Const.PNS_BLACK)
                .content("M 0 4 L 6 4 L 3 0 M 0 8 L 6 8 L 3 12 z").build();
    }
    /**
     * 黒線に白抜きのポインター矢印
     * @return
     */
    public static Shape getTranslatePointer() {
        return SVGPathBuilder.create().stroke(Const.PNS_BLACK).fill(Color.WHITE)
                .content("M 0 0 L 1 0 L 10 8 L 6 9 L 8 13 L 8 14 L 7 15 L 6 15 L 5 14 L 3 10 L 0 13 L 0 0 z").build();
    }
    /**
     * ミミズの這った線アイコン
     * @return
     */
    public static Shape getOpenPath() {
        return SVGPathBuilder.create().stroke(Const.PNS_BLACK).fill(Color.TRANSPARENT).strokeWidth(2)
                .content("M 0 7 Q 6 8 7 5 Q 8 0 5 0 Q 0 0 6 5 Q 8 7 15 1").build();
    }
    /**
     * 線アイコン
     * @return
     */
    public static Shape getLine() {
        return LineBuilder.create().startX(0).startY(8).endX(15).endY(0).strokeWidth(2).stroke(Const.PNS_BLACK).build();
    }
    /**
     * 線の太さ用アイコン
     * @param width
     * @param height
     * @return
     */
    public static Shape getLine(double width, double height) {
        return RectangleBuilder.create().fill(Const.PNS_BLACK).width(width).height(height).build();
    }
    /**
     * 円のアイコン
     * @param rx
     * @param ry
     * @param stroke
     * @param fill
     * @return
     */
    public static Shape getCircle(double rx, double ry, Paint stroke, Paint fill) {
        return EllipseBuilder.create().radiusX(rx).radiusY(ry).stroke(stroke).fill(fill).strokeWidth(2).build();
    }

    /**
     * 長方形のアイコン
     * @param w
     * @param h
     * @param stroke
     * @param fill
     * @return
     */
    public static Shape getRectangle(double w, double h, Paint stroke, Paint fill) {
        return RectangleBuilder.create().width(w).height(h).stroke(stroke).fill(fill).strokeWidth(2).build();
    }

    /**
     * Polygon アイコン
     * @param w
     * @param h
     * @param stroke
     * @param fill
     * @return
     */
    public static Shape getPolygon(double w, double h, Paint stroke, Paint fill) {
        Path path = new Path();
        List<Point2D> p = getPolygonPath(w, h);
        path.getElements().add(new MoveTo(p.get(0).getX(), p.get(0).getY()));
        for (int i=1; i<p.size(); i++) {
            path.getElements().add(new LineTo(p.get(i).getX(), p.get(i).getY()));
        }
        path.setStrokeWidth(2);
        path.setStroke(stroke);
        path.setFill(fill);
        return path;
    }
    /**
     * Polygon アイコンを描くための Path
     * @param w
     * @param h
     * @return
     */
    public static List<Point2D> getPolygonPath(double w, double h) {
        List<Point2D> p = new ArrayList<>();
        p.add(new Point2D(0, h/2.0));
        p.add(new Point2D(w/4.0, h));
        p.add(new Point2D(w *4.0/5.0, h));
        p.add(new Point2D(w, h/4.0));
        p.add(new Point2D(w *2.0/3.0, h/3.0));
        p.add(new Point2D(w/3.0, 0));
        p.add(new Point2D(0, h/2.0));
        return p;
    }
    /**
     * random dots からなるアイコン
     * @return
     */
    public static Shape getDots() {
        Path path = new Path();
        path.setStrokeWidth(2);
        path.setStroke(Const.PNS_BLACK);
        double size = 13;

        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(0.41, 4.46));
        points.add(new Point2D(0.54, 8.52));
        points.add(new Point2D(5.29, 1.20));
        points.add(new Point2D(5.90, 5.67));
        points.add(new Point2D(5.17, 9.30));
        points.add(new Point2D(9.17, 5.25));
        points.add(new Point2D(9.87, 8.46));
        points.add(new Point2D(8.17, 12.03));

        for (Point2D p : points) {
            if ((size/2 - p.getX())*(size/2 - p.getX()) + (size/2 - p.getY())*(size/2 - p.getY()) < size*size/4) {
                path.getElements().add(new MoveTo(p.getX(), p.getY()));
                path.getElements().add(new LineTo(p.getX(), p.getY()));
            }
        }
/*
        List<Point2D> points = SchemaUtils.getRandomPoints(size, size, 4);
        for (Point2D p : points) {
            if ((size/2 - p.getX())*(size/2 - p.getX()) + (size/2 - p.getY())*(size/2 - p.getY()) < size*size/4) {
                path.getElements().add(new MoveTo(p.getX(), p.getY()));
                path.getElements().add(new LineTo(p.getX(), p.getY()));
                System.out.printf("points.add(new Point2D(%.2f, %.2f))%n", p.getX(), p.getY());
            }
        }
*/
        return path;
    }
    /**
     * 網のアイコン
     * @return
     */
    public static Shape getNet() {
        Path path = new Path();
        path.setStrokeWidth(1);
        path.setStroke(Const.PNS_BLACK);
        double size = 15;
        List<Point2D> points = SchemaUtils.getNet(0,0, size, size, 4);
        for (Point2D p : points) {
            if ((size/2 - p.getX())*(size/2 - p.getX()) + (size/2 - p.getY())*(size/2 - p.getY()) < size*size/4) {
                path.getElements().add(new MoveTo(p.getX(), p.getY()));
                path.getElements().add(new LineTo(p.getX(), p.getY()));
            }
        }
        return path;
    }
    /**
     * Text のアイコン
     * @return
     */
    public static Shape getText() {
        Text t = new Text("T");
        Font f = Font.font(t.getFont().getFamily(), FontWeight.BOLD, 14);
        t.setFont(f);
        t.setFill(Const.PNS_BLACK);
        return t;
    }
    /**
     * 消しゴムアイコン
     * @return
     */
    public static Shape getEraser() {
        return SVGPathBuilder.create().stroke(Const.PNS_BLACK).fill(Color.GREY).strokeWidth(1).fillRule(FillRule.EVEN_ODD)
                .content("M 0.5 0  L 9.5 0 L 9.5 8 L 0.5 8 L 0.5 0 "
                        + "M 1 9 L 1 11 L 2 12 L 8 12 L 9 11 L 9 9  "
                        + "M 1 9 L 1 11 L 2 12 L 8 12 L 9 11 L 9 9  "
                ).build();
    }
    /**
     * 虫眼鏡アイコン
     * @return
     */
    public static Shape getLoupe() {
        return SVGPathBuilder.create().fill(Color.TRANSPARENT).stroke(Const.PNS_BLACK).strokeWidth(2)
                .content("M 8 8 L 11 11   M 0 4.5 A 4.5 4.5 0 1 1 9 4.5 A 4.5 4.5 0 1 1 0 4.5 ").build();
    }
    /**
     * [+] 付きの虫眼鏡アイコン
     * @return
     */
    public static Shape getLoupePlus() {
        return SVGPathBuilder.create().fill(Color.TRANSPARENT).stroke(Const.PNS_BLACK).strokeWidth(2)
                .content("M 8 8 L 11 11   M 0 4.5 A 4.5 4.5 0 1 1 9 4.5 A 4.5 4.5 0 1 1 0 4.5 "
                        + "M 2.5 4.5 L 6.5 4.5 M 4.5 2.5 L 4.5 6.5").build();
    }
    /**
     * [-] 付きの虫眼鏡アイコン
     * @return
     */
    public static Shape getLoupeMinus() {
        return SVGPathBuilder.create().fill(Color.TRANSPARENT).stroke(Const.PNS_BLACK).strokeWidth(2)
                .content("M 8 8 L 11 11   M 0 4.5 A 4.5 4.5 0 1 1 9 4.5 A 4.5 4.5 0 1 1 0 4.5 "
                        + "M 2.5 4.5 L 6.5 4.5").build();
    }
    /**
     * 時計回り回転アイコン
     * @return
     */
    public static Shape getRotate() {
        return SVGPathBuilder.create().fill(Color.TRANSPARENT).stroke(Const.PNS_BLACK).strokeWidth(2)
                .content("M 7 0 A 5 5 0 1 1 2 4   M 0.5 7 L 2 3 L 4.5 6").build();
    }
    /**
     * Clipping のアイコン
     * @return
     */
    public static Shape getClip() {
        return SVGPathBuilder.create().fill(Color.TRANSPARENT).stroke(Const.PNS_BLACK).strokeWidth(2)
                .content("M 0 2.5 L 9.5 2.5 L 9.5 12  M 2.5 0 L 2.5 9.5 L 12 9.5").build();
    }
    /**
     * Undo Icon
     * @return
     */
    public static Shape getUndo() {
        return SVGPathBuilder.create().fill(Const.PNS_BLACK)
                .content("M 0 4 L 10 0 L 10 8 z").build();
    }
    /**
     * Redo Icon
     * @return
     */
    public static Shape getRedo() {
        return SVGPathBuilder.create().fill(Const.PNS_BLACK)
                .content("M 0 0 L 0 8 L 10 4 z").build();
    }
    /**
     * Clear Icon
     * @return
     */
    public static Shape getClear() {
        Path path = new Path();
        path.setStrokeWidth(1);
        path.setStroke(Const.PNS_BLACK);
        double r = 6;
        double f = 7;
        double t = 2 * Math.PI / f;
        for (double i=0; i<f; i++) {
            double x1 = r * Math.cos(t * i);
            double y1 = r * Math.sin(t * i);
            double x2 = (r - 1.5) * Math.cos(t * i);
            double y2 = (r - 1.5) * Math.sin(t * i);
            path.getElements().add(new MoveTo(x1, y1));
            path.getElements().add(new LineTo(x2, y2));
        }

        return path;
    }


    //-------------------------------------------------------
    public static void main (String[] argv) {
        // Mac OS X needs this to avoid HeadlessException
        System.setProperty("java.awt.headless", "false");

        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                final JFrame frame = new JFrame();
                frame.setUndecorated(false);
                final JFXPanel fxp = new JFXPanel();
                frame.add(fxp);

                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        HBox pane = new HBox();
                        pane.setSpacing(5);
                        pane.setPadding(new Insets(5));
                        pane.setPrefSize(150, 100);
                        pane.getChildren().add(getCheckMark());
                        pane.getChildren().add(getComboBoxArrow());
                        pane.getChildren().add(getTranslatePointer());
                        pane.getChildren().add(getPolygon(16,16,Color.BLACK, Color.TRANSPARENT));
                        pane.getChildren().add(getOpenPath());
                        pane.getChildren().add(getLine());
                        pane.getChildren().add(getDots());
                        pane.getChildren().add(getNet());
                        pane.getChildren().add(getText());
                        pane.getChildren().add(getEraser());
                        pane.getChildren().add(getLoupe());
                        pane.getChildren().add(getLoupePlus());
                        pane.getChildren().add(getLoupeMinus());
                        pane.getChildren().add(getRotate());
                        pane.getChildren().add(getClip());
                        pane.getChildren().add(getUndo());
                        pane.getChildren().add(getRedo());
                        pane.getChildren().add(getClear());

                        Scene scene = new Scene(pane);
                        scene.getStylesheets().add(StyleClass.CSS_FILE);
                        fxp.setScene(scene);
                    }
                });

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
