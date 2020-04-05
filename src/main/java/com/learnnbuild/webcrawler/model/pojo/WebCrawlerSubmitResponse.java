package com.learnnbuild.webcrawler.model.pojo;

public class WebCrawlerSubmitResponse {

    private String acknowledgementToken;

    public String getAcknowledgementToken() {
        return acknowledgementToken;
    }

    public void setAcknowledgementToken(String acknowledgementToken) {
        this.acknowledgementToken = acknowledgementToken;
    }

    public WebCrawlerSubmitResponse(String acknowledgementToken) {
        this.acknowledgementToken = acknowledgementToken;
    }
}
