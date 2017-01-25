package open.dolphin.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
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
     * EventCode に休日情報を入れた SimpleDate を作る.
     * @param gc
     * @return
     */
    public static SimpleDate createSimpleDate(GregorianCalendar gc) {
        int year = gc.get(Calendar.YEAR);
        int month = gc.get(Calendar.MONTH);
        int day = gc.get(Calendar.DAY_OF_MONTH);
        return createSimpleDate(year, month, day);
    }

    /**
     * EventCode に休日情報を入れた SimpleDate を作る.
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static SimpleDate createSimpleDate(int year, int month, int day) {
        SimpleDate sd = new SimpleDate(year, month, day);
        String key = createKey(sd);
        sd.setEventCode(DATABASE.get(key));
        return sd;
    }
}
