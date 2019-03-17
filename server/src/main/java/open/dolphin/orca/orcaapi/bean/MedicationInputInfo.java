package open.dolphin.orca.orcaapi.bean;

/**
 * Medication_Input_Info. コメント埋め込み数値（繰り返し　５）
 *
 * @author pns
 */
public class MedicationInputInfo {
    /**
     * コメント埋め込み数値 (例: )
     */
    private String Medication_Input_Code;

    /**
     * コメント埋め込み数値 (例: )
     *
     * @return the Medication_Input_Code
     */
    public String getMedication_Input_Code() {
        return Medication_Input_Code;
    }

    /**
     * コメント埋め込み数値 (例: )
     *
     * @param Medication_Input_Code the Medication_Input_Code to set
     */
    public void setMedication_Input_Code(String Medication_Input_Code) {
        this.Medication_Input_Code = Medication_Input_Code;
    }
}
