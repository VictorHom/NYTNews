package com.example.victorhom.nytnews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.victorhom.nytnews.R;
import com.example.victorhom.nytnews.models.Article;

public class SingleArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_article);
        Toolbar menu = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(menu);
        // need to add a share ability here
        // hide the search ability here
        // add a back button as well

        Intent i = getIntent();
        Article article = i.getParcelableExtra("articleObject");
        final String url = article.getWebURL();

        WebView wv = (WebView) findViewById(R.id.wvArticle);
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return super.shouldOverrideUrlLoading(view, request);
                view.loadUrl(url);
                return true;
            }
        });
        wv.loadUrl(url);
    }
}
