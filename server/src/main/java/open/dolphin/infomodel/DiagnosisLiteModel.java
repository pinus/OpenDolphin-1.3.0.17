package open.dolphin.infomodel;

/**
 * DiagnosisDocument の undo 用にデータを入れておく軽いモデル.
 *
 * @author pns
 */
public class DiagnosisLiteModel {
    private String diagnosisDesc;
    private String diagnosisCode;
    private String category;
    private String categoryDesc;
    private String categoryCodeSys;
    private String outcome;
    private String outcomeDesc;
    private String outcomeCodeSys;
    private String startDate;
    private String endDate;
    private String status;

    public DiagnosisLiteModel(RegisteredDiagnosisModel rd) {
        diagnosisDesc = rd.getDiagnosis();
        diagnosisCode = rd.getDiagnosisCode();
        category = rd.getCategory();
        categoryDesc = rd.getCategoryDesc();
        categoryCodeSys = rd.getCategoryCodeSys();
        outcome = rd.getOutcome();
        outcomeDesc = rd.getOutcomeDesc();
        outcomeCodeSys = rd.getOutcomeCodeSys();
        startDate = rd.getStartDate();
        endDate = rd.getEndDate();
        status = rd.getStatus();
    }

    public String getDiagnosisDesc() {
        return diagnosisDesc;
    }

    public void setDiagnosisDesc(String diagnosisDesc) {
        this.diagnosisDesc = diagnosisDesc;
    }

    public String getDiagnosisCode() {
        return diagnosisCode;
    }

    public void setDiagnosisCode(String diagnosisCode) {
        this.diagnosisCode = diagnosisCode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public String getCategoryCodeSys() {
        return categoryCodeSys;
    }

    public void setCategoryCodeSys(String categoryCodeSys) {
        this.categoryCodeSys = categoryCodeSys;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getOutcomeDesc() {
        return outcomeDesc;
    }

    public void setOutcomeDesc(String outcomeDesc) {
        this.outcomeDesc = outcomeDesc;
    }

    public String getOutcomeCodeSys() {
        return outcomeCodeSys;
    }

    public void setOutcomeCodeSys(String outcomeCodeSys) {
        this.outcomeCodeSys = outcomeCodeSys;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void resume(RegisteredDiagnosisModel rd) {
        rd.setDiagnosis(getDiagnosisDesc());
        rd.setDiagnosisCode(getDiagnosisCode());
        rd.setCategory(getCategory());
        rd.setCategoryDesc(getCategoryDesc());
        rd.setCategoryCodeSys(getCategoryCodeSys());
        rd.setOutcome(getOutcome());
        rd.setOutcomeDesc(getOutcomeDesc());
        rd.setOutcomeCodeSys(getOutcomeCodeSys());
        rd.setStartDate(getStartDate());
        rd.setEndDate(getEndDate());
        rd.setStatus(getStatus());
    }

    public boolean equals(RegisteredDiagnosisModel other) {
        return equals(other.getDiagnosisCode(), diagnosisCode) &&
            equals(other.getCategory(), category) &&
            equals(other.getOutcome(), outcome) &&
            equals(other.getStartDate(), startDate) &&
            equals(other.getEndDate(), endDate);
    }

    /**
     * 両方 null なら等しいと判定する equals.
     * @param s1 文字列1
     * @param s2 文字列2
     * @return 判定結果
     */
    private boolean equals(String s1, String s2) {
        return (s1 == null)? s2 == null : s1.equals(s2);
    }
}
