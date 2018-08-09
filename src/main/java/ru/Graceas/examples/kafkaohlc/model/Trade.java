package ru.Graceas.examples.kafkaohlc.model;

public class Trade {

    double price;
    double amount;

    public Trade(double price, double amount) {
        this.price = price;
        this.amount = amount;
    }

    public double getPrice() {
        return this.price;
    }

    public double getAmount() {
        return this.amount;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "amount="       + amount +
                ", price="      + price  +
                '}';
    }
}
