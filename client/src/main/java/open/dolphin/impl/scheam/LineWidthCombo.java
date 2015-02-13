package open.dolphin.impl.scheam;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.shape.Shape;
import open.dolphin.impl.scheam.constant.Const;
import open.dolphin.impl.scheam.helper.ShapeIcon;
import open.dolphin.impl.scheam.widget.PnsComboBox;
import open.dolphin.impl.scheam.widget.PnsIconCallback;

/**
 * LineWidth を選択する ComboBox
 * @author pns
 */
public class LineWidthCombo extends PnsComboBox<Double> {
    /** 線の太さのメニュー */
    private final static ObservableList<Double> LINE_WIDTH = FXCollections.observableArrayList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 );
    /** Line アイコンの幅 */
    private final static double ICON_WIDTH = 40;
    /** ObjectProperty<Double> を DoubleProperty に変換するための Proxy */
    private final DoubleProperty selectionPropertyProxy;

    public LineWidthCombo() {
        super();
        getItems().addAll(LINE_WIDTH);
        setIconCallback(new PnsIconCallback<Double, Node>(){
            @Override
            public Node call(Double item) {
                Shape s = ShapeIcon.getLine(ICON_WIDTH, item);
                s.setFill(Const.PNS_BLACK);
                return s;
            }
            @Override
            public Node callSelected(Double item) {
                Shape s = ShapeIcon.getLine(ICON_WIDTH, item);
                s.setFill(Const.PNS_WHITE);
                return s;
            }
        });
        setFocusTraversable(false);

        // properties と bindBidirectional できるように，
        // SimpleObjectProperty<Double> を SimpleDoubleProperty に変換するための Proxy
        selectionPropertyProxy = new SimpleDoubleProperty();
        selectionPropertyProxy.addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                selectionProperty().set((Double)t1);
            }
        });
        selectionProperty().addListener(new ChangeListener<Double>(){
            @Override
            public void changed(ObservableValue<? extends Double> ov, Double t, Double t1) {
                selectionPropertyProxy.set(t1);
            }
        });

        // state の変化で Enable/Disable する
        final SimpleObjectProperty<State> state = new SimpleObjectProperty<>();
        state.addListener(new ChangeListener<State>(){
            @Override
            public void changed(ObservableValue<? extends State> ov, State t, State t1) {
                switch(t1) {
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
            }
        });
        // bind
        state.bind(SchemaEditorImpl.getProperties().stateProperty());

    }

    public DoubleProperty selectionPropertyProxy() { return selectionPropertyProxy; }
}
