package open.dolphin.orca.orcaapi.bean;

/**
 * Certification. 介護認定情報（繰り返し　５０）
 *
 * @author pns
 */
public class Certification {
    /**
     * 要介護状態コード (例: 11)
     */
    private String Need_Care_State_Code;

    /**
     * 要介護状態 (例: 要支援)
     */
    private String Need_Care_State;

    /**
     * 認定日 (例: 2014-05-13)
     */
    private String Certification_Date;

    /**
     * 開始 (例: 2014-05-13)
     */
    private String Certificate_StartDate;

    /**
     * 終了 (例: 2015-05-12)
     */
    private String Certificate_ExpiredDate;

    /**
     * 要介護状態コード (例: 11)
     *
     * @return the Need_Care_State_Code
     */
    public String getNeed_Care_State_Code() {
        return Need_Care_State_Code;
    }

    /**
     * 要介護状態コード (例: 11)
     *
     * @param Need_Care_State_Code the Need_Care_State_Code to set
     */
    public void setNeed_Care_State_Code(String Need_Care_State_Code) {
        this.Need_Care_State_Code = Need_Care_State_Code;
    }

    /**
     * 要介護状態 (例: 要支援)
     *
     * @return the Need_Care_State
     */
    public String getNeed_Care_State() {
        return Need_Care_State;
    }

    /**
     * 要介護状態 (例: 要支援)
     *
     * @param Need_Care_State the Need_Care_State to set
     */
    public void setNeed_Care_State(String Need_Care_State) {
        this.Need_Care_State = Need_Care_State;
    }

    /**
     * 認定日 (例: 2014-05-13)
     *
     * @return the Certification_Date
     */
    public String getCertification_Date() {
        return Certification_Date;
    }

    /**
     * 認定日 (例: 2014-05-13)
     *
     * @param Certification_Date the Certification_Date to set
     */
    public void setCertification_Date(String Certification_Date) {
        this.Certification_Date = Certification_Date;
    }

    /**
     * 開始 (例: 2014-05-13)
     *
     * @return the Certificate_StartDate
     */
    public String getCertificate_StartDate() {
        return Certificate_StartDate;
    }

    /**
     * 開始 (例: 2014-05-13)
     *
     * @param Certificate_StartDate the Certificate_StartDate to set
     */
    public void setCertificate_StartDate(String Certificate_StartDate) {
        this.Certificate_StartDate = Certificate_StartDate;
    }

    /**
     * 終了 (例: 2015-05-12)
     *
     * @return the Certificate_ExpiredDate
     */
    public String getCertificate_ExpiredDate() {
        return Certificate_ExpiredDate;
    }

    /**
     * 終了 (例: 2015-05-12)
     *
     * @param Certificate_ExpiredDate the Certificate_ExpiredDate to set
     */
    public void setCertificate_ExpiredDate(String Certificate_ExpiredDate) {
        this.Certificate_ExpiredDate = Certificate_ExpiredDate;
    }
}
