package open.dolphin.orca.orcaapi.bean;

/**
 * Medical_Message_Information. 診療行為登録結果
 * @author pns
 */
public class MedicalMessageInformation {
    /**
     * 診療行為結果コード (例: 03)
     */
    private String Medical_Result;

    /**
     * 診療行為結果メッセージ (例: 既に同日の診療データが登録されています)
     */
    private String Medical_Result_Message;

    /**
     * 診療行為警告情報（繰り返し50） (例:  )
     */
    private MedicalWarningInfo[] Medical_Warning_Info;

    /**
     * エラーメッセージ内容（繰り返し　５０） (例: )
     */
    private MedicalMessageInfo[] Medical_Message_Info;

    /**
     * 診療行為結果コード (例: 03)
     * @return the Medical_Result
     */
    public String getMedical_Result() {
        return Medical_Result;
    }

    /**
     * 診療行為結果コード (例: 03)
     * @param Medical_Result the Medical_Result to set
     */
    public void setMedical_Result(String Medical_Result) {
        this.Medical_Result = Medical_Result;
    }

    /**
     * 診療行為結果メッセージ (例: 既に同日の診療データが登録されています)
     * @return the Medical_Result_Message
     */
    public String getMedical_Result_Message() {
        return Medical_Result_Message;
    }

    /**
     * 診療行為結果メッセージ (例: 既に同日の診療データが登録されています)
     * @param Medical_Result_Message the Medical_Result_Message to set
     */
    public void setMedical_Result_Message(String Medical_Result_Message) {
        this.Medical_Result_Message = Medical_Result_Message;
    }

    /**
     * 診療行為警告情報（繰り返し50） (例:  )
     * @return the Medical_Warning_Info
     */
    public MedicalWarningInfo[] getMedical_Warning_Info() {
        return Medical_Warning_Info;
    }

    /**
     * 診療行為警告情報（繰り返し50） (例:  )
     * @param Medical_Warning_Info the Medical_Warning_Info to set
     */
    public void setMedical_Warning_Info(MedicalWarningInfo[] Medical_Warning_Info) {
        this.Medical_Warning_Info = Medical_Warning_Info;
    }

    /**
     * エラーメッセージ内容（繰り返し　５０） (例: )
     * @return the Medical_Message_Info
     */
    public MedicalMessageInfo[] getMedical_Message_Info() {
        return Medical_Message_Info;
    }

    /**
     * エラーメッセージ内容（繰り返し　５０） (例: )
     * @param Medical_Message_Info the Medical_Message_Info to set
     */
    public void setMedical_Message_Info(MedicalMessageInfo[] Medical_Message_Info) {
        this.Medical_Message_Info = Medical_Message_Info;
    }
}