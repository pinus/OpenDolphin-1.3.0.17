package open.dolphin.orca.orcaapi.bean;

/**
 * Disease_Warning_Info. 病名警告情報（繰り返し50）
 *
 * @author pns
 */
public class DiseaseWarningInfo {
    /**
     * 病名警告コード (例: W01)
     */
    private String Disease_Warning;

    /**
     * 病名警告メッセージ (例: 廃止・移行先・推奨のある病名が存在します)
     */
    private String Disease_Warning_Message;

    /**
     * エラー(警告)となった病名情報が何番目の「Disease_Information_child」に記述されているかを表します。 (例: 02)
     */
    private String Disease_Warning_Item_Position;

    /**
     * 警告対象の病名 (例: 左膝ガングリオンの疑い)
     */
    private String Disease_Warning_Name;

    /**
     * 警告対象の病名コード (例: 2049.7274039.8002)
     */
    private String Disease_Warning_Code;

    /**
     * 廃止、移行先、推奨(01:廃止、02:移行先、03:推奨) (例: 01)
     */
    private String Disease_Warning_Change;

    /**
     * 警告対象の開始日 (例: 2017-03-10)
     */
    private String Disease_Warning_StartDate;

    /**
     * 病名警告コード (例: W01)
     *
     * @return the Disease_Warning
     */
    public String getDisease_Warning() {
        return Disease_Warning;
    }

    /**
     * 病名警告コード (例: W01)
     *
     * @param Disease_Warning the Disease_Warning to set
     */
    public void setDisease_Warning(String Disease_Warning) {
        this.Disease_Warning = Disease_Warning;
    }

    /**
     * 病名警告メッセージ (例: 廃止・移行先・推奨のある病名が存在します)
     *
     * @return the Disease_Warning_Message
     */
    public String getDisease_Warning_Message() {
        return Disease_Warning_Message;
    }

    /**
     * 病名警告メッセージ (例: 廃止・移行先・推奨のある病名が存在します)
     *
     * @param Disease_Warning_Message the Disease_Warning_Message to set
     */
    public void setDisease_Warning_Message(String Disease_Warning_Message) {
        this.Disease_Warning_Message = Disease_Warning_Message;
    }

    /**
     * エラー(警告)となった病名情報が何番目の「Disease_Information_child」に記述されているかを表します。 (例: 02)
     *
     * @return the Disease_Warning_Item_Position
     */
    public String getDisease_Warning_Item_Position() {
        return Disease_Warning_Item_Position;
    }

    /**
     * エラー(警告)となった病名情報が何番目の「Disease_Information_child」に記述されているかを表します。 (例: 02)
     *
     * @param Disease_Warning_Item_Position the Disease_Warning_Item_Position to set
     */
    public void setDisease_Warning_Item_Position(String Disease_Warning_Item_Position) {
        this.Disease_Warning_Item_Position = Disease_Warning_Item_Position;
    }

    /**
     * 警告対象の病名 (例: 左膝ガングリオンの疑い)
     *
     * @return the Disease_Warning_Name
     */
    public String getDisease_Warning_Name() {
        return Disease_Warning_Name;
    }

    /**
     * 警告対象の病名 (例: 左膝ガングリオンの疑い)
     *
     * @param Disease_Warning_Name the Disease_Warning_Name to set
     */
    public void setDisease_Warning_Name(String Disease_Warning_Name) {
        this.Disease_Warning_Name = Disease_Warning_Name;
    }

    /**
     * 警告対象の病名コード (例: 2049.7274039.8002)
     *
     * @return the Disease_Warning_Code
     */
    public String getDisease_Warning_Code() {
        return Disease_Warning_Code;
    }

    /**
     * 警告対象の病名コード (例: 2049.7274039.8002)
     *
     * @param Disease_Warning_Code the Disease_Warning_Code to set
     */
    public void setDisease_Warning_Code(String Disease_Warning_Code) {
        this.Disease_Warning_Code = Disease_Warning_Code;
    }

    /**
     * 廃止、移行先、推奨(01:廃止、02:移行先、03:推奨) (例: 01)
     *
     * @return the Disease_Warning_Change
     */
    public String getDisease_Warning_Change() {
        return Disease_Warning_Change;
    }

    /**
     * 廃止、移行先、推奨(01:廃止、02:移行先、03:推奨) (例: 01)
     *
     * @param Disease_Warning_Change the Disease_Warning_Change to set
     */
    public void setDisease_Warning_Change(String Disease_Warning_Change) {
        this.Disease_Warning_Change = Disease_Warning_Change;
    }

    /**
     * 警告対象の開始日 (例: 2017-03-10)
     *
     * @return the Disease_Warning_StartDate
     */
    public String getDisease_Warning_StartDate() {
        return Disease_Warning_StartDate;
    }

    /**
     * 警告対象の開始日 (例: 2017-03-10)
     *
     * @param Disease_Warning_StartDate the Disease_Warning_StartDate to set
     */
    public void setDisease_Warning_StartDate(String Disease_Warning_StartDate) {
        this.Disease_Warning_StartDate = Disease_Warning_StartDate;
    }
}