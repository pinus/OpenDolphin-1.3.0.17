package open.dolphin.impl.scheam.stateeditor;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.ShapeHolder;

/**
 * 全部消す StateEditor
 * @author pns
 */
public class ClearEditor extends StateEditorBase {
    private final StackPane canvasPane;


    public ClearEditor(SchemaEditorImpl context) {
        canvasPane = context.getCanvasPane();
    }

    @Override
    public void start() {
        canvasPane.getChildren().clear();
    }

    @Override
    public void mouseDown(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseUp(MouseEvent e) {
    }

    @Override
    public ShapeHolder getHolder() {
        return null;
    }
}
