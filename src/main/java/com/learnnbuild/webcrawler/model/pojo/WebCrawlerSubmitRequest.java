package com.learnnbuild.webcrawler.model.pojo;

public class WebCrawlerSubmitRequest {
    private String URL;
    private Integer depth;

    public WebCrawlerSubmitRequest(String URL, Integer depth) {
        this.URL = URL;
        this.depth = depth;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }
}
