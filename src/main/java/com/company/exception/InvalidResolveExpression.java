package com.company.exception;

public class InvalidResolveExpression extends CalculateExpressionException {

    public InvalidResolveExpression() {
    }

    public InvalidResolveExpression(String message) {
        super(message);
    }

    public InvalidResolveExpression(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidResolveExpression(Throwable cause) {
        super(cause);
    }
}
