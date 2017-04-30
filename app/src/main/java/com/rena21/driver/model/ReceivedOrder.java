package com.rena21.driver.model;

public class ReceivedOrder {

    public String timeStamp;
    public String restaurantName;
    public String orderItems;

    public ReceivedOrder(String timeStamp, String restaurantName, String orderItems) {
        this.timeStamp = timeStamp;
        this.restaurantName = restaurantName;
        this.orderItems = orderItems;
    }
}