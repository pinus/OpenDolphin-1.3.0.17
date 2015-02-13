package open.dolphin.impl.scheam;

import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import open.dolphin.impl.scheam.constant.DefaultPresetColor;
import open.dolphin.impl.scheam.widget.PnsComboBox;
import open.dolphin.impl.scheam.widget.PnsIconCallback;

/**
 * Preset Color を選択するための PnsComboBox
 * @author pns
 */
public class PresetColorCombo extends PnsComboBox<List<ColorModel>> {

    /** これを Listen すると選択された ColorModel がとれる */
    private final ObjectProperty<ColorModel> selectionProxyProperty;

    public PresetColorCombo() {
        super();
        setFocusTraversable(false);

        List<ColorModel> redFill = DefaultPresetColor.getSeries(DefaultPresetColor.Series.Red, FillMode.Fill);
        List<ColorModel> deepRedFill = DefaultPresetColor.getSeries(DefaultPresetColor.Series.DeepRed, FillMode.Fill);
        List<ColorModel> deeperRedFill = DefaultPresetColor.getSeries(DefaultPresetColor.Series.DeeperRed, FillMode.Fill);
        List<ColorModel> brownFill = DefaultPresetColor.getSeries(DefaultPresetColor.Series.Brown, FillMode.Fill);
        List<ColorModel> deepBrownFill = DefaultPresetColor.getSeries(DefaultPresetColor.Series.DeepBrown, FillMode.Fill);
        List<ColorModel> purpleFill = DefaultPresetColor.getSeries(DefaultPresetColor.Series.Purple, FillMode.Fill);
        List<ColorModel> deepPurpleFill = DefaultPresetColor.getSeries(DefaultPresetColor.Series.DeepPurple, FillMode.Fill);
        List<ColorModel> grayLine = DefaultPresetColor.getSeries(DefaultPresetColor.Series.Gray, FillMode.Line);
        List<ColorModel> redLine = DefaultPresetColor.getSeries(DefaultPresetColor.Series.Red, FillMode.Line);
        List<ColorModel> special0 = DefaultPresetColor.getSpecialSeries(0);
        List<ColorModel> special1 = DefaultPresetColor.getSpecialSeries(1);
        List<ColorModel> special2 = DefaultPresetColor.getSpecialSeries(2);

        getItems().add(redFill);
        getItems().add(deepRedFill);
        getItems().add(deeperRedFill);
        getItems().add(brownFill);
        getItems().add(deepBrownFill);
        getItems().add(purpleFill);
        getItems().add(deepPurpleFill);
        getItems().add(grayLine);
        getItems().add(redLine);
        getItems().add(special0);
        getItems().add(special1);
        getItems().add(special2);

         setIconCallback(new PnsIconCallback<List<ColorModel>, Node>(){
            @Override
            public Node call(List<ColorModel> items) {
                HBox box = new HBox();
                box.setSpacing(2);
                box.setAlignment(Pos.CENTER);
                for (ColorModel m : items) {
                    box.getChildren().add(new ColorModelLabel(m));
                }
                return box;
            }
            @Override
            public Node callSelected(List<ColorModel> items) {
                return call(items);
            }
        });

        selectionProxyProperty = new SimpleObjectProperty<>();

        // 初期値
        setSelection(redFill);
    }

    /**
     * ColorModel を表示するラベル
     * クリックで Property に ColorModel をセットする
     */
    private class ColorModelLabel extends Rectangle {
        private ColorModel model;

        public ColorModelLabel(ColorModel m) {
            model = m;
            setWidth(20);
            setHeight(12);
            //setStrokeWidth(model.getLineWidth());
            setStrokeWidth(2);
            setStroke(model.getLineColor());
            if (model.getFillMode().equals(FillMode.Line)) {
                setFill(Color.TRANSPARENT);
            } else {
                setFill(model.getFillColor());
            }

            setOnMouseReleased(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent t) {
                    selectionProxyProperty.set(model);
                }
            });
        }
    }

    /**
     * マウスクリックで呼ばれる
     * super.showPopup すると Popup が開く
     * @param e
     */
    @Override
    public void showPopup(MouseEvent e) {
        // 右側の矢印をクリックした場合だけ Popup する
        if (e.getX() > getWidth() - 12) {
            super.showPopup(e);
        }
    }

    public ObjectProperty<ColorModel> selectionProxyProperty() { return selectionProxyProperty; }


    /**
    //-------------------------------------------------------
    public static void main (String[] argv) {
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
                        pane.setSpacing(5);
                        pane.setPadding(new Insets(5));
                        pane.setPrefSize(150, 100);

                        PresetColorCombo combo = new PresetColorCombo();
                        pane.getChildren().add(combo);

                        Scene scene = new Scene(pane);
                        scene.getStylesheets().add(StyleClass.CSS_FILE);
                        fxp.setScene(scene);
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
