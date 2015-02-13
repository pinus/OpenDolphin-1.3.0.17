package open.dolphin.impl.scheam.colorchooser;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorBuilder;
import javafx.scene.control.Slider;
import javafx.scene.control.SliderBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.SchemaEditorProperties;
import open.dolphin.impl.scheam.constant.StyleClass;
import open.dolphin.impl.scheam.helper.SchemaUtils;

/**
 * ColorChooser for line and fill color
 * @author pns
 */
public class ColorChooser extends Popup {
    private static final int GAP = 8;
    private final SchemaEditorProperties properties;
    private Color origFillColor;
    private double origFillBlur;

    private final ColorPalette linePalette;
    private final ColorPalette fillPalette;
    private final Slider blurSlider;

    public ColorChooser() {
        properties = SchemaEditorImpl.getProperties();
        setAutoHide(true);

        // startColor palette を2つ作る
        linePalette = new ColorPalette();
        fillPalette = new ColorPalette();

        // fill palette 用のラベル
        Label label = new Label("Fill Color");
        // fill blur スライダー
        blurSlider = SliderBuilder.create()
            .min(0.0).max(0.8)
            .majorTickUnit(0.1).minorTickCount(0)
            .showTickLabels(true).showTickMarks(true).snapToTicks(true).build();
        // ラベル，スライダー付きの fill palette
        VBox fillPalettePane = new VBox();
        fillPalettePane.setSpacing(GAP/2);
        fillPalettePane.setAlignment(Pos.TOP_CENTER);
        fillPalettePane.getChildren().addAll(label, fillPalette, blurSlider);

        // line palette 用のラベル
        label = new Label("Line Color");

        // 情報 pane
        ColorInfoPane infoPane = new ColorInfoPane();

        // line palette 用の pane を作って上記を並べる
        VBox linePalettePane = new VBox();
        linePalettePane.setAlignment(Pos.TOP_CENTER);
        linePalettePane.setSpacing(GAP/2);
        linePalettePane.getChildren().addAll(label, linePalette, infoPane);

        // fill palette と line palette を並べる
        HBox palettePane = new HBox();
        Separator separator = SeparatorBuilder.create()
                .orientation(Orientation.VERTICAL).prefWidth(GAP*2).valignment(VPos.CENTER).build();
        palettePane.getChildren().addAll(linePalettePane, separator, fillPalettePane);

        // Button
        Button okButton = ButtonBuilder.create()
                .defaultButton(true).text("OK").build();
        okButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                hide();
            }
        });

        Button cancelButton = ButtonBuilder.create()
                .cancelButton(true).text("Cancel").build();
        cancelButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                // colorProperty は properties にバインドされるので，colorProperty をセットすれば properties に反映される
                fillPalette.colorProperty().set(origFillColor);
                // blur は color palette にはない
                properties.setFillBlur(origFillBlur);
                hide();
            }
        });
        // ボタンを入れる pane
        HBox buttonPane = new HBox();
        buttonPane.setAlignment(Pos.BASELINE_RIGHT);
        buttonPane.setSpacing(8);
        buttonPane.getChildren().addAll(cancelButton, okButton);

        // 全体をまとめて背景，影をつける pane
        VBox pane = new VBox();
        pane.getStyleClass().add(StyleClass.SCHEMA_COLOR_PALETTE);
        pane.setPadding(new Insets(GAP));
        pane.getChildren().addAll(palettePane, buttonPane);

        getContent().add(pane);

        // pane をつかんで移動できるようにする
        pane.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>(){
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
                        toX += ( t.getScreenX() - x );
                        toY += ( t.getScreenY() - y );
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
     * 位置補正して show
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

            show(source, p.getX() + b.getWidth()/2, p.getY() + b.getHeight() - 16);

            properties.valueChangingProperty().set(true);
        }
    }

    @Override
    public void hide() {
        super.hide();
        properties.valueChangingProperty().set(false);
    }

    /**
     * 色の数値情報を表示する pane
     */
    private class ColorInfoPane extends GridPane {
        private final Font font = new Font(11);

        public ColorInfoPane() {
            //setGridLinesVisible(true);
            // 文字ラベル
            Label lineLabel = LabelBuilder.create().text("Line").font(font).prefWidth(30).build();
            Label fillLabel = LabelBuilder.create().text("Fill").font(font).build();
            Label blurLabel = LabelBuilder.create().text("Fill Blur").font(font).build();
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

            add(lRed, 2, 1); add(lGreen, 3, 1); add(lBlue, 4, 1); add(lOpacity, 5, 1);
            add(fRed, 2, 2); add(fGreen, 3, 2); add(fBlue, 4, 2); add(fOpacity, 5, 2);
            add(blur, 5, 3);

            properties.lineColorProperty().addListener(new ChangeListener<Color>() {
                @Override
                public void changed(ObservableValue<? extends Color> ov, Color t, Color t1) {
                    setValue(lRed, lGreen, lBlue, lOpacity, t1);
                }
            });
            properties.fillColorProperty().addListener(new ChangeListener<Color>() {
                @Override
                public void changed(ObservableValue<? extends Color> ov, Color t, Color t1) {
                    setValue(fRed, fGreen, fBlue, fOpacity, t1);
                }
            });
            properties.fillBlurProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                    setValue(blur, (double)t1);
                }
            });
        }
        private Label buildLabel() {
            return LabelBuilder.create().prefWidth(28).alignment(Pos.BASELINE_RIGHT).font(font).build();
        }
        private void setValue(Label red, Label green, Label blue, Label opacity, Color c) {
            red.setText(String.format("%d", (int)(c.getRed()*255)));
            green.setText(String.format("%d", (int)(c.getGreen()*255)));
            blue.setText(String.format("%d", (int)(c.getBlue()*255)));
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
