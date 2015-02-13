package open.dolphin.delegater;

/**
 *
 * @author pns
 */
public class BusinessDelegater {
    public static final int NO_ERROR = 0;
    public static final int ERROR = -1;

    private int errorCode = NO_ERROR;
    private String errorMessage = "delegate error";

    public BusinessDelegater() {
    }

    public <T> T getService(Class<T> clazz) {
        try {
            return DolphinClientContext.getContext().getWebTarget().proxy(clazz);
        } catch (Exception e) {
            processError(e);
        }
        return null;
    }

    public void setErrorCode(int errCode) {
        this.errorCode = errCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public boolean isNoError() {
        return errorCode == NO_ERROR;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMesseage) {
        this.errorMessage = errorMesseage;
    }

    public void processError(Exception e) {
        setErrorMessage(e.toString());
        setErrorCode(ERROR);
        e.printStackTrace(System.err);
    }
}
