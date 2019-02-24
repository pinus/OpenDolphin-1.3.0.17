package open.dolphin.orca.orcaapi.bean;

/**
 * Subjectives_Information. 症状詳記リスト（繰り返し50）
 * @author pns
 */
public class SubjectivesInformation {
    /**
     * 入外区分 (例: )
     */
    private String InOut;

    /**
     * 診療科 (例: )
     */
    private String Department_Code;

    /**
     * 診療科名称 (例: )
     */
    private String Department_Name;

    /**
     * 保険情報 (例: )
     */
    private HealthInsuranceInformation HealthInsurance_Information;

    /**
     * 診療日アフターケアのみ (例: )
     */
    private String Perform_Day;

    /**
     * 症状詳記区分 (例: )
     */
    private String Subjectives_Detail_Record;

    /**
     * 症状詳記区分名称 (例: )
     */
    private String Subjectives_Detail_Record_WholeName;

    /**
     * 連番号 (例: )
     */
    private String Subjectives_Number;

    /**
     * InOut
     *
     * @return InOut
     */
    public String getInOut() {
        return InOut;
    }

    /**
     * InOut
     *
     * @param InOut to set
     */
    public void setInOut(String InOut) {
        this.InOut = InOut;
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
     * HealthInsurance_Information
     *
     * @return HealthInsurance_Information
     */
    public HealthInsuranceInformation getHealthInsurance_Information() {
        return HealthInsurance_Information;
    }

    /**
     * HealthInsurance_Information
     *
     * @param HealthInsurance_Information to set
     */
    public void setHealthInsurance_Information(HealthInsuranceInformation HealthInsurance_Information) {
        this.HealthInsurance_Information = HealthInsurance_Information;
    }

    /**
     * Perform_Day
     *
     * @return Perform_Day
     */
    public String getPerform_Day() {
        return Perform_Day;
    }

    /**
     * Perform_Day
     *
     * @param Perform_Day to set
     */
    public void setPerform_Day(String Perform_Day) {
        this.Perform_Day = Perform_Day;
    }

    /**
     * Subjectives_Detail_Record
     *
     * @return Subjectives_Detail_Record
     */
    public String getSubjectives_Detail_Record() {
        return Subjectives_Detail_Record;
    }

    /**
     * Subjectives_Detail_Record
     *
     * @param Subjectives_Detail_Record to set
     */
    public void setSubjectives_Detail_Record(String Subjectives_Detail_Record) {
        this.Subjectives_Detail_Record = Subjectives_Detail_Record;
    }

    /**
     * Subjectives_Detail_Record_WholeName
     *
     * @return Subjectives_Detail_Record_WholeName
     */
    public String getSubjectives_Detail_Record_WholeName() {
        return Subjectives_Detail_Record_WholeName;
    }

    /**
     * Subjectives_Detail_Record_WholeName
     *
     * @param Subjectives_Detail_Record_WholeName to set
     */
    public void setSubjectives_Detail_Record_WholeName(String Subjectives_Detail_Record_WholeName) {
        this.Subjectives_Detail_Record_WholeName = Subjectives_Detail_Record_WholeName;
    }

    /**
     * Subjectives_Number
     *
     * @return Subjectives_Number
     */
    public String getSubjectives_Number() {
        return Subjectives_Number;
    }

    /**
     * Subjectives_Number
     *
     * @param Subjectives_Number to set
     */
    public void setSubjectives_Number(String Subjectives_Number) {
        this.Subjectives_Number = Subjectives_Number;
    }
}