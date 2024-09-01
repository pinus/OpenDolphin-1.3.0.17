package open.dolphin.ui;

import open.dolphin.helper.ScriptExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Mac で IME on/off を切り替える.
 * <ul>
 * <li>ver 1: AppleScript で on/off するバージョン: 遅すぎてストレスたまる
 * <li>ver 2: InputContext.selectInputMethod バージョン: 調子よかったが，1.6.0_29 で使えなくなる
 * <li>ver 3: Robot version 切り替わったかどうか判定するために event queue システム導入
 * <li>ver 4: enableInputMethod(true/false) バージョン: short-cut が効かなくなったり不安定
 * <li>ver 5: Robot version 復活. 物理キーが押されていると誤動作するのでキー入力でフォーカスが当たるところには使えない
 * <li>ver 6: key combination での robot 入力うまくいかず, F12, F13 キーで切り替えるように ATOK 側で設定することにした
 * <li>ver 7: <a href="https://github.com/daipeihust/im-select">im-select</a> 呼び出し法
 * </ul>
 *
 * @author pns
 */
public class IMEControl {
    private final static Logger logger = LoggerFactory.getLogger(IMEControl.class);
    private final static boolean isMac = System.getProperty("os.name").toLowerCase().startsWith("mac");
    //private final static int toEijiKey = Preferences.userNodeForPackage(Dolphin.class).getInt(Project.ATOK_TO_EIJI_KEY, KeyEvent.VK_F12);
    //private final static int toHiraganaKey = Preferences.userNodeForPackage(Dolphin.class).getInt(Project.ATOK_TO_HIRAGANA_KEY, KeyEvent.VK_F13);
    private final static String JAPANESE = "com.justsystems.inputmethod.atok33.Japanese";
    private final static String ROMAN = "com.justsystems.inputmethod.atok33.Roman";

    /**
     * IME-off. Shift-Control-C で英字 -> F12
     */
    public static void off() {
        if (isMac) {
            logger.info("atok eiji mode");
            //type(KeyEvent.VK_SHIFT, KeyEvent.VK_CONTROL, KeyEvent.VK_C);
            //type(toEijiKey);
            ScriptExecutor.imSelect(ROMAN);
        }
    }

    /**
     * IME-on. Shift-Control-Z でひらがな -> F13
     */
    public static void on() {
        if (isMac) {
            logger.info("atok hiragana mode");
            //type(KeyEvent.VK_SHIFT, KeyEvent.VK_CONTROL, KeyEvent.VK_Z);
            //type(toHiraganaKey);
            ScriptExecutor.imSelect(JAPANESE);
        }
    }

    private static void type(int... keys) {
        try {
            Robot robot = new Robot();
            for (int i = 0; i < keys.length; i++) { robot.keyPress(keys[i]); }
            for (int i = keys.length -1 ; i >= 0; i--) { robot.keyRelease(keys[i]); }
        } catch (AWTException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * IME-off when focused.
     *
     * @param c component to add focus listener
     */
    public static void off(final Component c) {
        c.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) { off(); }
        });
    }

    /**
     * IME-on when focused.
     *
     * @param c component to add focus listener
     */
    public static void on(final Component c) {
        c.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) { on(); }
        });
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
