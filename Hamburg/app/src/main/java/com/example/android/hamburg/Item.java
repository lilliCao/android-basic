package com.example.android.hamburg;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tali on 26.11.17.
 */

@SuppressLint("ParcelCreator")
public class Item implements Parcelable {
    private int imageId;
    private String name;
    private String status;
    private String text;
    private String link1;
    private String link2;

    @Override
    public int describeContents() {
        return 0;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLink1(String link1) {
        this.link1 = link1;
    }

    public void setLink2(String link2) {
        this.link2 = link2;
    }

    public Item(int imageId, String name, String text, String link1, String link2) {
        this.imageId = imageId;
        this.name = name;
        this.status = "";
        this.text = text;
        this.link1 = link1;
        this.link2 = link2;

    }

    public Item(String name, String text, String link1, String link2) {
        this.name = name;
        this.imageId = -1;
        this.status = "";
        this.text = text;
        this.link1 = link1;
        this.link2 = link2;
    }

    public String getText() {
        return this.text;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getLink1() {
        return link1;
    }

    public String getLink2() {
        return link2;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(imageId);
        parcel.writeString(name);
        parcel.writeString(status);
        parcel.writeString(text);
    }

}
