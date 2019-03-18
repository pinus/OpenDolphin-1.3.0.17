package open.dolphin.impl.scheam.constant;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import open.dolphin.impl.scheam.helper.SchemaUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ShapeIcons
 *
 * @author pns
 */
public class ShapeIcon {

    /**
     * チェックマーク
     *
     * @return
     */
    public static Shape getCheckMark() {
        SVGPath path = new SVGPath();
        path.setFill(Const.PNS_BLACK);
        path.setContent("M 0 5 L 3 9 L 4 9 L 9 0 L 7 0  L 3 6 L 2 5 z");
        return path;
    }

    /**
     * ComboBox の右側の三角矢印
     *
     * @return
     */
    public static Shape getComboBoxArrow() {
        SVGPath path = new SVGPath();
        path.setFill(Const.PNS_BLACK);
        path.setContent("M 0 4 L 6 4 L 3 0 M 0 8 L 6 8 L 3 12 z");
        return path;
    }

    /**
     * 黒線に白抜きのポインター矢印
     *
     * @return
     */
    public static Shape getTranslatePointer() {
        SVGPath path = new SVGPath();
        path.setFill(Color.WHITE);
        path.setStroke(Const.PNS_BLACK);
        path.setStrokeWidth(1);
        path.setContent("M 0 0 L 1 0 L 10 8 L 6 9 L 8 13 L 8 14 L 7 15 L 6 15 L 5 14 L 3 10 L 0 13 L 0 0 z");
        return path;
    }

    /**
     * ミミズの這った線アイコン
     *
     * @return
     */
    public static Shape getOpenPath() {
        SVGPath path = new SVGPath();
        path.setStroke(Const.PNS_BLACK);
        path.setFill(Color.TRANSPARENT);
        path.setStrokeWidth(2);
        path.setContent("M 0 7 Q 6 8 7 5 Q 8 0 5 0 Q 0 0 6 5 Q 8 7 15 1");
        return path;
    }

    /**
     * 線アイコン
     *
     * @return
     */
    public static Shape getLine() {
        Line line = new Line();
        line.setStartX(0);
        line.setStartY(8);
        line.setEndX(15);
        line.setEndY(0);
        line.setStrokeWidth(2);
        line.setStroke(Const.PNS_BLACK);
        return line;
    }

    /**
     * 線の太さ用アイコン
     *
     * @param width
     * @param height
     * @return
     */
    public static Shape getLine(double width, double height) {
        Rectangle rect = new Rectangle();
        rect.setFill(Const.PNS_BLACK);
        rect.setWidth(width);
        rect.setHeight(height);
        return rect;
    }

    /**
     * 円アイコン
     *
     * @return
     */
    public static Shape getCircle() {
        return getCircle(6, 6, Const.PNS_BLACK, Color.TRANSPARENT);
    }

    /**
     * 円のアイコン
     *
     * @param rx
     * @param ry
     * @param stroke
     * @param fill
     * @return
     */
    public static Shape getCircle(double rx, double ry, Paint stroke, Paint fill) {
        Ellipse e = new Ellipse();
        e.setRadiusX(rx);
        e.setRadiusY(ry);
        e.setStroke(stroke);
        e.setFill(fill);
        e.setStrokeWidth(2);
        return e;
    }

    /**
     * 長方形アイコン
     *
     * @return
     */
    public static Shape getRectangle() {
        return getRectangle(12, 12, Const.PNS_BLACK, Color.TRANSPARENT);
    }

    /**
     * 長方形のアイコン
     *
     * @param w
     * @param h
     * @param stroke
     * @param fill
     * @return
     */
    public static Shape getRectangle(double w, double h, Paint stroke, Paint fill) {
        Rectangle rect = new Rectangle();
        rect.setWidth(w);
        rect.setHeight(h);
        rect.setStroke(stroke);
        rect.setFill(fill);
        rect.setStrokeWidth(2);
        return rect;
    }

    /**
     * Polygon アイコン
     *
     * @return
     */
    public static Shape getPolygon() {
        return getPolygon(12, 12, Const.PNS_BLACK, Color.TRANSPARENT);
    }

    /**
     * Polygon アイコン
     *
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
        for (int i = 1; i < p.size(); i++) {
            path.getElements().add(new LineTo(p.get(i).getX(), p.get(i).getY()));
        }
        path.setStrokeWidth(2);
        path.setStroke(stroke);
        path.setFill(fill);
        return path;
    }

    /**
     * Polygon アイコンを描くための Path
     *
     * @param w
     * @param h
     * @return
     */
    public static List<Point2D> getPolygonPath(double w, double h) {
        List<Point2D> p = new ArrayList<>();
        p.add(new Point2D(0, h / 2.0));
        p.add(new Point2D(w / 4.0, h));
        p.add(new Point2D(w * 4.0 / 5.0, h));
        p.add(new Point2D(w, h / 4.0));
        p.add(new Point2D(w * 2.0 / 3.0, h / 3.0));
        p.add(new Point2D(w / 3.0, 0));
        p.add(new Point2D(0, h / 2.0));
        return p;
    }

    /**
     * random dots からなるアイコン
     *
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

        points.forEach(p -> {
            if ((size / 2 - p.getX()) * (size / 2 - p.getX()) + (size / 2 - p.getY()) * (size / 2 - p.getY()) < size * size / 4) {
                path.getElements().add(new MoveTo(p.getX(), p.getY()));
                path.getElements().add(new LineTo(p.getX(), p.getY()));
            }
        });
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
     *
     * @return
     */
    public static Shape getNet() {
        Path path = new Path();
        path.setStrokeWidth(1);
        path.setStroke(Const.PNS_BLACK);
        double size = 15;
        List<Point2D> points = SchemaUtils.getNet(0, 0, size, size, 4);
        for (Point2D p : points) {
            if ((size / 2 - p.getX()) * (size / 2 - p.getX()) + (size / 2 - p.getY()) * (size / 2 - p.getY()) < size * size / 4) {
                path.getElements().add(new MoveTo(p.getX(), p.getY()));
                path.getElements().add(new LineTo(p.getX(), p.getY()));
            }
        }
        return path;
    }

