package com.kakaopay.coupon.expiration.notification.load.kafka;

import com.google.gson.Gson;
import com.kakaopay.coupon.expiration.notification.load.model.NotificationDto;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;

import java.util.Properties;

public class Producer {
    private static final Properties PROPS = new Properties();
    private static KafkaProducer<Integer, String> kafkaProducer;
    private final Gson gson = new Gson();

    public Producer() {
        init();
        kafkaProducer = new KafkaProducer<> (PROPS);
    }

    private static void init() {
        PROPS.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        PROPS.put(ProducerConfig.ACKS_CONFIG, "1");
        PROPS.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        PROPS.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
    }

    public void publish(String topicName, int partition, NotificationDto notificationDto) {
        ProducerRecord<Integer, String> record = new ProducerRecord<> (topicName, partition, gson.toJson(notificationDto));
        kafkaProducer.send(record, (metadata, exception) -> {
            if (exception != null) {
                System.out.println("Exception Occurred!");
                exception.printStackTrace();
            }
        });
        System.out.println(record);
    }

    public void flush() {
        kafkaProducer.flush();
    }
}
