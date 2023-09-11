package com.smarket.hdc2023.widget.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间转换工具类
 */
public class TimeConversionUtils {
    /**
     * 将毫秒转换为mm:ss
     *
     * @param source  date
     * @param pattern 格式
     * @return 相应格式的时间
     */
    public static String dateToString(Date source, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return simpleDateFormat.format(source);
    }

    public static long StringToTime(String time, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取YY:MM:DD:HH:mm:ss格式的时间
     *
     * @return 相应格式的时间
     */
    public static String getCurrentTextTime() {
        Calendar startCalendar = Calendar.getInstance();
        int year = startCalendar.get(Calendar.YEAR);
        int month = startCalendar.get(Calendar.MONTH) + 1;
        int day = startCalendar.get(Calendar.DAY_OF_MONTH);
        int hour = startCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = startCalendar.get(Calendar.MINUTE);
        int second = startCalendar.get(Calendar.SECOND);
        return year + "年" + month + "月" + day + "日" + hour + "时" + minute + "分" + second + "秒";
    }


    /**
     * 获取YYYY-MM-dd HH:mm:ss格式的时间
     *
     * @return 相应格式的时间
     */
    public static String getCurrentTime() {
        Calendar startCalendar = Calendar.getInstance();
        int year = startCalendar.get(Calendar.YEAR);
        int month = startCalendar.get(Calendar.MONTH) + 1;
        int day = startCalendar.get(Calendar.DAY_OF_MONTH);
        int hour = startCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = startCalendar.get(Calendar.MINUTE);
        int second = startCalendar.get(Calendar.SECOND);
        return year + "_" + month + "_" + day + "_" + hour + "_" + minute + "_" + second;
    }

    public static String getCurrentMonthAndDay() {
        Calendar startCalendar = Calendar.getInstance();
        int month = startCalendar.get(Calendar.MONTH) + 1;
        int day = startCalendar.get(Calendar.DAY_OF_MONTH);
        return month + "月" + day + "日";
    }

    /**
     * 获取当前时间和目标时间之差
     *
     * @param targetTime 目标时间
     * @return 时间间隔
     */
    public static String getDaysInterval(long targetTime) {
        String result = "";
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis > targetTime) {
            result = "核酸已过期!";
            return result;
        }
        String stringTargetTime = dateToString(new Date(targetTime), "a HH:mm");

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(currentTimeMillis);
        int startYear = startCalendar.get(Calendar.YEAR);
        int startMoth = startCalendar.get(Calendar.MONTH) + 1;
        int startDay = startCalendar.get(Calendar.DAY_OF_MONTH);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(targetTime);
        int endYear = endCalendar.get(Calendar.YEAR);
        int endMoth = endCalendar.get(Calendar.MONTH) + 1;
        int endDay = endCalendar.get(Calendar.DAY_OF_MONTH);

        LocalDate start = LocalDate.of(startYear, startMoth, startDay);
        LocalDate end = LocalDate.of(endYear, endMoth, endDay);
        int days = (int) ChronoUnit.DAYS.between(start, end);
        switch (days) {
            case 0:
                result = "今天";
                break;
            case 1:
                result = "明天";
                break;
            case 2:
                result = "后天";
                break;
            default:
                if (days > 2) {
                    result = days + "天后";
                }
                break;
        }
        return result + stringTargetTime;
    }


    /**
     * 获取当前时间和目标时间间隔天数和小时
     *
     * @param targetTime 目标时间
     * @return 时间间隔
     */
    public static String getDaysAndHour(long targetTime) {
        String result;
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis >= targetTime) {
            result = "00-00";
            return result;
        }

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(currentTimeMillis);
        int startYear = startCalendar.get(Calendar.YEAR);
        int startMoth = startCalendar.get(Calendar.MONTH) + 1;
        int startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
        int startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
        int startMinute = startCalendar.get(Calendar.MINUTE);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(targetTime);
        int endYear = endCalendar.get(Calendar.YEAR);
        int endMoth = endCalendar.get(Calendar.MONTH) + 1;
        int endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
        int endHour = endCalendar.get(Calendar.HOUR_OF_DAY);
        int endMinute = endCalendar.get(Calendar.MINUTE);

        LocalDateTime start = LocalDateTime.of(startYear, startMoth, startDay, startHour, startMinute);
        LocalDateTime end = LocalDateTime.of(endYear, endMoth, endDay, endHour, endMinute);
        int allHour = (int) ChronoUnit.HOURS.between(start, end);
        int day = allHour / 24;
        int hour = allHour % 24;
        String dayS = String.valueOf(day);
        String hourS = String.valueOf(hour);
        if (dayS.length() == 1) {
            dayS = "0" + dayS;
        }
        if (hourS.length() == 1) {
            hourS = "0" + hourS;
        }
        result = dayS + "-" + hourS;
        return result;
    }
}
