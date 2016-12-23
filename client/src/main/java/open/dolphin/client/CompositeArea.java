package open.dolphin.client;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import open.dolphin.ui.MyJPopupMenu;

/**
 * CompositeArea.
 * KarteComposite インターフェースを持つ JTextArea - Memo で使っている
 * @author kazm
 */
public class CompositeArea extends JTextArea implements KarteComposite<JTextArea>, UndoableEditListener, CaretListener {
    private static final long serialVersionUID = 1L;

    private Chart parent;
    private boolean hasSelection;
    private ActionMap map;
    private final UndoManager undoManager;
    private Action undoAction;
    private Action redoAction;

    public CompositeArea(int row, int col) {
        super(row, col);
        undoManager = new UndoManager();
        putClientProperty("Quaqua.TextComponent.showPopup ", false);
        connect();
    }

    private void connect() {
        addCaretListener(this);
    }

    public void setParent(Chart chart) {
        parent = chart;
    }

    @Override
    public void enter(ActionMap map) {
        this.map = map;
        map.get(GUIConst.ACTION_PASTE).setEnabled(canPaste());

        // undo / redo
        parent.getChartMediator().addChain(this);
        undoAction = map.get(GUIConst.ACTION_UNDO);
        redoAction = map.get(GUIConst.ACTION_REDO);

        MyJPopupMenu menu = new MyJPopupMenu();
        menu.add(map.get(GUIConst.ACTION_COPY));
        menu.add(map.get(GUIConst.ACTION_CUT));
        menu.add(map.get(GUIConst.ACTION_PASTE));
        setComponentPopupMenu(menu);
    }

    @Override
    public void exit(ActionMap map) {
    }

    @Override
    public JTextArea getComponent() {
        return this;
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        boolean newSelection =  (e.getDot() != e.getMark());

        if (newSelection != hasSelection) {
            hasSelection = newSelection;
            map.get(GUIConst.ACTION_PASTE).setEnabled(canPaste());
            map.get(GUIConst.ACTION_CUT).setEnabled(hasSelection);
            map.get(GUIConst.ACTION_COPY).setEnabled(hasSelection);
        }
    }

    private boolean canPaste() {
        Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        return t == null? false : t.isDataFlavorSupported(DataFlavor.stringFlavor);
    }

    @Override
    public void undoableEditHappened(UndoableEditEvent e) {
        undoManager.addEdit(e.getEdit());
        updateUndoAction();
    }

    public void undo() {
        undoManager.undo();
        updateUndoAction();
    }

    public void redo() {
        undoManager.redo();
        updateUndoAction();
    }

    private void updateUndoAction() {
        if(undoManager.canUndo()) { undoAction.setEnabled(true); }
        else { undoAction.setEnabled(false); }

        if(undoManager.canRedo()) { redoAction.setEnabled(true); }
        else { redoAction.setEnabled(false); }
    }
}
