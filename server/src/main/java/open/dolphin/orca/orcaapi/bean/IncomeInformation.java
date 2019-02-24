package open.dolphin.orca.orcaapi.bean;

/**
 * Income_Information. 請求情報（繰り返し　２００）（並び順は診療日の古い順）
 * @author pns
 */
public class IncomeInformation {
    /**
     * 外来：診療日/入院：請求開始日 (例: 2013-10-01)
     */
    private String Perform_Date;

    /**
     * 請求終了日（入院のみ。外来は非表示） (例: )
     */
    private String Perform_End_Date;

    /**
     * 伝票発行日 (例: 2013-12-15)
     */
    private String IssuedDate;

    /**
     * 入外区分（1：入院、2：入院外） (例: 1)
     */
    private String InOut;

    /**
     * 伝票番号 (例: 0000053)
     */
    private String Invoice_Number;

    /**
     * 保険組合せ番号 (例: 0002)
     */
    private String Insurance_Combination_Number;

    /**
     * 負担割合（%） (例: 0)
     */
    private String Rate_Cd;

    /**
     * 診療科コード (例: 01)
     */
    private String Department_Code;

    /**
     * 診療科名称 (例: 内科)
     */
    private String Department_Name;

    /**
     * 負担額情報 (例: )
     */
    private CdInformation Cd_Information;

    /**
     * 請求点数 (例: )
     */
    private AcPointInformation Ac_Point_Information;

    /**
     * その他自費情報 (例: )
     */
    private OeEtcInformation Oe_Etc_Information;

    /**
     * 労災自賠責保険適用分（円） (例: )
     */
    private LsiInformation Lsi_Information;

    /**
     * 食事・生活療養費（外来は非表示）（食事療養費＋生活療養費＋食事療養費（自費）＋生活療養費（自費）） (例: )
     */
    private String Ml_Cost;

    /**
     * 食事療養費（外来またはゼロは非表示） (例: 19200)
     */
    private String Meal_Cost;

    /**
     * 生活療養費（外来またはゼロは非表示） (例: )
     */
    private String Living_Cost;

    /**
     * 食事療養費（自費）（外来またはゼロは非表示） (例: )
     */
    private String Oe_Meal_Cost;

    /**
     * 生活療養費（自費）（外来またはゼロは非表示） (例: )
     */
    private String Oe_Meal_Smoney;

    /**
     * 食事療養負担金（自費）（外来またはゼロは非表示） (例: )
     */
    private String Oe_Living_Cost;

    /**
     * 生活療養負担金（自費）（外来またはゼロは非表示） (例: )
     */
    private String Oe_Living_Smoney;

    /**
     * 室料差額（外来またはゼロは非表示） (例: 10800)
     */
    private String Room_Charge;

    /**
     * 室料差額消費税再掲（外来またはゼロは非表示） (例: 800)
     */
    private String Tax_In_Room_Charge;

    /**
     * 外来：診療日/入院：請求開始日 (例: 2013-10-01)
     * @return the Perform_Date
     */
    public String getPerform_Date() {
        return Perform_Date;
    }

    /**
     * 外来：診療日/入院：請求開始日 (例: 2013-10-01)
     * @param Perform_Date the Perform_Date to set
     */
    public void setPerform_Date(String Perform_Date) {
        this.Perform_Date = Perform_Date;
    }

    /**
     * 請求終了日（入院のみ。外来は非表示） (例: )
     * @return the Perform_End_Date
     */
    public String getPerform_End_Date() {
        return Perform_End_Date;
    }

    /**
     * 請求終了日（入院のみ。外来は非表示） (例: )
     * @param Perform_End_Date the Perform_End_Date to set
     */
    public void setPerform_End_Date(String Perform_End_Date) {
        this.Perform_End_Date = Perform_End_Date;
    }

    /**
     * 伝票発行日 (例: 2013-12-15)
     * @return the IssuedDate
     */
    public String getIssuedDate() {
        return IssuedDate;
    }

    /**
     * 伝票発行日 (例: 2013-12-15)
     * @param IssuedDate the IssuedDate to set
     */
    public void setIssuedDate(String IssuedDate) {
        this.IssuedDate = IssuedDate;
    }

    /**
     * 入外区分（1：入院、2：入院外） (例: 1)
     * @return the InOut
     */
    public String getInOut() {
        return InOut;
    }

