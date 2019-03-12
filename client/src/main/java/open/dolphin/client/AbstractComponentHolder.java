package open.dolphin.client;

import open.dolphin.helper.MouseHelper;
import open.dolphin.ui.Focuser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * ComponentHolder.
 * StampHolder と SchemaHolder.
 * @author Kazushi Minagawa
 * @author pns
 */
public abstract class AbstractComponentHolder extends JLabel implements ComponentHolder<JLabel>, MouseListener, MouseMotionListener {
    private static final long serialVersionUID = 1L;

    /** エディタの二重起動を防ぐためのフラグ */
    private boolean isEditable = true;

    public AbstractComponentHolder() {
        initialize();
    }

    private void initialize() {
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        ActionMap map = this.getActionMap();
        map.put(TransferHandler.getCutAction().getValue(Action.NAME), TransferHandler.getCutAction());
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME), TransferHandler.getCopyAction());
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME), TransferHandler.getPasteAction());
   }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean b) {
        isEditable = b;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // ダブルクリックで編集
        // ここでは e.isPopupTrigger は常に false になる
        //if (e.getClickCount() == 2 && !e.isPopupTrigger()) edit();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // requestFocusInWindow(); // 自動的にフォーカス取るようにしたら，うざかった
    }

    @Override
    public void mouseExited(MouseEvent e) {
     /* Component c = getParent(); // うざかったのでやめた
        while ( c != null) {
            if (c instanceof JTextPane) {
                c.requestFocusInWindow();
                break;
            }
            c = c.getParent();
        }*/
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // requestFocus はここの方がいい. mouseClicked だと，mouseRelease されるまで focus とれないから
        Focuser.requestFocus(this);
        // 右クリックで popup 表示
        if (e.isPopupTrigger()) { maybeShowPopup(e); }
        // ダブルクリックでエディタ表示
        else if (e.getClickCount() == 2 && ! MouseHelper.mouseMoved() && ! e.isAltDown()) { edit(); }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //windows
        if (e.isPopupTrigger() && e.getClickCount() != 2) { maybeShowPopup(e); }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // ドラッグの際にも，スタンプを selected 状態にする
        Focuser.requestFocus(this);
        setSelected(true);

        int ctrlMask = InputEvent.CTRL_DOWN_MASK;
        int optionMask = InputEvent.ALT_DOWN_MASK;
        int action = ((e.getModifiersEx() & (ctrlMask | optionMask)) != 0)?
                TransferHandler.COPY : TransferHandler.MOVE;

        JComponent c = (JComponent) e.getSource();
        TransferHandler handler = c.getTransferHandler();
        handler.exportAsDrag(c, e, action);
    }

    @Override
    public void mouseMoved(MouseEvent e) { }

    @Override
    public abstract void edit();

    public abstract void maybeShowPopup(MouseEvent e);
}
