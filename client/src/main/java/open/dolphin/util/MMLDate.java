package open.dolphin.util;

import open.dolphin.infomodel.ModelUtils;
import java.util.*;
import java.text.*;
import open.dolphin.infomodel.IInfoModel;

/**
 * Utility class to handle MML Date format.
 * @author  Kazushi Minagawa, Digital Globe, Inc. modified by pns
 */
public final class MMLDate extends Object {

//  private static final String MML_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    // mmlDate形式
    private static final String MML_DATETIME_PATTERN = IInfoModel.ISO_8601_DATE_FORMAT;
//  private static final String MML_DATE_PATTERN = "yyyy-MM-dd";
    // mmlDate 形式の日付部分のみ
    private static final String MML_DATE_PATTERN = IInfoModel.DATE_WITHOUT_TIME;
    // mmlDate 形式の時間部分のみ
    private static final String MML_TIME_PATTERN = "HH:mm:ss";

    public MMLDate() {}

    /**
     * GregorianCalendar の日付を pattern 形式で文字列にして返す
     * @param gc
     * @param pattern
     * @return
     */
    public static String getDateTime(GregorianCalendar gc, String pattern) {
        SimpleDateFormat f = new SimpleDateFormat(pattern);
        return f.format(gc.getTime());
    }

    /**
     * 今日を pattern で指定した形式で返す
     * @param pattern
     * @return
     */
    public static String getDateTime(String pattern) {
        return getDateTime(new GregorianCalendar(), pattern);
    }

    /**
     * GregorianCalendar の日付を mmlDate 形式で返す
     * @param gc
     * @return
     */
    public static String getDateTime(GregorianCalendar gc) {
        return getDateTime(gc, MML_DATETIME_PATTERN);
    }

    /**
     * 今日を時間付きの mmlDate 形式で返す
     * @return
     */
    public static String getDateTime() {
        return getDateTime(new GregorianCalendar());
    }

    /**
     * GregorianCalendar の日付を，時間なしの mmlDate 形式で返す
     * @param gc
     * @return
     */
    public static String getDate(GregorianCalendar gc) {
        return getDateTime(gc, MML_DATE_PATTERN);
    }

    /**
     * 今日を時間なしの mmlDate 形式で返す
     * @return
     */
    public static String getDate() {
        return getDate(new GregorianCalendar());
    }

    /**
     * GregorianCalendar の日付を，日付なしの mmlDate 形式で返す
     * @param gc
     * @return
     */
    public static String getTime(GregorianCalendar gc) {
        return getDateTime(gc, MML_TIME_PATTERN);
    }

    /**
     * 今日の時間だけを mmlDate 形式で返す
     * @return
     */
    public static String getTime() {
        return getTime(new  GregorianCalendar());
    }

    /**
     * 今日から n 日後の日付を，時間なしの mmlDate 形式で返す
     * @param n
     * @return
     */
    public static String getDayFromToday(int n) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DAY_OF_MONTH, n);
        return getDate(gc);
    }

    /**
     * 今日から n ヶ月後の日付を，時間なしの mmlDate 形式で返す
     * @param n
     * @return
     */
    public static String getMonthFromToday(int n) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.MONTH, n);
        return getDate(gc);
    }

    /**
     * 今日から n 年後の日付を，時間なしの mmlDate 形式で返す
     * @param n
     * @return
     */
    public static String getYearFromToday(int n) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.YEAR, n);
        return getDate(gc);
    }

    /**
     * 時間なしの mmlDate の日付を {年，月，日} の整数配列にして返す
     * @param mmlDate
     * @return
     */
    public static int[] getCalendarYMD(String mmlDate) {
        if (mmlDate == null) return null;

        int i=0;
        int[] ret = new int[3];
        for (String s : mmlDate.split("-")) ret[i++] = Integer.valueOf(s);
        ret[1]--; // 月は 0-11

        return ret;
    }

    /**
     * MML 形式から時間を取り除いて日付だけ取り出す
     * @param mmlDate
     * @return
     */
    public static String trimTime(String mmlDate) {
        return ModelUtils.trimTime(mmlDate);
    }

    /**
     * MML 形式から日付を取り除いて時間だけ取り出す
     * @param mmlDate
     * @return
     */
    public static String trimDate(String mmlDate) {
        return ModelUtils.trimDate(mmlDate);
    }

    /**
     * mmlDate 形式から GregorianCalendar を作る
     * @param mmlDate
     * @return
     */
    public static GregorianCalendar getCalendar(String mmlDate) {
        return ModelUtils.getCalendar(mmlDate);
    }
}
