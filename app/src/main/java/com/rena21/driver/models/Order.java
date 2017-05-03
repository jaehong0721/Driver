package com.rena21.driver.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Order implements Parcelable{
    public String restaurantName;
    public List<OrderItem> orderItems;

    public Order() {}

    protected Order(Parcel in) {
        restaurantName = in.readString();
        orderItems = in.createTypedArrayList(OrderItem.CREATOR);
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(restaurantName);
        dest.writeTypedList(orderItems);
    }
}
