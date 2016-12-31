package open.dolphin.client;

import javax.swing.JPanel;
import open.dolphin.event.BadgeListener;

/**
 *
 * @author kazm
 */
public interface MainComponent extends MainTool {

    public String getIcon();

    public void setIcon(String icon);

    public JPanel getUI();

    public void setUI(JPanel panel);

    public void addBadgeListener(BadgeListener listener);

}
