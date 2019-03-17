package open.dolphin.orca.orcaapi.bean;

/**
 * Medical_List_Information. 受診履歴情報（繰り返し　１５０）
 *
 * @author pns
 */
public class MedicalListInformation {
    /**
     * 診療年月日 (例: 2014-01-06)
     */
    private String Perform_Date;

    /**
     * 診療科コード（01：内科） (例: 01)
     */
    private String Department_Code;

    /**
     * 診療科名称 (例: 内科)
     */
    private String Department_Name;

    /**
     * 連番（診療科毎の同日連番） (例: 1)
     */
    private String Sequential_Number;

    /**
     * 保険組合せ番号 (例: 0002)
     */
    private String Insurance_Combination_Number;

    /**
     * 保険組合せ情報 (例:  )
     */
    private HealthInsuranceInformation HealthInsurance_Information;

    /**
     * 伝票番号 (例:  )
     */
    private String Invoice_Number;

    /**
     * 診療内容剤情報（繰り返し　１３５） (例:  )
     */
    private MedicalInformation[] Medical_Information;

    /**
     * 診療年月日 (例: 2014-01-06)
     *
     * @return the Perform_Date
     */
    public String getPerform_Date() {
        return Perform_Date;
    }

    /**
     * 診療年月日 (例: 2014-01-06)
     *
     * @param Perform_Date the Perform_Date to set
     */
    public void setPerform_Date(String Perform_Date) {
        this.Perform_Date = Perform_Date;
    }

    /**
     * 診療科コード（01：内科） (例: 01)
     *
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 診療科コード（01：内科） (例: 01)
     *
     * @param Department_Code the Department_Code to set
     */
    public void setDepartment_Code(String Department_Code) {
        this.Department_Code = Department_Code;
    }

    /**
     * 診療科名称 (例: 内科)
     *
     * @return the Department_Name
     */
    public String getDepartment_Name() {
        return Department_Name;
    }

    /**
     * 診療科名称 (例: 内科)
     *
     * @param Department_Name the Department_Name to set
     */
    public void setDepartment_Name(String Department_Name) {
        this.Department_Name = Department_Name;
    }

    /**
     * 連番（診療科毎の同日連番） (例: 1)
     *
     * @return the Sequential_Number
     */
    public String getSequential_Number() {
        return Sequential_Number;
    }

    /**
     * 連番（診療科毎の同日連番） (例: 1)
     *
     * @param Sequential_Number the Sequential_Number to set
     */
    public void setSequential_Number(String Sequential_Number) {
        this.Sequential_Number = Sequential_Number;
    }

    /**
     * 保険組合せ番号 (例: 0002)
     *
     * @return the Insurance_Combination_Number
     */
    public String getInsurance_Combination_Number() {
        return Insurance_Combination_Number;
    }

    /**
     * 保険組合せ番号 (例: 0002)
     *
     * @param Insurance_Combination_Number the Insurance_Combination_Number to set
     */
    public void setInsurance_Combination_Number(String Insurance_Combination_Number) {
        this.Insurance_Combination_Number = Insurance_Combination_Number;
    }

    /**
     * 保険組合せ情報 (例:  )
     *
     * @return the HealthInsurance_Information
     */
    public HealthInsuranceInformation getHealthInsurance_Information() {
        return HealthInsurance_Information;
    }

    /**
     * 保険組合せ情報 (例:  )
     *
     * @param HealthInsurance_Information the HealthInsurance_Information to set
     */
    public void setHealthInsurance_Information(HealthInsuranceInformation HealthInsurance_Information) {
        this.HealthInsurance_Information = HealthInsurance_Information;
    }

    /**
     * 伝票番号 (例:  )
     *
     * @return the Invoice_Number
     */
    public String getInvoice_Number() {
        return Invoice_Number;
    }

    /**
     * 伝票番号 (例:  )
     *
     * @param Invoice_Number the Invoice_Number to set
     */
    public void setInvoice_Number(String Invoice_Number) {
        this.Invoice_Number = Invoice_Number;
    }

    /**
     * 診療内容剤情報（繰り返し　１３５） (例:  )
     *
     * @return the Medical_Information
     */
    public MedicalInformation[] getMedical_Information() {
        return Medical_Information;
    }

    /**
     * 診療内容剤情報（繰り返し　１３５） (例:  )
     *
     * @param Medical_Information the Medical_Information to set
     */
    public void setMedical_Information(MedicalInformation[] Medical_Information) {
        this.Medical_Information = Medical_Information;
    }
}