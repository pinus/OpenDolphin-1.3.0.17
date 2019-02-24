package open.dolphin.orca.pushapi.bean;

/**
 * body. 明細
 * @author pns
 */
public class Body {
    /**
     * 患者登録更新モード（add：登録、modify：更新、delete：取消） (例: add)
     */
    private String Patient_Mode;

    /**
     * 患者番号 (例: 00198)
     */
    private String Patient_ID;

    /**
     * 受付年月日 (例: 2016-12-02)
     */
    private String Accept_Date;

    /**
     * 受付時間 (例: 16:03:38)
     */
    private String Accept_Time;

    /**
     * 受付ID (例: 00003)
     */
    private String Accept_Id;

    /**
     * 診療科コード (例: 01)
     */
    private String Department_Code;

    /**
     * ドクターコード (例: 10001)
     */
    private String Physician_Code;

    /**
     * 保険組合せ番号 (例: 0010)
     */
    private String Insurance_Combination_Number;

    /**
     * 登録(更新)日 (例: 2017-07-07)
     */
    private String Information_Date;

    /**
     * 登録(更新)時間 (例: 11:31:46)
     */
    private String Information_Time;

    /**
     * 診療年月日 (例: 2017-07-10)
     */
    private String Perform_Date;

    /**
     * (繰り返し　１５) (例:  )
     */
    private MedicalInformation[] Medical_Information;

    /**
     * 患者登録更新モード（add：登録、modify：更新、delete：取消） (例: add)
     * @return the Patient_Mode
     */
    public String getPatient_Mode() {
        return Patient_Mode;
    }

    /**
     * 患者登録更新モード（add：登録、modify：更新、delete：取消） (例: add)
     * @param Patient_Mode the Patient_Mode to set
     */
    public void setPatient_Mode(String Patient_Mode) {
        this.Patient_Mode = Patient_Mode;
    }

    /**
     * 患者番号 (例: 00198)
     * @return the Patient_ID
     */
    public String getPatient_ID() {
        return Patient_ID;
    }

    /**
     * 患者番号 (例: 00198)
     * @param Patient_ID the Patient_ID to set
     */
    public void setPatient_ID(String Patient_ID) {
        this.Patient_ID = Patient_ID;
    }

    /**
     * 受付年月日 (例: 2016-12-02)
     * @return the Accept_Date
     */
    public String getAccept_Date() {
        return Accept_Date;
    }

    /**
     * 受付年月日 (例: 2016-12-02)
     * @param Accept_Date the Accept_Date to set
     */
    public void setAccept_Date(String Accept_Date) {
        this.Accept_Date = Accept_Date;
    }

    /**
     * 受付時間 (例: 16:03:38)
     * @return the Accept_Time
     */
    public String getAccept_Time() {
        return Accept_Time;
    }

    /**
     * 受付時間 (例: 16:03:38)
     * @param Accept_Time the Accept_Time to set
     */
    public void setAccept_Time(String Accept_Time) {
        this.Accept_Time = Accept_Time;
    }

    /**
     * 受付ID (例: 00003)
     * @return the Accept_Id
     */
    public String getAccept_Id() {
        return Accept_Id;
    }

    /**
     * 受付ID (例: 00003)
     * @param Accept_Id the Accept_Id to set
     */
    public void setAccept_Id(String Accept_Id) {
        this.Accept_Id = Accept_Id;
    }

    /**
     * 診療科コード (例: 01)
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 診療科コード (例: 01)
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
     * 保険組合せ番号 (例: 0010)
     * @return the Insurance_Combination_Number
     */
    public String getInsurance_Combination_Number() {
        return Insurance_Combination_Number;
    }

    /**
     * 保険組合せ番号 (例: 0010)
     * @param Insurance_Combination_Number the Insurance_Combination_Number to set
     */
    public void setInsurance_Combination_Number(String Insurance_Combination_Number) {
        this.Insurance_Combination_Number = Insurance_Combination_Number;
    }

    /**
     * 登録(更新)日 (例: 2017-07-07)
     * @return the Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * 登録(更新)日 (例: 2017-07-07)
     * @param Information_Date the Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * 登録(更新)時間 (例: 11:31:46)
     * @return the Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * 登録(更新)時間 (例: 11:31:46)
     * @param Information_Time the Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * 診療年月日 (例: 2017-07-10)
     * @return the Perform_Date
     */
    public String getPerform_Date() {
        return Perform_Date;
    }

    /**
     * 診療年月日 (例: 2017-07-10)
     * @param Perform_Date the Perform_Date to set
     */
    public void setPerform_Date(String Perform_Date) {
        this.Perform_Date = Perform_Date;
    }

    /**
     * (繰り返し　１５) (例:  )
     * @return the Medical_Information
     */
    public MedicalInformation[] getMedical_Information() {
        return Medical_Information;
    }

    /**
     * (繰り返し　１５) (例:  )
     * @param Medical_Information the Medical_Information to set
     */
    public void setMedical_Information(MedicalInformation[] Medical_Information) {
        this.Medical_Information = Medical_Information;
    }
}
