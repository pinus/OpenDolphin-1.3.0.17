package open.dolphin.infomodel;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * SimpleDate.
 *
 * @author Kazushi Minagawa
 */
public class SimpleDate extends InfoModel implements Comparable<SimpleDate> {
    
    private int year;

    private int month; // 0~11

    private int day;

    private String eventCode;

    public SimpleDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public SimpleDate(int[] spec) {
        this(spec[0], spec[1], spec[2]);
    }

    public SimpleDate(GregorianCalendar gc) {
        this(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH));
    }

    public static SimpleDate mmlDateToSimpleDate(String mmlDate) {
        // mmlDate = YYYY-MM-DDThh:mm:ss
        int year = Integer.parseInt(mmlDate.substring(0, 4));
        int month = Integer.parseInt(mmlDate.substring(5, 7)) - 1; // for
        // Calendar
        int date = Integer.parseInt(mmlDate.substring(8, 10));
        return new SimpleDate(year, month, date);
    }

    public static String simpleDateToMmldate(SimpleDate sd) {
        return String.format("%d-%02d-%02d", sd.getYear(), sd.getMonth() + 1, sd.getDay());
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean equalDate(int year, int month, int day) {
        return (year == this.year && month == this.month && day == this.day);
    }

    @Override
    public String toString() {
        return String.valueOf(day);
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String c) {
        this.eventCode = c;
    }

    @Override
    public int compareTo(SimpleDate other) {

        if (other != null) {
            int oYear = other.getYear();
            int oMonth = other.getMonth();
            int oDay = other.getDay();

            if (year != oYear) {
                return year < oYear ? -1 : 1;

            } else if (month != oMonth) {
                return month < oMonth ? -1 : 1;

            } else if (day != oDay) {
                return day < oDay ? -1 : 1;

            } else {
                return 0;
            }
        }
        return -1;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof SimpleDate) && compareTo((SimpleDate) other) == 0;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.year;
        hash = 37 * hash + this.month;
        hash = 37 * hash + this.day;
        hash = 37 * hash + (this.eventCode != null ? this.eventCode.hashCode() : 0);
        return hash;
    }

    public int compareMonthDayTo(Object o) {

        if (o != null && o.getClass() == this.getClass()) {
            SimpleDate other = (SimpleDate) o;
            int oMonth = other.getMonth();
            int oDay = other.getDay();

            if (month != oMonth) {
                return month < oMonth ? -1 : 1;

            } else if (day != oDay) {
                return day < oDay ? -1 : 1;

            } else {
                return 0;
            }
        }
        return -1;
    }
}
