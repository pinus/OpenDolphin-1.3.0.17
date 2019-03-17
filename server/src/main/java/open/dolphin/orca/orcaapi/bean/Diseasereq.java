package open.dolphin.orca.orcaapi.bean;

/**
 * diseasereq.
 *
 * @author pns
 */
public class Diseasereq {
    /**
     * 患者番号 (例: 07009)
     */
    private String Patient_ID;

    /**
     * 基準月（空白時はシステム日の属する月） (例:  )
     */
    private String Base_Month;

    /**
     * 実施年月日 (例: 2017-03-01)
     */
    private String Perform_Date;

    /**
     * 実施時間 (例: 01:01:01)
     */
    private String Perform_Time;

    /**
     * 診療科情報 (例:  )
     */
    private DiagnosisInformation Diagnosis_Information;

    /**
     * 病名情報(繰り返し　５０) (例:  )
     */
    private DiseaseInformation[] Disease_Information;

    /**
     * 患者番号 (例: 07009)
     *
     * @return the Patient_ID
     */
    public String getPatient_ID() {
        return Patient_ID;
    }

    /**
     * 患者番号 (例: 07009)
     *
     * @param Patient_ID the Patient_ID to set
     */
    public void setPatient_ID(String Patient_ID) {
        this.Patient_ID = Patient_ID;
    }

    /**
     * 基準月（空白時はシステム日の属する月） (例:  )
     *
     * @return the Base_Month
     */
    public String getBase_Month() {
        return Base_Month;
    }

    /**
     * 基準月（空白時はシステム日の属する月） (例:  )
     *
     * @param Base_Month the Base_Month to set
     */
    public void setBase_Month(String Base_Month) {
        this.Base_Month = Base_Month;
    }

    /**
     * 実施年月日 (例: 2017-03-01)
     *
     * @return the Perform_Date
     */
    public String getPerform_Date() {
        return Perform_Date;
    }

    /**
     * 実施年月日 (例: 2017-03-01)
     *
     * @param Perform_Date the Perform_Date to set
     */
    public void setPerform_Date(String Perform_Date) {
        this.Perform_Date = Perform_Date;
    }

    /**
     * 実施時間 (例: 01:01:01)
     *
     * @return the Perform_Time
     */
    public String getPerform_Time() {
        return Perform_Time;
    }

    /**
     * 実施時間 (例: 01:01:01)
     *
     * @param Perform_Time the Perform_Time to set
     */
    public void setPerform_Time(String Perform_Time) {
        this.Perform_Time = Perform_Time;
    }

    /**
     * 診療科情報 (例:  )
     *
     * @return the Diagnosis_Information
     */
    public DiagnosisInformation getDiagnosis_Information() {
        return Diagnosis_Information;
    }

    /**
     * 診療科情報 (例:  )
     *
     * @param Diagnosis_Information the Diagnosis_Information to set
     */
    public void setDiagnosis_Information(DiagnosisInformation Diagnosis_Information) {
        this.Diagnosis_Information = Diagnosis_Information;
    }

    /**
     * 病名情報(繰り返し　５０) (例:  )
     *
     * @return the Disease_Information
     */
    public DiseaseInformation[] getDisease_Information() {
        return Disease_Information;
    }

    /**
     * 病名情報(繰り返し　５０) (例:  )
     *
     * @param Disease_Information the Disease_Information to set
     */
    public void setDisease_Information(DiseaseInformation[] Disease_Information) {
        this.Disease_Information = Disease_Information;
    }
}
