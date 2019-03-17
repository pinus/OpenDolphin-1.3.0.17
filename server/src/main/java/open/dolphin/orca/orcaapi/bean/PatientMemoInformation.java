package open.dolphin.orca.orcaapi.bean;

/**
 * Patient_Memo_Information. メモ登録情報
 *
 * @author pns
 */
public class PatientMemoInformation {
    /**
     * メモ登録区分(診療科=00)（1：メモ1のみの登録、2：メモ2のみの登録、3：メモ1，メモ2の登録） (例:  )
     */
    private String Patient_Memo_Department_00;

    /**
     * メモ登録区分（1：メモ1のみの登録、2：メモ2のみの登録、3：メモ1，メモ2の登録） (例:  )
     */
    private String Patient_Memo_Department;

    /**
     * 診療科※2 (例: )
     */
    private String Department_Code;

    /**
     * 診療科名称 (例: )
     */
    private String Department_Name;

    /**
     * メモ内容 (例: )
     */
    private String Patient_Memo;

    /**
     * 受付情報※3 (例: )
     */
    private AcceptInformation Accept_Information;

    /**
     * Patient_Memo_Department_00
     *
     * @return Patient_Memo_Department_00
     */
    public String getPatient_Memo_Department_00() {
        return Patient_Memo_Department_00;
    }

    /**
     * Patient_Memo_Department_00
     *
     * @param Patient_Memo_Department_00 to set
     */
    public void setPatient_Memo_Department_00(String Patient_Memo_Department_00) {
        this.Patient_Memo_Department_00 = Patient_Memo_Department_00;
    }

    /**
     * Patient_Memo_Department
     *
     * @return Patient_Memo_Department
     */
    public String getPatient_Memo_Department() {
        return Patient_Memo_Department;
    }

    /**
     * Patient_Memo_Department
     *
     * @param Patient_Memo_Department to set
     */
    public void setPatient_Memo_Department(String Patient_Memo_Department) {
        this.Patient_Memo_Department = Patient_Memo_Department;
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
     * Patient_Memo
     *
     * @return Patient_Memo
     */
    public String getPatient_Memo() {
        return Patient_Memo;
    }

    /**
     * Patient_Memo
     *
     * @param Patient_Memo to set
     */
    public void setPatient_Memo(String Patient_Memo) {
        this.Patient_Memo = Patient_Memo;
    }

    /**
     * Accept_Information
     *
     * @return Accept_Information
     */
    public AcceptInformation getAccept_Information() {
        return Accept_Information;
    }

    /**
     * Accept_Information
     *
     * @param Accept_Information to set
     */
    public void setAccept_Information(AcceptInformation Accept_Information) {
        this.Accept_Information = Accept_Information;
    }
}
