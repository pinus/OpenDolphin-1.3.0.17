package open.dolphin.orca.orcaapi.bean;

/**
 * PublicInsurance_Information. 公費情報(繰り返し4)
 * @author pns
 */
public class PublicinsuranceInformation {
    /**
     * 公費の種類 (例: 019).
     * ※２：一箇所でも設定されていれば、一致する保険組合せが対象に設定されます.
     * 公費単独の場合は、主保険情報は設定する必要はありません。
     */
    private String PublicInsurance_Class;

    /**
     * 公費の制度名称 (例: 原爆一般)
     */
    private String PublicInsurance_Name;

    /**
     * 負担者番号 (例: 19113760)
     */
    private String PublicInsurer_Number;

    /**
     * 受給者番号 (例: 1234566)
     */
    private String PublicInsuredPerson_Number;

    /**
     * 入院ー負担率（割） (例: 0.00)
     */
    private String Rate_Admission;

    /**
     * 入院ー固定額 (例: 0)
     */
    private String Money_Admission;

    /**
     * 外来ー負担率（割） (例: 0.00)
     */
    private String Rate_Outpatient;

    /**
     * 外来ー固定額 (例: 0)
     */
    private String Money_Outpatient;

    /**
     * 適用開始日 (例: 2010-05-01)
     */
    private String Certificate_IssuedDate;

    /**
     * 適用終了日 (例: 9999-12-31)
     */
    private String Certificate_ExpiredDate;

    /**
     * 法別番号 (例: )
     */
    private String PublicInsurance_Identification_Number;

    /**
     * 支払区分(未使用老人公費用) (例: )
     */
    private String PublicInsurance_Paykbn_Number;

    /**
     * 公費の種類 (例: 019).
     * ※２：一箇所でも設定されていれば、一致する保険組合せが対象に設定されます.
     * 公費単独の場合は、主保険情報は設定する必要はありません。
     * @return the PublicInsurance_Class
     */
    public String getPublicInsurance_Class() {
        return PublicInsurance_Class;
    }

    /**
     * 公費の種類 (例: 019).
     * ※２：一箇所でも設定されていれば、一致する保険組合せが対象に設定されます.
     * 公費単独の場合は、主保険情報は設定する必要はありません。
     * @param PublicInsurance_Class the PublicInsurance_Class to set
     */
    public void setPublicInsurance_Class(String PublicInsurance_Class) {
        this.PublicInsurance_Class = PublicInsurance_Class;
    }

    /**
     * 公費の制度名称 (例: 原爆一般)
     * @return the PublicInsurance_Name
     */
    public String getPublicInsurance_Name() {
        return PublicInsurance_Name;
    }

    /**
     * 公費の制度名称 (例: 原爆一般)
     * @param PublicInsurance_Name the PublicInsurance_Name to set
     */
    public void setPublicInsurance_Name(String PublicInsurance_Name) {
        this.PublicInsurance_Name = PublicInsurance_Name;
    }

    /**
     * 負担者番号 (例: 19113760)
     * @return the PublicInsurer_Number
     */
    public String getPublicInsurer_Number() {
        return PublicInsurer_Number;
    }

    /**
     * 負担者番号 (例: 19113760)
     * @param PublicInsurer_Number the PublicInsurer_Number to set
     */
    public void setPublicInsurer_Number(String PublicInsurer_Number) {
        this.PublicInsurer_Number = PublicInsurer_Number;
    }

    /**
     * 受給者番号 (例: 1234566)
     * @return the PublicInsuredPerson_Number
     */
    public String getPublicInsuredPerson_Number() {
        return PublicInsuredPerson_Number;
    }

    /**
     * 受給者番号 (例: 1234566)
     * @param PublicInsuredPerson_Number the PublicInsuredPerson_Number to set
     */
    public void setPublicInsuredPerson_Number(String PublicInsuredPerson_Number) {
        this.PublicInsuredPerson_Number = PublicInsuredPerson_Number;
    }

