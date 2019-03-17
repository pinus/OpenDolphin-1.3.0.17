package open.dolphin.orca.orcaapi.bean;

/**
 * appointres.
 *
 * @author pns
 */
public class Appointres {
    /**
     * 実施日 (例: 2014-07-04)
     */
    private String Information_Date;

    /**
     * 実施時間 (例: 11:07:20)
     */
    private String Information_Time;

    /**
     * 結果コード（ゼロ以外エラー） (例: K3)
     */
    private String Api_Result;

    /**
     * エラーメッセージ (例: 予約登録終了)
     */
    private String Api_Result_Message;

    /**
     * 警告メッセージ情報（繰り返し　５） (例:  )
     */
    private ApiWarningMessageInformation[] Api_Warning_Message_Information;

    /**
     *   (例: PatientInfo)
     */
    private String Reskey;

    /**
     * 予約日 (例: 2014-07-02)
     */
    private String Appointment_Date;

    /**
     * 予約時間 (例: 12:10:00)
     */
    private String Appointment_Time;

    /**
     * 予約ID (例: 00001)
     */
    private String Appointment_Id;

    /**
     * 予約診療科コード※７(01:内科) (例: 01)
     */
    private String Department_Code;

    /**
     * 予約診療科名称 (例: 内科)
     */
    private String Department_WholeName;

    /**
     * 予約ドクタコード (例: 10001)
     */
    private String Physician_Code;

    /**
     * 予約ドクター名 (例: 日本　一)
     */
    private String Physician_WholeName;

    /**
     * 診療内容区分※８(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断 、07:予防注射、99:該当なし) (例: 01)
     */
    private String Medical_Information;

    /**
     * 予約内容区分(01:患者による予約、02:医師による予約) (例: 00)
     */
    private String Appointment_Information;

    /**
     * 予約メモ内容 (例: 予約めもです)
     */
    private String Appointment_Note;

    /**
     * 患者基本情報 (例:  )
     */
    private PatientInformation Patient_Information;

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
     * 実施時間 (例: 11:07:20)
     *
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 実施時間 (例: 11:07:20)
     *
     * @param Information_Time the Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * 結果コード（ゼロ以外エラー） (例: K3)
     *
     * @return the Api_Result
     */
    public String getApi_Result() {
        return Api_Result;
    }

    /**
     * 結果コード（ゼロ以外エラー） (例: K3)
     *
     * @param Api_Result the Api_Result to set
     */
    public void setApi_Result(String Api_Result) {
        this.Api_Result = Api_Result;
    }

    /**
     * エラーメッセージ (例: 予約登録終了)
     *
     * @return the Api_Result_Message
     */
    public String getApi_Result_Message() {
        return Api_Result_Message;
    }

    /**
     * エラーメッセージ (例: 予約登録終了)
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
     *   (例: PatientInfo)
     *
     * @return the Reskey
     */
    public String getReskey() {
        return Reskey;
    }

    /**
     *   (例: PatientInfo)
     *
     * @param Reskey the Reskey to set
     */
    public void setReskey(String Reskey) {
        this.Reskey = Reskey;
    }

    /**
     * 予約日 (例: 2014-07-02)
     *
     * @return the Appointment_Date
     */
    public String getAppointment_Date() {
        return Appointment_Date;
    }

    /**
     * 予約日 (例: 2014-07-02)
     *
     * @param Appointment_Date the Appointment_Date to set
     */
    public void setAppointment_Date(String Appointment_Date) {
        this.Appointment_Date = Appointment_Date;
    }

    /**
     * 予約時間 (例: 12:10:00)
     *
     * @return the Appointment_Time
     */
    public String getAppointment_Time() {
        return Appointment_Time;
    }

    /**
     * 予約時間 (例: 12:10:00)
     *
     * @param Appointment_Time the Appointment_Time to set
     */
    public void setAppointment_Time(String Appointment_Time) {
        this.Appointment_Time = Appointment_Time;
    }

