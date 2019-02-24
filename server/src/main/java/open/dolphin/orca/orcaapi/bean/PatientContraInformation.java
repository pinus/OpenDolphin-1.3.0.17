package open.dolphin.orca.orcaapi.bean;

/**
 * Patient_Contra_Information. 患者禁忌薬剤情報
 * @author pns
 */
public class PatientContraInformation {
    /**
     * 患者禁忌薬剤情報（繰り返し　１００） (例:  )
     */
    private PatientContraInfo[] Patient_Contra_Info;

    /**
     * Patient_Contra_Info
     *
     * @return Patient_Contra_Info
     */
    public PatientContraInfo[] getPatient_Contra_Info() {
        return Patient_Contra_Info;
    }

    /**
     * Patient_Contra_Info
     *
     * @param Patient_Contra_Info to set
     */
    public void setPatient_Contra_Info(PatientContraInfo[] Patient_Contra_Info) {
        this.Patient_Contra_Info = Patient_Contra_Info;
    }
}
