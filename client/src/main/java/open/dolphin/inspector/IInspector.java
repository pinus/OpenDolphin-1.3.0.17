package open.dolphin.inspector;

import open.dolphin.client.ClientContext;
import open.dolphin.event.BadgeListener;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Inspector が持つインターフェース.
 * Calendar の cell 幅が一定になるために DEFAULT_WIDTH は
 * 261 から 14ドット単位で調節する必要がある.
 * @author pns
 */
public interface IInspector {

    public static final boolean IS_WIN = ClientContext.isWin();
    public static final int DEFAULT_WIDTH = IS_WIN? 261-14*2 : 261;
    public static final int DEFAULT_HEIGHT = IS_WIN? 150 : 175;
    public static final Color BACKGROUND = new Color(240,240,240);
    public static final Color BORDER_COLOR = Color.LIGHT_GRAY;

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
    default public Border getBorder() { return new InspectorBorder(getTitle()); }

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

    default public boolean isWin() { return IS_WIN; }
}
