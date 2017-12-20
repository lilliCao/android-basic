package com.example.android.readme;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

/**
 * Created by tali on 19.12.17.
 */

public class NewsAdapter extends ArrayAdapter<News> {


    public NewsAdapter(Activity activity, ArrayList<News> list) {
        super(activity, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.news, parent, false);
        }
        //get data
        final News news = getItem(position);
        //get view
        ImageView image = view.findViewById(R.id.image);
        TextView section = view.findViewById(R.id.section);
        TextView date = view.findViewById(R.id.date);
        TextView topic = view.findViewById(R.id.topic);
        TextView body = view.findViewById(R.id.body);
        TextView author = view.findViewById(R.id.author);
        LinearLayout share = view.findViewById(R.id.share);
        LinearLayout save = view.findViewById(R.id.save);
        //fetch data in view
        if (news.getImage() != null) {
            image.setImageBitmap(news.getImage());
        }
        setText(section, news.getSection());
        setText(date, news.getDate());
        setText(author, news.getAuthor());
        setText(topic, news.getTopic());
        String shortBody = news.getBody().length() >= 131 ? news.getBody().substring(0, 130) : news.getBody();
        shortBody = android.text.Html.fromHtml(shortBody).toString().trim();
        shortBody += "...";
        setText(body, shortBody);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getUrl()));
                view.getContext().startActivity(intent);
            }
        };
        //set all listener to open web
        image.setOnClickListener(clickListener);
        section.setOnClickListener(clickListener);
        date.setOnClickListener(clickListener);
        topic.setOnClickListener(clickListener);
        body.setOnClickListener(clickListener);

        //share click
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, news.getUrl());
                getContext().startActivity(Intent.createChooser(shareIntent, ""));
            }
        });
        return view;
    }

    private void setText(TextView section, String section1) {
        if (section1.isEmpty()) {
            section.setText(getContext().getString(R.string.no_information));
        } else {
            section.setText(section1);
        }
    }
}
