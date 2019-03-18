package open.dolphin.ui;

import open.dolphin.client.GUIConst;

import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Mac っぽいボーダー.
 *
 * @author pns
 */
public class PNSBorderFactory {

    public static Border createTitledBorder(String title) {
        return createTitledBorder(null, title, TitledBorder.LEFT, TitledBorder.TOP, null, Color.BLACK);
    }

    public static Border createTitledBorder(
            Border titleBorder,
            String title,
            int titleJustification, // TitledBorder.LEFT
            int titlePosition,      // TitledBorder.TOP
            Font titleFont,
            Color titleColor) {

        return new PNSTitledBorder(titleBorder, title, titleJustification, titlePosition, titleFont, titleColor);
    }

    public static Border createSelectedBorder() {
        return new PNSSelectedBorder();
    }

    public static Border createSelectedGrayBorder() {
        return new PNSSelectedGrayBorder();
    }

    public static Border createClearBorder() {
        return new PNSClearBorder();
    }

    public static Border createGroupBoxBorder(Insets borderInsets) {
        return new PNSImageBevelBorder(GUIConst.ICON_BORDER_GROUPBOX_18, new Insets(7, 7, 7, 7), borderInsets);
    }

    /**
     * 高さ 22 のタイトルバー風ボーダー.
     * KarteViewer などのタイトル部分.
     *
     * @param borderInsets
     * @return
     */
    public static Border createTitleBarBorder(Insets borderInsets) {
        return new PNSTexturedBorder(GUIConst.ICON_BORDER_TITLE_22, borderInsets);
    }

    /**
     * 高さ 16 のタイトルバー風ボーダー.
     * 未使用
     *
     * @param borderInsets
     * @return
     */
    public static Border createTitleBarBorder16(Insets borderInsets) {
        return new PNSTexturedBorder(GUIConst.ICON_BORDER_TITLE_16, borderInsets);
    }

    /**
     * 高さ 12 のタイトルバー風ボーダー.
     * 未使用
     *
     * @param borderInsets
     * @return
     */
    public static Border createTitleBarBorder12(Insets borderInsets) {
        return new PNSTexturedBorder(GUIConst.ICON_BORDER_TITLE_12, borderInsets);
    }

    /**
     * 高さ 38 のピンク色のタイトルバー風ボーダー.
     *
     * @param borderInsets
     * @return
     */

    public static Border createTitleBarBorderPink(Insets borderInsets) {
        return new PNSRoundedTextureBorder(GUIConst.ICON_BORDER_TITLE_PINK_38, borderInsets);
        /**
         * 高さ 38 の水色のタイトルバー風ボーダー.
         * @param borderInsets
         * @return
         */
    }

    public static Border createTitleBarBorderLightBlue(Insets borderInsets) {
        return new PNSRoundedTextureBorder(GUIConst.ICON_BORDER_TITLE_LIGHT_BLUE_38, borderInsets);
    }

    /**
     * 高さ 38 のグレーのタイトルバー風ボーダー.
     *
     * @param borderInsets
     * @return
     */
    public static Border createTitleBarBorderGray(Insets borderInsets) {
        return new PNSRoundedTextureBorder(GUIConst.ICON_BORDER_TITLE_38, borderInsets);
    }
}
