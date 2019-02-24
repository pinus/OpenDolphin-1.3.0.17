package open.dolphin.orca.orcaapi.bean;

/**
 * subjectiveslstres.
 * @author pns
 */
public class Subjectiveslstres {
    /**
     * 処理日付システム日付 (例: )
     */
    private String Information_Date;

    /**
     * 処理時間システム時間 (例: )
     */
    private String Information_Time;

    /**
     * 処理区分※1 (例: )
     */
    private String Api_Result;

    /**
     * 処理メッセージ (例: )
     */
    private String Api_Result_Message;

    /**
     *  (例: )
     */
    private String Reskey;

    /**
     * 患者情報 (例: )
     */
    private PatientInformation Patient_Information;

    /**
     * 診療年月送信内容 (例: )
     */
    private String Perform_Date;

    /**
     * 症状詳記リスト（繰り返し50） (例: )
     */
    private SubjectivesInformation[] Subjectives_Information;

    /**
     * 症状詳記コメント※2 (例: )
     */
    private SubjectivesCodeInformation Subjectives_Code_Information;

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
     * Perform_Date
     *
     * @return Perform_Date
     */
    public String getPerform_Date() {
        return Perform_Date;
    }

    /**
     * Perform_Date
     *
     * @param Perform_Date to set
     */
    public void setPerform_Date(String Perform_Date) {
        this.Perform_Date = Perform_Date;
    }

    /**
     * Subjectives_Information
     *
     * @return Subjectives_Information
     */
    public SubjectivesInformation[] getSubjectives_Information() {
        return Subjectives_Information;
    }

    /**
     * Subjectives_Information
     *
     * @param Subjectives_Information to set
     */
    public void setSubjectives_Information(SubjectivesInformation[] Subjectives_Information) {
        this.Subjectives_Information = Subjectives_Information;
    }

    /**
     * Subjectives_Code_Information
     *
     * @return Subjectives_Code_Information
     */
    public SubjectivesCodeInformation getSubjectives_Code_Information() {
        return Subjectives_Code_Information;
    }

    /**
     * Subjectives_Code_Information
     *
     * @param Subjectives_Code_Information to set
     */
    public void setSubjectives_Code_Information(SubjectivesCodeInformation Subjectives_Code_Information) {
        this.Subjectives_Code_Information = Subjectives_Code_Information;
    }
}