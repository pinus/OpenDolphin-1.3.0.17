package open.dolphin.event;

import java.util.EventObject;

/**
 * PNSBadgeTabbedPane に送る BadgeEvent.
 * @author pns
 */
public class BadgeEvent extends EventObject {
    private static final long serialVersionUID = 1L;

    private int badgeNumber;
    private int tabIndex;

    public BadgeEvent(Object source) {
        super(source);
    }

    /**
     * Badge に表示する数字.
     * @return the badgeNumber
     */
    public int getBadgeNumber() {
        return badgeNumber;
    }

    /**
     *
     * @param badgeNumber the badgeNumber to set
     */
    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    /**
     * Badge を付ける PNSBadgeTabbedPane の Index.
     * @return the tabIndex
     */
    public int getTabIndex() {
        return tabIndex;
    }

    /**
     *
     * @param tabIndex the tabIndex to set
     */
    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }
}
