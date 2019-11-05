package cn.org.atool.mybatis.fluent.exception;

public class NullParameterException extends RuntimeException {

    public NullParameterException() {
    }

    public NullParameterException(String message) {
        super(message);
    }

    public NullParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullParameterException(Throwable cause) {
        super(cause);
    }

    public NullParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
