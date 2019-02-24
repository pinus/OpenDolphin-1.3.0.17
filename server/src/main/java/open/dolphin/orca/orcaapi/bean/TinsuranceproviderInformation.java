package open.dolphin.orca.orcaapi.bean;

/**
 * TInsuranceProvider_Information. 保険者一覧情報（繰り返し２５００）
 * @author pns
 */
public class TinsuranceproviderInformation {
    /**
     * 保険者番号 (例: 138057)
     */
    private String InsuranceProvider_Number;

    /**
     * 保険者名称 (例: 文京区)
     */
    private String InsuranceProvider_WholeName;

    /**
     * 保険者名称（短縮１） (例:  )
     */
    private String InsuranceProvider_Name1;

    /**
     * 保険者名称（短縮２） (例:  )
     */
    private String InsuranceProvider_Name2;

    /**
     * 保険者名称（短縮３） (例:  )
     */
    private String InsuranceProvider_Name3;

    /**
     * 保険番号 (例: 060)
     */
    private String Insurance_Number;

    /**
     * 保険番号名称 (例: 国保)
     */
    private String Insurance_Number_Name;

    /**
     * 郵便番号 (例: 1120003)
     */
    private String InsuranceProvider_Address_ZipCode;

    /**
     * 住所 (例: 東京都文京区春日)
     */
    private String InsuranceProvider_WholeAddress1;

    /**
     * 番地方書 (例: １−１６−２１)
     */
    private String InsuranceProvider_WholeAddress2;

    /**
     * 電話番号 (例: 03-3812-7111)
     */
    private String InsuranceProvider_PhoneNumber;

    /**
     * 記号 (例:  )
     */
    private String InsuranceProvider_Symbol;

    /**
     * 給付割合（本人外来） (例:  )
     */
    private String Rate_Outpatient;

    /**
     * 給付割合（本人入院） (例:  )
     */
    private String Rate_Inpatient;

    /**
     * 給付割合（家族外来） (例:  )
     */
    private String Rate_Outpatient_F;

    /**
     * 給付割合（家族入院） (例:  )
     */
    private String Rate_Inpatient_F;

    /**
     * 異動内容 (例:  )
     */
    private String Change_Memo;

    /**
     * 異動年月日 (例:  )
     */
    private String Change_Date;

    /**
     * 保険者番号 (例: 138057)
     * @return the InsuranceProvider_Number
     */
    public String getInsuranceProvider_Number() {
        return InsuranceProvider_Number;
    }

    /**
     * 保険者番号 (例: 138057)
     * @param InsuranceProvider_Number the InsuranceProvider_Number to set
     */
    public void setInsuranceProvider_Number(String InsuranceProvider_Number) {
        this.InsuranceProvider_Number = InsuranceProvider_Number;
    }

    /**
     * 保険者名称 (例: 文京区)
     * @return the InsuranceProvider_WholeName
     */
    public String getInsuranceProvider_WholeName() {
        return InsuranceProvider_WholeName;
    }

    /**
     * 保険者名称 (例: 文京区)
     * @param InsuranceProvider_WholeName the InsuranceProvider_WholeName to set
     */
    public void setInsuranceProvider_WholeName(String InsuranceProvider_WholeName) {
        this.InsuranceProvider_WholeName = InsuranceProvider_WholeName;
    }

    /**
     * 保険者名称（短縮１） (例:  )
     * @return the InsuranceProvider_Name1
     */
    public String getInsuranceProvider_Name1() {
        return InsuranceProvider_Name1;
    }

    /**
     * 保険者名称（短縮１） (例:  )
     * @param InsuranceProvider_Name1 the InsuranceProvider_Name1 to set
     */
    public void setInsuranceProvider_Name1(String InsuranceProvider_Name1) {
        this.InsuranceProvider_Name1 = InsuranceProvider_Name1;
    }

    /**
     * 保険者名称（短縮２） (例:  )
     * @return the InsuranceProvider_Name2
     */
    public String getInsuranceProvider_Name2() {
        return InsuranceProvider_Name2;
    }

    /**
     * 保険者名称（短縮２） (例:  )
     * @param InsuranceProvider_Name2 the InsuranceProvider_Name2 to set
     */
    public void setInsuranceProvider_Name2(String InsuranceProvider_Name2) {
        this.InsuranceProvider_Name2 = InsuranceProvider_Name2;
    }

    /**
     * 保険者名称（短縮３） (例:  )
     * @return the InsuranceProvider_Name3
     */
    public String getInsuranceProvider_Name3() {
        return InsuranceProvider_Name3;
    }

    /**
     * 保険者名称（短縮３） (例:  )
     * @param InsuranceProvider_Name3 the InsuranceProvider_Name3 to set
     */
    public void setInsuranceProvider_Name3(String InsuranceProvider_Name3) {
        this.InsuranceProvider_Name3 = InsuranceProvider_Name3;
    }

    /**
     * 保険番号 (例: 060)
     * @return the Insurance_Number
     */
    public String getInsurance_Number() {
        return Insurance_Number;
    }

    /**
     * 保険番号 (例: 060)
     * @param Insurance_Number the Insurance_Number to set
     */
    public void setInsurance_Number(String Insurance_Number) {
        this.Insurance_Number = Insurance_Number;
    }

    /**
     * 保険番号名称 (例: 国保)
     * @return the Insurance_Number_Name
     */
    public String getInsurance_Number_Name() {
        return Insurance_Number_Name;
    }

    /**
     * 保険番号名称 (例: 国保)
     * @param Insurance_Number_Name the Insurance_Number_Name to set
     */
    public void setInsurance_Number_Name(String Insurance_Number_Name) {
        this.Insurance_Number_Name = Insurance_Number_Name;
    }

