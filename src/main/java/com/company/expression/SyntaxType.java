package com.company.expression;

public enum SyntaxType {

    VALUE( 0),
    WHITESPACE(0),
    LBRACKET(1),
    RBRACKET(1),
    PLUS(2),
    MINUS(2),
    FUNCTION( 3),
    FINISH(0),
    INVALID( -1);

    private final int priority;

    SyntaxType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}