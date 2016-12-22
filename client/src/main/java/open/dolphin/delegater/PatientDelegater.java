package open.dolphin.delegater;

import java.util.List;
import open.dolphin.dto.PatientSearchSpec;
import open.dolphin.infomodel.HealthInsuranceModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.PVTHealthInsuranceModel;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.service.PatientService;

/**
 *
 * @author pns
 */
public class  PatientDelegater extends BusinessDelegater {

    /**
     * 患者を登録する.
     * @param patient PatientModel
     * @return PatientModel の primary key
     */
    public long putPatient(PatientModel patient) {
        return getService().addPatient(patient);
    }

    /**
     * 患者情報を検索して返す.
     * 検索高速化のため，健康保険情報は持っていない (@OneToMany のため lazy fetch になっている)
     * カルテオープン時（AbstractMainComponent#openKarte）に fetchHealthInsurance する必要あり
     * @param spec PatientSearchSpec 検索仕様
     * @return
     */
    public List<PatientModel> getPatients(PatientSearchSpec spec) {
        return getService().getPatientList(spec);
    }

    /**
     * 保険情報を情報を取りに行く
     * @param patient
     */
    public void fetchHealthInsurance(PatientModel patient) {

        // 既に fetch してあればそのまま帰る
        if (patient.getPvtHealthInsurances() != null) { return; }

        long patientPk = patient.getId();
        List<PVTHealthInsuranceModel> insurances = getService().getHealthInsuranceList(patientPk);

        patient.setHealthInsurances(null);
        patient.setPvtHealthInsurances(insurances);
    }

    /**
     * 患者情報を更新する.
     * @param patient 更新する患者
     * @return 更新数 1
     */
    public int updatePatient(PatientModel patient) {
        return getService().update(patient);
    }

    private PatientService getService() {
        return getService(PatientService.class);
    }
}
