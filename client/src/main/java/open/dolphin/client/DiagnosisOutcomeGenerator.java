package open.dolphin.client;

import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.project.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.stream.Stream;

public class DiagnosisOutcomeGenerator {
    private static int OFFSET = Project.getPreferences().getInt(Project.OFFSET_OUTCOME_DATE, -1);
    private static String[] ACUTE_DISEASE = { "ヘルペス", "単純疱疹" };

    private Logger logger = LoggerFactory.getLogger(DiagnosisOutcomeGenerator.class);
    private RegisteredDiagnosisModel rd;
    private LastVisit lv;

    public DiagnosisOutcomeGenerator() { }

    public void setParams(RegisteredDiagnosisModel registeredDiagnosisModel, LastVisit lastVisit) {
        rd = registeredDiagnosisModel;
        lv = lastVisit;
    }

    /**
     * 前回受診から1ヶ月以下なら offset 日戻した日付.
     * 前回受診から2ヶ月以上なら, 前回受診から1ヶ月後の最終日.
     *
     * @return ISO_DATE 型式の outcome date
     */
    public String standard() {
        LocalDate startDate = Objects.nonNull(lv.getLastVisitInHistory()) ? lv.getLastVisitInHistory() : lv.getLastVisit();
        long monthBetween = ChronoUnit.MONTHS.between(startDate.withDayOfMonth(1), lv.getLastVisit().withDayOfMonth(1));
        logger.info("monthBetween " + monthBetween);

        int n = 1; // month interval

        LocalDate endDate = monthBetween <= n
            ? lv.getLastVisit().plusDays(OFFSET)
            : startDate.plusMonths(n).withDayOfMonth(startDate.plusMonths(n).lengthOfMonth());

        return endDate.format(DateTimeFormatter.ISO_DATE);
    }

    /**
     * 前回受診月の末日に
     *
     * @return
     */
    public String special() {
        LocalDate lastVisit = lv.getLastVisit();
        LocalDate endDate = Stream.of(ACUTE_DISEASE).anyMatch(rd.getDiagnosis()::contains)
            ? rd.getStarted().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            : Objects.nonNull(lv.getLastVisitInHistory()) ? lv.getLastVisitInHistory() : lv.getLastVisit();

        // 月末に設定. ただし受診日がその月の場合は offset 日戻す
        endDate = endDate.getYear() == lastVisit.getYear() && endDate.getMonthValue() == lastVisit.getMonthValue()
            ? lastVisit.plusDays(OFFSET)
            : endDate.withDayOfMonth(endDate.lengthOfMonth());

        // startDate が月末で, endDate と同じか後ろになってしまったら

        return endDate.format(DateTimeFormatter.ISO_DATE);
    }
}
