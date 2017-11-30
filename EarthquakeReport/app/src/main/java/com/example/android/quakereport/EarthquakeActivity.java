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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a fake list of earthquake locations.
        /*
        ArrayList<Earthquake> earthquakes = new ArrayList<>(
                Arrays.asList(
                new Earthquake("1.2","San Francisco","Feb,15-01"),
                new Earthquake("1.4","Shanghai","Feb,15-02"),
                new Earthquake("1.6","London","Feb,15-03"),
                new Earthquake("1.7","Tokyo","Feb,15-04"),
                new Earthquake("1.8","Mexico City","Feb,15-05"),
                new Earthquake("1.9","Moscow","Feb,15-06"),
                new Earthquake("3.4","Rio de Janeiro","Feb,15-07"),
                new Earthquake("6.7","Paris", "Feb,15-08")
        )
        );

        */

        final ArrayList<Earthquake> earthquakes=QueryUtils.extractEarthquakes();


        // Find a reference to the {@link ListView} in the layout
        final ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        EarthquakeAdapter adapter=new EarthquakeAdapter(this,earthquakes);
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Earthquake current = earthquakes.get(i);
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(current.getUrl()));
                startActivity(intent);
            }
        });
    }
}
