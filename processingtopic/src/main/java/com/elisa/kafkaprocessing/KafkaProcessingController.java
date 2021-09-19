package com.elisa.kafkaprocessing;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Properties;

@RestController
public class KafkaProcessingController {

    private KafkaStreams streamsInnerJoin;

    @RequestMapping("/topicsJoin/")
    public void startStreamStreamInnerJoin() {

        stop();

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-stream-inner-join");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> firstSource = builder.stream("first-topic");
        KStream<String, String> secondSource = builder.stream("second-topic");
        KStream<String, String> thirdSource = builder.stream("third-topic");

        KStream<String, String> joined = firstSource.join(secondSource,
                (leftValue, rightValue) -> leftValue + "," + rightValue, /* ValueJoiner */
                JoinWindows.of(Duration.ofMinutes(5)),
                Joined.with(
                        Serdes.String(), /* key */
                        Serdes.String(),   /* left value */
                        Serdes.String())  /* right value */
        ).join(thirdSource,
                (leftValue, rightValue) -> leftValue + "," + rightValue,
                JoinWindows.of(Duration.ofMinutes(5)),
                Joined.with(
                        Serdes.String(),
                        Serdes.String(),
                        Serdes.String())
        );;

        joined.to("elisa-topic");

        final Topology topology = builder.build();
        streamsInnerJoin = new KafkaStreams(topology, props);
        streamsInnerJoin.start();

    }

    private void stop () {
        if (streamsInnerJoin != null) {
            streamsInnerJoin.close();
        }
    }

}
