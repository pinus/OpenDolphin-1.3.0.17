package open.dolphin.calendar;

import java.awt.Color;

/**
 * CalendarEvent.
 * @author pns
 */
public enum CalendarEvent {
    TODAY("今日", new Color(255,255,0)), BIRTHDAY("誕生日", new Color(128,255,255)), PVT("受診日", new Color(255,192,203)),
    EXAM_APPO("検査予約", new Color(255,165,0)), RP("処方", new Color(255,140,0)), TREATMENT("処置", new Color(255,140,0)),
    TEST("検査", new Color(255,69,0)), IMAGE("画像", new Color(119,200,211)), MISC("その他", new Color(251,239,128))
    ;
    private final Color color;
    private final String title;

    private CalendarEvent(String t, Color c) {
        title = t;
        color = c;
    }

    public Color color() {
        return color;
    }

    public String title() {
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
}
