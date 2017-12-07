package com.example.android.booksearch;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static com.example.android.booksearch.MainActivity.FILTER_BY_EBOOK;
import static com.example.android.booksearch.MainActivity.FILTER_BY_LANGUAGE;
import static com.example.android.booksearch.MainActivity.FILTER_BY_PDF;
import static com.example.android.booksearch.MainActivity.SORT_BY_DATE;

/**
 * Created by tali on 05.12.17.
 */

public class BookLoader extends AsyncTaskLoader<ArrayList<Book>> {
    private String url;
    private String sortMethod;

    public BookLoader(Context context, String url, String sortMethod) {
        super(context);
        this.url = url;
        this.sortMethod = sortMethod;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        if (url == null) {
            return null;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String value = sharedPreferences.getString(getContext().getString(R.string.setting_language), "");
        String language = mapToAbb(value);
        ArrayList<Book> list = QueryUtils.fetchBookData(url);
        if (list == null || list.isEmpty()) {
            return list;
        }
        ArrayList<Book> tmp = new ArrayList<>(list);
        switch (sortMethod) {
            case SORT_BY_DATE:
                for (Book b : tmp) {
                    if (formatDate(b.getPublisherDate()) == 0) {
                        list.remove(b);
                    }
                }
                Collections.sort(list, new Comparator<Book>() {
                    @Override
                    public int compare(Book book, Book t1) {
                        long value1 = formatDate(book.getPublisherDate());
                        long value2 = formatDate(t1.getPublisherDate());
                        return value1 > value2 ? -1 : (value1 < value2 ? 1 : 0);
                    }
                });
                break;
            //Lambda not work --> TODO
            case FILTER_BY_EBOOK:
                for (Book b : tmp) {
                    if (!b.isEbook()) {
                        list.remove(b);
                    }
                }
                break;
            case FILTER_BY_LANGUAGE:
                for (Book b : tmp) {
                    if (!b.getLanguage().contains(language)) {
                        list.remove(b);
                    }
                }
                break;
            case FILTER_BY_PDF:
                for (Book b : tmp) {
                    if (!b.isAvailableInPdf()) {
                        list.remove(b);
                    }
                }
                break;
            default:
                break;
        }
        return list;
    }

    private String mapToAbb(String value) {
        String language = "";
        switch (value) {
            case "English":
                language = "en";
                break;
            case "Spanish":
                language = "es";
                break;
            case "German":
                language = "de";
                break;
            case "French":
                language = "fr";
                break;
            default:
                language = "vi";
                break;
        }
        return language;
    }

    public long formatDate(String date) {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        final SimpleDateFormat formattter2 = new SimpleDateFormat("yyyy-MM");
        final SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy");
        long result = 0;
        Date dateFormat;
        try {
            dateFormat = formatter.parse(date);
            result = dateFormat.getTime();
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                dateFormat = formattter2.parse(date);
                result = dateFormat.getTime();
                return result;
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                try {
                    dateFormat = formatter3.parse(date);
                    result = dateFormat.getTime();
                    return result;
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {
                    result = 0;
                }
            }
        }
        return result;
    }
}
