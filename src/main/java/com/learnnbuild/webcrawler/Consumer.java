package com.learnnbuild.webcrawler;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;

import com.learnnbuild.webcrawler.entity.WebCrawlerResponseEntity;
import com.learnnbuild.webcrawler.kafka.Payload;
import com.learnnbuild.webcrawler.model.pojo.RequestStatus;
import com.learnnbuild.webcrawler.repository.WebCrawlerRequestStatusRepository;
import com.learnnbuild.webcrawler.repository.WebCrawlerResponseRepository;
import com.learnnbuild.webcrawler.service.WebCrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;


public class Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    private CountDownLatch latch = new CountDownLatch(1);

    @Autowired
    private WebCrawlerService webCrawlerService;

    @Autowired
    private WebCrawlerResponseRepository webCrawlerResponseRepository;

    @Autowired
    private WebCrawlerRequestStatusRepository webCrawlerRequestStatusRepository;

    public CountDownLatch getLatch() {
        return latch;
    }

    @KafkaListener(topics = "webCrawler")
    public void receive(Payload payload) {
        LOGGER.info("received payload='{}'", payload.toString());
        WebCrawlerResponseEntity response = new WebCrawlerResponseEntity();
        webCrawlerService.updateWebCrawlerRequestStatus(payload.getUniqueIdentifier(), RequestStatus.IN_PROCESS);
        response.setUniqueToken(payload.getUniqueIdentifier());
        webCrawlerService.getWebPagesInfo(payload.getURL(), payload.getDepth(), response);
        webCrawlerService.setLinks(new HashSet<>());
        if(!response.isEmpty())
            webCrawlerResponseRepository.save(response);
        webCrawlerService.updateWebCrawlerRequestStatus(payload.getUniqueIdentifier(), RequestStatus.PROCESSED);
    }
}