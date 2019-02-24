package open.dolphin.orca.orcaapi.bean;

/**
 * patientmodres.
 * @author pns
 */
public class Patientmodres {
    /**
     * 実施日 (例: 2014-07-17)
     */
    private String Information_Date;

    /**
     * 実施時間 (例: 10:38:30)
     */
    private String Information_Time;

    /**
     * 結果コード(ゼロ以外エラー) (例: 00)
     */
    private String Api_Result;

    /**
     * エラーメッセージ (例: 登録終了)
     */
    private String Api_Result_Message;

    /**
     * 警告メッセージ１ (例: 警告！同一患者の登録があります。)
     */
    private String Api_Warning_Message1;

    /**
     * 警告メッセージ２ (例:  )
     */
    private String Api_Warning_Message2;

    /**
     * 警告メッセージ３ (例:  )
     */
    private String Api_Warning_Message3;

    /**
     * 警告メッセージ４ (例:  )
     */
    private String Api_Warning_Message4;

    /**
     * 警告メッセージ５ (例:  )
     */
    private String Api_Warning_Message5;

    /**
     *   (例: Acceptance_Info)
     */
    private String Reskey;

    /**
     * 患者基本情報 (例:  )
     */
    private PatientInformation Patient_Information;

    /**
     * 実施日 (例: 2014-07-17)
     * @return the Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * 実施日 (例: 2014-07-17)
     * @param Information_Date the Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * 実施時間 (例: 10:38:30)
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 実施時間 (例: 10:38:30)
     * @param Information_Time the Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * 結果コード(ゼロ以外エラー) (例: 00)
     * @return the Api_Result
     */
    public String getApi_Result() {
        return Api_Result;
    }

    /**
     * 結果コード(ゼロ以外エラー) (例: 00)
     * @param Api_Result the Api_Result to set
     */
    public void setApi_Result(String Api_Result) {
        this.Api_Result = Api_Result;
    }

    /**
     * エラーメッセージ (例: 登録終了)
     * @return the Api_Result_Message
     */
    public String getApi_Result_Message() {
        return Api_Result_Message;
    }

    /**
     * エラーメッセージ (例: 登録終了)
     * @param Api_Result_Message the Api_Result_Message to set
     */
    public void setApi_Result_Message(String Api_Result_Message) {
        this.Api_Result_Message = Api_Result_Message;
    }

    /**
     * 警告メッセージ１ (例: 警告！同一患者の登録があります。)
     * @return the Api_Warning_Message1
     */
    public String getApi_Warning_Message1() {
        return Api_Warning_Message1;
    }

    /**
     * 警告メッセージ１ (例: 警告！同一患者の登録があります。)
     * @param Api_Warning_Message1 the Api_Warning_Message1 to set
     */
    public void setApi_Warning_Message1(String Api_Warning_Message1) {
        this.Api_Warning_Message1 = Api_Warning_Message1;
    }

    /**
     * 警告メッセージ２ (例:  )
     * @return the Api_Warning_Message2
     */
    public String getApi_Warning_Message2() {
        return Api_Warning_Message2;
    }

    /**
     * 警告メッセージ２ (例:  )
     * @param Api_Warning_Message2 the Api_Warning_Message2 to set
     */
    public void setApi_Warning_Message2(String Api_Warning_Message2) {
        this.Api_Warning_Message2 = Api_Warning_Message2;
    }

    /**
     * 警告メッセージ３ (例:  )
     * @return the Api_Warning_Message3
     */
    public String getApi_Warning_Message3() {
        return Api_Warning_Message3;
    }

    /**
     * 警告メッセージ３ (例:  )
     * @param Api_Warning_Message3 the Api_Warning_Message3 to set
     */
    public void setApi_Warning_Message3(String Api_Warning_Message3) {
        this.Api_Warning_Message3 = Api_Warning_Message3;
    }

    /**
     * 警告メッセージ４ (例:  )
     * @return the Api_Warning_Message4
     */
    public String getApi_Warning_Message4() {
        return Api_Warning_Message4;
    }

    /**
     * 警告メッセージ４ (例:  )
     * @param Api_Warning_Message4 the Api_Warning_Message4 to set
     */
    public void setApi_Warning_Message4(String Api_Warning_Message4) {
        this.Api_Warning_Message4 = Api_Warning_Message4;
    }

    /**
     * 警告メッセージ５ (例:  )
     * @return the Api_Warning_Message5
     */
    public String getApi_Warning_Message5() {
        return Api_Warning_Message5;
    }

    /**
     * 警告メッセージ５ (例:  )
     * @param Api_Warning_Message5 the Api_Warning_Message5 to set
     */
    public void setApi_Warning_Message5(String Api_Warning_Message5) {
        this.Api_Warning_Message5 = Api_Warning_Message5;
    }

    /**
     *   (例: Acceptance_Info)
     * @return the Reskey
     */
    public String getReskey() {
        return Reskey;
    }

    /**
     *   (例: Acceptance_Info)
     * @param Reskey the Reskey to set
     */
    public void setReskey(String Reskey) {
        this.Reskey = Reskey;
    }

    /**
     * 患者基本情報 (例:  )
     * @return the Patient_Information
     */
    public PatientInformation getPatient_Information() {
        return Patient_Information;
    }

    /**
     * 患者基本情報 (例:  )
     * @param Patient_Information the Patient_Information to set
     */
    public void setPatient_Information(PatientInformation Patient_Information) {
        this.Patient_Information = Patient_Information;
    }
}