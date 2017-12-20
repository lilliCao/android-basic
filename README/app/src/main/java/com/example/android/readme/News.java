package com.example.android.readme;

import android.graphics.Bitmap;

/**
 * Created by tali on 19.12.17.
 */

public class News {
    private Bitmap image;
    private String topic;
    private String section;
    private String body;
    private String url;
    private String author;
    private String date;

    public News(Bitmap image, String topic, String section, String body, String author, String date, String url) {
        this.image = image;
        this.topic = topic;
        this.section = section;
        this.body = body;
        this.author = author;
        this.date = date;
        this.url = url;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
