import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledEditorKit;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class UndoTest {
    private Logger logger = LoggerFactory.getLogger(UndoTest.class);

    public class TextComponentUndoManager extends UndoManager {

        private JTextComponent textComponent;
        private Action undoAction;
        private Action redoAction;
        private CompoundEdit current = new CompoundEdit();
        private Timer timer;

        public TextComponentUndoManager(JTextComponent c) {
            textComponent = c;
            timer = new Timer(100, e -> flush());
            c.addInputMethodListener(new InputMethodListener() {
                @Override
                public void inputMethodTextChanged(InputMethodEvent event) {
                    logger.info("textchanged " + event);
                }

                @Override
                public void caretPositionChanged(InputMethodEvent event) {
                    logger.info("caretchanged " + event);
                }
            });
        }

        public void setUndoAction(Action action) {
            undoAction = action;
        }

        public void setRedoAction(Action action) {
            redoAction = action;
        }

        @Override
        public void undoableEditHappened(UndoableEditEvent e) {
            timer.restart();
            current.addEdit(e.getEdit());
            updateActionStatus();
        }

        private void flush() {
            timer.stop();

            logger.info("flush");
            current.end();
            addEdit(current);
            current = new CompoundEdit();
            updateActionStatus();
        }

        @Override
        public void undo() {
            logger.info("undo");
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
