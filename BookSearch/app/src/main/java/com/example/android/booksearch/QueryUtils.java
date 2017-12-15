package com.example.android.booksearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by tali on 05.12.17.
 */

public final class QueryUtils {
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static ArrayList<Book> fetchBookData(String urlString) {
        //string --> url object
        URL url = create(urlString);
        //do request
        String jsonResponse = makeHttpRequest(url);
        ArrayList<Book> list = extractResponse(jsonResponse);
        return list;
    }

    private static ArrayList<Book> extractResponse(String jsonResponse) {
        ArrayList<Book> list = new ArrayList<>();
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        try {
            JSONObject jo = new JSONObject(jsonResponse);
            if (jo == null) {
                return list;
            }
            JSONArray ja = jo.optJSONArray("items");
            if ((ja == null) || (ja.length() == 0)) {
                return list;
            }
            Log.e("Array length issss", String.valueOf(ja.length()));
            for (int i = 0; i < ja.length(); i++) {
                JSONObject child = (JSONObject) ja.get(i);
                JSONObject volumeInfo = child.optJSONObject("volumeInfo");

                String title = volumeInfo.getString("title");

                String author = "";
                if (volumeInfo.has("authors")) {
                    JSONArray authors = volumeInfo.getJSONArray("authors");

                    if (authors.length() > 0) {
                        for (int j = 0; j < authors.length(); j++) {
                            author = author + authors.get(j).toString() + ",";
                        }
                        author = author.substring(0, author.length() - 2);
                    }
                }

                String publisher = "";
                if (volumeInfo.has("publisher")) {
                    publisher = volumeInfo.getString("publisher");
                }
                String publisherDate = "";
                if (volumeInfo.has("publishedDate")) {
                    publisherDate = volumeInfo.getString("publishedDate");
                }
                String language = "";
                if (volumeInfo.has("language")) {
                    language = volumeInfo.getString("language");
                }
                String infoLink = "";
                if (volumeInfo.has("infoLink")) {
                    infoLink = volumeInfo.getString("infoLink");
                }
                String previewLink = "";
                if (volumeInfo.has("previewLink")) {
                    previewLink = volumeInfo.getString("previewLink");
                }
                Bitmap bookImage = null;
                String imageUrl = "";
                if (volumeInfo.has("imageLinks")) {
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                    if (imageLinks.has("smallThumbnail")) {
                        imageUrl = imageLinks.getString("smallThumbnail");
                    } else if (imageLinks.has("thumbnail")) {
                        imageUrl = imageLinks.getString("thumbnail");
                    }
                }
                if (!imageUrl.isEmpty()) {
                    bookImage = getBookImage(imageUrl);
                }

                JSONObject saleInfo = child.optJSONObject("saleInfo");
                String saleAbility = "";
                if (saleInfo.has("saleability")) {
                    saleAbility = saleInfo.getString("saleability");
                }
                boolean isEbook = false;
                if (saleInfo.has("isEbook")) {
                    isEbook = saleInfo.getBoolean("isEbook");
                }

                JSONObject accessInfo = child.getJSONObject("accessInfo");
                JSONObject pdf = accessInfo.getJSONObject("pdf");
                boolean isPdf = false;
                if (pdf.has("isAvailable")) {
                    isPdf = pdf.getBoolean("isAvailable");
                }

                list.add(new Book(title, author, publisher, publisherDate, language, isEbook, isPdf, saleAbility, infoLink, previewLink, bookImage));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static Bitmap getBookImage(String imageUrl) {
        URL url = create(imageUrl);
        Bitmap bitmap = null;
        InputStream ins = null;
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.connect();
            int code = httpURLConnection.getResponseCode();
            if (code == 200) {
                ins = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(ins);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static String makeHttpRequest(URL url) {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection httpURLConnection = null;
        InputStream ins = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.connect();
            int code = httpURLConnection.getResponseCode();
            if (code == 200) {
                ins = httpURLConnection.getInputStream();
                jsonResponse = streamToString(ins);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }

    private static String streamToString(InputStream ins) {
        StringBuilder builder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(ins, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                builder.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    private static URL create(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
