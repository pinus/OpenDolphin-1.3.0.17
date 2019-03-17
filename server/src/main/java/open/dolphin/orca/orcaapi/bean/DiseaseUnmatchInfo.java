package open.dolphin.orca.orcaapi.bean;

/**
 * Disease_Unmatch_Info. 不一致病名一覧(繰り返し　５０)
 *
 * @author pns
 */
public class DiseaseUnmatchInfo {
    /**
     * 一連病名コード (例: 5609002)
     */
    private String Disease_Code;

    /**
     * 一連病名 (例: 亜イレウス)
     */
    private String Disease_Name;

    /**
     * 補足コメント名称 (例:  )
     */
    private String Disease_Supplement_Name;

    /**
     * 補足コメントコード情報(繰り返し　３) (例: ZZZ2056)
     */
    private DiseaseSupplementSingle[] Disease_Supplement_Single;

    /**
     * 入外区分（Ｉ：入院、Ｏ：入院外、空白：入外） (例:  )
     */
    private String Disease_InOut;

    /**
     * 主病フラグ（PD：主疾患） (例:  )
     */
    private String Disease_Category;

    /**
     * 疑いフラグ (例:  )
     */
    private String Disease_SuspectedFlag;

    /**
     * 開始日 (例: 2015-01-15)
     */
    private String Disease_StartDate;

    /**
     * 転帰日 (例:  )
     */
    private String Disease_EndDate;

    /**
     * 転帰区分 (例:  )
     */
    private String Disease_OutCome;

    /**
     * カルテ病名 (例:  )
     */
    private String Disease_Karte_Name;

    /**
     * 疾患区分 (例:  )
     */
    private String Disease_Class;

    /**
     * 保険組合せ番号 (例:  )
     */
    private String Insurance_Combination_Number;

    /**
     * レセプト表示 (例:  )
     */
    private String Disease_Receipt_Print;

    /**
     * レセプト表示期間 (例:  )
     */
    private String Disease_Receipt_Print_Period;

    /**
     * 保険病名 (例:  )
     */
    private String Insurance_Disease;

    /**
     * 退院証明書 (例:  )
     */
    private String Discharge_Certificate;

    /**
     * 原疾患区分 (例:  )
     */
    private String Main_Disease_Class;

    /**
     * 合併症区分 (例:  )
     */
    private String Sub_Disease_Class;

    /**
     * 急性フラグ（A：急性） (例:  )
     */
    private String Disease_AcuteFlag;

    /**
     * 一連病名コード (例: 5609002)
     *
     * @return the Disease_Code
     */
    public String getDisease_Code() {
        return Disease_Code;
    }

    /**
     * 一連病名コード (例: 5609002)
     *
     * @param Disease_Code the Disease_Code to set
     */
    public void setDisease_Code(String Disease_Code) {
        this.Disease_Code = Disease_Code;
    }

    /**
     * 一連病名 (例: 亜イレウス)
     *
     * @return the Disease_Name
     */
    public String getDisease_Name() {
        return Disease_Name;
    }

    /**
     * 一連病名 (例: 亜イレウス)
     *
     * @param Disease_Name the Disease_Name to set
     */
    public void setDisease_Name(String Disease_Name) {
        this.Disease_Name = Disease_Name;
    }

    /**
     * 補足コメント名称 (例:  )
     *
     * @return the Disease_Supplement_Name
     */
    public String getDisease_Supplement_Name() {
        return Disease_Supplement_Name;
    }

    /**
     * 補足コメント名称 (例:  )
     *
     * @param Disease_Supplement_Name the Disease_Supplement_Name to set
     */
    public void setDisease_Supplement_Name(String Disease_Supplement_Name) {
        this.Disease_Supplement_Name = Disease_Supplement_Name;
    }

    /**
     * 補足コメントコード情報(繰り返し　３) (例: ZZZ2056)
     *
     * @return the Disease_Supplement_Single
     */
    public DiseaseSupplementSingle[] getDisease_Supplement_Single() {
        return Disease_Supplement_Single;
    }

