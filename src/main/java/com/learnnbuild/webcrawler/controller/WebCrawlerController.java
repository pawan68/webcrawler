package com.learnnbuild.webcrawler.controller;

import com.learnnbuild.webcrawler.Producer;
import com.learnnbuild.webcrawler.model.pojo.*;
import com.learnnbuild.webcrawler.repository.WebCrawlerRequestStatusRepository;
import com.learnnbuild.webcrawler.repository.WebCrawlerResponseRepository;
import com.learnnbuild.webcrawler.service.WebCrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WebCrawlerController {


    @Autowired
    private WebCrawlerService webCrawlerService;

    @Autowired
    private Producer producer;

    @Autowired
    private WebCrawlerResponseRepository webCrawlerResponseRepository;

    @Autowired
    private WebCrawlerRequestStatusRepository webCrawlerRequestStatusRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(WebCrawlerController.class);

    @RequestMapping("/getCrawledResponse")
    public ResponseEntity<WebCrawlerResponse> crawlWeb(@RequestBody WebCrawlerRequest request) {
        return webCrawlerService.getCrawledResponse(request);
    }

    @RequestMapping("/submitCrawlRequest")
    public ResponseEntity<WebCrawlerSubmitResponse> submitCrawlRequest(@RequestBody WebCrawlerSubmitRequest request){
        return webCrawlerService.process(request);
    }

    @RequestMapping("/getStatus")
    public ResponseEntity<WebCrawlerRequestStatusResponse> getStatus(@RequestBody WebCrawlerRequest request){
        return webCrawlerService.process(request);
    }
}
