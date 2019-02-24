package open.dolphin.orca.orcaapi.bean;

/**
 * contraindication_checkres.
 * @author pns
 */
public class ContraindicationCheckres {
    /**
     * 実施日 (例: )
     */
    private String Information_Date;

    /**
     * 実施時間 (例: )
     */
    private String Information_Time;

    /**
     * エラーコード (例: )
     */
    private String Api_Result;

    /**
     * メッセージ (例: )
     */
    private String Api_Result_Message;

    /**
     *  (例: )
     */
    private String Reskey;

    /**
     * 診療年月 (例: )
     */
    private String Perform_Month;

    /**
     * 患者情報 (例: )
     */
    private PatientInformation Patient_Information;

    /**
     * チェック薬剤情報（繰り返し30） (例: )
     */
    private MedicalInformation6[] Medical_Information;

    /**
     * 症状詳記内容※4（繰り返し50） (例: )
     */
    private SymptomInformation[] Symptom_Information;

    /**
     * Information_Date
     *
     * @return Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * Information_Date
     *
     * @param Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * Information_Time
     *
     * @return Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * Information_Time
     *
     * @param Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * Api_Result
     *
     * @return Api_Result
     */
    public String getApi_Result() {
        return Api_Result;
    }

    /**
     * Api_Result
     *
     * @param Api_Result to set
     */
    public void setApi_Result(String Api_Result) {
        this.Api_Result = Api_Result;
    }

    /**
     * Api_Result_Message
     *
     * @return Api_Result_Message
     */
    public String getApi_Result_Message() {
        return Api_Result_Message;
    }

    /**
     * Api_Result_Message
     *
     * @param Api_Result_Message to set
     */
    public void setApi_Result_Message(String Api_Result_Message) {
        this.Api_Result_Message = Api_Result_Message;
    }

    /**
     * Reskey
     *
     * @return Reskey
     */
    public String getReskey() {
        return Reskey;
    }

    /**
     * Reskey
     *
     * @param Reskey to set
     */
    public void setReskey(String Reskey) {
        this.Reskey = Reskey;
    }

    /**
     * Perform_Month
     *
     * @return Perform_Month
     */
    public String getPerform_Month() {
        return Perform_Month;
    }

    /**
     * Perform_Month
     *
     * @param Perform_Month to set
     */
    public void setPerform_Month(String Perform_Month) {
        this.Perform_Month = Perform_Month;
    }

    /**
     * Patient_Information
     *
     * @return Patient_Information
     */
    public PatientInformation getPatient_Information() {
        return Patient_Information;
    }

    /**
     * Patient_Information
     *
     * @param Patient_Information to set
     */
    public void setPatient_Information(PatientInformation Patient_Information) {
        this.Patient_Information = Patient_Information;
    }

    /**
     * Medical_Information
     *
     * @return Medical_Information
     */
    public MedicalInformation6[] getMedical_Information() {
        return Medical_Information;
    }

    /**
     * Medical_Information
     *
     * @param Medical_Information to set
     */
    public void setMedical_Information(MedicalInformation6[] Medical_Information) {
        this.Medical_Information = Medical_Information;
    }

    /**
     * Symptom_Information
     *
     * @return Symptom_Information
     */
    public SymptomInformation[] getSymptom_Information() {
        return Symptom_Information;
    }

    /**
     * Symptom_Information
     *
     * @param Symptom_Information to set
     */
    public void setSymptom_Information(SymptomInformation[] Symptom_Information) {
        this.Symptom_Information = Symptom_Information;
    }
}