package open.dolphin.helper;

import java.awt.*;
import java.util.prefs.Preferences;

/**
 * PreferencesUtils.
 *
 * @author pns
 */
public class PreferencesUtils {

    public static void putRectangle(Preferences prefs, String key, Rectangle bounds) {
        prefs.putInt(key+"X", bounds.x);
        prefs.putInt(key+"Y", bounds.y);
        prefs.putInt(key+"Width", bounds.width);
        prefs.putInt(key+"Height", bounds.height);
    }

    public static Rectangle getRectangle(Preferences prefs, String key, Rectangle defaults) {
        int x = prefs.getInt(key+"X", defaults.x);
        int y = prefs.getInt(key+"Y", defaults.y);
        int width = prefs.getInt(key+"Width", defaults.width);
        int height = prefs.getInt(key+"Height", defaults.height);

        return new Rectangle(x, y, width, height);
    }
}
