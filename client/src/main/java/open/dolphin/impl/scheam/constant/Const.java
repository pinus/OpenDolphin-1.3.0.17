package open.dolphin.impl.scheam.constant;

import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * 定数
 * @author pns
 */
public class Const {
    public static final String ROLE = "参考図";
    public static final String TTITLE = "参考画像";

    public static final Color PNS_BLACK = Color.rgb(65, 65, 65);
    public static final Color PNS_GLAY = Color.rgb(140,140,140);
    public static final Color PNS_LIGHT_GLAY = Color.rgb(200,200,200);
    public static final Color PNS_WHITE = Color.rgb(240,240,240);

    /** 消しゴムの大きさ = lineWidth * WIDTH_FACTOR */
    public static final double ERASER_WIDTH_FACTOR = 5;

    /**
     * Images
     */
    /** PnsStage のタイトルバーの　x - + ボタン */
    public static final ImageView IMAGE_BAR_BUTTONS = new ImageView(new Image("schemaeditor/closebox.png"));
    public static final ImageView IMAGE_BAR_BUTTONS_HOVER = new ImageView(new Image("schemaeditor/closebox-hover.png"));
    public static final ImageView IMAGE_BAR_BUTTONS_DISABLE = new ImageView(new Image("schemaeditor/closebox-disable.png"));
    /** PnsStage resize のカーソル画像 */
    private static final Image IMAGE_NWSE = new Image("schemaeditor/resize-nwse.png");
    private static final Image IMAGE_NESW = new Image("schemaeditor/resize-nesw.png");
    private static final Image IMAGE_EW = new Image("schemaeditor/resize-ew.png");
    private static final Image IMAGE_NS = new Image("schemaeditor/resize-ns.png");
    public static final ImageCursor IMAGE_CURSOR_NWSE = new ImageCursor(IMAGE_NWSE, IMAGE_NWSE.getWidth()/2, IMAGE_NWSE.getHeight()/2);
    public static final ImageCursor IMAGE_CURSOR_NESW = new ImageCursor(IMAGE_NESW, IMAGE_NESW.getWidth()/2, IMAGE_NESW.getHeight()/2);
    public static final ImageCursor IMAGE_CURSOR_EW = new ImageCursor(IMAGE_EW, IMAGE_EW.getWidth()/2, IMAGE_EW.getHeight()/2);
    public static final ImageCursor IMAGE_CURSOR_NS = new ImageCursor(IMAGE_NS, IMAGE_NS.getWidth()/2, IMAGE_NS.getHeight()/2);
}
