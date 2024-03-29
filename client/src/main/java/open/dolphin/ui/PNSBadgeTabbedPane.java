package open.dolphin.ui;

import open.dolphin.client.Dolphin;
import open.dolphin.event.BadgeEvent;

import java.awt.*;

/**
 * Badge を付けられる PNSTabbedPane.
 * Badge を付けたい target に対してリスナを付ける.
 * <pre>
 * target.addBadgeListener(tabbedPane::setBadge);
 * </pre>
 * target で付けたいバッジを通知する.
 * <pre>
 * badgeEvent.setBadgeNumber(1);
 * badgeEvent.setTabIndex(0);
 * badgeListener.badgeChanged(e);
 * </pre>
 *
 * @author pns
 */
public class PNSBadgeTabbedPane extends PNSTabbedPane {

    private static final int BADGE_RADIUS = Dolphin.forWin ? 6 : 8;
    private static final int BADGE_FONT_SIZE = Dolphin.forWin ? 10 : 11;
    private static final int BADGE_OFFSET = 4;
    private static final String BADGE_FONT = "Arial";
    private static final Color BADGE_COLOR = new Color(233, 91, 73);

    private int tabIndex = 0;
    private int badgeNumber;
    private int fontSize = BADGE_FONT_SIZE;

    public PNSBadgeTabbedPane() {
        super();
        initComponents();
    }

    private void initComponents() {
        setButtonVgap(BADGE_RADIUS - BADGE_OFFSET);
    }

    @Override
    public void paintButtonPanel(Graphics graphics) {
        if (badgeNumber > 0) {
            Graphics2D g = (Graphics2D) graphics;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 円
            g.setColor(BADGE_COLOR);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
            Point p = getButtonTopRightCornerLocation(tabIndex);
            Point center = new Point(p.x - BADGE_OFFSET, p.y + BADGE_OFFSET);
            g.fillOval(center.x - BADGE_RADIUS, center.y - BADGE_RADIUS, BADGE_RADIUS * 2, BADGE_RADIUS * 2);

            // 文字
            g.setColor(Color.WHITE);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            if (badgeNumber >= 100) { fontSize = BADGE_FONT_SIZE - 3; }
            g.setFont(new Font(BADGE_FONT, Font.BOLD, fontSize));

            FontMetrics fm = g.getFontMetrics();
            String num = String.valueOf(badgeNumber);
            int strWidth = fm.stringWidth(num);

            g.drawString(num, center.x - strWidth / 2, center.y + fm.getHeight() / 2 - fm.getDescent());
        }
    }

    /**
     * BadgeEvent に従って Badge を付ける.
     *
     * @param e BadgeEvent
     */
    public void setBadge(BadgeEvent e) {
        badgeNumber = e.getBadgeNumber();
        tabIndex = e.getTabIndex();
        repaint();
    }
}
