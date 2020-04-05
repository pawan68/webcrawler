package com.learnnbuild.webcrawler.kafka;

public class Payload {

    private String URL;
    private Integer depth;
    private String uniqueIdentifier;

    public Payload() {
    }

    public Payload(String URL, Integer depth, String uniqueIdentifier) {
        this.URL = URL;
        this.depth = depth;
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
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

    @Override
    public String toString() {
        return "Payload{" +
                "URL='" + URL + '\'' +
                ", depth=" + depth +
                ", uniqueIdentifier='" + uniqueIdentifier + '\'' +
                '}';
    }
}