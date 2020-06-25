import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledEditorKit;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class UndoTest {
    private Logger logger = LoggerFactory.getLogger(UndoTest.class);

    public class TextComponentUndoManager extends UndoManager {

        private JTextComponent textComponent;
        private Action undoAction;
        private Action redoAction;

        public TextComponentUndoManager(JTextComponent c) {
            textComponent = c;
        }

        public void setUndoAction(Action action) {
            undoAction = action;
        }

        public void setRedoAction(Action action) {
            redoAction = action;
        }

        @Override
        public void undoableEditHappened(UndoableEditEvent e) {
            addEdit(e.getEdit());
            updateActionStatus(); // 文字入力毎に action が enable/disable される
        }

        @Override
        public void undo() {
            if (canUndo()) { super.undo(); }
            updateActionStatus();
        }

        @Override
        public void redo() {
            if (canRedo()) { super.redo(); }
            updateActionStatus();
        }

        private void updateActionStatus() {
            if (Objects.isNull(undoAction) || Objects.isNull(redoAction)) { return; }
            undoAction.setEnabled(canUndo());
            redoAction.setEnabled(canRedo());
        }
    }

    private void start() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextPane pane = new JTextPane();
        TextComponentUndoManager undoManager = new TextComponentUndoManager(pane);
        pane.setEditorKit(new StyledEditorKit());
        pane.getDocument().addUndoableEditListener(undoManager);

        JScrollPane scroll = new JScrollPane(pane);
        frame.getContentPane().add(scroll);

        JButton undoBtn = new JButton();
        Action undo = new AbstractAction("Undo") {
            @Override
            public void actionPerformed(ActionEvent e) { undoManager.undo(); }
        };
        undoBtn.setAction(undo);
        undoManager.setUndoAction(undo);
        undo.setEnabled(false);

        JButton redoBtn = new JButton();
        Action redo = new AbstractAction("Redo") {
            @Override
            public void actionPerformed(ActionEvent e) { undoManager.redo(); }
        };
        redoBtn.setAction(redo);
        undoManager.setRedoAction(redo);
        redo.setEnabled(false);

        ActionMap am = pane.getActionMap();
        am.put("undo", undo);
        am.put("redo", redo);
        InputMap im = pane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke("meta Z"), "undo");
        im.put(KeyStroke.getKeyStroke("shift meta Z"), "redo");

        JToolBar tb = new JToolBar();
        tb.add(undoBtn);
        tb.add(redoBtn);
        frame.getContentPane().add(tb, BorderLayout.NORTH);

        frame.setSize(400, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] arg) {
        UndoTest app = new UndoTest();
        app.start();
    }
}