    /**
     * 入外区分（1：入院、2：入院外） (例: 1)
     * @param InOut the InOut to set
     */
    public void setInOut(String InOut) {
        this.InOut = InOut;
    }

    /**
     * 伝票番号 (例: 0000053)
     * @return the Invoice_Number
     */
    public String getInvoice_Number() {
        return Invoice_Number;
    }

    /**
     * 伝票番号 (例: 0000053)
     * @param Invoice_Number the Invoice_Number to set
     */
    public void setInvoice_Number(String Invoice_Number) {
        this.Invoice_Number = Invoice_Number;
    }

    /**
     * 保険組合せ番号 (例: 0002)
     * @return the Insurance_Combination_Number
     */
    public String getInsurance_Combination_Number() {
        return Insurance_Combination_Number;
    }

    /**
     * 保険組合せ番号 (例: 0002)
     * @param Insurance_Combination_Number the Insurance_Combination_Number to set
     */
    public void setInsurance_Combination_Number(String Insurance_Combination_Number) {
        this.Insurance_Combination_Number = Insurance_Combination_Number;
    }

    /**
     * 負担割合（%） (例: 0)
     * @return the Rate_Cd
     */
    public String getRate_Cd() {
        return Rate_Cd;
    }

    /**
     * 負担割合（%） (例: 0)
     * @param Rate_Cd the Rate_Cd to set
     */
    public void setRate_Cd(String Rate_Cd) {
        this.Rate_Cd = Rate_Cd;
    }

    /**
     * 診療科コード (例: 01)
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 診療科コード (例: 01)
     * @param Department_Code the Department_Code to set
     */
    public void setDepartment_Code(String Department_Code) {
        this.Department_Code = Department_Code;
    }

    /**
     * 診療科名称 (例: 内科)
     * @return the Department_Name
     */
    public String getDepartment_Name() {
        return Department_Name;
    }

    /**
     * 診療科名称 (例: 内科)
     * @param Department_Name the Department_Name to set
     */
    public void setDepartment_Name(String Department_Name) {
        this.Department_Name = Department_Name;
    }

    /**
     * 負担額情報 (例: )
     * @return the Cd_Information
     */
    public CdInformation getCd_Information() {
        return Cd_Information;
    }

    /**
     * 負担額情報 (例: )
     * @param Cd_Information the Cd_Information to set
     */
    public void setCd_Information(CdInformation Cd_Information) {
        this.Cd_Information = Cd_Information;
    }

    /**
     * 請求点数 (例: )
     * @return the Ac_Point_Information
     */
    public AcPointInformation getAc_Point_Information() {
        return Ac_Point_Information;
    }

    /**
     * 請求点数 (例: )
     * @param Ac_Point_Information the Ac_Point_Information to set
     */
    public void setAc_Point_Information(AcPointInformation Ac_Point_Information) {
        this.Ac_Point_Information = Ac_Point_Information;
    }

    /**
     * その他自費情報 (例: )
     * @return the Oe_Etc_Information
     */
    public OeEtcInformation getOe_Etc_Information() {
        return Oe_Etc_Information;
    }

    /**
     * その他自費情報 (例: )
     * @param Oe_Etc_Information the Oe_Etc_Information to set
     */
    public void setOe_Etc_Information(OeEtcInformation Oe_Etc_Information) {
        this.Oe_Etc_Information = Oe_Etc_Information;
    }

    /**
     * 労災自賠責保険適用分（円） (例: )
     * @return the Lsi_Information
     */
    public LsiInformation getLsi_Information() {
        return Lsi_Information;
    }

    /**
     * 労災自賠責保険適用分（円） (例: )
     * @param Lsi_Information the Lsi_Information to set
     */
    public void setLsi_Information(LsiInformation Lsi_Information) {
        this.Lsi_Information = Lsi_Information;
    }

    /**
     * 食事・生活療養費（外来は非表示）（食事療養費＋生活療養費＋食事療養費（自費）＋生活療養費（自費）） (例: )
     * @return the Ml_Cost
     */
    public String getMl_Cost() {
        return Ml_Cost;
    }

