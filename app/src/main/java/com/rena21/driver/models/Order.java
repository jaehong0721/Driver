package com.rena21.driver.models;

import android.support.annotation.Keep;

import java.util.List;

@Keep
public class Order{
    public List<OrderItem> orderItems;
    public boolean accepted;
    public boolean delivered;
    public long deliveredTime;
    public int totalPrice;
    public int totalDeposit;

    public Order() {}
}
