package com.rena21.driver.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransformDataUtil {

    public static String makeDeliveryAreasString(List<String> areas) {
        StringBuilder sb = new StringBuilder();
        for(String area : areas) {
            sb.append(area);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public static List<String> makeDeliveryAreasList(String areas) {
        String[] splitAreas = areas.split(",");
        List<String> areaList = new ArrayList<>(Arrays.asList(splitAreas));
        return areaList;
    }
}
