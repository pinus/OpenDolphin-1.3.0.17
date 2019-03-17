package open.dolphin.orca.orcaapi.bean;

/**
 * insprogetreq.
 *
 * @author pns
 */
public class Insprogetreq {
    /**
     * 保険者番号 (例: 138057)
     */
    private String InsuranceProvider_Number;

    /**
     * 保険番号 (例: 060)
     */
    private String Insurance_Number;

    /**
     * 保険者番号 (例: 138057)
     *
     * @return the InsuranceProvider_Number
     */
    public String getInsuranceProvider_Number() {
        return InsuranceProvider_Number;
    }

    /**
     * 保険者番号 (例: 138057)
     *
     * @param InsuranceProvider_Number the InsuranceProvider_Number to set
     */
    public void setInsuranceProvider_Number(String InsuranceProvider_Number) {
        this.InsuranceProvider_Number = InsuranceProvider_Number;
    }

    /**
     * 保険番号 (例: 060)
     *
     * @return the Insurance_Number
     */
    public String getInsurance_Number() {
        return Insurance_Number;
    }

    /**
     * 保険番号 (例: 060)
     *
     * @param Insurance_Number the Insurance_Number to set
     */
    public void setInsurance_Number(String Insurance_Number) {
        this.Insurance_Number = Insurance_Number;
    }
}