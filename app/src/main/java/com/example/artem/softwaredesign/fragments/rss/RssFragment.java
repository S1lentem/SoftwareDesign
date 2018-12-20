package com.example.artem.softwaredesign.fragments.rss;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.data.HtmlConverter;
import com.example.artem.softwaredesign.data.RssFeedListAdapter;
import com.example.artem.softwaredesign.data.models.RssFeed;
import com.example.artem.softwaredesign.interfaces.fragments.OnFragmentRssListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class RssFragment extends Fragment {
    private Context context;
    private OnFragmentRssListener onFragmentRssListener;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;

    private List<RssFeed> mFeedModelList;

    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        private String urlLink;

        @Override
        protected void onPreExecute() {
            mSwipeLayout.setRefreshing(true);
            urlLink = onFragmentRssListener.getNewsResources();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                mFeedModelList = parseFeed(inputStream);
                onFragmentRssListener.saveRssInCache(mFeedModelList);
                return true;
            } catch (IOException | XmlPullParserException e) {
                Log.e("Error", e.getMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mSwipeLayout.setRefreshing(false);

            if (success) {
                mRecyclerView.setAdapter(new RssFeedListAdapter(mFeedModelList, context));
            } else {
                Toast.makeText(context, "Enter a valid Rss feed url", Toast.LENGTH_LONG).show();
            }
        }
    }

    private List<RssFeed> parseFeed(InputStream inputStream) throws XmlPullParserException,
            IOException {
        String title = null;
        String link = null;
        String description = null;
        String date = null;
        String url = null;
        boolean isItem = false;
        List<RssFeed> items = new ArrayList<RssFeed>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if(name == null)
                    continue;

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MyXmlParser", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result;
                }
                else if (name.equalsIgnoreCase("pubDate")){
                    date = result;
                }
                else if (name.equalsIgnoreCase("enclosure")){
                    url = xmlPullParser.getAttributeValue("", "url");
                }

                if (title != null && link != null && description != null && date != null) {
                    if(isItem) {
                        HtmlConverter parser = new HtmlConverter(description);
                        RssFeed item = new RssFeed(title, link, parser.getText(), date, url);
                        items.add(item);
                    }
                    title = null;
                    link = null;
                    description = null;
                    date = null;
                    url = null;
                    isItem = false;
                }
            }
            return items;
        } finally {
            inputStream.close();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String newsResources = onFragmentRssListener.getNewsResources();
        if (newsResources == null || newsResources.equals("")){
            onFragmentRssListener.redirectedToSettings();
        }

        View view = inflater.inflate(R.layout.fragment_rss, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mSwipeLayout = view.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        String message;
        if (isOnline()) {
            message = getResources().getString(R.string.online_message);
            mSwipeLayout.setOnRefreshListener(() -> new FetchFeedTask().execute((Void) null));
            new FetchFeedTask().execute((Void) null);
        } else {
            message = getResources().getString(R.string.offline_message);
            mSwipeLayout.setOnRefreshListener(()->{});
            mRecyclerView.setAdapter(
                    new RssFeedListAdapter(onFragmentRssListener.getRssFromCache(), context));
        }
        Toast toast = Toast.makeText((Context) onFragmentRssListener, message,Toast.LENGTH_LONG);

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof  OnFragmentRssListener){
            onFragmentRssListener = (OnFragmentRssListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentRssListener");
        }
    }

    private Boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }
}
