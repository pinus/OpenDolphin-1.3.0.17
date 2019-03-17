package open.dolphin.orca.orcaapi.bean;

/**
 * subjectivesmodres.
 *
 * @author pns
 */
public class Subjectivesmodres {
    /**
     * 実施日 (例: 2014-07-04)
     */
    private String Information_Date;

    /**
     * 実施時間 (例: 11:35:33)
     */
    private String Information_Time;

    /**
     * 結果コード (例: K1)
     */
    private String Api_Result;

    /**
     * 結果メッセージ (例: レセコメント登録終了)
     */
    private String Api_Result_Message;

    /**
     * 警告メッセージ情報（繰り返し　５） (例:  )
     */
    private ApiWarningMessageInformation[] Api_Warning_Message_Information;

    /**
     * レスポンスキー情報 (例: Acceptance_Info)
     */
    private String Reskey;

    /**
     * 入外区分（I:入院、それ以外:入院外） (例: O)
     */
    private String InOut;

    /**
     * 患者情報 (例:  )
     */
    private PatientInformation Patient_Information;

    /**
     * 診療年月 (例: 2014-07)
     */
    private String Perform_Date;

    /**
     * 診療科コード※１（01:内科） (例: 01)
     */
    private String Department_Code;

    /**
     * 診療科名称 (例: 内科)
     */
    private String Department_WholeName;

    /**
     * 保険組合せ番号 (例: 0000)
     */
    private String Insurance_Combination_Number;

    /**
     * 保険組合せ情報 (例:  )
     */
    private HealthInsuranceInformation HealthInsurance_Information;

    /**
     * 連番 (例: 01)
     */
    private String Subjectives_Number;

    /**
     * 詳記区分 (例: 07)
     */
    private String Subjectives_Detail_Record;

    /**
     * 詳記区分名称 (例: その他（１）)
     */
    private String Subjectives_Detail_Record_WholeName;

    /**
     * 症状詳記内容 (例: その他コメント)
     */
    private String Subjectives_Code;

    /**
     * 実施日 (例: 2014-07-04)
     *
     * @return the Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * 実施日 (例: 2014-07-04)
     *
     * @param Information_Date the Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * 実施時間 (例: 11:35:33)
     *
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 実施時間 (例: 11:35:33)
     *
     * @param Information_Time the Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * 結果コード (例: K1)
     *
     * @return the Api_Result
     */
    public String getApi_Result() {
        return Api_Result;
    }

    /**
     * 結果コード (例: K1)
     *
     * @param Api_Result the Api_Result to set
     */
    public void setApi_Result(String Api_Result) {
        this.Api_Result = Api_Result;
    }

    /**
     * 結果メッセージ (例: レセコメント登録終了)
     *
     * @return the Api_Result_Message
     */
    public String getApi_Result_Message() {
        return Api_Result_Message;
    }

    /**
     * 結果メッセージ (例: レセコメント登録終了)
     *
     * @param Api_Result_Message the Api_Result_Message to set
     */
    public void setApi_Result_Message(String Api_Result_Message) {
        this.Api_Result_Message = Api_Result_Message;
    }

    /**
     * 警告メッセージ情報（繰り返し　５） (例:  )
     *
     * @return the Api_Warning_Message_Information
     */
    public ApiWarningMessageInformation[] getApi_Warning_Message_Information() {
        return Api_Warning_Message_Information;
    }

    /**
     * 警告メッセージ情報（繰り返し　５） (例:  )
     *
     * @param Api_Warning_Message_Information the Api_Warning_Message_Information to set
     */
    public void setApi_Warning_Message_Information(ApiWarningMessageInformation[] Api_Warning_Message_Information) {
        this.Api_Warning_Message_Information = Api_Warning_Message_Information;
    }

    /**
     * レスポンスキー情報 (例: Acceptance_Info)
     *
     * @return the Reskey
     */
    public String getReskey() {
        return Reskey;
    }

    /**
     * レスポンスキー情報 (例: Acceptance_Info)
     *
     * @param Reskey the Reskey to set
     */
    public void setReskey(String Reskey) {
        this.Reskey = Reskey;
    }

    /**
     * 入外区分（I:入院、それ以外:入院外） (例: O)
     *
     * @return the InOut
     */
    public String getInOut() {
        return InOut;
    }

