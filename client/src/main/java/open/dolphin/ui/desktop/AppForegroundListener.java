package open.dolphin.ui.desktop;

public interface AppForegroundListener extends SystemEventListener {
    public void appRaisedToForeground(final AppForegroundEvent e);
    public void appMovedToBackground(final AppForegroundEvent e);
}
