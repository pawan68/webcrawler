package com.learnnbuild.webcrawler.model.pojo;

import org.springframework.util.StringUtils;

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
