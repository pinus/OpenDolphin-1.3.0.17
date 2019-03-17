package open.dolphin.orca.orcaapi.bean;

/**
 * medicalget02res.
 *
 * @author pns
 */
public class Medicalget02res {
    /**
     * 実施日 (例: 2015-08-18)
     */
    private String Information_Date;

    /**
     * 実施時間 (例: 18:36:51)
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
     * 診療年月日 (例: 2015-08-18)
     */
    private String Perform_Date;

    /**
     * 診療科コード（01：内科） (例: 10)
     */
    private String Department_Code;

    /**
     * 診療科名称 (例: 外科)
     */
    private String Department_Name;

    /**
     * 連番（診療科毎の同日連番、未設定は１） (例: 1)
     */
    private String Sequential_Number;

    /**
     * 受診履歴情報（繰り返し　５） (例:  )
     */
    private MedicalListInformation[] Medical_List_Information;

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
     * 実施時間 (例: 18:36:51)
     *
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 実施時間 (例: 18:36:51)
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
     * 診療年月日 (例: 2015-08-18)
     *
     * @return the Perform_Date
     */
    public String getPerform_Date() {
        return Perform_Date;
    }

    /**
     * 診療年月日 (例: 2015-08-18)
     *
     * @param Perform_Date the Perform_Date to set
     */
    public void setPerform_Date(String Perform_Date) {
        this.Perform_Date = Perform_Date;
    }

    /**
     * 診療科コード（01：内科） (例: 10)
     *
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 診療科コード（01：内科） (例: 10)
     *
     * @param Department_Code the Department_Code to set
     */
    public void setDepartment_Code(String Department_Code) {
        this.Department_Code = Department_Code;
    }

    /**
     * 診療科名称 (例: 外科)
     *
     * @return the Department_Name
     */
    public String getDepartment_Name() {
        return Department_Name;
    }

    /**
     * 診療科名称 (例: 外科)
     *
     * @param Department_Name the Department_Name to set
     */
    public void setDepartment_Name(String Department_Name) {
        this.Department_Name = Department_Name;
    }

    /**
     * 連番（診療科毎の同日連番、未設定は１） (例: 1)
     *
     * @return the Sequential_Number
     */
    public String getSequential_Number() {
        return Sequential_Number;
    }

    /**
     * 連番（診療科毎の同日連番、未設定は１） (例: 1)
     *
     * @param Sequential_Number the Sequential_Number to set
     */
    public void setSequential_Number(String Sequential_Number) {
        this.Sequential_Number = Sequential_Number;
    }

    /**
     * 受診履歴情報（繰り返し　５） (例:  )
     *
     * @return the Medical_List_Information
     */
    public MedicalListInformation[] getMedical_List_Information() {
        return Medical_List_Information;
    }

    /**
     * 受診履歴情報（繰り返し　５） (例:  )
     *
     * @param Medical_List_Information the Medical_List_Information to set
     */
    public void setMedical_List_Information(MedicalListInformation[] Medical_List_Information) {
        this.Medical_List_Information = Medical_List_Information;
    }
}