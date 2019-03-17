package open.dolphin.orca.orcaapi.bean;

/**
 * Medical_Message_Info. エラーメッセージ内容（繰り返し　５０）
 *
 * @author pns
 */
public class MedicalMessageInfo {
    /**
     * エラーコード (例: 0001)
     */
    private String Medical_Result;

    /**
     * エラーメッセージ１ (例: 該当する点数マスターが存在しません)
     */
    private String Medical_Result_Message1;

    /**
     * エラーメッセージ２ (例: )
     */
    private String Medical_Result_Message2;

    /**
     * エラー剤位置 (例: 6)
     */
    private String Medical_Position;

    /**
     * エラー行位置 (例: 1)
     */
    private String Medical_Item_Position;

    /**
     * エラー診療コード (例: 096000002)
     */
    private String Medical_Result_Code;

    /**
     * エラーコード (例: 0001)
     *
     * @return the Medical_Result
     */
    public String getMedical_Result() {
        return Medical_Result;
    }

    /**
     * エラーコード (例: 0001)
     *
     * @param Medical_Result the Medical_Result to set
     */
    public void setMedical_Result(String Medical_Result) {
        this.Medical_Result = Medical_Result;
    }

    /**
     * エラーメッセージ１ (例: 該当する点数マスターが存在しません)
     *
     * @return the Medical_Result_Message1
     */
    public String getMedical_Result_Message1() {
        return Medical_Result_Message1;
    }

    /**
     * エラーメッセージ１ (例: 該当する点数マスターが存在しません)
     *
     * @param Medical_Result_Message1 the Medical_Result_Message1 to set
     */
    public void setMedical_Result_Message1(String Medical_Result_Message1) {
        this.Medical_Result_Message1 = Medical_Result_Message1;
    }

    /**
     * エラーメッセージ２ (例: )
     *
     * @return the Medical_Result_Message2
     */
    public String getMedical_Result_Message2() {
        return Medical_Result_Message2;
    }

    /**
     * エラーメッセージ２ (例: )
     *
     * @param Medical_Result_Message2 the Medical_Result_Message2 to set
     */
    public void setMedical_Result_Message2(String Medical_Result_Message2) {
        this.Medical_Result_Message2 = Medical_Result_Message2;
    }

    /**
     * エラー剤位置 (例: 6)
     *
     * @return the Medical_Position
     */
    public String getMedical_Position() {
        return Medical_Position;
    }

    /**
     * エラー剤位置 (例: 6)
     *
     * @param Medical_Position the Medical_Position to set
     */
    public void setMedical_Position(String Medical_Position) {
        this.Medical_Position = Medical_Position;
    }

    /**
     * エラー行位置 (例: 1)
     *
     * @return the Medical_Item_Position
     */
    public String getMedical_Item_Position() {
        return Medical_Item_Position;
    }

    /**
     * エラー行位置 (例: 1)
     *
     * @param Medical_Item_Position the Medical_Item_Position to set
     */
    public void setMedical_Item_Position(String Medical_Item_Position) {
        this.Medical_Item_Position = Medical_Item_Position;
    }

    /**
     * エラー診療コード (例: 096000002)
     *
     * @return the Medical_Result_Code
     */
    public String getMedical_Result_Code() {
        return Medical_Result_Code;
    }

    /**
     * エラー診療コード (例: 096000002)
     *
     * @param Medical_Result_Code the Medical_Result_Code to set
     */
    public void setMedical_Result_Code(String Medical_Result_Code) {
        this.Medical_Result_Code = Medical_Result_Code;
    }
}