    /**
     * 入院ー負担率（割） (例: 0.00)
     * @return the Rate_Admission
     */
    public String getRate_Admission() {
        return Rate_Admission;
    }

    /**
     * 入院ー負担率（割） (例: 0.00)
     * @param Rate_Admission the Rate_Admission to set
     */
    public void setRate_Admission(String Rate_Admission) {
        this.Rate_Admission = Rate_Admission;
    }

    /**
     * 入院ー固定額 (例: 0)
     * @return the Money_Admission
     */
    public String getMoney_Admission() {
        return Money_Admission;
    }

    /**
     * 入院ー固定額 (例: 0)
     * @param Money_Admission the Money_Admission to set
     */
    public void setMoney_Admission(String Money_Admission) {
        this.Money_Admission = Money_Admission;
    }

    /**
     * 外来ー負担率（割） (例: 0.00)
     * @return the Rate_Outpatient
     */
    public String getRate_Outpatient() {
        return Rate_Outpatient;
    }

    /**
     * 外来ー負担率（割） (例: 0.00)
     * @param Rate_Outpatient the Rate_Outpatient to set
     */
    public void setRate_Outpatient(String Rate_Outpatient) {
        this.Rate_Outpatient = Rate_Outpatient;
    }

    /**
     * 外来ー固定額 (例: 0)
     * @return the Money_Outpatient
     */
    public String getMoney_Outpatient() {
        return Money_Outpatient;
    }

    /**
     * 外来ー固定額 (例: 0)
     * @param Money_Outpatient the Money_Outpatient to set
     */
    public void setMoney_Outpatient(String Money_Outpatient) {
        this.Money_Outpatient = Money_Outpatient;
    }

    /**
     * 適用開始日 (例: 2010-05-01)
     * @return the Certificate_IssuedDate
     */
    public String getCertificate_IssuedDate() {
        return Certificate_IssuedDate;
    }

    /**
     * 適用開始日 (例: 2010-05-01)
     * @param Certificate_IssuedDate the Certificate_IssuedDate to set
     */
    public void setCertificate_IssuedDate(String Certificate_IssuedDate) {
        this.Certificate_IssuedDate = Certificate_IssuedDate;
    }

    /**
     * 適用終了日 (例: 9999-12-31)
     * @return the Certificate_ExpiredDate
     */
    public String getCertificate_ExpiredDate() {
        return Certificate_ExpiredDate;
    }

    /**
     * 適用終了日 (例: 9999-12-31)
     * @param Certificate_ExpiredDate the Certificate_ExpiredDate to set
     */
    public void setCertificate_ExpiredDate(String Certificate_ExpiredDate) {
        this.Certificate_ExpiredDate = Certificate_ExpiredDate;
    }

    /**
     * PublicInsurance_Identification_Number
     *
     * @return PublicInsurance_Identification_Number
     */
    public String getPublicInsurance_Identification_Number() {
        return PublicInsurance_Identification_Number;
    }

    /**
     * PublicInsurance_Identification_Number
     *
     * @param PublicInsurance_Identification_Number to set
     */
    public void setPublicInsurance_Identification_Number(String PublicInsurance_Identification_Number) {
        this.PublicInsurance_Identification_Number = PublicInsurance_Identification_Number;
    }

    /**
     * PublicInsurance_Paykbn_Number
     *
     * @return PublicInsurance_Paykbn_Number
     */
    public String getPublicInsurance_Paykbn_Number() {
        return PublicInsurance_Paykbn_Number;
    }

    /**
     * PublicInsurance_Paykbn_Number
     *
     * @param PublicInsurance_Paykbn_Number to set
     */
    public void setPublicInsurance_Paykbn_Number(String PublicInsurance_Paykbn_Number) {
        this.PublicInsurance_Paykbn_Number = PublicInsurance_Paykbn_Number;
    }
}
