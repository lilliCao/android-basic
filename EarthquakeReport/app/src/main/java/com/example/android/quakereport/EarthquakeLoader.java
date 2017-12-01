package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by tali on 01.12.17.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private String url;
    private static final String LOG_TAG = EarthquakeLoader.class.getName();


    public EarthquakeLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if (url == null) {
            return null;
        }
        List<Earthquake> list = QueryUtils.fetchEarthquakeData(url);
        //For default sorting by strength
        Collections.sort(list, new Comparator<Earthquake>() {
            @Override
            public int compare(Earthquake earthquake, Earthquake t1) {
                double a = earthquake.getStrength();
                double b = t1.getStrength();
                return a > b ? -1 : (a < b ? 1 : 0);
            }
        });
        return list;
    }
}
