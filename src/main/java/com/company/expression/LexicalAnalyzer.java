package com.company.expression;

import com.company.currency.Currency;
import com.company.currency.CurrencyConfiguration;
import com.company.currency.CurrencyValue;
import com.company.exception.ParseException;
import com.company.util.CustomTokenizer;

import javax.xml.parsers.ParserConfigurationException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.company.expression.SyntaxType.*;

public class LexicalAnalyzer {

    private Map<String, Currency> converterFunctions;
    private List<String> mathFunctions;
    private String text;
    private CustomTokenizer tokenizer;
    private CurrencyConfiguration currencyConfiguration;

    public static final String VALUE_REGEXP = "[0-9]*\\.?[0-9]+";
    public static final String FUNCTION_REGEXP = "^[a-zA-Z]+$";

    public LexicalAnalyzer(String text) throws ParserConfigurationException {
        this.text = text;
        this.converterFunctions = new HashMap<>();

        ArrayList<String> delims = new ArrayList<>(OperationResolver.operations);
        Collections.addAll(delims, "(", ")", " ");
        this.tokenizer = new CustomTokenizer(text, delims, true);

        currencyConfiguration = new CurrencyConfiguration();
        for (Currency currency : currencyConfiguration.getCurrencies()) {
            String currencyName = currency.getName();
            String functionPrefix = "to" + Character.toUpperCase(currencyName.charAt(0)) + currencyName.substring(1);
            converterFunctions.put(functionPrefix, currency);
        }
    }

    public Lexeme getNextLexeme() throws ParseException {
        if (!tokenizer.hasNext())
            return new Lexeme(SyntaxType.FINISH, text.isEmpty() ? 0 : text.length() - 1, "", null);

        return parseLexeme(tokenizer.nextToken());
    }

    private Lexeme parseLexeme(CustomTokenizer.Token token) throws ParseException {
        CurrencyValue currencyValue = null;
        SyntaxType syntaxType = parseSyntaxTypeByToken(token.getToken());

        switch (syntaxType) {
            case VALUE:
                currencyValue = parseCurrencyValue(token);
                break;
            case INVALID:
                syntaxType = converterFunctions.containsKey(token.getToken()) ? FUNCTION : syntaxType;
                break;
        }

        return new Lexeme(syntaxType, token.getPosition(), token.getToken(), currencyValue);
    }

    private CurrencyValue parseCurrencyValue(CustomTokenizer.Token token) throws ParseException {
        Pattern pattern = Pattern.compile(VALUE_REGEXP);
        Matcher matcher = pattern.matcher(token.getToken());

        if (!matcher.find())
            return null;

        return currencyConfiguration.resolveCurrencyValue(token.getToken());
    }

    private SyntaxType parseSyntaxTypeByToken(String token) {
        switch (token) {
            case " ": return WHITESPACE;
            case "+": return PLUS;
            case "-": return MINUS;
            case "(": return LBRACKET;
            case ")": return RBRACKET;
        }

        if (isValue(token))
            return VALUE;

        if (isFunction(token))
            return FUNCTION;

        return INVALID;
    }

    private boolean isValue(String token) {
        Pattern pattern = Pattern.compile(VALUE_REGEXP);
        Matcher matcher = pattern.matcher(token);

        return matcher.find();
    }

    private boolean isFunction(String token) {
        Pattern pattern = Pattern.compile(FUNCTION_REGEXP);
        Matcher matcher = pattern.matcher(token);

        return matcher.find();
    }

    public Map<String, Currency> getConverterFunctions() {
        return converterFunctions;
    }
}
