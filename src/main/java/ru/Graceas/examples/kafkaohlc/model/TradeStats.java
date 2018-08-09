package ru.Graceas.examples.kafkaohlc.model;

public class TradeStats {

    double open;
    double high;
    double low = Double.MAX_VALUE;
    double close;

    double amount;

    public TradeStats add(Trade trade) {

        this.open    = this.open == 0.00 ? trade.getPrice() : this.open;
        this.high    = this.high > trade.getPrice() ? this.high : trade.getPrice();
        this.low     = this.low  < trade.getPrice() ? this.low  : trade.getPrice();
        this.close   = trade.getPrice();

        this.amount += trade.getAmount();

        return this;
    }
}
