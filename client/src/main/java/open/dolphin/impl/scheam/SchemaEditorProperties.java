package open.dolphin.impl.scheam;

import java.util.prefs.Preferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import open.dolphin.impl.scheam.constant.Default;
import open.dolphin.impl.scheam.helper.SchemaUtils;

/**
 * SchemaEditorProperties.
 * SchemaEditor で使われる変数を一括管理する.
 * @author pns
 */
public class SchemaEditorProperties {
    private static Preferences prefs;

    // プレファレンスファイルに load/save する変数
    private final DoubleProperty lineWidth = new SimpleDoubleProperty(Default.LINE_WIDTH.value);
    private final ObjectProperty<Color> lineColor = new SimpleObjectProperty<>(Default.LINE_COLOR.value);
    private final ObjectProperty<Color> fillColor = new SimpleObjectProperty<>(Default.FILL_COLOR.value);
    private final DoubleProperty fillBlur = new SimpleDoubleProperty(Default.FILL_BLUR.value);
    private final ObjectProperty<FillMode> fillMode = new SimpleObjectProperty<>(Default.FILL_MODE.value);
    private final ObjectProperty<State> state = new SimpleObjectProperty<>(Default.STATE.value);
    private final DoubleProperty fontSize = new SimpleDoubleProperty(Default.FONT_SIZE.value);

    // プファレンス非対応変数
    private final ObjectProperty<State> previewState = new SimpleObjectProperty<>(Default.STATE.value);
    private final StringProperty fontName = new SimpleStringProperty(Default.FONT_NAME.value);
    private final ObjectProperty<FontWeight> fontWeight = new SimpleObjectProperty<>(FontWeight.BOLD);

    // UndoManager で使う。これが true なら properties が変更中。Holder の valueChangingProperty に bind される。
    private final BooleanProperty valueChangingProperty = new SimpleBooleanProperty();

    // ColorModel に関連したプロパティー
    private final Property[] propertiesRelatedToColorModel = { lineWidth, lineColor, fillColor, fillBlur, fillMode };
    // 描画 State のリスト
    private final State[] drawStates = {State.Pen, State.Line, State.Oval, State.Rectangle, State.Polygon, State.Dots, State.Net };

    public SchemaEditorProperties() {
        // property 保存のための preference　~/Library/Preferences/open.dolphin.impl.plist
        prefs = Preferences.userNodeForPackage(SchemaEditorImpl.class);

        // State 変化で PreviewState も変化
        state.addListener((ObservableValue<? extends State> ov, State t, State t1) -> {
            for (State s : drawStates) {
                if (t1.equals(s)) {
                    previewState.set(t1);
                    break;
                }
            }
        });
    }

    /**
     * ColorModel に関連したプロパティー.
     * これらのうちどれかが変化すると ColorModel も変化すべき.
     * @return
     */
    public Property[] getPropertiesRelatedToColorModel() { return propertiesRelatedToColorModel; }

    /**
     * UndoManager で途中経過を記録したいときはこれを利用する
     * Slider の valueChangingProperty と bindBidirectional したりして使う
     * When true, indicates the current value of this Property is changing.
     * @return
     */
    public BooleanProperty valueChangingProperty() { return valueChangingProperty; }

    /**
     * 線の太さ.
     * @return
     */
    public DoubleProperty lineWidthProperty() { return lineWidth; }
    public double getLineWidth() { return lineWidth.get(); }
    public void setLineWidth(double d) { lineWidth.set(d); }

    /**
     * 線の色.
     * @return
     */
    public ObjectProperty<Color> lineColorProperty() { return lineColor; }
    public Color getLineColor() { return lineColor.get(); }
    public void setLineColor(Color c) { lineColor.set(c); }

    /**
     * FillColor.
     * @return
     */
    public ObjectProperty<Color> fillColorProperty() { return fillColor; }
    public Color getFillColor() { return fillColor.get(); }
    public void setFillColor(Color c) { fillColor.set(c); }

