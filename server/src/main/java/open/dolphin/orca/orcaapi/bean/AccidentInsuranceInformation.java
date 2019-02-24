package open.dolphin.orca.orcaapi.bean;

/**
 * Accident_Insurance_Information. 労災情報
 * @author pns
 */
public class AccidentInsuranceInformation {
    /**
     * 労災自賠保険区分 (例: 短期給付)
     */
    private String Accident_Insurance_WholeName;

    /**
     * 傷病の部位 (例: 右手指)
     */
    private String Disease_Location;

    /**
     * 傷病年月日 (例: 2014-08-12)
     */
    private String Disease_Date;

    /**
     * 労働保険番号 (例: 12345678901)
     */
    private String Accident_Insurance_Number;

    /**
     * 年金証書番号 (例: 123456789)
     */
    private String PensionCertificate_Number;

    /**
     * 災害区分 (例: 業務中の災害)
     */
    private String Accident_Class;

    /**
     * 災害区分名称 (例:  )
     */
    private String Accident_Class_Name;

    /**
     * 労働基準監督署コード (例: 32101)
     */
    private String Labor_Station_Code;

    /**
     * 労働基準監督署 (例: 松江)
     */
    private String Labor_Station_Code_Name;

    /**
     * 事業所情報 (例:  )
     */
    private LiabilityOfficeInformation Liability_Office_Information;

    /**
     * 自賠責保険会社名 (例: オルカ自賠責保険)
     */
    private String Liability_Insurance_Office_Name;

    /**
     * アフターケア　健康管理手帳番号 (例: 1234567890123)
     */
    private String PersonalHealthRecord_Number;

    /**
     * アフターケア　損傷区分情報 (例:  )
     */
    private DamageClass Damage_Class;

    /**
     * 新規継続区分 (例:  )
     */
    private String Accident_Continuous;

    /**
     * 新規継続区分名称 (例:  )
     */
    private String Accident_Continuous_Name;

    /**
     * 転帰事由 (例:  )
     */
    private String Outcome_Reason;

    /**
     * 転帰事由名称 (例:  )
     */
    private String Outcome_Reason_Name;

    /**
     * 四肢特例区分 (例:  )
     */
    private String Limbs_Exception;

    /**
     * 四肢特例区分名称 (例:  )
     */
    private String Limbs_Exception_Name;

    /**
     * 労災レセ回数記載基準年月 (例:  )
     */
    private String Accident_Base_Month;

    /**
     * 労災レセ回数記載回数 (例:  )
     */
    private String Accident_Receipt_Count;

    /**
     * 自賠責請求区分 (例:  )
     */
    private String Liability_Insurance;

    /**
     * 自賠責請求区分名称 (例:  )
     */
    private String Liability_Insurance_Name;

    /**
     * 自賠責担当医コード (例:  )
     */
    private String Liability_Physician_Code;

    /**
     * 自賠責担当医名称 (例:  )
     */
    private String Liability_Physician_Code_Name;

    /**
     * 第三者行為 現物支給区分 (例:  )
     */
    private String Third_Party_Supply;

    /**
     * 第三者行為 現物支給名称 (例:  )
     */
    private String Third_Party_Supply_Name;

    /**
     * 第三者行為 特記事項区分 (例:  )
     */
    private String Third_Party_Report;

    /**
     * 第三者行為 特記事項名称 (例:  )
     */
    private String Third_Party_Report_Name;

    /**
     * 労災自賠保険区分 (例: 短期給付)
     * @return the Accident_Insurance_WholeName
     */
    public String getAccident_Insurance_WholeName() {
        return Accident_Insurance_WholeName;
    }

    /**
     * 労災自賠保険区分 (例: 短期給付)
     * @param Accident_Insurance_WholeName the Accident_Insurance_WholeName to set
     */
    public void setAccident_Insurance_WholeName(String Accident_Insurance_WholeName) {
        this.Accident_Insurance_WholeName = Accident_Insurance_WholeName;
    }

    /**
     * 傷病の部位 (例: 右手指)
     * @return the Disease_Location
     */
    public String getDisease_Location() {
        return Disease_Location;
    }

    /**
     * 傷病の部位 (例: 右手指)
     * @param Disease_Location the Disease_Location to set
     */
    public void setDisease_Location(String Disease_Location) {
        this.Disease_Location = Disease_Location;
    }

