package com.company.expression;

import com.company.currency.CurrencyValue;
import com.company.exception.InvalidResolveExpression;

import java.util.Set;
import java.util.Stack;

public class OperationResolver {

    public static Set<String> operations = Set.of("+", "-");

    public CurrencyValue resolve(Stack<CurrencyValue> operands, Lexeme lexeme) throws InvalidResolveExpression {
        SyntaxType syntaxType = lexeme.getSyntaxType();
        if (operands.empty())
            throw new InvalidResolveExpression("Unable to calculate operation '"
                    + lexeme.getText() + "' pos: " + lexeme.getPosition());

        CurrencyValue right = operands.pop();

        if (operands.empty())
            throw new InvalidResolveExpression("Unable to calculate operation '"
                    + lexeme.getText() + "' pos: " + lexeme.getPosition());

        CurrencyValue left = operands.pop();

        if (!left.getCurrency().equals(right.getCurrency()))
            throw new InvalidResolveExpression("Unable to calculate values with different exchange currency '"
                    + lexeme.getText() + "' pos: " + lexeme.getPosition());

        switch (syntaxType) {
            case PLUS : return new CurrencyValue(left.getValue() + right.getValue(), left.getCurrency());
            case MINUS: return new CurrencyValue(left.getValue() - right.getValue(), left.getCurrency());
        }

        throw new UnsupportedOperationException("Unsupported operation '"
                + lexeme.getText() + "' on pos: " + lexeme.getPosition());
    }
}