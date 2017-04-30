package com.rena21.driver.model;

public class ReceivedOrder {

    public String restaurantName;
    public String orderItems;

    public ReceivedOrder(String restaurantName, String orderItems) {
        this.restaurantName = restaurantName;
        this.orderItems = orderItems;
    }
}