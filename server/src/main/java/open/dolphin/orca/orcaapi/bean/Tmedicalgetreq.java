package open.dolphin.orca.orcaapi.bean;

/**
 * tmedicalgetreq.
 *
 * @author pns
 */
public class Tmedicalgetreq {
    /**
     * 診療日 (例: 2013-10-02)
     */
    private String Perform_Date;

    /**
     * 入院外来区分（1:入院中、2:入院外） (例: 2)
     */
    private String InOut;

    /**
     * 診療科コード（01:内科） (例: 01)
     */
    private String Department_Code;

    /**
     * 患者番号 (例: 12)
     */
    private String Patient_ID;

    /**
     * 診療日 (例: 2013-10-02)
     *
     * @return the Perform_Date
     */
    public String getPerform_Date() {
        return Perform_Date;
    }

    /**
     * 診療日 (例: 2013-10-02)
     *
     * @param Perform_Date the Perform_Date to set
     */
    public void setPerform_Date(String Perform_Date) {
        this.Perform_Date = Perform_Date;
    }

    /**
     * 入院外来区分（1:入院中、2:入院外） (例: 2)
     *
     * @return the InOut
     */
    public String getInOut() {
        return InOut;
    }

    /**
     * 入院外来区分（1:入院中、2:入院外） (例: 2)
     *
     * @param InOut the InOut to set
     */
    public void setInOut(String InOut) {
        this.InOut = InOut;
    }

    /**
     * 診療科コード（01:内科） (例: 01)
     *
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 診療科コード（01:内科） (例: 01)
     *
     * @param Department_Code the Department_Code to set
     */
    public void setDepartment_Code(String Department_Code) {
        this.Department_Code = Department_Code;
    }

    /**
     * 患者番号 (例: 12)
     *
     * @return the Patient_ID
     */
    public String getPatient_ID() {
        return Patient_ID;
    }

    /**
     * 患者番号 (例: 12)
     *
     * @param Patient_ID the Patient_ID to set
     */
    public void setPatient_ID(String Patient_ID) {
        this.Patient_ID = Patient_ID;
    }
}