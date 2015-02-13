package open.dolphin.impl.scheam;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import open.dolphin.impl.scheam.helper.SchemaUtils;

/**
 * Tool を載せる pane
 * @author pns
 */
public class ToolPane extends HBox {
    /** ツールボタンの高さ */
    private final static double BUTTON_HEIGHT = 22;
    /** プロパティー */
    private final SchemaEditorProperties properties;

    public ToolPane(SchemaEditorImpl context) {
        properties = context.getProperties();
        setSpacing(2);
        setPadding(new Insets(2));

        // Preview Button
        PreviewPane previewPane = new PreviewPane();

        // FillMode Toggle Set
        FillModeToggle fillModeToggle = new FillModeToggle(28, BUTTON_HEIGHT);
        fillModeToggle.getItems().addAll(FillMode.values());
        fillModeToggle.selectionProperty().bindBidirectional(properties.fillModeProperty());

        // Line Width Combo
        LineWidthCombo lineWidthCombo = new LineWidthCombo();
        lineWidthCombo.setPrefHeight(BUTTON_HEIGHT); lineWidthCombo.setMinHeight(BUTTON_HEIGHT); lineWidthCombo.setMaxHeight(BUTTON_HEIGHT);
        lineWidthCombo.selectionPropertyProxy().bindBidirectional(properties.lineWidthProperty());

        // Preset Color Combo
        final PresetColorCombo presetColorCombo = new PresetColorCombo();
        presetColorCombo.setPrefHeight(BUTTON_HEIGHT); lineWidthCombo.setMinHeight(BUTTON_HEIGHT); lineWidthCombo.setMaxHeight(BUTTON_HEIGHT);
        presetColorCombo.selectionProxyProperty().addListener(new ChangeListener<ColorModel>(){
            @Override
            public void changed(ObservableValue<? extends ColorModel> ov, ColorModel t, ColorModel m) {
                // 下でつけるリスナで invalidate されると null でここに来る
                if (m == null) { return; }

                properties.valueChangingProperty().set(true);

                double lineOpacity = properties.getLineColor().getOpacity();
                properties.setLineColor(SchemaUtils.mergeOpacity(m.getLineColor(), lineOpacity));
                double fillOpacity = properties.getFillColor().getOpacity();
                properties.setFillColor(SchemaUtils.mergeOpacity(m.getFillColor(), fillOpacity));

                //properties.setLineWidth(m.getLineWidth());
                properties.setFillBlur(m.getFillBlur());
                properties.setFillMode(m.getFillMode());

                properties.valueChangingProperty().set(false);
            }
        });
        // ColorModel 関連の PropertyChange があったら， PresetColorCombo を invalidate する
        for (Property p : properties.getPropertiesRelatedToColorModel()) {
            p.addListener(new InvalidationListener(){
                @Override
                public void invalidated(Observable o) {
                    presetColorCombo.selectionProxyProperty().set(null);
                }
            });
        }

        // State Toggle Set
        final StateToggle stateToggle = new StateToggle(28, BUTTON_HEIGHT);
        stateToggle.getItems().addAll(State.values());
        stateToggle.selectionProperty().bindBidirectional(properties.stateProperty());
        UndoManager undoManager = context.getUndoManager();
        // undo/redo ボタン制御
        stateToggle.setDisable(State.Undo);
        stateToggle.setDisable(State.Redo);
        undoManager.undoQueueSizeProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                if ((int)t1 == 0) { stateToggle.setDisable(State.Undo); }
                else if ((int)t  == 0) { stateToggle.setEnable(State.Undo); }
            }
        });
        undoManager.redoQueueSizeProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                if ((int)t1 == 0) { stateToggle.setDisable(State.Redo); }
                else if ((int)t  == 0) { stateToggle.setEnable(State.Redo); }
            }
        });
        // Tooltip
        stateToggle.setTooltip(State.Pen, "F  ");
        stateToggle.setTooltip(State.Line, "L  ");
        stateToggle.setTooltip(State.Oval, "O  ");
        stateToggle.setTooltip(State.Rectangle, "Q  ");
        stateToggle.setTooltip(State.Polygon, "P  ");
        stateToggle.setTooltip(State.Dots, "D  ");
        stateToggle.setTooltip(State.Net, "N  ");
        stateToggle.setTooltip(State.Text, "X  ");
        stateToggle.setTooltip(State.Eraser, "E  ");
        stateToggle.setTooltip(State.Translate, "T  ");
        stateToggle.setTooltip(State.Rotate, "R  ");
        stateToggle.setTooltip(State.Clip, "C  ");
        stateToggle.setTooltip(State.Scale, "Z  ");

        // レイアウト
        HBox upper = new HBox();
        upper.setSpacing(2);
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        upper.getChildren().addAll(fillModeToggle, lineWidthCombo, spacer1, presetColorCombo, spacer2);

        HBox lower = new HBox();
        lower.setSpacing(2);
        lower.getChildren().addAll(stateToggle);

        VBox right = new VBox();
        right.getChildren().addAll(upper, new Separator(), lower);

        getChildren().addAll(previewPane, right);
    }
}
