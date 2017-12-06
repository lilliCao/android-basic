package com.example.android.booksearch;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by tali on 05.12.17.
 */

public class BookLoader extends AsyncTaskLoader<ArrayList<Book>> {
    private String url;

    public BookLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        if (url == null) {
            return null;
        }
        ArrayList<Book> list = QueryUtils.fetchBookData(url);
        return list;
    }
}
