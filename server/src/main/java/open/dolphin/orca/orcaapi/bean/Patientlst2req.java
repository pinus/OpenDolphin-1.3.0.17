package open.dolphin.orca.orcaapi.bean;

/**
 * patientlst2req.
 * @author pns
 */
public class Patientlst2req {
    /**
     * 患者番号情報(繰り返し100) (例:  )
     */
    private PatientIdInformation[] Patient_ID_Information;

    /**
     * 患者番号情報(繰り返し100) (例:  )
     * @return the Patient_ID_Information
     */
    public PatientIdInformation[] getPatient_ID_Information() {
        return Patient_ID_Information;
    }

    /**
     * 患者番号情報(繰り返し100) (例:  )
     * @param Patient_ID_Information the Patient_ID_Information to set
     */
    public void setPatient_ID_Information(PatientIdInformation[] Patient_ID_Information) {
        this.Patient_ID_Information = Patient_ID_Information;
    }
}
