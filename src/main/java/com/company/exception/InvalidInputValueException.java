package com.company.exception;

import com.company.expression.Lexeme;

public class InvalidInputValueException extends CalculateExpressionException {

    public InvalidInputValueException() {
    }

    public InvalidInputValueException(String message) {
        super(message);
    }

    public InvalidInputValueException(Lexeme lexeme) {
        super(String.format("Invalid expression input: '%s' on position index: %d",
                lexeme.getText(), lexeme.getPosition()));
    }
}
