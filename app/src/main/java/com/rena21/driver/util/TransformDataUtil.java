package com.rena21.driver.util;


import java.util.List;

public class TransformDataUtil {

    public static String makeDeliveryAreaListInLine(List<String> areas) {
        StringBuilder sb = new StringBuilder();
        for(String area : areas) {
            sb.append(area);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
