package open.dolphin.orca.orcaapi.bean;

/**
 * appointlst2req.
 *
 * @author pns
 */
public class Appointlst2req {
    /**
     * 患者番号 (例: 1)
     */
    private String Patient_ID;

    /**
     * 基準日 (例: 2012-12-18)
     */
    private String Base_Date;

    /**
     * 患者番号 (例: 1)
     *
     * @return the Patient_ID
     */
    public String getPatient_ID() {
        return Patient_ID;
    }

    /**
     * 患者番号 (例: 1)
     *
     * @param Patient_ID the Patient_ID to set
     */
    public void setPatient_ID(String Patient_ID) {
        this.Patient_ID = Patient_ID;
    }

    /**
     * 基準日 (例: 2012-12-18)
     *
     * @return the Base_Date
     */
    public String getBase_Date() {
        return Base_Date;
    }

    /**
     * 基準日 (例: 2012-12-18)
     *
     * @param Base_Date the Base_Date to set
     */
    public void setBase_Date(String Base_Date) {
        this.Base_Date = Base_Date;
    }
}