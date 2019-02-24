package open.dolphin.orca.orcaapi.bean;

/**
 * Lsi_Information. 労災自賠責保険適用分（円）
 * @author pns
 */
public class LsiInformation {
    /**
     * 初診(ゼロは非表示） (例: )
     */
    private String Lsi_Fv_Money;

    /**
     * 再診(ゼロは非表示） (例: )
     */
    private String Lsi_Sv_Money;

    /**
     * 指導(ゼロは非表示） (例: )
     */
    private String Lsi_Mm_Money;

    /**
     * その他(ゼロは非表示） (例: )
     */
    private String Lsi_Other_Money;

    /**
     * 初診(ゼロは非表示） (例: )
     * @return the Lsi_Fv_Money
     */
    public String getLsi_Fv_Money() {
        return Lsi_Fv_Money;
    }

    /**
     * 初診(ゼロは非表示） (例: )
     * @param Lsi_Fv_Money the Lsi_Fv_Money to set
     */
    public void setLsi_Fv_Money(String Lsi_Fv_Money) {
        this.Lsi_Fv_Money = Lsi_Fv_Money;
    }

    /**
     * 再診(ゼロは非表示） (例: )
     * @return the Lsi_Sv_Money
     */
    public String getLsi_Sv_Money() {
        return Lsi_Sv_Money;
    }

    /**
     * 再診(ゼロは非表示） (例: )
     * @param Lsi_Sv_Money the Lsi_Sv_Money to set
     */
    public void setLsi_Sv_Money(String Lsi_Sv_Money) {
        this.Lsi_Sv_Money = Lsi_Sv_Money;
    }

    /**
     * 指導(ゼロは非表示） (例: )
     * @return the Lsi_Mm_Money
     */
    public String getLsi_Mm_Money() {
        return Lsi_Mm_Money;
    }

    /**
     * 指導(ゼロは非表示） (例: )
     * @param Lsi_Mm_Money the Lsi_Mm_Money to set
     */
    public void setLsi_Mm_Money(String Lsi_Mm_Money) {
        this.Lsi_Mm_Money = Lsi_Mm_Money;
    }

    /**
     * その他(ゼロは非表示） (例: )
     * @return the Lsi_Other_Money
     */
    public String getLsi_Other_Money() {
        return Lsi_Other_Money;
    }

    /**
     * その他(ゼロは非表示） (例: )
     * @param Lsi_Other_Money the Lsi_Other_Money to set
     */
    public void setLsi_Other_Money(String Lsi_Other_Money) {
        this.Lsi_Other_Money = Lsi_Other_Money;
    }
}