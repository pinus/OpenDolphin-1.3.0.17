package open.dolphin.orca.orcaapi.bean;

/**
 * Medical_Warning_Info. 診療行為警告情報（繰り返し50）
 * @author pns
 */
public class MedicalWarningInfo {
    /**
     * 診療行為警告コード (例: M01)
     */
    private String Medical_Warning;

    /**
     * 診療行為警告メッセージ (例: 点数マスタに登録がありません)
     */
    private String Medical_Warning_Message;

    /**
     * エラーとなった診療行為情報が何番目の「Medical_Information_child」に記述されているかを表します。※２ (例: 01)
     */
    private String Medical_Warning_Position;

    /**
     * エラーとなった診療内容が何番目の「Medication_info_child」に記述されているかを表します。※２ (例: 01)
     */
    private String Medical_Warning_Item_Position;

    /**
     * 警告対象の診療行為コード (例: 012007410)
     */
    private String Medical_Warning_Code;

    /**
     * ワーニングメッセージ１ (例: )
     */
    private String Medical_Warning_Message1;

    /**
     * ワーニングメッセージ２ (例: )
     */
    private String Medical_Warning_Message2;

    /**
     * 診療行為警告コード (例: M01)
     * @return the Medical_Warning
     */
    public String getMedical_Warning() {
        return Medical_Warning;
    }

    /**
     * 診療行為警告コード (例: M01)
     * @param Medical_Warning the Medical_Warning to set
     */
    public void setMedical_Warning(String Medical_Warning) {
        this.Medical_Warning = Medical_Warning;
    }

    /**
     * 診療行為警告メッセージ (例: 点数マスタに登録がありません)
     * @return the Medical_Warning_Message
     */
    public String getMedical_Warning_Message() {
        return Medical_Warning_Message;
    }

    /**
     * 診療行為警告メッセージ (例: 点数マスタに登録がありません)
     * @param Medical_Warning_Message the Medical_Warning_Message to set
     */
    public void setMedical_Warning_Message(String Medical_Warning_Message) {
        this.Medical_Warning_Message = Medical_Warning_Message;
    }

    /**
     * エラーとなった診療行為情報が何番目の「Medical_Information_child」に記述されているかを表します。※２ (例: 01)
     * @return the Medical_Warning_Position
     */
    public String getMedical_Warning_Position() {
        return Medical_Warning_Position;
    }

    /**
     * エラーとなった診療行為情報が何番目の「Medical_Information_child」に記述されているかを表します。※２ (例: 01)
     * @param Medical_Warning_Position the Medical_Warning_Position to set
     */
    public void setMedical_Warning_Position(String Medical_Warning_Position) {
        this.Medical_Warning_Position = Medical_Warning_Position;
    }

    /**
     * エラーとなった診療内容が何番目の「Medication_info_child」に記述されているかを表します。※２ (例: 01)
     * @return the Medical_Warning_Item_Position
     */
    public String getMedical_Warning_Item_Position() {
        return Medical_Warning_Item_Position;
    }

    /**
     * エラーとなった診療内容が何番目の「Medication_info_child」に記述されているかを表します。※２ (例: 01)
     * @param Medical_Warning_Item_Position the Medical_Warning_Item_Position to set
     */
    public void setMedical_Warning_Item_Position(String Medical_Warning_Item_Position) {
        this.Medical_Warning_Item_Position = Medical_Warning_Item_Position;
    }

    /**
     * 警告対象の診療行為コード (例: 012007410)
     * @return the Medical_Warning_Code
     */
    public String getMedical_Warning_Code() {
        return Medical_Warning_Code;
    }

    /**
     * 警告対象の診療行為コード (例: 012007410)
     * @param Medical_Warning_Code the Medical_Warning_Code to set
     */
    public void setMedical_Warning_Code(String Medical_Warning_Code) {
        this.Medical_Warning_Code = Medical_Warning_Code;
    }

    /**
     * ワーニングメッセージ１ (例: )
     * @return the Medical_Warning_Message1
     */
    public String getMedical_Warning_Message1() {
        return Medical_Warning_Message1;
    }

    /**
     * ワーニングメッセージ１ (例: )
     * @param Medical_Warning_Message1 the Medical_Warning_Message1 to set
     */
    public void setMedical_Warning_Message1(String Medical_Warning_Message1) {
        this.Medical_Warning_Message1 = Medical_Warning_Message1;
    }

    /**
     * ワーニングメッセージ２ (例: )
     * @return the Medical_Warning_Message2
     */
    public String getMedical_Warning_Message2() {
        return Medical_Warning_Message2;
    }

    /**
     * ワーニングメッセージ２ (例: )
     * @param Medical_Warning_Message2 the Medical_Warning_Message2 to set
     */
    public void setMedical_Warning_Message2(String Medical_Warning_Message2) {
        this.Medical_Warning_Message2 = Medical_Warning_Message2;
    }
}