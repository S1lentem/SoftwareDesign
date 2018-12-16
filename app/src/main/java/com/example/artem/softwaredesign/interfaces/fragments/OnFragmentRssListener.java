package com.example.artem.softwaredesign.interfaces.fragments;

import com.example.artem.softwaredesign.data.models.RssFeed;

import java.util.List;

public interface OnFragmentRssListener {
    String getNewsResources();
    void redirectedToSettings();
    void saveRssInCache(List<RssFeed> feeds);
}
