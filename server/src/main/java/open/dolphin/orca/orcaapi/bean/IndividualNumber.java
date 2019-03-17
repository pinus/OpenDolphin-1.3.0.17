package open.dolphin.orca.orcaapi.bean;

/**
 * Individual_Number. 個人番号情報（繰り返し　２０）
 *
 * @author pns
 */
public class IndividualNumber {
    /**
     * Id_key (例:  )
     */
    private String In_Id;

    /**
     * 個人番号 (例:  )
     */
    private String In_Number;

    /**
     * 備考（説明） (例:  )
     */
    private String In_Description;

    /**
     * @return the In_Id
     */
    public String getIn_Id() {
        return In_Id;
    }

    /**
     * Id_key (例:  )
     *
     * @param In_Id the In_Id to set
     */
    public void setIn_Id(String In_Id) {
        this.In_Id = In_Id;
    }

    /**
     * 個人番号 (例:  )
     *
     * @return the In_Number
     */
    public String getIn_Number() {
        return In_Number;
    }

    /**
     * 個人番号 (例:  )
     *
     * @param In_Number the In_Number to set
     */
    public void setIn_Number(String In_Number) {
        this.In_Number = In_Number;
    }

    /**
     * 備考（説明） (例:  )
     *
     * @return the In_Description
     */
    public String getIn_Description() {
        return In_Description;
    }

    /**
     * 備考（説明） (例:  )
     *
     * @param In_Description the In_Description to set
     */
    public void setIn_Description(String In_Description) {
        this.In_Description = In_Description;
    }
}
