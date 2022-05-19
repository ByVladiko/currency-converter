package com.company.currency;

public class Converter {

    public CurrencyValue convert(CurrencyValue value, Currency currency) {
        return new CurrencyValue(value.getValue() / value.getCurrency().getValue() * currency.getValue(), currency);
    }

}
