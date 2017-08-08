package com.rena21.driver.models;


import java.util.ArrayList;
import java.util.Observable;

public class VendorImageData extends Observable {

    public enum RESULT{
        ADDED,
        REMOVED
    }

    private final ArrayList<String> imageUrls;

    public VendorImageData() {
        this.imageUrls = new ArrayList<>();
    }

    public void addImageUrl(String url) {
        imageUrls.add(url);
        setChanged();
        notifyObservers(RESULT.ADDED);
    }

    public void removeImageUrl(String url) {
        imageUrls.remove(url);
        setChanged();
        notifyObservers(RESULT.REMOVED);
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }
}
