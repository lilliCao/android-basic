package com.example.android.hamburg;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;


public class MainActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create ViewPager
        ViewPager viewPager = findViewById(R.id.viewPager);
        //Create FragmentPager Adapter
        FragmentPagerAdapter fragmentPagerAdapter =
                new FragmentPagerAdapter(
                        getSupportFragmentManager(), this
                );
        viewPager.setAdapter(fragmentPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        //Add toolbar --> set windowNoTitle in style.xml
        //Create tool menu in res/menu/toolbar.xml
        //Override method onCreateOptionMenu
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Create Spinner
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View view = findViewById(R.id.setting_button);
        switch (item.getItemId()) {
            case R.id.setting_button:
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Intent intent = null;
                        switch (menuItem.getItemId()) {
                            case R.id.about:
                                intent = new Intent(MainActivity.this, AboutActivity.class);
                                break;
                            case R.id.account:
                                intent = new Intent(MainActivity.this, AccountActivity.class);
                                break;
                            default:
                                intent = new Intent(MainActivity.this, SettingActivity.class);
                                break;
                        }
                        startActivity(intent);
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
