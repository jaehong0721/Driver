package com.rena21.driver.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Order implements Parcelable {
    public List<OrderItem> orderItems;
    public boolean accepted;

    public Order() {}

    protected Order(Parcel in) {
        orderItems = in.createTypedArrayList(OrderItem.CREATOR);
        accepted = in.readByte() != 0;
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
        dest.writeTypedList(orderItems);
        dest.writeByte((byte) (accepted ? 1 : 0));
    }
}
