package open.dolphin.ui;

import ch.randelshofer.quaqua.JSheet;
import ch.randelshofer.quaqua.SheetEvent;
import ch.randelshofer.quaqua.SheetListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 * JOptionPane を quaqua の JSheet で置き換える
 * Sheet の表示スピード調節：e.g. defaults write NSGlobalDomain NSWindowResizeTime 0.05 (default=0.2)
 * @author pinus
 */
public class MyJSheet extends JSheet {
    private static final long serialVersionUID = 1L;
    static int answer;

    public MyJSheet(Frame frame) {
        super(frame);
    }
    public MyJSheet(Dialog dialog) {
        super(dialog);
    }

    // JOptionPane.showConfirmDialog 互換
    public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType, int messageType) {
        return showOptionDialog(parentComponent, message, title, optionType, messageType, null, null, null);
    }

    // JOptionPane.showMessageDialog 互換
    public static void showMessageDialog(Component parentComponent, Object message, String title, int messageType) {
        showOptionDialog(parentComponent, message, title, JOptionPane.DEFAULT_OPTION, messageType, null, null, null);
    }

    // JOptionPane.showOptionDialog 互換
    public static int showOptionDialog(Component parentComponent, Object message, String title,
            int optionType, int messageType, Icon icon, final Object[] options, Object initialValue) {

        JOptionPane pane = new JOptionPane(message, messageType, optionType, icon, options, initialValue);
        pane.setInitialValue(initialValue);
        pane.selectInitialValue();
        JSheet.showSheet(pane, parentComponent, new SheetListener(){
            public void optionSelected(SheetEvent e) {
                answer = e.getOption();
            }
        });

        return answer;
    }

    // dialog = jop.createDialog を MyJSheet.createDialog(jop, parent) と置き換えるだけで JSheet になる
    public static MyJSheet createDialog(final JOptionPane pane, Component parentComponent) {
        Window window = getWindowForComponent(parentComponent);
        final MyJSheet sheet;
        if (window instanceof Frame) {
            sheet = new MyJSheet((Frame) window);
        } else {
            sheet = new MyJSheet((Dialog) window);
        }
        JComponent contentPane = (JComponent) sheet.getContentPane();
//      contentPane.setLayout(new BorderLayout());
        contentPane.add(pane, BorderLayout.NORTH);
        sheet.setResizable(false);
        sheet.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent ce) {
                // reset value to ensure closing works properly
                pane.setValue(JOptionPane.UNINITIALIZED_VALUE);
                Object object = pane.getInitialValue();
                if (object instanceof Component) {
                    ((Component) object).requestFocusInWindow();
                }
            }
        });
        pane.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent event) {
                // Let the defaultCloseOperation handle the closing
                // if the user closed the window without selecting a button
                // (newValue = null in that case).  Otherwise, close the sheet.
                if (sheet.isVisible() && event.getSource() == pane &&
                        (event.getPropertyName().equals(JOptionPane.VALUE_PROPERTY)) &&
                        event.getNewValue() != null &&
                        event.getNewValue() != JOptionPane.UNINITIALIZED_VALUE) {
                    sheet.setVisible(false);
                    sheet.dispose();
                    sheet.fireOptionSelected(pane);
                }
            }
        });
        sheet.pack();
        return sheet;
    }

    /**
     * その component に既に JSheet が表示されているかどうか
     * @param parentComponent
     * @return
     */
    public static boolean isAlreadyShown(Component parentComponent) {
        Window window = getWindowForComponent(parentComponent);
        Window[] windowList = window.getOwnedWindows();
        for (Window w : windowList) {
            if (w instanceof MyJSheet && w.isVisible()) {
                // すでに JSheet が表示されている
                return true;
            }
        }
        return false;
    }

    private static Window getWindowForComponent(Component parentComponent) {
        if (parentComponent == null) {
            return JOptionPane.getRootFrame();
        }
        if (parentComponent instanceof Frame || parentComponent instanceof Dialog) {
            return (Window) parentComponent;
        }
        return getWindowForComponent(parentComponent.getParent());
    }
}
