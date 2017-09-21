package com.rena21.driver.models;

import android.support.annotation.Keep;

@Keep
public class OrderItem {
    public String name;
    public String count;
    public int price;

    public OrderItem() {}

    @Override public String toString() {
        return "OrderItem{" +
                "name='" + name + '\'' +
                ", count='" + count + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
