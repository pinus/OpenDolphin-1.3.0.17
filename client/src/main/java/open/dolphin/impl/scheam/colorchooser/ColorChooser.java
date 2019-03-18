package open.dolphin.impl.scheam.colorchooser;

import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.SchemaEditorProperties;
import open.dolphin.impl.scheam.helper.SchemaUtils;

/**
 * ColorChooser for line and fill color.
 *
 * @author pns
 */
public class ColorChooser extends Popup {
    private static final int GAP = 8;
    private final SchemaEditorProperties properties;
    private final ColorPalette linePalette;
    private final ColorPalette fillPalette;
    private final Slider blurSlider;
    private Color origFillColor;
    private double origFillBlur;

    public ColorChooser() {
        properties = SchemaEditorImpl.getProperties();
        setAutoHide(true);

        // startColor palette を2つ作る
        linePalette = new ColorPalette();
        fillPalette = new ColorPalette();

        // fill palette 用のラベル
        Label label = new Label("Fill Color");

        // fill blur スライダー
        blurSlider = new Slider();
        blurSlider.setMin(0.0);
        blurSlider.setMax(0.8);
        blurSlider.setMajorTickUnit(0.1);
        blurSlider.setMinorTickCount(0);
        blurSlider.setShowTickLabels(true);
        blurSlider.setShowTickMarks(true);
        blurSlider.setSnapToTicks(true);

        // ラベル，スライダー付きの fill palette
        VBox fillPalettePane = new VBox();
        fillPalettePane.setSpacing(GAP / 2);
        fillPalettePane.setAlignment(Pos.TOP_CENTER);
        fillPalettePane.getChildren().addAll(label, fillPalette, blurSlider);

        // line palette 用のラベル
        label = new Label("Line Color");

        // 情報 pane
        ColorInfoPane infoPane = new ColorInfoPane();

        // line palette 用の pane を作って上記を並べる
        VBox linePalettePane = new VBox();
        linePalettePane.setAlignment(Pos.TOP_CENTER);
        linePalettePane.setSpacing(GAP / 2);
        linePalettePane.getChildren().addAll(label, linePalette, infoPane);

        // fill palette と line palette を並べる
        HBox palettePane = new HBox();
        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        separator.setValignment(VPos.CENTER);
        separator.setPrefWidth(GAP * 2);

        palettePane.getChildren().addAll(linePalettePane, separator, fillPalettePane);

        // Button
        Button okButton = new Button();
        okButton.setDefaultButton(true);
        okButton.setText("OK");
        okButton.setOnAction(e -> hide());

        Button cancelButton = new Button();
        cancelButton.setCancelButton(true);
        cancelButton.setText("Cancel");
        cancelButton.setOnAction(e -> {
            // colorProperty は properties にバインドされるので，colorProperty をセットすれば properties に反映される
            fillPalette.colorProperty().set(origFillColor);
            // blur は color palette にはない
            properties.setFillBlur(origFillBlur);
            hide();
        });
        // ボタンを入れる pane
        HBox buttonPane = new HBox();
        buttonPane.setAlignment(Pos.BASELINE_RIGHT);
        buttonPane.setSpacing(8);
        buttonPane.getChildren().addAll(cancelButton, okButton);

        // 全体をまとめて背景，影をつける pane
        VBox pane = new VBox();
        pane.getStyleClass().add("schema-color-chooser");
        pane.setPadding(new Insets(GAP));
        pane.getChildren().addAll(palettePane, buttonPane);

        getContent().add(pane);

        // pane をつかんで移動できるようにする
        pane.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
            private double x, y;
            private double toX = -1, toY = -1;

            @Override
            public void handle(MouseEvent t) {
                if (MouseEvent.MOUSE_RELEASED.equals(t.getEventType())) {
                    toX = -1;

                } else if (MouseEvent.MOUSE_DRAGGED.equals(t.getEventType())) {
                    if (toX == -1) {
                        toX = ColorChooser.this.getX();
                        toY = ColorChooser.this.getY();

                    } else {
                        toX += (t.getScreenX() - x);
                        toY += (t.getScreenY() - y);
                        ColorChooser.this.setX(toX);
                        ColorChooser.this.setY(toY);
                    }
                    x = t.getScreenX();
                    y = t.getScreenY();
                }
            }
        });

        // bind
        linePalette.colorProperty().bindBidirectional(properties.lineColorProperty());
        fillPalette.colorProperty().bindBidirectional(properties.fillColorProperty());
        blurSlider.valueProperty().bindBidirectional(properties.fillBlurProperty());
    }

    /**
     * 位置補正して show.
     *
     * @param source
     */
    public void show(Node source) {
        if (isShowing()) {
            hide();

        } else {
            // 現在の値をバックアップ
            origFillColor = properties.getFillColor();
            origFillBlur = properties.getFillBlur();

            Point2D p = SchemaUtils.getScreenLocation(source);
            Bounds b = source.getBoundsInLocal();

            show(source, p.getX() + b.getWidth() / 2, p.getY() + b.getHeight() - 16);

            properties.valueChangingProperty().set(true);
        }
    }

    @Override
    public final void hide() {
        super.hide();
        properties.valueChangingProperty().set(false);
    }

    /**
     * 色の数値情報を表示する pane.
     */
    private class ColorInfoPane extends GridPane {
        private final Font font = new Font(11);

        public ColorInfoPane() {
            //setGridLinesVisible(true);
            // 文字ラベル
            Label lineLabel = new Label("Line");
            lineLabel.setPrefWidth(30);
            lineLabel.setFont(font);
            Label fillLabel = new Label("Fill");
            fillLabel.setFont(font);
            Label blurLabel = new Label("Fill Blur");
            blurLabel.setFont(font);

            // 数値ラベル
            final Label lRed = buildLabel();
            final Label lGreen = buildLabel();
            final Label lBlue = buildLabel();
            final Label lOpacity = buildLabel();
            final Label fRed = buildLabel();
            final Label fGreen = buildLabel();
            final Label fBlue = buildLabel();
            final Label fOpacity = buildLabel();
            final Label blur = buildLabel();
            // 初期値
            setValue(lRed, lGreen, lBlue, lOpacity, properties.getLineColor());
            setValue(fRed, fGreen, fBlue, fOpacity, properties.getFillColor());
            setValue(blur, properties.getFillBlur());

            add(lineLabel, 1, 1);
            add(fillLabel, 1, 2);
            add(blurLabel, 1, 3, 4, 1);

            add(lRed, 2, 1);
            add(lGreen, 3, 1);
            add(lBlue, 4, 1);
            add(lOpacity, 5, 1);
            add(fRed, 2, 2);
            add(fGreen, 3, 2);
            add(fBlue, 4, 2);
            add(fOpacity, 5, 2);
            add(blur, 5, 3);

            properties.lineColorProperty().addListener((ObservableValue<? extends Color> ov, Color t, Color col) -> setValue(lRed, lGreen, lBlue, lOpacity, col));
            properties.fillColorProperty().addListener((ObservableValue<? extends Color> ov, Color t, Color col) -> setValue(fRed, fGreen, fBlue, fOpacity, col));
            properties.fillBlurProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number b) -> setValue(blur, (double) b));
        }

        private Label buildLabel() {
            Label label = new Label();
            label.setPrefWidth(28);
            label.setAlignment(Pos.BASELINE_RIGHT);
            label.setFont(font);
            return label;
        }

        private void setValue(Label red, Label green, Label blue, Label opacity, Color c) {
            red.setText(String.format("%d", (int) (c.getRed() * 255)));
            green.setText(String.format("%d", (int) (c.getGreen() * 255)));
            blue.setText(String.format("%d", (int) (c.getBlue() * 255)));
            opacity.setText(String.format("%.1f", c.getOpacity()));
        }

        private void setValue(Label l, double d) {
            l.setText(String.format("%.1f", d));
        }
    }

