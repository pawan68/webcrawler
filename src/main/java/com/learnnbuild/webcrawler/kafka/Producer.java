package com.learnnbuild.webcrawler.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;


public class Producer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private KafkaTemplate<String, Payload> kafkaTemplate;

    public void send(Payload payload) {
        LOGGER.info("sending payload='{}'", payload.toString());
        kafkaTemplate.send("webCrawler", payload);
    }
}
