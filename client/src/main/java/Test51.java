import javax.swing.*;

/**
 *
 * @author pns
 */
public class Test51 {
    public static void main (String[] argv) {
        JFrame frame = new JFrame();
        JTextField tf = new JTextField();
        tf.addActionListener(e -> {
            String text = tf.getText();
            int code = (int) text.charAt(0);
            System.out.printf("%x%n", code);
        });
        frame.add(tf);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
