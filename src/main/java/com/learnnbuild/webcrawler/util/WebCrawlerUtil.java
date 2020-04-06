package com.learnnbuild.webcrawler.util;

import com.learnnbuild.webcrawler.entity.WebCrawlerRequestStatusEntity;
import com.learnnbuild.webcrawler.kafka.Payload;
import com.learnnbuild.webcrawler.model.pojo.Detail;
import com.learnnbuild.webcrawler.model.pojo.ErrorResponse;
import com.learnnbuild.webcrawler.model.pojo.RequestStatus;
import com.learnnbuild.webcrawler.model.pojo.WebCrawlerSubmitRequest;
import com.learnnbuild.webcrawler.service.PersistenceService;
import com.learnnbuild.webcrawler.service.WebCrawlerService;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Component
public class WebCrawlerUtil {

    public static Elements getPageLinks(Document document) {
        return document.select("a[href]");
    }

    public static Elements getImageLinks(Document document) {
        return document.select("img");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(WebCrawlerService.class);

    @Autowired
    private PersistenceService persistenceService;

    public static Detail getDetail(Elements imageLinks, Document document) {
        Detail detail = new Detail();
        if(!CollectionUtils.isEmpty(imageLinks))
            detail.setImageCount(imageLinks.size());
        if(document != null) {
            detail.setPageLink(document.location());
            detail.setPageTitle(document.title());
        }
        return detail;
    }

    public static Payload generatePayload(WebCrawlerSubmitRequest request) {
        UUID uuid = UUID.randomUUID();
        return new Payload(request.getURL(), request.getDepth(), uuid.toString());
    }

    public void updateWebCrawlerRequestStatus(String uniqueToken, RequestStatus requestStatus) {
        if(StringUtils.isEmpty(uniqueToken))
            return;
        try {
            WebCrawlerRequestStatusEntity statusRequest = persistenceService.findCrawlerRequestStatusById(uniqueToken);
            if (statusRequest != null) {
                statusRequest.setStatus(requestStatus);
                statusRequest.setUniqueToken(uniqueToken);
                persistenceService.saveRequestStatusResponse(statusRequest);
            } else {
                WebCrawlerRequestStatusEntity webCrawlerRequestStatusEntity = new WebCrawlerRequestStatusEntity(requestStatus,
                        uniqueToken);
                persistenceService.saveRequestStatusResponse(webCrawlerRequestStatusEntity);
            }
        } catch (Exception e) {
            LOGGER.error("Error occured while updating record in redis ", e);
        }
    }

    public ErrorResponse getErrorResponse(String errorMessage) {
        return new ErrorResponse(errorMessage);
    }
}
