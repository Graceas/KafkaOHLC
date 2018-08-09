package ru.Graceas.examples.kafkaohlc;

public class Constants {
    public static final String APP_ID                   = "tradestats";
    public static final String TRADE_TOPIC              = "trade";
    public static final String OUTPUT_TOPIC             = "trade-stats-output";
    public static final String STORE_TOPIC              = "trade-stats-store";
    public static final String TRADE_TICKER             = "btc";
    public static final int    MAX_PRICE_CHANGE         = 100;
    public static final int    TIME_WINDOW_MS           = 6000;
    public static final double START_PRICE              = 20000;
    public static final int    DELAY_BETWEEN_SENDING_MS = 100;
    public static final String BROKER_URI               = "localhost:9092";
}
