package open.dolphin.impl.scheam;

import javafx.scene.paint.Color;

/**
 * Color 属性をひとまとめにした Model
 * @author pns
 */
public class ColorModel {
    private Color lineColor;
    private double lineWidth;
    private Color fillColor;
    private double fillBlur;
    private FillMode fillMode;

    public void setLineColor(Color c) { lineColor = c; }
    public Color getLineColor() { return lineColor; }

    public void setLineWidth(double d) { lineWidth = d; }
    public double getLineWidth() { return lineWidth; }

    public void setFillColor(Color c) { fillColor = c; }
    public Color getFillColor() { return fillColor; }

    public void setFillBlur(double d) { fillBlur = d; }
    public double getFillBlur() { return fillBlur; }

    public void setFillMode(FillMode m) { fillMode = m; }
    public FillMode getFillMode() { return fillMode; }
}
