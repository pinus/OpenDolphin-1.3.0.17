package open.dolphin.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import java.awt.event.*;
import java.util.Objects;

/**
 * JTextComponent に Undo 機能を付ける.<br>
 * 使用例:
 * <pre>{@code
 * JTextPane c = new JTextPane();
 * UndoManager manager = TextComponentUndoManager.createManager(c);
 * }</pre>
 *
 * @author pns
 */
public class TextComponentUndoManager extends UndoManager {
    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(TextComponentUndoManager.class);

    private JTextComponent textComponent;
    private Action undoAction;
    private Action redoAction;

    // 連続する UndoableEditEvent をまとめる CompoundEdit
    private CompoundEdit current;

    // delay msec 以内に起きた UndoableEditEvent はまとめて1つにする
    private Timer timer;
    private int delay = 100;

    // ATOK 確定アンドゥ
    private static KeyStroke CTRL_BACKSPACE = KeyStroke.getKeyStroke("ctrl pressed BACK_SPACE");
    private boolean ctrlBackspace = false;
    private boolean inKakuteiUndo = false;

    public TextComponentUndoManager(JTextComponent c) {
        textComponent = c;
        timer = new Timer(delay, e -> flush());
        current = new CompoundEdit();

        // ATOK 関連
        c.addInputMethodListener(new InputMethodListener() {
            private long lap;
            @Override
            public void inputMethodTextChanged(InputMethodEvent event) {
                lap = System.currentTimeMillis() - lap;

                // 確定アンドゥは commit されたら終了
                if (inKakuteiUndo && event.getCommittedCharacterCount() > 0) {
                    logger.info("Kakutei-undo done: " + event.getCommittedCharacterCount());
                    inKakuteiUndo = false;
                }

                // 確定アンドゥ 1回目
                if (ctrlBackspace) {
                    logger.info("Kakutei-undo start");
                    undo();
                    ctrlBackspace = false;
                    inKakuteiUndo = true;
                } else if (inKakuteiUndo && lap < 10) {
                    // 2回目以降の ctrl-backspace キーは ATOK に取られて検出できないが，
                    // lap が非常に短く入ってくるので検出できる
                    logger.info("Kakutei-undo cont'd: " + lap);
                    undo();
                }

                lap = System.currentTimeMillis();
            }

            @Override
            public void caretPositionChanged(InputMethodEvent event) { }
        });

        // ATOK 関連動作のためにキーを調べる
        c.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // ATOK 確定アンドゥ (ctrl-backspace) の検出
                KeyStroke key = KeyStroke.getKeyStrokeForEvent(e);
                if (key.equals(CTRL_BACKSPACE)) {
                    ctrlBackspace = true;
                }
            }
        });
    }

    @Override
    public void undoableEditHappened(UndoableEditEvent e) {
        timer.restart();
        current.addEdit(e.getEdit());
        updateActionStatus(); // 文字入力毎に action が enable/disable される
    }

    /**
     * まとめた UndoableEdit を addEdit する. Timer から呼ばれる.
     */
    private void flush() {
        timer.stop();
        current.end();
        addEdit(current);

        current = new CompoundEdit();
        updateActionStatus();
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

        // listener 登録
        c.getDocument().addUndoableEditListener(manager);

        return manager;
    }
}
