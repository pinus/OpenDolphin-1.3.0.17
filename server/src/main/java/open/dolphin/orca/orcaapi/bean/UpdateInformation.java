package open.dolphin.orca.orcaapi.bean;

/**
 * Update_Information. 更新日情報リクエストのマスタIDが設定されている場合に設定（繰り返し5）
 *
 * @author pns
 */
public class UpdateInformation {
    /**
     * 更新日 (例: 2017-10-11)
     */
    private String Update_Date;

    /**
     * 更新件数 (例: 10)
     */
    private String Count;

    /**
     * Update_Date
     *
     * @return Update_Date
     */
    public String getUpdate_Date() {
        return Update_Date;
    }

    /**
     * Update_Date
     *
     * @param Update_Date to set
     */
    public void setUpdate_Date(String Update_Date) {
        this.Update_Date = Update_Date;
    }

    /**
     * Count
     *
     * @return Count
     */
    public String getCount() {
        return Count;
    }

    /**
     * Count
     *
     * @param Count to set
     */
    public void setCount(String Count) {
        this.Count = Count;
    }
}
