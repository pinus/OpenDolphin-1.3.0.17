package open.dolphin.orca.orcaapi.bean;

/**
 * Cd_Information. 負担額情報
 * @author pns
 */
public class CdInformation {
    /**
     * 請求金額 (例: 340)
     */
    private String Ac_Money;

    /**
     * 保険適用金額　負担金額（円）の保険分 (例: 0)
     */
    private String Ai_Money;

    /**
     * 自費金額　負担金額（円）の自費分+その他自費のその他計の合計 (例: 0)
     */
    private String Oe_Money;

    /**
     * 薬剤一部負担金（ゼロは非表示） (例: )
     */
    private String Dg_Smoney;

    /**
     * 老人一部負担金（ゼロは非表示） (例: )
     */
    private String Om_Smoney;

    /**
     * 公費一部負担金（ゼロは非表示） (例: 340)
     */
    private String Pi_Smoney;

    /**
     * 労災合計金額　労災自賠責保険適用分（円）の集計値（初診+再診+指導+その他）（ゼロは非表示） (例: )
     */
    private String Lsi_Total_Money;

    /**
     * 請求金額消費税再掲（ゼロは非表示） (例: 960)
     */
    private String Tax_In_Ac_Money;

    /**
     * 入金額 (例: 0)
     */
    private String Ic_Money;

    /**
     * 食事・生活療養負担金（外来またはゼロは非表示）（食事療養負担金＋生活療養負担金） (例: )
     */
    private String Ml_Smoney;

    /**
     * 食事療養負担金（外来またはゼロは非表示） (例: 5460)
     */
    private String Meal_Smoney;

    /**
     * 生活療養負担金（外来またはゼロは非表示） (例: )
     */
    private String Living_Smoney;

    /**
     * 保険適用金額内労災診察等合計金額（ゼロは非表示。患者が負担する場合編集）　労災自賠責保険適用分（円）の 集計値　（初診＋再診＋指導＋その他） (例: )
     */
    private String Lsi_Total_Money_In_Ai_Money;

    /**
     * 減免金額（ゼロは非表示） (例: )
     */
    private String Dis_Money;

    /**
     * 調整金１（ゼロは非表示） (例: )
     */
    private String Ad_Money1;

    /**
     * 調整金２（ゼロは非表示） (例: )
     */
    private String Ad_Money2;

    /**
     * 請求金額 (例: 340)
     * @return the Ac_Money
     */
    public String getAc_Money() {
        return Ac_Money;
    }

    /**
     * 請求金額 (例: 340)
     * @param Ac_Money the Ac_Money to set
     */
    public void setAc_Money(String Ac_Money) {
        this.Ac_Money = Ac_Money;
    }

    /**
     * 保険適用金額　負担金額（円）の保険分 (例: 0)
     * @return the Ai_Money
     */
    public String getAi_Money() {
        return Ai_Money;
    }

    /**
     * 保険適用金額　負担金額（円）の保険分 (例: 0)
     * @param Ai_Money the Ai_Money to set
     */
    public void setAi_Money(String Ai_Money) {
        this.Ai_Money = Ai_Money;
    }

    /**
     * 自費金額　負担金額（円）の自費分+その他自費のその他計の合計 (例: 0)
     * @return the Oe_Money
     */
    public String getOe_Money() {
        return Oe_Money;
    }

    /**
     * 自費金額　負担金額（円）の自費分+その他自費のその他計の合計 (例: 0)
     * @param Oe_Money the Oe_Money to set
     */
    public void setOe_Money(String Oe_Money) {
        this.Oe_Money = Oe_Money;
    }

    /**
     * 薬剤一部負担金（ゼロは非表示） (例: )
     * @return the Dg_Smoney
     */
    public String getDg_Smoney() {
        return Dg_Smoney;
    }

    /**
     * 薬剤一部負担金（ゼロは非表示） (例: )
     * @param Dg_Smoney the Dg_Smoney to set
     */
    public void setDg_Smoney(String Dg_Smoney) {
        this.Dg_Smoney = Dg_Smoney;
    }

    /**
     * 老人一部負担金（ゼロは非表示） (例: )
     * @return the Om_Smoney
     */
    public String getOm_Smoney() {
        return Om_Smoney;
    }

    /**
     * 老人一部負担金（ゼロは非表示） (例: )
     * @param Om_Smoney the Om_Smoney to set
     */
    public void setOm_Smoney(String Om_Smoney) {
        this.Om_Smoney = Om_Smoney;
    }

    /**
     * 公費一部負担金（ゼロは非表示） (例: 340)
     * @return the Pi_Smoney
     */
    public String getPi_Smoney() {
        return Pi_Smoney;
    }

    /**
     * 公費一部負担金（ゼロは非表示） (例: 340)
     * @param Pi_Smoney the Pi_Smoney to set
     */
    public void setPi_Smoney(String Pi_Smoney) {
        this.Pi_Smoney = Pi_Smoney;
    }

    /**
     * 労災合計金額　労災自賠責保険適用分（円）の集計値（初診+再診+指導+その他）（ゼロは非表示） (例: )
     * @return the Lsi_Total_Money
     */
    public String getLsi_Total_Money() {
        return Lsi_Total_Money;
    }

