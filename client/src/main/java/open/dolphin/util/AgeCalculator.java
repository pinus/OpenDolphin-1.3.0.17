package open.dolphin.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

/**
 * Utility to calculate Gregorian Time.
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class AgeCalculator {

    public AgeCalculator() {}

    /**
     * Returns age from MML Date format.
     * @param mmlBirthday YYYY-MM-DD
     * @return age as String
     */
    public static String getAge(String mmlBirthday) {
    	
        GregorianCalendar gc1 = getCalendar(mmlBirthday);
        GregorianCalendar gc2 = new GregorianCalendar();  // Today
        int years = 0;

        gc1.clear(Calendar.MILLISECOND);
        gc1.clear(Calendar.SECOND);
        gc1.clear(Calendar.MINUTE);
        gc1.clear(Calendar.HOUR_OF_DAY);

        gc2.clear(Calendar.MILLISECOND);
        gc2.clear(Calendar.SECOND);
        gc2.clear(Calendar.MINUTE);
        gc2.clear(Calendar.HOUR_OF_DAY);

        while ( gc1.before(gc2) ) {
            gc1.add(Calendar.YEAR, 1);
            years++;
        }
        years--;

        int month = 12;

        while ( gc1.after(gc2)) {
            gc1.add(Calendar.MONTH, -1);
            month--;
        }

        return String.format("%s.%s", years, month);
    }

    /**
     * Returns GregorianCalendar from MML Date format.
     * @params mmlDate (YYYY-MM-DD)
     * @return GregorianCalendar of birthday
     */
    private static GregorianCalendar getCalendar(String mmlDate) {

        // Trim time if contains
        int index = mmlDate.indexOf('T');
        if ( index != -1 ) {
            mmlDate = mmlDate.substring(0, index);
        }
        StringTokenizer st = new StringTokenizer(mmlDate, "-");
        String yearSt = st.nextToken();
        String monthSt = st.nextToken();
        if (monthSt.startsWith("0")) {
            monthSt = monthSt.substring(1);
        }
        String daySt = st.nextToken();
        if (daySt.startsWith("0")) {
            daySt = daySt.substring(1);
        }
        int year = Integer.parseInt(yearSt);
        int month = Integer.parseInt(monthSt);
        month--;
        int day = Integer.parseInt(daySt);

        return new GregorianCalendar(year, month, day);
    }
}
