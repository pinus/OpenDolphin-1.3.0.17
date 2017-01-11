package open.dolphin.inspector;

import javax.swing.JPanel;
import javax.swing.border.Border;
import open.dolphin.client.ClientContext;
import open.dolphin.event.BadgeListener;
import open.dolphin.ui.PNSBorderFactory;

/**
 *
 * @author pns
 */
public interface IInspector {

    public static final int DEFAULT_WIDTH = ClientContext.isMac()? 280 : 260;
    public static final int DEFAULT_HEIGHT = ClientContext.isMac()? 175 : 178;

    /**
     * Inspector を区別する ID としての名前.
     * @return
     */
    public String getName();

    /**
     * GUI で使うタイトル. Name と違うものがある.
     * @return
     */
    public String getTitle();

    /**
     * Inspector をのせた JPanel.
     * @return
     */
    public JPanel getPanel();

    /**
     * Border が必要な場合はここから引き出す.
     * @return
     */
    default public Border getBorder() { return PNSBorderFactory.createTitledBorder(getTitle()); }

    /**
     * 内容を更新する.
     */
    public void update();

    /**
     * Tab のバッジを表示したい時に使う.
     * @param listener BadgeListener
     * @param index Badge を付ける tab のインデックス番号
     */
    default public void addBadgeListener(BadgeListener listener, int index) {}
}
