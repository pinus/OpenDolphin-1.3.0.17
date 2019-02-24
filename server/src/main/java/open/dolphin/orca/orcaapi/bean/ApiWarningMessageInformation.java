package open.dolphin.orca.orcaapi.bean;

/**
 * Api_Warning_Message_Information. 警告メッセージ情報（繰り返し　５）
 * @author pns
 */
public class ApiWarningMessageInformation {
    /**
     * 警告メッセージ (例: 受付日を自動設定しました)
     */
    private String Api_Warning_Message;

    /**
     * 警告メッセージ (例: 受付日を自動設定しました)
     * @return the Api_Warning_Message
     */
    public String getApi_Warning_Message() {
        return Api_Warning_Message;
    }

    /**
     * 警告メッセージ (例: 受付日を自動設定しました)
     * @param Api_Warning_Message the Api_Warning_Message to set
     */
    public void setApi_Warning_Message(String Api_Warning_Message) {
        this.Api_Warning_Message = Api_Warning_Message;
    }
}
