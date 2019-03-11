package open.dolphin.delegater;

import org.apache.log4j.Logger;

import java.lang.reflect.ParameterizedType;

/**
 * BusinessDelegater.
 *
 * @author pns
 */
public class BusinessDelegater<T> {
    public enum Result { NO_ERROR, ERROR }

    private Result errorCode = Result.NO_ERROR;
    private String errorMessage = "delegate error";

    protected Logger logger;

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
        logger = Logger.getLogger(target);
    }

    public T getService() {
        try {
            return DolphinClientContext.getContext().getWebTarget().proxy(target);
        } catch (Exception e) {
            processError(e);
        }
        return null;
    }

    public void setErrorCode(Result errCode) {
        this.errorCode = errCode;
    }

    public Result getErrorCode() {
        return errorCode;
    }

    public boolean isNoError() {
        return errorCode.equals(Result.NO_ERROR);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMesseage) {
        this.errorMessage = errorMesseage;
    }

    public void processError(Exception e) {
        setErrorMessage(e.toString());
        setErrorCode(Result.ERROR);
        e.printStackTrace(System.err);
    }
}
