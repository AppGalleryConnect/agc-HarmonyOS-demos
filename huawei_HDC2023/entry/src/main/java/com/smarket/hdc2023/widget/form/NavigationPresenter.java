package com.smarket.hdc2023.widget.form;

import com.smarket.hdc2023.widget.form.bean.LocationDataBean;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.app.Context;
import ohos.utils.net.Uri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 2023/8/29 14:23
 */
public class NavigationPresenter {
    public static final String BAIDU_MAP_PACKAGE_NAME = "com.baidu.BaiduMap";
    public static final String GAUD_MAP_PACKAGE_NAME = "com.autonavi.minimap";
    public static final String TENCENT_MAP_PACKAGE_NAME = "com.tencent.map";
    private static InstallHintsDialog installHintsDialog;

    /**
     * 开始导航
     *
     * @param context  上下文
     * @param location 地址
     */
    public static void openNavigation(Context context, String location) {
        List<String> mapInstallList = getMapInstallList(context);
        LocationDataBean locationDataBean = null;//inverseGeocoding(location);
        if (locationDataBean == null) {
            TextDialogUtil.show(context, "位置解析失败!\n请检查位置数据");
            if (context instanceof TransparentAbility) {
                context.terminateAbility();
            }
            return;
        }
        if (mapInstallList.size() == 0) {
            TextDialogUtil.show(context, "未检测到地图应用\n请前往应用市场安装");
            if (context instanceof TransparentAbility) {
                context.terminateAbility();
            }
        } else {
            new MapSelectionDialog(context, locationDataBean, mapInstallList);
        }
    }


    /**
     * 写死坐标(火星坐标)
     *
     * @return 位置
     */
    public static LocationDataBean getLocation() {
        LocationDataBean locationDataBean = new LocationDataBean();
        locationDataBean.setAddress("天安云谷");
        locationDataBean.setLatitude(22.658778);
        locationDataBean.setLongitude(114.068443);
        return locationDataBean;
    }

    /**
     * 获取设备上安装的地图应用
     *
     * @param context 上下文
     * @return 应用列表
     */
    public static List<String> getMapInstallList(Context context) {
        List<String> mapInstallList = new ArrayList<>();
        if (AppIsInstall.isInstall(context, BAIDU_MAP_PACKAGE_NAME)) {
            mapInstallList.add(BAIDU_MAP_PACKAGE_NAME);
        }
        if (AppIsInstall.isInstall(context, GAUD_MAP_PACKAGE_NAME)) {
            mapInstallList.add(GAUD_MAP_PACKAGE_NAME);
        }
        if (AppIsInstall.isInstall(context, TENCENT_MAP_PACKAGE_NAME)) {
            mapInstallList.add(TENCENT_MAP_PACKAGE_NAME);
        }
        return mapInstallList;
    }

    public static void baiduMap(Context context, double latitude, double longitude) {
        //百度
        Intent mapIntent = new Intent();
        String format = String.format("baidumap://map/direction?destination=%s" +
                ",%s&coord_type=gcj02&src=" + context.getBundleName(), latitude, longitude);
        Uri uri = Uri.parse(format);
        Operation operation = new Intent.OperationBuilder()
                .withFlags(Intent.FLAG_ABILITY_NEW_MISSION)
                .withUri(uri)
                .build();
        mapIntent.setOperation(operation);
        context.startAbility(mapIntent, 0);
    }

    public static void baiduMap(Context context, String address) {
        //百度
        Intent mapIntent = new Intent();
        String format = "baidumap://map/direction?destination=" + address
                + "&coord_type=gcj02&src=" + context.getBundleName();
        Uri uri = Uri.parse(format);
        Operation operation = new Intent.OperationBuilder()
                .withFlags(Intent.FLAG_ABILITY_NEW_MISSION)
                .withUri(uri)
                .build();
        mapIntent.setOperation(operation);
        context.startAbility(mapIntent, 0);
    }

    public static void tencentMap(Context context, double latitude, double longitude) {
        //腾讯
        Intent mapIntent = new Intent();
        String format = String.format("qqmap://map/routeplan?type=drive&fromcoord=CurrentLocation" +
                "&tocoord=%s,%s&referer=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77", latitude, longitude);
        Uri uri = Uri.parse(format);
        Operation operation = new Intent.OperationBuilder()
                .withFlags(Intent.FLAG_ABILITY_NEW_MISSION)
                .withUri(uri)
                .build();
        mapIntent.setOperation(operation);
        context.startAbility(mapIntent, 0);
    }

    public static void gaudMap(Context context, double latitude, double longitude) {
        //高德
        Intent mapIntent = new Intent();
        String format = String.format("amapuri://route/plan/?dlat=%s&dlon=%s&dev=0&t=0", latitude, longitude);
        Uri uri = Uri.parse(format);
        Operation operation = new Intent.OperationBuilder()
                .withFlags(Intent.FLAG_ABILITY_NEW_MISSION)
                .withUri(uri)
                .build();
        mapIntent.setOperation(operation);
        context.startAbility(mapIntent, 0);
    }


    public static void gaudMap(Context context, Uri uri) {
        //高德
        boolean install = AppIsInstall.isInstall(context, GAUD_MAP_PACKAGE_NAME);
        if (install) {
            Intent mapIntent = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withUri(uri)
                    .build();
            mapIntent.setOperation(operation);
            context.startAbility(mapIntent, 0);
        } else {
            if (installHintsDialog == null || !installHintsDialog.isShowing()) {
                installHintsDialog = new InstallHintsDialog(context, GAUD_MAP_PACKAGE_NAME);
            }
        }
    }

    /**
     * 打开腾讯地图
     *
     * @param context 上下文
     * @param uri
     */
    public static void tencentMap(Context context, Uri uri) {
        boolean install = AppIsInstall.isInstall(context, TENCENT_MAP_PACKAGE_NAME);
        if (install) {
            Intent mapIntent = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withUri(uri)
                    .build();
            mapIntent.setOperation(operation);
            context.startAbility(mapIntent, 0);
        } else {
            new InstallHintsDialog(context, TENCENT_MAP_PACKAGE_NAME);
        }
    }
}
