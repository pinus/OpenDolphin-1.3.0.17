package open.dolphin.client;

import open.dolphin.infomodel.DiagnosisCategoryModel;

/**
 * DiagnosisCategory.
 *
 * @author pns
 */
public enum DiagnosisCategory {

    none("", ""),
    mainDiagnosis(DiagnosisDocument.MAIN_DIAGNOSIS, "MML0012"),
    suspectedDiagnosis(DiagnosisDocument.SUSPECTED_DIAGNOSIS, "MML0015"),
    ;

    private final DiagnosisCategoryModel model = new DiagnosisCategoryModel();

    private DiagnosisCategory(String desc, String codeSys) {
        model.setDiagnosisCategory(name().equals("none") ? "" : name());
        model.setDiagnosisCategoryDesc(desc);
        model.setDiagnosisCategoryCodeSys(codeSys);
    }

    public DiagnosisCategoryModel model() {
        return model;
    }

    @Override
    public String toString() {
        return model.getDiagnosisCategoryDesc();
    }
}
