package com.example.artem.softwaredesign.fragments.rss;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.artem.softwaredesign.R;
import com.example.artem.softwaredesign.data.models.User;
import com.example.artem.softwaredesign.interfaces.fragments.OnFragmentNewSourceListener;
import com.example.artem.softwaredesign.support.TextManager;
import com.google.android.material.textfield.TextInputLayout;

public class RssResourceFragment extends Fragment {

    private OnFragmentNewSourceListener onFragmentNewSourceListener;

    private EditText newsSourceTextView;
    private EditText countNewsForCacheEditText;

    private TextInputLayout newsSourceTextInputLayout;
    private TextInputLayout countNewsForCacheTextInputLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_source, container, false);

        newsSourceTextView = view.findViewById(R.id.first_news_source);
        countNewsForCacheEditText = view.findViewById(R.id.count_rss_in_cache_edit_text);

        newsSourceTextInputLayout = view.findViewById(R.id.first_news_source_text_input_layout);
        countNewsForCacheTextInputLayout = view.findViewById(R.id.count_rss_in_cache_text_input_layout);

        User user = onFragmentNewSourceListener.getUser();
        String resource = user.getNewsSource();
        int countResources = user.getCountRssForCached();
        newsSourceTextView.setText(resource != null ? resource : "");
        countNewsForCacheEditText.setText(String.valueOf(countResources));
        view.findViewById(R.id.save_news_resource_button).setOnClickListener(v ->{
            if (isValidSource()){
                onFragmentNewSourceListener.saveNewsResources(newsSourceTextView.getText().toString(),
                        Integer.valueOf(countNewsForCacheEditText.getText().toString()));
            }
            if (getArguments() != null && getArguments().getBoolean("first")){
                onFragmentNewSourceListener.goToRssFeeds();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentNewSourceListener) {
            onFragmentNewSourceListener = (OnFragmentNewSourceListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentNewSourceListener");
        }
    }

    private boolean isValidSource(){
        String resourceNews = newsSourceTextView.getText().toString();
        String countInCache = countNewsForCacheEditText.getText().toString();
        if (TextManager.isEmpty(resourceNews, countInCache)){
            Toast toast = Toast.makeText((Context) onFragmentNewSourceListener,
                    getResources().getString(R.string.message_for_empty_fields), Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
        return true;
    }
}
