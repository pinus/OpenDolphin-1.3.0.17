package open.dolphin.client;

import java.awt.Cursor;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.*;

/**
 * ComponentHolder
 *
 * @author  Kazushi Minagawa modified by pns
 */
public abstract class AbstractComponentHolder extends JLabel implements MouseListener, MouseMotionListener {

    /** エディタの二重起動を防ぐためのフラグ */
    private boolean isEditable = true;

    public AbstractComponentHolder() {
        initialize();
    }

    private void initialize() {
        this.setFocusable(true);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        ActionMap map = this.getActionMap();
        map.put(TransferHandler.getCutAction().getValue(Action.NAME), TransferHandler.getCutAction());
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME), TransferHandler.getCopyAction());
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME), TransferHandler.getPasteAction());

        // set ime off to reduce ATOK memory consumption
        // これを入れると，focus が当たっていない状態からダブルクリックをしても e.getClickCount() が 2 にならない
        // IMEControl.setImeOffIfFocused(this);
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
//        requestFocusInWindow(); // 自動的にフォーカス取るようにしたら，うざかった
    }

    @Override
    public void mouseExited(MouseEvent e) {
/*        Component c = getParent(); // うざかったのでやめた
        while ( c != null) {
            if (c instanceof JTextPane) {
                c.requestFocusInWindow();
                break;
            }
            c = c.getParent();
        }*/
    }
    // mac
    @Override
    public void mousePressed(MouseEvent e) {
        // requestFocus はここの方がいい。mouseClicked だと，mouseRelease されるまで focus とれないから
        requestFocusInWindow();
        // 右クリックで popup 表示
        if (e.isPopupTrigger() && e.getClickCount() != 2) maybeShowPopup(e);
        // ダブルクリックでエディタ表示
        if (!e.isPopupTrigger() && e.getClickCount() == 2) edit();
    }
    // windows
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger() && e.getClickCount() != 2) maybeShowPopup(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // ドラッグの際にも，スタンプを selected 状態にする
        requestFocusInWindow();

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

    public abstract void edit();
    public abstract void maybeShowPopup(MouseEvent e);
}
