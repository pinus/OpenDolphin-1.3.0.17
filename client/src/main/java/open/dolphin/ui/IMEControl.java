package open.dolphin.ui;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 * Mac で IME on/off を切り替える
 *
 * ver 1: AppleScript で on/off するバージョン　：　遅すぎてストレスたまる
 * ver 2: InputContext.selectInputMethod バージョン　：　調子よかったが，1.6.0_29 で使えなくなる
 * ver 3: Robot version 切り替わったかどうか判定するために event queue システム導入
 * ver 4: enableInputMethod(true/false) バージョン
 * @author pns
 */
public class IMEControl {

    static {
        // 「かな」キーで ime on
/*        KeyboardFocusManager.setCurrentKeyboardFocusManager(new DefaultKeyboardFocusManager() {
            @Override
            public void processKeyEvent(Component focusedComponent, KeyEvent e) {
                if (focusedComponent instanceof JTextComponent) {
                    if (e.getKeyCode() == KeyEvent.VK_KATAKANA) {
                        focusedComponent.enableInputMethods(true);
                    //} else if (e.getKeyCode() == KeyEvent.VK_ALPHANUMERIC) {
                    //    focusedComponent.enableInputMethods(false);
                    }
                }
                super.processKeyEvent(focusedComponent, e);
            }
        });*/

        // 上の方法だと，時々ショートカットキーが効かなくなることがあったので，
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener(){
            @Override
            public void eventDispatched(AWTEvent event) {
                if (((KeyEvent)event).getKeyCode() == KeyEvent.VK_KATAKANA) {
                    Object source = event.getSource();
                    if (source instanceof Component) {
                        ((Component)source).enableInputMethods(true);
                    }
                }
            }
        }, AWTEvent.KEY_EVENT_MASK);
    }

    /**
     * IME off
     * @param c
     */
    public static void setImeOff(Component c) {
        c.enableInputMethods(false);
    }

    /**
     * IME on
     * @param c
     */
    public static void setImeOn(Component c) {
        if (c instanceof JTextComponent) {
            c.enableInputMethods(true);
        }
    }

    /**
     * Component c がフォーカスを取ったら ime を切る
     * @param c
     */
    public static void setImeOffIfFocused(final Component c) {
        c.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                setImeOff(c);
            }
        });
    }

    /**
     * Component c がフォーカスを取ったら ime を on にする
     * @param c
     */
    public static void setImeOnIfFocused(final Component c) {
        c.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                setImeOn(c);
            }
            @Override
            public void focusLost(FocusEvent e) {
                Component w = SwingUtilities.getWindowAncestor(c);
                if (w != null) { setImeOff(w); }
            }
        });
    }

    public static void main(String[] argv) {
        //System.out.println("java.version = " + System.getProperty("java.version") + " " + System.getProperty("sun.arch.data.model") + " bit");

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextField tf = new JTextField(100);
        IMEControl.setImeOnIfFocused(tf);
        //IMEControl.setImeOffIfFocused(tf);
        f.add(tf);
        f.pack();
        f.setVisible(true);

        JFrame f2 = new JFrame();
        JTextField tf2 = new JTextField(50);
        IMEControl.setImeOffIfFocused(tf2);
        f2.add(tf2);
        f2.pack();
        f2.setVisible(true);

    }
}
