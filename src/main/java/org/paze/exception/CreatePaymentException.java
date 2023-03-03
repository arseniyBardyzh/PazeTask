package org.paze.exception;

public class CreatePaymentException extends RuntimeException{
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public CreatePaymentException() {
        super();
    }

    public CreatePaymentException(String message, int errorCode, String errorMessage) {
        super(message);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public CreatePaymentException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreatePaymentException(Throwable cause) {
        super(cause);
    }

    protected CreatePaymentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
