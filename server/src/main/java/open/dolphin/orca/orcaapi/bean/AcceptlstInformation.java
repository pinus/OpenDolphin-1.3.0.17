package open.dolphin.orca.orcaapi.bean;

/**
 * Acceptlst_Infomation. 受付一覧情報(繰り返し500)
 *
 * @author pns
 */
public class AcceptlstInformation {
    /**
     * 受付時間 (例: 15:30:00)
     */
    private String Acceptance_Time;

    /**
     * 受付ID (例: 00001)
     */
    private String Acceptance_Id;

    /**
     * 診療科コード※４(01:内科) (例: 01)
     */
    private String Department_Code;

    /**
     * 診療科名称 (例: 内科)
     */
    private String Department_WholeName;

    /**
     * ドクターコード (例: 10001)
     */
    private String Physician_Code;

    /**
     * ドクター名 (例: 日本　一)
     */
    private String Physician_WholeName;

    /**
     * 診療内容区分※５(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断、07:予防注射、99:該当なし) (例: 01)
     */
    private String Medical_Information;

    /**
     * claim情報 (例: 0)
     */
    private String Claim_Infometion;

    /**
     * 会計時間※６ (例: 15:50:00)
     */
    private String Account_Time;

    /**
     * 予約時間 (例: 11:00:00)
     */
    private String Appointment_Time;

    /**
     * 予約ID (例: 02)
     */
    private String Appointment_Id;

    /**
     * 患者基本情報 (例:  )
     */
    private PatientInformation Patient_Information;

    /**
     * 保険組合せ情報 (例:  )
     */
    private HealthInsuranceInformation HealthInsurance_Information;

    /**
     * メモ登録情報 (例:  )
     */
    private PatientMemoInformation Patient_Memo_Information;

    /**
     * 並び順情報 (例:  )
     */
    private DisplayOrder Display_Order;

    /**
     * 受付時間 (例: 15:30:00)
     *
     * @return the Acceptance_Time
     */
    public String getAcceptance_Time() {
        return Acceptance_Time;
    }

    /**
     * 受付時間 (例: 15:30:00)
     *
     * @param Acceptance_Time the Acceptance_Time to set
     */
    public void setAcceptance_Time(String Acceptance_Time) {
        this.Acceptance_Time = Acceptance_Time;
    }

    /**
     * 受付ID (例: 00001)
     *
     * @return the Acceptance_Id
     */
    public String getAcceptance_Id() {
        return Acceptance_Id;
    }

    /**
     * 受付ID (例: 00001)
     *
     * @param Acceptance_Id the Acceptance_Id to set
     */
    public void setAcceptance_Id(String Acceptance_Id) {
        this.Acceptance_Id = Acceptance_Id;
    }

    /**
     * 診療科コード※４(01:内科) (例: 01)
     *
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 診療科コード※４(01:内科) (例: 01)
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
     * 診療内容区分※５(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断 、07:予防注射、99:該当なし) (例: 01)
     *
     * @return the Medical_Information
     */
    public String getMedical_Information() {
        return Medical_Information;
    }

    /**
     * 診療内容区分※５(01:診察１、02:薬のみ、03:注射のみ、04:検査のみ、05:リハビリテーション、06:健康診断 、07:予防注射、99:該当なし) (例: 01)
     *
     * @param Medical_Information the Medical_Information to set
     */
    public void setMedical_Information(String Medical_Information) {
        this.Medical_Information = Medical_Information;
    }

    /**
     * claim情報 (例: 0) スペルおかしい
     *
     * @return the Claim_Infometion
     */
    public String getClaim_Infometion() {
        return Claim_Infometion;
    }

    /**
     * claim情報 (例: 0) スペルおかしい
     *
     * @param Claim_Infometion the Claim_Infometion to set
     */
    public void setClaim_Infometion(String Claim_Infometion) {
        this.Claim_Infometion = Claim_Infometion;
    }

    /**
     * 会計時間 (例: 15:50:00)
     *
     * @return the Account_Time
     */
    public String getAccount_Time() {
        return Account_Time;
    }

    /**
     * 会計時間 (例: 15:50:00)
     *
     * @param Account_Time the Account_Time to set
     */
    public void setAccount_Time(String Account_Time) {
        this.Account_Time = Account_Time;
    }

    /**
     * 予約時間 (例: 11:00:00)
     *
     * @return the Appointment_Time
     */
    public String getAppointment_Time() {
        return Appointment_Time;
    }

    /**
     * 予約時間 (例: 11:00:00)
     *
     * @param Appointment_Time the Appointment_Time to set
     */
    public void setAppointment_Time(String Appointment_Time) {
        this.Appointment_Time = Appointment_Time;
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
     * Patient_Memo_Information
     *
     * @return Patient_Memo_Information
     */
    public PatientMemoInformation getPatient_Memo_Information() {
        return Patient_Memo_Information;
    }

    /**
     * Patient_Memo_Information
     *
     * @param Patient_Memo_Information to set
     */
    public void setPatient_Memo_Information(PatientMemoInformation Patient_Memo_Information) {
        this.Patient_Memo_Information = Patient_Memo_Information;
    }

    /**
     * Display_Order
     *
     * @return Display_Order
     */
    public DisplayOrder getDisplay_Order() {
        return Display_Order;
    }

    /**
     * Display_Order
     *
     * @param Display_Order to set
     */
    public void setDisplay_Order(DisplayOrder Display_Order) {
        this.Display_Order = Display_Order;
    }
}
