package open.dolphin.helper;

import com.apple.eawt.AppEvent;
import com.apple.eawt.AppForegroundListener;
import com.apple.eawt.Application;

/**
 * アプリケーションが foreground にいるか，background に回っているかの状態を保持する.
 * @author pns
 */
public class AppForeground {

    private static boolean appForeground = true;

    static {
        Application app = Application.getApplication();
        app.addAppEventListener(new AppForegroundListener(){
            @Override
            public void appRaisedToForeground(AppEvent.AppForegroundEvent afe) {
                appForeground = true;
            }

            @Override
            public void appMovedToBackground(AppEvent.AppForegroundEvent afe) {
                appForeground = false;
            }
        });
    }

    public static boolean isForeground() { return appForeground; }
}
