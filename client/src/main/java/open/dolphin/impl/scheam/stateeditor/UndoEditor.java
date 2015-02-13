package open.dolphin.impl.scheam.stateeditor;

import javafx.scene.input.MouseEvent;
import open.dolphin.impl.scheam.SchemaEditorImpl;
import open.dolphin.impl.scheam.ShapeHolder;
import open.dolphin.impl.scheam.UndoManager;

/**
 *
 * @author pns
 */
public class UndoEditor extends StateEditorBase {

    private final UndoManager undoManager;

    public UndoEditor(SchemaEditorImpl context) {
        undoManager = context.getUndoManager();
    }

    @Override
    public void start() {
        undoManager.undo();
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
