package com.example.artem.softwaredesign.data.models;

public class RssFeed {
    private String title;
    private String link;
    private String description;
    private String date;
    private String url;

    public RssFeed(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public RssFeed(String title, String link, String description, String date){
        this(title, link, description);
        this.date = date;
    }

    public RssFeed(String title, String link, String description, String date, String url){
        this(title, link, description, date);
        this.url = url;
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

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}
