package com.rena21.driver.models;

import android.os.Parcel;

public class OrderItem {
    public String name;
    public String count;
    public int price;

    public OrderItem() {}

    protected OrderItem(Parcel in) {
        name = in.readString();
        count = in.readString();
    }

    @Override public String toString() {
        return "OrderItem{" +
                "name='" + name + '\'' +
                ", count='" + count + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
