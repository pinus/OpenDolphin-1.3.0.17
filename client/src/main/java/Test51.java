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

        JToggleButton boldButton = new LetterButton("B", "bold left");
        JToggleButton italicButton = new LetterButton("I", "italic");
        JToggleButton underlineButton = new LetterButton("U", "underline right");

        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(boldButton);
        buttonPanel.add(italicButton);
        buttonPanel.add(underlineButton);
        buttonPanel.add(Box.createHorizontalGlue());

        frame.add(content);
        frame.pack();
        frame.setVisible(true);
    }

    static class LetterButton extends PNSToggleButton {
        private Font boldFont = new Font("Courier", Font.BOLD, 16);
        private Font italicFont = new Font("Courier", Font.ITALIC, 16);
        private Font plainFont = new Font("Courier", Font.PLAIN, 16);

        private String letter;
        private boolean bold, italic, underline;

        public LetterButton(String letter, String format) {
            super(format);
            this.letter = letter;
            bold = format.contains("bold");
            italic = format.contains("italic");
            underline = format.contains("underline");

            setPreferredSize(new Dimension(48,24));
            setBorderPainted(false);
            setSelected(false);
        }

        @Override
        public void paintIcon(Graphics2D g) {
            FontMetrics fm = g.getFontMetrics();
            int strW = fm.stringWidth(letter);
            int strH = fm.getAscent() - 2;
            int w = getWidth();
            int h = getHeight();

            if (bold) { g.setFont(boldFont); }
            else if (italic) { g.setFont(italicFont); }
            else { g.setFont(plainFont); }

            g.drawString(letter, (w - strW) / 2, (h + strH) / 2);
        }
    }

    static class PNSToggleButton extends JToggleButton {

        private final Color INACTIVE_FRAME = new Color(219, 219, 219);
        private final Color INACTIVE_FILL_SELECTED = new Color(227, 227, 227);
        private final Color INACTIVE_FILL = new Color(246, 246, 246);
        private final Color ACTIVE_FRAME = new Color(175, 175, 175);
        private final Color ACTIVE_FRAME_SELECTED = new Color(91, 91, 91);
        private final Color ACTIVE_FILL = new Color(240, 240, 240);
        private final Color ACTIVE_FILL_SELECTED = new Color(100, 100, 100);
        private final Color INACTIVE_TEXT = new Color(180, 180, 180);
        private final Color INACTIVE_TEXT_SELECTED = new Color(50, 50, 50);

        protected Window parent = null;
        protected boolean appForeground = true;
        private int swingConstant;

        public PNSToggleButton(String format) {
            swingConstant = format.contains("right")
                    ? SwingConstants.RIGHT
                    : format.contains("left")
                    ? SwingConstants.LEFT
                    : SwingConstants.CENTER;
        }

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

        @Override
        public void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // background and border
            if (parent.isActive() && appForeground) {
                if (this.isSelected()) {
                    g.setColor(ACTIVE_FILL_SELECTED);
                    fill(g, w, h);

                    g.setColor(ACTIVE_FRAME_SELECTED);

                } else {
                    g.setColor(ACTIVE_FILL);
                    fill(g, w, h);

                    g.setColor(ACTIVE_FRAME);
                }

            } else {
                if (this.isSelected()) {
                    g.setColor(INACTIVE_FILL_SELECTED);
                    fill(g, w, h);

                    g.setColor(INACTIVE_FRAME);

                } else {
                    g.setColor(INACTIVE_FILL);
                    fill(g, w, h);

                    g.setColor(INACTIVE_FRAME);

                }
            }

            // foreground
            if (parent.isActive() && appForeground) {
                if (this.isSelected()) {
                    g.setColor(Color.WHITE);
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
            paintIcon(g);
        }

        private void fill(Graphics2D g, int w, int h) {
            if (swingConstant == SwingConstants.LEFT) {
                g.fillRoundRect(0, 0, w, h, 10, 10);
                g.fillRect(w-10,0, w, h);
            } else if (swingConstant == SwingConstants.RIGHT) {
                g.fillRoundRect(0, 0, w, h, 10, 10);
                g.fillRect(0,0,10, h);
            } else {
                g.fillRect(0, 0, w, h);
            }
        }

        public void paintIcon(Graphics2D g) { }
    }
}
