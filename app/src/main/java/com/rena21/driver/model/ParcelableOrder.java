package com.rena21.driver.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.rena21.driver.pojo.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class ParcelableOrder implements Parcelable {

    public String restaurantName;
    public List<OrderItem> orderItems;

    public ParcelableOrder(String restaurantName, List<OrderItem> orderItems) {
        this.restaurantName = restaurantName;
        this.orderItems = orderItems;
    }

    protected ParcelableOrder(Parcel in) {
        restaurantName = in.readString();

        orderItems = new ArrayList<>();
        in.readList(orderItems, OrderItem.class.getClassLoader());
    }

    public static final Creator<ParcelableOrder> CREATOR = new Creator<ParcelableOrder>() {
        @Override
        public ParcelableOrder createFromParcel(Parcel in) {
            return new ParcelableOrder(in);
        }

        @Override
        public ParcelableOrder[] newArray(int size) {
            return new ParcelableOrder[size];
        }
    };

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(restaurantName);
        dest.writeList(orderItems);
    }
}