    /**
     * 傷病年月日 (例: 2014-08-12)
     * @return the Disease_Date
     */
    public String getDisease_Date() {
        return Disease_Date;
    }

    /**
     * 傷病年月日 (例: 2014-08-12)
     * @param Disease_Date the Disease_Date to set
     */
    public void setDisease_Date(String Disease_Date) {
        this.Disease_Date = Disease_Date;
    }

    /**
     * 労働保険番号 (例: 12345678901)
     * @return the Accident_Insurance_Number
     */
    public String getAccident_Insurance_Number() {
        return Accident_Insurance_Number;
    }

    /**
     * 労働保険番号 (例: 12345678901)
     * @param Accident_Insurance_Number the Accident_Insurance_Number to set
     */
    public void setAccident_Insurance_Number(String Accident_Insurance_Number) {
        this.Accident_Insurance_Number = Accident_Insurance_Number;
    }

    /**
     * 年金証書番号 (例: 123456789)
     * @return the PensionCertificate_Number
     */
    public String getPensionCertificate_Number() {
        return PensionCertificate_Number;
    }

    /**
     * 年金証書番号 (例: 123456789)
     * @param PensionCertificate_Number the PensionCertificate_Number to set
     */
    public void setPensionCertificate_Number(String PensionCertificate_Number) {
        this.PensionCertificate_Number = PensionCertificate_Number;
    }

    /**
     * 災害区分 (例: 業務中の災害)
     * @return the Accident_Class
     */
    public String getAccident_Class() {
        return Accident_Class;
    }

    /**
     * 災害区分 (例: 業務中の災害)
     * @param Accident_Class the Accident_Class to set
     */
    public void setAccident_Class(String Accident_Class) {
        this.Accident_Class = Accident_Class;
    }

    /**
     * 災害区分名称 (例:  )
     * @return the Accident_Class_Name
     */
    public String getAccident_Class_Name() {
        return Accident_Class_Name;
    }

    /**
     * 災害区分名称 (例:  )
     * @param Accident_Class_Name the Accident_Class_Name to set
     */
    public void setAccident_Class_Name(String Accident_Class_Name) {
        this.Accident_Class_Name = Accident_Class_Name;
    }

    /**
     * 労働基準監督署コード (例: 32101)
     * @return the Labor_Station_Code
     */
    public String getLabor_Station_Code() {
        return Labor_Station_Code;
    }

    /**
     * 労働基準監督署コード (例: 32101)
     * @param Labor_Station_Code the Labor_Station_Code to set
     */
    public void setLabor_Station_Code(String Labor_Station_Code) {
        this.Labor_Station_Code = Labor_Station_Code;
    }

    /**
     * 労働基準監督署 (例: 松江)
     * @return the Labor_Station_Code_Name
     */
    public String getLabor_Station_Code_Name() {
        return Labor_Station_Code_Name;
    }

    /**
     * 労働基準監督署 (例: 松江)
     * @param Labor_Station_Code_Name the Labor_Station_Code_Name to set
     */
    public void setLabor_Station_Code_Name(String Labor_Station_Code_Name) {
        this.Labor_Station_Code_Name = Labor_Station_Code_Name;
    }

    /**
     * 事業所情報 (例:  )
     * @return the Liability_Office_Information
     */
    public LiabilityOfficeInformation getLiability_Office_Information() {
        return Liability_Office_Information;
    }

    /**
     * 事業所情報 (例:  )
     * @param Liability_Office_Information the Liability_Office_Information to set
     */
    public void setLiability_Office_Information(LiabilityOfficeInformation Liability_Office_Information) {
        this.Liability_Office_Information = Liability_Office_Information;
    }

    /**
     * 自賠責保険会社名 (例: オルカ自賠責保険)
     * @return the Liability_Insurance_Office_Name
     */
    public String getLiability_Insurance_Office_Name() {
        return Liability_Insurance_Office_Name;
    }

    /**
     * 自賠責保険会社名 (例: オルカ自賠責保険)
     * @param Liability_Insurance_Office_Name the Liability_Insurance_Office_Name to set
     */
    public void setLiability_Insurance_Office_Name(String Liability_Insurance_Office_Name) {
        this.Liability_Insurance_Office_Name = Liability_Insurance_Office_Name;
    }

