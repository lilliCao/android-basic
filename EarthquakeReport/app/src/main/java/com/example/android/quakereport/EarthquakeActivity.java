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
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.view.View.GONE;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String URL_GET =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    private static final int LOADER_ID = 1;
    private static final int SORT_BY_DATE = 0;
    private static final int SORT_BY_STRENGTH = 1;
    private static final String EARTHQUAKE_LIST ="earthquake_list" ;
    private static EarthquakeAdapter adapter;
    private TextView emptyView;
    private ImageView emptyImage;
    private ProgressBar loading;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EARTHQUAKE_LIST, (ArrayList<? extends Parcelable>) getListFromAdapter());
    }

    private List<Earthquake> getListFromAdapter(){
        List<Earthquake> list = new ArrayList<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            list.add(adapter.getItem(i));
        }
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        // Find a reference to the {@link ListView} in the layout
        final ListView earthquakeListView = (ListView) findViewById(R.id.list);
        // Create a new {@link ArrayAdapter} of earthquakes
        emptyView = (TextView) findViewById(R.id.text);
        emptyImage = (ImageView) findViewById(R.id.image);
        loading = (ProgressBar) findViewById(R.id.loading);
        earthquakeListView.setEmptyView(emptyView);
        if(savedInstanceState!=null){
            ArrayList<Earthquake> list=savedInstanceState.getParcelableArrayList(EARTHQUAKE_LIST);
            adapter=new EarthquakeAdapter(this,list);
        }else{
            adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
            callBackgroundThread();
        }
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Earthquake current = adapter.getItem(i);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(current.getUrl()));
                startActivity(intent);
            }
        });
        //background task
        //EarthquakeAsyntask task=new EarthquakeAsyntask();
        //task.execute(URL_GET);


        //Create toolbar set up
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    private void callBackgroundThread() {
        //check network infor
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infor = connectivityManager.getActiveNetworkInfo();
        if (infor != null && infor.isConnected()) {
            setOnInternet();
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            setNoInternet();
        }
    }

    private void setNoInternet() {
        loading.setVisibility(GONE);
        emptyView.setText(R.string.no_internet);
        emptyImage.setImageResource(R.drawable.ic_cloud_off_black_24dp);
        emptyImage.setVisibility(View.VISIBLE);
    }

    private void setOnInternet() {
        emptyImage.setVisibility(GONE);
        emptyView.setVisibility(GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    private void onSortMethod(int type) {
        List<Earthquake> list=getListFromAdapter();
        adapter.clear();
        //Apply sort
        switch (type) {
            case SORT_BY_DATE:
                Collections.sort(list, new Comparator<Earthquake>() {
                    @Override
                    public int compare(Earthquake earthquake, Earthquake t1) {
                        long a = earthquake.getDate();
                        long b = t1.getDate();
                        return a > b ? -1 : (a < b ? 1 : 0);
                    }
                });

                break;
            case SORT_BY_STRENGTH:
                Collections.sort(list, new Comparator<Earthquake>() {
                    @Override
                    public int compare(Earthquake earthquake, Earthquake t1) {
                        double a = earthquake.getStrength();
                        double b = t1.getStrength();
                        return a > b ? -1 : (a < b ? 1 : 0);
                    }
                });
                break;
        }
        adapter.addAll(list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View view = findViewById(R.id.setting_button);
        switch (item.getItemId()) {
            case R.id.setting_button:
                //create pop up menu
                PopupMenu popupMenu = new PopupMenu(EarthquakeActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.popup_items, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.sort_date:
                                onSortMethod(SORT_BY_DATE);
                                break;
                            case R.id.sort_strength:
                                onSortMethod(SORT_BY_STRENGTH);
                                break;
                            default:
                                LoaderManager manager = getLoaderManager();
                                if (manager.getLoader(LOADER_ID) == null) {
                                    callBackgroundThread();
                                } else {
                                    connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                    NetworkInfo infor = connectivityManager.getActiveNetworkInfo();
                                    if (infor != null && infor.isConnected()) {
                                        setOnInternet();
                                        manager.restartLoader(LOADER_ID, null, EarthquakeActivity.this);
                                    } else {
                                        adapter.clear();
                                        setNoInternet();
                                    }
                                }
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        loading.setVisibility(View.VISIBLE);
        return new EarthquakeLoader(this, URL_GET);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        loading.setVisibility(GONE);
        if (earthquakes == null || earthquakes.isEmpty()) {
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
