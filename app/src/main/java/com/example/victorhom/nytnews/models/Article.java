package com.example.victorhom.nytnews.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by victorhom on 10/21/16.
 */
public class Article implements Parcelable {
    String webURL;

    public String getWebURL() {
        return webURL;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    String headline;
    String thumbnail;

    public Article(JSONObject articleJSONObject) {
        try {
            this.webURL = articleJSONObject.getString("web_url");
            headline = articleJSONObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = articleJSONObject.getJSONArray("multimedia");
            if (multimedia.length() > 0) {
                JSONObject multimediaObject = multimedia.getJSONObject(0);
                this.thumbnail = "https://www.nytimes.com/" + multimediaObject.getString("url");
            } else {
                this.thumbnail = "";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory to convert a list of articles
    public static ArrayList<Article> fromJSONArray(JSONArray articles) {
        ArrayList<Article> results = new ArrayList<>();

        for (int i = 0; i < articles.length(); i++) {
            try {
                results.add(new Article(articles.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.webURL);
        dest.writeString(this.headline);
        dest.writeString(this.thumbnail);
    }

    protected Article(Parcel in) {
        this.webURL = in.readString();
        this.headline = in.readString();
        this.thumbnail = in.readString();
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
