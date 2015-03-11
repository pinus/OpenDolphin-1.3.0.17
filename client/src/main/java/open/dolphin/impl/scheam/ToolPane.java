package open.dolphin.impl.scheam;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleButton;
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

    public ToolPane(SchemaEditorImpl context) {
        SchemaEditorProperties properties = SchemaEditorImpl.getProperties();
        setSpacing(2);
        setPadding(new Insets(2));

        // Preview Button
        PreviewPane previewPane = new PreviewPane();

        // FillMode Toggle Set
        FillModeToggle fillModeToggle = new FillModeToggle();
        fillModeToggle.getItems().addAll(FillMode.values());
        fillModeToggle.setPrefWidth(32*3+2);
        fillModeToggle.setAllButtonWidth(32);
        fillModeToggle.setAllButtonHeight(BUTTON_HEIGHT);
        fillModeToggle.selectedItemProperty().bindBidirectional(properties.fillModeProperty());

        // Line Width Combo
        LineWidthCombo lineWidthCombo = new LineWidthCombo();
        lineWidthCombo.setPrefHeight(BUTTON_HEIGHT); lineWidthCombo.setMinHeight(BUTTON_HEIGHT); lineWidthCombo.setMaxHeight(BUTTON_HEIGHT);
        lineWidthCombo.selectionPropertyProxy().bindBidirectional(properties.lineWidthProperty());

        // Preset Color Combo
        final PresetColorCombo presetColorCombo = new PresetColorCombo();
        presetColorCombo.setPrefHeight(BUTTON_HEIGHT); lineWidthCombo.setMinHeight(BUTTON_HEIGHT); lineWidthCombo.setMaxHeight(BUTTON_HEIGHT);
        presetColorCombo.selectionProxyProperty().addListener((ObservableValue<? extends ColorModel> ov, ColorModel t, ColorModel m) -> {
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
        });

        // ColorModel 関連の PropertyChange があったら， PresetColorCombo を invalidate する
        for (Property p : properties.getPropertiesRelatedToColorModel()) {
            p.addListener( o -> presetColorCombo.selectionProxyProperty().set(null));
        }

        // State Toggle Set
        final StateToggle stateToggle = new StateToggle();
        stateToggle.setAllButtonHeight(BUTTON_HEIGHT);
        stateToggle.setAllButtonWidth(32);
        stateToggle.setPrefWidth(State.values().length * 32 + 2);
        stateToggle.selectedItemProperty().bindBidirectional(properties.stateProperty());

        // undo / redo / clear ボタンの処理
        UndoManager manager = context.getUndoManager();
        manager.undoQueueSizeProperty().addListener((ObservableValue<? extends Number> o, Number ov, Number nv) -> {
            if ((int)nv == 0) { stateToggle.setDisable(State.Undo, true); }
            else if ((int)ov  == 0) { stateToggle.setDisable(State.Undo, false); }
            stateToggle.updateCell(State.Undo);
        });
        manager.redoQueueSizeProperty().addListener((ObservableValue<? extends Number> o, Number ov, Number nv) -> {
            if ((int)nv == 0) { stateToggle.setDisable(State.Redo, true); }
            else if ((int)ov  == 0) { stateToggle.setDisable(State.Redo, false); }
            stateToggle.updateCell(State.Redo);
        });
        stateToggle.getButton(State.Undo).setOnAction(e -> {
            manager.undo();
            ((ToggleButton)e.getSource()).setSelected(false);
        });
        stateToggle.getButton(State.Redo).setOnAction(e -> {
            manager.redo();
            ((ToggleButton)e.getSource()).setSelected(false);
        });
        stateToggle.getButton(State.Clear).setOnAction(e -> {
            context.getCanvasPane().getChildren().clear();
            ((ToggleButton)e.getSource()).setSelected(false);
        });

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
        Region spacer3 = new Region();
        VBox.setVgrow(spacer3, Priority.ALWAYS);
        right.getChildren().addAll(upper, spacer3, lower);

        getChildren().addAll(previewPane, right);
    }
}
