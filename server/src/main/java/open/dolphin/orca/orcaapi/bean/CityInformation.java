package open.dolphin.orca.orcaapi.bean;

/**
 * City_Information. 所在地郡市区情報
 *
 * @author pns
 */
public class CityInformation {
    /**
     * 郡市区名 (例: 松江)
     */
    private String C_WholeName;

    /**
     * 郡市区コード (例: 2)
     */
    private String C_Class;

    /**
     * 郡市の区分 (例: 市)
     */
    private String C_Class_Name;

    /**
     * @return the C_WholeName
     */
    public String getC_WholeName() {
        return C_WholeName;
    }

    /**
     * @param C_WholeName the C_WholeName to set
     */
    public void setC_WholeName(String C_WholeName) {
        this.C_WholeName = C_WholeName;
    }

    /**
     * 郡市区コード (例: 2)
     *
     * @return the C_Class
     */
    public String getC_Class() {
        return C_Class;
    }

    /**
     * 郡市区コード (例: 2)
     *
     * @param C_Class the C_Class to set
     */
    public void setC_Class(String C_Class) {
        this.C_Class = C_Class;
    }

    /**
     * 郡市の区分 (例: 市)
     *
     * @return the C_Class_Name
     */
    public String getC_Class_Name() {
        return C_Class_Name;
    }

    /**
     * 郡市の区分 (例: 市)
     *
     * @param C_Class_Name the C_Class_Name to set
     */
    public void setC_Class_Name(String C_Class_Name) {
        this.C_Class_Name = C_Class_Name;
    }
}