    /**
     * 食事・生活療養費（外来は非表示）（食事療養費＋生活療養費＋食事療養費（自費）＋生活療養費（自費）） (例: )
     * @param Ml_Cost the Ml_Cost to set
     */
    public void setMl_Cost(String Ml_Cost) {
        this.Ml_Cost = Ml_Cost;
    }

    /**
     * 食事療養費（外来またはゼロは非表示） (例: 19200)
     * @return the Meal_Cost
     */
    public String getMeal_Cost() {
        return Meal_Cost;
    }

    /**
     * 食事療養費（外来またはゼロは非表示） (例: 19200)
     * @param Meal_Cost the Meal_Cost to set
     */
    public void setMeal_Cost(String Meal_Cost) {
        this.Meal_Cost = Meal_Cost;
    }

    /**
     * 生活療養費（外来またはゼロは非表示） (例: )
     * @return the Living_Cost
     */
    public String getLiving_Cost() {
        return Living_Cost;
    }

    /**
     * 生活療養費（外来またはゼロは非表示） (例: )
     * @param Living_Cost the Living_Cost to set
     */
    public void setLiving_Cost(String Living_Cost) {
        this.Living_Cost = Living_Cost;
    }

    /**
     * 食事療養費（自費）（外来またはゼロは非表示） (例: )
     * @return the Oe_Meal_Cost
     */
    public String getOe_Meal_Cost() {
        return Oe_Meal_Cost;
    }

    /**
     * 食事療養費（自費）（外来またはゼロは非表示） (例: )
     * @param Oe_Meal_Cost the Oe_Meal_Cost to set
     */
    public void setOe_Meal_Cost(String Oe_Meal_Cost) {
        this.Oe_Meal_Cost = Oe_Meal_Cost;
    }

    /**
     * 生活療養費（自費）（外来またはゼロは非表示） (例: )
     * @return the Oe_Meal_Smoney
     */
    public String getOe_Meal_Smoney() {
        return Oe_Meal_Smoney;
    }

    /**
     * 生活療養費（自費）（外来またはゼロは非表示） (例: )
     * @param Oe_Meal_Smoney the Oe_Meal_Smoney to set
     */
    public void setOe_Meal_Smoney(String Oe_Meal_Smoney) {
        this.Oe_Meal_Smoney = Oe_Meal_Smoney;
    }

    /**
     * 食事療養負担金（自費）（外来またはゼロは非表示） (例: )
     * @return the Oe_Living_Cost
     */
    public String getOe_Living_Cost() {
        return Oe_Living_Cost;
    }

    /**
     * 食事療養負担金（自費）（外来またはゼロは非表示） (例: )
     * @param Oe_Living_Cost the Oe_Living_Cost to set
     */
    public void setOe_Living_Cost(String Oe_Living_Cost) {
        this.Oe_Living_Cost = Oe_Living_Cost;
    }

    /**
     * 生活療養負担金（自費）（外来またはゼロは非表示） (例: )
     * @return the Oe_Living_Smoney
     */
    public String getOe_Living_Smoney() {
        return Oe_Living_Smoney;
    }

    /**
     * 生活療養負担金（自費）（外来またはゼロは非表示） (例: )
     * @param Oe_Living_Smoney the Oe_Living_Smoney to set
     */
    public void setOe_Living_Smoney(String Oe_Living_Smoney) {
        this.Oe_Living_Smoney = Oe_Living_Smoney;
    }

    /**
     * 室料差額（外来またはゼロは非表示） (例: 10800)
     * @return the Room_Charge
     */
    public String getRoom_Charge() {
        return Room_Charge;
    }

    /**
     * 室料差額（外来またはゼロは非表示） (例: 10800)
     * @param Room_Charge the Room_Charge to set
     */
    public void setRoom_Charge(String Room_Charge) {
        this.Room_Charge = Room_Charge;
    }

    /**
     * 室料差額消費税再掲（外来またはゼロは非表示） (例: 800)
     * @return the Tax_In_Room_Charge
     */
    public String getTax_In_Room_Charge() {
        return Tax_In_Room_Charge;
    }

    /**
     * 室料差額消費税再掲（外来またはゼロは非表示） (例: 800)
     * @param Tax_In_Room_Charge the Tax_In_Room_Charge to set
     */
    public void setTax_In_Room_Charge(String Tax_In_Room_Charge) {
        this.Tax_In_Room_Charge = Tax_In_Room_Charge;
    }
}