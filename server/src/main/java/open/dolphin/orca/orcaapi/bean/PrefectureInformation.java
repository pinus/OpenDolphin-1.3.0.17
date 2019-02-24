package open.dolphin.orca.orcaapi.bean;

/**
 * Prefecture_Information. 所在地都道府県情報
 * @author pns
 */
public class PrefectureInformation {
    /**
     * 都道府県名 (例: 島根)
     */
    private String P_WholeName;

    /**
     * 都道府県コード (例: 4)
     */
    private String P_Class;

    /**
     * 都道府県の区分 (例: 県)
     */
    private String P_Class_Name;

    /**
     * 都道府県名 (例: 島根)
     * @return the P_WholeName
     */
    public String getP_WholeName() {
        return P_WholeName;
    }

    /**
     * 都道府県名 (例: 島根)
     * @param P_WholeName the P_WholeName to set
     */
    public void setP_WholeName(String P_WholeName) {
        this.P_WholeName = P_WholeName;
    }

    /**
     * 都道府県コード (例: 4)
     * @return the P_Class
     */
    public String getP_Class() {
        return P_Class;
    }

    /**
     * 都道府県コード (例: 4)
     * @param P_Class the P_Class to set
     */
    public void setP_Class(String P_Class) {
        this.P_Class = P_Class;
    }

    /**
     * 都道府県の区分 (例: 県)
     * @return the P_Class_Name
     */
    public String getP_Class_Name() {
        return P_Class_Name;
    }

    /**
     * 都道府県の区分 (例: 県)
     * @param P_Class_Name the P_Class_Name to set
     */
    public void setP_Class_Name(String P_Class_Name) {
        this.P_Class_Name = P_Class_Name;
    }
}
