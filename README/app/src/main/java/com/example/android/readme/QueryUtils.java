package com.example.android.readme;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

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
 * Created by tali on 19.12.17.
 */

public final class QueryUtils {
    private static final int READ_TIME_OUT = 10000;
    private static final String METHOD = "GET";
    private static final int CONNECT_TIME_OUT = 15000;
    private static final int SUCCESS = 200;

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static ArrayList<News> fetchData(String urlString) {
        //url string to URL
        URL url = createUrl(urlString);
        //make request
        String jsonResponse = makeHttpRequest(url);
        ArrayList<News> list = extractResponce(jsonResponse);
        return list;
    }

    private static ArrayList<News> extractResponce(String jsonResponse) {
        ArrayList<News> list = new ArrayList<>();
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        try {
            JSONObject jo = new JSONObject(jsonResponse);
            JSONObject response = jo.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                String date = "";
                String author = "";
                String section = "";
                String topic = "";
                String body = "";
                String url = "";
                Bitmap image = null;
                String imageUrl = "";
                JSONObject item = results.getJSONObject(i);
                section = setData(item, "sectionName");
                date = setData(item, "webPublicationDate");
                topic = setData(item, "webTitle");
                url = setData(item, "webUrl");
                if (item.has("fields")) {
                    JSONObject fields = item.getJSONObject("fields");
                    body = setData(fields, "body");
                    imageUrl = setData(fields, "thumbnail");
                    if (!imageUrl.isEmpty()) {
                        image = getImage(imageUrl);
                    }
                }
                if (item.has("tags")) {
                    JSONArray tags = item.getJSONArray("tags");
                    for (int j = 0; j < tags.length(); j++) {
                        JSONObject tag = tags.getJSONObject(j);
                        author = author + setData(tag, "webTitle") + ",";
                    }
                    if (!author.isEmpty()) {
                        author = author.substring(0, author.length() - 2);
                    }
                }

                list.add(new News(image, topic, section, body, author, date, url));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static Bitmap getImage(String imageUrl) {
        URL url = createUrl(imageUrl);
        Bitmap bitmap = null;
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(METHOD);
            httpURLConnection.setReadTimeout(READ_TIME_OUT);
            httpURLConnection.setConnectTimeout(CONNECT_TIME_OUT);
            httpURLConnection.connect();
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == SUCCESS) {
                inputStream = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static String setData(JSONObject item, String field) throws JSONException {
        if (item.has(field)) {
            return item.getString(field);
        } else return "";
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
            httpURLConnection.setRequestMethod(METHOD);
            httpURLConnection.setReadTimeout(READ_TIME_OUT);
            httpURLConnection.setConnectTimeout(CONNECT_TIME_OUT);
            httpURLConnection.connect();
            int reponseCode = httpURLConnection.getResponseCode();
            if (reponseCode == SUCCESS) {
                ins = httpURLConnection.getInputStream();
                jsonResponse = fromInputStreamToString(ins);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }

    private static String fromInputStreamToString(InputStream ins) {
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

    private static URL createUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
