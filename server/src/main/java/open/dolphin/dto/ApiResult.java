package open.dolphin.dto;

/**
 * API Results.
 *
 * @author pns
 */
public class ApiResult {
    /**
     * 実施日 (例: 2014-10-17)
     */
    private String date;

    /**
     * 実施時間 (例: 14:15:00)
     */
    private String time;

    /**
     * Api結果コード (例: 90)
     */
    private String apiResult;

    /**
     * Api結果メッセージ (例: 他端末使用中)
     */
    private String apiResultMessage;

    /**
     * エラーコード (例: 02)
     */
    private String errorCode;

    /**
     * エラーメッセージ (例: 警告がある病名が存在します)
     */
    private String errorMessage;

    /**
     * 警告情報
     */
    private ApiWarning[] warningInfo;

    /**
     * date
     *
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * date
     *
     * @param date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * time
     *
     * @return time
     */
    public String getTime() {
        return time;
    }

    /**
     * time
     *
     * @param time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * apiResult
     *
     * @return apiResult
     */
    public String getApiResult() {
        return apiResult;
    }

    /**
     * apiResult
     *
     * @param apiResult to set
     */
    public void setApiResult(String apiResult) {
        this.apiResult = apiResult;
    }

    /**
     * apiResultMessage
     *
     * @return apiResultMessage
     */
    public String getApiResultMessage() {
        return apiResultMessage;
    }

    /**
     * apiResultMessage
     *
     * @param apiResultMessage to set
     */
    public void setApiResultMessage(String apiResultMessage) {
        this.apiResultMessage = apiResultMessage;
    }

    /**
     * errorCode
     *
     * @return errorCode
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * errorCode
     *
     * @param errorCode to set
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * errorMessage
     *
     * @return errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * errorMessage
     *
     * @param errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * warningInfo
     *
     * @return warningInfo
     */
    public ApiWarning[] getWarningInfo() {
        return warningInfo;
    }

    /**
     * warningInfo
     *
     * @param warningInfo to set
     */
    public void setWarningInfo(ApiWarning[] warningInfo) {
        this.warningInfo = warningInfo;
    }
}
