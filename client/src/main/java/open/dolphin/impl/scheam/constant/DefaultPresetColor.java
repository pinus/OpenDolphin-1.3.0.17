package open.dolphin.impl.scheam.constant;

import javafx.scene.paint.Color;
import open.dolphin.impl.scheam.ColorModel;
import open.dolphin.impl.scheam.FillMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * プリセット色を作る.
 * @author pns
 */
public class DefaultPresetColor {
    /** 色系列の seeds */
    private static final String[] COLOR_SEEDS = {
        // red
        "#FF0000", "#E01000", "#C02000",
        // brown
        "#885500", "#663300",
        // purple
        "#A000A0", "#700050",
    };

    /** seeds から作成する variation の数 */
    private static final int VARIATIONS = 8;

    /** Color Series の名前 */
    public static enum Series {
        Red(0), DeepRed(VARIATIONS), DeeperRed(VARIATIONS*2), Brown(VARIATIONS*3),
        DeepBrown(VARIATIONS*4), Purple(VARIATIONS*5), DeepPurple(VARIATIONS*6),
        Gray(VARIATIONS*7);

        private final int offset;
        private Series(int o) {
            offset = o;
        }
        public int getOffset() { return offset; }
    }

    /** seeds から計算して作った色を入れる配列 */
    private static final List<Color> colorList = new ArrayList<>();

    /**  色配列を seeds から計算して作る */
    static {
        // seed から生成するもの
        for (String seed : COLOR_SEEDS) {
            Color src = Color.web(seed);
            double hue = src.getHue();
            double saturation = src.getSaturation();
            double brightness = src.getBrightness();

            for (int i=0; i<VARIATIONS; i++) {
                colorList.add(Color.hsb(hue, saturation, brightness));
                saturation *= 0.80;
                brightness += (1.0 - brightness) / 10;
            }
        }
        // gray scale は別に作る
        for (int i=0; i<VARIATIONS; i++) {
            double brightness = Math.pow((double)i / (double)(VARIATIONS), 0.7);
            colorList.add(Color.hsb(0, 0, brightness));
        }
    }

    private DefaultPresetColor() {}

    public static List<Color> getColorList() { return Collections.unmodifiableList(colorList); }

    /**
     * Satulation, Brightness を少しずつ変えた Color Series を作る.
     * Opacity 情報は入っていない.
     * @param series
     * @param fillMode
     * @return
     */
    public static List<ColorModel> getSeries(Series series, FillMode fillMode) {
        List<ColorModel> list = new ArrayList<>();

        if (fillMode.equals(FillMode.Fill)) {
            for (int i=0; i<VARIATIONS; i++) {
                ColorModel m = new ColorModel();
                m.setFillColor(colorList.get(i + series.getOffset()));

                m.setLineColor(Const.PNS_BLACK);
                m.setLineWidth(2.0);
                m.setFillBlur(0.1);
                m.setFillMode(FillMode.Fill);

                list.add(m);
            }

        } else if (fillMode.equals(FillMode.Line)) {
            for (int i=0; i<VARIATIONS; i++) {
                ColorModel m = new ColorModel();
                m.setFillColor(colorList.get(i + series.getOffset()));
                m.setLineColor(colorList.get(i + series.getOffset()));

                m.setLineWidth(2.0);
                m.setFillBlur(0.1);
                m.setFillMode(FillMode.Line);

                list.add(m);
            }
        }
        return list;
    }

    /**
     * 特別な Color Series.
     * 0: 枠が赤で中が茶色
     * 1: 枠が赤で中が紫
     * @param n
     * @return
     */
    public static List<ColorModel> getSpecialSeries(int n) {
        List<ColorModel> list = new ArrayList<>();

        switch (n) {
            case 0:
                for (int i=0; i<VARIATIONS; i++) {
                    ColorModel m = new ColorModel();
                    m.setFillColor(colorList.get(i + Series.DeepRed.getOffset()));

                    m.setLineColor(colorList.get(0));
                    m.setLineWidth(3.0);
                    m.setFillBlur(0.1);
                    m.setFillMode(FillMode.Mixed);

                    list.add(m);
                }
                break;

            case 1:
                for (int i=0; i<VARIATIONS; i++) {
                    ColorModel m = new ColorModel();
                    m.setFillColor(colorList.get(i + Series.Brown.getOffset()));

                    m.setLineColor(colorList.get(0));
                    m.setLineWidth(3.0);
                    m.setFillBlur(0.1);
                    m.setFillMode(FillMode.Mixed);

                    list.add(m);
                }
                break;

            case 2:
                for (int i=0; i<VARIATIONS; i++) {
                    ColorModel m = new ColorModel();
                    m.setFillColor(colorList.get(i + Series.Purple.getOffset()));

                    m.setLineColor(colorList.get(0));
                    m.setLineWidth(3.0);
                    m.setFillBlur(0.1);
                    m.setFillMode(FillMode.Mixed);

                    list.add(m);
                }
                break;

        }

        return list;
    }
}
