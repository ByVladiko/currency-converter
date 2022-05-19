package com.company.currency;

public class Currency {

    private String name;
    private String symbol;
    private PositionType positionType;
    private double value;

    public Currency(String name, String symbol, PositionType positionType, double value) {
        this.name = name;
        this.symbol = symbol;
        this.positionType = positionType;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public PositionType getPositionType() {
        return positionType;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", positionType=" + positionType +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Currency currency = (Currency) o;

        if (Double.compare(currency.value, value) != 0)
            return false;

        if (name != null ? !name.equals(currency.name) : currency.name != null)
            return false;

        if (symbol != null ? !symbol.equals(currency.symbol) : currency.symbol != null)
            return false;

        return positionType == currency.positionType;
    }

    @Override
    public int hashCode() {
        int result;
        result = name != null ? name.hashCode() : 0;
        result = 31 * result + (symbol != null ? symbol.hashCode() : 0);
        result = 31 * result + (positionType != null ? positionType.hashCode() : 0);
        long temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
