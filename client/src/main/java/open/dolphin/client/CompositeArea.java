package open.dolphin.client;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.ActionMap;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import open.dolphin.helper.TextComponentUndoManager;
import open.dolphin.ui.MyJPopupMenu;

/**
 * CompositeArea.
 * KarteComposite インターフェースを持つ JTextArea - Memo で使っている
 * @author kazm
 * @author pns
 */
public class CompositeArea extends JTextArea implements KarteComposite<JTextArea>, CaretListener {
    private static final long serialVersionUID = 1L;

    private boolean hasSelection;
    private ActionMap map;
    private final TextComponentUndoManager undoManager;

    public CompositeArea(int row, int col) {
        super(row, col);
        undoManager = new TextComponentUndoManager();
        putClientProperty("Quaqua.TextComponent.showPopup ", false);
        connect();
    }

    private void connect() {
        addCaretListener(this);
    }

    public void startUndoListener() {
        getDocument().addUndoableEditListener(undoManager::listener);
    }

    @Override
    public void enter(ActionMap map) {
        this.map = map;
        map.get(GUIConst.ACTION_PASTE).setEnabled(canPaste());

        // undo / redo
        undoManager.setUndoAction(map.get(GUIConst.ACTION_UNDO));
        undoManager.setRedoAction(map.get(GUIConst.ACTION_REDO));

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

    /**
     * ChartMediator で addChain されて呼ばれる.
     */
    public void undo() {
        undoManager.undo();
    }

    public void redo() {
        undoManager.redo();
    }
}
