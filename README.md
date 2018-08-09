# kafka-ohlc
Kafka OHLC Example - Some aggregates on generated trading data

This example reads generated trading data from topic 'trade'. Each trade includes a price and amount of stocks.
We output statistics on the previous 6 second window - open price, high price, low price and close price.

To run:
-------
0. Build the project with `mvn package`, this will generate an original-final-kafka-streams-ohlc-1.0-SN.jar with the streams app and all its dependencies.
1. Create a stocks input topic:

    `sh bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic trade --partitions 1 --replication-factor 1`

2. Next, run following command for generate same trades data and stop it with ctrl-c when you think there's enough data:
`$ java -cp target/final-kafka-streams-ohlc-1.0-SN.jar ru.Graceas.examples.kafkaohlc.TradeGenProducer`

3. Run the streams app:
`java -cp target/final-kafka-streams-ohlc-1.0-SN.jar ru.Graceas.examples.kafkaohlc.TradeStatsExample`
Streams apps typically run forever, but this one will just run for a minute and exit

4. Check the results:
`bin/kafka-console-consumer.sh --topic trade-stats-output --from-beginning --bootstrap-server localhost:9092 --property print.key=true`

If you want to reset state and re-run the application execute the following commands:

`sh bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic trade`
`sh bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic trade-stats-store`
`sh bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic trade-stats-output`
`sh bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic trade --partitions 1 --replication-factor 1`


Example output:
_______________

`
{"ticker":"btc","timestamp":1533801372000}	{"open":20031.0,"high":20690.0,"low":19934.0,"close":20690.0,"amount":36.0}
{"ticker":"btc","timestamp":1533801378000}	{"open":20713.0,"high":20958.0,"low":19559.0,"close":19624.0,"amount":57.0}
{"ticker":"btc","timestamp":1533801384000}	{"open":19749.0,"high":19749.0,"low":18825.0,"close":18998.0,"amount":58.0}
{"ticker":"btc","timestamp":1533801390000}	{"open":19067.0,"high":19360.0,"low":18166.0,"close":18336.0,"amount":58.0}
{"ticker":"btc","timestamp":1533801396000}	{"open":18366.0,"high":18382.0,"low":17319.0,"close":18033.0,"amount":57.0}
{"ticker":"btc","timestamp":1533801402000}	{"open":17875.0,"high":18006.0,"low":16801.0,"close":17017.0,"amount":59.0}
{"ticker":"btc","timestamp":1533801408000}	{"open":17063.0,"high":17430.0,"low":16793.0,"close":16862.0,"amount":58.0}
{"ticker":"btc","timestamp":1533801414000}	{"open":16745.0,"high":17610.0,"low":16292.0,"close":17610.0,"amount":57.0}
{"ticker":"btc","timestamp":1533801420000}	{"open":17738.0,"high":17812.0,"low":16900.0,"close":16939.0,"amount":59.0}
{"ticker":"btc","timestamp":1533801426000}	{"open":16946.0,"high":16946.0,"low":16387.0,"close":16639.0,"amount":57.0}
{"ticker":"btc","timestamp":1533801432000}	{"open":16782.0,"high":17009.0,"low":16607.0,"close":16821.0,"amount":58.0}
{"ticker":"btc","timestamp":1533801438000}	{"open":16937.0,"high":16937.0,"low":15689.0,"close":15739.0,"amount":58.0}
{"ticker":"btc","timestamp":1533801444000}	{"open":15786.0,"high":16163.0,"low":14605.0,"close":14614.0,"amount":58.0}
{"ticker":"btc","timestamp":1533801450000}	{"open":14530.0,"high":14587.0,"low":13875.0,"close":13875.0,"amount":57.0}
{"ticker":"btc","timestamp":1533801456000}	{"open":13796.0,"high":14073.0,"low":13321.0,"close":13448.0,"amount":58.0}
{"ticker":"btc","timestamp":1533801462000}	{"open":13413.0,"high":14217.0,"low":13366.0,"close":14217.0,"amount":58.0}
{"ticker":"btc","timestamp":1533801468000}	{"open":14317.0,"high":14317.0,"low":13766.0,"close":13858.0,"amount":22.0}
`
