package open.dolphin.orca.orcaapi.bean;

/**
 * 患者病名情報 リクエスト.
 * http://www.orca.med.or.jp/receipt/tec/api/disease.html
 *
 * @author pns
 */
public class DiseaseInforeq {
    /**
     * 患者番号 (例: 00012)
     */
    private String Patient_ID;

    /**
     * 基準月 (例: 2012-05)
     */
    private String Base_Date;

    /**
     * 転帰済選択区分 (例: All)
     */
    private String Select_Mode;

    /**
     * 患者番号 (例: 00012)
     *
     * @return the Patient_ID
     */
    public String getPatient_ID() {
        return Patient_ID;
    }

    /**
     * 患者番号 (例: 00012)
     *
     * @param Patient_ID the Patient_ID to set
     */
    public void setPatient_ID(String Patient_ID) {
        this.Patient_ID = Patient_ID;
    }

    /**
     * 基準月 (例: 2012-05)
     *
     * @return the Base_Date
     */
    public String getBase_Date() {
        return Base_Date;
    }

    /**
     * 基準月 (例: 2012-05)
     *
     * @param Base_Date the Base_Date to set
     */
    public void setBase_Date(String Base_Date) {
        this.Base_Date = Base_Date;
    }

    /**
     * 転帰済選択区分 (例: All)
     *
     * @return the Select_Mode
     */
    public String getSelect_Mode() {
        return Select_Mode;
    }

    /**
     * 転帰済選択区分 (例: All)
     *
     * @param Select_Mode the Select_Mode to set
     */
    public void setSelect_Mode(String Select_Mode) {
        this.Select_Mode = Select_Mode;
    }
}
