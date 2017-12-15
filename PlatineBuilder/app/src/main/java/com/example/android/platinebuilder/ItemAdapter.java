package com.example.android.platinebuilder;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.android.platinebuilder.MainActivity.DEFAULT_ZOOM_PROZENT;

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
            ViewGroup.LayoutParams params = view.getLayoutParams();
            int size = (int) (params.width * (DEFAULT_ZOOM_PROZENT / 100.0));
            params.height = size;
            view.setLayoutParams(params);

            LinearLayout horizon = view.findViewById(R.id.connectors_horizontal);
            ViewGroup.MarginLayoutParams paramsHorizon = (ViewGroup.MarginLayoutParams) horizon.getLayoutParams();
            paramsHorizon.setMargins(0, (int) (size * 1.0 / 5.0), 0, 0);
            horizon.setLayoutParams(paramsHorizon);

            TextView capa=view.findViewById(R.id.capacity);
            float textS=capa.getTextSize();
            textS= (float) (textS*(DEFAULT_ZOOM_PROZENT/100.0));
            capa.setTextSize(0,textS);
        }


        Item item = getItem(position);
        if (item.isPlatine()) {
            LinearLayout platine = view.findViewById(R.id.platines);
            platine.setVisibility(View.VISIBLE);
            TextView cap = view.findViewById(R.id.capacity);
            ImageView image = view.findViewById(R.id.platine);
            cap.setText(String.valueOf(item.getCapacity()));
            image.setImageResource(R.drawable.chip);
        }

        return view;
    }
}
