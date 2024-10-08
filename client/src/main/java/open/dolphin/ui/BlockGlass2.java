package open.dolphin.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.*;

/**
 * An infinite progress panel displays a rotating figure and
 * a message to notice the user of a long, duration unknown
 * task. The shape and the text are drawn upon a white veil
 * which alpha level (or shield value) lets the underlying
 * component shine through. This panel is meant to be used
 * asa <i>glass pane</i> in the window performing the long
 * operation.
 * <br><br>
 * On the contrary to regular glass panes, you don't need to
 * set it visible or not by yourself. Once you've started the
 * animation all the mouse events are intercepted by this
 * panel, preventing them from being forwared to the
 * underlying components.
 * <br><br>
 * The panel can be controlled by the <code>start()</code>,
 * <code>stop()</code> and <code>interrupt()</code> methods.
 * <br><br>
 * Example:
 * <br><br>
 * <pre>BlockGlass pane = new BlockGlass();
 * frame.setGlassPane(pane);
 * pane.start()</pre>
 * <br><br>
 * Several properties can be configured at creation time. The
 * message and its font can be changed at runtime. Changing the
 * font can be done using <code>setFont()</code> and
 * <code>setForeground()</code>.
 *
 * @author Romain Guy, modified by pns
 * @version 2.0-pns
 */

public class BlockGlass2 extends JComponent implements MouseListener {
    /**
     * Contains the bars composing the circular shape.
     */
    protected Area[] ticker = null;
    /**
     * The animation thread is responsible for fade in/out and rotation.
     */
    protected Thread animationThread = null;
    /**
     * Runnable for the animation thread.
     */
    private Animator animator;
    /**
     * Notifies whether the animation is running or not.
     */
    protected boolean started = false;
    /**
     * Alpha level of the veil, used for fade in/out.
     */
    protected int alphaLevel = 0;
    /**
     * Initial delay before ramp up
     */
    private final int initialDelay = 300;
    /**
     * Duration of the veil's fade in/out.
     */
    protected int rampDelay = 100;
    /**
     * Color of the veil.
     */
    private final int veilColor = 200;
    /**
     * Alpha level of the veil.
     */
    protected float shield = 0.70f;
    /**
     * Message displayed below the circular shape.
     */
    protected String text = "";
    /**
     * Amount of bars composing the circular shape.
     */
    protected int barsCount = 14;
    /**
     * Amount of frames per seconde. Lowers this to save CPU.
     */
    protected float fps = 15.0f;
    /**
     * Rendering hints to set anti aliasing.
     */
    protected RenderingHints hints = null;
    /**
     * Frame width and height
     */
    private int frameWidth;
    private int frameHeight;
    /**
     * Show ticker.
     */
    private boolean showTicker = true;

    private final Logger logger = LoggerFactory.getLogger(BlockGlass2.class);

    /**
     * Creates a new progress panel with default values:<br>
     * <ul>
     * <li>No message</li>
     * <li>14 bars</li>
     * <li>Veil's alpha level is 70%</li>
     * <li>15 frames per second</li>
     * <li>Fade in/out last 300 ms</li>
     * </ul>
     */
    public BlockGlass2() {
        this("");
    }

    /**
     * Creates a new progress panel with default values:<br>
     * <ul>
     * <li>14 bars</li>
     * <li>Veil's alpha level is 70%</li>
     * <li>15 frames per second</li>
     * <li>Fade in/out last 300 ms</li>
     * </ul>
     *
     * @param text The message to be displayed. Can be null or empty.
     */
    public BlockGlass2(String text) {
        this(text, 14);
    }

    /**
     * Creates a new progress panel with default values:<br>
     * <ul>
     * <li>Veil's alpha level is 70%</li>
     * <li>15 frames per second</li>
     * <li>Fade in/out last 300 ms</li>
     * </ul>
     *
     * @param text      The message to be displayed. Can be null or empty.
     * @param barsCount The amount of bars composing the circular shape
     */
    public BlockGlass2(String text, int barsCount) {
        this(text, barsCount, 0.70f);
    }

    /**
     * Creates a new progress panel with default values:<br>
     * <ul>
     * <li>15 frames per second</li>
     * <li>Fade in/out last 300 ms</li>
     * </ul>
     *
     * @param text      The message to be displayed. Can be null or empty.
     * @param barsCount The amount of bars composing the circular shape.
     * @param shield    The alpha level between 0.0 and 1.0 of the colored
     *                  shield (or veil).
     */
    public BlockGlass2(String text, int barsCount, float shield) {
        this(text, barsCount, shield, 15.0f);
    }

