package open.dolphin.orca.orcaapi.bean;

/**
 * Insurance_Information.
 * @author pns
 */
public class InsuranceInformation {
    /**
     * 基準日送信内容 (例: )
     */
    private String Base_Date;

    /**
     * 保険情報※1保険番号マスタの内容（繰り返し99） (例: )
     */
    private HealthInsuranceInformation[] HealthInsurance_Information;

    /**
     * 公費情報※1保険番号マスタの内容（繰り返し99） (例: )
     */
    private PublicinsuranceInformation[] PublicInsurance_Information;

    /**
     * Base_Date
     *
     * @return Base_Date
     */
    public String getBase_Date() {
        return Base_Date;
    }

    /**
     * Base_Date
     *
     * @param Base_Date to set
     */
    public void setBase_Date(String Base_Date) {
        this.Base_Date = Base_Date;
    }

    /**
     * HealthInsurance_Information
     *
     * @return HealthInsurance_Information
     */
    public HealthInsuranceInformation[] getHealthInsurance_Information() {
        return HealthInsurance_Information;
    }

    /**
     * HealthInsurance_Information
     *
     * @param HealthInsurance_Information to set
     */
    public void setHealthInsurance_Information(HealthInsuranceInformation[] HealthInsurance_Information) {
        this.HealthInsurance_Information = HealthInsurance_Information;
    }

    /**
     * PublicInsurance_Information
     *
     * @return PublicInsurance_Information
     */
    public PublicinsuranceInformation[] getPublicInsurance_Information() {
        return PublicInsurance_Information;
    }

    /**
     * PublicInsurance_Information
     *
     * @param PublicInsurance_Information to set
     */
    public void setPublicInsurance_Information(PublicinsuranceInformation[] PublicInsurance_Information) {
        this.PublicInsurance_Information = PublicInsurance_Information;
    }
}
