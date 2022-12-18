package open.dolphin.ui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Default ボタンが青地に白になる JButton.
 * com.apple.laf.AquaButtonUI が extend できなくなったので作った.
 */
public class PNSButton extends JButton {
    private Window parent;

    public PNSButton() {
        this(null, null);
    }

    public PNSButton(Icon icon) {
        this(null, icon);
    }

    public PNSButton(String text) {
        this(text, null);
    }

    public PNSButton(Action a) { super(a); }

    public PNSButton(String text, Icon icon) {
        super(text, icon);
    }

    public void paint(Graphics g) {
        parent = SwingUtilities.getWindowAncestor(this);

        if (model.isEnabled()) {
            if (Objects.nonNull(parent) && parent.isActive() && isDefaultButton() && !model.isPressed()) {
                setForeground(Color.WHITE);
            } else {
                setForeground(Color.BLACK);
            }
        } else {
            setForeground(Color.GRAY);
        }
        super.paint(g);
    }
}