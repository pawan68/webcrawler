package com.learnnbuild.webcrawler.model.pojo;

import java.util.ArrayList;
import java.util.List;

public class WebCrawlerResponse {
    private Integer totalLinks;
    private Integer totalImages;
    private List<Detail> details;

    public WebCrawlerResponse() {
        this.details = new ArrayList<>();
        this.totalImages = 0;
        this.totalLinks = 0;
    }

    public WebCrawlerResponse(Integer totalLinks, Integer totalImages, List<Detail> details) {
        this.totalLinks = totalLinks;
        this.totalImages = totalImages;
        this.details = details;
    }

    public Integer getTotalLinks() {
        return totalLinks;
    }

    public void setTotalLinks(Integer totalLinks) {
        this.totalLinks = totalLinks;
    }

    public Integer getTotalImages() {
        return totalImages;
    }

    public void setTotalImages(Integer totalImages) {
        this.totalImages = totalImages;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }
}
