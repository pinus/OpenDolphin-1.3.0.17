package open.dolphin.orca.orcaapi.bean;

/**
 * medicalres.
 * @author pns
 */
public class Medicalres {
    /**
     * 実施日 (例: 2014-10-17)
     */
    private String Information_Date;

    /**
     * 実施時間 (例: 14:15:00)
     */
    private String Information_Time;

    /**
     * 結果コード (例: 00)
     */
    private String Api_Result;

    /**
     * 結果メッセージ (例: 登録処理終了)
     */
    private String Api_Result_Message;

    /**
     * レスポンスキー情報 (例: MedicalInfo)
     */
    private String Reskey;

    /**
     * 診療日 (例: 2014-10-17)
     */
    private String Perform_Date;

    /**
     * 診療時間 (例: 14:10:12)
     */
    private String Perform_Time;

    /**
     *   (例: 64d3e23a-40b5-4aa8-90d4-ab7fd48a2322 )
     */
    private String Medical_Uid;

    /**
     * 診療科コード※１(01:内科) (例: 01)
     */
    private String Department_Code;

    /**
     * 診療科名称 (例: 内科)
     */
    private String Department_Name;

    /**
     * ドクターコード (例: 10001)
     */
    private String Physician_Code;

    /**
     * ドクター名 (例: 日本　一)
     */
    private String Physician_WholeName;

    /**
     * 患者情報 (例:  )
     */
    private PatientInformation2 Patient_Information;

    /**
     * 診療行為登録結果 (例:  )
     */
    private MedicalMessageInformation Medical_Message_Information;

    /**
     * 病名登録結果 (例:  )
     */
    private DiseaseMessageInformation Disease_Message_Information;

    /**
     * 実施日 (例: 2014-10-17)
     * @return the Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * 実施日 (例: 2014-10-17)
     * @param Information_Date the Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * 実施時間 (例: 14:15:00)
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 実施時間 (例: 14:15:00)
     * @param Information_Time the Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * 結果コード (例: 00)
     * @return the Api_Result
     */
    public String getApi_Result() {
        return Api_Result;
    }

    /**
     * 結果コード (例: 00)
     * @param Api_Result the Api_Result to set
     */
    public void setApi_Result(String Api_Result) {
        this.Api_Result = Api_Result;
    }

    /**
     * 結果メッセージ (例: 登録処理終了)
     * @return the Api_Result_Message
     */
    public String getApi_Result_Message() {
        return Api_Result_Message;
    }

    /**
     * 結果メッセージ (例: 登録処理終了)
     * @param Api_Result_Message the Api_Result_Message to set
     */
    public void setApi_Result_Message(String Api_Result_Message) {
        this.Api_Result_Message = Api_Result_Message;
    }

    /**
     * レスポンスキー情報 (例: MedicalInfo)
     * @return the Reskey
     */
    public String getReskey() {
        return Reskey;
    }

    /**
     * レスポンスキー情報 (例: MedicalInfo)
     * @param Reskey the Reskey to set
     */
    public void setReskey(String Reskey) {
        this.Reskey = Reskey;
    }

    /**
     * 診療日 (例: 2014-10-17)
     * @return the Perform_Date
     */
    public String getPerform_Date() {
        return Perform_Date;
    }

    /**
     * 診療日 (例: 2014-10-17)
     * @param Perform_Date the Perform_Date to set
     */
    public void setPerform_Date(String Perform_Date) {
        this.Perform_Date = Perform_Date;
    }

    /**
     * 診療時間 (例: 14:10:12)
     * @return the Perform_Time
     */
    public String getPerform_Time() {
        return Perform_Time;
    }

    /**
     * 診療時間 (例: 14:10:12)
     * @param Perform_Time the Perform_Time to set
     */
    public void setPerform_Time(String Perform_Time) {
        this.Perform_Time = Perform_Time;
    }

    /**
     *   (例: 64d3e23a-40b5-4aa8-90d4-ab7fd48a2322 )
     * @return the Medical_Uid
     */
    public String getMedical_Uid() {
        return Medical_Uid;
    }

    /**
     *   (例: 64d3e23a-40b5-4aa8-90d4-ab7fd48a2322 )
     * @param Medical_Uid the Medical_Uid to set
     */
    public void setMedical_Uid(String Medical_Uid) {
        this.Medical_Uid = Medical_Uid;
    }

    /**
     * 診療科コード※１(01:内科) (例: 01)
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 診療科コード※１(01:内科) (例: 01)
     * @param Department_Code the Department_Code to set
     */
    public void setDepartment_Code(String Department_Code) {
        this.Department_Code = Department_Code;
    }

    /**
     * 診療科名称 (例: 内科)
     * @return the Department_Name
     */
    public String getDepartment_Name() {
        return Department_Name;
    }

    /**
     * 診療科名称 (例: 内科)
     * @param Department_Name the Department_Name to set
     */
    public void setDepartment_Name(String Department_Name) {
        this.Department_Name = Department_Name;
    }

    /**
     * ドクターコード (例: 10001)
     * @return the Physician_Code
     */
    public String getPhysician_Code() {
        return Physician_Code;
    }

    /**
     * ドクターコード (例: 10001)
     * @param Physician_Code the Physician_Code to set
     */
    public void setPhysician_Code(String Physician_Code) {
        this.Physician_Code = Physician_Code;
    }

    /**
     * ドクター名 (例: 日本　一)
     * @return the Physician_WholeName
     */
    public String getPhysician_WholeName() {
        return Physician_WholeName;
    }

    /**
     * ドクター名 (例: 日本　一)
     * @param Physician_WholeName the Physician_WholeName to set
     */
    public void setPhysician_WholeName(String Physician_WholeName) {
        this.Physician_WholeName = Physician_WholeName;
    }

    /**
     * 患者情報 (例:  )
     * @return the Patient_Information
     */
    public PatientInformation2 getPatient_Information() {
        return Patient_Information;
    }

    /**
     * 患者情報 (例:  )
     * @param Patient_Information the Patient_Information to set
     */
    public void setPatient_Information(PatientInformation2 Patient_Information) {
        this.Patient_Information = Patient_Information;
    }

    /**
     * 診療行為登録結果 (例:  )
     * @return the Medical_Message_Information
     */
    public MedicalMessageInformation getMedical_Message_Information() {
        return Medical_Message_Information;
    }

    /**
     * 診療行為登録結果 (例:  )
     * @param Medical_Message_Information the Medical_Message_Information to set
     */
    public void setMedical_Message_Information(MedicalMessageInformation Medical_Message_Information) {
        this.Medical_Message_Information = Medical_Message_Information;
    }

    /**
     * 病名登録結果 (例:  )
     * @return the Disease_Message_Information
     */
    public DiseaseMessageInformation getDisease_Message_Information() {
        return Disease_Message_Information;
    }

    /**
     * 病名登録結果 (例:  )
     * @param Disease_Message_Information the Disease_Message_Information to set
     */
    public void setDisease_Message_Information(DiseaseMessageInformation Disease_Message_Information) {
        this.Disease_Message_Information = Disease_Message_Information;
    }
}