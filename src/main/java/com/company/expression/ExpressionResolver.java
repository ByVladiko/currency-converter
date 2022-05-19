package com.company.expression;

import com.company.currency.Converter;
import com.company.currency.Currency;
import com.company.currency.CurrencyValue;
import com.company.exception.UnsupportedOperationException;
import com.company.exception.*;

import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static com.company.expression.SyntaxType.*;

public class ExpressionResolver {

    private final Stack<Lexeme> postfixResultStack;
    private final Stack<Lexeme> operationsStack;
    private LexicalAnalyzer lexicalAnalyzer;
    private final Map<SyntaxType, String> operationTypes;
    private Converter converter;
    private OperationResolver operationResolver;

    public ExpressionResolver() {
        this.postfixResultStack = new Stack<>();
        this.operationsStack = new Stack<>();
        this.operationTypes = new HashMap<>() {{
            put(PLUS, "+");
            put(MINUS, "-");
        }};
        this.converter = new Converter();
        this.operationResolver = new OperationResolver();
    }

    public CurrencyValue resolve(String expression) throws CalculateExpressionException, ParserConfigurationException {
        postfixResultStack.clear();
        operationsStack.clear();
        lexicalAnalyzer = new LexicalAnalyzer(expression);
        fillPostfixStack();
        return calcPostfixStack();
    }

    private void fillPostfixStack() throws InvalidInputValueException, ParseException {
        for (Lexeme lexeme = lexicalAnalyzer.getNextLexeme();
             !lexeme.getSyntaxType().equals(FINISH);
             lexeme = lexicalAnalyzer.getNextLexeme()) {

            SyntaxType syntaxType = lexeme.getSyntaxType();
            switch (syntaxType) {
                case WHITESPACE:
                    continue;
                case LBRACKET:
                    operationsStack.push(lexeme);
                    continue;
                case VALUE:
                    postfixResultStack.push(lexeme);
                    continue;
                case INVALID:
                    throw new InvalidInputValueException(lexeme);
            }

            if (operationTypes.containsKey(syntaxType) || syntaxType == FUNCTION) {
                resolveOperation(lexeme);
                continue;
            }

            if (syntaxType == RBRACKET) {
                resolveRbracket(lexeme);
                continue;
            }

            throw new InvalidInputValueException(lexeme);
        }

        if (operationsStack.empty())
            return;

        for (Lexeme lexeme : operationsStack) {
            postfixResultStack.push(lexeme);
        }
    }

    private void resolveRbracket(Lexeme lexeme) throws InvalidInputValueException {
        SyntaxType operationSyntaxType = operationsStack.peek().getSyntaxType();
        while (operationSyntaxType != LBRACKET && !operationsStack.isEmpty()) {
            postfixResultStack.push(operationsStack.pop());

            if (operationsStack.isEmpty())
                throw new InvalidInputValueException("Wrong right bracket ')' order in position: " + lexeme.getPosition());

            operationSyntaxType = operationsStack.peek().getSyntaxType();
        }

        if (operationSyntaxType == LBRACKET) {
            operationsStack.pop();
            return;
        }

        throw new InvalidInputValueException("Wrong right bracket ')' order in position: " + lexeme.getPosition());
    }

    private void resolveOperation(Lexeme lexeme) {
        if (operationsStack.empty() || operationsStack.peek().getSyntaxType() == LBRACKET) {
            operationsStack.push(lexeme);
            return;
        }

        SyntaxType operationSyntaxType = operationsStack.peek().getSyntaxType();
        while (lexeme.getSyntaxType().getPriority() <= operationSyntaxType.getPriority()) {
            postfixResultStack.push(operationsStack.pop());

            if (operationsStack.empty())
                break;

            operationSyntaxType = operationsStack.peek().getSyntaxType();
        }

        operationsStack.push(lexeme);
    }

    private CurrencyValue calcPostfixStack() throws CalculateExpressionException {
        Stack<CurrencyValue> operands = new Stack<>();
        for (Lexeme lexeme : postfixResultStack) {
            SyntaxType syntaxType = lexeme.getSyntaxType();
            if (syntaxType == VALUE) {
                operands.push(lexeme.getValue());
                continue;
            }

            if (operationTypes.containsKey(syntaxType)) {
                operands.push(operationResolver.resolve(operands, lexeme));
                continue;
            }

            if (syntaxType == FUNCTION) {
                Map<String, Currency> converterFunctions = lexicalAnalyzer.getConverterFunctions();
                if (converterFunctions.containsKey(lexeme.getText())) {
                    operands.push(converter.convert(operands.pop(), converterFunctions.get(lexeme.getText())));
                    continue;
                }
            }

            throw new UnsupportedOperationException("Unsupported operation: '"
                    + lexeme.getText() + "' pos: " + lexeme.getPosition());
        }

        if (operands.size() > 1)
            throw new InvalidResolveExpression("Missing operations between numbers");

        if (operands.size() < 1)
            throw new InvalidResolveExpression("Missing result");

        return operands.pop();
    }
}
