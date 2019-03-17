package open.dolphin.orca.orcaapi.bean;

/**
 * Visit_List_Information. 来院一覧情報(繰り返し500)
 *
 * @author pns
 */
public class VisitListInformation {
    /**
     * 患者情報 (例:  )
     */
    private PatientInformation Patient_Information;

    /**
     * 診療科コード　※１(01:内科) (例: 01)
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
     * 伝票番号 (例: 0020672)
     */
    private String Voucher_Number;

    /**
     * 連番 (例: 1)
     */
    private String Sequential_Number;

    /**
     * 保険組合せ番号 (例: 0003)
     */
    private String Insurance_Combination_Number;

    /**
     * 保険組合せ情報 (例:  )
     */
    private HealthInsuranceInformation HealthInsurance_Information;

    /**
     * 更新日付 (例:  )
     */
    private String Update_Date;

    /**
     * 更新時間 (例:  )
     */
    private String Update_Time;

    /**
     * 患者情報更新日 (例:  )
     */
    private String Patient_Update_Date;

    /**
     * 患者情報更新時間 (例:  )
     */
    private String Patient_Update_Time;

    /**
     * 来院日カレンダー (例: 0000000100000001000000000000000)
     */
    private String Visit_Calendar;

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
     * 診療科コード　※１(01:内科) (例: 01)
     *
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 診療科コード　※１(01:内科) (例: 01)
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
     * ドクターコード (例: 10001)
     *
     * @return the Physician_Code
     */
    public String getPhysician_Code() {
        return Physician_Code;
    }

    /**
     * ドクターコード (例: 10001)
     *
     * @param Physician_Code the Physician_Code to set
     */
    public void setPhysician_Code(String Physician_Code) {
        this.Physician_Code = Physician_Code;
    }

    /**
     * ドクター名 (例: 日本　一)
     *
     * @return the Physician_WholeName
     */
    public String getPhysician_WholeName() {
        return Physician_WholeName;
    }

    /**
     * ドクター名 (例: 日本　一)
     *
     * @param Physician_WholeName the Physician_WholeName to set
     */
    public void setPhysician_WholeName(String Physician_WholeName) {
        this.Physician_WholeName = Physician_WholeName;
    }

    /**
     * 伝票番号 (例: 0020672)
     *
     * @return the Voucher_Number
     */
    public String getVoucher_Number() {
        return Voucher_Number;
    }

    /**
     * 伝票番号 (例: 0020672)
     *
     * @param Voucher_Number the Voucher_Number to set
     */
    public void setVoucher_Number(String Voucher_Number) {
        this.Voucher_Number = Voucher_Number;
    }

    /**
     * 連番 (例: 1)
     *
     * @return the Sequential_Number
     */
    public String getSequential_Number() {
        return Sequential_Number;
    }

    /**
     * 連番 (例: 1)
     *
     * @param Sequential_Number the Sequential_Number to set
     */
    public void setSequential_Number(String Sequential_Number) {
        this.Sequential_Number = Sequential_Number;
    }

    /**
     * 保険組合せ番号 (例: 0003)
     *
     * @return the Insurance_Combination_Number
     */
    public String getInsurance_Combination_Number() {
        return Insurance_Combination_Number;
    }

    /**
     * 保険組合せ番号 (例: 0003)
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
     * 更新日付 (例:  )
     *
     * @return the Update_Date
     */
    public String getUpdate_Date() {
        return Update_Date;
    }

    /**
     * 更新日付 (例:  )
     *
     * @param Update_Date the Update_Date to set
     */
    public void setUpdate_Date(String Update_Date) {
        this.Update_Date = Update_Date;
    }

    /**
     * 更新時間 (例:  )
     *
     * @return the Update_Time
     */
    public String getUpdate_Time() {
        return Update_Time;
    }

    /**
     * 更新時間 (例:  )
     *
     * @param Update_Time the Update_Time to set
     */
    public void setUpdate_Time(String Update_Time) {
        this.Update_Time = Update_Time;
    }

    /**
     * 患者情報更新日 (例:  )
     *
     * @return the Patient_Update_Date
     */
    public String getPatient_Update_Date() {
        return Patient_Update_Date;
    }

    /**
     * 患者情報更新日 (例:  )
     *
     * @param Patient_Update_Date the Patient_Update_Date to set
     */
    public void setPatient_Update_Date(String Patient_Update_Date) {
        this.Patient_Update_Date = Patient_Update_Date;
    }

    /**
     * 患者情報更新時間 (例:  )
     *
     * @return the Patient_Update_Time
     */
    public String getPatient_Update_Time() {
        return Patient_Update_Time;
    }

    /**
     * 患者情報更新時間 (例:  )
     *
     * @param Patient_Update_Time the Patient_Update_Time to set
     */
    public void setPatient_Update_Time(String Patient_Update_Time) {
        this.Patient_Update_Time = Patient_Update_Time;
    }

    /**
     * 来院日カレンダー (例: 0000000100000001000000000000000)
     *
     * @return the Visit_Calendar
     */
    public String getVisit_Calendar() {
        return Visit_Calendar;
    }

    /**
     * 来院日カレンダー (例: 0000000100000001000000000000000)
     *
     * @param Visit_Calendar the Visit_Calendar to set
     */
    public void setVisit_Calendar(String Visit_Calendar) {
        this.Visit_Calendar = Visit_Calendar;
    }
}