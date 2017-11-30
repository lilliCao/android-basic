package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tali on 30.11.17.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    private final static SimpleDateFormat simpleDatFormat=new SimpleDateFormat("MMM DD yyyy");
    private final static SimpleDateFormat simpleDatFormatTime=new SimpleDateFormat("h:mm");
    private final static DecimalFormat simpleDecimalFormat=new DecimalFormat("0.0");
    public EarthquakeAdapter(@NonNull Activity activity, ArrayList<Earthquake> list) {
        super(activity,0,list);
    }
    private int getColor(int strength){
        switch (strength){
            case 0:
                return ContextCompat.getColor(getContext(),R.color.magnitude1);
            case 1:
                return ContextCompat.getColor(getContext(),R.color.magnitude2);
            case 2:
                return ContextCompat.getColor(getContext(),R.color.magnitude3);
            case 3:
                return ContextCompat.getColor(getContext(),R.color.magnitude4);
            case 4:
                return ContextCompat.getColor(getContext(),R.color.magnitude5);
            case 5:
                return ContextCompat.getColor(getContext(),R.color.magnitude6);
            case 6:
                return ContextCompat.getColor(getContext(),R.color.magnitude7);
            case 7:
                return ContextCompat.getColor(getContext(),R.color.magnitude8);
            default:
                return ContextCompat.getColor(getContext(),R.color.magnitude9);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(getContext())
                    .inflate(R.layout.earthquake,parent,false);
        }
        Earthquake current = getItem(position);
        TextView strength = (TextView) view.findViewById(R.id.strength);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView subName = (TextView) view.findViewById(R.id.sub_name);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView time= (TextView) view.findViewById(R.id.time);
        strength.setText(simpleDecimalFormat.format(current.getStrength()));
        GradientDrawable circle= (GradientDrawable) strength.getBackground();
        circle.setColor(getColor((int) current.getStrength()));
        String title=current.getName();
        String titles[] = title.split("of ");
        if(titles.length>1){
            String subText=titles[0]+"of";
            subName.setText(titles[1]);
            name.setText(subText);
        }else{
            subName.setText(title);
            name.setText("");
        }

        date.setText(simpleDatFormat.format(new Date(current.getDate())));
        time.setText(simpleDatFormatTime.format(new Date(current.getDate())));
        return view;

    }
}
