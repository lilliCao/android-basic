/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>>{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String URL_GET=
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    private EarthquakeAdapter adapter;
    private TextView emptyView;
    private ProgressBar loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        // Find a reference to the {@link ListView} in the layout
        final ListView earthquakeListView = (ListView) findViewById(R.id.list);
        // Create a new {@link ArrayAdapter} of earthquakes
        emptyView= (TextView) findViewById(R.id.empty_view);
        loading= (ProgressBar) findViewById(R.id.loading);
        earthquakeListView.setEmptyView(emptyView);
        adapter=new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Earthquake current =adapter.getItem(i);
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(current.getUrl()));
                startActivity(intent);
            }
        });
        //background task
        //EarthquakeAsyntask task=new EarthquakeAsyntask();
        //task.execute(URL_GET);

        //check network infor
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infor=connectivityManager.getActiveNetworkInfo();
        if(infor !=null && infor.isConnected()){
            getLoaderManager().initLoader(0,null,this);
        }else{
            loading.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet);
        }

        //Create toolbar set up


    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        return new EarthquakeLoader(this,URL_GET);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        loading.setVisibility(View.GONE);
        if(earthquakes==null || earthquakes.isEmpty()){
            emptyView.setText(R.string.no_earthquakes);
            return;
        }
        adapter.clear();
        adapter.addAll(earthquakes);
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        adapter.clear();
    }
    /*
    private class EarthquakeAsyntask extends AsyncTask<String,Void,List<Earthquake>>{

        @Override
        protected List<Earthquake> doInBackground(String... strings) {
            if(strings.length >0){
                return QueryUtils.fetchEarthquakeData(strings[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Earthquake> earthquakes) {
            if(earthquakes==null || earthquakes.size()>0){
                updateUI(earthquakes);
            }else {
                return;
            }
        }
    }
    */
}
