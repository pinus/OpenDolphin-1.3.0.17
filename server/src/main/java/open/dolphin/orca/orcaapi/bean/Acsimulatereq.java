package open.dolphin.orca.orcaapi.bean;

/**
 * acsimulatereq.
 *
 * @author pns
 */
public class Acsimulatereq {
    /**
     * 患者番号 (例: 3999)
     */
    private String Patient_ID;

    /**
     * 診療年月日 (例: 2012-12-27)
     */
    private String Perform_Date;

    /**
     * 時間外区分 (例: 1)
     */
    private String Time_Class;

    /**
     * 診療情報 (例: )
     */
    private DiagnosisInformation Diagnosis_Information;

    /**
     * 患者番号 (例: 3999)
     *
     * @return the Patient_ID
     */
    public String getPatient_ID() {
        return Patient_ID;
    }

    /**
     * 患者番号 (例: 3999)
     *
     * @param Patient_ID the Patient_ID to set
     */
    public void setPatient_ID(String Patient_ID) {
        this.Patient_ID = Patient_ID;
    }

    /**
     * 診療年月日 (例: 2012-12-27)
     *
     * @return the Perform_Date
     */
    public String getPerform_Date() {
        return Perform_Date;
    }

    /**
     * 診療年月日 (例: 2012-12-27)
     *
     * @param Perform_Date the Perform_Date to set
     */
    public void setPerform_Date(String Perform_Date) {
        this.Perform_Date = Perform_Date;
    }

    /**
     * 時間外区分 (例: 1)
     *
     * @return the Time_Class
     */
    public String getTime_Class() {
        return Time_Class;
    }

    /**
     * 時間外区分 (例: 1)
     *
     * @param Time_Class the Time_Class to set
     */
    public void setTime_Class(String Time_Class) {
        this.Time_Class = Time_Class;
    }

    /**
     * 診療情報 (例: )
     *
     * @return the Diagnosis_Information
     */
    public DiagnosisInformation getDiagnosis_Information() {
        return Diagnosis_Information;
    }

    /**
     * 診療情報 (例: )
     *
     * @param Diagnosis_Information the Diagnosis_Information to set
     */
    public void setDiagnosis_Information(DiagnosisInformation Diagnosis_Information) {
        this.Diagnosis_Information = Diagnosis_Information;
    }
}