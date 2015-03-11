package open.dolphin.impl.scheam;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Callback;
import open.dolphin.impl.scheam.constant.Const;
import open.dolphin.impl.scheam.constant.ShapeIcon;
import open.dolphin.impl.scheam.widget.PnsToggleSet;

/**
 * FillMode (line, fill, mixed） を Toggle で切り替える.
 * @author pns
 */
public class FillModeToggle extends PnsToggleSet<FillMode> {
    private final Shape line, fill, mix, sline, sfill, smix;

    public FillModeToggle() {
        double r = 6;
        line = ShapeIcon.getCircle(r, r, Const.PNS_BLACK, Color.TRANSPARENT);
        fill = ShapeIcon.getCircle(r, r, Const.PNS_GLAY, Const.PNS_GLAY);
        mix = ShapeIcon.getCircle(r, r, Const.PNS_BLACK, Const.PNS_GLAY);
        sline = ShapeIcon.getCircle(r, r, Const.PNS_WHITE, Color.TRANSPARENT);
        sfill = ShapeIcon.getCircle(r, r, Const.PNS_LIGHT_GLAY, Const.PNS_LIGHT_GLAY);
        smix = ShapeIcon.getCircle(r, r, Const.PNS_WHITE, Const.PNS_LIGHT_GLAY);

        // Cell Factory
        Callback<FillMode, Node> cellFactory = f -> {
            FillMode selectedMode = getSelectionModel().getSelectedItem();

            switch(f) {
                case Line:
                    if (selectedMode == FillMode.Line) { return sline; }
                    else { return line; }

                case Fill:
                    if (selectedMode == FillMode.Fill) { return sfill; }
                    else { return fill; }

                case Mixed:
                    if (selectedMode == FillMode.Mixed) { return smix; }
                    else { return mix; }
            }
            return null;
        };

        setCellFactory(cellFactory);

        // state の変化で FillModeToggle のアイコンを変える
        final ObjectProperty<State> state = new SimpleObjectProperty<>();

        state.addListener((ObservableValue<? extends State> ov, State t, State s) -> {
            switch(s) {
                case Oval:
                case Rectangle:
                case Polygon:
                case Dots:
                case Net:
                    setDisable(false);
                    break;
                default:
                    setDisable(true);
            }
        });
        // bind
        state.bind(SchemaEditorImpl.getProperties().stateProperty());
    }
}
