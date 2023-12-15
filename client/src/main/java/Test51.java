import javax.swing.*;

/**
 * http://java-sl.com/tip_merge_undo_edits.html
 */
public class Test51 {

    public static void main(String[] args) {
        new Test51().start();
    }

    private void start() {
        JOptionPane.showMessageDialog(null, "Start");
        for (int i=0; i<500; i++) {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setBounds(0, 0, 600, 400);
            f.setVisible(true);

            //try {Thread.sleep(1000); } catch (Exception e) {}
            f.setVisible(false);
            f.dispose();
        }

        JOptionPane.showMessageDialog(null, "End");
    }
}