package open.dolphin.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * UltraSonicProgressLabel
 *
 * @author Minagawa, Kazushi
 */
public class UltraSonicProgressLabel extends JLabel {

    protected static final Dimension DEFAULT_SIZE = new Dimension(100, 14);
    protected static final Color BORDER_COLOR = new Color(162, 199, 237);
    protected static final Color WATER_COLOR = new Color(112, 173, 239);
    protected static final Color PULSE_HEAD = new Color(110, 170, 238);
    protected static final Color PULSE_TAIL = Color.WHITE;
    protected static final int PULSE_COUNT = 10;
    protected static final int DEFAULT_FPS = 10;  // 10 frames per sec
        private Area[] pulses;

    private int pulseCount = PULSE_COUNT;

    private Color borderColor = BORDER_COLOR;

    private Color waterColor = WATER_COLOR;

    private Color pulseHeadColor = PULSE_HEAD;

    private Color pulseTailColor = PULSE_TAIL;

    private int fps = DEFAULT_FPS;

    private int frame;

    private boolean started;

    private Timer timer;


    /**
     * Creates a new progress panel with default values
     */
    public UltraSonicProgressLabel() {
        this.setPreferredSize(DEFAULT_SIZE);
    }

    public int getPulseCount() {
        return pulseCount;
    }

    public void setPulseCount(int pulseCount) {
        this.pulseCount = pulseCount;
    }

    public Color getPulseTailColor() {
        return pulseTailColor;
    }

    public void setPulseTailColor(Color pulseEndColor) {
        this.pulseTailColor = pulseEndColor;
    }

    public Color getPulseHeadColor() {
        return pulseHeadColor;
    }

    public void setPulseHeadColor(Color pulseStartColor) {
        this.pulseHeadColor = pulseStartColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getWaterColor() {
        return waterColor;
    }

    public void setWaterColor(Color waterColor) {
        this.waterColor = waterColor;
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void start() {
        pulses = buildPulses();
        started = true;
        int delay = 1000 / fps;
        timer = new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        timer.start();
    }

    public void stop() {
        timer.stop();
        started = false;
        repaint();
        frame = 0;
    }

    public void advance() {
    }

    @Override
    public void paintComponent(Graphics gracphics) {

        Graphics2D g = (Graphics2D) gracphics.create();
        int x = 0;
        int y = 0;
        int width = getWidth();
        int height = getHeight();

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHints(rh);

        if (started) {
            // Border ?????????????????????
            g.setColor(borderColor);
            g.draw(getProgressBorder());
            // ????????????????????????????????????
            GradientPaint borderGradient = new GradientPaint(x + 1, height / 2, waterColor, width - 1, height / 2, borderColor);
            g.setPaint(borderGradient);
            g.fill(getProgressContent());

            // ??????????????????????????????
            Area pulse = pulses[frame % pulseCount];
            int pw = (int) pulse.getBounds().getWidth();
            int tail = (int) pulse.getBounds().getX();
            int top = tail + pw;
            GradientPaint pulseGradient = new GradientPaint(top, 0, pulseHeadColor, tail, 0, pulseTailColor);
            g.setPaint(pulseGradient);
            g.fill(pulse);

            frame++;

        } else {
            // Border ?????????????????????
            g.setColor(Color.LIGHT_GRAY);
            g.draw(getProgressBorder());
            // ????????????????????????????????????
            GradientPaint borderGradient = new GradientPaint(x + 1, y + 1, Color.white, x + 1, height - 1, Color.LIGHT_GRAY);
            g.setPaint(borderGradient);
            g.fill(getProgressContent());
        }
        g.dispose();
    }

    private Area[] buildPulses() {

        Area[] ret = new Area[pulseCount];

        double fixedWidth = getWidth() / ((double) pulseCount);

        for (int i = 0; i < pulseCount; i++) {
            Area primitive = buildPrimitive();
            AffineTransform toRight = AffineTransform.getTranslateInstance(i * fixedWidth, 0);
            primitive.transform(toRight);
            ret[i] = primitive;
        }

        return ret;
    }

    private Area buildPrimitive() {

        int width = getWidth() - 1;
        int height = getHeight() - 1;
        int pw = width / pulseCount;

        Rectangle2D.Double body = new Rectangle2D.Double(1, 1, pw, height);

        Area primitive = new Area(body);

        return primitive;
    }

    private RoundRectangle2D.Double getProgressBorder() {
        return new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 4, 4);

    }

    private RoundRectangle2D.Double getProgressContent() {
        return new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, 3, 3);
    }
}
