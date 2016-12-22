package open.dolphin.ui;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;

/**
 * Mac の JPopupMenu の問題を回避するため MOUSE_RELEASED or KEY_RELEASED を待ってから popup 表示する
 * ver.1 getNextEvent の後，MOUSE_RELEASED 以外はすぐ post するようにしたら，ID1200 の変な Event が大量発生して失敗
 * ver.2 getNextEvent を Queue にためておいて，MOUSE_RELEASED の後，順番に systemQueue に post するようにした.
 * ver.3 getNextEvent を新しい EventQueue にためておいて，MOUSE_RELEASED の後 systemQueue に push してしまうようにした.
 * ver.4 1回だけ getNextEvent() から戻らなくなったことがあったので，Timeout するようにした.
 * ver.5 TimeOut を入れてもまれに freeze 発生. AWTEventListener をつける方法に変更
 * @author pns
 */
public class MyJPopupMenu extends JPopupMenu {
    private static final long serialVersionUID = 1L;

    private AWTEventListener listener;
    private Component invoker;
    private int x;
    private int y;

    public MyJPopupMenu() {
        super();
        listener = new EventListener();
    }

    @Override
    public void show(Component invoker, int x , int y) {
        // 他のアプリケーションからクリックされたとき
        com.apple.eawt.Application.getApplication().requestForeground(true);

        this.invoker = invoker;
        this.x = x;
        this.y = y;
        Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.MOUSE_EVENT_MASK);
    }

    // MOUSE_RELEASED を検出する Listener
    private class EventListener implements AWTEventListener {
        public void eventDispatched(AWTEvent event) {
            if (event.getID() == MouseEvent.MOUSE_RELEASED) {
                superShow();
            }
        }
    }

    public void superShow() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(listener);
        super.show(invoker, x, y);
    }
}
