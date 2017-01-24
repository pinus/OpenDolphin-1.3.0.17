package open.dolphin.client;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import javax.swing.table.AbstractTableModel;
import open.dolphin.infomodel.SimpleDate;

/**
 * CalendarTableModel.
 * GregorianCalendar and HashMap based.
 * @author Kazushi Minagawa Digital Globe, Inc.
 * @author pns
 */
public class CalendarTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private static final String[] COLUMN_NAME = { "日", "月", "火", "水", "木", "金", "土" };

    // このカレンダーテーブルの年月
    private int year;
    private int month;

    // このカレンダーテーブルの左上隅の日
    private GregorianCalendar startDate;

    private final int numRows = 6; // 6週で固定
    private final int numCols = 7; // 7日で固定

    // SimpleDate を入れる HashMap
    private final HashMap<GregorianCalendar, SimpleDate> data = new HashMap<>();

    // Event のマークされた SimpleDate を登録する
    private Collection<SimpleDate> markDates;

    /**
     * CalendarTableModel を生成する.
     * @param year   カレンダの年
     * @param month　 カレンダの月
     */
    public CalendarTableModel(int year, int month) {
        init(year, month);
    }

    /**
     * 指定した year, month で初期化する.
     * @param y
     * @param m
     */
    private void init(int y, int m) {
        year = y;
        month = m;

        // 作成する月の最初の日  yyyyMM1
        GregorianCalendar gc = new GregorianCalendar(year, month, 1);

        // その月の１日の日付は週の何日目か 1=SUN 7=SAT
        int diff = gc.get(Calendar.DAY_OF_WEEK);

        // このカレンダーの左上の日まで戻して左上の日を登録
        startDate = (GregorianCalendar) gc.clone();
        startDate.add(Calendar.DAY_OF_MONTH, - diff +1);
    }

    /**
     * model を１週間進める.
     */
    public void nextWeek() {
        startDate.add(Calendar.DAY_OF_MONTH, 7);
        calibrateYearMonth();
    }

    /**
     * model を１週間戻す.
     */
    public void previousWeek() {
        startDate.add(Calendar.DAY_OF_MONTH, -7);
        calibrateYearMonth();
    }

    /**
     * model を１ヶ月進める.
     */
    public void nextMonth() {
        if (month == 11) { reset(year+1, 0); }
        else { reset(year, month+1); }
    }

    /**
     * model を１ヶ月戻す.
     */
    public void previousMonth() {
        if (month == 0) { reset(year-1, 11); }
        else { reset(year, month-1); }
    }

    /**
     * 今表示しているカレンダーがどの月か決める.
     * startDate の２週後の週の週末(20日後)を含む月がこのモデルの表す月と定義する.
     */
    private void calibrateYearMonth() {
        GregorianCalendar gc = (GregorianCalendar) startDate.clone();
        gc.add(Calendar.DAY_OF_MONTH, 20);
        year = gc.get(Calendar.YEAR);
        month = gc.get(Calendar.MONTH);

        fireTableDataChanged();
    }

    /**
     * model を y年 m月にリセットする.
     * @param y
     * @param m
     */
    public void reset(int y, int m) {
        init(y, m);
        fireTableDataChanged();
    }

    /**
     * 現在の model の年を返す.
     * @return
     */
    public int getYear() {
        return year;
    }

    /**
     * 現在の model の月を返す.
     * @return
     */
    public int getMonth() {
        return month;
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
        return numCols;
    }

    /**
     * 指定された row, col から SimpleDate を取り出す.
     * @param row
     * @param col
     * @return
     */
    @Override
    public Object getValueAt(int row, int col) {

        // Cell 番号を得る
        int cellNumber = row * numCols + col;

        // startDay から cellNumber だけ進める
        GregorianCalendar targetDay = (GregorianCalendar) startDate.clone();
        targetDay.add(Calendar.DAY_OF_MONTH, cellNumber);

        // 戻り値の SimpleDate
        SimpleDate ret = data.get(targetDay);

        // データがない場合は作る
        if (ret == null) {
            ret = new SimpleDate(targetDay);
            data.put(targetDay, ret);
        }

        return ret;
    }

    /**
     * 指定された row, col に SimpleDate を設定する.
     * SimpleDate の日付に設定することにしたので，row, col は使ってない.
     * @param value
     * @param row
     * @param col
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        SimpleDate d = (SimpleDate) value;
        GregorianCalendar gc = new GregorianCalendar(d.getYear(), d.getMonth(), d.getDay());
        data.put(gc, d);
    }

    /**
     * マークされた SimpleDate をモデルに追加する.
     * @param c
     */
    public void setMarkDates(Collection<SimpleDate> c) {
        markDates = c;

        // 既存のデータを消去して，SimpleDate を登録し直す
        clear();
        c.stream().filter(date -> year == date.getYear() && month == date.getMonth()).forEach(date -> {
            GregorianCalendar gc = new GregorianCalendar(date.getYear(), date.getMonth(), date.getDay());
            data.put(gc, date);
        });
        fireTableDataChanged();
    }

    /**
     * マークされた SimpleDate のコレクションを返す.
     * @return
     */
    public Collection<SimpleDate> getMarkDates() {
        return markDates;
    }

    /**
     * この月の SimpleDate を消す.
     */
    public void clear() {
        GregorianCalendar gc = new GregorianCalendar(year, month ,1);
        int days = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
        for(int i=0; i<days; i++) {
            // null で消去
            data.put(gc, null);
            gc.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    /**
     * 指定 row, col が今月から外れているかどうか.
     * @param row
     * @param col
     * @return
     */
    public boolean isOutOfMonth(int row, int col) {
        // startDate から cellNumber 進める
        int cellNumber = row * numCols + col;
        GregorianCalendar gc = (GregorianCalendar) startDate.clone();
        gc.add(Calendar.DAY_OF_MONTH, cellNumber);

        return year != gc.get(Calendar.YEAR) || month != gc.get(Calendar.MONTH);
    }
}
