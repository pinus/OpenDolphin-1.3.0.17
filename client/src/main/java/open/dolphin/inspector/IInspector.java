package open.dolphin.inspector;

import javax.swing.JPanel;
import open.dolphin.client.ClientContext;
import open.dolphin.event.BadgeListener;

/**
 *
 * @author pns
 */
public interface IInspector {
    
    public static final int DEFAULT_WIDTH = ClientContext.isMac()? 280 : 260;
    public static final int DEFAULT_HEIGHT = ClientContext.isMac()? 175 : 178;

    public JPanel getPanel();

    public void update();

    default public void addBadgeListener(BadgeListener listener) {}
}
