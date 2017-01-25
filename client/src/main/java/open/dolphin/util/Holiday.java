package open.dolphin.util;

import java.util.HashMap;
import open.dolphin.infomodel.SimpleDate;

/**
 * 休日かどうか調べる.
 * @author pns
 */
public class Holiday {

    // yyyyMMdd 文字列をキー，休日名を value とする HashMap
    private static final HashMap<String,String> DATABASE = new HashMap<>();

    static {
        for (String[] data : HolidayDatabase.HOLIDAY_DATA) {
            DATABASE.put(data[0], data[1]);
        }
    }

    private static String createKey(SimpleDate sd) {
        return String.format("%d%02d%02d", sd.getYear(), sd.getMonth()+1, sd.getDay());
    }

    /**
     * 指定された SimpleDate が休日かどうかを返す.
     * @param sd
     * @return
     */
    public static boolean isHoliday(SimpleDate sd) {
        String key = createKey(sd);
        return DATABASE.containsKey(key);
    }

    /**
     * 指定された SimpleDate に休日名をセットする.
     * @param sd
     */
    public static void setTo(SimpleDate sd) {
        String key = createKey(sd);
        sd.setEventCode(DATABASE.get(key));
    }
}
