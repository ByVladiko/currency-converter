package com.company.expression;

import com.company.currency.CurrencyValue;

public class Lexeme {

    private SyntaxType syntaxType;
    private int position;
    private String text;
    private CurrencyValue value;

    public Lexeme(SyntaxType syntaxType, int position, String text, CurrencyValue value) {
        this.syntaxType = syntaxType;
        this.position = position;
        this.text = text;
        this.value = value;
    }

    public SyntaxType getSyntaxType() {
        return syntaxType;
    }

    public int getPosition() {
        return position;
    }

    public String getText() {
        return text;
    }

    public CurrencyValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value == null ? text : String.valueOf(value);
    }
}
