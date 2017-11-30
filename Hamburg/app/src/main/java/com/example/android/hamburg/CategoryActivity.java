package com.example.android.hamburg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by tali on 25.11.17.
 */

public class CategoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_container);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new CategoryFragment())
                .commit();
    }

}
