package open.dolphin.impl.scheam.constant;

import open.dolphin.impl.scheam.FillMode;
import javafx.scene.paint.Color;
import open.dolphin.impl.scheam.State;

/**
 * Default 値を入れる.
 * @author pns
 */
public class Default {

    // default values of properties with property names
    public static class LINE_WIDTH {
        public static final String key = "LineWidth";
        public static final double value = 2.0;
    }
    public static class LINE_COLOR {
        public static final String key = "LineColor";
        public static final Color value = Color.color(0, 0, 0, 0.5);
        public static final String string = "#0000007f";
    }
    public static class FILL_COLOR {
        public static final String key = "StartColor";
        public static final Color value = Color.color(1, 0, 0, 0.5);
        public static final String string = "#ff00007f";
    }
    public static class FILL_BLUR {
        public static final String key = "FillBlur";
        public static final double value = 0.3;
    }
    public static class FILL_MODE {
        public static final String key = "FillMode";
        public static final FillMode value = FillMode.Fill;
    }
    public static class STATE {
        public static final String key = "State";
        public static final State value = State.Polygon;
    }
    public static class FONT_NAME {
        public static final String key = "FontName";
        public static final String value = "SansSerif";
    }
    public static class FONT_SIZE {
        public static final String key = "FontSize";
        public static final double value = 24;
    }
}
