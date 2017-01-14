package open.dolphin.setting;

import java.util.EventListener;

/**
 *
 * @author pns
 */
public interface StateListener extends EventListener {

    public void state(SettingPanelState state);
}
