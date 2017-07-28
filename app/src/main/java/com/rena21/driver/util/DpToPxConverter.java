package com.rena21.driver.util;


import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DpToPxConverter {

    public static int convertDpToPx(int dp, DisplayMetrics displayMetrics) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }
}
