package open.dolphin.ui.desktop;

import java.util.HashMap;

public class Desktop {
    private static Desktop DESKTOP = new Desktop();
    private static com.apple.eawt.Application APP = com.apple.eawt.Application.getApplication();
    private static HashMap<SystemEventListener, com.apple.eawt.AppForegroundListener> APP_LISTENERS = new HashMap<>();

    public static Desktop getDesktop() {
        return DESKTOP;
    }

    public void addAppEventListener(SystemEventListener listener) {
        if (listener instanceof AppForegroundListener) {
            AppForegroundListener l = (AppForegroundListener) listener;
            AppForegroundEvent evt = new AppForegroundEvent();
            com.apple.eawt.AppForegroundListener appleListener = new com.apple.eawt.AppForegroundListener() {
                @Override
                public void appRaisedToForeground(com.apple.eawt.AppEvent.AppForegroundEvent appForegroundEvent) {
                    l.appRaisedToForeground(evt);
                }
                @Override
                public void appMovedToBackground(com.apple.eawt.AppEvent.AppForegroundEvent appForegroundEvent) {
                    l.appMovedToBackground(evt);
                }
            };

            APP.addAppEventListener(appleListener);
            APP_LISTENERS.put(listener, appleListener);
        }
    }

    public void removeAppEventListener(SystemEventListener listener) {
        com.apple.eawt.AppForegroundListener appleListener = APP_LISTENERS.get(listener);
        APP.removeAppEventListener(appleListener);
    }

    public void setAboutHandler(AboutHandler aboutHandler) {
        com.apple.eawt.AboutHandler appleHandler = ae -> aboutHandler.handleAbout(new AboutEvent());
        APP.setAboutHandler(appleHandler);
    }

    public void setPreferencesHandler(PreferencesHandler preferencesHandler) {
        com.apple.eawt.PreferencesHandler appleHandler = pe -> preferencesHandler.handlePreferences(new PreferencesEvent());
        APP.setPreferencesHandler(appleHandler);
    }

    public void setQuitHandler(QuitHandler quitHandler) {
        com.apple.eawt.QuitHandler appleHandler = (qe, qr) -> quitHandler.handleQuitRequestWith(new QuitEvent(), new QuitResponse() {
            @Override
            public void performQuit() { }
            @Override
            public void cancelQuit() {
                qr.cancelQuit();
            }
        });
        APP.setQuitHandler(appleHandler);
    }
}