    /**
     * 労災合計金額　労災自賠責保険適用分（円）の集計値（初診+再診+指導+その他）（ゼロは非表示） (例: )
     * @param Lsi_Total_Money the Lsi_Total_Money to set
     */
    public void setLsi_Total_Money(String Lsi_Total_Money) {
        this.Lsi_Total_Money = Lsi_Total_Money;
    }

    /**
     * 請求金額消費税再掲（ゼロは非表示） (例: 960)
     * @return the Tax_In_Ac_Money
     */
    public String getTax_In_Ac_Money() {
        return Tax_In_Ac_Money;
    }

    /**
     * 請求金額消費税再掲（ゼロは非表示） (例: 960)
     * @param Tax_In_Ac_Money the Tax_In_Ac_Money to set
     */
    public void setTax_In_Ac_Money(String Tax_In_Ac_Money) {
        this.Tax_In_Ac_Money = Tax_In_Ac_Money;
    }

    /**
     * 入金額 (例: 0)
     * @return the Ic_Money
     */
    public String getIc_Money() {
        return Ic_Money;
    }

    /**
     * 入金額 (例: 0)
     * @param Ic_Money the Ic_Money to set
     */
    public void setIc_Money(String Ic_Money) {
        this.Ic_Money = Ic_Money;
    }

    /**
     * 食事・生活療養負担金（外来またはゼロは非表示）（食事療養負担金＋生活療養負担金） (例: )
     * @return the Ml_Smoney
     */
    public String getMl_Smoney() {
        return Ml_Smoney;
    }

    /**
     * 食事・生活療養負担金（外来またはゼロは非表示）（食事療養負担金＋生活療養負担金） (例: )
     * @param Ml_Smoney the Ml_Smoney to set
     */
    public void setMl_Smoney(String Ml_Smoney) {
        this.Ml_Smoney = Ml_Smoney;
    }

    /**
     * 食事療養負担金（外来またはゼロは非表示） (例: 5460)
     * @return the Meal_Smoney
     */
    public String getMeal_Smoney() {
        return Meal_Smoney;
    }

    /**
     * 食事療養負担金（外来またはゼロは非表示） (例: 5460)
     * @param Meal_Smoney the Meal_Smoney to set
     */
    public void setMeal_Smoney(String Meal_Smoney) {
        this.Meal_Smoney = Meal_Smoney;
    }

    /**
     * 生活療養負担金（外来またはゼロは非表示） (例: )
     * @return the Living_Smoney
     */
    public String getLiving_Smoney() {
        return Living_Smoney;
    }

    /**
     * 生活療養負担金（外来またはゼロは非表示） (例: )
     * @param Living_Smoney the Living_Smoney to set
     */
    public void setLiving_Smoney(String Living_Smoney) {
        this.Living_Smoney = Living_Smoney;
    }

    /**
     * 保険適用金額内労災診察等合計金額（ゼロは非表示。患者が負担する場合編集）　労災自賠責保険適用分（円）の 集計値　（初診＋再診＋指導＋その他） (例: )
     * @return the Lsi_Total_Money_In_Ai_Money
     */
    public String getLsi_Total_Money_In_Ai_Money() {
        return Lsi_Total_Money_In_Ai_Money;
    }

    /**
     * 保険適用金額内労災診察等合計金額（ゼロは非表示。患者が負担する場合編集）　労災自賠責保険適用分（円）の 集計値　（初診＋再診＋指導＋その他） (例: )
     * @param Lsi_Total_Money_In_Ai_Money the Lsi_Total_Money_In_Ai_Money to set
     */
    public void setLsi_Total_Money_In_Ai_Money(String Lsi_Total_Money_In_Ai_Money) {
        this.Lsi_Total_Money_In_Ai_Money = Lsi_Total_Money_In_Ai_Money;
    }

    /**
     * 減免金額（ゼロは非表示） (例: )
     * @return the Dis_Money
     */
    public String getDis_Money() {
        return Dis_Money;
    }

    /**
     * 減免金額（ゼロは非表示） (例: )
     * @param Dis_Money the Dis_Money to set
     */
    public void setDis_Money(String Dis_Money) {
        this.Dis_Money = Dis_Money;
    }

    /**
     * 調整金１（ゼロは非表示） (例: )
     * @return the Ad_Money1
     */
    public String getAd_Money1() {
        return Ad_Money1;
    }

    /**
     * 調整金１（ゼロは非表示） (例: )
     * @param Ad_Money1 the Ad_Money1 to set
     */
    public void setAd_Money1(String Ad_Money1) {
        this.Ad_Money1 = Ad_Money1;
    }

    /**
     * 調整金２（ゼロは非表示） (例: )
     * @return the Ad_Money2
     */
    public String getAd_Money2() {
        return Ad_Money2;
    }

    /**
     * 調整金２（ゼロは非表示） (例: )
     * @param Ad_Money2 the Ad_Money2 to set
     */
    public void setAd_Money2(String Ad_Money2) {
        this.Ad_Money2 = Ad_Money2;
    }
}