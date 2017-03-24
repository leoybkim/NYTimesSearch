package com.leoybkim.nytimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by leo on 20/03/17.
 */

public class Article implements Serializable {

    private String mWebUrl;
    private String mHeadline;
    private String mThumbNail;

    public Article(JSONObject jsonObject) {
        try {
            mWebUrl = jsonObject.getString("web_url");
            mHeadline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if (multimedia.length() > 0) {
                // Return the first image if exists
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                mThumbNail = "http://www.nytimes.com/" + multimediaJson.getString("url");
            } else {
                mThumbNail = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Article> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Article> results = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                results.add(new Article(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public String getHadline() {
        return mHeadline;
    }

    public String getThumbNail() {
        return mThumbNail;
    }

}
