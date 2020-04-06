package com.learnnbuild.webcrawler.entity;

import com.learnnbuild.webcrawler.model.pojo.RequestStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("WebCrawlerRequestStatusEntity")
public class WebCrawlerRequestStatusEntity  implements Serializable {

    @Id
    private String uniqueToken;
    private RequestStatus status;

    public WebCrawlerRequestStatusEntity() {
    }

    public WebCrawlerRequestStatusEntity(RequestStatus status, String uniqueToken) {
        this.status = status;
        this.uniqueToken = uniqueToken;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getUniqueToken() {
        return uniqueToken;
    }

    public void setUniqueToken(String uniqueToken) {
        this.uniqueToken = uniqueToken;
    }
}
