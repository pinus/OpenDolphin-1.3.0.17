package open.dolphin.helper;

import java.awt.MouseInfo;
import java.awt.Point;
import javax.swing.ToolTipManager;

/**
 * MouseHelper.
 *
 * @author pns
 */
public class MouseHelper {
    // ダブルクリック後の Protection Time：この時間の間にドラッグが始まったらダブルクリックはキャンセルする
    public static final int PROTECTION_TIME = 50;

    /**
     * 50ms 以内にマウスが動いたかどうか.
     *
     * @return 動いたら true
     */
    public static boolean mouseMoved() {
        Point p1 = MouseInfo.getPointerInfo().getLocation();
        try {
            Thread.sleep(PROTECTION_TIME);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        Point p2 = MouseInfo.getPointerInfo().getLocation();

        return p1.x != p2.x || p1.y != p2.y;
    }

    /**
     * 出ている ToolTipWindow を消す.
     */
    public static void hideToolTipWindow() {
        ToolTipManager manager = ToolTipManager.sharedInstance();
        if (manager.isEnabled()) {
            manager.setEnabled(false);
            manager.setEnabled(true);
        }
    }
}
