package com.example.artem.softwaredesign.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.artem.softwaredesign.R;

public class WebActivity extends AppCompatActivity {


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        WebView webView = findViewById(R.id.web_view);

        Uri uri = getIntent().getData();
        webView.setWebViewClient(new Callback());
        webView.loadUrl(uri.toString());
    }

    private class Callback extends WebViewClient{

        @Override
        public  boolean shouldOverrideUrlLoading(WebView view, String url){
            return false;
        }
    }
}
