package open.dolphin.inspector;

import open.dolphin.client.Dolphin;
import open.dolphin.event.BadgeListener;
import open.dolphin.infomodel.IInfoModel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Inspector が持つインターフェース.
 * Calendar の cell 幅が一定になるために DEFAULT_WIDTH は
 * 261 から 14ドット単位で調節する必要がある.
 *
 * @author pns
 */
public interface IInspector {

    int DEFAULT_WIDTH = Dolphin.forWin? 261 - 14 * 2 : 261;
    int DEFAULT_HEIGHT = Dolphin.forWin? 150 : 175;
    Color BACKGROUND = new Color(240, 240, 240);
    Color BORDER_COLOR = Color.LIGHT_GRAY;

    KeyStroke META_W = KeyStroke.getKeyStroke("meta W");
    KeyStroke META_Z = KeyStroke.getKeyStroke("meta Z");
    KeyStroke SHIFT_META_Z = KeyStroke.getKeyStroke("shift meta Z");
    KeyStroke BACK_SPACE = KeyStroke.getKeyStroke("BACK_SPACE");

    /**
     * Inspector を区別する ID としての名前.
     *
     * @return name
     */
    String getName();

    /**
     * GUI で使うタイトル. Name と違うものがある.
     *
     * @return title
     */
    String getTitle();

    /**
     * Inspector をのせた JPanel.
     *
     * @return JPanel
     */
    JPanel getPanel();

    /**
     * Border が必要な場合はここから引き出す.
     *
     * @return Border
     */
    default Border getBorder() {
        return new InspectorBorder(getTitle());
    }

    /**
     * 内容を更新する.
     */
    void update();

    /**
     * Tab のバッジを表示したい時に使う.
     *
     * @param listener BadgeListener
     * @param index    Badge を付ける tab のインデックス番号
     */
    default void addBadgeListener(BadgeListener listener, int index) { }

    /**
     * Windows かどうか.
     *
     * @return true if win
     */
    default boolean isWin() {
        return Dolphin.forWin;
    }

    /**
     * 今日の日付を ISO 型式で返す.
     *
     * @return ISO 型式の日付
     */
    default String today() {
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(IInfoModel.DATE_WITHOUT_TIME);
        return sdf.format(today);
    }
}
