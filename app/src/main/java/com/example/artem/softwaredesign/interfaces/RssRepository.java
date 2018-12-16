package com.example.artem.softwaredesign.interfaces;

import com.example.artem.softwaredesign.data.models.RssFeed;

import java.util.List;

public interface RssRepository {
    List<RssFeed> getAllFeedFromCache();
    void addRss(RssFeed rssFeed);
}
