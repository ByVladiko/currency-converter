package com.company.currency;

import com.company.exception.ParseException;
import com.company.expression.LexicalAnalyzer;
import com.company.xml.XmlConfiguration;

import javax.xml.parsers.ParserConfigurationException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyConfiguration {

    private final List<Currency> currencies;

    public CurrencyConfiguration() throws ParserConfigurationException {
        XmlConfiguration xmlConfigurationUtil = new XmlConfiguration();
        this.currencies = xmlConfigurationUtil.readCurrencyConfiguration();
    }

    public Currency findCurrency(String symbol, PositionType positionType) {
        for (Currency currency : currencies) {
            if (currency.getPositionType() == positionType && currency.getSymbol().equals(symbol))
                return currency;
        }

        return null;
    }

    public CurrencyValue resolveCurrencyValue(String input) throws ParseException {
        Pattern pattern = Pattern.compile(LexicalAnalyzer.VALUE_REGEXP);
        Matcher matcher = pattern.matcher(input);

        if (!matcher.find())
            return null;

        String symbolCurrency = input.replace(matcher.group(), "");
        if (symbolCurrency.isEmpty())
            return new CurrencyValue(Double.parseDouble(matcher.group()), null);


        PositionType positionType = input.indexOf(symbolCurrency) > 0 ? PositionType.POSTFIX : PositionType.PREFIX;
        Currency currency = findCurrency(symbolCurrency, positionType);

        if (currency == null)
            throw new ParseException("Unknown currency value " + input);

        return new CurrencyValue(Double.parseDouble(matcher.group()), currency);
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }
}
