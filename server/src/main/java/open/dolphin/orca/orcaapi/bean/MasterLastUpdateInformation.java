package open.dolphin.orca.orcaapi.bean;

/**
 * Master_Last_Update_Information. マスタ最終更新日情報リクエストのマスタIDが設定されていない場合に設定（ 繰り返し20）
 * @author pns
 */
public class MasterLastUpdateInformation {
    /**
     * マスタID点数マスタ...medication_master病名マスタ...disease_master (例: medication_master)
     */
    private String Master_Id;

    /**
     * 最終更新日 (例: 2017-10-11)
     */
    private String Last_Update_Date;

    /**
     * Master_Id
     *
     * @return Master_Id
     */
    public String getMaster_Id() {
        return Master_Id;
    }

    /**
     * Master_Id
     *
     * @param Master_Id to set
     */
    public void setMaster_Id(String Master_Id) {
        this.Master_Id = Master_Id;
    }

    /**
     * Last_Update_Date
     *
     * @return Last_Update_Date
     */
    public String getLast_Update_Date() {
        return Last_Update_Date;
    }

    /**
     * Last_Update_Date
     *
     * @param Last_Update_Date to set
     */
    public void setLast_Update_Date(String Last_Update_Date) {
        this.Last_Update_Date = Last_Update_Date;
    }
}
