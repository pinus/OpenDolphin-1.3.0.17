package open.dolphin.orca.pushapi.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PushAPI から返ってくる Response.
 * @author pns
 */
public class Response extends Command {

    /**
     * Response として送られてくるデータ.
     */
    private Data data;

    /**
     * エラーとなったコマンド.
     */
    @JsonProperty("for")
    private String cause;

    /**
     * エラーコード.
     * <table>
     * <tr><td>INTERNAL_ERROR</td><td>サーバ側のエラー</td></tr>
     * <tr><td>NO_SUCH_SUBSCRIPTION</td><td>購読停止時に指定したsub.idが存在しない</td></tr>
     * <tr><td>INVALID_PARAMS</td><td>パラメータが不正</td></tr>
     * <tr><td>PARSE_ERROR</td><td>受け取ったJSONのパースエラー</td></tr>
     * </table>
     */
    private String code;

    /**
     * エラーの理由.
     */
    private String reason;

    /**
     * エラーとなったコマンド.
     * @return the cause
     */
    public String getCause() {
        return cause;
    }

    /**
     * エラーコード.
     * <table>
     * <tr><td>INTERNAL_ERROR</td><td>サーバ側のエラー</td></tr>
     * <tr><td>NO_SUCH_SUBSCRIPTION</td><td>購読停止時に指定したsub.idが存在しない</td></tr>
     * <tr><td>INVALID_PARAMS</td><td>パラメータが不正</td></tr>
     * <tr><td>PARSE_ERROR</td><td>受け取ったJSONのパースエラー</td></tr>
     * </table>
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * エラーの理由.
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * エラーの理由.
     * @param reason エラーの理由
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * データ.
     * @return the data
     */
    public Data getData() {
        return data;
    }
}
