package open.dolphin.orca.orcaapi.bean;

/**
 * Damage_Class. アフターケア　損傷区分情報
 * @author pns
 */
public class DamageClass {
    /**
     * 損傷区分コード (例: 14)
     */
    private String D_Code;

    /**
     * 損傷区分 (例: 外傷による末梢神経損傷)
     */
    private String D_WholeName;

    /**
     * 損傷区分コード (例: 14)
     * @return the D_Code
     */
    public String getD_Code() {
        return D_Code;
    }

    /**
     * 損傷区分コード (例: 14)
     * @param D_Code the D_Code to set
     */
    public void setD_Code(String D_Code) {
        this.D_Code = D_Code;
    }

    /**
     * 損傷区分 (例: 外傷による末梢神経損傷)
     * @return the D_WholeName
     */
    public String getD_WholeName() {
        return D_WholeName;
    }

    /**
     * 損傷区分 (例: 外傷による末梢神経損傷)
     * @param D_WholeName the D_WholeName to set
     */
    public void setD_WholeName(String D_WholeName) {
        this.D_WholeName = D_WholeName;
    }
}
