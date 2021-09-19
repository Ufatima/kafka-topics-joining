package com.elisa.kafkaproducer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class KafkaProducerController {

    private static final String KEY = "FIXED-KEY";

    @RequestMapping("/topicProduce/")
    public void sendMessages() {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");


        List<String> strings = Arrays.asList("Asiakaslähtöisyys", "Vastuullisuus", "Uusiutuminen", "Tuloksellisuus",
                "Yhteistyö");
        
        Producer<String, String> producer = new KafkaProducer<>(props);

        try {
            for (String a: strings) {
                // Every 10 seconds send a message
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {}

                    producer.send(new ProducerRecord<String, String>("first-topic", KEY, a));
                    producer.send(new ProducerRecord<String, String>("second-topic", KEY, a));
                    producer.send(new ProducerRecord<String, String>("third-topic", KEY, a));

            }
        } finally {
            producer.close();
        }

    }

}
