package open.dolphin.orca.orcaapi.bean;

/**
 * Medical_Info. 診療行為剤内容（繰り返し　４０）
 *
 * @author pns
 */
public class MedicalInfo {
    /**
     * 診療種別区分 (例: 960)
     */
    private String Medical_Class;

    /**
     * 診療種別区分名称 (例: 保険外（消費税あり）)
     */
    private String Medical_Class_Name;

    /**
     * 回数 (例: 1)
     */
    private String Medical_Class_Number;

    /**
     * 診療剤明細（繰り返し　４０） (例: )
     */
    private MedicationInfo[] Medication_Info;

    /**
     * 禁忌薬剤コード (例: )
     */
    private String Contra_Code;

    /**
     * 禁忌薬剤名称 (例: )
     */
    private String Contra_Name;

    /**
     * 症状詳記区分※1 (例: )
     */
    private String Interact_Code;

    /**
     * 投与日最終投与日※2 (例: )
     */
    private String Administer_Date;

    /**
     * 今回投与区分1:今回投与分※2 (例: )
     */
    private String Administer_Class;

    /**
     * 方向性※3 (例: )
     */
    private String Context_Class;

    /**
     * 診療種別区分 (例: 960)
     *
     * @return the Medical_Class
     */
    public String getMedical_Class() {
        return Medical_Class;
    }

    /**
     * 診療種別区分 (例: 960)
     *
     * @param Medical_Class the Medical_Class to set
     */
    public void setMedical_Class(String Medical_Class) {
        this.Medical_Class = Medical_Class;
    }

    /**
     * 診療種別区分名称 (例: 保険外（消費税あり）)
     *
     * @return the Medical_Class_Name
     */
    public String getMedical_Class_Name() {
        return Medical_Class_Name;
    }

    /**
     * 診療種別区分名称 (例: 保険外（消費税あり）)
     *
     * @param Medical_Class_Name the Medical_Class_Name to set
     */
    public void setMedical_Class_Name(String Medical_Class_Name) {
        this.Medical_Class_Name = Medical_Class_Name;
    }

    /**
     * 回数 (例: 1)
     *
     * @return the Medical_Class_Number
     */
    public String getMedical_Class_Number() {
        return Medical_Class_Number;
    }

    /**
     * 回数 (例: 1)
     *
     * @param Medical_Class_Number the Medical_Class_Number to set
     */
    public void setMedical_Class_Number(String Medical_Class_Number) {
        this.Medical_Class_Number = Medical_Class_Number;
    }

    /**
     * 診療剤明細（繰り返し　４０） (例: )
     *
     * @return the Medication_Info
     */
    public MedicationInfo[] getMedication_Info() {
        return Medication_Info;
    }

    /**
     * 診療剤明細（繰り返し　４０） (例: )
     *
     * @param Medication_Info the Medication_Info to set
     */
    public void setMedication_Info(MedicationInfo[] Medication_Info) {
        this.Medication_Info = Medication_Info;
    }

    /**
     * Contra_Code
     *
     * @return Contra_Code
     */
    public String getContra_Code() {
        return Contra_Code;
    }

    /**
     * Contra_Code
     *
     * @param Contra_Code to set
     */
    public void setContra_Code(String Contra_Code) {
        this.Contra_Code = Contra_Code;
    }

    /**
     * Contra_Name
     *
     * @return Contra_Name
     */
    public String getContra_Name() {
        return Contra_Name;
    }

    /**
     * Contra_Name
     *
     * @param Contra_Name to set
     */
    public void setContra_Name(String Contra_Name) {
        this.Contra_Name = Contra_Name;
    }

    /**
     * Interact_Code
     *
     * @return Interact_Code
     */
    public String getInteract_Code() {
        return Interact_Code;
    }

    /**
     * Interact_Code
     *
     * @param Interact_Code to set
     */
    public void setInteract_Code(String Interact_Code) {
        this.Interact_Code = Interact_Code;
    }

    /**
     * Administer_Date
     *
     * @return Administer_Date
     */
    public String getAdminister_Date() {
        return Administer_Date;
    }

    /**
     * Administer_Date
     *
     * @param Administer_Date to set
     */
    public void setAdminister_Date(String Administer_Date) {
        this.Administer_Date = Administer_Date;
    }

    /**
     * Administer_Class
     *
     * @return Administer_Class
     */
    public String getAdminister_Class() {
        return Administer_Class;
    }

    /**
     * Administer_Class
     *
     * @param Administer_Class to set
     */
    public void setAdminister_Class(String Administer_Class) {
        this.Administer_Class = Administer_Class;
    }

    /**
     * Context_Class
     *
     * @return Context_Class
     */
    public String getContext_Class() {
        return Context_Class;
    }

    /**
     * Context_Class
     *
     * @param Context_Class to set
     */
    public void setContext_Class(String Context_Class) {
        this.Context_Class = Context_Class;
    }
}