package open.dolphin.infomodel;

import javax.persistence.Embeddable;

/**
 * Diagnosis のカテゴリーモデル.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
@Embeddable
public class DiagnosisCategoryModel extends InfoModel {
    private static final long serialVersionUID = 7606390775489282517L;

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
