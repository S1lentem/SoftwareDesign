package com.example.artem.softwaredesign.data.storages.SQLite.rss;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.artem.softwaredesign.data.models.RssFeed;
import com.example.artem.softwaredesign.interfaces.RssRepository;

import java.util.ArrayList;
import java.util.List;

public class RssSQLiteRepository implements RssRepository {
    private final RssDbHelper rssDbHelper;

    public RssSQLiteRepository(Context context){
        rssDbHelper = new RssDbHelper(context);
    }

    public List<RssFeed> getAllFeedFromCache(){
        ArrayList<RssFeed> allRssFeed = new ArrayList<>();

        try (SQLiteDatabase db = rssDbHelper.getReadableDatabase()) {
            String request = "SELECT * FROM " + rssDbHelper.getTableName() + ";";
            Cursor cursor = db.rawQuery(request, null);
            while (cursor.moveToNext()){
                int id = cursor.getInt(RssColumnsTable.id.ordinal());
                String title = cursor.getString(RssColumnsTable.title.ordinal());
                String description = cursor.getString(RssColumnsTable.description.ordinal());
                String link = cursor.getString(RssColumnsTable.link.ordinal());
                String date = cursor.getString(RssColumnsTable.date.ordinal());
                String url = cursor.getString(RssColumnsTable.url.ordinal());
                allRssFeed.add(new RssFeed(title, link, description, date, url));
            }
        }
        return allRssFeed;
    }


    public void addRss(RssFeed rssFeed){
        try (SQLiteDatabase db = rssDbHelper.getReadableDatabase()){
            ContentValues contentValues = new ContentValues();
            contentValues.put(RssColumnsTable.title.toString(), rssFeed.getTitle());
            contentValues.put(RssColumnsTable.description.toString(), rssFeed.getDescription());
            contentValues.put(RssColumnsTable.link.toString(), rssFeed.getLink());
            contentValues.put(RssColumnsTable.date.toString(), rssFeed.getDate());
            contentValues.put(RssColumnsTable.url.toString(), rssFeed.getUrl());

            db.insert(rssDbHelper.getTableName(), null, contentValues);
        }
    }

    @Override
    public void clearCache() {
        try (SQLiteDatabase db = rssDbHelper.getReadableDatabase()){
            db.delete(rssDbHelper.getTableName(), null, null);
        }
    }
}
