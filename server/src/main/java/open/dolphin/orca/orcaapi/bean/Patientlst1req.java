package open.dolphin.orca.orcaapi.bean;

/**
 * patientlst1req.
 * @author pns
 */
public class Patientlst1req {
    /**
     * 開始日 (例: 2014-05-01)
     */
    private String Base_StartDate;

    /**
     * 終了日 (例: 2014-07-01)
     */
    private String Base_EndDate;

    /**
     * テスト患者区分 (例: 1) １:テスト患者対象外
     */
    private String Contain_TestPatient_Flag;

    /**
     * 開始日 (例: 2014-05-01)
     * @return the Base_StartDate
     */
    public String getBase_StartDate() {
        return Base_StartDate;
    }

    /**
     * 開始日 (例: 2014-05-01)
     * @param Base_StartDate the Base_StartDate to set
     */
    public void setBase_StartDate(String Base_StartDate) {
        this.Base_StartDate = Base_StartDate;
    }

    /**
     * 終了日 (例: 2014-07-01)
     * @return the Base_EndDate
     */
    public String getBase_EndDate() {
        return Base_EndDate;
    }

    /**
     * 終了日 (例: 2014-07-01)
     * @param Base_EndDate the Base_EndDate to set
     */
    public void setBase_EndDate(String Base_EndDate) {
        this.Base_EndDate = Base_EndDate;
    }

    /**
     * テスト患者区分 (例: 1) １:テスト患者対象外
     * @return the Contain_TestPatient_Flag
     */
    public String getContain_TestPatient_Flag() {
        return Contain_TestPatient_Flag;
    }

    /**
     * テスト患者区分 (例: 1) １:テスト患者対象外
     * @param Contain_TestPatient_Flag the Contain_TestPatient_Flag to set
     */
    public void setContain_TestPatient_Flag(String Contain_TestPatient_Flag) {
        this.Contain_TestPatient_Flag = Contain_TestPatient_Flag;
    }
}