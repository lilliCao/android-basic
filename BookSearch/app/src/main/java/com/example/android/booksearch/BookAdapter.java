package com.example.android.booksearch;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tali on 05.12.17.
 */

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(@NonNull Activity activity, ArrayList<Book> list) {
        super(activity, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        //Haven't use? --> inflate view
        if (view == null) {
            view = LayoutInflater.from(getContext())
                    .inflate(R.layout.book, parent, false);
        }
        final Book book = getItem(position);
        //get view
        TextView title = view.findViewById(R.id.title);
        TextView author = view.findViewById(R.id.author);
        TextView publisher = view.findViewById(R.id.publish_name);
        TextView publishDate = view.findViewById(R.id.publish_date);
        TextView language = view.findViewById(R.id.language);
        TextView saleAbility = view.findViewById(R.id.sale);
        final TextView infoLink = view.findViewById(R.id.infoLink);
        final TextView previewLink = view.findViewById(R.id.previewLink);
        TextView isEbook = view.findViewById(R.id.ebook);
        TextView isPdf = view.findViewById(R.id.pdf);
        final LinearLayout details = view.findViewById(R.id.detail);
        ImageButton moreDetails = view.findViewById(R.id.moreDetails);
        moreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (details.getVisibility() == View.GONE) {
                    details.setVisibility(View.VISIBLE);
                } else {
                    details.setVisibility(View.GONE);
                }
            }
        });
        infoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(book.getInfoLink()));
                view.getContext().startActivity(intent);
            }
        });
        previewLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(book.getPreviewLink()));
                view.getContext().startActivity(intent);
            }
        });
        details.setVisibility(View.GONE);
        //update view
        setTextView(title, book.getTitle());
        setTextView(author, book.getAuthors());
        setTextView(publisher, book.getPublisher());
        setTextView(publishDate, book.getPublisherDate());
        setTextView(language, "Language: " + book.getLanguage());
        setTextView(saleAbility, "Sale status: " + book.getSaleAbility());
        setTextView(infoLink, "Information link: " + book.getInfoLink());
        setTextView(previewLink, "Preview link: " + book.getPreviewLink());
        if (book.isEbook()) {
            setTextView(isEbook, "Ebook: yes");
        } else {
            setTextView(isEbook, "Ebook: no");
        }
        if (book.isAvailableInPdf()) {
            setTextView(isPdf, "Available in pdf: yes");
        } else {
            setTextView(isPdf, "Available in pdf: no");
        }
        return view;
    }

    public static void setTextView(TextView v, String data) {
        if (data.isEmpty()) {
            v.setText("no information");
        } else {
            v.setText(data);
        }
    }
}
