package open.dolphin.impl.scheam;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import open.dolphin.impl.scheam.iconcallback.*;
import open.dolphin.impl.scheam.widget.PnsIconCallback;
import open.dolphin.impl.scheam.widget.PnsToggleSet;

/**
 * FillMode (line, fill, mixed） を Toggle で切り替える
 * @author pns
 */
public class FillModeToggle extends PnsToggleSet<FillMode> {

    public FillModeToggle(double w, double h) {
        super(w, h);

        final PnsIconCallback<FillMode, Node> ovalIconCallback = new IconCallbackOval();
        final PnsIconCallback<FillMode, Node> rectangleIconCallback = new IconCallbackRectangle();
        final PnsIconCallback<FillMode, Node> polygonIconCallback = new IconCallbackPolygon();
        final ObjectProperty<State> state = new SimpleObjectProperty<>();

        // state の変化で FillModeToggle のアイコンを変える
        state.addListener(new ChangeListener<State>(){
            @Override
            public void changed(ObservableValue<? extends State> ov, State t, State t1) {
                setDisable(false);
                switch(t1) {
                    case Oval:
                        setIconCallback(ovalIconCallback);
                        break;
                    case Rectangle:
                        setIconCallback(rectangleIconCallback);
                        break;
                    case Polygon:
                    case Dots:
                    case Net:
                        setIconCallback(polygonIconCallback);
                        break;
                    default:
                        setIconCallback(ovalIconCallback);
                        setDisable(true);
                }
            }
        });
        // bind
        state.bind(SchemaEditorImpl.getProperties().stateProperty());
    }
}
