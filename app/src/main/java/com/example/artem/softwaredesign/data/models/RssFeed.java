package com.example.artem.softwaredesign.data.models;

public class RssFeed {
    private String title;
    private String link;
    private String description;

    public RssFeed(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }
}
