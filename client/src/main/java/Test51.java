import java.awt.Dialog;
import javax.swing.*;

/**
 *
 * @author pns
 */
public class Test51 {
    public static void main (String[] argv) {
        JDialog d = new JDialog();
        d.setModal(true);

        JButton b = new JButton("Open Frame");
        b.addActionListener(e -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(200, 200);
            frame.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
            frame.setVisible(true);
        });

        d.add(b);
        d.pack();
        d.setVisible(true);
    }
}
