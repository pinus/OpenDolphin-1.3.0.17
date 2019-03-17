package open.dolphin.orca.orcaapi.bean;

/**
 * diseaseres.
 *
 * @author pns
 */
public class Diseaseres {
    /**
     * 処理日付 (例: 2017-05-18)
     */
    private String Information_Date;

    /**
     * 処理時間 (例: 14:35:31)
     */
    private String Information_Time;

    /**
     * 処理区分 (例: 000)
     */
    private String Api_Result;

    /**
     * 処理メッセージ (例: 処理実施終了)
     */
    private String Api_Result_Message;

    /**
     *   (例: Acceptance_Info)
     */
    private String Reskey;

    /**
     * 実施日付 (例: 2017-05-18)
     */
    private String Perform_Date;

    /**
     * 実施時間 (例: 01:01:01)
     */
    private String Perform_Time;

    /**
     * 診療科コード (例: 01)
     */
    private String Department_Code;

    /**
     * 診療科名 (例: 内科)
     */
    private String Department_Name;

    /**
     * 患者番号 (例: 00126)
     */
    private String Patient_ID;

    /**
     * 基準月（空白時はシステム日の属する月） (例: 2017-05)
     */
    private String Base_Month;

    /**
     * 病名登録結果(繰り返し　５０) (例:  )
     */
    private DiseaseMessageInformation[] Disease_Message_Information;

    /**
     * 不一致病名情報 (例:  )
     */
    private DiseaseUnmatchInformation Disease_Unmatch_Information;

    /**
     * 処理日付 (例: 2017-05-18)
     *
     * @return the Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * 処理日付 (例: 2017-05-18)
     *
     * @param Information_Date the Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * 処理時間 (例: 14:35:31)
     *
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 処理時間 (例: 14:35:31)
     *
     * @param Information_Time the Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * 処理区分 (例: 000)
     *
     * @return the Api_Result
     */
    public String getApi_Result() {
        return Api_Result;
    }

    /**
     * 処理区分 (例: 000)
     *
     * @param Api_Result the Api_Result to set
     */
    public void setApi_Result(String Api_Result) {
        this.Api_Result = Api_Result;
    }

    /**
     * 処理メッセージ (例: 処理実施終了)
     *
     * @return the Api_Result_Message
     */
    public String getApi_Result_Message() {
        return Api_Result_Message;
    }

    /**
     * 処理メッセージ (例: 処理実施終了)
     *
     * @param Api_Result_Message the Api_Result_Message to set
     */
    public void setApi_Result_Message(String Api_Result_Message) {
        this.Api_Result_Message = Api_Result_Message;
    }

    /**
     *   (例: Acceptance_Info)
     *
     * @return the Reskey
     */
    public String getReskey() {
        return Reskey;
    }

    /**
     *   (例: Acceptance_Info)
     *
     * @param Reskey the Reskey to set
     */
    public void setReskey(String Reskey) {
        this.Reskey = Reskey;
    }

    /**
     * 実施日付 (例: 2017-05-18)
     *
     * @return the Perform_Date
     */
    public String getPerform_Date() {
        return Perform_Date;
    }

    /**
     * 実施日付 (例: 2017-05-18)
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
     * 診療科名 (例: 内科)
     *
     * @return the Department_Name
     */
    public String getDepartment_Name() {
        return Department_Name;
    }

    /**
     * 診療科名 (例: 内科)
     *
     * @param Department_Name the Department_Name to set
     */
    public void setDepartment_Name(String Department_Name) {
        this.Department_Name = Department_Name;
    }

    /**
     * 患者番号 (例: 00126)
     *
     * @return the Patient_ID
     */
    public String getPatient_ID() {
        return Patient_ID;
    }

    /**
     * 患者番号 (例: 00126)
     *
     * @param Patient_ID the Patient_ID to set
     */
    public void setPatient_ID(String Patient_ID) {
        this.Patient_ID = Patient_ID;
    }

    /**
     * 基準月（空白時はシステム日の属する月） (例: 2017-05)
     *
     * @return the Base_Month
     */
    public String getBase_Month() {
        return Base_Month;
    }

    /**
     * 基準月（空白時はシステム日の属する月） (例: 2017-05)
     *
     * @param Base_Month the Base_Month to set
     */
    public void setBase_Month(String Base_Month) {
        this.Base_Month = Base_Month;
    }

    /**
     * 病名登録結果(繰り返し　５０) (例:  )
     *
     * @return the Disease_Message_Information
     */
    public DiseaseMessageInformation[] getDisease_Message_Information() {
        return Disease_Message_Information;
    }

    /**
     * 病名登録結果(繰り返し　５０) (例:  )
     *
     * @param Disease_Message_Information the Disease_Message_Information to set
     */
    public void setDisease_Message_Information(DiseaseMessageInformation[] Disease_Message_Information) {
        this.Disease_Message_Information = Disease_Message_Information;
    }

    /**
     * 不一致病名情報 (例:  )
     *
     * @return the Disease_Unmatch_Information
     */
    public DiseaseUnmatchInformation getDisease_Unmatch_Information() {
        return Disease_Unmatch_Information;
    }

    /**
     * 不一致病名情報 (例:  )
     *
     * @param Disease_Unmatch_Information the Disease_Unmatch_Information to set
     */
    public void setDisease_Unmatch_Information(DiseaseUnmatchInformation Disease_Unmatch_Information) {
        this.Disease_Unmatch_Information = Disease_Unmatch_Information;
    }
}