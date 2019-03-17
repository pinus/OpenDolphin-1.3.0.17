package open.dolphin.orca.orcaapi.bean;

/**
 * system01_managereq.
 *
 * @author pns
 */
public class System01Managereq {
    /**
     * リクエスト番号
     * 01:診療科情報, 02:ドクター情報, 03:職員情報, 04:医療機関基本情報 (例: 01)
     */
    private String Request_Number;

    /**
     * 基準日 (例: 2012-06-01)
     */
    private String Base_Date;

    /**
     * リクエスト番号
     * 01:診療科情報, 02:ドクター情報, 03:職員情報, 04:医療機関基本情報 (例: 01)
     *
     * @return the Request_Number
     */
    public String getRequest_Number() {
        return Request_Number;
    }

    /**
     * リクエスト番号 (例: 01)
     *
     * @param Request_Number the Request_Number to set
     */
    public void setRequest_Number(String Request_Number) {
        this.Request_Number = Request_Number;
    }

    /**
     * 基準日 (例: 2012-06-01)
     *
     * @return the Base_Date
     */
    public String getBase_Date() {
        return Base_Date;
    }

    /**
     * 基準日 (例: 2012-06-01)
     *
     * @param Base_Date the Base_Date to set
     */
    public void setBase_Date(String Base_Date) {
        this.Base_Date = Base_Date;
    }
}