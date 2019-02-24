package open.dolphin.orca.orcaapi.bean;

/**
 * Insurance. 介護保険情報（繰り返し　１０）
 * @author pns
 */
public class Insurance {
    /**
     * 保険者番号 (例: 123456)
     */
    private String InsuranceProvider_Number;

    /**
     * 被保険者番号 (例: 098765)
     */
    private String HealthInsuredPerson_Number;

    /**
     * 開始 (例: 2014-05-13)
     */
    private String Certificate_StartDate;

    /**
     * 終了 (例: 2015-12-31)
     */
    private String Certificate_ExpiredDate;

    /**
     * 保険者番号 (例: 123456)
     * @return the InsuranceProvider_Number
     */
    public String getInsuranceProvider_Number() {
        return InsuranceProvider_Number;
    }

    /**
     * 保険者番号 (例: 123456)
     * @param InsuranceProvider_Number the InsuranceProvider_Number to set
     */
    public void setInsuranceProvider_Number(String InsuranceProvider_Number) {
        this.InsuranceProvider_Number = InsuranceProvider_Number;
    }

    /**
     * 被保険者番号 (例: 098765)
     * @return the HealthInsuredPerson_Number
     */
    public String getHealthInsuredPerson_Number() {
        return HealthInsuredPerson_Number;
    }

    /**
     * 被保険者番号 (例: 098765)
     * @param HealthInsuredPerson_Number the HealthInsuredPerson_Number to set
     */
    public void setHealthInsuredPerson_Number(String HealthInsuredPerson_Number) {
        this.HealthInsuredPerson_Number = HealthInsuredPerson_Number;
    }

    /**
     * 開始 (例: 2014-05-13)
     * @return the Certificate_StartDate
     */
    public String getCertificate_StartDate() {
        return Certificate_StartDate;
    }

    /**
     * 開始 (例: 2014-05-13)
     * @param Certificate_StartDate the Certificate_StartDate to set
     */
    public void setCertificate_StartDate(String Certificate_StartDate) {
        this.Certificate_StartDate = Certificate_StartDate;
    }

    /**
     * 終了 (例: 2015-12-31)
     * @return the Certificate_ExpiredDate
     */
    public String getCertificate_ExpiredDate() {
        return Certificate_ExpiredDate;
    }

    /**
     * 終了 (例: 2015-12-31)
     * @param Certificate_ExpiredDate the Certificate_ExpiredDate to set
     */
    public void setCertificate_ExpiredDate(String Certificate_ExpiredDate) {
        this.Certificate_ExpiredDate = Certificate_ExpiredDate;
    }
}

