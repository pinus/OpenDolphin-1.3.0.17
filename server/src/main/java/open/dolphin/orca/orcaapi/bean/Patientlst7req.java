package open.dolphin.orca.orcaapi.bean;

/**
 * patientlst7req.
 *
 * @author pns
 */
public class Patientlst7req {
    /**
     * リクエスト番号01 (例: )
     */
    private String Request_Number;

    /**
     * 患者番号 (例: )
     */
    private String Patient_ID;

    /**
     * 基準日未設定はシステム日付 (例: )
     */
    private String Base_Date;

    /**
     * 診療科※1 (例: )
     */
    private String Department_Code;

    /**
     * メモ区分※2 (例: )
     */
    private String Memo_Class;

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
     * Base_Date
     *
     * @return Base_Date
     */
    public String getBase_Date() {
        return Base_Date;
    }

    /**
     * Base_Date
     *
     * @param Base_Date to set
     */
    public void setBase_Date(String Base_Date) {
        this.Base_Date = Base_Date;
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
     * Memo_Class
     *
     * @return Memo_Class
     */
    public String getMemo_Class() {
        return Memo_Class;
    }

    /**
     * Memo_Class
     *
     * @param Memo_Class to set
     */
    public void setMemo_Class(String Memo_Class) {
        this.Memo_Class = Memo_Class;
    }
}