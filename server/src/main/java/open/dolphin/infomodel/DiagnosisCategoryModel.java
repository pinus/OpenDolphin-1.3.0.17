package open.dolphin.infomodel;

import jakarta.persistence.Embeddable;

/**
 * Diagnosis のカテゴリーモデル.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
@Embeddable
public class DiagnosisCategoryModel extends InfoModel {
    
    private String diagnosisCategory;
    private String diagnosisCategoryDesc;
    private String diagnosisCategoryCodeSys;

    public String getDiagnosisCategory() {
        return diagnosisCategory;
    }

    public void setDiagnosisCategory(String category) {
        this.diagnosisCategory = category;
    }

    public String getDiagnosisCategoryDesc() {
        return diagnosisCategoryDesc;
    }

    public void setDiagnosisCategoryDesc(String categoryDesc) {
        this.diagnosisCategoryDesc = categoryDesc;
    }

    public String getDiagnosisCategoryCodeSys() {
        return diagnosisCategoryCodeSys;
    }

    public void setDiagnosisCategoryCodeSys(String categoryCodeSys) {
        this.diagnosisCategoryCodeSys = categoryCodeSys;
    }

    @Override
    public String toString() {
        return getDiagnosisCategoryDesc();
    }
}
