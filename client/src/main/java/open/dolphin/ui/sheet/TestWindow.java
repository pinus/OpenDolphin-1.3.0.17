package open.dolphin.ui.sheet;

import javax.swing.*;
import java.awt.*;

/**
 * @author pns
 */
public class TestWindow extends JWindow {
    private static final long serialVersionUID = 1L;

    private boolean blocking;

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        block();
    }

    public synchronized void block() {
        try {
            blocking = true;
            while (blocking) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void done() {
        blocking = false;
        notifyAll();
    }

    public static void main(String[] argv) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(1000, 500, 640, 400);
        f.setVisible(true);

        TestWindow w = new TestWindow();
        JButton b = new JButton("OK");
        b.addActionListener(e -> {
            w.done();
            w.setVisible(false);
            //System.exit(0);
        });

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(b);
        buttonPanel.add(Box.createHorizontalGlue());

        JLabel label = new JLabel("JWindow");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        contentPane.add(label, BorderLayout.CENTER);

        w.add(contentPane);
        w.setBounds(1200, 524, 200, 100);

        System.out.println("window showing");
        w.setVisible(true);

        System.out.println("block done");
    }
}
