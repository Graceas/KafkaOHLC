package ru.Graceas.examples.kafkaohlc.model;

public class TickerWindow {
    String ticker;
    long timestamp;

    public TickerWindow(String ticker, long timestamp) {
        this.ticker = ticker;
        this.timestamp = timestamp;
    }
}
