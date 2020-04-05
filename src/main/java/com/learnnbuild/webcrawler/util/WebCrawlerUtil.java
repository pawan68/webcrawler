package com.learnnbuild.webcrawler.util;

import com.learnnbuild.webcrawler.entity.WebCrawlerRequestStatusEntity;
import com.learnnbuild.webcrawler.kafka.Payload;
import com.learnnbuild.webcrawler.model.pojo.Detail;
import com.learnnbuild.webcrawler.model.pojo.RequestStatus;
import com.learnnbuild.webcrawler.model.pojo.WebCrawlerRequestStatus;
import com.learnnbuild.webcrawler.model.pojo.WebCrawlerSubmitRequest;
import com.learnnbuild.webcrawler.repository.WebCrawlerRequestStatusRepository;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Optional;
import java.util.UUID;

@Component
public class WebCrawlerUtil {

    public static Elements getPageLinks(Document document) {
        return document.select("a[href]");
    }

    public static Elements getImageLinks(Document document) {
        return document.select("img");
    }

    @Autowired
    private WebCrawlerRequestStatusRepository webCrawlerRequestStatusRepository;

    public static Detail getDetail(Elements linksOnWebPage, Elements imageLinks, Document document) {
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
}
