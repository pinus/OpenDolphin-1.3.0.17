package open.dolphin.ui;

import ch.randelshofer.quaqua.QuaquaBorderFactory;
import ch.randelshofer.quaqua.QuaquaIconFactory;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.border.Border;
import open.dolphin.client.GUIConst;

/**
 * Mac っぽいボーダー.
 * @author pns
 */
public class MyBorderFactory {
    public static Border createSelectedBorder() {
        return new MySelectedBorder();
    }

    public static Border createSelectedGrayBorder() {
        return new MySelectedGrayBorder();
    }

    public static Border createClearBorder() {
        return new MyClearBorder();
    }

    public static Border createTextFieldBorder() {
        return new MyTextFieldBorder();
    }

    public static Border createGroupBoxBorder(Insets borderInsets) {
       return QuaquaBorderFactory.create(
               QuaquaIconFactory.createImage("/ch/randelshofer/quaqua/images/GroupBox.png"),
               new Insets(7,7,7,7), // image insets この内部が引き延ばされる
               borderInsets,
               true,
               new Color(0x08000000,true),
               false);
    }

    public static Border createGroupBoxEmptyBorder(Insets borderInsets) {
       return QuaquaBorderFactory.create(
               QuaquaIconFactory.createImage("/ch/randelshofer/quaqua/images/GroupBox.empty.png"),
               new Insets(7,7,7,7), // image insets
               borderInsets,
               true,
               new Color(0x00000000,true),
               false);
    }

    // 高さは 22 で固定
    public static Border createTitleBorder(Insets borderInsets) {
       return new MyTitleBorder(GUIConst.ICON_BORDER_TITLE_22, borderInsets);
    }

    // 高さは 16 で固定
    public static Border createTitleBorder16(Insets borderInsets) {
       return new MyTitleBorder(GUIConst.ICON_BORDER_TITLE_16, borderInsets);
    }

    // 高さは 12 で固定
    public static Border createTitleBorder12(Insets borderInsets) {
       return new MyTitleBorder(GUIConst.ICON_BORDER_TITLE_12, borderInsets);
    }

    // 高さは 38 で固定
    public static Border createTitleBorderPink(Insets borderInsets) {
       return new MyRoundedTitleBorder(GUIConst.ICON_BORDER_TITLE_PINK_38, borderInsets);
    }
    public static Border createTitleBorderLightBlue(Insets borderInsets) {
       return new MyRoundedTitleBorder(GUIConst.ICON_BORDER_TITLE_LIGHT_BLUE_38, borderInsets);
    }
    public static Border createTitleBorderGray(Insets borderInsets) {
       return new MyRoundedTitleBorder(GUIConst.ICON_BORDER_TITLE_38, borderInsets);
    }

    /**
     * Image から BufferedImage に変換
     * @param src
     * @return
     */
    public static BufferedImage imageToBufferedImage(Image src) {
        int width = 0;
        int height = 0;

        if (src != null) {
            width = src.getWidth(null);
            height = src.getHeight(null);
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(src, 0, 0, null);
        g.dispose();

        return image;
    }
    /**
     * ImageIcon から BufferedImage に変換
     * @param src
     * @return
     */
    public static BufferedImage imageToBufferedImage(ImageIcon src) {
        return imageToBufferedImage(src.getImage());
    }
}
