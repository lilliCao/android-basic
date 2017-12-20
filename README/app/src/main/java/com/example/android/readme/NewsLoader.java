package com.example.android.readme;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by tali on 19.12.17.
 */

public class NewsLoader extends AsyncTaskLoader<ArrayList<News>> {
    private String url;

    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<News> loadInBackground() {
        if (url == null || url.isEmpty()) {
            return null;
        }
        return QueryUtils.fetchData(url);
    }
}
