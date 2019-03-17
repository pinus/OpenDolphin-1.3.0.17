package open.dolphin.orca.orcaapi.bean;

/**
 * Appointlst_Infomation. 予約情報(繰り返し500)
 *
 * @author pns
 */
public class AppointlstInformation {
    /**
     * 予約日 (例: 2012-12-22)
     */
    private String Appointment_Date;

    /**
     * 予約時間 (例: 15:30:00)
     */
    private String Appointment_Time;

    /**
     * 診療内容区分※３(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断、07:予防注射、99:該当なし) (例: 01)
     */
    private String Medical_Information;

    /**
     * 診療内容名称 (例: 診察１)
     */
    private String Medical_Information_WholeName;

    /**
     * 予約診療科コード※４(01:内科) (例: 01)
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
     * 来院情報(1:来院済) (例:  )
     */
    private String Visit_Information;

    /**
     * 予約ID (例: 02)
     */
    private String Appointment_Id;

    /**
     * 患者情報 (例:  )
     */
    private PatientInformation Patient_Information;

    /**
     * 予約メモ内容 (例: 予約メモテスト)
     */
    private String Appointment_Note;

    /**
     * 予約日 (例: 2012-12-22)
     *
     * @return the Appointment_Date
     */
    public String getAppointment_Date() {
        return Appointment_Date;
    }

    /**
     * 予約日 (例: 2012-12-22)
     *
     * @param Appointment_Date the Appointment_Date to set
     */
    public void setAppointment_Date(String Appointment_Date) {
        this.Appointment_Date = Appointment_Date;
    }

    /**
     * 予約時間 (例: 15:30:00)
     *
     * @return the Appointment_Time
     */
    public String getAppointment_Time() {
        return Appointment_Time;
    }

    /**
     * 予約時間 (例: 15:30:00)
     *
     * @param Appointment_Time the Appointment_Time to set
     */
    public void setAppointment_Time(String Appointment_Time) {
        this.Appointment_Time = Appointment_Time;
    }

    /**
     * 診療内容区分※３(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断、07:予防注射、99:該当なし) (例: 01)
     *
     * @return the Medical_Information
     */
    public String getMedical_Information() {
        return Medical_Information;
    }

    /**
     * 診療内容区分※３(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断、07:予防注射、99:該当なし) (例: 01)
     *
     * @param Medical_Information the Medical_Information to set
     */
    public void setMedical_Information(String Medical_Information) {
        this.Medical_Information = Medical_Information;
    }

    /**
     * 診療内容名称 (例: 診察１)
     *
     * @return the Medical_Information_WholeName
     */
    public String getMedical_Information_WholeName() {
        return Medical_Information_WholeName;
    }

    /**
     * 診療内容名称 (例: 診察１)
     *
     * @param Medical_Information_WholeName the Medical_Information_WholeName to set
     */
    public void setMedical_Information_WholeName(String Medical_Information_WholeName) {
        this.Medical_Information_WholeName = Medical_Information_WholeName;
    }

    /**
     * 予約診療科コード※４(01:内科) (例: 01)
     *
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 予約診療科コード※４(01:内科) (例: 01)
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
     * 来院情報(1:来院済) (例:  )
     *
     * @return the Visit_Information
     */
    public String getVisit_Information() {
        return Visit_Information;
    }

    /**
     * 来院情報(1:来院済) (例:  )
     *
     * @param Visit_Information the Visit_Information to set
     */
    public void setVisit_Information(String Visit_Information) {
        this.Visit_Information = Visit_Information;
    }

    /**
     * 予約ID (例: 02)
     *
     * @return the Appointment_Id
     */
    public String getAppointment_Id() {
        return Appointment_Id;
    }

    /**
     * 予約ID (例: 02)
     *
     * @param Appointment_Id the Appointment_Id to set
     */
    public void setAppointment_Id(String Appointment_Id) {
        this.Appointment_Id = Appointment_Id;
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
     * 予約メモ内容 (例: 予約メモテスト)
     *
     * @return the Appointment_Note
     */
    public String getAppointment_Note() {
        return Appointment_Note;
    }

    /**
     * 予約メモ内容 (例: 予約メモテスト)
     *
     * @param Appointment_Note the Appointment_Note to set
     */
    public void setAppointment_Note(String Appointment_Note) {
        this.Appointment_Note = Appointment_Note;
    }
}