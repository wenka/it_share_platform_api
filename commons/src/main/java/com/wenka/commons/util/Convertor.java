package com.wenka.commons.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */

public abstract class Convertor {
    public Convertor() {
    }

    public static String formatDate(Date date, String format) {
        String result = null;

        try {
            SimpleDateFormat e = new SimpleDateFormat(format);
            result = e.format(date);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return result;
    }

    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    public static String formatTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parseDate(String strDate, String format) {
        Date result = null;

        try {
            SimpleDateFormat e = new SimpleDateFormat(format);
            result = e.parse(strDate);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return result;
    }

    public static Date parseDate(String strDate) {
        return parseDate(strDate, "yyyy-MM-dd");
    }

    public static Date parseTime(String strDate) {
        return parseDate(strDate, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date getMaxTime(Date date) {
        Date d = getMinTime(date);
        d.setTime(d.getTime() + 86400000L - 1L);
        return d;
    }

    public static Date getMinTime(Date date) {
        return parseDate(formatDate(date, "yyyy-MM-dd"), "yyyy-MM-dd");
    }

    public static Date getFirstDayThisWeek() {
        Calendar calendar = Calendar.getInstance();
        int min = calendar.getActualMinimum(7);
        int current = calendar.get(7);
        calendar.add(7, min - current + 1);
        Date start = calendar.getTime();
        return getMinTime(start);
    }

    public static Date getLastDayThisWeek() {
        Calendar calendar = Calendar.getInstance();
        int min = calendar.getActualMinimum(7);
        int current = calendar.get(7);
        calendar.add(7, min - current + 1);
        calendar.add(7, 6);
        Date end = calendar.getTime();
        return getMaxTime(end);
    }
}
