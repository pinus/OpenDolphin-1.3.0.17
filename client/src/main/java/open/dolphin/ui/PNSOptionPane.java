package open.dolphin.ui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * PNSButton を使った JOptionPane.
 */
public class PNSOptionPane extends JOptionPane {

    static final String ok = "OK";
    static final String cancel = "Cancel";
    static final String yes = "はい";
    static final String no = "いいえ";

    public static void showMessageDialog(Component parentComponent, Object message) {
        showMessageDialog(parentComponent, message, UIManager.getString("OptionPane.messageDialogTitle"), INFORMATION_MESSAGE);
    }

    public static void showMessageDialog(Component parentComponent, Object message, String title, int messageType) {
        showMessageDialog(parentComponent, message, title, messageType, null);
    }

    public static void showMessageDialog(Component parentComponent, Object message, String title, int messageType, Icon icon) {
        JOptionPane pane = new PNSOptionPane(message, messageType, DEFAULT_OPTION);
        JDialog dialog = new JDialog();
        dialog.getRootPane().putClientProperty("apple.awt.transparentTitleBar", Boolean.TRUE);
        pane.addPropertyChangeListener(e -> {
            if (VALUE_PROPERTY.equals(e.getPropertyName())) {
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
        dialog.setModal(true);
        dialog.setTitle(title);
        dialog.add(pane);
        dialog.pack();
        dialog.setLocationRelativeTo(parentComponent);
        dialog.setVisible(true);
    }

    public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType, int messageType) {
        return showConfirmDialog(parentComponent, message, title, optionType, messageType, null);
    }

    public static int showConfirmDialog(Component parentComponent, Object message, String title, int optionType, int messageType, Icon icon) {
        return showOptionDialog(parentComponent, message, title, optionType, messageType, icon, null, null);
    }

    public static int showOptionDialog(Component parentComponent, Object message, String title, int optionType, int messageType, Icon icon, Object[] options, Object initialValue) {
        JOptionPane pane = new PNSOptionPane(message, messageType, optionType, icon, options, initialValue);
        JDialog dialog = new JDialog();
        dialog.getRootPane().putClientProperty("apple.awt.transparentTitleBar", Boolean.TRUE);

        pane.addPropertyChangeListener(e -> {
            if (VALUE_PROPERTY.equals(e.getPropertyName())) {
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
        dialog.setModal(true);
        dialog.setTitle(title);
        dialog.add(pane);
        dialog.pack();
        dialog.setLocationRelativeTo(parentComponent);
        dialog.setVisible(true);

        Object selectedValue = pane.getValue();
        if (selectedValue == null ) { return CLOSED_OPTION; }
        if (options == null) {
            if (selectedValue instanceof Integer) {
                return (Integer) selectedValue;
            }
            return CLOSED_OPTION;
        }
        for (int i=0; i<options.length; i++) {
            if (options[i].equals(selectedValue)) {
                return i;
            }
        }
        return CLOSED_OPTION;
    }

    public PNSOptionPane(Object message, int messageType) {
        this(message, messageType, DEFAULT_OPTION);
    }
    /**
     * optionType からオプションをでっち上げた JOptionPane を作る.
     *
     * @param message message
     * @param messageType message type
     * @param optionType option type
     */
    public PNSOptionPane(Object message, int messageType, int optionType) {
        super(message, messageType, optionType);
        setOptionsAndInitialValue(optionType);
    }

    /**
     * 文字列 options を PNSButton に変換して JOptionPane を作る.
     *
     * @param message message
     * @param messageType message type
     * @param optionType option type
     * @param icon icon
     * @param options options
     * @param initialValue initial value
     */
    public PNSOptionPane(Object message, int messageType, int optionType, Icon icon, Object[] options, Object initialValue) {
        super(message, messageType, optionType, icon, options, initialValue);
        if (Objects.nonNull(options)) {
            setOptionsAndInitialValue(options, initialValue);
        } else {
            setOptionsAndInitialValue(optionType);
        }
    }

    /**
     * PNSButton をでっち上げる setOptions.
     *
     * @param options options
     */
    @Override
    public void setOptions(Object[] options) {
        if (Objects.nonNull(options) && options[0] instanceof String) {
            JButton[] buttons = new PNSButton[options.length];
            for (int i=0; i<options.length; i++) {
                buttons[i] = new PNSButton((String) options[i]);
                int value = i;
                buttons[i].addActionListener(e -> setValue(value));
            }
            super.setOptions(buttons);
        } else {
            super.setOptions(options);
        }
    }

    /**
     * でっち上げた options から initialValue を取り出す.
     *
     * @param initialValue initial value
     */
    @Override
    public void setInitialValue(Object initialValue) {
        Object[] options = super.getOptions();
        if (Objects.nonNull(getOptions()) && options[0] instanceof JButton && initialValue instanceof String) {
            for (Object option : options) {
                if (((JButton) option).getText().equals(initialValue)) {
                    super.setInitialValue(option);
                    return;
                }
            }
        }
        super.setInitialValue(initialValue);
    }

    /**
     * optionType から options と initialValue をでっち上げて設定する.
     *
     * @param optionType option type
     */
    public void setOptionsAndInitialValue(int optionType) {
        String[] options;
        String initialValue;

        switch (optionType) {
            case YES_NO_OPTION -> {
                options = new String[]{yes, no};
                initialValue = yes;
            }
            case YES_NO_CANCEL_OPTION -> {
                options = new String[]{yes, no, cancel};
                initialValue = yes;

            }
            case OK_CANCEL_OPTION -> {
                options = new String[]{ok, cancel};
                initialValue = ok;
            }
            default -> {
                options = new String[]{ok};
                initialValue = ok;
            }
        }
        setOptionsAndInitialValue(options, initialValue);
    }

    /**
     * 文字列の options, initialValue から PNSButton を作って設定する.
     *
     * @param options options
     * @param initialValue initial value
     */
    public void setOptionsAndInitialValue(Object[] options, Object initialValue) {
        if (Objects.nonNull(options) && options[0] instanceof String) {
            JButton[] buttons = new PNSButton[options.length];
            for (int i=0; i<options.length; i++) {
                buttons[i] = new PNSButton((String) options[i]);
                int value = i;
                buttons[i].addActionListener(e -> setValue(value));
                if (options[i].equals(initialValue)) {
                    setInitialValue(buttons[i]);
                }
            }
            setOptions(buttons);
        }
    }
}
