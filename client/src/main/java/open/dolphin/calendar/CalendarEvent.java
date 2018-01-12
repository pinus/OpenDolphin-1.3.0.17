package open.dolphin.calendar;

import java.awt.Color;
import java.util.Arrays;

/**
 * CalendarEvent.
 * @author pns
 */
public enum CalendarEvent {
    TODAY("今日", new Color(0xffdead)), BIRTHDAY("誕生日", new Color(0xadd8e6)), PVT("受診日", new Color(255,192,203)),

    EXAM_APPO("再診", new Color(255,165,0)), IMAGE_APPO("画像検査", new Color(119,200,211)),
    TEST_APPO("検体検査", new Color(255, 69, 0)), MISC_APPO("その他", new Color(251,239,128)),

    medOrder("処方", Color.PINK), treatmentOrder("処置", Color.PINK), instractionChargeOrder("指導", Color.PINK),
    testOrder("ラボテスト", Color.PINK), physiologyOrder("生体検査", Color.PINK), radiologyOrder("放射線", Color.PINK), image("画像", Color.PINK)

    ;
    private final Color color;
    private final String title;

    private CalendarEvent(String t, Color c) {
        title = t;
        color = c;
    }

    public String code() {
        return name();
    }

    public Color color() {
        return color;
    }

    public String title() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }

    /**
     * CalendarEvent の色を返す.
     * @param code
     * @return
     */
    public static Color getColor(String code) {
        for (CalendarEvent event : CalendarEvent.values()) {
            if (event.name().equals(code)) { return event.color(); }
        }
        return null;
    }

    /**
     * CalendarEvent のタイトル文字列を返す.
     * @param code
     * @return
     */
    public static String getTitle(String code) {
        for (CalendarEvent event : CalendarEvent.values()) {
            if (event.name().equals(code)) { return event.title(); }
        }
        return code;
    }

    /**
     * CalendarEvent のコートから title を返す.
     * @param title
     * @return
     */
    public static String getCode(String title) {
        for (CalendarEvent event : CalendarEvent.values()) {
            if (event.title().equals(title)) { return event.name(); }
        }
        return title;
    }

    /**
     * code が予約関連のコードかどうかを返す.
     * @param code
     * @return
     */
    public static boolean isAppoint(String code) {
        return Arrays.asList(CalendarEvent.values()).stream()
                .filter(event -> event.name().contains("APPO"))
                .anyMatch(event -> event.code().equals(code));
    }

    /**
     * code がオーダー関連のコードかどうかを返す.
     * @param code
     * @return
     */
    public static boolean isModule(String code) {
        return Arrays.asList(CalendarEvent.values()).stream()
                .filter(event -> event.name().contains("Order"))
                .anyMatch(event -> event.code().equals(code));
    }
}
