package com.example.victorhom.nytnews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.victorhom.nytnews.R;
import com.example.victorhom.nytnews.models.Article;

public class SingleArticleActivity extends AppCompatActivity {
    private Menu menu;
    private Article article;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        this.menu = menu;
        // no share item on the articles page
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        shareItem.setVisible(true);
        shareItem.setEnabled(true);

        // hide & disable the search, filter, and options
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem filterItem = menu.findItem(R.id.menu_item_filter);
        MenuItem settingsItem = menu.findItem(R.id.menu_item_setting);
        searchItem.setVisible(false);
        searchItem.setEnabled(false);
        filterItem.setVisible(false);
        filterItem.setEnabled(false);
        settingsItem.setVisible(false);
        settingsItem.setEnabled(false);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_share) {
            shareLink();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void shareLink() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, article.getWebURL());
        startActivity(Intent.createChooser(shareIntent, "Share link using"));
        finish();
    }

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
        article = i.getParcelableExtra("articleObject");
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
