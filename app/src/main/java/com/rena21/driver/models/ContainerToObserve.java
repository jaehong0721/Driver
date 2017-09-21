package com.rena21.driver.models;


import java.util.Observable;

public class ContainerToObserve<T> extends Observable {

    private T object;

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
        setChanged();
        notifyObservers();
    }
}
