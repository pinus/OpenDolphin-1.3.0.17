package open.dolphin.client;

import open.dolphin.infomodel.DiagnosisOutcomeModel;

/**
 * DiagnosisOutcome.
 * ORCA の CLAIM では 1.治癒，2.死亡，3.中止，4.移行 の４つしか判定しない
 * @author pns
 */
public enum DiagnosisOutcome {

    none("", ""),
    fullyRecovered("全治", "MML0016"),
    end("終了", "MML0016"),
    pause("中止", "MML0016"),
    ;

    private final DiagnosisOutcomeModel model = new DiagnosisOutcomeModel();

    private DiagnosisOutcome(String desc, String codeSys) {
        model.setOutcome(name().equals("none") ? "" : name());
        model.setOutcomeDesc(desc);
        model.setOutcomeCodeSys(codeSys);
    }

    public DiagnosisOutcomeModel model() {
        return model;
    }

    @Override
    public String toString() {
        return model.getOutcomeDesc();
    }
}
