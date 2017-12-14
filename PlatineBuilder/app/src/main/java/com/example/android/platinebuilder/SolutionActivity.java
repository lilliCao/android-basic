package com.example.android.platinebuilder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class SolutionActivity extends AppCompatActivity {
    private boolean show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button rule = findViewById(R.id.rule);
        final Button gui = findViewById(R.id.gui);
        final TextView textRule = findViewById(R.id.ruleText);
        final TextView guiHelp = findViewById(R.id.gui_help);
        Button showAll = findViewById(R.id.show_all);
        Button level1 = findViewById(R.id.level1);
        Button level2 = findViewById(R.id.level2);
        Button level3 = findViewById(R.id.level3);
        Button level4 = findViewById(R.id.level4);
        Button level5 = findViewById(R.id.level5);
        Button level6 = findViewById(R.id.level6);
        Button level7 = findViewById(R.id.level7);
        Button level8 = findViewById(R.id.level8);
        Button level9 = findViewById(R.id.level9);
        Button level10 = findViewById(R.id.level10);
        Button level11 = findViewById(R.id.level11);
        ImageView image1 = findViewById(R.id.level1_im);
        ImageView image2 = findViewById(R.id.level2_im);
        ImageView image3 = findViewById(R.id.level3_im);
        ImageView image4 = findViewById(R.id.level4_im);
        ImageView image5 = findViewById(R.id.level5_im);
        ImageView image6 = findViewById(R.id.level6_im);
        ImageView image7 = findViewById(R.id.level7_im);
        ImageView image8 = findViewById(R.id.level8_im);
        ImageView image9 = findViewById(R.id.level9_im);
        ImageView image10 = findViewById(R.id.level10_im);
        ImageView image11 = findViewById(R.id.level11_im);
        rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textRule.getVisibility() == View.GONE) {
                    textRule.setVisibility(View.VISIBLE);
                } else {
                    textRule.setVisibility(View.GONE);
                }
            }
        });
        gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (guiHelp.getVisibility() == View.GONE) {
                    guiHelp.setVisibility(View.VISIBLE);
                } else {
                    guiHelp.setVisibility(View.GONE);
                }
            }
        });
        final HashMap<Button, ImageView> map = new HashMap<>();
        map.put(level1, image1);
        map.put(level2, image2);
        map.put(level3, image3);
        map.put(level4, image4);
        map.put(level5, image5);
        map.put(level6, image6);
        map.put(level7, image7);
        map.put(level8, image8);
        map.put(level9, image9);
        map.put(level10, image10);
        map.put(level11, image11);
        for (final Map.Entry<Button, ImageView> item : map.entrySet()) {
            item.getKey().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.getValue().getVisibility() == View.GONE) {
                        item.getValue().setVisibility(View.VISIBLE);
                    } else {
                        item.getValue().setVisibility(View.GONE);
                    }
                }
            });
        }
        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!show) {
                    textRule.setVisibility(View.VISIBLE);
                    guiHelp.setVisibility(View.VISIBLE);
                    for (final Map.Entry<Button, ImageView> item : map.entrySet()) {
                        item.getValue().setVisibility(View.VISIBLE);
                    }
                    show = true;
                } else {
                    textRule.setVisibility(View.GONE);
                    guiHelp.setVisibility(View.GONE);
                    for (final Map.Entry<Button, ImageView> item : map.entrySet()) {
                        item.getValue().setVisibility(View.GONE);
                    }
                }
            }
        });
    }
}
