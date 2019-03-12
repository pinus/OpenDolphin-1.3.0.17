package open.dolphin.calendar;

import open.dolphin.infomodel.SimpleDate;

import java.util.EventListener;

/**
 *
 * @author pns
 */
public interface CalendarListener extends EventListener {

    public void dateSelected(SimpleDate date);
}
