package com.smarket.hdc2023.help;


import ohos.aafwk.ability.DataAbilityHelper;
import ohos.agp.utils.TextTool;
import ohos.app.Context;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.utils.net.Uri;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 添加日历日程工具类
 */
public class CalendarReminderUtils {
    private static final String CALENDER_URL = "dataability:///com.android.calendar/calendars";
    private static final String CALENDER_EVENT_URL = "dataability:///com.android.calendar/events";
    private static final String CALENDER_REMINDER_URL = "dataability:///com.android.calendar/reminders";

    private static final String CALENDARS_NAME = "HDC2021";
    private static final String CALENDARS_ACCOUNT_NAME = "HDC2021";
    private static final String CALENDARS_ACCOUNT_TYPE = "HDC2021";
    private static final String CALENDARS_DISPLAY_NAME = "HDC2021";

    /**
     * 检查是否已经添加了日历账户，如果没有添加先添加一个日历账户再查询
     * 获取账户成功返回账户id，否则返回-1
     */
    private static int checkAndAddCalendarAccount(Context context) {
        int oldId = checkCalendarAccount(context);
        if (oldId >= 0) {
            return oldId;
        } else {
            long addId = addCalendarAccount(context);
            if (addId >= 0) {
                return checkCalendarAccount(context);
            } else {
                return -1;
            }
        }
    }

