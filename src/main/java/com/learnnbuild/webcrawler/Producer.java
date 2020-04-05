package com.learnnbuild.webcrawler;

import com.learnnbuild.webcrawler.kafka.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;


public class Producer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

//    @Value("${kafka.topic.json}")
//    private String jsonTopic;

    @Autowired
    private KafkaTemplate<String, Payload> kafkaTemplate;

    public void send(Payload payload) {
        LOGGER.info("sending payload='{}'", payload.toString());
        kafkaTemplate.send("webCrawler", payload);
    }
}