    /**
     * Creates a new progress panel with default values:<br>
     * <ul>
     * <li>Fade in/out last 300 ms</li>
     * </ul>
     *
     * @param text      The message to be displayed. Can be null or empty.
     * @param barsCount The amount of bars composing the circular shape.
     * @param shield    The alpha level between 0.0 and 1.0 of the colored
     *                  shield (or veil).
     * @param fps       The number of frames per second. Lower this value to
     *                  decrease CPU usage.
     */
    public BlockGlass2(String text, int barsCount, float shield, float fps) {
        this(text, barsCount, shield, fps, 300);
    }

    /**
     * Creates a new progress panel.
     *
     * @param text      The message to be displayed. Can be null or empty.
     * @param barsCount The amount of bars composing the circular shape.
     * @param shield    The alpha level between 0.0 and 1.0 of the colored
     *                  shield (or veil).
     * @param fps       The number of frames per second. Lower this value to
     *                  decrease CPU usage.
     * @param rampDelay The duration, in milliseconds, of the fade in and
     *                  the fade out of the veil.
     */
    public BlockGlass2(String text, int barsCount, float shield, float fps, int rampDelay) {
        this.text = text;
        this.rampDelay = Math.max(rampDelay, 0);
        this.shield = Math.max(shield, 0.0f);
        this.fps = fps > 0.0f ? fps : 15.0f;
        this.barsCount = barsCount > 0 ? barsCount : 14;

        this.hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        this.hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.hints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    /**
     * Returns the current displayed message.
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * Changes the displayed message at runtime.
     *
     * @param text The message to be displayed. Can be null or empty.
     */
    public void setText(String text) {
        this.text = text;
        repaint();
    }

    /**
     * Starts / stops the waiting animation according to the visibility.
     *
     * @param visible  true to make the component visible; false to make it invisible
     */
    @Override
    public void setVisible(boolean visible) {
        synchronized (this) {
            if (visible) {
                start();
            } else {
                stop();
            }
        }
    }

    /**
     * Starts the waiting animation by fading the veil in, then rotating the ticker.
     * Ticker visibility can be controlled by the client property blockglass.show.ticker.
     * This method handles the visibility of the glass pane.
     */
    private void start() {
        super.setVisible(true);
        showTicker = getClientProperty("blockglass.show.ticker") instanceof Boolean b ? b : true;
        addMouseListener(this);
        frameHeight = getHeight();
        frameWidth = getWidth();
        ticker = buildTicker();
        animator = new Animator(true);
        animationThread = new Thread(animator);
        animationThread.setPriority(Thread.MIN_PRIORITY);
        animationThread.start();
    }

    /**
     * Stops the waiting animation by stopping the rotation
     * of the circular shape and then by fading out the veil.
     * This methods sets the panel invisible at the end.
     */
    private void stop() {
        if (animationThread != null && animator.rampUp) {
            animationThread.interrupt();
            animator = new Animator(false);
            animationThread = new Thread(animator);
            animationThread.setPriority(Thread.MIN_PRIORITY);
            animationThread.start();
        }
    }

    /**
     * Interrupts the animation, whatever its state is. You
     * can use it when you need to stop the animation without
     * running the fade out phase.
     * This methods sets the panel invisible at the end.
     */
    public void interrupt() {
        synchronized (this) {
            if (animationThread != null) {
                //logger.info("interrupted:" + System.identityHashCode(animationThread));
                //StackTracer.showTrace(3);
                animationThread.interrupt();
                animationThread = null;
            }
            removeMouseListener(this);
            super.setVisible(false);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        if (started) {
            double maxY = 0.0;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHints(hints);

            g2.setColor(new Color(veilColor, veilColor, veilColor, (int) (alphaLevel * shield)));
            g2.fillRect(0, 0, frameWidth, frameHeight);

            if (showTicker) {
                for (int i = 0; i < ticker.length; i++) {
                    int channel = 224 - 128 / (i + 1);
                    g2.setColor(new Color(channel, channel, channel, alphaLevel));
                    g2.fill(ticker[i]);

                    Rectangle2D bounds = ticker[i].getBounds2D();
                    if (bounds.getMaxY() > maxY) {
                        maxY = bounds.getMaxY();
                    }
                }
            }

            if (text != null && !text.isEmpty()) {
                FontRenderContext context = g2.getFontRenderContext();
                TextLayout layout = new TextLayout(text, getFont(), context);
                Rectangle2D bounds = layout.getBounds();
                g2.setColor(getForeground());
                layout.draw(g2, (float) (frameWidth - bounds.getWidth()) / 2,
                    (float) (maxY + layout.getLeading() + 2 * layout.getAscent()));
            }
        }
    }

    /**
     * Builds the circular shape and returns the result as an array of
     * <code>Area</code>. Each <code>Area</code> is one of the bars
     * composing the shape.
     */
    private Area[] buildTicker() {
        Area[] tickers = new Area[barsCount];
        Point2D.Double center = new Point2D.Double((double) frameWidth / 2, (double) frameHeight / 2);

        double fixedAngle = 2.0 * Math.PI / barsCount;

        for (double i = 0.0; i < barsCount; i++) {
            Area primitive = buildPrimitive();

            AffineTransform toCenter = AffineTransform.getTranslateInstance(center.getX(), center.getY());
            AffineTransform toBorder = AffineTransform.getTranslateInstance(15.0, -2.0);
            AffineTransform toCircle = AffineTransform.getRotateInstance(-i * fixedAngle, center.getX(), center.getY());

            AffineTransform toWheel = new AffineTransform();
            toWheel.concatenate(toCenter);
            toWheel.concatenate(toBorder);

            primitive.transform(toWheel);
            primitive.transform(toCircle);

            tickers[(int) i] = primitive;
        }

        return tickers;
    }

    /**
     * Builds a bar.
     */
    private Area buildPrimitive() {
        Rectangle2D.Double body = new Rectangle2D.Double(2, 0, 10, 4);
        Ellipse2D.Double head = new Ellipse2D.Double(0, 0, 4, 4);
        Ellipse2D.Double tail = new Ellipse2D.Double(10, 0, 4, 4);

        Area tick = new Area(body);
        tick.add(new Area(head));
        tick.add(new Area(tail));

        return tick;
    }

    private void beep() {
        Toolkit.getDefaultToolkit().beep();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        beep();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Animation thread.
     */
    private class Animator implements Runnable {
        public boolean rampUp;

        protected Animator(boolean rampUp) {
            this.rampUp = rampUp;
        }

        @Override
        public void run() {
            Point2D.Double center = new Point2D.Double((double) frameWidth / 2, (double) frameHeight / 2);
            double fixedIncrement = 2.0 * Math.PI / barsCount;
            AffineTransform toCircle = AffineTransform.getRotateInstance(fixedIncrement, center.getX(), center.getY());

            Window parent = SwingUtilities.getWindowAncestor(BlockGlass2.this);
            //logger.info((rampUp? "rumpup:" : "rumpdown:") + " parent = " + ((JFrame)parent).getTitle());

            /* initial delay */
            if (rampUp) {
                try {
                    Thread.sleep(initialDelay);
                } catch (InterruptedException ie) {
                    started = false;
                    repaint();
                    interrupt();
                    return;
                }
            }

            long start = System.currentTimeMillis();
            if (rampDelay == 0) {
                alphaLevel = rampUp ? 255 : 0;
            }

            started = true;
            boolean inRamp = rampUp;

            while (!Thread.interrupted()) {
                // visible でないのに animation が続くのを防ぐ安全装置
                if (parent == null || !parent.isVisible()) {
                    interrupt();
                    return;
                }

                if (!inRamp) {
                    for (Area ticker1 : ticker) {
                        ticker1.transform(toCircle);
                    }
                }

                repaint();

                if (rampUp) {
                    if (alphaLevel < 255) {
                        alphaLevel = (int) (255 * (System.currentTimeMillis() - start) / rampDelay);
                        if (alphaLevel >= 255) {
                            alphaLevel = 255;
                            inRamp = false;
                        }
                    }
                } else if (alphaLevel > 0) {
                    alphaLevel = (int) (255 - (255 * (System.currentTimeMillis() - start) / rampDelay));
                    if (alphaLevel <= 0) {
                        alphaLevel = 0;
                        break;
                    }
                } else { // rampDown with alphaLevel == 0
                    break;
                }

                try {
                    Thread.sleep(inRamp ? 10 : (long) (1000 / fps));
                } catch (InterruptedException ie) {
                    break;
                }
                Thread.yield();
                if (rampUp) System.out.print(".");
                else System.out.print(",");
            }
            // System.out.println("blocking animation done");

            if (!rampUp) {
                started = false;
                repaint();
                interrupt();
            }
        }
    }
}
