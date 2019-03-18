package open.dolphin.impl.scheam.widget;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * @author pns
 */
public class PnsWidgetSampler extends Application {
    private static final Insets MARGIN = new Insets(10);
    private PnsStage stage;

    public static void main(String[] argv) {
        Application.launch(argv);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = new PnsStage();
        stage.setTitle("PnsWidget Sampler");
        // content を入れる pane
        final PnsCardPane cardPane = new PnsCardPane();

        /**
         * Tool Pane
         */
        PnsToggleSet<String> selector = new PnsToggleSet<>();
        String[] category = {"ウィジェット", "タブ"};
        selector.getItems().addAll(category);
        selector.selectedItemProperty().addListener((ov, prev, selected) -> cardPane.show(selected));
        StackPane.setMargin(selector, new Insets(3, 10, 8, 10));
        stage.addTool(selector);

        /**
         * Content Pane
         */
        Node widget = getWidgetCard();
        StackPane.setMargin(widget, MARGIN);
        cardPane.addCard(widget, category[0]);

        Node tab = getTabCard();
        StackPane.setMargin(tab, new Insets(10, 16, 16, 16));
        cardPane.addCard(tab, category[1]);

        selector.selectedIndexProperty().set(0);

        stage.addContent(cardPane);
        stage.setWidth(640);
        stage.setHeight(480);
        stage.show();
    }

    /**
     * いろいろな Widget.
     *
     * @return
     */
    private Node getWidgetCard() {
        // Text を使う → Label を使ってフォントを変えるとおかしくなる
        Text text = new Text();
        // CheckBox
        CheckBox checkBox = new CheckBox();
        checkBox.setText("apply pns-stage.css");
        checkBox.selectedProperty().addListener((ov, o, n) -> {
            if (n) {
                stage.getScene().getStylesheets().add("css/pns-stage.css");
            } else {
                stage.getScene().getStylesheets().clear();
            }
        });
        checkBox.setSelected(true);
        checkBox.setTooltip(new Tooltip("Check here to apply pns-stage.css"));
        // TextField
        TextField textField = new TextField();
        text.textProperty().bind(textField.textProperty());
        textField.setText("TextField");

        // ComboBox
        PnsComboBox<String> comboBox = new PnsComboBox<>();
        comboBox.setPrefWidth(160);
        comboBox.getItems().addAll("Verdana", "ヒラギノ丸ゴシック", "Courier");
        comboBox.selectedItemProperty().addListener((ov, o, n) -> {
            switch (n) {
                case "Verdana":
                    text.setFont(Font.font("Verdana"));
                    break;
                case "ヒラギノ丸ゴシック":
                    text.setFont(Font.font("Hiragino Maru Gothic ProN"));
                    break;
                case "Courier":
                    text.setFont(Font.font("Courier"));
                    break;
            }
        });
        comboBox.getSelectionModel().select(2);
        // Slider
        Slider slider = new Slider(1, 3, 1);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(5);
        slider.setValue(1);
        slider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double n) {
                if (n == 1) {
                    return "小";
                } else if (n == 2) {
                    return "中";
                } else {
                    return "大";
                }
            }

