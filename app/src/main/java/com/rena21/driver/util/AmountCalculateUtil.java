package com.rena21.driver.util;


import com.rena21.driver.models.OrderItem;

import java.util.Collection;
import java.util.List;

public class AmountCalculateUtil {
    public static int sumOfEachOrderItem(List<OrderItem> orderItems) {
        int result = 0;
        for(int i = 0; i<orderItems.size(); i++) {
            result += orderItems.get(i).price;
        }
        return result;
    }

    public static int sumOfTotal(Collection<Integer> total) {
        int result = 0;
        for(Integer price : total) {
            result += price;
        }
        return result;
    }
}
