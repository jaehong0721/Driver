package com.rena21.driver.models;


import java.util.ArrayList;
import java.util.HashMap;

public class Reply {
    public ArrayList<RepliedEstimateItem> repliedItems;
    public long timeMillis;
    public String vendorName;
    public int totalPrice;
    public boolean isPicked;

    @Override public String toString() {
        return "Reply{" +
                "repliedItems=" + repliedItems +
                ", timeMillis=" + timeMillis +
                ", vendorName='" + vendorName + '\'' +
                ", totalPrice=" + totalPrice +
                ", isPicked=" + isPicked +
                '}';
    }

    public ArrayList<HashMap<String, String>> getRepliedItemNameMapList() {
        ArrayList<HashMap<String, String>> itemNameMapList = new ArrayList<>();
        for(int i = 0; i<repliedItems.size(); i++) {
            if(repliedItems.get(i).price != 0) {
                HashMap<String, String> itemNameMap = new HashMap<>();
                itemNameMap.put("name", repliedItems.get(i).itemName);
                itemNameMapList.add(itemNameMap);
            }
        }
        return itemNameMapList;
    }
}