            @Override
            public Double fromString(String string) {
                return null;
            }
        });
        slider.valueProperty().addListener((ov, o, n) -> {
            text.setScaleX((double) n);
            text.setScaleY((double) n);
        });
        // ContextMenu
        ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("不透明");
        item1.setOnAction(e -> text.setOpacity(1.0));
        MenuItem item2 = new MenuItem("opacity 0.5");
        item2.setOnAction(e -> text.setOpacity(0.5));
        MenuItem item3 = new MenuItem("opacity 0.1");
        item3.setOnAction(e -> text.setOpacity(0.1));
        contextMenu.getItems().addAll(item1, new SeparatorMenuItem(), item2, item3);
        checkBox.setContextMenu(contextMenu);
        // ProgressBar
        Button startButton = new Button("START");
        startButton.setPrefWidth(96);
        ProgressBar bar = new ProgressBar(0);
        bar.setPrefWidth(160);
        Service service = new Service<Integer>() {
            @Override
            protected Task<Integer> createTask() {
                Task task = new Task<Integer>() {
                    @Override
                    protected Integer call() throws Exception {
                        for (int i = 0; i < 100; i++) {
                            updateProgress(i + 1, 100);
                            if (isCancelled()) {
                                break;
                            }
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                            }
                        }
                        return 0;
                    }

                    @Override
                    protected void cancelled() {
                        updateProgress(-1, 100);
                    }

                    @Override
                    protected void succeeded() {
                        updateProgress(0, 100);
                    }
                };
                return task;
            }
        };
        startButton.setOnAction(e -> {
            if (service.isRunning()) {
                service.cancel();
                bar.progressProperty().unbind();
            } else {
                service.restart();
                bar.progressProperty().bind(service.progressProperty());
            }
        });
        service.runningProperty().addListener((ov, o, running) -> {
            if (running) {
                startButton.setText("STOP");
            } else {
                startButton.setText("START");
            }
        });
        // TextArea
        TextArea textArea = new TextArea();
        textArea.setPrefColumnCount(10);
        textArea.setPrefRowCount(3);
        textArea.setText("春はあけぼの。"
                + "やうやう白くなりゆく山際、少しあかりて、紫だちたる雲の細くたなびきたる。");

        // Widget 部分のレイアウト
        GridPane widgetPane = new GridPane();
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setHalignment(HPos.RIGHT);
        widgetPane.getColumnConstraints().add(0, col0);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.LEFT);
        widgetPane.getColumnConstraints().add(1, col1);
        widgetPane.setHgap(8);
        widgetPane.setVgap(16);
        widgetPane.add(new Label("表示テキスト："), 0, 0);
        widgetPane.add(textField, 1, 0);
        widgetPane.add(new Label("フォント："), 0, 1);
        widgetPane.add(comboBox, 1, 1);
        widgetPane.add(new Label("大きさ："), 0, 3);
        widgetPane.add(slider, 1, 3);
        widgetPane.add(new Label("プログレスバー："), 0, 4);
        widgetPane.add(bar, 1, 4);
        widgetPane.add(startButton, 2, 4);
        widgetPane.add(new Label("テキストエリア："), 0, 5);
        widgetPane.add(textArea, 1, 5, 2, 1);

        // Label 部分のレイアウト
        HBox labelPane = new HBox();
        labelPane.setMinHeight(36);
        labelPane.setMaxHeight(36);
        labelPane.setPrefHeight(36);
        Region spacer1 = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        HBox.setHgrow(text, Priority.ALWAYS);
        HBox.setHgrow(checkBox, Priority.NEVER);
        labelPane.getChildren().addAll(spacer1, text, spacer2, checkBox);
        // 全体のレイアウト
        VBox pane = new VBox();
        pane.getChildren().addAll(labelPane, widgetPane);
        return pane;
    }

    /**
     * TabPane のサンプル.
     *
     * @return
     */
    private Node getTabCard() {
        PnsTabPane pane = new PnsTabPane();
        pane.setSide(Side.TOP);

        Tab tab1 = new Tab();
        ImageView image = new ImageView("http://pns.cocolog-nifty.com/logo400.jpg");
        image.setScaleX(2.0);
        image.setScaleY(2.0);
        StackPane content1 = new StackPane();
        content1.getChildren().add(image);
        tab1.setClosable(false);
        tab1.setText("うさぎ");
        tab1.setContent(content1);

        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("ブランク");

        Tab tab3 = new Tab();
        SVGPath svg = new SVGPath();
        svg.setContent("M100,10 L100,10 40,180 190,60 10,60 160,180 z");
        StackPane content2 = new StackPane();
        content2.getChildren().add(svg);
        tab3.setClosable(false);
        tab3.setText("SVGPath");
        tab3.setContent(content2);

        pane.getTabs().addAll(tab1, tab2, tab3);
        return pane;
    }
}