    /**
     * アフターケア　健康管理手帳番号 (例: 1234567890123)
     * @return the PersonalHealthRecord_Number
     */
    public String getPersonalHealthRecord_Number() {
        return PersonalHealthRecord_Number;
    }

    /**
     * アフターケア　健康管理手帳番号 (例: 1234567890123)
     * @param PersonalHealthRecord_Number the PersonalHealthRecord_Number to set
     */
    public void setPersonalHealthRecord_Number(String PersonalHealthRecord_Number) {
        this.PersonalHealthRecord_Number = PersonalHealthRecord_Number;
    }

    /**
     * アフターケア　損傷区分情報 (例:  )
     * @return the Damage_Class
     */
    public DamageClass getDamage_Class() {
        return Damage_Class;
    }

    /**
     * アフターケア　損傷区分情報 (例:  )
     * @param Damage_Class the Damage_Class to set
     */
    public void setDamage_Class(DamageClass Damage_Class) {
        this.Damage_Class = Damage_Class;
    }

    /**
     * 新規継続区分 (例:  )
     * @return the Accident_Continuous
     */
    public String getAccident_Continuous() {
        return Accident_Continuous;
    }

    /**
     * 新規継続区分 (例:  )
     * @param Accident_Continuous the Accident_Continuous to set
     */
    public void setAccident_Continuous(String Accident_Continuous) {
        this.Accident_Continuous = Accident_Continuous;
    }

    /**
     * 新規継続区分名称 (例:  )
     * @return the Accident_Continuous_Name
     */
    public String getAccident_Continuous_Name() {
        return Accident_Continuous_Name;
    }

    /**
     * 新規継続区分名称 (例:  )
     * @param Accident_Continuous_Name the Accident_Continuous_Name to set
     */
    public void setAccident_Continuous_Name(String Accident_Continuous_Name) {
        this.Accident_Continuous_Name = Accident_Continuous_Name;
    }

    /**
     * 転帰事由 (例:  )
     * @return the Outcome_Reason
     */
    public String getOutcome_Reason() {
        return Outcome_Reason;
    }

    /**
     * 転帰事由 (例:  )
     * @param Outcome_Reason the Outcome_Reason to set
     */
    public void setOutcome_Reason(String Outcome_Reason) {
        this.Outcome_Reason = Outcome_Reason;
    }

    /**
     * 転帰事由名称 (例:  )
     * @return the Outcome_Reason_Name
     */
    public String getOutcome_Reason_Name() {
        return Outcome_Reason_Name;
    }

    /**
     * 転帰事由名称 (例:  )
     * @param Outcome_Reason_Name the Outcome_Reason_Name to set
     */
    public void setOutcome_Reason_Name(String Outcome_Reason_Name) {
        this.Outcome_Reason_Name = Outcome_Reason_Name;
    }

    /**
     * 四肢特例区分 (例:  )
     * @return the Limbs_Exception
     */
    public String getLimbs_Exception() {
        return Limbs_Exception;
    }

    /**
     * 四肢特例区分 (例:  )
     * @param Limbs_Exception the Limbs_Exception to set
     */
    public void setLimbs_Exception(String Limbs_Exception) {
        this.Limbs_Exception = Limbs_Exception;
    }

    /**
     * 四肢特例区分名称 (例:  )
     * @return the Limbs_Exception_Name
     */
    public String getLimbs_Exception_Name() {
        return Limbs_Exception_Name;
    }

    /**
     * 四肢特例区分名称 (例:  )
     * @param Limbs_Exception_Name the Limbs_Exception_Name to set
     */
    public void setLimbs_Exception_Name(String Limbs_Exception_Name) {
        this.Limbs_Exception_Name = Limbs_Exception_Name;
    }

    /**
     * 労災レセ回数記載基準年月 (例:  )
     * @return the Accident_Base_Month
     */
    public String getAccident_Base_Month() {
        return Accident_Base_Month;
    }

    /**
     * 労災レセ回数記載基準年月 (例:  )
     * @param Accident_Base_Month the Accident_Base_Month to set
     */
    public void setAccident_Base_Month(String Accident_Base_Month) {
        this.Accident_Base_Month = Accident_Base_Month;
    }

