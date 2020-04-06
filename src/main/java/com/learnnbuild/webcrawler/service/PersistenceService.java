package com.learnnbuild.webcrawler.service;

import com.learnnbuild.webcrawler.entity.WebCrawlerRequestStatusEntity;
import com.learnnbuild.webcrawler.entity.WebCrawlerResponseEntity;
import com.learnnbuild.webcrawler.repository.WebCrawlerRequestStatusRepository;
import com.learnnbuild.webcrawler.repository.WebCrawlerResponseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersistenceService {
    @Autowired
    private WebCrawlerResponseRepository webCrawlerResponseRepository;

    @Autowired
    private WebCrawlerRequestStatusRepository webCrawlerRequestStatusRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceService.class);

    public void saveCrawlerResponse(WebCrawlerResponseEntity response) {
        try {
            webCrawlerResponseRepository.save(response);
        } catch (Exception e) {
            LOGGER.error("Exception occured while saving. ", e);
        }
    }

    public void saveRequestStatusResponse(WebCrawlerRequestStatusEntity response) {
        try {
            webCrawlerRequestStatusRepository.save(response);
        } catch (Exception e) {
            LOGGER.error("Exception occured while saving. ", e);
        }
    }

    public WebCrawlerRequestStatusEntity findCrawlerRequestStatusById(String uniqueToken) {
        try {
            Optional<WebCrawlerRequestStatusEntity> statusRequest = webCrawlerRequestStatusRepository.findById(uniqueToken);
            return statusRequest.orElse(null);
        } catch (Exception e) {
            LOGGER.error("Exception occured while fetching. ", e);
        }
        return null;
    }

    public WebCrawlerResponseEntity findCrawlerResponseById(String uniqueToken) {
        try {
            Optional<WebCrawlerResponseEntity> statusRequest = webCrawlerResponseRepository.findById(uniqueToken);
            return statusRequest.orElse(null);
        } catch (Exception e) {
            LOGGER.error("Exception occured while fetching. ", e);
        }
        return null;
    }
}
