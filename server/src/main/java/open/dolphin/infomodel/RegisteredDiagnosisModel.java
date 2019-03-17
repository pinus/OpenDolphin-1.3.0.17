package open.dolphin.infomodel;

import open.dolphin.util.ModelUtils;

import javax.persistence.*;

/**
 * 診断履歴クラス.
 *
 * @author Kazushi Minagawa, Digital Globe,Inc.
 */
@Entity
@Table(name = "d_diagnosis")
public class RegisteredDiagnosisModel extends KarteEntryBean<RegisteredDiagnosisModel> {
    private static final long serialVersionUID = 8449675831667704574L;

    // 疾患名
    @Column(nullable = false)
    private String diagnosis;

    // 疾患コード
    private String diagnosisCode;

    // 疾患コード体系名
    private String diagnosisCodeSystem;

    // 病名分類モデル
    @Embedded
    private DiagnosisCategoryModel diagnosisCategoryModel;

    // 転帰モデル
    @Embedded
    private DiagnosisOutcomeModel diagnosisOutcomeModel;

    // 疾患の初診日
    private String firstEncounterDate;

    // 関連健康保険情報
    private String relatedHealthInsurance;

    @Transient
    private PatientLiteModel patientLiteModel;

    @Transient
    private UserLiteModel userLiteModel;

    private static String[] splitDiagnosis(String diagnosis) {
        return (diagnosis == null) ? null : diagnosis.split("\\s*,\\s*");
    }

