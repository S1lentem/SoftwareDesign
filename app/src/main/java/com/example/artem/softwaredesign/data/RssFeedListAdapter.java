package com.example.artem.softwaredesign.data;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.activities.WebActivity;
import com.example.artem.softwaredesign.data.models.RssFeed;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RssFeedListAdapter
        extends RecyclerView.Adapter<RssFeedListAdapter.FeedModelViewHolder> {

    private final Context context;
    private List<RssFeed> mRssFeedModels;


    public static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private View rssFeedView;

        public FeedModelViewHolder(View v) {
            super(v);
            rssFeedView = v;

        }
    }

    public RssFeedListAdapter(List<RssFeed> rssFeedModels, Context context) {
        mRssFeedModels = rssFeedModels;
        this.context = context;
    }

    @Override
    public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rss_feed, parent, false);
        FeedModelViewHolder holder = new FeedModelViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(FeedModelViewHolder holder, int position) {
        final RssFeed rssFeedModel = mRssFeedModels.get(position);
        ((TextView) holder.rssFeedView.findViewById(R.id.titleText)).setText(rssFeedModel.getTitle());
        ((TextView) holder.rssFeedView.findViewById(R.id.descriptionText)).setText(rssFeedModel.getDescription());
        ((TextView) holder.rssFeedView.findViewById(R.id.dateText)).setText(rssFeedModel.getDate());

        if (rssFeedModel.getUrl() != null && !rssFeedModel.getUrl().equals("")){
            Picasso.with(context).load(rssFeedModel.getUrl())
                    .into((ImageView) holder.rssFeedView.findViewById(R.id.imageFeed));

        }

        holder.rssFeedView.setOnClickListener(v -> {
            if (isOnline()) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.setData(Uri.parse(rssFeedModel.getLink()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }

    private Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}