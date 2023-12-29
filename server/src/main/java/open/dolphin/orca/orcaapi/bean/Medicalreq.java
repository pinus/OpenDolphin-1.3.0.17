package open.dolphin.orca.orcaapi.bean;

/**
 * medicalreq.
 *
 * @author pns
 */
public class Medicalreq {
    /**
     * 入外区分(I:入院、それ以外:入院外) (例:  )
     */
    private String InOut;

    /**
     * 患者番号 (例: 17)
     */
    private String Patient_ID;

    /**
     * 診療日 (例: 2014-10-17)
     */
    private String Perform_Date;

    /**
     * 診療時間 (例: 14:10:12)
     */
    private String Perform_Time;

    /**
     *   (例:  )
     */
    private String Medical_Uid;

    /**
     * 診療情報 (例:  )
     */
    private DiagnosisInformation Diagnosis_Information;

    /**
     * Push通知指示 (例: Yes)
     */
    private String Medical_Push;

    /**
     * 入外区分(I:入院、それ以外:入院外) (例:  )
     *
     * @return the InOut
     */
    public String getInOut() {
        return InOut;
    }

    /**
     * 入外区分(I:入院、それ以外:入院外) (例:  )
     *
     * @param InOut the InOut to set
     */
    public void setInOut(String InOut) {
        this.InOut = InOut;
    }

    /**
     * 患者番号 (例: 17)
     *
     * @return the Patient_ID
     */
    public String getPatient_ID() {
        return Patient_ID;
    }

    /**
     * 患者番号 (例: 17)
     *
     * @param Patient_ID the Patient_ID to set
     */
    public void setPatient_ID(String Patient_ID) {
        this.Patient_ID = Patient_ID;
    }

    /**
     * 診療日 (例: 2014-10-17)
     *
     * @return the Perform_Date
     */
    public String getPerform_Date() {
        return Perform_Date;
    }

    /**
     * 診療日 (例: 2014-10-17)
     *
     * @param Perform_Date the Perform_Date to set
     */
    public void setPerform_Date(String Perform_Date) {
        this.Perform_Date = Perform_Date;
    }

    /**
     * 診療時間 (例: 14:10:12)
     *
     * @return the Perform_Time
     */
    public String getPerform_Time() {
        return Perform_Time;
    }

    /**
     * 診療時間 (例: 14:10:12)
     *
     * @param Perform_Time the Perform_Time to set
     */
    public void setPerform_Time(String Perform_Time) {
        this.Perform_Time = Perform_Time;
    }

    /**
     *   (例:  )
     *
     * @return the Medical_Uid
     */
    public String getMedical_Uid() {
        return Medical_Uid;
    }

    /**
     *   (例:  )
     *
     * @param Medical_Uid the Medical_Uid to set
     */
    public void setMedical_Uid(String Medical_Uid) {
        this.Medical_Uid = Medical_Uid;
    }

    /**
     * 診療情報 (例:  )
     *
     * @return the Diagnosis_Information
     */
    public DiagnosisInformation getDiagnosis_Information() {
        return Diagnosis_Information;
    }

    /**
     * 診療情報 (例:  )
     *
     * @param Diagnosis_Information the Diagnosis_Information to set
     */
    public void setDiagnosis_Information(DiagnosisInformation Diagnosis_Information) {
        this.Diagnosis_Information = Diagnosis_Information;
    }

    /**
     * Push通知指示 (例: Yes)
     *
     * @return Medical_Push
     */
    public String getMedical_Push() {
        return Medical_Push;
    }

    /**
     * Push通知指示 (例: Yes)
     *
     * @param Medical_Push to set
     */
    public void setMedical_Push(String Medical_Push) {
        this.Medical_Push = Medical_Push;
    }
}
