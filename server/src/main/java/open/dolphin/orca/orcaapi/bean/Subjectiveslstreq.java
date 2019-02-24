package open.dolphin.orca.orcaapi.bean;

/**
 * subjectiveslstreq.
 * @author pns
 */
public class Subjectiveslstreq {
    /**
     * リクエスト番号01、02 (例: )
     * Request_Number = 01 により、全ての症状詳記区分リストを返却します。
     * Request_Number = 02 において、診療科、保険組合せ等を設定することにより該当の症状詳記区分に登録されているコメント内容を返却します
     */
    private String Request_Number;

    /**
     * 患者番号 (例: )
     */
    private String Patient_ID;

    /**
     * 診療年月未設定はシステム日付 (例: )
     */
    private String Perform_Date;

    /**
     * 入外区分I:入院、O:外来※1 (例: )
     */
    private String InOut;

    /**
     * 診療科※1 (例: )
     */
    private String Department_Code;

    /**
     * 保険組合せ番号※1、※2 (例: )
     */
    private String Insurance_Combination_Number;

    /**
     * 診療日※1、※2 (例: )
     */
    private String Perform_Day;

    /**
     * 症状詳記区分※1 (例: )
     */
    private String Subjectives_Detail_Record;

    /**
     * 連番号※1、※2※3 (例: )
     */
    private String Subjectives_Number;

    /**
     * Request_Number
     *
     * @return Request_Number
     */
    public String getRequest_Number() {
        return Request_Number;
    }

    /**
     * Request_Number
     *
     * @param Request_Number to set
     */
    public void setRequest_Number(String Request_Number) {
        this.Request_Number = Request_Number;
    }

    /**
     * Patient_ID
     *
     * @return Patient_ID
     */
    public String getPatient_ID() {
        return Patient_ID;
    }

    /**
     * Patient_ID
     *
     * @param Patient_ID to set
     */
    public void setPatient_ID(String Patient_ID) {
        this.Patient_ID = Patient_ID;
    }

    /**
     * Perform_Date
     *
     * @return Perform_Date
     */
    public String getPerform_Date() {
        return Perform_Date;
    }

    /**
     * Perform_Date
     *
     * @param Perform_Date to set
     */
    public void setPerform_Date(String Perform_Date) {
        this.Perform_Date = Perform_Date;
    }

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
     * Insurance_Combination_Number
     *
     * @return Insurance_Combination_Number
     */
    public String getInsurance_Combination_Number() {
        return Insurance_Combination_Number;
    }

    /**
     * Insurance_Combination_Number
     *
     * @param Insurance_Combination_Number to set
     */
    public void setInsurance_Combination_Number(String Insurance_Combination_Number) {
        this.Insurance_Combination_Number = Insurance_Combination_Number;
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