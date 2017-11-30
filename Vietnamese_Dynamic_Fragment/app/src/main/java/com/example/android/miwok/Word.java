package com.example.android.miwok;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tali on 22.11.17.
 */

class Word implements Parcelable{
    private String origin;
    private String translation;
    private int imageNumber;
    private int audioNumber;

    Word(String origin, String translation, int imageNumber, int audioNumber) {
        this.origin = origin;
        this.translation = translation;
        this.imageNumber = imageNumber;
        this.audioNumber = audioNumber;
    }

    public Word(String origin, String translation) {
        this.origin = origin;
        this.translation = translation;
    }

    public Word(String origin, String translation,int audioNumber){
        this.origin=origin;
        this.translation=translation;
        this.audioNumber=audioNumber;
    }
;
    public String getOrigin() {
        return origin;
    }

    public String getTranslation() {
        return translation;
    }

    public int getImageNumber() {
        return imageNumber;
    }

    public int getAudioNumber() {
        return audioNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.origin);
        parcel.writeString(this.translation);
        parcel.writeInt(this.imageNumber);
        parcel.writeInt(this.audioNumber);
    }
}
