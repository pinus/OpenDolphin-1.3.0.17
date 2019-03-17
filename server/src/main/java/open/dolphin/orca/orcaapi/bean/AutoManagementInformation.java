package open.dolphin.orca.orcaapi.bean;

/**
 * Auto_Management_Information. 管理料等自動算定情報（繰り返し　３）
 *
 * @author pns
 */
public class AutoManagementInformation {
    /**
     * 管理料コード (例:  )
     */
    private String Medication_Code;

    /**
     * 管理料名称 (例:  )
     */
    private String Medication_Name;

    /**
     * 有効終了日 (例:  )
     */
    private String Medication_EndDate;

    /**
     * Medication_Code
     *
     * @return Medication_Code
     */
    public String getMedication_Code() {
        return Medication_Code;
    }

    /**
     * Medication_Code
     *
     * @param Medication_Code to set
     */
    public void setMedication_Code(String Medication_Code) {
        this.Medication_Code = Medication_Code;
    }

    /**
     * Medication_Name
     *
     * @return Medication_Name
     */
    public String getMedication_Name() {
        return Medication_Name;
    }

    /**
     * Medication_Name
     *
     * @param Medication_Name to set
     */
    public void setMedication_Name(String Medication_Name) {
        this.Medication_Name = Medication_Name;
    }

    /**
     * Medication_EndDate
     *
     * @return Medication_EndDate
     */
    public String getMedication_EndDate() {
        return Medication_EndDate;
    }

    /**
     * Medication_EndDate
     *
     * @param Medication_EndDate to set
     */
    public void setMedication_EndDate(String Medication_EndDate) {
        this.Medication_EndDate = Medication_EndDate;
    }
}
