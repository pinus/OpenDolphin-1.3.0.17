import javax.swing.*;
import java.awt.*;

/**
 * @author pns
 */
public class Test51 {

    public static void main(String[] argv) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getRootPane().putClientProperty("apple.awt.brushMetalLook", Boolean.TRUE);

        JPanel cmdPanel = new JPanel();
        cmdPanel.setPreferredSize(new Dimension(600, 26));
        cmdPanel.setLayout(new BoxLayout(cmdPanel, BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        cmdPanel.add(buttonPanel);
        cmdPanel.add(Box.createVerticalGlue());

        JTextArea textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(600,500));
        textArea.setText("TEST");

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(cmdPanel);
        content.add(textArea);

        JToggleButton boldButton = new BoldButton();
        boldButton.setPreferredSize(new Dimension(22,22));
        boldButton.setBorderPainted(false);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(boldButton);
        buttonPanel.add(Box.createHorizontalGlue());

        frame.add(content);
        frame.pack();
        frame.setVisible(true);
    }


    static abstract class ButtonBase extends JToggleButton {
        private final Color INACTIVE_FRAME = new Color(219, 219, 219);
        private final Color INACTIVE_FILL_SELECTED = new Color(227, 227, 227);
        private final Color INACTIVE_FILL = new Color(246, 246, 246);
        private final Color ACTIVE_FRAME = new Color(175, 175, 175);
        private final Color ACTIVE_FRAME_SELECTED = new Color(91, 91, 91);
        private final Color ACTIVE_FILL = new Color(240, 240, 240);
        private final Color ACTIVE_FILL_SELECTED = new Color(100, 100, 100);
        private final Color INACTIVE_TEXT = new Color(180, 180, 180);
        private final Color INACTIVE_TEXT_SELECTED = new Color(50, 50, 50);

        /**
         * 親の Window
         */
        protected Window parent = null;

        /**
         * Application が foreground かどうか
         */
        protected boolean appForeground = true;

        protected Font font = new Font(Font.SANS_SERIF, Font.BOLD, 16);

        @Override
        public void addNotify() {
            super.addNotify();

            if (parent == null) {
                parent = SwingUtilities.windowForComponent(this);

                // AppForegroundListener
                com.apple.eawt.Application app = com.apple.eawt.Application.getApplication();
                app.addAppEventListener(new com.apple.eawt.AppForegroundListener() {
                    @Override
                    public void appRaisedToForeground(com.apple.eawt.AppEvent.AppForegroundEvent afe) {
                        appForeground = true;
                        repaint();
                    }

                    @Override
                    public void appMovedToBackground(com.apple.eawt.AppEvent.AppForegroundEvent afe) {
                        appForeground = false;
                        repaint();
                    }
                });

            }
        }

        protected void setForegroundColor(Graphics2D g) {
            if (parent.isActive() && appForeground) {
                if (this.isSelected()) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.BLACK);
                }
            } else {
                if (this.isSelected()) {
                    g.setColor(INACTIVE_TEXT_SELECTED);
                } else {
                    g.setColor(INACTIVE_TEXT);
                }
            }
        }

        protected void drawBorder(Graphics2D g, int swingConstant) {
            if (parent.isActive() && appForeground) {
                if (this.isSelected()) {
                    g.setColor(ACTIVE_FILL_SELECTED);

                    g.setColor(ACTIVE_FRAME_SELECTED);

                } else {
                    g.setColor(ACTIVE_FILL); }

                    g.setColor(ACTIVE_FRAME);

            } else {
                if (this.isSelected()) {
                    g.setColor(INACTIVE_FILL_SELECTED);

                    g.setColor(INACTIVE_FRAME);
                } else {
                    g.setColor(INACTIVE_FILL);

                    g.setColor(INACTIVE_FRAME);
                }
            }
        }
    }

    static class BoldButton extends ButtonBase {
        @Override
        public void paintComponent(Graphics graphics) {
            // super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            FontMetrics fm = g.getFontMetrics();
            int strW = fm.stringWidth("B");
            int strH = fm.getHeight();
            int w = getWidth();
            int h = getHeight();

            setForegroundColor(g);
            g.drawString("B", (w + strW) / 2, (h + strH) / 2 );
            drawBorder(g, SwingConstants.LEFT);
        }
    }
}
