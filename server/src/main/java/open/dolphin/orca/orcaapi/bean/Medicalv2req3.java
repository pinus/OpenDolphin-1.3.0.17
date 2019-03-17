package open.dolphin.orca.orcaapi.bean;

/**
 * medicalv2req3.
 *
 * @author pns
 */
public class Medicalv2req3 {
    /**
     * リクエスト番号  (例: )
     * 検索機能   Request_Number=00
     * 登録機能   Request_Number=01
     * 削除機能   Request_Number=02
     */
    private String Request_Number;

    /**
     * 患者番号 (例: )
     */
    private String Patient_ID;

    /**
     * 初診算定日△:リクエスト=01のみ必須※1 (例: )
     */
    private String First_Calculation_Date;

    /**
     * 最終来院日リクエスト=01のみ設定※2 (例: )
     */
    private String LastVisit_Date;

    /**
     * 診療科△:リクエスト=01のみ設定※3 (例: )
     */
    private String Department_Code;

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
}
