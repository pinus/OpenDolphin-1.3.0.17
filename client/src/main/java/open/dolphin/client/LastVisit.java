package open.dolphin.client;

import open.dolphin.util.MMLDate;
import org.apache.log4j.Logger;

import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * LastVisit. Chart#getkarte().getPvtDateEntry() から最終受診日を調べる.
 * このメソッドは KarteEditor を開いて閉じると, あとは null を返すようになるので，
 * ChartImpl が開かれたときにインスタンス作って使い回しする.
 * <p>
 * 今日受診していたら今日の日付, 今日受診していない場合は History の最終受診日になる.
 * 今日受診していても，History の最終受診日を知りたい場合は InHistory を使う.
 *
 * @author pns
 */
public class LastVisit {

    private Chart context;
    // 最新受診日の GregorianCalendar
    private GregorianCalendar lastVisitGC = null;
    // 最終受診日の時間なし ISO_DATE 形式
    private String lastVisitIso = null;
    // 今日の受診を除く最終受診日の GregorianCalendar
    private GregorianCalendar lastVisitInHistoryGc = null;
    // 今日の受診を除く最終受診日の時間なし ISO_DATE 形式
    private String lastVisitInHistoryIso = null;
    // ロガー
    private Logger logger = Logger.getLogger(LastVisit.class);

    public LastVisit(Chart context) {
        this.context = context;

        // ISO_DATE_TIME 形式の受診歴リスト
        List<String> list = context.getKarte().getPvtDateEntry();
        if (list == null || list.isEmpty()) { return; }

        list.sort(Comparator.naturalOrder());
        String isoDateTime = list.get(list.size() - 1); // 最後の要素
        lastVisitIso = MMLDate.trimTime(isoDateTime);
        lastVisitGC = MMLDate.getCalendar(isoDateTime);

        // lastVisit が今日なら今日を取り除く
        String today = MMLDate.getDate();
        if (today.equals(lastVisitIso)) { list.remove(list.size() - 1); }
        if (list.isEmpty()) { return; }

        // 今日の受診より１回前の受診
        isoDateTime = list.get(list.size() - 1);
        lastVisitInHistoryIso = MMLDate.trimTime(isoDateTime);
        lastVisitInHistoryGc = MMLDate.getCalendar(isoDateTime);
    }

    /**
     * 今日を除いた最終受診日（int 配列形式 int[0]=year, int[1]=month(0～11), int[2]=day）
     *
     * @return { year, month, day }
     */
    public int[] getLastVisitInHistoryYmd() {
        return MMLDate.getCalendarYMD(lastVisitInHistoryIso);
    }

    /**
     * 最終受診日を時間なしの mmlDate 形式（2008-02-01）で返す
     *
     * @return ISO_DATE
     */
    public String getLastVisit() {
        return lastVisitIso;
    }

    /**
     * 最終受診日を，int 配列 int[0]=year, int[1]=month, int[2]=day で返す (month は 0-11)
     *
     * @return { year, month, day }
     */
    public int[] getLastVisitYmd() {
        return MMLDate.getCalendarYMD(lastVisitIso);
    }

    /**
     * 最終受診日を GlegorianCalendar で返す
     *
     * @return GregorianCalendar
     */
    public GregorianCalendar getLastVisitGc() {
        return lastVisitGC;
    }
}
