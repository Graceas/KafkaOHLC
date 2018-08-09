package ru.Graceas.examples.kafkaohlc;

import ru.Graceas.examples.kafkaohlc.json.JsonSerializer;
import ru.Graceas.examples.kafkaohlc.model.Trade;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.Random;

public class TradeGenProducer {

    public static KafkaProducer<String, Trade> producer = null;

    public static void main(String[] args) throws Exception {

        System.out.println("Press CTRL-C to stop generating data");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Shutting Down");
                if (producer != null)
                    producer.close();
            }
        });

        JsonSerializer<Trade> tradeSerializer = new JsonSerializer<>();

        Properties props = new Properties();

        props.put("bootstrap.servers", Constants.BROKER_URI);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", tradeSerializer.getClass().getName());

        producer = new KafkaProducer<>(props);

        Random random = new Random();
        long   iter   = 0;
        double log    = 0.00;
        double amount = 0.00;
        double price  = Constants.START_PRICE;

        while (true) {
            iter++;
            amount = 1;

            price = price + random.nextInt(Constants.MAX_PRICE_CHANGE * 2) - random.nextInt(Constants.MAX_PRICE_CHANGE * 2);

            if (price < 2000) {
                price += 1000;
            }

            Trade trade = new Trade(price, amount);
            ProducerRecord<String, Trade> record = new ProducerRecord<>(Constants.TRADE_TOPIC, Constants.TRADE_TICKER, trade);

            producer.send(record, (RecordMetadata r, Exception e) -> {
                if (e != null) {
                    System.out.println("Error producing to topic " + r.topic());
                    e.printStackTrace();
                }
            });

            Thread.sleep(Constants.DELAY_BETWEEN_SENDING_MS);
        }
    }
}
