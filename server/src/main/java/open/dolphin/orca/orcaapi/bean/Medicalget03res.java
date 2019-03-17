package open.dolphin.orca.orcaapi.bean;

/**
 * medicalget03res.
 *
 * @author pns
 */
public class Medicalget03res {
    /**
     * 実施日 (例: 2015-08-18)
     */
    private String Information_Date;

    /**
     * 実施時間 (例: 19:15:16)
     */
    private String Information_Time;

    /**
     * エラーコード (例: 00)
     */
    private String Api_Result;

    /**
     * メッセージ (例: 処理終了)
     */
    private String Api_Result_Message;

    /**
     *   (例: MedicalInfo)
     */
    private String Reskey;

    /**
     * 患者情報 (例:  )
     */
    private PatientInformation Patient_Information;

    /**
     * 診療年月 (例: 2015-08)
     */
    private String Perform_Date;

    /**
     * 診療行為情報（繰り返し　４００） (例:  )
     * MedicalInfo と Medical_List_Information が混乱している
     */
    private MedicationInfo[] Medical_List_Information;

    /**
     * 実施日 (例: 2015-08-18)
     *
     * @return the Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * 実施日 (例: 2015-08-18)
     *
     * @param Information_Date the Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * 実施時間 (例: 19:15:16)
     *
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 実施時間 (例: 19:15:16)
     *
     * @param Information_Time the Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * エラーコード (例: 00)
     *
     * @return the Api_Result
     */
    public String getApi_Result() {
        return Api_Result;
    }

    /**
     * エラーコード (例: 00)
     *
     * @param Api_Result the Api_Result to set
     */
    public void setApi_Result(String Api_Result) {
        this.Api_Result = Api_Result;
    }

    /**
     * メッセージ (例: 処理終了)
     *
     * @return the Api_Result_Message
     */
    public String getApi_Result_Message() {
        return Api_Result_Message;
    }

    /**
     * メッセージ (例: 処理終了)
     *
     * @param Api_Result_Message the Api_Result_Message to set
     */
    public void setApi_Result_Message(String Api_Result_Message) {
        this.Api_Result_Message = Api_Result_Message;
    }

    /**
     *   (例: MedicalInfo)
     *
     * @return the Reskey
     */
    public String getReskey() {
        return Reskey;
    }

    /**
     *   (例: MedicalInfo)
     *
     * @param Reskey the Reskey to set
     */
    public void setReskey(String Reskey) {
        this.Reskey = Reskey;
    }

    /**
     * 患者情報 (例:  )
     *
     * @return the Patient_Information
     */
    public PatientInformation getPatient_Information() {
        return Patient_Information;
    }

    /**
     * 患者情報 (例:  )
     *
     * @param Patient_Information the Patient_Information to set
     */
    public void setPatient_Information(PatientInformation Patient_Information) {
        this.Patient_Information = Patient_Information;
    }

    /**
     * 診療年月 (例: 2015-08)
     *
     * @return the Perform_Date
     */
    public String getPerform_Date() {
        return Perform_Date;
    }

    /**
     * 診療年月 (例: 2015-08)
     *
     * @param Perform_Date the Perform_Date to set
     */
    public void setPerform_Date(String Perform_Date) {
        this.Perform_Date = Perform_Date;
    }

    /**
     * 診療行為情報（繰り返し　４００） (例:  )
     * MedicalInfo と Medical_List_Information が混乱している
     *
     * @return the Medical_List_Information
     */
    public MedicationInfo[] getMedical_List_Information() {
        return Medical_List_Information;
    }

    /**
     * 診療行為情報（繰り返し　４００） (例:  )
     * MedicalInfo と Medical_List_Information が混乱している
     *
     * @param Medical_List_Information the Medical_List_Information to set
     */
    public void setMedical_List_Information(MedicationInfo[] Medical_List_Information) {
        this.Medical_List_Information = Medical_List_Information;
    }
}