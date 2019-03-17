package open.dolphin.orca.orcaapi.bean;

/**
 * medicalv2res3.
 *
 * @author pns
 */
public class Medicalv2res3 {
    /**
     * 処理日付システム日付 (例: )
     */
    private String Information_Date;

    /**
     * 処理時間システム時間 (例: )
     */
    private String Information_Time;

    /**
     * 処理区分※1 (例: )
     */
    private String Api_Result;

    /**
     * 処理メッセージ (例: )
     */
    private String Api_Result_Message;

    /**
     * (例: )
     */
    private String Reskey;

    /**
     * 患者情報 (例: )
     */
    private PatientInformation Patient_Information;

    /**
     * 初診算定日※2 (例: )
     */
    private String First_Calculation_Date;

    /**
     * 最終来院日※3 (例: )
     */
    private String LastVisit_Date;

    /**
     * 診療科送信内容 (例: )
     */
    private String Department_Code;

    /**
     * 診療科名称 (例: )
     */
    private String Department_Name;

    /**
     * 初回来院日※4 (例: )
     */
    private String FirstVisit_Date;

    /**
     * Information_Date
     *
     * @return Information_Date
     */
    public String getInformation_Date() {
        return Information_Date;
    }

    /**
     * Information_Date
     *
     * @param Information_Date to set
     */
    public void setInformation_Date(String Information_Date) {
        this.Information_Date = Information_Date;
    }

    /**
     * Information_Time
     *
     * @return Information_Time
     */
    public String getInformation_Time() {
        return Information_Time;
    }

    /**
     * Information_Time
     *
     * @param Information_Time to set
     */
    public void setInformation_Time(String Information_Time) {
        this.Information_Time = Information_Time;
    }

    /**
     * Api_Result
     *
     * @return Api_Result
     */
    public String getApi_Result() {
        return Api_Result;
    }

    /**
     * Api_Result
     *
     * @param Api_Result to set
     */
    public void setApi_Result(String Api_Result) {
        this.Api_Result = Api_Result;
    }

    /**
     * Api_Result_Message
     *
     * @return Api_Result_Message
     */
    public String getApi_Result_Message() {
        return Api_Result_Message;
    }

    /**
     * Api_Result_Message
     *
     * @param Api_Result_Message to set
     */
    public void setApi_Result_Message(String Api_Result_Message) {
        this.Api_Result_Message = Api_Result_Message;
    }

    /**
     * Reskey
     *
     * @return Reskey
     */
    public String getReskey() {
        return Reskey;
    }

    /**
     * Reskey
     *
     * @param Reskey to set
     */
    public void setReskey(String Reskey) {
        this.Reskey = Reskey;
    }

    /**
     * Patient_Information
     *
     * @return Patient_Information
     */
    public PatientInformation getPatient_Information() {
        return Patient_Information;
    }

    /**
     * Patient_Information
     *
     * @param Patient_Information to set
     */
    public void setPatient_Information(PatientInformation Patient_Information) {
        this.Patient_Information = Patient_Information;
    }

    /**
     * First_Calculation_Date
     *
     * @return First_Calculation_Date
     */
    public String getFirst_Calculation_Date() {
        return First_Calculation_Date;
    }

    /**
     * First_Calculation_Date
     *
     * @param First_Calculation_Date to set
     */
    public void setFirst_Calculation_Date(String First_Calculation_Date) {
        this.First_Calculation_Date = First_Calculation_Date;
    }

    /**
     * LastVisit_Date
     *
     * @return LastVisit_Date
     */
    public String getLastVisit_Date() {
        return LastVisit_Date;
    }

    /**
     * LastVisit_Date
     *
     * @param LastVisit_Date to set
     */
    public void setLastVisit_Date(String LastVisit_Date) {
        this.LastVisit_Date = LastVisit_Date;
    }

    /**
     * Department_Code
     *
     * @return Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * Department_Code
     *
     * @param Department_Code to set
     */
    public void setDepartment_Code(String Department_Code) {
        this.Department_Code = Department_Code;
    }

    /**
     * Department_Name
     *
     * @return Department_Name
     */
    public String getDepartment_Name() {
        return Department_Name;
    }

    /**
     * Department_Name
     *
     * @param Department_Name to set
     */
    public void setDepartment_Name(String Department_Name) {
        this.Department_Name = Department_Name;
    }

    /**
     * FirstVisit_Date
     *
     * @return FirstVisit_Date
     */
    public String getFirstVisit_Date() {
        return FirstVisit_Date;
    }

    /**
     * FirstVisit_Date
     *
     * @param FirstVisit_Date to set
     */
    public void setFirstVisit_Date(String FirstVisit_Date) {
        this.FirstVisit_Date = FirstVisit_Date;
    }
}