    /**
     * Text のアイコン
     *
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
     *
     * @return
     */
    public static Shape getEraser() {
        SVGPath path = new SVGPath();
        path.setStroke(Const.PNS_BLACK);
        path.setFill(Color.GRAY);
        path.setStrokeWidth(1);
        path.setFillRule(FillRule.EVEN_ODD);
        path.setContent("M 0.5 0  L 9.5 0 L 9.5 8 L 0.5 8 L 0.5 0 "
                + "M 1 9 L 1 11 L 2 12 L 8 12 L 9 11 L 9 9  "
                + "M 1 9 L 1 11 L 2 12 L 8 12 L 9 11 L 9 9  ");
        return path;
    }

    /**
     * 虫眼鏡アイコン
     *
     * @return
     */
    public static Shape getLoupe() {
        SVGPath path = new SVGPath();
        path.setFill(Color.TRANSPARENT);
        path.setStroke(Const.PNS_BLACK);
        path.setStrokeWidth(2);
        path.setContent("M 8 8 L 11 11   M 0 4.5 A 4.5 4.5 0 1 1 9 4.5 A 4.5 4.5 0 1 1 0 4.5 ");
        return path;
    }

    /**
     * [+] 付きの虫眼鏡アイコン
     *
     * @return
     */
    public static Shape getLoupePlus() {
        SVGPath path = new SVGPath();
        path.setFill(Color.TRANSPARENT);
        path.setStroke(Const.PNS_BLACK);
        path.setStrokeWidth(2);
        path.setContent("M 8 8 L 11 11   M 0 4.5 A 4.5 4.5 0 1 1 9 4.5 A 4.5 4.5 0 1 1 0 4.5 "
                + "M 2.5 4.5 L 6.5 4.5 M 4.5 2.5 L 4.5 6.5");
        return path;
    }

    /**
     * [-] 付きの虫眼鏡アイコン
     *
     * @return
     */
    public static Shape getLoupeMinus() {
        SVGPath path = new SVGPath();
        path.setFill(Color.TRANSPARENT);
        path.setStroke(Const.PNS_BLACK);
        path.setStrokeWidth(2);
        path.setContent("M 8 8 L 11 11   M 0 4.5 A 4.5 4.5 0 1 1 9 4.5 A 4.5 4.5 0 1 1 0 4.5 "
                + "M 2.5 4.5 L 6.5 4.5");
        return path;
    }

    /**
     * 時計回り回転アイコン
     *
     * @return
     */
    public static Shape getRotate() {
        SVGPath path = new SVGPath();
        path.setFill(Color.TRANSPARENT);
        path.setStroke(Const.PNS_BLACK);
        path.setStrokeWidth(2);
        path.setContent("M 7 0 A 5 5 0 1 1 2 4   M 0.5 7 L 2 3 L 4.5 6");
        return path;
    }

    /**
     * Clipping のアイコン
     *
     * @return
     */
    public static Shape getClip() {
        SVGPath path = new SVGPath();
        path.setFill(Color.TRANSPARENT);
        path.setStroke(Const.PNS_BLACK);
        path.setStrokeWidth(2);
        path.setContent("M 0 2.5 L 9.5 2.5 L 9.5 12  M 2.5 0 L 2.5 9.5 L 12 9.5");
        return path;
    }

    /**
     * Undo Icon
     *
     * @return
     */
    public static Shape getUndo() {
        SVGPath path = new SVGPath();
        path.setStroke(Const.PNS_BLACK);
        path.setContent("M 0 4 L 10 0 L 10 8 z");
        return path;
    }

    /**
     * Redo Icon
     *
     * @return
     */
    public static Shape getRedo() {
        SVGPath path = new SVGPath();
        path.setStroke(Const.PNS_BLACK);
        path.setContent("M 0 0 L 0 8 L 10 4 z");
        return path;
    }

    /**
     * Clear Icon
     *
     * @return
     */
    public static Shape getClear() {
        Path path = new Path();
        path.setStrokeWidth(1);
        path.setStroke(Const.PNS_BLACK);
        double r = 6;
        double f = 7;
        double t = 2 * Math.PI / f;
        for (double i = 0; i < f; i++) {
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
    public static void main(String[] argv) {
        // Mac OS X needs this to avoid HeadlessException
        System.setProperty("java.awt.headless", "false");

        SwingUtilities.invokeLater(() -> {
            final JFrame frame = new JFrame();
            frame.setUndecorated(false);
            final JFXPanel fxp = new JFXPanel();
            frame.add(fxp);

            Platform.runLater(() -> {
                HBox pane = new HBox();
                pane.setSpacing(5);
                pane.setPadding(new Insets(5));
                pane.setPrefSize(150, 100);
                pane.getChildren().add(getCheckMark());
                pane.getChildren().add(getComboBoxArrow());
                pane.getChildren().add(getTranslatePointer());
                pane.getChildren().add(getPolygon(12, 12, Const.PNS_BLACK, Color.TRANSPARENT));
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
                scene.getStylesheets().add("css/pns-stage.css");
                scene.getStylesheets().add("css/schemaeditorimpl.css");
                fxp.setScene(scene);
            });

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
