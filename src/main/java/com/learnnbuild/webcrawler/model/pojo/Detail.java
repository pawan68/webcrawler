package com.learnnbuild.webcrawler.model.pojo;

public class Detail {
    private String pageTitle;
    private String pageLink;
    private Integer imageCount;

    public Detail() {
        this.imageCount = 0;
    }

    public Detail(String pageTitle, String pageLink, Integer imageCount) {
        this.pageTitle = pageTitle;
        this.pageLink = pageLink;
        this.imageCount = imageCount;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageLink() {
        return pageLink;
    }

    public void setPageLink(String pageLink) {
        this.pageLink = pageLink;
    }

    public Integer getImageCount() {
        return imageCount;
    }

    public void setImageCount(Integer imageCount) {
        this.imageCount = imageCount;
    }
}
