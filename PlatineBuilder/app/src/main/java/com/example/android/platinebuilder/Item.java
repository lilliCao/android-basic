package com.example.android.platinebuilder;

/**
 * Created by tali on 13.12.17.
 */

public class Item implements Cloneable {
    private int capacity;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private int numberOfConect;
    private int total;
    private boolean isPlatine;
    private boolean isHorizon;


    public Item(int capacity) {
        this.capacity = capacity;
        this.isPlatine = true;
    }

    public Item() {
        this.isPlatine = false;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isPlatine() {
        return isPlatine;
    }

    public void setPlatine(boolean platine) {
        isPlatine = platine;
    }

    public int getNumberOfConect() {
        return numberOfConect;
    }

    public void setNumberOfConect(int numberOfConect) {
        this.numberOfConect = numberOfConect;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isHorizon() {
        return isHorizon;
    }

    public void setHorizon(boolean horizon) {
        isHorizon = horizon;
    }
}
