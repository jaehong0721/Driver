package com.rena21.driver.listener;


import com.rena21.driver.models.Order;

public interface OrderDeliveryFinishedListener {
    void onOrderDeliveryFinished(Order order, String fileName);
}
