package open.dolphin.orca.orcaapi.bean;

/**
 * Patient_ID_Information. 患者番号情報(繰り返し100)
 *
 * @author pns
 */
public class PatientIdInformation {
    /**
     * 患者番号 (例: 12)
     */
    private String Patient_ID;

    /**
     * 患者番号 (例: 12)
     *
     * @return the Patient_ID
     */
    public String getPatient_ID() {
        return Patient_ID;
    }

    /**
     * 患者番号 (例: 12)
     *
     * @param Patient_ID the Patient_ID to set
     */
    public void setPatient_ID(String Patient_ID) {
        this.Patient_ID = Patient_ID;
    }
}