package com.rena21.driver.models;


public class RepliedEstimateItem extends RequestedEstimateItem {
    public int price;

    @Override public String toString() {
        return "RepliedEstimateItem{" +
                "price=" + price +
                '}';
    }
}