    /**
     * 補足コメントコード情報(繰り返し　３) (例: ZZZ2056)
     *
     * @param Disease_Supplement_Single the Disease_Supplement_Single to set
     */
    public void setDisease_Supplement_Single(DiseaseSupplementSingle[] Disease_Supplement_Single) {
        this.Disease_Supplement_Single = Disease_Supplement_Single;
    }

    /**
     * 入外区分（Ｉ：入院、Ｏ：入院外、空白：入外） (例:  )
     *
     * @return the Disease_InOut
     */
    public String getDisease_InOut() {
        return Disease_InOut;
    }

    /**
     * 入外区分（Ｉ：入院、Ｏ：入院外、空白：入外） (例:  )
     *
     * @param Disease_InOut the Disease_InOut to set
     */
    public void setDisease_InOut(String Disease_InOut) {
        this.Disease_InOut = Disease_InOut;
    }

    /**
     * 主病フラグ（PD：主疾患） (例:  )
     *
     * @return the Disease_Category
     */
    public String getDisease_Category() {
        return Disease_Category;
    }

    /**
     * 主病フラグ（PD：主疾患） (例:  )
     *
     * @param Disease_Category the Disease_Category to set
     */
    public void setDisease_Category(String Disease_Category) {
        this.Disease_Category = Disease_Category;
    }

    /**
     * 疑いフラグ (例:  )
     *
     * @return the Disease_SuspectedFlag
     */
    public String getDisease_SuspectedFlag() {
        return Disease_SuspectedFlag;
    }

    /**
     * 疑いフラグ (例:  )
     *
     * @param Disease_SuspectedFlag the Disease_SuspectedFlag to set
     */
    public void setDisease_SuspectedFlag(String Disease_SuspectedFlag) {
        this.Disease_SuspectedFlag = Disease_SuspectedFlag;
    }

    /**
     * 開始日 (例: 2015-01-15)
     *
     * @return the Disease_StartDate
     */
    public String getDisease_StartDate() {
        return Disease_StartDate;
    }

    /**
     * 開始日 (例: 2015-01-15)
     *
     * @param Disease_StartDate the Disease_StartDate to set
     */
    public void setDisease_StartDate(String Disease_StartDate) {
        this.Disease_StartDate = Disease_StartDate;
    }

    /**
     * 転帰日 (例:  )
     *
     * @return the Disease_EndDate
     */
    public String getDisease_EndDate() {
        return Disease_EndDate;
    }

    /**
     * 転帰日 (例:  )
     *
     * @param Disease_EndDate the Disease_EndDate to set
     */
    public void setDisease_EndDate(String Disease_EndDate) {
        this.Disease_EndDate = Disease_EndDate;
    }

    /**
     * 転帰区分 (例:  )
     *
     * @return the Disease_OutCome
     */
    public String getDisease_OutCome() {
        return Disease_OutCome;
    }

    /**
     * 転帰区分 (例:  )
     *
     * @param Disease_OutCome the Disease_OutCome to set
     */
    public void setDisease_OutCome(String Disease_OutCome) {
        this.Disease_OutCome = Disease_OutCome;
    }

    /**
     * カルテ病名 (例:  )
     *
     * @return the Disease_Karte_Name
     */
    public String getDisease_Karte_Name() {
        return Disease_Karte_Name;
    }

    /**
     * カルテ病名 (例:  )
     *
     * @param Disease_Karte_Name the Disease_Karte_Name to set
     */
    public void setDisease_Karte_Name(String Disease_Karte_Name) {
        this.Disease_Karte_Name = Disease_Karte_Name;
    }

    /**
     * 疾患区分 (例:  )
     *
     * @return the Disease_Class
     */
    public String getDisease_Class() {
        return Disease_Class;
    }