    /**
     * 郵便番号 (例: 1120003)
     * @return the InsuranceProvider_Address_ZipCode
     */
    public String getInsuranceProvider_Address_ZipCode() {
        return InsuranceProvider_Address_ZipCode;
    }

    /**
     * 郵便番号 (例: 1120003)
     * @param InsuranceProvider_Address_ZipCode the InsuranceProvider_Address_ZipCode to set
     */
    public void setInsuranceProvider_Address_ZipCode(String InsuranceProvider_Address_ZipCode) {
        this.InsuranceProvider_Address_ZipCode = InsuranceProvider_Address_ZipCode;
    }

    /**
     * 住所 (例: 東京都文京区春日)
     * @return the InsuranceProvider_WholeAddress1
     */
    public String getInsuranceProvider_WholeAddress1() {
        return InsuranceProvider_WholeAddress1;
    }

    /**
     * 住所 (例: 東京都文京区春日)
     * @param InsuranceProvider_WholeAddress1 the InsuranceProvider_WholeAddress1 to set
     */
    public void setInsuranceProvider_WholeAddress1(String InsuranceProvider_WholeAddress1) {
        this.InsuranceProvider_WholeAddress1 = InsuranceProvider_WholeAddress1;
    }

    /**
     * 番地方書 (例: １−１６−２１)
     * @return the InsuranceProvider_WholeAddress2
     */
    public String getInsuranceProvider_WholeAddress2() {
        return InsuranceProvider_WholeAddress2;
    }

    /**
     * 番地方書 (例: １−１６−２１)
     * @param InsuranceProvider_WholeAddress2 the InsuranceProvider_WholeAddress2 to set
     */
    public void setInsuranceProvider_WholeAddress2(String InsuranceProvider_WholeAddress2) {
        this.InsuranceProvider_WholeAddress2 = InsuranceProvider_WholeAddress2;
    }

    /**
     * 電話番号 (例: 03-3812-7111)
     * @return the InsuranceProvider_PhoneNumber
     */
    public String getInsuranceProvider_PhoneNumber() {
        return InsuranceProvider_PhoneNumber;
    }

    /**
     * 電話番号 (例: 03-3812-7111)
     * @param InsuranceProvider_PhoneNumber the InsuranceProvider_PhoneNumber to set
     */
    public void setInsuranceProvider_PhoneNumber(String InsuranceProvider_PhoneNumber) {
        this.InsuranceProvider_PhoneNumber = InsuranceProvider_PhoneNumber;
    }

    /**
     * 記号 (例:  )
     * @return the InsuranceProvider_Symbol
     */
    public String getInsuranceProvider_Symbol() {
        return InsuranceProvider_Symbol;
    }

    /**
     * 記号 (例:  )
     * @param InsuranceProvider_Symbol the InsuranceProvider_Symbol to set
     */
    public void setInsuranceProvider_Symbol(String InsuranceProvider_Symbol) {
        this.InsuranceProvider_Symbol = InsuranceProvider_Symbol;
    }

    /**
     * 給付割合（本人外来） (例:  )
     * @return the Rate_Outpatient
     */
    public String getRate_Outpatient() {
        return Rate_Outpatient;
    }

    /**
     * 給付割合（本人外来） (例:  )
     * @param Rate_Outpatient the Rate_Outpatient to set
     */
    public void setRate_Outpatient(String Rate_Outpatient) {
        this.Rate_Outpatient = Rate_Outpatient;
    }

    /**
     * 給付割合（本人入院） (例:  )
     * @return the Rate_Inpatient
     */
    public String getRate_Inpatient() {
        return Rate_Inpatient;
    }

    /**
     * 給付割合（本人入院） (例:  )
     * @param Rate_Inpatient the Rate_Inpatient to set
     */
    public void setRate_Inpatient(String Rate_Inpatient) {
        this.Rate_Inpatient = Rate_Inpatient;
    }

    /**
     * 給付割合（家族外来） (例:  )
     * @return the Rate_Outpatient_F
     */
    public String getRate_Outpatient_F() {
        return Rate_Outpatient_F;
    }

    /**
     * 給付割合（家族外来） (例:  )
     * @param Rate_Outpatient_F the Rate_Outpatient_F to set
     */
    public void setRate_Outpatient_F(String Rate_Outpatient_F) {
        this.Rate_Outpatient_F = Rate_Outpatient_F;
    }

    /**
     * 給付割合（家族入院） (例:  )
     * @return the Rate_Inpatient_F
     */
    public String getRate_Inpatient_F() {
        return Rate_Inpatient_F;
    }

    /**
     * 給付割合（家族入院） (例:  )
     * @param Rate_Inpatient_F the Rate_Inpatient_F to set
     */
    public void setRate_Inpatient_F(String Rate_Inpatient_F) {
        this.Rate_Inpatient_F = Rate_Inpatient_F;
    }

    /**
     * 異動内容 (例:  )
     * @return the Change_Memo
     */
    public String getChange_Memo() {
        return Change_Memo;
    }

    /**
     * 異動内容 (例:  )
     * @param Change_Memo the Change_Memo to set
     */
    public void setChange_Memo(String Change_Memo) {
        this.Change_Memo = Change_Memo;
    }

    /**
     * 異動年月日 (例:  )
     * @return the Change_Date
     */
    public String getChange_Date() {
        return Change_Date;
    }

    /**
     * 異動年月日 (例:  )
     * @param Change_Date the Change_Date to set
     */
    public void setChange_Date(String Change_Date) {
        this.Change_Date = Change_Date;
    }
}