package com.rena21.driver.models;

import java.util.List;

public class Order{
    public List<OrderItem> orderItems;
    public boolean accepted;
    public boolean delivered;
    public long deliveredTime;

    public Order() {}
}
