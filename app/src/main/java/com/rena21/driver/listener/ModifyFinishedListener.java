package com.rena21.driver.listener;


import com.rena21.driver.models.Order;

public interface ModifyFinishedListener {
    void onModifyFinished(Order order, String fileName);
}
