package com.learnnbuild.webcrawler.model.pojo;

public class WebCrawlerRequest {
    private String uniqueToken;

    public WebCrawlerRequest() {
    }

    public WebCrawlerRequest(String uniqueToken) {
        this.uniqueToken = uniqueToken;
    }

    public String getUniqueToken() {
        return uniqueToken;
    }

    public void setUniqueToken(String uniqueToken) {
        this.uniqueToken = uniqueToken;
    }
}
