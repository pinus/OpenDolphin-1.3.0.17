package open.dolphin.helper;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

/**
 * TextComponentUndoManager.
 * <pre>
 * TextComponentUndoManager manager = new TextComponentUndoManager();
 * JTextPane textComponent = new JTextPane();
 * manager.addUndoActionTo(textComponent);
 * textComponent.getDocument().addUndoableEditListener(manager::listener);
 * </pre>
 * @author pns
 */
public class TextComponentUndoManager extends UndoManager {
    private static final long serialVersionUID = 1L;

    private Action undoAction;
    private Action redoAction;

    public TextComponentUndoManager() {

        undoAction = new AbstractAction("undo") {
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        };

        redoAction = new AbstractAction("redo") {
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(ActionEvent e) {
                redo();
            }
        };
    }

    public void addUndoActionTo(JTextComponent c) {
        ActionMap am = c.getActionMap();
        InputMap im = c.getInputMap();
        am.put("undo", undoAction);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.META_DOWN_MASK), "undo");
        am.put("redo", redoAction);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.META_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK), "redo");
    }

    public void setUndoAction(Action action) {
        undoAction = action;
    }

    public void setRedoAction(Action action) {
        redoAction = action;
    }

    public void listener (UndoableEditEvent e) {
        addEdit(e.getEdit());
        updateActionStatus();
    }

    @Override
    public void undo() {
        super.undo();
        updateActionStatus();
    }

    @Override
    public void redo() {
        super.redo();
        updateActionStatus();
    }

    private void updateActionStatus() {
        undoAction.setEnabled(canUndo());
        redoAction.setEnabled(canRedo());
    }
}
