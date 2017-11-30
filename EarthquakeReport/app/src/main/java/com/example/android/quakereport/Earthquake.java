package com.example.android.quakereport;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tali on 30.11.17.
 */

@SuppressLint("ParcelCreator")
public class Earthquake implements Parcelable {
    private double strength;
    private String name;
    private long date;
    private String url;

    public Earthquake(double strength, String name, long date,String url) {
        this.strength = strength;
        this.name = name;
        this.date = date;
        this.url=url;
    }

    public double getStrength() {
        return strength;
    }

    public String getName() {
        return name;
    }

    public long getDate() {
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(this.strength);
        parcel.writeString(this.name);
        parcel.writeLong(this.date);
        parcel.writeString(this.url);

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
