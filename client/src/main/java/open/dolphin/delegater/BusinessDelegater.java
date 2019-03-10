package open.dolphin.delegater;

import java.lang.reflect.ParameterizedType;

/**
 * BusinessDelegater.
 *
 * @author pns
 */
public class BusinessDelegater<T> {
    public static final int NO_ERROR = 0;
    public static final int ERROR = -1;

    private int errorCode = NO_ERROR;
    private String errorMessage = "delegate error";

    // 呼び出し元の class
    private Class<T> target;

    public BusinessDelegater() {
        // 呼び出し元の class を取得
        String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
        try {
            target = (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            processError(e);
        }
    }

    public T getService() {
        try {
            return DolphinClientContext.getContext().getWebTarget().proxy(target);
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