    public boolean isValidMML() {
        return getDiagnosis() != null;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDiagnosisCode() {
        return diagnosisCode;
    }

    public void setDiagnosisCode(String diagnosisCode) {
        this.diagnosisCode = diagnosisCode;
    }

    public String getDiagnosisCodeSystem() {
        return diagnosisCodeSystem;
    }

    public void setDiagnosisCodeSystem(String diagnosisCodeSystem) {
        this.diagnosisCodeSystem = diagnosisCodeSystem;
    }

    public String getCategory() {
        return diagnosisCategoryModel != null ? diagnosisCategoryModel.getDiagnosisCategory() : null;
    }

    public void setCategory(String category) {
        if (diagnosisCategoryModel == null) {
            diagnosisCategoryModel = new DiagnosisCategoryModel();
        }
        this.diagnosisCategoryModel.setDiagnosisCategory(category);
    }

    public String getCategoryDesc() {
        return diagnosisCategoryModel != null ? diagnosisCategoryModel.getDiagnosisCategoryDesc() : null;
    }

    public void setCategoryDesc(String categoryDesc) {
        if (diagnosisCategoryModel == null) {
            diagnosisCategoryModel = new DiagnosisCategoryModel();
        }
        this.diagnosisCategoryModel.setDiagnosisCategoryDesc(categoryDesc);
    }

    public String getCategoryCodeSys() {
        return diagnosisCategoryModel != null ? diagnosisCategoryModel.getDiagnosisCategoryCodeSys() : null;
    }

    public void setCategoryCodeSys(String categoryTable) {
        if (diagnosisCategoryModel == null) {
            diagnosisCategoryModel = new DiagnosisCategoryModel();
        }
        this.diagnosisCategoryModel.setDiagnosisCategoryCodeSys(categoryTable);
    }

    public String getFirstEncounterDate() {
        return firstEncounterDate;
    }

    public void setFirstEncounterDate(String firstEncounterDate) {
        this.firstEncounterDate = firstEncounterDate;
    }

    public String getStartDate() {
        if (getStarted() != null) {
            return ModelUtils.getDateAsString(getStarted());
        }
        return null;
    }

    public void setStartDate(String startDate) {
        if (startDate != null) {
            int index = startDate.indexOf('T');
            if (index < 0) {
                startDate += "T00:00:00";
            }
            //System.out.println(startDate);
            setStarted(ModelUtils.getDateTimeAsObject(startDate));
        }
    }

    public String getEndDate() {
        if (getEnded() != null) {
            return ModelUtils.getDateAsString(getEnded());
        }
        return null;
    }

    public void setEndDate(String endDate) {
        if (endDate != null) {
            int index = endDate.indexOf('T');
            if (index < 0) {
                endDate += "T00:00:00";
            }
            setEnded(ModelUtils.getDateTimeAsObject(endDate));
        } else {
            setEnded(null);
        }
    }

    public String getOutcome() {
        return diagnosisOutcomeModel != null ? diagnosisOutcomeModel.getOutcome() : null;
    }

    public void setOutcome(String outcome) {
        if (diagnosisOutcomeModel == null) {
            diagnosisOutcomeModel = new DiagnosisOutcomeModel();
        }
        this.diagnosisOutcomeModel.setOutcome(outcome);
    }

    public String getOutcomeDesc() {
        return diagnosisOutcomeModel != null ? diagnosisOutcomeModel.getOutcomeDesc() : null;
    }

    public void setOutcomeDesc(String outcomeDesc) {
        if (diagnosisOutcomeModel == null) {
            diagnosisOutcomeModel = new DiagnosisOutcomeModel();
        }
        this.diagnosisOutcomeModel.setOutcomeDesc(outcomeDesc);
    }

    public String getOutcomeCodeSys() {
        return diagnosisOutcomeModel != null ? diagnosisOutcomeModel.getOutcomeCodeSys() : null;
    }

    public void setOutcomeCodeSys(String outcomeTable) {
        if (diagnosisOutcomeModel == null) {
            diagnosisOutcomeModel = new DiagnosisOutcomeModel();
        }
        this.diagnosisOutcomeModel.setOutcomeCodeSys(outcomeTable);
    }

    public String getRelatedHealthInsurance() {
        return relatedHealthInsurance;
    }

    public void setRelatedHealthInsurance(String relatedHealthInsurance) {
        this.relatedHealthInsurance = relatedHealthInsurance;
    }

    public DiagnosisCategoryModel getDiagnosisCategoryModel() {
        return diagnosisCategoryModel;
    }

    public void setDiagnosisCategoryModel(
            DiagnosisCategoryModel diagnosisCategoryModel) {
        this.diagnosisCategoryModel = diagnosisCategoryModel;
    }

    public DiagnosisOutcomeModel getDiagnosisOutcomeModel() {
        return diagnosisOutcomeModel;
    }

    public void setDiagnosisOutcomeModel(
            DiagnosisOutcomeModel diagnosisOutcomeModel) {
        this.diagnosisOutcomeModel = diagnosisOutcomeModel;
    }

    public PatientLiteModel getPatientLiteModel() {
        return patientLiteModel;
    }

    public void setPatientLiteModel(PatientLiteModel patientLiteModel) {
        this.patientLiteModel = patientLiteModel;
    }

    public UserLiteModel getUserLiteModel() {
        return userLiteModel;
    }

    public void setUserLiteModel(UserLiteModel userLiteModel) {
        this.userLiteModel = userLiteModel;
    }

    public String getDiagnosisName() {
        String[] splits = splitDiagnosis(this.diagnosis);
        return (splits != null && splits.length == 2 && splits[0] != null) ? splits[0] : this.diagnosis;
    }

    public String getDiagnosisAlias() {
        String[] splits = splitDiagnosis(this.diagnosis);
        return (splits != null && splits.length == 2 && splits[1] != null) ? splits[1] : null;
    }

    public String getAliasOrName() {
        String alias = getDiagnosisAlias();
        return (alias == null) ? this.diagnosis : alias;
    }

    /**
     * RegisteredDiagnosisModel の equal 判定.
     *
     * @param obj 比較対象
     * @return 判定結果
     */
    @Override
    public boolean equals(Object obj) {
        // obj が RegisteredDiagnosisModel でなければ not equal
        if (!(obj instanceof RegisteredDiagnosisModel)) {
            return false;
        }

        RegisteredDiagnosisModel target = (RegisteredDiagnosisModel) obj;

        // id == 0 はまだデータベースに保存されていない病名
        // SystemHash の値 ＋ diagnosis の値 で判定（厳密に一意ではないが，まず重なる可能性はない）
        if (this.getId() == 0 && target.getId() == 0) {
            return (System.identityHashCode(this) == System.identityHashCode(obj)
                    && (this.getDiagnosis().equals(target.getDiagnosis())));
        }

        // id != 0 の場合
        return (this.getId() == target.getId());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
