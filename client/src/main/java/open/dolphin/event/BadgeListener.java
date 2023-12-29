package open.dolphin.event;

import java.util.EventListener;

/**
 * BadgeEvent を渡す Listener.
 *
 * @author pns
 */
public interface BadgeListener extends EventListener {
    void badgeChanged(BadgeEvent e);
}
