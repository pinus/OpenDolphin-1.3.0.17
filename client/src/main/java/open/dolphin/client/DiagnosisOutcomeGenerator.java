package open.dolphin.client;

import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.project.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DiagnosisOutcomeGenerator {
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
    public String getDiagnosisOutcomeDate() {
        LocalDate startDate = Objects.nonNull(lv.getLastVisitInHistory()) ? lv.getLastVisitInHistory() : lv.getLastVisit();
        long monthBetween = ChronoUnit.MONTHS.between(startDate.withDayOfMonth(1), lv.getLastVisit().withDayOfMonth(1));
        logger.info("monthBetween " + monthBetween);

        int offset = Project.getPreferences().getInt(Project.OFFSET_OUTCOME_DATE, -1);
        int n = 1; // month interval

        LocalDate endDate = monthBetween <= n
            ? lv.getLastVisit().plusDays(offset)
            : startDate.plusMonths(n).withDayOfMonth(startDate.plusMonths(n).lengthOfMonth());

        logger.info("outcome = " + endDate.format(DateTimeFormatter.ISO_DATE));

        return endDate.format(DateTimeFormatter.ISO_DATE);
    }
}
