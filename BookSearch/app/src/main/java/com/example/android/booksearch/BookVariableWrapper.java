package com.example.android.booksearch;

import java.io.BufferedReader;
import java.util.Observable;

/**
 * Created by tali on 06.12.17.
 */

public class BookVariableWrapper extends Observable {
    public String getSorthMethod() {
        return sorthMethod;
    }

    private String sorthMethod;
    public BookVariableWrapper(String sorthMethod){
        this.sorthMethod=sorthMethod;
    }
    public void setSorthMethod(String sorthMethod){
        this.sorthMethod=sorthMethod;
        setChanged();
        notifyObservers();
    }
}
