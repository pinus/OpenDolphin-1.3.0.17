package open.dolphin.orca.orcaapi.bean;

/**
 * Ac_Point_Information. 請求点数
 * @author pns
 */
public class AcPointInformation {
    /**
     * 合計点数 (例: 1800)
     */
    private String Ac_Ttl_Point;

    /**
     * 保険適用外合計金額（ゼロは非表示）　負担金額（円）の自費分 (例: 1080)
     */
    private String Me_Ttl_Money;

    /**
     * 保険適用外合計金額消費税再掲（ゼロは非表示） (例: 80)
     */
    private String Tax_In_Me_Ttl_Money;

    /**
     * 点数詳細（繰り返し１６） (例: )
     */
    private AcPointDetail[] Ac_Point_Detail;

    /**
     * 合計点数 (例: 1800)
     * @return the Ac_Ttl_Point
     */
    public String getAc_Ttl_Point() {
        return Ac_Ttl_Point;
    }

    /**
     * 合計点数 (例: 1800)
     * @param Ac_Ttl_Point the Ac_Ttl_Point to set
     */
    public void setAc_Ttl_Point(String Ac_Ttl_Point) {
        this.Ac_Ttl_Point = Ac_Ttl_Point;
    }

    /**
     * 保険適用外合計金額（ゼロは非表示）　負担金額（円）の自費分 (例: 1080)
     * @return the Me_Ttl_Money
     */
    public String getMe_Ttl_Money() {
        return Me_Ttl_Money;
    }

    /**
     * 保険適用外合計金額（ゼロは非表示）　負担金額（円）の自費分 (例: 1080)
     * @param Me_Ttl_Money the Me_Ttl_Money to set
     */
    public void setMe_Ttl_Money(String Me_Ttl_Money) {
        this.Me_Ttl_Money = Me_Ttl_Money;
    }

    /**
     * 保険適用外合計金額消費税再掲（ゼロは非表示） (例: 80)
     * @return the Tax_In_Me_Ttl_Money
     */
    public String getTax_In_Me_Ttl_Money() {
        return Tax_In_Me_Ttl_Money;
    }

    /**
     * 保険適用外合計金額消費税再掲（ゼロは非表示） (例: 80)
     * @param Tax_In_Me_Ttl_Money the Tax_In_Me_Ttl_Money to set
     */
    public void setTax_In_Me_Ttl_Money(String Tax_In_Me_Ttl_Money) {
        this.Tax_In_Me_Ttl_Money = Tax_In_Me_Ttl_Money;
    }

    /**
     * 点数詳細（繰り返し１６） (例: )
     * @return the Ac_Point_Detail
     */
    public AcPointDetail[] getAc_Point_Detail() {
        return Ac_Point_Detail;
    }

    /**
     * 点数詳細（繰り返し１６） (例: )
     * @param Ac_Point_Detail the Ac_Point_Detail to set
     */
    public void setAc_Point_Detail(AcPointDetail[] Ac_Point_Detail) {
        this.Ac_Point_Detail = Ac_Point_Detail;
    }
}