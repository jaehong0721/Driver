package com.rena21.driver.pojo;

public class OrderItem {
    public String name;
    public String count;

    @Override public String toString() {
        return "OrderItem{" +
                "name='" + name + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