/*
    //ーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー
    public static void main(String[] s) {
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
                        pane.setPrefSize(150, 100);
                        Button button = new Button("Color Chooser");
                        final ColorChooser chooser = new ColorChooser();
                        button.setOnAction(new EventHandler<ActionEvent>(){
                            @Override
                            public void handle(ActionEvent t) {
                                chooser.show((Node)t.getSource());
                            }
                        });
                        pane.getChildren().addAll(new Button("Dummy"), button);
                        Scene scene = new Scene(pane);
                        scene.getStylesheets().add(StyleClass.CSS_FILE);
                        fxp.setScene(scene);
                        pane.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>(){
                            private int x, y;
                            private java.awt.Rectangle r;
                            @Override
                            public void handle(MouseEvent t) {
                                if (MouseEvent.MOUSE_RELEASED.equals(t.getEventType())) {
                                    r = null;

                                } else if (MouseEvent.MOUSE_DRAGGED.equals(t.getEventType())) {
                                    if (r == null) {
                                        r = frame.getBounds();
                                    } else {
                                        r.x += ( (int)t.getScreenX() - x );
                                        r.y += ( (int)t.getScreenY() - y );
                                        frame.setBounds(r);
                                    }
                                    x = (int)t.getScreenX();
                                    y = (int)t.getScreenY();
                                }
                            }
                        });
                    }
                });

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
    */
}
