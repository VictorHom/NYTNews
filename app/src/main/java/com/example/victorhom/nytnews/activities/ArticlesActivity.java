package com.example.victorhom.nytnews.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.victorhom.nytnews.R;
import com.example.victorhom.nytnews.abstract_classes.EndlessRecyclerViewScrollListener;
import com.example.victorhom.nytnews.adapters.ArticleAdapter;
import com.example.victorhom.nytnews.fragments.FilterArticle;
import com.example.victorhom.nytnews.models.Article;
import com.example.victorhom.nytnews.network.Checker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// right now, every query is just adding to the list
public class ArticlesActivity extends AppCompatActivity implements FilterArticle.OnDataPass {

    //GridView gvResults;
    ArrayList<Article> articles;
    ArticleAdapter articleAdapter;
    Boolean filterOn = false;
    Menu menu;
    SearchView svSearch;

    // this is for the data that is from the OnDataPass interface
    String date;
    String order;
    ArrayList<String> topics;
    private int pageSave = 0;
    private String querySave;
    private RecyclerView rvArticles;
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        this.menu = menu;
        // no share item on the articles page
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        shareItem.setVisible(false);
        shareItem.setEnabled(false);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        svSearch = searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                makeAPIGetCall(query, 0, true);
                querySave = query;
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        } else if (id == R.id.menu_item_filter) {
            toggleFilter(item);
            return true;
        } else if (id == R.id.menu_item_setting) {
            showEditDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    // toggle the filter on and off
    private void toggleFilter(MenuItem item) {
        filterOn = !filterOn;
        if (filterOn) {
            // turn off then
            item.setIcon(R.drawable.filter_remove_outline);
        } else {
            item.setIcon(R.drawable.filter_outline);
        }
    }
    // open the dialog for settings in the filter
    private void showEditDialog() {
        // date, order, topics
        Bundle bundle = new Bundle();
        // passing this data as a way to show current settings in the dialog
        bundle.putString("date", date);
        bundle.putString("order", order);
        bundle.putStringArrayList("topics", topics);
        FragmentManager fm = getSupportFragmentManager();
        FilterArticle fa = FilterArticle.newInstance("Some Title","hello");
        fa.setArguments(bundle);
        fa.show(fm, "fragment_edit_name");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        setView();
        Toolbar menu = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(menu);

        if (savedInstanceState != null) {
            pageSave = savedInstanceState.getInt("page");
            querySave = savedInstanceState.getString("query");
        }

    }

    public void setView() {
        rvArticles = (RecyclerView) findViewById(R.id.rvArticles);

        //gvResults = (GridView) findViewById(R.id.gvResults);
        articles = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this, articles);
        rvArticles.setAdapter(articleAdapter);
        StaggeredGridLayoutManager gridLayoutManager;
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        } else {
//            gridLayoutManager = new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);
//        }

        rvArticles.setLayoutManager(gridLayoutManager);

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                pageSave = page;
                querySave = svSearch.getQuery().toString();
                makeAPIGetCall(querySave, page, false);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvArticles.addOnScrollListener(scrollListener);

        // Adds the scroll listener to RecyclerView
        rvArticles.addOnScrollListener(scrollListener);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
    }

    private void setSwipeRefresh() {
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(() -> {
            makeAPIGetCall(querySave, pageSave, true);
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.pastelBlue,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    // this method is used for the initial page load of page 0 in onCreateOptionsMenu
    // then additional calls in onCreate
    public void makeAPIGetCall(String query, int page, final boolean clearArticles) {
        OkHttpClient okClient = new OkHttpClient();
        HttpUrl.Builder httpUrl = HttpUrl.parse("https://api.nytimes.com/svc/search/v2/articlesearch.json").newBuilder();
        httpUrl.addQueryParameter("api-key", "bfabf189143c4a728e507120cecf2d01");
        httpUrl.addQueryParameter("page", String.valueOf(page));
        httpUrl.addQueryParameter("q", query);
        appendAdditionalQueryParams(httpUrl);


        String okUrl = httpUrl.build().toString();
        Request request = new Request.Builder()
                .url(okUrl)
                .build();

        apiGetCallHandler(okClient, request, clearArticles);

    }

    private void apiGetCallHandler(OkHttpClient okClient, Request request, boolean clearArticles) {
        final boolean clearArticlesFinal = clearArticles;
        okClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ArticlesActivity.this.runOnUiThread(() -> {
                    // shows when I turn off wifi
                    Toast.makeText(ArticlesActivity.this, "Sorry, we are unable to retrieve articles from NYT at this time", Toast.LENGTH_LONG).show();
                    if (!Checker.isOnline()) {
                        Toast.makeText(ArticlesActivity.this, "Check to make sure you are connected to wifi", Toast.LENGTH_LONG).show();
                    } else if (!Checker.isNetworkConnected(getApplicationContext())) {
                        Toast.makeText(ArticlesActivity.this, "There could be a network problem", Toast.LENGTH_LONG).show();
                    }

                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseData = response.body().string();
                final boolean clear = clearArticlesFinal;

                // Run view-related code back on the main thread
                ArticlesActivity.this.runOnUiThread(() -> {
                    try {

                        JSONObject json = new JSONObject(responseData);
                        JSONArray jsonArticle = json.getJSONObject("response").getJSONArray("docs");
                        if (jsonArticle.length() == 0) {
                            throw new JSONException("no retrievals made");
                        }
                        if (clear) {
                            // 1. First, clear the array of data
                            articles.clear();
                            // 2. Notify the adapter of the update
                            articleAdapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
                            // 3. Reset endless scroll listener when performing a new search
                            scrollListener.resetState();
                        }
                        articles.addAll(Article.fromJSONArray(jsonArticle));
                        articleAdapter.notifyDataSetChanged();
                        // called to stop the reloading animation
                        swipeContainer.setRefreshing(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ArticlesActivity.this, "We are having a tough time retrieving articles or there are no articles from your search", Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

    // if filter is on, will append additional queries to api call
    private void appendAdditionalQueryParams(HttpUrl.Builder httpUrlBuilder) {
        if (filterOn) {
            if (!order.equals("")) {
                httpUrlBuilder.addQueryParameter("sort", order);
            }

            if (!date.equals("")) {
                // begin_date=20160112
                httpUrlBuilder.addQueryParameter("begin_date", date);
            }

            // &fq=news_desk:("Education"%20"Health")
            if (topics.size() > 0) {
                String queryTopics = composeNewsDeskQuery(topics);
                httpUrlBuilder.addQueryParameter("fq", queryTopics);
            }
        }
    }

    //news_desk:("Education"%20"Health")
    private String composeNewsDeskQuery(ArrayList<String> topics) {
        StringBuilder query = new StringBuilder();
        query.append("news_desk:(");
        for (int i = 0; i < topics.size(); i++) {
            query.append("\"");
            query.append(topics.get(i));
            query.append("\"");
            if (i != topics.size() -1) {
                query.append(" ");
            }
        }
        query.append(")");
        return query.toString();
    }

    // the purpose of this interface is to pass data from the fragment back to the articles activity
    @Override
    public void getSetttings(String date, String order, ArrayList<String> topics) {
        this.date = date;
        this.order = order;
        this.topics = topics;
    }

    @Override
    protected void onSaveInstanceState (Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("page", pageSave);
        bundle.putString("query", querySave);
    }

}
