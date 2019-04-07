package open.dolphin.client;

import open.dolphin.infomodel.DocInfoModel;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * LastVisit. {@code Chart#getkarte().getPvtDateEntry()} から最終受診日を調べる.
 * このメソッドは KarteEditor を開いて閉じると, あとは null を返すようになるので，
 * ChartImpl が開かれたときにインスタンス作って使い回しする.
 * <p>
 * 今日受診していたら今日の日付, 今日受診していない場合は History の最終受診日になる.
 * 今日受診していても，History の最終受診日を知りたい場合は InHistory を使う.
 *
 * @author pns
 */
public class LastVisit {

    // Chart
    private Chart context;
    // 最終受診日
    private LocalDate lastVisit = null;
    // DocumentHistory の最終受診日
    private LocalDate lastVisitInHistory = null;
    // ロガー
    private Logger logger = Logger.getLogger(LastVisit.class);

    public LastVisit(Chart context) {
        this.context = context;
    }

    /**
     * DocumentHistory inspector の updateHistory から呼んでもらって update する.
     *
     * @param docInfoModels List of DocInfoModel
     */
    public void update(List<DocInfoModel> docInfoModels) {
        String pvtDate = context.getPatientVisit().getPvtDate();
        logger.debug("pvt date = " + pvtDate);

        // ISO_DATE 型式のリスト
        List<String> docList = docInfoModels.stream()
                .map(DocInfoModel::getFirstConfirmDateTrimTime)
                .sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        logger.debug("doc list = " + docList);

        lastVisitInHistory = docList.isEmpty() ? null
                : LocalDate.parse(docList.get(0), DateTimeFormatter.ISO_DATE);

        lastVisit = Objects.isNull(pvtDate) ? lastVisitInHistory
                : LocalDate.parse(pvtDate, DateTimeFormatter.ISO_DATE_TIME);
    }

    /**
     * 最終受診日を返す.
     *
     * @return last visit in LocalDateTime
     */
    @Nullable
    public LocalDate getLastVisit() {
        return lastVisit;
    }

    /**
     * 今日を除いた最終受診日を返す.
     *
     * @return last visit in LocalDateTime
     */
    @Nullable
    public LocalDate getLastVisitInHistory() {
        return lastVisitInHistory;
    }

    /**
     * 最終受診日を，int 配列 int[0]=year, int[1]=month, int[2]=day で返す (month は 0-11)
     *
     * @return { year, month, day }
     */
    @Nullable
    public int[] getLastVisitYmd() {
        return Objects.isNull(lastVisit) ? null :
                new int[]{lastVisit.getYear(),
                        lastVisit.getMonthValue() - 1,
                        lastVisit.getDayOfMonth()};
    }

    /**
     * 今日を除いた最終受診日（int 配列形式 int[0]=year, int[1]=month(0～11), int[2]=day）
     *
     * @return { year, month, day }
     */
    @Nullable
    public int[] getLastVisitInHistoryYmd() {
        return Objects.isNull(lastVisitInHistory) ? null :
                new int[]{lastVisitInHistory.getYear(),
                        lastVisitInHistory.getMonthValue() - 1,
                        lastVisitInHistory.getDayOfMonth()};
    }

    /**
     * 最終受診日を GlegorianCalendar で返す
     *
     * @return GregorianCalendar
     */
    @Nullable
    public GregorianCalendar getLastVisitGc() {
        return Objects.isNull(lastVisit) ? null :
                GregorianCalendar.from(lastVisit.atStartOfDay(ZoneId.systemDefault()));
    }
}