    /**
     * 入外区分（I:入院、それ以外:入院外） (例: O)
     *
     * @param InOut the InOut to set
     */
    public void setInOut(String InOut) {
        this.InOut = InOut;
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
     * 診療年月 (例: 2014-07)
     *
     * @return the Perform_Date
     */
    public String getPerform_Date() {
        return Perform_Date;
    }

    /**
     * 診療年月 (例: 2014-07)
     *
     * @param Perform_Date the Perform_Date to set
     */
    public void setPerform_Date(String Perform_Date) {
        this.Perform_Date = Perform_Date;
    }

    /**
     * 診療科コード※１（01:内科） (例: 01)
     *
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 診療科コード※１（01:内科） (例: 01)
     *
     * @param Department_Code the Department_Code to set
     */
    public void setDepartment_Code(String Department_Code) {
        this.Department_Code = Department_Code;
    }

    /**
     * 診療科名称 (例: 内科)
     *
     * @return the Department_WholeName
     */
    public String getDepartment_WholeName() {
        return Department_WholeName;
    }

    /**
     * 診療科名称 (例: 内科)
     *
     * @param Department_WholeName the Department_WholeName to set
     */
    public void setDepartment_WholeName(String Department_WholeName) {
        this.Department_WholeName = Department_WholeName;
    }

    /**
     * 保険組合せ番号 (例: 0000)
     *
     * @return the Insurance_Combination_Number
     */
    public String getInsurance_Combination_Number() {
        return Insurance_Combination_Number;
    }

    /**
     * 保険組合せ番号 (例: 0000)
     *
     * @param Insurance_Combination_Number the Insurance_Combination_Number to set
     */
    public void setInsurance_Combination_Number(String Insurance_Combination_Number) {
        this.Insurance_Combination_Number = Insurance_Combination_Number;
    }

    /**
     * 保険組合せ情報 (例:  )
     *
     * @return the HealthInsurance_Information
     */
    public HealthInsuranceInformation getHealthInsurance_Information() {
        return HealthInsurance_Information;
    }

    /**
     * 保険組合せ情報 (例:  )
     *
     * @param HealthInsurance_Information the HealthInsurance_Information to set
     */
    public void setHealthInsurance_Information(HealthInsuranceInformation HealthInsurance_Information) {
        this.HealthInsurance_Information = HealthInsurance_Information;
    }

    /**
     * 連番 (例: 01)
     *
     * @return the Subjectives_Number
     */
    public String getSubjectives_Number() {
        return Subjectives_Number;
    }

    /**
     * 連番 (例: 01)
     *
     * @param Subjectives_Number the Subjectives_Number to set
     */
    public void setSubjectives_Number(String Subjectives_Number) {
        this.Subjectives_Number = Subjectives_Number;
    }

    /**
     * 詳記区分 (例: 07)
     *
     * @return the Subjectives_Detail_Record
     */
    public String getSubjectives_Detail_Record() {
        return Subjectives_Detail_Record;
    }

    /**
     * 詳記区分 (例: 07)
     *
     * @param Subjectives_Detail_Record the Subjectives_Detail_Record to set
     */
    public void setSubjectives_Detail_Record(String Subjectives_Detail_Record) {
        this.Subjectives_Detail_Record = Subjectives_Detail_Record;
    }

    /**
     * 詳記区分名称 (例: その他（１）)
     *
     * @return the Subjectives_Detail_Record_WholeName
     */
    public String getSubjectives_Detail_Record_WholeName() {
        return Subjectives_Detail_Record_WholeName;
    }

    /**
     * 詳記区分名称 (例: その他（１）)
     *
     * @param Subjectives_Detail_Record_WholeName the Subjectives_Detail_Record_WholeName to set
     */
    public void setSubjectives_Detail_Record_WholeName(String Subjectives_Detail_Record_WholeName) {
        this.Subjectives_Detail_Record_WholeName = Subjectives_Detail_Record_WholeName;
    }

    /**
     * 症状詳記内容 (例: その他コメント)
     *
     * @return the Subjectives_Code
     */
    public String getSubjectives_Code() {
        return Subjectives_Code;
    }

    /**
     * 症状詳記内容 (例: その他コメント)
     *
     * @param Subjectives_Code the Subjectives_Code to set
     */
    public void setSubjectives_Code(String Subjectives_Code) {
        this.Subjectives_Code = Subjectives_Code;
    }
}