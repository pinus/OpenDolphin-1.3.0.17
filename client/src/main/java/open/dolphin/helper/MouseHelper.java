package open.dolphin.helper;

import java.awt.MouseInfo;
import java.awt.Point;

/**
 *
 * @author pns
 */
public class MouseHelper {
    // ダブルクリック後の Protection Time：この時間の間にドラッグが始まったらダブルクリックはキャンセルする
    public static final int PROTECTION_TIME = 50;

    public static boolean mouseMoved() {
        Point p1 = MouseInfo.getPointerInfo().getLocation();
        try{ Thread.sleep(PROTECTION_TIME); } catch (Exception ex) { ex.printStackTrace(System.err);}
        Point p2 = MouseInfo.getPointerInfo().getLocation();

        return p1.x != p2.x || p1.y != p2.y;
    }
}
