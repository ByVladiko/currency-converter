package com.company.exception;

public class CalculateExpressionException extends Exception {

    public CalculateExpressionException() {
    }

    public CalculateExpressionException(String message) {
        super(message);
    }

    public CalculateExpressionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CalculateExpressionException(Throwable cause) {
        super(cause);
    }
}
