package com.learnnbuild.webcrawler.service;

import com.learnnbuild.webcrawler.Producer;
import com.learnnbuild.webcrawler.entity.WebCrawlerRequestStatusEntity;
import com.learnnbuild.webcrawler.entity.WebCrawlerResponseEntity;
import com.learnnbuild.webcrawler.kafka.Payload;
import com.learnnbuild.webcrawler.model.pojo.*;
import com.learnnbuild.webcrawler.repository.WebCrawlerRequestStatusRepository;
import com.learnnbuild.webcrawler.repository.WebCrawlerResponseRepository;
import com.learnnbuild.webcrawler.util.WebCrawlerUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;

import static com.learnnbuild.webcrawler.util.WebCrawlerUtil.*;

@Service
public class WebCrawlerService {

    private HashSet<String> links;

    public HashSet<String> getLinks() {
        return links;
    }

    public void setLinks(HashSet<String> links) {
        this.links = links;
    }

    @Autowired
    private WebCrawlerRequestStatusRepository webCrawlerRequestStatusRepository;

    @Autowired
    private WebCrawlerResponseRepository webCrawlerResponseRepository;

    @Autowired
    private Producer producer;

    private static final Logger LOGGER = LoggerFactory.getLogger(WebCrawlerService.class);

    public WebCrawlerService() {
        this.links = new HashSet<>();
    }

    public void getWebPagesInfo(String URL, int depth, WebCrawlerResponseEntity webCrawlerResponse) {
        if(!links.contains(URL) && depth != 0 && !StringUtils.isEmpty(URL)) {
            System.out.println("Fetching URL: " + URL + " depth: " + depth);
            try {
                links.add(URL);
                Document document = Jsoup.connect(URL).get();
                Elements linksOnWebPage = getPageLinks(document);
                Elements imageLinks = getImageLinks(document);
                Detail detail = getDetail(linksOnWebPage, imageLinks, document);
                webCrawlerResponse.getDetails().add(detail);
                webCrawlerResponse.setTotalImages(webCrawlerResponse.getTotalImages() + imageLinks.size());
                webCrawlerResponse.setTotalLinks(webCrawlerResponse.getTotalLinks() + linksOnWebPage.size());
                depth--;
                for (Element page : linksOnWebPage) {
                    getWebPagesInfo(page.attr("abs:href"), depth, webCrawlerResponse);
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }

    public ResponseEntity<WebCrawlerSubmitResponse> process(WebCrawlerSubmitRequest request) {
        Payload payload = null;
        try {
            payload = WebCrawlerUtil.generatePayload(request);
            producer.send(payload);
            updateWebCrawlerRequestStatus(payload.getUniqueIdentifier(), RequestStatus.SUBMITTED);
            return new ResponseEntity<>(new WebCrawlerSubmitResponse(payload.getUniqueIdentifier()), HttpStatus.OK);

        } catch (Exception e) {
            if(payload != null) {
                updateWebCrawlerRequestStatus(payload.getUniqueIdentifier(), RequestStatus.FAILED);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<WebCrawlerRequestStatusResponse> process(WebCrawlerRequest request) {
        try {
            WebCrawlerRequestStatusEntity webCrawlerRequestStatus = getWebCrawlerRequestStatus(request.getUniqueToken());
            if(webCrawlerRequestStatus != null)
                return new ResponseEntity<>(new WebCrawlerRequestStatusResponse(webCrawlerRequestStatus.getStatus()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception occured while processing status request", e);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private WebCrawlerRequestStatusEntity getWebCrawlerRequestStatus(String uniqueToken) {
        try {
            Optional<WebCrawlerRequestStatusEntity> statusRequest = webCrawlerRequestStatusRepository.findById(uniqueToken);
            return statusRequest.orElse(null);
        } catch (Exception e) {
            LOGGER.error("Exception occured while fetching status from redis ", e);
        }
        return null;
    }

    public void updateWebCrawlerRequestStatus(String uniqueToken, RequestStatus requestStatus) {
        if(StringUtils.isEmpty(uniqueToken))
            return;
        try {
            Optional<WebCrawlerRequestStatusEntity> statusRequest = webCrawlerRequestStatusRepository.findById(uniqueToken);
            if (statusRequest.isPresent()) {
                WebCrawlerRequestStatusEntity webCrawlerRequestStatusEntity = statusRequest.get();
                webCrawlerRequestStatusEntity.setStatus(requestStatus);
                webCrawlerRequestStatusEntity.setUniqueToken(uniqueToken);
                webCrawlerRequestStatusRepository.save(webCrawlerRequestStatusEntity);
            } else {
                WebCrawlerRequestStatusEntity webCrawlerRequestStatusEntity = new WebCrawlerRequestStatusEntity(requestStatus,
                        uniqueToken);
                webCrawlerRequestStatusRepository.save(webCrawlerRequestStatusEntity);
            }
        } catch (Exception e) {
            LOGGER.error("Error occured while updating record in redis ", e);
        }
    }

    public ResponseEntity<WebCrawlerResponse> getCrawledResponse(WebCrawlerRequest request) {
        try {
            String uniqueToken = request.getUniqueToken();
            Optional<WebCrawlerResponseEntity> webCrawlerResponseFetched = webCrawlerResponseRepository.findById(uniqueToken);
            WebCrawlerResponse response = null;
            if (webCrawlerResponseFetched.isPresent()) {
                WebCrawlerResponseEntity webCrawlerResponseEntity = webCrawlerResponseFetched.get();
                response = new WebCrawlerResponse(webCrawlerResponseEntity.getTotalLinks(), webCrawlerResponseEntity.getTotalImages(), webCrawlerResponseEntity.getDetails());
            }
            if (response != null)
                return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception occured while fetching crawled response ", e);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
