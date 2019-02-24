package open.dolphin.orca.pushapi.bean;

/**
 * Medical_Information. (繰り返し　１５)
 * @author pns
 */
public class MedicalInformation {
    /**
     * 保険組合せ番号 (例: 0006)
     */
    private String Insurance_Combination_Number;

    /**
     * 診療科 (例: 01)
     */
    private String Department_Code;

    /**
     * ドクタコード (例: 10001)
     */
    private String Physician_Code;

    /**
     * 伝票番号 (例: 0000895)
     */
    private String Invoice_Number;

    /**
     * 保険組合せ番号 (例: 0006)
     * @return the Insurance_Combination_Number
     */
    public String getInsurance_Combination_Number() {
        return Insurance_Combination_Number;
    }

    /**
     * 保険組合せ番号 (例: 0006)
     * @param Insurance_Combination_Number the Insurance_Combination_Number to set
     */
    public void setInsurance_Combination_Number(String Insurance_Combination_Number) {
        this.Insurance_Combination_Number = Insurance_Combination_Number;
    }

    /**
     * 診療科 (例: 01)
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 診療科 (例: 01)
     * @param Department_Code the Department_Code to set
     */
    public void setDepartment_Code(String Department_Code) {
        this.Department_Code = Department_Code;
    }

    /**
     * ドクタコード (例: 10001)
     * @return the Physician_Code
     */
    public String getPhysician_Code() {
        return Physician_Code;
    }

    /**
     * ドクタコード (例: 10001)
     * @param Physician_Code the Physician_Code to set
     */
    public void setPhysician_Code(String Physician_Code) {
        this.Physician_Code = Physician_Code;
    }

    /**
     * 伝票番号 (例: 0000895)
     * @return the Invoice_Number
     */
    public String getInvoice_Number() {
        return Invoice_Number;
    }

    /**
     * 伝票番号 (例: 0000895)
     * @param Invoice_Number the Invoice_Number to set
     */
    public void setInvoice_Number(String Invoice_Number) {
        this.Invoice_Number = Invoice_Number;
    }
}