    /**
     * 労災レセ回数記載回数 (例:  )
     * @return the Accident_Receipt_Count
     */
    public String getAccident_Receipt_Count() {
        return Accident_Receipt_Count;
    }

    /**
     * 労災レセ回数記載回数 (例:  )
     * @param Accident_Receipt_Count the Accident_Receipt_Count to set
     */
    public void setAccident_Receipt_Count(String Accident_Receipt_Count) {
        this.Accident_Receipt_Count = Accident_Receipt_Count;
    }

    /**
     * 自賠責請求区分 (例:  )
     * @return the Liability_Insurance
     */
    public String getLiability_Insurance() {
        return Liability_Insurance;
    }

    /**
     * 自賠責請求区分 (例:  )
     * @param Liability_Insurance the Liability_Insurance to set
     */
    public void setLiability_Insurance(String Liability_Insurance) {
        this.Liability_Insurance = Liability_Insurance;
    }

    /**
     * 自賠責請求区分名称 (例:  )
     * @return the Liability_Insurance_Name
     */
    public String getLiability_Insurance_Name() {
        return Liability_Insurance_Name;
    }

    /**
     * 自賠責請求区分名称 (例:  )
     * @param Liability_Insurance_Name the Liability_Insurance_Name to set
     */
    public void setLiability_Insurance_Name(String Liability_Insurance_Name) {
        this.Liability_Insurance_Name = Liability_Insurance_Name;
    }

    /**
     * 自賠責担当医コード (例:  )
     * @return the Liability_Physician_Code
     */
    public String getLiability_Physician_Code() {
        return Liability_Physician_Code;
    }

    /**
     * 自賠責担当医コード (例:  )
     * @param Liability_Physician_Code the Liability_Physician_Code to set
     */
    public void setLiability_Physician_Code(String Liability_Physician_Code) {
        this.Liability_Physician_Code = Liability_Physician_Code;
    }

    /**
     * 自賠責担当医名称 (例:  )
     * @return the Liability_Physician_Code_Name
     */
    public String getLiability_Physician_Code_Name() {
        return Liability_Physician_Code_Name;
    }

    /**
     * 自賠責担当医名称 (例:  )
     * @param Liability_Physician_Code_Name the Liability_Physician_Code_Name to set
     */
    public void setLiability_Physician_Code_Name(String Liability_Physician_Code_Name) {
        this.Liability_Physician_Code_Name = Liability_Physician_Code_Name;
    }

    /**
     * 第三者行為 現物支給区分 (例:  )
     * @return the Third_Party_Supply
     */
    public String getThird_Party_Supply() {
        return Third_Party_Supply;
    }

    /**
     * 第三者行為 現物支給区分 (例:  )
     * @param Third_Party_Supply the Third_Party_Supply to set
     */
    public void setThird_Party_Supply(String Third_Party_Supply) {
        this.Third_Party_Supply = Third_Party_Supply;
    }

    /**
     * 第三者行為 現物支給名称 (例:  )
     * @return the Third_Party_Supply_Name
     */
    public String getThird_Party_Supply_Name() {
        return Third_Party_Supply_Name;
    }

    /**
     * 第三者行為 現物支給名称 (例:  )
     * @param Third_Party_Supply_Name the Third_Party_Supply_Name to set
     */
    public void setThird_Party_Supply_Name(String Third_Party_Supply_Name) {
        this.Third_Party_Supply_Name = Third_Party_Supply_Name;
    }

    /**
     * 第三者行為 特記事項区分 (例:  )
     * @return the Third_Party_Report
     */
    public String getThird_Party_Report() {
        return Third_Party_Report;
    }

    /**
     * 第三者行為 特記事項区分 (例:  )
     * @param Third_Party_Report the Third_Party_Report to set
     */
    public void setThird_Party_Report(String Third_Party_Report) {
        this.Third_Party_Report = Third_Party_Report;
    }

    /**
     * 第三者行為 特記事項名称 (例:  )
     * @return the Third_Party_Report_Name
     */
    public String getThird_Party_Report_Name() {
        return Third_Party_Report_Name;
    }

    /**
     * 第三者行為 特記事項名称 (例:  )
     * @param Third_Party_Report_Name the Third_Party_Report_Name to set
     */
    public void setThird_Party_Report_Name(String Third_Party_Report_Name) {
        this.Third_Party_Report_Name = Third_Party_Report_Name;
    }
}