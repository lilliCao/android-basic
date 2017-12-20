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
import android.widget.ImageView;
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
        ImageView image = view.findViewById(R.id.book_image);
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
        if (!(book.getBookImage() == null)) {
            image.setImageBitmap(book.getBookImage());
        }
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
                Intent intent = new Intent(getContext().getApplicationContext(),WebviewActivity.class);
                intent.putExtra("url", book.getInfoLink());
                view.getContext().startActivity(intent);
                /*
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(book.getInfoLink()));
                view.getContext().startActivity(intent);
                */
            }
        });
        previewLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext().getApplicationContext(),WebviewActivity.class);
                intent.putExtra("url", book.getPreviewLink());
                view.getContext().startActivity(intent);
            }
        });
        details.setVisibility(View.GONE);
        //update view
        setTextView(title, book.getTitle());
        setTextView(author, book.getAuthors());
        setTextView(publisher, book.getPublisher());
        setTextView(publishDate, book.getPublisherDate());
        setTextView(language, getContext().getString(R.string.language) + book.getLanguage());
        setTextView(saleAbility, getContext().getString(R.string.sale) + book.getSaleAbility());
        if (!book.getInfoLink().isEmpty()) {
            setTextView(infoLink, getContext().getString(R.string.info_link));
        }
        if (!book.getPreviewLink().isEmpty()) {
            setTextView(previewLink, getContext().getString(R.string.preview_link));
        }
        if (book.isEbook()) {
            setTextView(isEbook, getContext().getString(R.string.ebook_yes));
        } else {
            setTextView(isEbook, getContext().getString(R.string.ebook_no));
        }
        if (book.isAvailableInPdf()) {
            setTextView(isPdf, getContext().getString(R.string.pdf_yes));
        } else {
            setTextView(isPdf, getContext().getString(R.string.pdf_no));
        }
        return view;
    }

    public static void setTextView(TextView v, String data) {
        if (data.isEmpty()) {
            v.setText(v.getContext().getString(R.string.no_infor));
        } else {
            v.setText(data);
        }
    }
}
