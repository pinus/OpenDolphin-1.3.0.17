package open.dolphin.ui.sheet;

import javax.swing.JButton;
import javax.swing.JWindow;

/**
 *
 * @author pns
 */
public class TestWindow extends JWindow {
    private static final long serialVersionUID = 1L;




    public static void main (String[] argv) {
        TestWindow w = new TestWindow();
        JButton b = new JButton("OK");
        b.addActionListener(e -> {
            w.setVisible(false);
            System.exit(0);
        });
        w.add(b);
        w.setBounds(200, 200, 200, 200);
        w.setVisible(true);

        System.out.println("window shown");
    }
}
