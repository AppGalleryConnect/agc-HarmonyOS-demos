package com.smarket.hdc2023.help;

import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.analytics.HiAnalyticsTools;
import com.huawei.hms.analytics.type.HAEventType;
import com.huawei.hms.analytics.type.HAParamType;
import com.huawei.hms.analytics.type.HAReportPolicy;
import ohos.aafwk.ability.Ability;
import ohos.utils.PacMap;

import java.util.HashSet;
import java.util.Set;

/**
 * 华为统计工具类
 */
public class HiAnalyticsUtil {
    private static HiAnalyticsInstance instance;

    /**
     * 初始化华为统计
     *
     * @param ability ability
     */
    public static void initInstance(Ability ability) {
        if (instance == null) {
            HiAnalyticsTools.enableLog();
            instance = HiAnalytics.getInstance(ability);
            // 创建应用切后台上报策略
            HAReportPolicy moveBackgroundPolicy = HAReportPolicy.ON_MOVE_BACKGROUND_POLICY;
            // 创建定时上报策略
            HAReportPolicy scheduledTimePolicy = HAReportPolicy.ON_SCHEDULED_TIME_POLICY;
            // 设置定时上报时间周期为600秒
            scheduledTimePolicy.setThreshold(60);
            Set<HAReportPolicy> reportPolicies = new HashSet<>();
            // 添加定时上报策略和应用切后台上报策略
            reportPolicies.add(scheduledTimePolicy);
            reportPolicies.add(moveBackgroundPolicy);
            // 设置应用切后台上报策略和定时上报策略
            instance.setReportPolicies(reportPolicies);
        }
    }

    /**
     * 上报用户浏览页面事件
     *
     * @param ability  ability
     * @param pageName 页面名称
     * @param duration 浏览时长(毫秒)
     */
    public static void addPageViewEvent(Ability ability, String pageName, int duration) {
        initInstance(ability);
        PacMap pacMapPre = new PacMap();
        pacMapPre.putString(HAParamType.PAGENAME, pageName);
        pacMapPre.putIntValue(HAParamType.DURATION, duration);
        pacMapPre.putString(HAParamType.USERID, instance.getAAID());
        instance.onEvent(HAEventType.PAGEVIEW, pacMapPre);
    }

    /**
     * 上报用户点击功能按钮事件
     *
     * @param ability    ability
     * @param buttonName 按钮名称
     */
    public static void addModularClickEvent(Ability ability, String buttonName) {
        initInstance(ability);
        PacMap pacMapPre = new PacMap();
        pacMapPre.putString(HAParamType.BUTTONNAME, buttonName);
        pacMapPre.putString(HAParamType.USERID, instance.getAAID());
        instance.onEvent(HAEventType.MODULARCLICK, pacMapPre);
    }
}