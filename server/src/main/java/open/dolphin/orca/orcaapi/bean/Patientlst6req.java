package open.dolphin.orca.orcaapi.bean;

/**
 * patientlst6req.
 *
 * @author pns
 */
public class Patientlst6req {
    /**
     * 処理区分 (例: 01)
     */
    private String Reqest_Number;

    /**
     * 患者番号 (例: 166)
     */
    private String Patient_ID;

    /**
     * 基準日 (例:  )
     */
    private String Base_Date;

    /**
     * 開始年月 (例:  )
     */
    private String Start_Date;

    /**
     * 終了年月 (例:  )
     */
    private String End_Date;

    /**
     * 処理区分 (例: 01)
     *
     * @return the Reqest_Number
     */
    public String getReqest_Number() {
        return Reqest_Number;
    }

    /**
     * 処理区分 (例: 01)
     *
     * @param Reqest_Number the Reqest_Number to set
     */
    public void setReqest_Number(String Reqest_Number) {
        this.Reqest_Number = Reqest_Number;
    }

    /**
     * 患者番号 (例: 166)
     *
     * @return the Patient_ID
     */
    public String getPatient_ID() {
        return Patient_ID;
    }

    /**
     * 患者番号 (例: 166)
     *
     * @param Patient_ID the Patient_ID to set
     */
    public void setPatient_ID(String Patient_ID) {
        this.Patient_ID = Patient_ID;
    }

    /**
     * 基準日 (例:  )
     *
     * @return the Base_Date
     */
    public String getBase_Date() {
        return Base_Date;
    }

    /**
     * 基準日 (例:  )
     *
     * @param Base_Date the Base_Date to set
     */
    public void setBase_Date(String Base_Date) {
        this.Base_Date = Base_Date;
    }

    /**
     * 開始年月 (例:  )
     *
     * @return the Start_Date
     */
    public String getStart_Date() {
        return Start_Date;
    }

    /**
     * 開始年月 (例:  )
     *
     * @param Start_Date the Start_Date to set
     */
    public void setStart_Date(String Start_Date) {
        this.Start_Date = Start_Date;
    }

    /**
     * 終了年月 (例:  )
     *
     * @return the End_Date
     */
    public String getEnd_Date() {
        return End_Date;
    }

    /**
     * 終了年月 (例:  )
     *
     * @param End_Date the End_Date to set
     */
    public void setEnd_Date(String End_Date) {
        this.End_Date = End_Date;
    }
}