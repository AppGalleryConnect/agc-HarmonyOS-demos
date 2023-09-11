package com.smarket.hdc2023.widget.form;

import com.smarket.hdc2023.bean.ServerTime;
import com.smarket.hdc2023.widget.form.bean.AgendaBean;
import ohos.agp.utils.TextTool;
import ohos.app.Context;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;
import ohos.hiviewdfx.HiLog;
import ohos.utils.zson.ZSONArray;
import ohos.utils.zson.ZSONObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 轻量级存储工具类
 */
public class PreferencesUtils {
    private static volatile Preferences preferences;
    private static Context myContext;

    private PreferencesUtils() {
    }

    public static void init(Context context) {
        if (myContext == null) {
            myContext = context;
        }
        if (preferences == null) {
            synchronized (PreferencesUtils.class) {
                if (preferences == null) {
                    String fileName = "myPreferences"; // fileName表示文件名，其取值不能为空，也不能包含路径，默认存储目录可以通过context.getPreferencesDir()获取。
                    preferences = new DatabaseHelper(context).getPreferences(fileName);
                }
            }
        }
    }

    public static Preferences getInstance() {
        return preferences;
    }

    public static void saveAgendaData(@NotNull List<AgendaBean> agendaBeans) {
        preferences.putInt("agendaDataCount", agendaBeans.size());
        String data = ZSONObject.toZSONString(agendaBeans);
        Set<String> dataSet = new HashSet<>();
        dataSet.add(data);
        preferences.putStringSet("agendaData", dataSet);
        preferences.flush();
    }

    public static List<AgendaBean> getAgendaData() {
        Set<String> agendaData = preferences.getStringSet("agendaData", new HashSet<>());
        if (agendaData != null && agendaData.size() > 0) {
            for (String agendaDataString : agendaData) {
                return agendaData.isEmpty() ? null : ZSONArray.stringToClassList(agendaDataString, AgendaBean.class);
            }
        } else {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    public static int getAgendaDataCount() {
        return preferences.getInt("agendaDataCount", 0);
    }

    /**
     * 获取议程2x4卡片议程组数量
     *
     * @return 议程2x4卡片议程组数量
     */
    public static int getAgendaDataCount2() {
        return preferences.getInt("agendaDataCount2", 0);
    }

    /**
     * 缓存议程2x4卡片议程组数量
     *
     * @param agenda 议程2x4卡片议程组数量
     */
    public static void saveAgendaDataCount2(@NotNull int agenda) {
        preferences.putInt("agendaDataCount2", agenda);
        preferences.flush();
    }


    /**
     * 缓存议程接口返回值
     *
     * @param agendaResult     议程接口返回值
     * @param agendaResultTime 议程接口时间
     */
    public static void saveAgendaResult(@NotNull String agendaResult, @NotNull long agendaResultTime) {
        Set<String> dataSet = new HashSet<>();
        dataSet.add(agendaResult);
        preferences.putStringSet("agendaResult", dataSet);
        preferences.putLong("agendaResultTime", agendaResultTime);
        preferences.flush();
    }

    /**
     * 获取议程接口返回值缓存
     *
     * @return 议程接口返回值缓存
     */
    public static String getAgendaResult() {
        String res = null;
        String agendaResult = "";
        Set<String> agendaResultSet = preferences.getStringSet("agendaResult", new HashSet<>());
        if (agendaResultSet != null && agendaResultSet.size() > 0) {
            for (String agendaResultString : agendaResultSet) {
                agendaResult = agendaResultString;
            }
        }
        long agendaResultTime = preferences.getLong("agendaResultTime", 0);
        if (agendaResultTime > 0 && !TextTool.isNullOrEmpty(agendaResult)) {
            //获取当前时间
            ServerTime serverTime = com.smarket.hdc2023.http.HttpUtil.getServerTime();
            if (serverTime.getServerTime() - agendaResultTime < 25 * 60) {
                res = agendaResult;
            }
        }
        return res;
    }
}