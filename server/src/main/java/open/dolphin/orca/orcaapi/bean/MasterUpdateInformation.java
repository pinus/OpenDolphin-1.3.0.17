package open.dolphin.orca.orcaapi.bean;

/**
 * Master_Update_Information. マスタ更新情報
 *
 * @author pns
 */
public class MasterUpdateInformation {
    /**
     * 直近のマスタ更新実施日 (例: 2014-10-02)
     */
    private String Last_Update_Date;

    /**
     * マスタ構造情報(繰り返し99) (例:  )
     */
    private MasterVersionInformation[] Master_Version_Information;

    /**
     * 直近のマスタ更新実施日 (例: 2014-10-02)
     *
     * @return the Last_Update_Date
     */
    public String getLast_Update_Date() {
        return Last_Update_Date;
    }

    /**
     * 直近のマスタ更新実施日 (例: 2014-10-02)
     *
     * @param Last_Update_Date the Last_Update_Date to set
     */
    public void setLast_Update_Date(String Last_Update_Date) {
        this.Last_Update_Date = Last_Update_Date;
    }

    /**
     * マスタ構造情報(繰り返し99) (例:  )
     *
     * @return the Master_Version_Information
     */
    public MasterVersionInformation[] getMaster_Version_Information() {
        return Master_Version_Information;
    }

    /**
     * マスタ構造情報(繰り返し99) (例:  )
     *
     * @param Master_Version_Information the Master_Version_Information to set
     */
    public void setMaster_Version_Information(MasterVersionInformation[] Master_Version_Information) {
        this.Master_Version_Information = Master_Version_Information;
    }
}