package com.example.android.booksearch;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tali on 05.12.17.
 */

@SuppressLint("ParcelCreator")
public class Book implements Parcelable {
    private String title;
    private String authors;
    private String publisher;
    private String publisherDate;
    private String language;
    private boolean isEbook;
    private boolean availableInPdf;
    private String saleAbility;
    private String infoLink;
    private String previewLink;
    private Bitmap bookImage;

    public Book(String title, String authors, String publisher, String publisherDate, String language, boolean isEbook, boolean availableInPdf, String saleAbility, String infoLink, String previewLink, Bitmap bookImage) {
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publisherDate = publisherDate;
        this.language = language;
        this.isEbook = isEbook;
        this.availableInPdf = availableInPdf;
        this.saleAbility = saleAbility;
        this.infoLink = infoLink;
        this.previewLink = previewLink;
        this.bookImage = bookImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.authors);
        parcel.writeString(this.language);
        parcel.writeString(this.publisher);
        parcel.writeString(this.publisherDate);
        parcel.writeBooleanArray(new boolean[]{this.isEbook, this.availableInPdf});
        parcel.writeString(this.saleAbility);
        parcel.writeString(this.infoLink);
        parcel.writeString(this.previewLink);
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublisherDate() {
        return publisherDate;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isEbook() {
        return isEbook;
    }

    public boolean isAvailableInPdf() {
        return availableInPdf;
    }

    public String getSaleAbility() {
        return saleAbility;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPublisherDate(String publisherDate) {
        this.publisherDate = publisherDate;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setEbook(boolean ebook) {
        isEbook = ebook;
    }

    public void setAvailableInPdf(boolean availableInPdf) {
        this.availableInPdf = availableInPdf;
    }

    public void setSaleAbility(String saleAbility) {
        this.saleAbility = saleAbility;
    }

    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }

    public Bitmap getBookImage() {
        return bookImage;
    }

    public void setBookImage(Bitmap bookImage) {
        this.bookImage = bookImage;
    }
}
