package open.dolphin.impl.scheam.colorchooser;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import open.dolphin.impl.scheam.constant.DefaultPresetColor;
import open.dolphin.impl.scheam.helper.SchemaUtils;

/**
 * 色見本のパレットと opacity のスライダからなる color palette.
 * opacity は color に含めて扱う.
 * @author pns
 */
public class ColorPalette extends Pane {
    /** パレットの行数 */
    private static final int PALETTE_ROWS = 8;
    /** 各々のラベルのサイズ */
    private static final int PALETTE_LABEL_SIZE = 16;
    /** 色配列から作った color labels を保持する配列 */
    private final List<ColorLabel> labelList = new ArrayList<>();
    /** opacity を管理するスライダー */
    private final DoubleProperty opacityProperty = new SimpleDoubleProperty();
    /** 選択した色を保持する property. これは opacity も保持する.  */
    private final ObjectProperty<Color> colorProperty = new SimpleObjectProperty<>();
    /** Slider の valueChangingProperty に連動 */
    private final BooleanProperty valueChangingProperty = new SimpleBooleanProperty();

    /**
     * ColorPalette のコンストラクタ.
     */
    public ColorPalette() {
        // RadioButton のように Toggle 動作させるための group
        final ToggleGroup group = new ToggleGroup();

        // 色見本 label を入れるための TilePane
        final TilePane colorTile = new TilePane(2,2);
        colorTile.setOrientation(Orientation.VERTICAL);
        colorTile.setPrefRows(PALETTE_ROWS);

        // colorTile の生成. 色見本には opacity は反映しない.
        DefaultPresetColor.getColorList().forEach(color -> {
            ColorLabel label = new ColorLabel(color);
            label.setToggleGroup(group);
            colorTile.getChildren().add(label);
            labelList.add(label);
        });

        // opacity スライダ
        Slider slider = new Slider();
        slider.setMin(0.0);
        slider.setMax(1.0);
        slider.setMajorTickUnit(0.1);
        slider.setMinorTickCount(0);
        slider.setSnapToTicks(true);

        // slider と opacityProperty を bind
        slider.valueProperty().bindBidirectional(opacityProperty);
        slider.valueChangingProperty().bindBidirectional(valueChangingProperty);

        // opacityProperty の変化 → colorProperty を設定
        opacityProperty.addListener((ObservableValue<? extends Number> ov, Number t, Number opacity) -> {
            Color c = colorProperty.get();
            colorProperty.set(Color.color(c.getRed(), c.getGreen(), c.getBlue(), (double)opacity));
        });

        // 色見本の選択 → colorProperty を設定　色見本の色には opacity 情報が入ってないので opacityProperty と合成する
        group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle t, Toggle toggle) -> {
            // ToggleButton では同じ label を選択すると選択が解除されるので，t1 == null で入ってくる
            if (toggle == null) {
                // 前の Toggle を選択しなおす（RadioButton にすればその必要はないが，RadioButton はボタン画像がが消せない）
                t.setSelected(true);
            } else {
                Color c = ((ColorLabel)toggle).getColor();
                colorProperty.set(Color.color(c.getRed(), c.getGreen(), c.getBlue(), opacityProperty.get()));
            }
        });

        // color property の変化 → 色見本を選択　＆　opacityProperty を設定
        colorProperty.addListener((ObservableValue<? extends Color> ov, Color t, Color color) -> {
            // 色見本の選択
            for (ColorLabel label : labelList) {
                // 同じ色の color label を選択状態にする
                if (SchemaUtils.equalsExceptOpacity(label.getColor(), color)) {
                    // 同じ色が見つかって，かつ選択状態になければ選択する
                    if (! label.isSelected()) { group.selectToggle(label); }
                    break;
                }
            }
            // opacityProperty の設定
            opacityProperty.set(color.getOpacity());
        });

        // レイアウト
        VBox vbox = new VBox();
        vbox.setSpacing(8);
        vbox.getChildren().add(colorTile);
        vbox.getChildren().add(slider);
        getChildren().add(vbox);
    }

    /**
     * 選択色の property を返す
     * @return
     */
    public ObjectProperty<Color> colorProperty() { return colorProperty; }
    public Color getColor() { return colorProperty.get(); }
    public void setColor(Color c) { colorProperty.set(c);}

    /**
     * Slider の valueChangingProperty に連動
     * @return
     */
    public BooleanProperty valueChangingProperty() { return valueChangingProperty; }

    /**
     * パレットの各々の色のラベル
     */
    private class ColorLabel extends ToggleButton {
        // インスタンス毎にラベルの色を保持
        private final Color color;

        /**
         * color c をもつ color label を作る
         * @param c
         */
        public ColorLabel(Color c) {
            getStyleClass().add("schema-color-label");
            // to fix size, all three properties are mandatory
            setPrefSize(PALETTE_LABEL_SIZE, PALETTE_LABEL_SIZE);
            setMaxSize(PALETTE_LABEL_SIZE, PALETTE_LABEL_SIZE);
            setMinSize(PALETTE_LABEL_SIZE, PALETTE_LABEL_SIZE);
            color = c;
            Rectangle r = new Rectangle(PALETTE_LABEL_SIZE, PALETTE_LABEL_SIZE);
            r.setFill(c);
            setGraphic(r);
        }
        /**
         * この color label のもつ色を返す
         * @return
         */
        public Color getColor() {
            return color;
        }
    }
}
