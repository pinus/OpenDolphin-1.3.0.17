package open.dolphin.impl.scheam;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import open.dolphin.impl.scheam.constant.ShapeIcon;
import open.dolphin.impl.scheam.widget.PnsComboBox;
import open.dolphin.impl.scheam.widget.PnsListCell;

import java.util.HashMap;

/**
 * LineWidth を選択する ComboBox.
 *
 * @author pns
 */
public class LineWidthCombo extends PnsComboBox<Double> {
    /**
     * 線の太さのメニュー
     */
    private final static ObservableList<Double> LINE_WIDTH = FXCollections.observableArrayList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
    /**
     * Line アイコンの幅
     */
    private final static double ICON_WIDTH = 40;
    /**
     * {@code ObjectProperty<Double>} を DoubleProperty に変換するための Proxy
     */
    private final DoubleProperty selectionPropertyProxy;

    public LineWidthCombo() {
        super();
        getItems().addAll(LINE_WIDTH);
        setFocusTraversable(false);

        // properties と bindBidirectional できるように，
        // SimpleObjectProperty<Double> を SimpleDoubleProperty に変換するための Proxy
        selectionPropertyProxy = new SimpleDoubleProperty();
        selectionPropertyProxy.addListener((ObservableValue<? extends Number> ov, Number t, Number t1)
                -> selectedItemProperty().set((Double) t1));
        selectedItemProperty().addListener((ObservableValue<? extends Double> ov, Double t, Double t1)
                -> selectionPropertyProxy.set(t1));

        // icon callback
        Callback<ListView<Double>, ListCell<Double>> cellFactory = v -> new LineWidthCell();
        setButtonCell(cellFactory.call(null));
        setCellFactory(cellFactory);

        // state の変化で Enable/Disable する
        final SimpleObjectProperty<State> state = new SimpleObjectProperty<>();
        state.addListener((ObservableValue<? extends State> ov, State t, State s) -> {
            switch (s) {
                case Text:
                case Scale:
                case Rotate:
                case Clip:
                case Clear:
                case Undo:
                case Redo:
                    setDisable(true);
                    break;
                default:
                    setDisable(false);
            }
        });
        // bind
        state.bind(SchemaEditorImpl.getProperties().stateProperty());
    }

    public DoubleProperty selectionPropertyProxy() {
        return selectionPropertyProxy;
    }

    /**
     * Line width icon
     */
    private class LineWidthCell extends PnsListCell<Double> {
        private final HashMap<Double, Label> map;

        public LineWidthCell() {
            map = new HashMap<>();
            LINE_WIDTH.forEach(w -> {
                Label label = new Label();
                label.setGraphic(ShapeIcon.getLine(ICON_WIDTH, w));
                map.put(w, label);
            });
        }

        @Override
        public Label getCell(Double w) {
            return map.get(w);
        }
    }
}
