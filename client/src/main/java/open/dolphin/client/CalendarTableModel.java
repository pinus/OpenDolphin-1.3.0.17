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

    // このカレンダーテーブルの１番左上隅の日（前月かもしれない）
    private GregorianCalendar startDate;

    private int numRows;
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

        // 最終日の週数がテーブルの行数になる
        int num = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
        gc.add(Calendar.DAY_OF_MONTH, num - 1);
        numRows = gc.get(Calendar.WEEK_OF_MONTH);
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
