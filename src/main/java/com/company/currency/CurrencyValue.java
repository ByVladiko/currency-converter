package com.company.currency;

public class CurrencyValue {

    private final double value;
    private Currency currency;

    public CurrencyValue(double value, Currency currency) {
        this.value = value;
        this.currency = currency;
    }

    public double getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        if (currency == null)
            return String.valueOf(value);

        if (currency.getPositionType().equals(PositionType.POSTFIX))
            return value + currency.getSymbol();

        if (currency.getPositionType().equals(PositionType.PREFIX))
            return currency.getSymbol() + value;

        return "Value: " + value + " Currency: " + currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        CurrencyValue that = (CurrencyValue) o;

        if (Double.compare(that.value, value) != 0)
            return false;

        return currency != null ? currency.equals(that.currency) : that.currency == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp = Double.doubleToLongBits(value);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }
}
