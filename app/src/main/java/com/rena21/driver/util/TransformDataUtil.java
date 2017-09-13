package com.rena21.driver.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransformDataUtil {

    public static String makeDeliveryAreasString(List<String> areas) {
        if(areas.size() == 0) return null;

        StringBuilder sb = new StringBuilder();
        for(String area : areas) {
            sb.append(area);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public static List<String> makeDeliveryAreasList(String areas) {
        if(areas.equals("")) return null;

        String[] splitAreas = areas.split(",");
        List<String> areaList = new ArrayList<>(Arrays.asList(splitAreas));
        return areaList;
    }

    public static String getPhoneNumberFrom(String estimateKey) {
        if(estimateKey == null || estimateKey.length() == 0)
            throw new RuntimeException("견적요청 key값이 올바르지 않습니다.");

        return estimateKey.split("_")[0];
    }
}
