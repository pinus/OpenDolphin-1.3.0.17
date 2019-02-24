package open.dolphin.orca.orcaapi.bean;

/**
 * contraindication_checkreq.
 * @author pns
 */
public class ContraindicationCheckreq {
    /**
     * リクエスト番号01(固定) (例: )
     */
    private String Request_Number;

    /**
     * 患者番号 (例: )
     */
    private String Patient_ID;

    /**
     * 診療年月未設定はシステム日付 (例: )
     */
    private String Perform_Month;

    /**
     * チェック期間未設定はシステム管理の相互作用チェック期間 (例: )
     */
    private String Check_Term;

    /**
     * チェック薬剤情報（繰り返し30） (例: )
     */
    private MedicalInformation6[] Medical_Information;

    /**
     * Request_Number
     *
     * @return Request_Number
     */
    public String getRequest_Number() {
        return Request_Number;
    }

    /**
     * Request_Number
     *
     * @param Request_Number to set
     */
    public void setRequest_Number(String Request_Number) {
        this.Request_Number = Request_Number;
    }

    /**
     * Patient_ID
     *
     * @return Patient_ID
     */
    public String getPatient_ID() {
        return Patient_ID;
    }

    /**
     * Patient_ID
     *
     * @param Patient_ID to set
     */
    public void setPatient_ID(String Patient_ID) {
        this.Patient_ID = Patient_ID;
    }

    /**
     * Perform_Month
     *
     * @return Perform_Month
     */
    public String getPerform_Month() {
        return Perform_Month;
    }

    /**
     * Perform_Month
     *
     * @param Perform_Month to set
     */
    public void setPerform_Month(String Perform_Month) {
        this.Perform_Month = Perform_Month;
    }

    /**
     * Check_Term
     *
     * @return Check_Term
     */
    public String getCheck_Term() {
        return Check_Term;
    }

    /**
     * Check_Term
     *
     * @param Check_Term to set
     */
    public void setCheck_Term(String Check_Term) {
        this.Check_Term = Check_Term;
    }

    /**
     * Medical_Information
     *
     * @return Medical_Information
     */
    public MedicalInformation6[] getMedical_Information() {
        return Medical_Information;
    }

    /**
     * Medical_Information
     *
     * @param Medical_Information to set
     */
    public void setMedical_Information(MedicalInformation6[] Medical_Information) {
        this.Medical_Information = Medical_Information;
    }
}
