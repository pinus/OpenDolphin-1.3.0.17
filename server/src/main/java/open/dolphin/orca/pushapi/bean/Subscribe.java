package open.dolphin.orca.pushapi.bean;

import open.dolphin.orca.pushapi.SubscriptionEvent;

/**
 * Subscribe Command.
 *
 * @author pns
 */
public final class Subscribe extends Command {
    public Subscribe(SubscriptionEvent event) {
        setCommand("subscribe");
        setEvent(event.eventName());
    }
}
