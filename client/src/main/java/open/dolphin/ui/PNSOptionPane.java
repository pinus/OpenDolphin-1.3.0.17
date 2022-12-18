package open.dolphin.ui;

import javax.swing.*;
import java.awt.*;

/**
 * PNSButton を使った JOptionPane.
 */
public class PNSOptionPane extends JOptionPane {

    public static void showMessageDialog(Component parentComponent, Object message) {
        showMessageDialog(parentComponent, message, UIManager.getString("OptionPane.messageDialogTitle"), INFORMATION_MESSAGE);
    }

    public static void showMessageDialog(Component parentComponent, Object message, String title, int messageType) {
        showMessageDialog(parentComponent, message, title, messageType, null);
    }

    public static void showMessageDialog(Component parentComponent, Object message, String title, int messageType, Icon icon) {
        PNSButton button = new PNSButton("OK");
        JOptionPane pane = new JOptionPane(message, messageType, DEFAULT_OPTION, icon, new Object[]{ button }, button);
        JDialog dialog = new JDialog();
        button.addActionListener(e -> dialog.setVisible(false));
        dialog.setModal(true);
        dialog.add(pane);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
