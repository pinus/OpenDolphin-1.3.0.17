package open.dolphin.ui;

import open.dolphin.client.Dolphin;
import open.dolphin.helper.ScriptExecutor;
import open.dolphin.project.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Objects;
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
 * <li>ver 8: FocusManger で一元管理バージョン
 * </ul>
 *
 * @author pns
 */
public class IMEControl {
    private final static String JAPANESE = Preferences.userNodeForPackage(Dolphin.class).get(Project.ATOK_JAPANESE_KEY, "com.justsystems.inputmethod.atok34.Japanese");
    private final static String ROMAN = Preferences.userNodeForPackage(Dolphin.class).get(Project.ATOK_ROMAN_KEY, "com.justsystems.inputmethod.atok34.Roman");

    private String toSet = ROMAN;
    private final Logger logger = LoggerFactory.getLogger(IMEControl.class);

    public IMEControl() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("permanentFocusOwner", e -> {
            if (Objects.nonNull(e.getNewValue())) {
                if (e.getNewValue() instanceof JPasswordField) {
                    toSet = ROMAN;
                } else if (e.getNewValue() instanceof JTextComponent c) {
                    toSet = Objects.nonNull(c.getClientProperty(Project.ATOK_ROMAN_KEY)) ? ROMAN : JAPANESE;
                } else {
                    toSet = ROMAN;
                }
                ScriptExecutor.imSelect(toSet);
                // protect time
                try { Thread.sleep(50); } catch (Exception ex) {}
            }
        });
    }

    public static void main(String[] argv) {
        new IMEControl();

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel l1 = new JLabel("TF1");
        JLabel l2 = new JLabel("TF2");
        JLabel l3 = new JLabel("TF3");

        JTextField tf1 = new JTextField(30);
        JPasswordField tf2 = new JPasswordField(30);
        JTextField tf3 = new JTextField(30);
        tf3.putClientProperty(Project.ATOK_ROMAN_KEY, true);

        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        p1.add(l1);
        p1.add(tf1);
        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));
        p2.add(l2);
        p2.add(tf2);
        JPanel p3 = new JPanel();
        p3.setLayout(new BoxLayout(p3, BoxLayout.X_AXIS));
        p3.add(l3);
        p3.add(tf3);

        f.getRootPane().setLayout(new BoxLayout(f.getRootPane(), BoxLayout.Y_AXIS));
        f.getRootPane().add(p1);
        f.getRootPane().add(p2);
        f.getRootPane().add(p3);
        f.pack();
        f.setLocation(200,100);
        f.setVisible(true);
    }
}
