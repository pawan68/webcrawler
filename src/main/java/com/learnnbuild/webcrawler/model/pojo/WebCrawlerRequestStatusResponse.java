package com.learnnbuild.webcrawler.model.pojo;

public class WebCrawlerRequestStatusResponse {
    private RequestStatus requestStatus;

    public WebCrawlerRequestStatusResponse() {

    }

    public WebCrawlerRequestStatusResponse(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}
