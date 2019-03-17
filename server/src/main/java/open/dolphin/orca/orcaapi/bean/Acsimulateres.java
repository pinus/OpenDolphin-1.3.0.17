package open.dolphin.orca.orcaapi.bean;

/**
 * acsimulateres.
 *
 * @author pns
 */
public class Acsimulateres {
    /**
     * 実施日 (例: 2012-12-27)
     */
    private String Information_Date;

    /**
     * 実施時間 (例: 20:03:33)
     */
    private String Information_Time;

    /**
     * 結果コード (例: 00)
     */
    private String Api_Result;

    /**
     * 結果メッセージ (例: 処理終了)
     */
    private String Api_Result_Message;

    /**
     * レスポンスキー情報 (例: MedicalInfo)
     */
    private String Reskey;

    /**
     * 診療年月日 (例: 2012-12-27)
     */
    private String Perform_Date;

    /**
     * 時間外区分 (例: 1)
     */
    private String Time_Class;

    /**
     * 診療科コード (例: 01)
     */
    private String Department_Code;

    /**
     * 診療科名称 (例: 内科)
     */
    private String Department_Name;

    /**
     * 患者情報 (例: )
     */
    private PatientInformation2 Patient_Information;

    /**
     * 実施日 (例: 2012-12-27)
     *
     * @return the Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * 実施日 (例: 2012-12-27)
     *
     * @param Information_Date the Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * 実施時間 (例: 20:03:33)
     *
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 実施時間 (例: 20:03:33)
     *
     * @param Information_Time the Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * 結果コード (例: 00)
     *
     * @return the Api_Result
     */
    public String getApi_Result() {
        return Api_Result;
    }

    /**
     * 結果コード (例: 00)
     *
     * @param Api_Result the Api_Result to set
     */
    public void setApi_Result(String Api_Result) {
        this.Api_Result = Api_Result;
    }

    /**
     * 結果メッセージ (例: 処理終了)
     *
     * @return the Api_Result_Message
     */
    public String getApi_Result_Message() {
        return Api_Result_Message;
    }

    /**
     * 結果メッセージ (例: 処理終了)
     *
     * @param Api_Result_Message the Api_Result_Message to set
     */
    public void setApi_Result_Message(String Api_Result_Message) {
        this.Api_Result_Message = Api_Result_Message;
    }

    /**
     * レスポンスキー情報 (例: MedicalInfo)
     *
     * @return the Reskey
     */
    public String getReskey() {
        return Reskey;
    }

    /**
     * レスポンスキー情報 (例: MedicalInfo)
     *
     * @param Reskey the Reskey to set
     */
    public void setReskey(String Reskey) {
        this.Reskey = Reskey;
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
     * 診療科コード (例: 01)
     *
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 診療科コード (例: 01)
     *
     * @param Department_Code the Department_Code to set
     */
    public void setDepartment_Code(String Department_Code) {
        this.Department_Code = Department_Code;
    }

    /**
     * 診療科名称 (例: 内科)
     *
     * @return the Department_Name
     */
    public String getDepartment_Name() {
        return Department_Name;
    }

    /**
     * 診療科名称 (例: 内科)
     *
     * @param Department_Name the Department_Name to set
     */
    public void setDepartment_Name(String Department_Name) {
        this.Department_Name = Department_Name;
    }

    /**
     * 患者情報 (例: )
     *
     * @return the Patient_Information
     */
    public PatientInformation2 getPatient_Information() {
        return Patient_Information;
    }

    /**
     * 患者情報 (例: )
     *
     * @param Patient_Information the Patient_Information to set
     */
    public void setPatient_Information(PatientInformation2 Patient_Information) {
        this.Patient_Information = Patient_Information;
    }
}