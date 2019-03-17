package open.dolphin.orca.orcaapi.bean;

/**
 * Oe_Etc_Information. その他自費情報
 *
 * @author pns
 */
public class OeEtcInformation {
    /**
     * その他自費（非課税分）合計金額(ゼロは非表示） (例: 1000)
     */
    private String Oe_Etc_Ttl_Money_Non_Taxable;

    /**
     * その他自費（課税分）合計金額(ゼロは非表示） (例: 1080)
     */
    private String Oe_Etc_Ttl_Money_Taxable;

    /**
     * その他自費（課税分）合計金額消費税再掲(ゼロは非表示） (例: 80)
     */
    private String Tax_In_Oe_Etc_Ttl_Money_Taxable;

    /**
     * その他自費詳細（繰り返し10） (例: )
     */
    private OeEtcDetail[] Oe_Etc_Detail;

    /**
     * その他自費（非課税分）合計金額(ゼロは非表示） (例: 1000)
     *
     * @return the Oe_Etc_Ttl_Money_Non_Taxable
     */
    public String getOe_Etc_Ttl_Money_Non_Taxable() {
        return Oe_Etc_Ttl_Money_Non_Taxable;
    }

    /**
     * その他自費（非課税分）合計金額(ゼロは非表示） (例: 1000)
     *
     * @param Oe_Etc_Ttl_Money_Non_Taxable the Oe_Etc_Ttl_Money_Non_Taxable to set
     */
    public void setOe_Etc_Ttl_Money_Non_Taxable(String Oe_Etc_Ttl_Money_Non_Taxable) {
        this.Oe_Etc_Ttl_Money_Non_Taxable = Oe_Etc_Ttl_Money_Non_Taxable;
    }

    /**
     * その他自費（課税分）合計金額(ゼロは非表示） (例: 1080)
     *
     * @return the Oe_Etc_Ttl_Money_Taxable
     */
    public String getOe_Etc_Ttl_Money_Taxable() {
        return Oe_Etc_Ttl_Money_Taxable;
    }

    /**
     * その他自費（課税分）合計金額(ゼロは非表示） (例: 1080)
     *
     * @param Oe_Etc_Ttl_Money_Taxable the Oe_Etc_Ttl_Money_Taxable to set
     */
    public void setOe_Etc_Ttl_Money_Taxable(String Oe_Etc_Ttl_Money_Taxable) {
        this.Oe_Etc_Ttl_Money_Taxable = Oe_Etc_Ttl_Money_Taxable;
    }

    /**
     * その他自費（課税分）合計金額消費税再掲(ゼロは非表示） (例: 80)
     *
     * @return the Tax_In_Oe_Etc_Ttl_Money_Taxable
     */
    public String getTax_In_Oe_Etc_Ttl_Money_Taxable() {
        return Tax_In_Oe_Etc_Ttl_Money_Taxable;
    }

    /**
     * その他自費（課税分）合計金額消費税再掲(ゼロは非表示） (例: 80)
     *
     * @param Tax_In_Oe_Etc_Ttl_Money_Taxable the Tax_In_Oe_Etc_Ttl_Money_Taxable to set
     */
    public void setTax_In_Oe_Etc_Ttl_Money_Taxable(String Tax_In_Oe_Etc_Ttl_Money_Taxable) {
        this.Tax_In_Oe_Etc_Ttl_Money_Taxable = Tax_In_Oe_Etc_Ttl_Money_Taxable;
    }

    /**
     * その他自費詳細（繰り返し10） (例: )
     *
     * @return the Oe_Etc_Detail
     */
    public OeEtcDetail[] getOe_Etc_Detail() {
        return Oe_Etc_Detail;
    }

    /**
     * その他自費詳細（繰り返し10） (例: )
     *
     * @param Oe_Etc_Detail the Oe_Etc_Detail to set
     */
    public void setOe_Etc_Detail(OeEtcDetail[] Oe_Etc_Detail) {
        this.Oe_Etc_Detail = Oe_Etc_Detail;
    }
}