package open.dolphin.orca.orcaapi.bean;

/**
 * Medical_Information. チェック薬剤情報（繰り返し30）.
 * for contraindicationcheckres.
 * @author pns
 */
public class MedicalInformation6 {
    /**
     * 薬剤コード送信された内容をそのまま返却 (例: )
     */
    private String Medication_Code;

    /**
     * 薬剤名称 (例: )
     */
    private String Medication_Name;

    /**
     * エラーコード点数マスタ登録エラー、薬剤以外はエラー (例: )
     */
    private String Medical_Result;

    /**
     * エラーメッセージ (例: )
     */
    private String Medical_Result_Message;

    /**
     * 併用禁忌情報（繰り返し30） (例: )
     */
    private MedicalInfo[] Medical_Info;

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
     * Medical_Result
     *
     * @return Medical_Result
     */
    public String getMedical_Result() {
        return Medical_Result;
    }

    /**
     * Medical_Result
     *
     * @param Medical_Result to set
     */
    public void setMedical_Result(String Medical_Result) {
        this.Medical_Result = Medical_Result;
    }

    /**
     * Medical_Result_Message
     *
     * @return Medical_Result_Message
     */
    public String getMedical_Result_Message() {
        return Medical_Result_Message;
    }

    /**
     * Medical_Result_Message
     *
     * @param Medical_Result_Message to set
     */
    public void setMedical_Result_Message(String Medical_Result_Message) {
        this.Medical_Result_Message = Medical_Result_Message;
    }

    /**
     * Medical_Info
     *
     * @return Medical_Info
     */
    public MedicalInfo[] getMedical_Info() {
        return Medical_Info;
    }

    /**
     * Medical_Info
     *
     * @param Medical_Info to set
     */
    public void setMedical_Info(MedicalInfo[] Medical_Info) {
        this.Medical_Info = Medical_Info;
    }
}
