package com.example.android.hamburg;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tali on 26.11.17.
 */

public class ItemAdapter extends ArrayAdapter<Item> {
    private boolean isSetting;
    private boolean noAdaptableImage;
    private int backgroundColor;

    public ItemAdapter(@NonNull Activity activity, ArrayList<Item> arrayList, int backgroundColor, boolean noAdaptableImage) {
        super(activity, 0, arrayList);
        this.backgroundColor = backgroundColor;
        this.noAdaptableImage = noAdaptableImage;
        this.isSetting = false;
    }

    public ItemAdapter(@NonNull Activity activity, ArrayList<Item> arrayList, int backgroundColor, boolean noAdaptableImage, boolean isSetting) {
        super(activity, 0, arrayList);
        this.backgroundColor = backgroundColor;
        this.noAdaptableImage = noAdaptableImage;
        this.isSetting = isSetting;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        //If not used -->inflating new view
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item, parent, false);
        }
        //get current item
        Item currentItem = getItem(position);
        //Get views
        TextView textViewName = listItemView.findViewById(R.id.itemListName);
        LinearLayout listView = listItemView.findViewById(R.id.itemList);
        //Adapt new view
        int color = ContextCompat.getColor(getContext(), this.backgroundColor);
        listView.setBackgroundColor(color);
        textViewName.setText(currentItem.getName());
        if (currentItem.getImageId() != -1) {
            ImageView imageView = listItemView.findViewById(R.id.itemListImage);
            imageView.setBackgroundColor(Color.WHITE);
            if (isSetting) {
                imageView.getLayoutParams().height = 150;
                imageView.getLayoutParams().width = 150;
            } else {
                imageView.getLayoutParams().height = 250;
                imageView.getLayoutParams().width = 250;
            }
            if (this.noAdaptableImage) {
                GlideApp.with(getContext()).load(currentItem.getImageId()).into(imageView);
            } else {
                imageView.setImageResource(currentItem.getImageId());
            }
        }
        TextView link1 = listItemView.findViewById(R.id.link1);
        TextView link2 = listItemView.findViewById(R.id.link2);
        TextView content = listItemView.findViewById(R.id.content);
        link1.setVisibility(View.GONE);
        link2.setVisibility(View.GONE);
        content.setText("");
        return listItemView;
    }
}
