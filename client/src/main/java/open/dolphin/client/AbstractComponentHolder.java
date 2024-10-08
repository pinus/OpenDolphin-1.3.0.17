package open.dolphin.client;

import open.dolphin.helper.MouseHelper;
import open.dolphin.ui.Focuser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.FocusManager;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.text.Position;
import javax.swing.undo.*;
import java.awt.*;
import java.awt.event.*;

/**
 * ComponentHolder. StampHolder と SchemaHolder.
 * マウス選択および {@link open.dolphin.client.KartePane#caretUpdate(CaretEvent) KartePane#caretUpdate} で
 * キャレットが Component 位置にきたときにフォーカスを取る. フォーカスを取ると
 * {@link open.dolphin.client.KarteComposite#enter(ActionMap) enter(ActionMap)} が呼ばれる.
 *
 * @param <T> ComponentHolder の扱うデータ型 (StampHolder = ModuleModel, SchemaHolder = SchemaModel)
 *
 * @author Kazushi Minagawa
 * @author pns
 */
public abstract class AbstractComponentHolder<T> extends JLabel
    implements ComponentHolder<JLabel>, MouseListener, MouseMotionListener, KeyListener {
    private final Logger logger = LoggerFactory.getLogger(AbstractComponentHolder.class);

    private static final KeyStroke TAB = KeyStroke.getKeyStroke("TAB");
    private static final KeyStroke SHIFT_TAB = KeyStroke.getKeyStroke("shift TAB");
    private static final KeyStroke CTRL_ENTER = KeyStroke.getKeyStroke("ctrl ENTER");
    private static final KeyStroke SPACE = KeyStroke.getKeyStroke("SPACE");

    // 親の KartePane
    private final KartePane kartePane;
    // JTextPane 内での開始と終了ポジション. 自動更新される.
    private Position start;
    private Position end;
    // エディタの二重起動を防ぐためのフラグ
    private boolean isEditable = true;
    // ActionMap
    private ActionMap actionMap;
    // UndoSupport
    private UndoManager undoManager;

    public AbstractComponentHolder(KartePane kartePane) {
        this.kartePane = kartePane;
        initialize();
    }

    private void initialize() {
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        setFocusTraversalKeysEnabled(false); // これをしないと TAB キーを取られる

        ActionMap am = this.getActionMap();
        am.put(TransferHandler.getCutAction().getValue(Action.NAME), TransferHandler.getCutAction());
        am.put(TransferHandler.getCopyAction().getValue(Action.NAME), TransferHandler.getCopyAction());
        am.put(TransferHandler.getPasteAction().getValue(Action.NAME), TransferHandler.getPasteAction());

        undoManager = new UndoManager();
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean b) {
        isEditable = b;
    }

    @Override
    public void enter(ActionMap map) {
        actionMap = map;
        updateMenuState();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        KeyStroke key = KeyStroke.getKeyStrokeForEvent(e);

        if (TAB.equals(key)) {
            // TAB キーでフォーカス次移動 (インスペクタでのフォーカス移動)
            if (!kartePane.getTextPane().isEditable()) {
                SwingUtilities.invokeLater(FocusManager.getCurrentManager()::focusNextComponent);
            }

        } else if (SHIFT_TAB.equals(key)) {
            // shift TAB キーでフォーカス前移動 (インスペクタでのフォーカス移動)
            if (!kartePane.getTextPane().isEditable()) {
                SwingUtilities.invokeLater(FocusManager.getCurrentManager()::focusPreviousComponent);
            }

        } else if (SPACE.equals(key)) {
            // SPACE で編集
            edit();

        } else if (CTRL_ENTER.equals(key)) {
            // ctrl-ENTER でポップアップ表示
            MouseEvent me = new MouseEvent(this, 0, 0, 0,
                10, this.getHeight(), 0, true, 0);
            maybeShowPopup(me);

        } else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN
            || e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT
            || e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
            JTextPane pane = kartePane.getTextPane();
            pane.requestFocusInWindow();
            pane.dispatchEvent(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {
        Focuser.requestFocus(this);
        // 右クリックで popup 表示
        if (e.isPopupTrigger()) {
            maybeShowPopup(e);
        }
        // ダブルクリックでエディタ表示
        else if (e.getClickCount() == 2 && !MouseHelper.mouseMoved() && !e.isAltDown()) {
            edit();
        }
        // ComponentHolder 位置に Caret を設定
        kartePane.getTextPane().setCaretPosition(start.getOffset());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //windows
        if (e.isPopupTrigger() && e.getClickCount() != 2) {
            maybeShowPopup(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // ドラッグの際にも，スタンプを selected 状態にする
        Focuser.requestFocus(this);

        int ctrlMask = InputEvent.CTRL_DOWN_MASK;
        int optionMask = InputEvent.ALT_DOWN_MASK;
        int action = ((e.getModifiersEx() & (ctrlMask | optionMask)) != 0) ?
                TransferHandler.COPY : TransferHandler.MOVE;

        JComponent c = (JComponent) e.getSource();
        TransferHandler handler = c.getTransferHandler();
        handler.exportAsDrag(c, e, action);
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    /**
     * KarteStyledDocument の createPosition で作成される.
     * 実体は {@link javax.swing.text.GapContent GapContent.StickyPosition} への参照.
     * Document の変更に応じて自動更新される.
     * @see javax.swing.text.GapContent#createPosition(int)
     *
     * @param start この Component の開始位置
     * @param end start + 1
     */
    @Override
    public void setEntry(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public int getStartPos() { return start.getOffset(); }

    @Override
    public int getEndPos() { return end.getOffset(); }

    @Override
    public abstract void edit();

    /**
     * この ComponentHolder が扱うモデルを返す.
     *
     * @return ModuleModel or SchemaModel
     */
    public abstract T getModel();

    public abstract void maybeShowPopup(MouseEvent e);

    public void undo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
            updateMenuState();
        } else {
            kartePane.undo();
        }
    }

    public void redo() {
        if (undoManager.canRedo()) {
            undoManager.redo();
            updateMenuState();
        } else {
            kartePane.redo();
        }
    }

    /**
     * Undo / Redo 関連のメニューを update する.
     */
    public void updateMenuState() {
        actionMap.get(GUIConst.ACTION_UNDO).setEnabled(undoManager.canUndo());
        actionMap.get(GUIConst.ACTION_REDO).setEnabled(undoManager.canRedo());
    }

    /**
     * Undoable updateModel.
     *
     * @param newValue ModuleModel (Stamp) or SchemaModel
     */
    public void undoableUpdateModel(T newValue) {
        UndoableEdit edit = new UndoableEdit(getModel(), newValue);
        undoManager.addEdit(edit);
        updateModel(newValue);
    }

    /**
     * この ComponentHolder のモデルを update する.
     *
     * @param newValue ModuleModel or SchemaModel
     */
    public abstract void updateModel(T newValue);

    private class UndoableEdit extends AbstractUndoableEdit {
        private T oldValue;
        private T newValue;

        public UndoableEdit(T oldValue, T newValue) {
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        @Override
        public void undo() throws CannotUndoException {
            super.undo();
            updateModel(oldValue);
        }
        @Override
        public void redo() throws CannotRedoException {
            super.redo();
            updateModel(newValue);
        }
        @Override
        public void die() {
            super.die();
            oldValue = newValue = null;
        }
    }
}
