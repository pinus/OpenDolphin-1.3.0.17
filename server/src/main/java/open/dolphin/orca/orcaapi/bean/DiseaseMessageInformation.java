package open.dolphin.orca.orcaapi.bean;

/**
 * Disease_Message_Information. 病名登録結果
 * @author pns
 */
public class DiseaseMessageInformation {
    /**
     * 病名結果コード (例: 02)
     */
    private String Disease_Result;

    /**
     * 病名結果メッセージ (例: 警告がある病名が存在します)
     */
    private String Disease_Result_Message;

    /**
     * 病名警告情報（繰り返し50） (例:  )
     */
    private DiseaseWarningInfo[] Disease_Warning_Info;

    /**
     * 病名結果コード (例: 02)
     * @return the Disease_Result
     */
    public String getDisease_Result() {
        return Disease_Result;
    }

    /**
     * 病名結果コード (例: 02)
     * @param Disease_Result the Disease_Result to set
     */
    public void setDisease_Result(String Disease_Result) {
        this.Disease_Result = Disease_Result;
    }

    /**
     * 病名結果メッセージ (例: 警告がある病名が存在します)
     * @return the Disease_Result_Message
     */
    public String getDisease_Result_Message() {
        return Disease_Result_Message;
    }

    /**
     * 病名結果メッセージ (例: 警告がある病名が存在します)
     * @param Disease_Result_Message the Disease_Result_Message to set
     */
    public void setDisease_Result_Message(String Disease_Result_Message) {
        this.Disease_Result_Message = Disease_Result_Message;
    }

    /**
     * 病名警告情報（繰り返し50） (例:  )
     * @return the Disease_Warning_Info
     */
    public DiseaseWarningInfo[] getDisease_Warning_Info() {
        return Disease_Warning_Info;
    }

    /**
     * 病名警告情報（繰り返し50） (例:  )
     * @param Disease_Warning_Info the Disease_Warning_Info to set
     */
    public void setDisease_Warning_Info(DiseaseWarningInfo[] Disease_Warning_Info) {
        this.Disease_Warning_Info = Disease_Warning_Info;
    }
}