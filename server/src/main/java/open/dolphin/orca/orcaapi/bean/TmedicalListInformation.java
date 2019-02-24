package open.dolphin.orca.orcaapi.bean;

/**
 * Tmedical_List_Information. 中途データ一覧情報（繰り返し５００）
 * @author pns
 */
public class TmedicalListInformation {
    /**
     * 患者情報 (例:  )
     */
    private PatientInformation Patient_Information;

    /**
     * 診療科コード（01:内科） (例: 01)
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
     * 保険組合せ番号 (例: 0002)
     */
    private String Insurance_Combination_Number;

    /**
     * 保険組合せ情報 (例:  )
     */
    private HealthInsuranceInformation HealthInsurance_Information;

    /**
     * 同日再入院区分(1:同日再入院分) (例:  )
     */
    private String Readmission_Day_In;

    /**
     * API登録のUID (例: 52e4f8aa-2b3d-11e3-83c8-8c736e794c62)
     */
    private String Medical_Uid;

    /**
     * 登録時間 (例: 17:33:33)
     */
    private String Medical_Time;

    /**
     * 展開区分（1:展開中、0:以外） (例: 0)
     */
    private String Medical_Mode;

    /**
     * 登録区分（1:中途終了登録分、0:以外） (例: 0)
     */
    private String Medical_Mode2;

    /**
     * 患者情報 (例:  )
     * @return the Patient_Information
     */
    public PatientInformation getPatient_Information() {
        return Patient_Information;
    }

    /**
     * 患者情報 (例:  )
     * @param Patient_Information the Patient_Information to set
     */
    public void setPatient_Information(PatientInformation Patient_Information) {
        this.Patient_Information = Patient_Information;
    }

    /**
     * 診療科コード（01:内科） (例: 01)
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 診療科コード（01:内科） (例: 01)
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
     * 保険組合せ番号 (例: 0002)
     * @return the Insurance_Combination_Number
     */
    public String getInsurance_Combination_Number() {
        return Insurance_Combination_Number;
    }

    /**
     * 保険組合せ番号 (例: 0002)
     * @param Insurance_Combination_Number the Insurance_Combination_Number to set
     */
    public void setInsurance_Combination_Number(String Insurance_Combination_Number) {
        this.Insurance_Combination_Number = Insurance_Combination_Number;
    }

    /**
     * 保険組合せ情報 (例:  )
     * @return the HealthInsurance_Information
     */
    public HealthInsuranceInformation getHealthInsurance_Information() {
        return HealthInsurance_Information;
    }

    /**
     * 保険組合せ情報 (例:  )
     * @param HealthInsurance_Information the HealthInsurance_Information to set
     */
    public void setHealthInsurance_Information(HealthInsuranceInformation HealthInsurance_Information) {
        this.HealthInsurance_Information = HealthInsurance_Information;
    }

    /**
     * 同日再入院区分(1:同日再入院分) (例:  )
     * @return the Readmission_Day_In
     */
    public String getReadmission_Day_In() {
        return Readmission_Day_In;
    }

    /**
     * 同日再入院区分(1:同日再入院分) (例:  )
     * @param Readmission_Day_In the Readmission_Day_In to set
     */
    public void setReadmission_Day_In(String Readmission_Day_In) {
        this.Readmission_Day_In = Readmission_Day_In;
    }

    /**
     * API登録のUID (例: 52e4f8aa-2b3d-11e3-83c8-8c736e794c62)
     * @return the Medical_Uid
     */
    public String getMedical_Uid() {
        return Medical_Uid;
    }

    /**
     * API登録のUID (例: 52e4f8aa-2b3d-11e3-83c8-8c736e794c62)
     * @param Medical_Uid the Medical_Uid to set
     */
    public void setMedical_Uid(String Medical_Uid) {
        this.Medical_Uid = Medical_Uid;
    }

    /**
     * 登録時間 (例: 17:33:33)
     * @return the Medical_Time
     */
    public String getMedical_Time() {
        return Medical_Time;
    }

    /**
     * 登録時間 (例: 17:33:33)
     * @param Medical_Time the Medical_Time to set
     */
    public void setMedical_Time(String Medical_Time) {
        this.Medical_Time = Medical_Time;
    }

    /**
     * 展開区分（1:展開中、0:以外） (例: 0)
     * @return the Medical_Mode
     */
    public String getMedical_Mode() {
        return Medical_Mode;
    }

    /**
     * 展開区分（1:展開中、0:以外） (例: 0)
     * @param Medical_Mode the Medical_Mode to set
     */
    public void setMedical_Mode(String Medical_Mode) {
        this.Medical_Mode = Medical_Mode;
    }

    /**
     * 登録区分（1:中途終了登録分、0:以外） (例: 0)
     * @return the Medical_Mode2
     */
    public String getMedical_Mode2() {
        return Medical_Mode2;
    }

    /**
     * 登録区分（1:中途終了登録分、0:以外） (例: 0)
     * @param Medical_Mode2 the Medical_Mode2 to set
     */
    public void setMedical_Mode2(String Medical_Mode2) {
        this.Medical_Mode2 = Medical_Mode2;
    }
}