    /**
     * 疾患区分 (例:  )
     *
     * @param Disease_Class the Disease_Class to set
     */
    public void setDisease_Class(String Disease_Class) {
        this.Disease_Class = Disease_Class;
    }

    /**
     * 保険組合せ番号 (例:  )
     *
     * @return the Insurance_Combination_Number
     */
    public String getInsurance_Combination_Number() {
        return Insurance_Combination_Number;
    }

    /**
     * 保険組合せ番号 (例:  )
     *
     * @param Insurance_Combination_Number the Insurance_Combination_Number to set
     */
    public void setInsurance_Combination_Number(String Insurance_Combination_Number) {
        this.Insurance_Combination_Number = Insurance_Combination_Number;
    }

    /**
     * レセプト表示 (例:  )
     *
     * @return the Disease_Receipt_Print
     */
    public String getDisease_Receipt_Print() {
        return Disease_Receipt_Print;
    }

    /**
     * レセプト表示 (例:  )
     *
     * @param Disease_Receipt_Print the Disease_Receipt_Print to set
     */
    public void setDisease_Receipt_Print(String Disease_Receipt_Print) {
        this.Disease_Receipt_Print = Disease_Receipt_Print;
    }

    /**
     * レセプト表示期間 (例:  )
     *
     * @return the Disease_Receipt_Print_Period
     */
    public String getDisease_Receipt_Print_Period() {
        return Disease_Receipt_Print_Period;
    }

    /**
     * レセプト表示期間 (例:  )
     *
     * @param Disease_Receipt_Print_Period the Disease_Receipt_Print_Period to set
     */
    public void setDisease_Receipt_Print_Period(String Disease_Receipt_Print_Period) {
        this.Disease_Receipt_Print_Period = Disease_Receipt_Print_Period;
    }

    /**
     * 保険病名 (例:  )
     *
     * @return the Insurance_Disease
     */
    public String getInsurance_Disease() {
        return Insurance_Disease;
    }

    /**
     * 保険病名 (例:  )
     *
     * @param Insurance_Disease the Insurance_Disease to set
     */
    public void setInsurance_Disease(String Insurance_Disease) {
        this.Insurance_Disease = Insurance_Disease;
    }

    /**
     * 退院証明書 (例:  )
     *
     * @return the Discharge_Certificate
     */
    public String getDischarge_Certificate() {
        return Discharge_Certificate;
    }

    /**
     * 退院証明書 (例:  )
     *
     * @param Discharge_Certificate the Discharge_Certificate to set
     */
    public void setDischarge_Certificate(String Discharge_Certificate) {
        this.Discharge_Certificate = Discharge_Certificate;
    }

    /**
     * 原疾患区分 (例:  )
     *
     * @return the Main_Disease_Class
     */
    public String getMain_Disease_Class() {
        return Main_Disease_Class;
    }

    /**
     * 原疾患区分 (例:  )
     *
     * @param Main_Disease_Class the Main_Disease_Class to set
     */
    public void setMain_Disease_Class(String Main_Disease_Class) {
        this.Main_Disease_Class = Main_Disease_Class;
    }

    /**
     * 合併症区分 (例:  )
     *
     * @return the Sub_Disease_Class
     */
    public String getSub_Disease_Class() {
        return Sub_Disease_Class;
    }

    /**
     * 合併症区分 (例:  )
     *
     * @param Sub_Disease_Class the Sub_Disease_Class to set
     */
    public void setSub_Disease_Class(String Sub_Disease_Class) {
        this.Sub_Disease_Class = Sub_Disease_Class;
    }

    /**
     * Disease_AcuteFlag
     *
     * @return Disease_AcuteFlag
     */
    public String getDisease_AcuteFlag() {
        return Disease_AcuteFlag;
    }

    /**
     * Disease_AcuteFlag
     *
     * @param Disease_AcuteFlag to set
     */
    public void setDisease_AcuteFlag(String Disease_AcuteFlag) {
        this.Disease_AcuteFlag = Disease_AcuteFlag;
    }
}