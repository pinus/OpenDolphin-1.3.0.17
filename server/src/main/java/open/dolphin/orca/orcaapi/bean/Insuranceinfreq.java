package open.dolphin.orca.orcaapi.bean;

/**
 * insuranceinfreq.
 * @author pns
 */
public class Insuranceinfreq {
    /**
     * リクエスト番号01 (例: )
     */
    private String Request_Number;

    /**
     * 基準日省略可(システム日付) (例: )
     */
    private String Base_Date;

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
     * Base_Date
     *
     * @return Base_Date
     */
    public String getBase_Date() {
        return Base_Date;
    }

    /**
     * Base_Date
     *
     * @param Base_Date to set
     */
    public void setBase_Date(String Base_Date) {
        this.Base_Date = Base_Date;
    }
}