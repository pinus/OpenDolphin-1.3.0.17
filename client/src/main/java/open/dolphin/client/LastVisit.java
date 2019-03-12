package open.dolphin.client;

import open.dolphin.util.MMLDate;
import org.apache.log4j.Logger;

import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 最終受診日を調べる: getEntryCollection("visit") なんていいものがあるのを知って大幅に簡略化した
 * ただ，これは KarteEditor を開いて閉じると，あとは null を返すようになる
 * なので，ChartImpl が開かれたときにインスタンス作って，後は使い回ししないとダメ
 * <p>
 * 今日受診していたら今日の日付，今日受診していない場合は History の最終受診日
 * 今日受診していても，History の最終受診日を知りたい場合は InHistory を使う
 *
 * @author pns
 */
public class LastVisit {

    private Logger logger;
    private Chart context;
    // 最新受診日の GregorianCalendar
    private GregorianCalendar lastVisitGC = null;
    // 最終受診日の時間なし mmlDate 形式
    private String lastVisitMML = null;
    // 今日の受診を除く最終受診日の GregorianCalendar
    private GregorianCalendar lastVisitInHistoryGC = null;
    // 今日の受診を除く最終受診日の時間なし mmlDate 形式 */
    private String lastVisitInHistoryMML = null;

    public LastVisit(Chart context) {
        this.context = context;
        logger = ClientContext.getBootLogger();

        // 時間付き mmlDate 形式の受診歴リスト
        //List<String> list = context.getKarte().getEntryCollection("visit");
        List<String> list = context.getKarte().getPvtDateEntry();
        //System.out.println("LastVisit list=" + list);
        if (list == null || list.isEmpty()) return;

        list.sort(Comparator.naturalOrder());
        String mmlDateTime = list.get(list.size() - 1);
        lastVisitMML = MMLDate.trimTime(mmlDateTime);
        lastVisitGC = MMLDate.getCalendar(mmlDateTime);

        // lastVisit が今日かどうか
        String today = MMLDate.getDate();
        if (today.equals(lastVisitMML)) list.remove(list.size() - 1);

        if (list.isEmpty()) return;
        // 今日の受診より１回前の受診
        mmlDateTime = list.get(list.size() - 1);
        lastVisitInHistoryMML = MMLDate.trimTime(mmlDateTime);
        lastVisitInHistoryGC = MMLDate.getCalendar(mmlDateTime);
    }

    /**
     * 今日を除いた最終受診日（時間なしの mmlDate 形式：2008-02-01）
     *
     * @return ISO_DATE
     */
    public String getLastVisitInHistory() {
        return MMLDate.trimTime(lastVisitInHistoryMML);
    }

    /**
     * 今日を除いた最終受診日（int 配列形式 int[0]=year, int[1]=month(0～11), int[2]=day）
     *
     * @return { year, month, day }
     */
    public int[] getLastVisitInHistoryYmd() {
        return MMLDate.getCalendarYMD(lastVisitInHistoryMML);
    }

    /**
     * 今日を除いた最終受診日（GlegorianCaledar 形式）
     *
     * @return DocumentHistory のテーブル上の最終受診日（GlegorianCaledar 形式）
     */
    public GregorianCalendar getLastVisitInHistoryGc() {
        return lastVisitInHistoryGC;
    }

    /**
     * 最終受診日を時間なしの mmlDate 形式（2008-02-01）で返す
     *
     * @return ISO_DATE
     */
    public String getLastVisit() {
        return lastVisitMML;
    }

    /**
     * 最終受診日を，int 配列 int[0]=year, int[1]=month, int[2]=day で返す (month は 0-11)
     *
     * @return { year, month, day }
     */
    public int[] getLastVisitYmd() {
        return MMLDate.getCalendarYMD(lastVisitMML);
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
