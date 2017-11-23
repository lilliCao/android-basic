package com.example.android.miwok;



/**
 * Created by tali on 22.11.17.
 */

class Word {
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
}