    /**
     * 予約ID (例: 00001)
     *
     * @return the Appointment_Id
     */
    public String getAppointment_Id() {
        return Appointment_Id;
    }

    /**
     * 予約ID (例: 00001)
     *
     * @param Appointment_Id the Appointment_Id to set
     */
    public void setAppointment_Id(String Appointment_Id) {
        this.Appointment_Id = Appointment_Id;
    }

    /**
     * 予約診療科コード※７(01:内科) (例: 01)
     *
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 予約診療科コード※７(01:内科) (例: 01)
     *
     * @param Department_Code the Department_Code to set
     */
    public void setDepartment_Code(String Department_Code) {
        this.Department_Code = Department_Code;
    }

    /**
     * 予約診療科名称 (例: 内科)
     *
     * @return the Department_WholeName
     */
    public String getDepartment_WholeName() {
        return Department_WholeName;
    }

    /**
     * 予約診療科名称 (例: 内科)
     *
     * @param Department_WholeName the Department_WholeName to set
     */
    public void setDepartment_WholeName(String Department_WholeName) {
        this.Department_WholeName = Department_WholeName;
    }

    /**
     * 予約ドクタコード (例: 10001)
     *
     * @return the Physician_Code
     */
    public String getPhysician_Code() {
        return Physician_Code;
    }

    /**
     * 予約ドクタコード (例: 10001)
     *
     * @param Physician_Code the Physician_Code to set
     */
    public void setPhysician_Code(String Physician_Code) {
        this.Physician_Code = Physician_Code;
    }

    /**
     * 予約ドクター名 (例: 日本　一)
     *
     * @return the Physician_WholeName
     */
    public String getPhysician_WholeName() {
        return Physician_WholeName;
    }

    /**
     * 予約ドクター名 (例: 日本　一)
     *
     * @param Physician_WholeName the Physician_WholeName to set
     */
    public void setPhysician_WholeName(String Physician_WholeName) {
        this.Physician_WholeName = Physician_WholeName;
    }

    /**
     * 診療内容区分※８(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断 、07:予防注射、99:該当なし) (例: 01)
     *
     * @return the Medical_Information
     */
    public String getMedical_Information() {
        return Medical_Information;
    }

    /**
     * 診療内容区分※８(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断 、07:予防注射、99:該当なし) (例: 01)
     *
     * @param Medical_Information the Medical_Information to set
     */
    public void setMedical_Information(String Medical_Information) {
        this.Medical_Information = Medical_Information;
    }

    /**
     * 予約内容区分(01:患者による予約、02:医師による予約) (例: 00)
     *
     * @return the Appointment_Information
     */
    public String getAppointment_Information() {
        return Appointment_Information;
    }

    /**
     * 予約内容区分(01:患者による予約、02:医師による予約) (例: 00)
     *
     * @param Appointment_Information the Appointment_Information to set
     */
    public void setAppointment_Information(String Appointment_Information) {
        this.Appointment_Information = Appointment_Information;
    }

    /**
     * 予約メモ内容 (例: 予約めもです)
     *
     * @return the Appointment_Note
     */
    public String getAppointment_Note() {
        return Appointment_Note;
    }

    /**
     * 予約メモ内容 (例: 予約めもです)
     *
     * @param Appointment_Note the Appointment_Note to set
     */
    public void setAppointment_Note(String Appointment_Note) {
        this.Appointment_Note = Appointment_Note;
    }

    /**
     * 患者基本情報 (例:  )
     *
     * @return the Patient_Information
     */
    public PatientInformation getPatient_Information() {
        return Patient_Information;
    }

    /**
     * 患者基本情報 (例:  )
     *
     * @param Patient_Information the Patient_Information to set
     */
    public void setPatient_Information(PatientInformation Patient_Information) {
        this.Patient_Information = Patient_Information;
    }
}