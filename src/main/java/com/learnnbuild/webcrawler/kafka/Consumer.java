package com.learnnbuild.webcrawler.kafka;

import com.learnnbuild.webcrawler.repository.WebCrawlerRequestStatusRepository;
import com.learnnbuild.webcrawler.repository.WebCrawlerResponseRepository;
import com.learnnbuild.webcrawler.service.PersistenceService;
import com.learnnbuild.webcrawler.service.WebCrawlerService;
import com.learnnbuild.webcrawler.util.WebCrawlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;


public class Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    private CountDownLatch latch = new CountDownLatch(1);

    @Autowired
    private WebCrawlerService webCrawlerService;

    @Autowired
    private WebCrawlerResponseRepository webCrawlerResponseRepository;

    @Autowired
    private WebCrawlerRequestStatusRepository webCrawlerRequestStatusRepository;

    @Autowired
    private WebCrawlerUtil webCrawlerUtil;

    @Autowired
    private PersistenceService persistenceService;

    public CountDownLatch getLatch() {
        return latch;
    }

    @KafkaListener(topics = "webCrawler")
    public void receive(Payload payload) {
        try {
            LOGGER.info("received payload='{}'", payload.toString());
            webCrawlerService.processMessage(payload);
        } catch (Exception e) {
            LOGGER.error("Error occured while reading the kafka message. ", e);
        }
    }
}