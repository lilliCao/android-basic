package com.example.android.booksearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        WebView webView = findViewById(R.id.webview);
        Intent myIntent = getIntent();
        String url = myIntent.getStringExtra("url");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //Load page with completely zoom out
        webSettings.setLoadWithOverviewMode(true);
        //Make it like a normal viewport as browser
        webSettings.setUseWideViewPort(true);
        //open page inside of my Webview
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
