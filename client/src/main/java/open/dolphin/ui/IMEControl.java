package open.dolphin.ui;

import open.dolphin.client.Dolphin;
import open.dolphin.helper.ScriptExecutor;
import open.dolphin.project.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.prefs.Preferences;

/**
 * Mac で IME on/off を切り替える.
 * <ul>
 * <li>ver 1: AppleScript で on/off するバージョン: 遅すぎてストレスたまる
 * <li>ver 2: InputContext.selectInputMethod バージョン: 調子よかったが，1.6.0_29 で使えなくなる
 * <li>ver 3: Robot version 切り替わったかどうか判定するために event queue システム導入
 * <li>ver 4: enableInputMethod(true/false) バージョン: short-cut が効かなくなったり不安定
 * <li>ver 5: Robot version 復活. 物理キーが押されていると誤動作するのでキー入力でフォーカスが当たるところには使えない
 * <li>ver 6: key combination での robot 入力うまくいかず, F12, F13 キーで切り替えるように ATOK 側で設定することにした
 * <li>ver 7: im-select 呼び出し法 (https://github.com/daipeihust/im-select)
 * </ul>
 *
 * @author pns
 */
public class IMEControl {
    private final static Logger logger = LoggerFactory.getLogger(IMEControl.class);
    private final static boolean isMac = System.getProperty("os.name").toLowerCase().startsWith("mac");
    private final static String JAPANESE = Preferences.userNodeForPackage(Dolphin.class).get(Project.ATOK_JAPANESE_KEY, "com.justsystems.inputmethod.atok34.Japanese");
    private final static String ROMAN = Preferences.userNodeForPackage(Dolphin.class).get(Project.ATOK_ROMAN_KEY, "com.justsystems.inputmethod.atok34.Roman");

    /**
     * IME-off.
     */
    public static void off() {
        if (isMac) {
            logger.info("atok eiji mode");
            ScriptExecutor.imSelect(ROMAN);
        }
    }

    /**
     * IME-on.
     */
    public static void on() {
        if (isMac) {
            logger.info("atok hiragana mode");
            ScriptExecutor.imSelect(JAPANESE);
        }
    }

    /**
     * IME-off when focused.
     *
     * @param c component to add focus listener
     */
    public static void off(final Component c) {
        c.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { off(); }
            @Override
            public void focusLost(FocusEvent e) { onLeaving(e.getSource()); }
        });
    }

    /**
     * IME-on when focused.
     *
     * @param c component to add focus listener
     */
    public static void on(final Component c) {
        c.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { on(); }
            @Override
            public void focusLost(FocusEvent e) { onLeaving(e.getSource()); }
        });
    }

    /**
     * IME-off on leaving.
     *
     * @param c component to add focus listener
     */
    public static void offIfFocusLost(final Component c) {
        c.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}
            @Override
            public void focusLost(FocusEvent e) { onLeaving(e.getSource()); }
        });
    }

    /**
     * ATOK ひらがなモードのまま離れると, 変換ウインドウが変なところに出るの対策.
     *
     * @param source ATOK off したい source
     */
    private static void onLeaving(Object source) {
        if (source instanceof JComponent comp && comp.isVisible()) { off(); }
    }

    public static void main(String[] argv) {

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel l1 = new JLabel("TF1");
        JLabel l2 = new JLabel("TF2");

        JTextField tf1 = new JTextField(30);
        on(tf1);
        JTextField tf2 = new JTextField(30);
        off(tf2);

        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        p1.add(l1);
        p1.add(tf1);
        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));
        p2.add(l2);
        p2.add(tf2);

        f.getRootPane().setLayout(new BoxLayout(f.getRootPane(), BoxLayout.Y_AXIS));
        f.getRootPane().add(p1);
        f.getRootPane().add(p2);
        f.pack();
        f.setLocation(200,100);
        f.setVisible(true);
    }
}
