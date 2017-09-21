package com.rena21.driver.etc;


import com.rena21.driver.util.FileNameUtil;

import java.util.Comparator;

public class ComparatorTimeSort implements Comparator<String> {

    @Override public int compare(String o1, String o2) {
        o1 = FileNameUtil.getTimeFromFileName(o1);
        o2 = FileNameUtil.getTimeFromFileName(o2);

        return o2.compareTo(o1);
    }
}
