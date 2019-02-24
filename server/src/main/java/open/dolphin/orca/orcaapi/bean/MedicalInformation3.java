package open.dolphin.orca.orcaapi.bean;

/**
 * Medical_Information. 診療情報.
 * for medicalgetres.
 * @author pns
 */
public class MedicalInformation3 {
    /**
     * 診療科コード　※３(01:内科) (例: 01)
     */
    private String Department_Code;

    /**
     * 連番 (例: 2)
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
     * 含む移行(レセ電データから移行した情報を含むか否か)(False:含まない) (例:  )
     */
    private String Contain_Migration;

    /**
     * 診療科コード　※３(01:内科) (例: 01)
     * @return the Department_Code
     */
    public String getDepartment_Code() {
        return Department_Code;
    }

    /**
     * 診療科コード　※３(01:内科) (例: 01)
     * @param Department_Code the Department_Code to set
     */
    public void setDepartment_Code(String Department_Code) {
        this.Department_Code = Department_Code;
    }

    /**
     * 連番 (例: 2)
     * @return the Sequential_Number
     */
    public String getSequential_Number() {
        return Sequential_Number;
    }

    /**
     * 連番 (例: 2)
     * @param Sequential_Number the Sequential_Number to set
     */
    public void setSequential_Number(String Sequential_Number) {
        this.Sequential_Number = Sequential_Number;
    }

    /**
     * 保険組合せ番号 (例: 0002)
     * @return the Insurance_Combination_Number
     */
    public String getInsurance_Combination_Number() {
        return Insurance_Combination_Number;
    }

    /**
     * 保険組合せ番号 (例: 0002)
     * @param Insurance_Combination_Number the Insurance_Combination_Number to set
     */
    public void setInsurance_Combination_Number(String Insurance_Combination_Number) {
        this.Insurance_Combination_Number = Insurance_Combination_Number;
    }

    /**
     * 保険組合せ情報 (例:  )
     * @return the HealthInsurance_Information
     */
    public HealthInsuranceInformation getHealthInsurance_Information() {
        return HealthInsurance_Information;
    }

    /**
     * 保険組合せ情報 (例:  )
     * @param HealthInsurance_Information the HealthInsurance_Information to set
     */
    public void setHealthInsurance_Information(HealthInsuranceInformation HealthInsurance_Information) {
        this.HealthInsurance_Information = HealthInsurance_Information;
    }

    /**
     * 伝票番号 (例:  )
     * @return the Invoice_Number
     */
    public String getInvoice_Number() {
        return Invoice_Number;
    }

    /**
     * 伝票番号 (例:  )
     * @param Invoice_Number the Invoice_Number to set
     */
    public void setInvoice_Number(String Invoice_Number) {
        this.Invoice_Number = Invoice_Number;
    }

    /**
     * Contain_Migration
     *
     * @return Contain_Migration
     */
    public String getContain_Migration() {
        return Contain_Migration;
    }

    /**
     * Contain_Migration
     *
     * @param Contain_Migration to set
     */
    public void setContain_Migration(String Contain_Migration) {
        this.Contain_Migration = Contain_Migration;
    }
}