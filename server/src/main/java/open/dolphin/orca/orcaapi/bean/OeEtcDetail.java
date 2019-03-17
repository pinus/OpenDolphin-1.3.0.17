package open.dolphin.orca.orcaapi.bean;

/**
 * Oe_Etc_Detail[10]. その他自費詳細
 *
 * @author pns
 */
public class OeEtcDetail {
    /**
     * 番号 (例: 1)
     */
    private String Oe_Etc_Number;

    /**
     * 項目名 (例: 文書料)
     */
    private String Oe_Etc_Name;

    /**
     * 非課税金額(ゼロは非表示） (例: 1000)
     */
    private String Oe_Etc_Money_Non_Taxable;

    /**
     * 課税金額(ゼロは非表示） (例: 1080)
     */
    private String Oe_Etc_Money_Taxable;

    /**
     * 番号 (例: 1)
     *
     * @return the Oe_Etc_Number
     */
    public String getOe_Etc_Number() {
        return Oe_Etc_Number;
    }

    /**
     * 番号 (例: 1)
     *
     * @param Oe_Etc_Number the Oe_Etc_Number to set
     */
    public void setOe_Etc_Number(String Oe_Etc_Number) {
        this.Oe_Etc_Number = Oe_Etc_Number;
    }

    /**
     * 項目名 (例: 文書料)
     *
     * @return the Oe_Etc_Name
     */
    public String getOe_Etc_Name() {
        return Oe_Etc_Name;
    }

    /**
     * 項目名 (例: 文書料)
     *
     * @param Oe_Etc_Name the Oe_Etc_Name to set
     */
    public void setOe_Etc_Name(String Oe_Etc_Name) {
        this.Oe_Etc_Name = Oe_Etc_Name;
    }

    /**
     * 非課税金額(ゼロは非表示） (例: 1000)
     *
     * @return the Oe_Etc_Money_Non_Taxable
     */
    public String getOe_Etc_Money_Non_Taxable() {
        return Oe_Etc_Money_Non_Taxable;
    }

    /**
     * 非課税金額(ゼロは非表示） (例: 1000)
     *
     * @param Oe_Etc_Money_Non_Taxable the Oe_Etc_Money_Non_Taxable to set
     */
    public void setOe_Etc_Money_Non_Taxable(String Oe_Etc_Money_Non_Taxable) {
        this.Oe_Etc_Money_Non_Taxable = Oe_Etc_Money_Non_Taxable;
    }

    /**
     * 課税金額(ゼロは非表示） (例: 1080)
     *
     * @return the Oe_Etc_Money_Taxable
     */
    public String getOe_Etc_Money_Taxable() {
        return Oe_Etc_Money_Taxable;
    }

    /**
     * 課税金額(ゼロは非表示） (例: 1080)
     *
     * @param Oe_Etc_Money_Taxable the Oe_Etc_Money_Taxable to set
     */
    public void setOe_Etc_Money_Taxable(String Oe_Etc_Money_Taxable) {
        this.Oe_Etc_Money_Taxable = Oe_Etc_Money_Taxable;
    }
}
