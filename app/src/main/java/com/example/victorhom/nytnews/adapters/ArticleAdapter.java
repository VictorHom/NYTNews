package com.example.victorhom.nytnews.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.victorhom.nytnews.R;
import com.example.victorhom.nytnews.activities.SingleArticleActivity;
import com.example.victorhom.nytnews.models.Article;

import java.util.List;

/**
 * Created by victorhom on 10/21/16.
 */

// NOTE: contrived but for the purpose of practice - long titles will use a different layout
// "Short Answers to Hard Questions About Postpartum Depression"
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>{

    private static List<Article> mArticles;
    private static Context mContext;
    private final int SHORTTEXT = 0, LONGTEXT = 1;
    private final int LONGTITLE = "NOTE: contrived but for the purpose of practice".length();

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvArticleTitle;
        public ImageView ivArticleThumbnail;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvArticleTitle = (TextView) itemView.findViewById(R.id.tvArticleTitle);
            ivArticleThumbnail = (ImageView) itemView.findViewById(R.id.ivArticle);
            itemView.setOnClickListener(this);
        }

        // Handles the row being being clicked
        // with the recycler view, moved onclick logic into the viewholder
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                Article article = mArticles.get(position);
                // We can access the data within the views
                Intent i = new Intent(mContext, SingleArticleActivity.class);
                i.putExtra("articleObject", article);
                mContext.startActivity(i);
            }
        }

    }

    // Clean all elements of the recycler
    public void clear() {
        mArticles.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Article> list) {
        mArticles.addAll(list);
        notifyDataSetChanged();
    }

    //pass in the articles array into the constructor
    public ArticleAdapter(Context context, List<Article> articles) {
        //super(context, android.R.layout.simple_list_item_1, articles);
        mContext = context;
        mArticles = articles;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Decide type base on the length of the headline
    @Override
    public int getItemViewType(int position) {
        //More to come
        if (mArticles.get(position).getHeadline().length() > LONGTITLE) {
            return LONGTEXT;
        } else {
            return SHORTTEXT;
        }
    }

    // every adapter has 3 important methods;
    // onCreateViewHolder to inflate the view and create the holder
    // onBindViewHolder to set the view attributes based on the data
    // getItemCount to determine the number of items
    // need all 3 to finish the adapter
    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder viewHolder;
        switch (viewType) {
            case SHORTTEXT:
                View v1 = inflater.inflate(R.layout.item_article_result, parent, false);
                viewHolder = new ViewHolder(v1);
                break;
            case LONGTEXT:
                View v2 = inflater.inflate(R.layout.item_article_result_only_text, parent, false);
                viewHolder = new ViewHolder(v2);
                break;
            default:
                View v = inflater.inflate(R.layout.item_article_result_only_text, parent, false);
                viewHolder = new ViewHolder(v);
                break;
        }

        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = mArticles.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.tvArticleTitle;
        textView.setText(article.getHeadline());
        ImageView imageView = viewHolder.ivArticleThumbnail;
        String thumbnail = article.getThumbnail();
        if (!TextUtils.isEmpty(thumbnail)) {
            Glide.with(getContext()).load(thumbnail)
                    .placeholder(R.drawable.cube_send)
                    .error(R.drawable.close_octagon_outline).into(imageView);
        }

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mArticles.size();
    }



}
