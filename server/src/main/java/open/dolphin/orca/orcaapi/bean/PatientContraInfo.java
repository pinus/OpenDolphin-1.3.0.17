package open.dolphin.orca.orcaapi.bean;

/**
 * Patient_Contra_Info. 患者禁忌薬剤情報（繰り返し　１００）
 * @author pns
 */
public class PatientContraInfo {
    /**
     * 薬剤コード (例:  )
     */
    private String Medication_Code;

    /**
     * 薬剤名称 (例:  )
     */
    private String Medication_Name;

    /**
     * 有効終了日 (例:  )
     */
    private String Medication_EndDate;

    /**
     * 禁忌開始日 (例:  )
     */
    private String Contra_StartDate;

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

    /**
     * Contra_StartDate
     *
     * @return Contra_StartDate
     */
    public String getContra_StartDate() {
        return Contra_StartDate;
    }

    /**
     * Contra_StartDate
     *
     * @param Contra_StartDate to set
     */
    public void setContra_StartDate(String Contra_StartDate) {
        this.Contra_StartDate = Contra_StartDate;
    }
}
