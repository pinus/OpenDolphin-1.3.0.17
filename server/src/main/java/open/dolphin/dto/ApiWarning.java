package open.dolphin.dto;

/**
 * API Warnings.
 *
 * @author pns
 */
public class ApiWarning {
    /**
     * 警告コード (例: W01)
     */
    private String warning;

    /**
     * 警告メッセージ (例: 廃止・移行先・推奨のある病名が存在します)
     */
    private String warningMessage;

    /**
     * 警告対象のコード (例: 2049.7274039.8002)
     */
    private String warningCode;

    /**
     * warning
     *
     * @return warning
     */
    public String getWarning() {
        return warning;
    }

    /**
     * warning
     *
     * @param warning to set
     */
    public void setWarning(String warning) {
        this.warning = warning;
    }

    /**
     * warningMessage
     *
     * @return warningMessage
     */
    public String getWarningMessage() {
        return warningMessage;
    }

    /**
     * warningMessage
     *
     * @param warningMessage to set
     */
    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    /**
     * warningCode
     *
     * @return warningCode
     */
    public String getWarningCode() {
        return warningCode;
    }

    /**
     * warningCode
     *
     * @param warningCode to set
     */
    public void setWarningCode(String warningCode) {
        this.warningCode = warningCode;
    }
}