    /**
     * 检查是否存在现有账户，存在则返回账户id，否则返回-1
     */
    private static int checkCalendarAccount(Context context) {
        DataAbilityHelper dataAbilityHelper = DataAbilityHelper.creator(context);
        ResultSet resultSet = null;
        try {
            resultSet = dataAbilityHelper.query(Uri.parse(CALENDER_URL), null, null);
            if (resultSet == null) { //查询返回空值
                return -1;
            }
            int count = resultSet.getColumnCount();
            if (count > 0) { //存在现有账户，取第一个账户的id返回
                resultSet.goToFirstRow();
                return resultSet.getInt(resultSet.getColumnIndexForName("_id"));
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    /**
     * 添加日历账户，账户创建成功则返回账户id，否则返回-1
     */
    private static long addCalendarAccount(Context context) {
        DataAbilityHelper dataAbilityHelper = DataAbilityHelper.creator(context);
        TimeZone timeZone = TimeZone.getDefault();
        ValuesBucket value = new ValuesBucket();
        value.putString("name", CALENDARS_NAME);
        value.putString("account_name", CALENDARS_ACCOUNT_NAME);
        value.putString("account_type", CALENDARS_ACCOUNT_TYPE);
        value.putString("calendar_displayName", CALENDARS_DISPLAY_NAME);
        value.putInteger("visible", 1);
        value.putInteger("calendar_access_level", 700);
        value.putInteger("sync_events", 1);
        value.putString("calendar_timezone", timeZone.getID());
        value.putString("ownerAccount", "HDC2021");
        value.putInteger("canOrganizerRespond", 0);


        Uri calendarUri = Uri.parse(CALENDER_URL);

        try {
            int result = dataAbilityHelper.insert(calendarUri, value);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        }

    }

    /**
     * 添加日历事件
     * previousDate
     */
    public static int addCalendarEvent(Context context, String title, String location, String description, long reminderTime, long endTime) {
        DataAbilityHelper dataAbilityHelper = DataAbilityHelper.creator(context);
        if (context == null) {
            return -1;
        }
        int calId = checkAndAddCalendarAccount(context); //获取日历账户的id
        if (calId < 0) { //获取账户id失败直接返回，添加日历事件失败
            return -2;
        }

        boolean isSelect = isSelectCalendarEvent(context, title);//查询是都已经添加过
        if (isSelect) {
            //如果已经添加过就不做处理,否则会有两条一样的日程数据
            return -3;
        }
        reminderTime = TimeHelper.timestamp2Date(reminderTime);
        endTime = TimeHelper.timestamp2Date(endTime);
        //添加日历事件
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(reminderTime);//设置开始时间
        long start = mCalendar.getTime().getTime();
        mCalendar.setTimeInMillis(endTime);//设置终止时间
        long end = mCalendar.getTime().getTime();

        ValuesBucket event = new ValuesBucket();
        event.putString("title", title);
        event.putString("description", description);
        event.putInteger("calendar_id", calId); //插入账户的id
        event.putString("eventLocation", location);//时间地点
        event.putLong("dtstart", start);
        event.putLong("dtend", end);
        event.putInteger("hasAlarm", 1);//设置有闹钟提醒
        event.putString("eventTimezone", "Asia/Shanghai");//这个是时区，必须有


        event.putInteger("eventStatus", 1);
        event.putInteger("selfAttendeeStatus", 1);
        event.putInteger("accessLevel", 0);
        event.putInteger("availability", 0);
        event.putInteger("hasExtendedProperties", 0);
        event.putBoolean("hasAttendeeData", false);
        event.putBoolean("guestsCanInviteOthers", false);
        event.putBoolean("guestsCanModify", false);
        event.putBoolean("guestsCanSeeGuests", true);
//        event.putString("organizer", "yjm");
        event.putBoolean("isOrganizer", true);


        //        // 日程的一键服务相关数据
//        event.putString("hwext_service_description", description);
//        event.putString("hwext_service_type", "Collection");
//        event.putString("hwext_service_cp_bz_uri",
//                "smarket://com.smarket.hdc2023");
        int result = 0;
        try {
//            result = dataAbilityHelper.insert(Uri.parse("dataability:///com.huawei.calendar/events"), event);
            result = dataAbilityHelper.insert(Uri.parse("dataability:///com.android.calendar/events"), event);
            LogUtil.e("!!!!!!!!!!" + result);
            //事件提醒的设定
            ValuesBucket values = new ValuesBucket();
            values.putInteger("event_id", result);
//        values.put(CalendarContract.Reminders.MINUTES, previousDate * 24 * 60);// 提前previousDate天有提醒
            values.putInteger("minutes", 1);// 提前1分钟提醒
            values.putInteger("method", 1);
            int uri = dataAbilityHelper.insert(Uri.parse("dataability:///com.android.calendar/reminders"), values);
            LogUtil.e("uri!!!!!!!!!!" + uri);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("**********" + e.getMessage());
        }
        return 0;
    }

    /**
     * 删除日历事件
     */
    public static void deleteCalendarEvent(Context context, String title) {

        if (context == null) {

            return;
        }
        DataAbilityHelper dataAbilityHelper = DataAbilityHelper.creator(context);
        ResultSet resultSet = null;
        try {
            resultSet = dataAbilityHelper.query(Uri.parse(CALENDER_EVENT_URL), null, null);

            if (resultSet == null) { //查询返回空值
                return;
            }
            if (resultSet.getColumnCount() > 0) {
                //遍历所有事件，找到title跟需要查询的title一样的项

                while (resultSet.goToNextRow()){

                    String eventTitle = resultSet.getString(resultSet.getColumnIndexForName("title"));
                    if (!TextTool.isNullOrEmpty(eventTitle) && title.equals(eventTitle)) {
                        int id = resultSet.getInt(resultSet.getColumnIndexForName("_id"));//取得id
                        Uri deleteUri = Uri.parse(CALENDER_EVENT_URL).makeBuilder().appendEncodedPath(String.valueOf(id)).build();
                        int rows = dataAbilityHelper.delete(deleteUri, null);
                        if (rows == -1) { //事件删除失败
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    /**
     * 查询日历事件
     */
    private static boolean isSelectCalendarEvent(Context context, String title) {
        boolean isSelect = false;//是否查询到数据
        if (context == null) {
            return false;
        }
        DataAbilityHelper dataAbilityHelper = DataAbilityHelper.creator(context);
        ResultSet resultSet = null;
        try {
            resultSet = dataAbilityHelper.query(Uri.parse(CALENDER_EVENT_URL), null, null);

            if (resultSet == null) { //查询返回空值
                return false;
            }
            if (resultSet.getColumnCount() > 0) {
                //遍历所有事件，找到title跟需要查询的title一样的项
                for (resultSet.goToFirstRow(); !resultSet.isAtLastRow(); resultSet.goToNextRow()) {
                    String eventTitle = resultSet.getString(resultSet.getColumnIndexForName("title"));
                    if (!TextTool.isNullOrEmpty(title) && title.equals(eventTitle)) {
                        isSelect = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return isSelect;
    }
}

