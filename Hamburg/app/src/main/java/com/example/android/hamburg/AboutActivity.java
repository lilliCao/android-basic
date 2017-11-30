package com.example.android.hamburg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by tali on 26.11.17.
 */

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ArrayList<Item> listItem = new ArrayList<>(Arrays.asList(
                new Item(R.drawable.ic_android_black_24dp, getString(R.string.about), getString(R.string.about_content)
                        , "", ""),
                new Item(R.drawable.ic_help_outline_black_24dp, getString(R.string.help), getString(R.string.help_content)
                        , "https://github.com/lilliCao/AndroidBasic", "")
        ));
        ItemAdapter itemAdapter = new ItemAdapter(this, listItem, R.color.colorAccomodation, true);
        ListView listView = findViewById(R.id.list_item_setting);
        listView.setAdapter(itemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item currentItem = listItem.get(i);
                TextView textView = view.findViewById(R.id.content);
                TextView link1View = view.findViewById(R.id.link1);
                TextView link2View = view.findViewById(R.id.link2);
                if (textView.getText().toString().isEmpty()) {
                    textView.setText(currentItem.getText());
                    if (link1View.getText().toString().isEmpty() && !currentItem.getLink1().isEmpty()) {
                        link1View.setText(currentItem.getLink1());
                        link1View.setVisibility(View.VISIBLE);
                    }
                    if (link2View.getText().toString().isEmpty() && !currentItem.getLink2().isEmpty()) {
                        link2View.setText(currentItem.getLink2());
                        link2View.setVisibility(View.VISIBLE);
                    }
                } else {
                    textView.setText("");
                    link1View.setText("");
                    link2View.setText("");
                    link1View.setVisibility(View.GONE);
                    link2View.setVisibility(View.GONE);
                }

            }
        });
    }
}
