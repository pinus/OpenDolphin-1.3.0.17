package open.dolphin.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Java Swing Hacks #36
 */
public class MoreInfoPanel extends JPanel {
    public static final int SPIN_WIDGET_HEIGHT = 14;
        public Component topComponent;
    public Component bottomComponent;
    protected SpinWidget spinWidget;
    private JPanel spinComponent;

    public MoreInfoPanel(Component tc, Component mic) {
        this(tc, mic, "");
    }

    public MoreInfoPanel(Component tc, Component mic, String spinMessage) {
        topComponent = tc;
        spinComponent = new JPanel(new BorderLayout());
        spinWidget = new SpinWidget();
        spinComponent.add(spinWidget);

        spinComponent.add(spinWidget, BorderLayout.WEST);

        JLabel messageLabel = new JLabel(spinMessage);
        spinComponent.add(messageLabel, BorderLayout.CENTER);
        messageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                spinWidget.handleClick();
            }
        });

        bottomComponent = mic;
        doMyLayout();
    }

    protected void doMyLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(topComponent);
        add(spinComponent);
        add(bottomComponent);
        resetBottomVisibility();
    }

    protected void resetBottomVisibility() {
        if ((bottomComponent == null) ||
                (spinWidget == null))
            return;
        bottomComponent.setVisible(spinWidget.isOpen());
        revalidate();
        if (isShowing()) {
            Container ancestor = getTopLevelAncestor();
            if (ancestor instanceof Window)
                ((Window) ancestor).pack();
            repaint();
        }
    }

    public void showBottom(boolean b) {
        spinWidget.setOpen(b);
    }

    public boolean isBottomShowing() {
        return spinWidget.isOpen();
    }


    public class SpinWidget extends JPanel {
                final int HALF_HEIGHT = SPIN_WIDGET_HEIGHT / 2;
        boolean open;
        Dimension mySize = new Dimension(SPIN_WIDGET_HEIGHT, SPIN_WIDGET_HEIGHT);
        int offsetX = 1;
        int offsetY = 3;

        int[] openXPoints =
                {1 + offsetX, HALF_HEIGHT + offsetX, SPIN_WIDGET_HEIGHT - 1 + offsetX};
        int[] openYPoints =
                {HALF_HEIGHT + offsetY, SPIN_WIDGET_HEIGHT - 1 + offsetY, HALF_HEIGHT + offsetY};
        int[] closedXPoints =
                {1 + offsetX, 1 + offsetX, HALF_HEIGHT + offsetX};
        int[] closedYPoints =
                {1 + offsetY, SPIN_WIDGET_HEIGHT - 1 + offsetY, HALF_HEIGHT + offsetY};

        Polygon openTriangle =
                new Polygon(openXPoints, openYPoints, 3);
        Polygon closedTriangle =
                new Polygon(closedXPoints, closedYPoints, 3);

        public SpinWidget() {
            setOpen(false);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleClick();
                }
            });
        }

        public void handleClick() {
            setOpen(!isOpen());
        }

        public boolean isOpen() {
            return open;
        }

        public void setOpen(boolean o) {
            open = o;
            resetBottomVisibility();
        }

        @Override
        public Dimension getMinimumSize() {
            return mySize;
        }

        @Override
        public Dimension getPreferredSize() {
            return mySize;
        }

        // don't override update(), get the default clear
        @Override
        public void paint(Graphics g) {
            if (isOpen())
                g.fillPolygon(openTriangle);
            else
                g.fillPolygon(closedTriangle);
        }

    }

}
