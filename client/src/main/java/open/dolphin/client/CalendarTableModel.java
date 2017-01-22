package open.dolphin.client;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import javax.swing.table.AbstractTableModel;
import open.dolphin.infomodel.SimpleDate;

/**
 * CalendarTableModel.
 *
 * @author Kazushi Minagawa Digital Globe, Inc.
 * @author pns
 */
public class CalendarTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private static final String[] COLUMN_NAME = { "日", "月", "火", "水", "木", "金", "土" };

    private final int year;
    private final int month;

    // このカレンダーの最初の日
    private final int startDay;

    // この月の開始日，終了日の対応する Cell 番号 (0から始まる)
    private int firstCell;
    private int lastCell;

    private final int numCols = COLUMN_NAME.length;
    private final int numRows;
    private final int numDaysOfMonth;

    // この月の SimpleDate を入れる
    private SimpleDate[] data;

    // Event のマークされた SimpleDate を登録する
    private Collection<SimpleDate> markDates;

    /**
     * CalendarTableModel を生成する.
     * @param year   カレンダの年
     * @param month　 カレンダの月
     */
    public CalendarTableModel(int year, int month) {

        this.year = year;
        this.month = month;

        // 作成する月の最初の日  yyyyMM1
        GregorianCalendar gc = new GregorianCalendar(year, month, 1);

        // 最初の日は週の何日目か 1=SUN 7=SAT
        firstCell = gc.get(Calendar.DAY_OF_WEEK);
        firstCell--;  // TableModel の Cell 番号へ変換する

        // この月の日数を得る
        numDaysOfMonth = gc.getActualMaximum(Calendar.DAY_OF_MONTH);

        // gc をその月の最後の日まで進める　1日 + （日数-1）
        gc.add(Calendar.DAY_OF_MONTH, numDaysOfMonth - 1);

        // 最後の日はその月の何週目か
        numRows = gc.get(Calendar.WEEK_OF_MONTH);

        // それは週の何日目か
        lastCell = gc.get(Calendar.DAY_OF_WEEK);
        lastCell--;

        // １次元のセル番号へ変換する
        lastCell += (numRows-1) * numCols;

        // このカレンダの表示開始日を求める
        GregorianCalendar startDate = new GregorianCalendar(year, month, 1);
        startDate.add(Calendar.DAY_OF_MONTH, - firstCell);

        // startDay はたいてい前の月になる
        startDay = startDate.get(Calendar.DAY_OF_MONTH);

        // データ配列作成
        data = new SimpleDate[numRows * 7];
    }

    @Override
    public String getColumnName(int col) {
        return COLUMN_NAME[col];
    }

    @Override
    public int getRowCount() {
        return numRows;
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAME.length;
    }

    @Override
    public Object getValueAt(int row, int col) {

        // Cell 番号を得る
        int cellNumber = row * numCols + col;

        // データがない場合は作る
        if (data[cellNumber] == null) {
            if (cellNumber < firstCell) {
                // 先月
                data[cellNumber] = month == 0?
                        new SimpleDate(year-1, 11, startDay + cellNumber):
                        new SimpleDate(year, month-1, startDay + cellNumber);

            } else if (cellNumber > lastCell) {
                // 来月
                data[cellNumber] = month == 11?
                        new SimpleDate(year+1, 0, cellNumber - lastCell):
                        new SimpleDate(year, month+1, cellNumber - lastCell);

            } else {
                // 当月
                data[cellNumber] = new SimpleDate(year, month, cellNumber - firstCell + 1);
            }
        }

        return data[cellNumber];
    }

    @Override
    public void setValueAt(Object value, int row, int col) {

        int cellNumber = row * numCols + col;
        data[cellNumber] = (SimpleDate) value;
    }

    public void setMarkDates(Collection<SimpleDate> c) {
        markDates = c;

        // 既存のデータを消去して，SimpleDate を登録し直す
        clear();
        c.stream().filter(date -> year == date.getYear() && month == date.getMonth()).forEach(date -> {
            int day = date.getDay();
            int cellNumber = firstCell + (day-1);
            int row = cellNumber / numCols;
            int col = cellNumber % numCols;
            setValueAt(date, row, col);
        });
        fireTableDataChanged();
    }

    public Collection<SimpleDate> getMarkDates() {
        return markDates;
    }

    public void clear() {
        data = new SimpleDate[numRows * 7];
    }

    public boolean isOutOfMonth(int row, int col) {
        int cellNumber = row * numCols + col;
        return cellNumber < firstCell || cellNumber > lastCell;
    }

    public SimpleDate getFirstDate() {
        return new SimpleDate(year, month, 1);
    }

    public SimpleDate getLastDate() {
        return new SimpleDate(year, month, numDaysOfMonth);
    }
}
