package com.example.android.platinebuilder;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tali on 13.12.17.
 */

public class ItemAdapter extends ArrayAdapter<Item> {
    public ItemAdapter(@NonNull Activity activity, ArrayList<Item> list) {
        super(activity, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        Item item = getItem(position);
        if (item.isPlatine()) {
            RelativeLayout platine = view.findViewById(R.id.platines);
            platine.setVisibility(View.VISIBLE);
            TextView cap = view.findViewById(R.id.capacity);
            ImageView image = view.findViewById(R.id.platine);
            cap.setText(String.valueOf(item.getCapacity()));
            image.setImageResource(R.drawable.chip);
        }
        return view;
    }
}
