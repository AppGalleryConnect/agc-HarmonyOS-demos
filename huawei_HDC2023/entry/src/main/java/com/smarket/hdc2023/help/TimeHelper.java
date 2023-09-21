package com.smarket.hdc2023.help;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeHelper {
    public static String MESSAGE_DATE_PATTERN = "MM月dd日 HH点mm分";
    public static String MESSAGE_NOTIFY_DATE_PATTERN = "yyyy-MM-dd HH:mm:SS";
    public static String MESSAGE_NOTIFY_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static String MESSAGE_NOTIFY_TIME_PATTERN2 = "yyyy.MM.dd";
    public static String MESSAGE_NOTIFY_TIME_PATTERN3 = "MM月dd日";
    public static String MESSAGE_NOTIFY_TIME_PATTERN4 = "HH:mm";
    public static String MESSAGE_NOTIFY_TIME_PATTERN5 = "dd";
    public static String MESSAGE_NOTIFY_TIME_PATTERN6 = "yyyy.MM.dd";
    public static String MESSAGE_NOTIFY_TIME_PATTERN7 = "MM.dd";

    /*
     * 将10 or 13 位时间戳转为时间字符串
     * convert the number 1407449951 1407499055617 to date/time format timestamp
     */
    public static long timestamp2Date(long str_num) {
        if ((new Long(str_num)).toString().length() == 10) {
            return str_num * 1000L;
        }
        return str_num;
    }

    public static boolean isOneHours(String startTime) {
        long time = Long.valueOf(startTime);
        time = timestamp2Date(time);
        long timeOneHours = time + (60 * 60 * 1000);
        long newTime = new Date().getTime();
        return newTime < time&&newTime>timeOneHours;
    }

    public static String getDate(Long time) {
        long str_num = timestamp2Date(time);

        SimpleDateFormat sdf = new SimpleDateFormat(MESSAGE_NOTIFY_TIME_PATTERN2);

        return sdf.format(new Date(str_num));
    }

    public static String getDateByDay(Long time) {
        long str_num = timestamp2Date(time);

        SimpleDateFormat sdf = new SimpleDateFormat(MESSAGE_NOTIFY_TIME_PATTERN3);

        return sdf.format(new Date(str_num));
    }

    public static String getDateByDay2(Long time) {
        long str_num = timestamp2Date(time);

        SimpleDateFormat sdf = new SimpleDateFormat(MESSAGE_NOTIFY_TIME_PATTERN7);

        return sdf.format(new Date(str_num));
    }

    public static String getDayByDate(Long time) {
        long str_num = timestamp2Date(time);

        SimpleDateFormat sdf = new SimpleDateFormat(MESSAGE_NOTIFY_TIME_PATTERN5);

        return sdf.format(new Date(str_num));
    }

    public static String getDateByTime(Long time) {
        long str_num = timestamp2Date(time);

        SimpleDateFormat sdf = new SimpleDateFormat(MESSAGE_NOTIFY_TIME_PATTERN4);

        return sdf.format(new Date(str_num));
    }

    public static long getTimeBy10() {

        return new Date().getTime() / 1000;
    }

    public static boolean isDay(long startTime, String startData, String endData) {
        startTime = timestamp2Date(startTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat(MESSAGE_NOTIFY_TIME_PATTERN2);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00")); // 设置北京时区
        Date date = new Date();
        long startnewData = 0L;
        long endnewData = 0L;
        try {
            date = dateFormat.parse(startData);
            startnewData = date.getTime();
            date = dateFormat.parse(endData);
            endnewData = date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startTime > startnewData && startTime < endnewData;
    }

    public static String getDayPattern6ByTime(Long time) {
        long str_num = timestamp2Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(MESSAGE_NOTIFY_TIME_PATTERN6);
        return sdf.format(new Date(str_num));
    }

    public static String getByDateTime(Long time) {
        long str_num = timestamp2Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(MESSAGE_NOTIFY_TIME_PATTERN);
        return sdf.format(new Date(str_num));
    }
}
