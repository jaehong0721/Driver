package com.rena21.driver.listener;


import com.rena21.driver.models.OrderItem;

import java.util.List;

public interface OrderDeliveryFinishedListener {
    void onOrderDeliveryFinished(List<OrderItem> orderItems, String fileName);
}
