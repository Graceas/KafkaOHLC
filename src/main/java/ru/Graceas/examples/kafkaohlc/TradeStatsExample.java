package ru.Graceas.examples.kafkaohlc;

import ru.Graceas.examples.kafkaohlc.json.JsonDeserializer;
import ru.Graceas.examples.kafkaohlc.json.JsonSerializer;
import ru.Graceas.examples.kafkaohlc.json.WrapperSerde;
import ru.Graceas.examples.kafkaohlc.model.TickerWindow;
import ru.Graceas.examples.kafkaohlc.model.Trade;
import ru.Graceas.examples.kafkaohlc.model.TradeStats;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.TimeWindows;

import java.util.Properties;

public class TradeStatsExample {

    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, Constants.APP_ID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BROKER_URI);
        props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, TradeSerde.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KStreamBuilder builder = new KStreamBuilder();

        KStream<String, Trade> source = builder.stream(Constants.TRADE_TOPIC);

        KStream<TickerWindow, TradeStats> stats = source.groupByKey()
                .aggregate(TradeStats::new,
                        (k, v, tradestats) -> tradestats.add(v),
                        TimeWindows.of(Constants.TIME_WINDOW_MS),
                        new TradeStatsSerde(),
                        Constants.STORE_TOPIC)
                .toStream((key, value) -> new TickerWindow(key.key(), key.window().start()));

        stats.to(new TickerWindowSerde(), new TradeStatsSerde(),  Constants.OUTPUT_TOPIC);

        KafkaStreams streams = new KafkaStreams(builder, props);

        streams.cleanUp();

        streams.start();

        Thread.sleep(60000L);

        streams.close();

    }

    static public final class TradeSerde extends WrapperSerde<Trade> {
        public TradeSerde() {
            super(new JsonSerializer<Trade>(), new JsonDeserializer<Trade>(Trade.class));
        }
    }

    static public final class TradeStatsSerde extends WrapperSerde<TradeStats> {
        public TradeStatsSerde() {
            super(new JsonSerializer<TradeStats>(), new JsonDeserializer<TradeStats>(TradeStats.class));
        }
    }

    static public final class TickerWindowSerde extends WrapperSerde<TickerWindow> {
        public TickerWindowSerde() {
            super(new JsonSerializer<TickerWindow>(), new JsonDeserializer<TickerWindow>(TickerWindow.class));
        }
    }

}
