package com.learnnbuild.webcrawler.entity;

import com.learnnbuild.webcrawler.model.pojo.Detail;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RedisHash("WebCrawlerResponseEntity")
public class WebCrawlerResponseEntity implements Serializable {
    @Id
    private String uniqueToken;
    private Integer totalLinks;
    private Integer totalImages;

    public WebCrawlerResponseEntity() {
        this.details = new ArrayList<>();
        this.totalImages = 0;
        this.totalLinks = 0;
    }

    private List<Detail> details;

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
        if(details == null)
            return new ArrayList<>();
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public String getUniqueToken() {
        return uniqueToken;
    }

    public void setUniqueToken(String uniqueToken) {
        this.uniqueToken = uniqueToken;
    }

    public boolean isEmpty() {
        return this.details.isEmpty() && this.totalImages == 0 && this.totalLinks == 0;
    }
}
