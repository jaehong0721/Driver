package com.rena21.driver.models;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderItem implements Parcelable{
    public String name;
    public String count;
    public String price;

    public OrderItem() {}

    protected OrderItem(Parcel in) {
        name = in.readString();
        count = in.readString();
    }

    public static final Creator<OrderItem> CREATOR = new Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel in) {
            return new OrderItem(in);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(count);
    }

    @Override public String toString() {
        return "OrderItem{" +
                "name='" + name + '\'' +
                ", count='" + count + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
