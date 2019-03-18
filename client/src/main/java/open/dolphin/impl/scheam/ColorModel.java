package open.dolphin.impl.scheam;

import javafx.scene.paint.Color;

/**
 * Color 属性をひとまとめにした Model.
 *
 * @author pns
 */
public class ColorModel {
    private Color lineColor;
    private double lineWidth;
    private Color fillColor;
    private double fillBlur;
    private FillMode fillMode;

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color c) {
        lineColor = c;
    }

    public double getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(double d) {
        lineWidth = d;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color c) {
        fillColor = c;
    }

    public double getFillBlur() {
        return fillBlur;
    }

    public void setFillBlur(double d) {
        fillBlur = d;
    }

    public FillMode getFillMode() {
        return fillMode;
    }

    public void setFillMode(FillMode m) {
        fillMode = m;
    }
}
