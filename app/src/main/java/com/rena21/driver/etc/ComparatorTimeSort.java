package com.rena21.driver.etc;


import java.util.Comparator;

public class ComparatorTimeSort implements Comparator<String> {

    @Override public int compare(String o1, String o2) {
        o1 = getTimeStampToCompare(o1);
        o2 = getTimeStampToCompare(o2);

        return -o1.compareTo(o2);
    }

    private String getTimeStampToCompare(String fileName) {
        if(fileName.length() != 26 ) {
            throw new RuntimeException("옳지 않은 fileName 형식입니다");
        }
        return fileName.split("_")[1];
    }
}
