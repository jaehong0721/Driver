package com.rena21.driver.models;


import java.util.ArrayList;

public class Estimate {
    public String restaurantAddress;
    public String restaurantName;
    public ArrayList<RequestedEstimateItem> items;
    public boolean isFinish;
}
