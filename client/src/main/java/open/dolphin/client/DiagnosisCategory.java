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

    public static DiagnosisCategoryModel[] models() {
        DiagnosisCategoryModel[] models = new DiagnosisCategoryModel[values().length];
        for (int i=0; i<models.length; i++) {
            models[i] = values()[i].model();
        }
        return models;
    }

    @Override
    public String toString() {
        return model.getDiagnosisCategoryDesc();
    }
}
