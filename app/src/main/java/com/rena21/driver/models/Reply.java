package com.rena21.driver.models;


import java.util.ArrayList;

public class Reply {
    public ArrayList<RepliedEstimateItem> repliedItems;
    public long timeMillis;
    public String vendorName;
    public int totalPrice;
    public boolean isPicked;

    @Override public String toString() {
        return "Reply{" +
                "repliedItems=" + repliedItems +
                ", timeMillis=" + timeMillis +
                ", vendorName='" + vendorName + '\'' +
                ", totalPrice=" + totalPrice +
                ", isPicked=" + isPicked +
                '}';
    }
}
