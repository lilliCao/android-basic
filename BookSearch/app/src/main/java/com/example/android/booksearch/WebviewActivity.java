package com.example.android.booksearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Currently not use because page looks very bad in webView
 */

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
        //open page inside of my Webview
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.google.de");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
