package open.dolphin.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * JTextComponent に Undo 機能を付ける.<br>
 * 使用例:
 * <pre>{@code
 * JTextPane c = new JTextPane();
 * c.getDocument().addUndoableEditListener(TextComponentUndoManager.createManager(c));
 * }</pre>
 *
 * @author pns
 */
public class TextComponentUndoManager extends UndoManager {
    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(TextComponentUndoManager.class);

    private Action undoAction;
    private Action redoAction;

    // 連続する UndoableEditEvent をまとめる CompoundEdit
    private TextComponentUndoableEdit current;

    // delay msec 以内に起きた UndoableEditEvent はまとめて1つにする
    private final Timer timer;
    private final int delay = 30;

    public TextComponentUndoManager(JTextComponent c) {
        timer = new Timer(delay, e -> flush());
        current = new TextComponentUndoableEdit();

        // ATOK 関連
        AtokListener atokListener = new AtokListener(c, this);
        c.addKeyListener(atokListener);
        c.addInputMethodListener(atokListener);
    }

    @Override
    public void undoableEditHappened(UndoableEditEvent e) {
        timer.restart();
        current.addEdit(e.getEdit());
        updateActionStatus(); // 文字入力毎に action が enable/disable される
    }

    /**
     * current にまとめた UndoableEdit を addEdit する. Timer から呼ばれる.
     */
    public void flush() {
        timer.stop();
        current.end();
        addEdit(current);

        current = new TextComponentUndoableEdit();
        updateActionStatus();
    }

    /**
     * Word 単位で undo をまとめる addEdit.
     *
     * @param e UndoableEdit to add
     * @return return value of addEdit
     */
    @Override
    public boolean addEdit(UndoableEdit e) {
        if (toMergeEdit(e)) {
            TextComponentUndoableEdit last = (TextComponentUndoableEdit) lastEdit();
            TextComponentUndoableEdit toMerge = (TextComponentUndoableEdit) e;
            return last.mergeEdit(toMerge.lastEdit());

        } else {
            return super.addEdit(e);
        }
    }

    /**
     * 指定した UndoableEdit を前の UndoableEdit に merge するかどうか.
     * undo を word 単位でまとめるために使う.
     *
     * @param e UndoableEdit to merge
     * @return true to merge
     */
    private boolean toMergeEdit(UndoableEdit e) {
        TextComponentUndoableEdit edit = (TextComponentUndoableEdit) e;
        // DocumentEvent でなければ merge しない
        if (!(edit.lastEdit() instanceof AbstractDocument.DefaultDocumentEvent)) { return false; }

        try {
            AbstractDocument.DefaultDocumentEvent last = (AbstractDocument.DefaultDocumentEvent) edit.lastEdit();
            int start = last.getOffset();
            int len = last.getLength();
            String text = last.getDocument().getText(start, len);

            // 1文字で, アルファベット or 削除で, undo 可能の場合, undo を1つにまとめる
            return (edit.size() == 1
                && (text.matches("[A-Z,a-z]") || last.getType() == DocumentEvent.EventType.REMOVE)
                && lastEdit() instanceof TextComponentUndoableEdit
                && lastEdit().canUndo());

        } catch (BadLocationException ex) {
            logger.error(ex.getMessage());
        }
        return false;
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

    /**
     * enable/disable を update するために action を登録する.
     *
     * @param undo undo action
     * @param redo redo action
     */
    public void setActions(Action undo, Action redo) {
        undoAction = undo;
        redoAction = redo;
    }

    /**
     * TextComponentUndoManager 用の UndoableEdit.
     */
    private class TextComponentUndoableEdit extends CompoundEdit {
        @Override
        public UndoableEdit lastEdit() {
            return super.lastEdit();
        }

        public int size() {
            return super.edits.size();
        }

        /**
         * この UndoableEdit が保持する DocumentEvent に edit を merge する.
         *
         * @param edit DocumentEvent
         * @return addEdit した戻り値
         */
        public boolean mergeEdit(UndoableEdit edit) {
            setInProgress(true);
            boolean ret = addEdit(edit);
            end();
            return ret;
        }

        /**
         * package-private field の inProgress を書き換える.
         *
         * @param inProgress inProgress to set
         */
        public void setInProgress(boolean inProgress) {
            try {
                Field field = getClass().getSuperclass().getDeclaredField("inProgress");
                field.setAccessible(true);
                field.set(this, inProgress);

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * JTextComponent に Undo 機能を付ける utility static method.
     *
     * @param c JTextComponent
     * @return TextComponentUndoManager
     */
    public static TextComponentUndoManager createManager(JTextComponent c) {
        TextComponentUndoManager manager = new TextComponentUndoManager(c);

        // default undo/redo action
        Action undo = new AbstractAction("undo") {
            @Override
            public void actionPerformed(ActionEvent e) { manager.undo(); }
        };
        Action redo = new AbstractAction("redo") {
            @Override
            public void actionPerformed(ActionEvent e) { manager.redo(); }
        };
        manager.setActions(undo, redo);

        // short cut を付ける
        ActionMap am = c.getActionMap();
        InputMap im = c.getInputMap();
        am.put("undo", undo);
        im.put(KeyStroke.getKeyStroke("meta Z"), "undo");
        am.put("redo", redo);
        im.put(KeyStroke.getKeyStroke("shift meta Z"), "redo");

        return manager;
    }
}
