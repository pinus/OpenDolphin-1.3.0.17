package open.dolphin.orca.orcaapi.bean;

/**
 * Personally_Information. 患者個別情報
 *
 * @author pns
 */
public class PersonallyInformation {
    /**
     * 妊婦区分 (例:  )
     */
    private String Pregnant_Class;

    /**
     * 認知症地域包括診療加算算定（True：該当である） (例:  )
     */
    private String Community_Disease2;

    /**
     * 小児かかりつけ診療料算定（True：該当である） (例:  )
     */
    private String Community_Disease3;

    /**
     * Pregnant_Class
     *
     * @return Pregnant_Class
     */
    public String getPregnant_Class() {
        return Pregnant_Class;
    }

    /**
     * Pregnant_Class
     *
     * @param Pregnant_Class to set
     */
    public void setPregnant_Class(String Pregnant_Class) {
        this.Pregnant_Class = Pregnant_Class;
    }

    /**
     * Community_Disease2
     *
     * @return Community_Disease2
     */
    public String getCommunity_Disease2() {
        return Community_Disease2;
    }

    /**
     * Community_Disease2
     *
     * @param Community_Disease2 to set
     */
    public void setCommunity_Disease2(String Community_Disease2) {
        this.Community_Disease2 = Community_Disease2;
    }

    /**
     * Community_Disease3
     *
     * @return Community_Disease3
     */
    public String getCommunity_Disease3() {
        return Community_Disease3;
    }

    /**
     * Community_Disease3
     *
     * @param Community_Disease3 to set
     */
    public void setCommunity_Disease3(String Community_Disease3) {
        this.Community_Disease3 = Community_Disease3;
    }
}
