package open.dolphin.orca.orcaapi.bean;

/**
 * appointreq.
 * @author pns
 */
public class Appointreq {
    /**
     * 患者番号 (例: 00012)
     */
    private String Patient_ID;

    /**
     * 予約氏名(患者氏名) (例: 日医　太郎)
     */
    private String WholeName;

    /**
     * カナ氏名 (例: ニチイ　タロウ)
     */
    private String WholeName_inKana;

    /**
     * 予約日 (例: 2014-07-02)
     */
    private String Appointment_Date;

    /**
     * 予約時間 (例: 12:10:00)
     */
    private String Appointment_Time;

    /**
     * 予約ID (例:  )
     */
    private String Appointment_Id;

    /**
     * 診療科コード※４(01:内科) (例: 01)
     */
    private String Department_Code;

    /**
     * ドクターコード (例: 10001)
     */
    private String Physician_Code;

    /**
     * 診療内容区分※５(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断 、07:予防注射、99:該当なし) (例: 01)
     */
    private String Medical_Information;

    /**
     * 予約内容区分(01:患者による予約、02:医師による予約) (例: 01)
     */
    private String Appointment_Information;

    /**
     * 予約メモ内容 (例: 予約めもです)
     */
    private String Appointment_Note;

    /**
     * 患者番号 (例: 00012)
     * @return the Patient_ID
     */
    public String getPatient_ID() {
        return Patient_ID;
    }

    /**
     * 患者番号 (例: 00012)
     * @param Patient_ID the Patient_ID to set
     */
    public void setPatient_ID(String Patient_ID) {
        this.Patient_ID = Patient_ID;
    }

    /**
     * 予約氏名(患者氏名) (例: 日医　太郎)
     * @return the WholeName
     */
    public String getWholeName() {
        return WholeName;
    }

    /**
     * 予約氏名(患者氏名) (例: 日医　太郎)
     * @param WholeName the WholeName to set
     */
    public void setWholeName(String WholeName) {
        this.WholeName = WholeName;
    }

    /**
     * カナ氏名 (例: ニチイ　タロウ)
     * @return the WholeName_inKana
     */
    public String getWholeName_inKana() {
        return WholeName_inKana;
    }

    /**
     * カナ氏名 (例: ニチイ　タロウ)
     * @param WholeName_inKana the WholeName_inKana to set
     */
    public void setWholeName_inKana(String WholeName_inKana) {
        this.WholeName_inKana = WholeName_inKana;
    }

    /**
     * 予約日 (例: 2014-07-02)
     * @return the Appointment_Date
     */
    public String getAppointment_Date() {
        return Appointment_Date;
    }

    /**
     * 予約日 (例: 2014-07-02)
     * @param Appointment_Date the Appointment_Date to set
     */
    public void setAppointment_Date(String Appointment_Date) {
        this.Appointment_Date = Appointment_Date;
    }

    /**
     * 予約時間 (例: 12:10:00)
     * @return the Appointment_Time
     */
    public String getAppointment_Time() {
        return Appointment_Time;
    }

    /**
     * 予約時間 (例: 12:10:00)
     * @param Appointment_Time the Appointment_Time to set
     */
    public void setAppointment_Time(String Appointment_Time) {
        this.Appointment_Time = Appointment_Time;
    }

    /**
     * 予約ID (例:  )
     * @return the Appointment_Id
     */
    public String getAppointment_Id() {
        return Appointment_Id;
    }

    /**
     * 予約ID (例:  )
     * @param Appointment_Id the Appointment_Id to set
     */
    public void setAppointment_Id(String Appointment_Id) {
        this.Appointment_Id = Appointment_Id;
    }

    /**
     * 診療科コード※４(01:内科) (例: 01)
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 診療科コード※４(01:内科) (例: 01)
     * @param Department_Code the Department_Code to set
     */
    public void setDepartment_Code(String Department_Code) {
        this.Department_Code = Department_Code;
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
     * 診療内容区分※５(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断 、07:予防注射、99:該当なし) (例: 01)
     * @return the Medical_Information
     */
    public String getMedical_Information() {
        return Medical_Information;
    }

    /**
     * 診療内容区分※５(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断 、07:予防注射、99:該当なし) (例: 01)
     * @param Medical_Information the Medical_Information to set
     */
    public void setMedical_Information(String Medical_Information) {
        this.Medical_Information = Medical_Information;
    }

    /**
     * 予約内容区分(01:患者による予約、02:医師による予約) (例: 01)
     * @return the Appointment_Information
     */
    public String getAppointment_Information() {
        return Appointment_Information;
    }

    /**
     * 予約内容区分(01:患者による予約、02:医師による予約) (例: 01)
     * @param Appointment_Information the Appointment_Information to set
     */
    public void setAppointment_Information(String Appointment_Information) {
        this.Appointment_Information = Appointment_Information;
    }

    /**
     * 予約メモ内容 (例: 予約めもです)
     * @return the Appointment_Note
     */
    public String getAppointment_Note() {
        return Appointment_Note;
    }

    /**
     * 予約メモ内容 (例: 予約めもです)
     * @param Appointment_Note the Appointment_Note to set
     */
    public void setAppointment_Note(String Appointment_Note) {
        this.Appointment_Note = Appointment_Note;
    }
}