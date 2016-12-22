package open.dolphin.client;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.ActionMap;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import open.dolphin.ui.MyJPopupMenu;

/**
 *
 * @author kazm
 */
public class CompositeArea extends JTextArea implements KarteComposite<JTextArea>, CaretListener {
    private static final long serialVersionUID = 1L;

    private boolean hasSelection;
    private ActionMap map;

    public CompositeArea(int row, int col) {
        super(row, col);
        //this.addCaretListener(this);
        this.putClientProperty("Quaqua.TextComponent.showPopup ", false);
    }

    @Override
    public void enter(ActionMap map) {
        this.map = map;
        map.get(GUIConst.ACTION_PASTE).setEnabled(canPaste());
        MyJPopupMenu menu = new MyJPopupMenu();
        menu.add(map.get(GUIConst.ACTION_COPY));
        menu.add(map.get(GUIConst.ACTION_CUT));
        menu.add(map.get(GUIConst.ACTION_PASTE));
        this.setComponentPopupMenu(menu);
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

        boolean ret = false;
        Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (t == null) {
            return false;
        }

        return t.isDataFlavorSupported(DataFlavor.stringFlavor);
    }
}