    /**
     * blur property　直径に対して 0.0 〜 1.0 倍.
     * @return
     */
    public DoubleProperty fillBlurProperty() { return fillBlur; }
    public double getFillBlur() { return fillBlur.get(); }
    public void setFillBlur(double d) { fillBlur.set(d); }

    /**
     * 塗りのモード.
     * @return
     */
    public ObjectProperty<FillMode> fillModeProperty() { return fillMode; }
    public FillMode getFillMode() { return fillMode.get(); }
    public void setFillMode(FillMode f) { fillMode.set(f); }

    /**
     * フォント名.
     * @return
     */
    public StringProperty fontNameProperty() { return fontName; }
    public String getFontName() { return fontName.get(); }
    public void setFontName(String s) { fontName.set(s); }

    /**
     * フォントスタイル.
     * @return
     */
    public ObjectProperty<FontWeight> fontWeightProperty() { return fontWeight; }
    public FontWeight getFontWeight() { return fontWeight.get(); }
    public void setFontWeight(FontWeight w) { fontWeight.set(w); }

    /**
     * フォントサイズ.
     * @return
     */
    public DoubleProperty fontSizeProperty() { return fontSize; }
    public double getFontSize() { return fontSize.get(); }
    public void setFontSize(double d) { fontSize.set(d); }

    /**
     * フォントを作って返す.
     * @return
     */
    public Font getFont() {
        return Font.font(getFontName(), getFontWeight(), getFontSize());
    }

    /**
     * 選択されたツールボタンの種類（State）.
     * @return
     */
    public ObjectProperty<State> stateProperty() { return state; }
    public State getState() { return state.get(); }
    public void setState(State s) { state.set(s); }

    /**
     * Preview 画面に表示する State : Pen, Line, Oval, Rectangle, Polygon.
     * @return
     */
    public ObjectProperty<State> previewStateProperty() { return previewState; }
    public State getPreviewState() { return previewState.get(); }
    public void setPreviewState(State s) { previewState.set(s); }

    /**
     * プレファレンスファイルから読み込む.
     */
    public void load() {
        setLineWidth( prefs.getDouble(Default.LINE_WIDTH.key, Default.LINE_WIDTH.value)                         );
        setLineColor( Color.web(prefs.get(Default.LINE_COLOR.key, Default.LINE_COLOR.string) )                  );
        setState(     State.valueOf( prefs.get(Default.STATE.key, Default.STATE.value.toString()) )             );
        setFillColor( Color.web(prefs.get(Default.FILL_COLOR.key, Default.FILL_COLOR.string))                   );
        setFillBlur(  prefs.getDouble(Default.FILL_BLUR.key, Default.FILL_BLUR.value)                           );
        setFillMode(  FillMode.valueOf( prefs.get(Default.FILL_MODE.key, Default.FILL_MODE.value.toString()) )  );
        setFontSize(  prefs.getDouble(Default.FONT_SIZE.key, Default.FONT_SIZE.value)                           );
    }
    /**
     * プレファレンスファイルに保存する.
     */
    public void save() {
        prefs.putDouble( Default.LINE_WIDTH.key,    getLineWidth());
        prefs.put(       Default.LINE_COLOR.key,    SchemaUtils.colorToString(getLineColor()));
        prefs.put(       Default.FILL_COLOR.key,    SchemaUtils.colorToString(getFillColor()));
        prefs.putDouble( Default.FILL_BLUR.key,     getFillBlur());
        prefs.put      ( Default.FILL_MODE.key,     getFillMode().name());
        prefs.putDouble( Default.FONT_SIZE.key,     getFontSize());

        // 描画 State 以外は保存しない
        for (State s : drawStates) {
            if (s.equals(getState())) {
                prefs.put(Default.STATE.key, s.name());
                break;
            }
        }
    }

    public static void main(String[] arg) {
        SchemaEditorProperties p = new SchemaEditorProperties();
        String str = SchemaUtils.colorToString(Color.web("#01234567"));
        System.out.println(str);
        System.out.println(Color.web(str));
    }
}
