package com.rena21.driver.util;

import org.joda.time.DateTime;

public class JodaTimeUtil {

    enum DayOfWeek {
        MON("월"),
        TUE("화"),
        WED("수"),
        THU("목"),
        FRI("금"),
        SAT("토"),
        SUN("일");

        private final String dayOfWeekInKorean;

        DayOfWeek(String dayOfWeekInKorean) {
            this.dayOfWeekInKorean = dayOfWeekInKorean;
        }

        // Joda time에서 월요일(1)부터 시작해서 일요일(7)로 구성 되어 있다.
        public static String getDayOfWeekInKorean(int dayOfWeek) {
            return DayOfWeek.values()[dayOfWeek - 1].dayOfWeekInKorean;
        }
    }

    public static String getSimpleMMdd(long timeInMillis) {
        DateTime dateTime = new DateTime(timeInMillis);
        int dayOfWeek = dateTime.getDayOfWeek();
        String result = "" +
                dateTime.getMonthOfYear() + "월" +
                dateTime.getDayOfMonth() + "일" + " " +
                "(" + DayOfWeek.getDayOfWeekInKorean(dayOfWeek) + ")";
        return result;
    }

    public static String yyyyMMdd(DateTime dateTime) {
        String result = "" +
                dateTime.getYear() + "년 " +
                dateTime.getMonthOfYear() + "월 " +
                dateTime.getDayOfMonth() + "일";
        return result;
    }

    public static String yyyyMMddSimple(DateTime dateTime) {
        String result = "" +
                dateTime.getYear() + "." +
                dateTime.getMonthOfYear() + "." +
                dateTime.getDayOfMonth();
        return result;
    }

    public static boolean isEqualsOnlyDate(DateTime time1, DateTime time2) {
        long time1Long = time1.withTimeAtStartOfDay().getMillis();
        long time2Long = time2.withTimeAtStartOfDay().getMillis();
        return time1Long == time2Long;
    }

}
