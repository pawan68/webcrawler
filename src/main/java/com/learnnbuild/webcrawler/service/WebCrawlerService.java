package com.learnnbuild.webcrawler.service;

import com.learnnbuild.webcrawler.entity.WebCrawlerRequestStatusEntity;
import com.learnnbuild.webcrawler.entity.WebCrawlerResponseEntity;
import com.learnnbuild.webcrawler.kafka.Payload;
import com.learnnbuild.webcrawler.kafka.Producer;
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

import static com.learnnbuild.webcrawler.util.WebCrawlerUtil.*;

@Service
public class WebCrawlerService {

    private HashSet<String> links;

    @Autowired
    private WebCrawlerRequestStatusRepository webCrawlerRequestStatusRepository;

    @Autowired
    private WebCrawlerResponseRepository webCrawlerResponseRepository;

    @Autowired
    private Producer producer;

    @Autowired
    private WebCrawlerUtil webCrawlerUtil;

    @Autowired
    private PersistenceService persistenceService;

    private static final Logger LOGGER = LoggerFactory.getLogger(WebCrawlerService.class);

    public HashSet<String> getLinks() {
        return links;
    }

    public void setLinks(HashSet<String> links) {
        this.links = links;
    }

    public WebCrawlerService() {
        this.links = new HashSet<>();
    }

    public void getWebPagesInfo(String URL, int depth, WebCrawlerResponseEntity webCrawlerResponse) {
        if (!links.contains(URL) && depth != 0 && !StringUtils.isEmpty(URL)) {
            LOGGER.info("Fetching URL: {} depth: {} ", URL, depth);
            try {
                links.add(URL);
                Document document = Jsoup.connect(URL).get();
                Elements linksOnWebPage = getPageLinks(document);
                Elements imageLinks = getImageLinks(document);
                Detail detail = getDetail(imageLinks, document);
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

    public ResponseEntity<Object> process(WebCrawlerSubmitRequest request) {
        Payload payload = null;
        try {
            payload = WebCrawlerUtil.generatePayload(request);
            producer.send(payload);
            webCrawlerUtil.updateWebCrawlerRequestStatus(payload.getUniqueIdentifier(), RequestStatus.SUBMITTED);
            return new ResponseEntity<>(new WebCrawlerSubmitResponse(payload.getUniqueIdentifier()), HttpStatus.OK);

        } catch (Exception e) {
            if (payload != null) {
                webCrawlerUtil.updateWebCrawlerRequestStatus(payload.getUniqueIdentifier(), RequestStatus.FAILED);
            }
        }
        ErrorResponse errorResponse = webCrawlerUtil.getErrorResponse("Error occured while processing the submit request!");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> process(WebCrawlerRequest request) {
        try {
            WebCrawlerRequestStatusEntity webCrawlerRequestStatus = persistenceService.findCrawlerRequestStatusById(request.getUniqueToken());
            if (webCrawlerRequestStatus != null)
                return new ResponseEntity<>(new WebCrawlerRequestStatusResponse(webCrawlerRequestStatus.getStatus()), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception occured while processing status request", e);
        }
        ErrorResponse errorResponse = webCrawlerUtil.getErrorResponse("Response matching with unique token does not exist! ");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> getCrawledResponse(WebCrawlerRequest request) {
        try {
            String uniqueToken = request.getUniqueToken();
            WebCrawlerResponseEntity webCrawlerResponseFetched = persistenceService.findCrawlerResponseById(uniqueToken);
            WebCrawlerResponse response = null;
            if (webCrawlerResponseFetched != null) {
                response = new WebCrawlerResponse(webCrawlerResponseFetched.getTotalLinks(), webCrawlerResponseFetched.getTotalImages(), webCrawlerResponseFetched.getDetails());
            }
            if (response != null)
                return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception occured while fetching crawled response ", e);
        }
        ErrorResponse errorResponse = webCrawlerUtil.getErrorResponse("Response matching with given uniqueToken does not exist!");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    public void processMessage(Payload payload) {
        try {
            WebCrawlerResponseEntity responseEntity = new WebCrawlerResponseEntity();
            webCrawlerUtil.updateWebCrawlerRequestStatus(payload.getUniqueIdentifier(), RequestStatus.IN_PROCESS);
            responseEntity.setUniqueToken(payload.getUniqueIdentifier());
            getWebPagesInfo(payload.getURL(), payload.getDepth(), responseEntity);
            responseEntity.setTotalLinks(responseEntity.getDetails().size());
            setLinks(new HashSet<>());
            if (!responseEntity.isEmpty()) {
                persistenceService.saveCrawlerResponse(responseEntity);
            }
            webCrawlerUtil.updateWebCrawlerRequestStatus(payload.getUniqueIdentifier(), RequestStatus.PROCESSED);
        } catch (Exception e) {
            LOGGER.error("Error occured while reading the kafka message. ", e);
        }
    }
}
