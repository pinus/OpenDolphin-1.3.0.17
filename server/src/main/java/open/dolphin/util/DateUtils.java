package open.dolphin.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Java 8 Date/Time API.
 *
 * @author pns
 */
public class DateUtils {
    private static String ISO_DATE = "yyyy-MM-dd";
    private static String ISO_TIME = "HH:mm:ss";
    private static String ISO_DATE_TIME = ISO_DATE + "'T'" + ISO_TIME;

    /**
     * ISO_DATE または ISO_DATE_TIME 型式から LocalDateTime を作る.
     *
     * @param isoDateTime ISO_DATE (1975-01-01) or ISO_DATE_TIME (1975-01-01T12:23:34)
     * @return parsed Date
     */
    public static LocalDateTime toLocalDateTime(String isoDateTime) {
        String target = isoDateTime.contains("T") ? isoDateTime : isoDateTime + "T00:00:00";
        return LocalDateTime.parse(target, DateTimeFormatter.ofPattern(ISO_DATE_TIME));
    }

    /**
     * LocalDateTime から ISO_DATE 形式を作る.
     *
     * @param localDateTime LocalDateTime
     * @return 1975-01-01
     */
    public static String toIsoDate(LocalDateTime localDateTime) {
        return localDateTimeToFormattedString(localDateTime, ISO_DATE);
    }

    /**
     * LocalDateTime から ISO_DATE_TIME 形式を作る.
     *
     * @param localDateTime LocalDateTime
     * @return 1975-01-01T12:23:34
     */
    public static String toIsoDateTime(LocalDateTime localDateTime) {
        return localDateTimeToFormattedString(localDateTime, ISO_DATE_TIME);
    }

    /**
     * LocalDateTime から format で指定した形式の日付文字列を作る.
     *
     * @param localDateTime LocalDateTime
     * @param format        String for DateTimeFormatter.ofPattern
     * @return formatted String
     */
    public static String localDateTimeToFormattedString(LocalDateTime localDateTime, String format) {
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 今日を format で指定した形式の日付文字列で返す.
     *
     * @param format String for DateTimeFormatter.ofPattern
     * @return formatted String
     */
    public static String todayToFormattedString(String format) {
        return localDateTimeToFormattedString(LocalDateTime.now(), format);
    }

    /**
     * 今日を ISO_DATE_TIME 型式の文字列で返す.
     *
     * @return formatted String
     */
    public static String todayToIsoDateTime() {
        return todayToFormattedString(ISO_DATE_TIME);
    }

    /**
     * 今日を ISO_DATE 型式の文字列で返す.
     *
     * @return formatted String
     */
    public static String todayToIsoDate() {
        return todayToFormattedString(ISO_DATE);
    }

    /**
     * 現在時間を ISO_TIME 型式の文字列で返す.
     *
     * @return formatted String
     */
    public static String todayToIsoTime() {
        return todayToFormattedString(ISO_TIME);
    }

    /**
     * 今日から days 日後の日付を ISO_DATE 型式で返す.
     *
     * @param  days 日数
     * @return ISO_DATE
     */
    public static String getIsoDateDaysAhead(int days) {
        LocalDateTime ldt = LocalDateTime.now().plusDays(days);
        return localDateTimeToFormattedString(ldt, ISO_DATE);
    }

    /**
     * 今日から months ヶ月後の日付を ISO_DATE 型式で返す.
     *
     * @param  months 月数
     * @return ISO_DATE
     */
    public static String getIsoDateMonthsAhead(int months) {
        LocalDateTime ldt = LocalDateTime.now().plusMonths(months);
        return localDateTimeToFormattedString(ldt, ISO_DATE);
    }

    /**
     * 今日から years 年後の日付を ISO_DATE 型式で返す.
     *
     * @param  years 年数
     * @return ISO_DATE
     */
    public static String getIsoDateYearsAhead(int years) {
        LocalDateTime ldt = LocalDateTime.now().plusYears(years);
        return localDateTimeToFormattedString(ldt, ISO_DATE);
    }

    /**
     * ISO_DATE 型式の日付を {年，月，日} の整数配列にして返す.
     *
     * @param isoDate
     * @return
     */
    public static int[] toDateArray(String isoDate) {
        if (isoDate == null) { return null; }

        int i = 0;
        int[] ret = new int[3];
        for (String s : isoDate.split("-")) {
            ret[i++] = Integer.valueOf(s);
        }
        ret[1]--; // 月は 0-11

        return ret;
    }

    /**
     * ISO_DATE_TIME 形式から時間を取り除いて ISO_DATE を返す.
     *
     * @param isoDateTime
     * @return ISO_DATE
     */
    public static String trimTime(String isoDateTime) {
        if (isoDateTime == null) { return null; }

        int index = isoDateTime.indexOf('T');
        if (index > -1) {
            return isoDateTime.substring(0, index);
        } else {
            return isoDateTime;
        }
    }

    /**
     * ISO_DATE_TIME 形式から日付を取り除いて ISO_TIME を返す.
     *
     * @param isoDateTime
     * @return
     */
    public static String trimDate(String isoDateTime) {
        if (isoDateTime == null) { return null; }

        int index = isoDateTime.indexOf('T');
        if (index > -1) {
            return isoDateTime.substring(index + 1, index + 6); // THH:mm:ss -> HH:mm:ss
        } else {
            return isoDateTime;
        }
    }

    public static void main(String[] arg) {
        System.out.println(toLocalDateTime("1975-01-01"));
        System.out.println(toLocalDateTime("1975-01-01T12:23:34"));
        System.out.println(toIsoDate(LocalDateTime.now()));
        System.out.println(toIsoDateTime(LocalDateTime.now()));
        System.out.println(localDateTimeToFormattedString(LocalDateTime.now(), ISO_DATE));
        System.out.println(todayToIsoDate());
        System.out.println(todayToIsoTime());
        System.out.println(todayToIsoDateTime());
        System.out.println(getIsoDateDaysAhead(365));
        System.out.println(getIsoDateMonthsAhead(12));
        System.out.println(getIsoDateYearsAhead(1));
    }
}
