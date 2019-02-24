package open.dolphin.orca.orcaapi.bean;

/**
 * patientlst1res.
 * @author pns
 */
public class Patientlst1res {
    /**
     * 実施日 (例: 2014-07-15)
     */
    private String Information_Date;

    /**
     * 実施時間 (例: 15:35:04)
     */
    private String Information_Time;

    /**
     * 結果コード（ゼロ以外エラー） (例: 00)
     */
    private String Api_Result;

    /**
     * エラーメッセージ (例: 処理終了)
     */
    private String Api_Result_Message;

    /**
     *   (例: PatientInfo)
     */
    private String Reskey;

    /**
     * 対象件数 (例: 0002)
     */
    private String Target_Patient_Count;

    /**
     * 患者情報(繰り返し1000) (例:  )
     */
    private PatientInformation[] Patient_Information;

    /**
     * 実施日 (例: 2014-07-15)
     * @return the Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * 実施日 (例: 2014-07-15)
     * @param Information_Date the Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * 実施時間 (例: 15:35:04)
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 実施時間 (例: 15:35:04)
     * @param Information_Time the Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * 結果コード（ゼロ以外エラー） (例: 00)
     * @return the Api_Result
     */
    public String getApi_Result() {
        return Api_Result;
    }

    /**
     * 結果コード（ゼロ以外エラー） (例: 00)
     * @param Api_Result the Api_Result to set
     */
    public void setApi_Result(String Api_Result) {
        this.Api_Result = Api_Result;
    }

    /**
     * エラーメッセージ (例: 処理終了)
     * @return the Api_Result_Message
     */
    public String getApi_Result_Message() {
        return Api_Result_Message;
    }

    /**
     * エラーメッセージ (例: 処理終了)
     * @param Api_Result_Message the Api_Result_Message to set
     */
    public void setApi_Result_Message(String Api_Result_Message) {
        this.Api_Result_Message = Api_Result_Message;
    }

    /**
     *   (例: PatientInfo)
     * @return the Reskey
     */
    public String getReskey() {
        return Reskey;
    }

    /**
     *   (例: PatientInfo)
     * @param Reskey the Reskey to set
     */
    public void setReskey(String Reskey) {
        this.Reskey = Reskey;
    }

    /**
     * 対象件数 (例: 0002)
     * @return the Target_Patient_Count
     */
    public String getTarget_Patient_Count() {
        return Target_Patient_Count;
    }

    /**
     * 対象件数 (例: 0002)
     * @param Target_Patient_Count the Target_Patient_Count to set
     */
    public void setTarget_Patient_Count(String Target_Patient_Count) {
        this.Target_Patient_Count = Target_Patient_Count;
    }

    /**
     * 患者情報(繰り返し1000) (例:  )
     * @return the Patient_Information
     */
    public PatientInformation[] getPatient_Information() {
        return Patient_Information;
    }

    /**
     * 患者情報(繰り返し1000) (例:  )
     * @param Patient_Information the Patient_Information to set
     */
    public void setPatient_Information(PatientInformation[] Patient_Information) {
        this.Patient_Information = Patient_Information;
    }
}