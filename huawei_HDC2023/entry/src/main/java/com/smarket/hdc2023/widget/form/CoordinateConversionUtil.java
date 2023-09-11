package com.smarket.hdc2023.widget.form;


import com.smarket.hdc2023.widget.form.bean.LocationDataBean;

/**
 * 坐标系转换工具类
 *
 * @since 2023/8/6 10:14
 */
public class CoordinateConversionUtil {
    // 圆周率
    private static double PI = 3.1415926535897932384626;
    //克拉索夫斯基椭球是克拉索夫斯基于1940年提出的地球椭球a，其长半径为6378245米，短半径为6356863米
    private static double a = 6378245.0;
    //克拉索夫斯基椭球参数第一偏心率平方
    private static double ee = 0.00669342162296594323;
    private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    private static boolean outOfChina(double lat, double lon) {
        return lon < 72.004 || lon > 137.8347 || lat < 0.8293 || lat > 55.8271;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }


    /**
     * 地球坐标转换为火星坐标
     * World Geodetic System ==> Mars Geodetic System
     *
     * @param wgLat 地球坐标
     * @param wgLon mglat,mglon 火星坐标
     */
    public static LocationDataBean transform2Mars(double wgLat, double wgLon) {
        LocationDataBean bean = new LocationDataBean();
        double mgLat = 0;
        double mgLon = 0;
        if (outOfChina(wgLat, wgLon)) {
            mgLat = wgLat;
            mgLon = wgLon;
            bean.setLatitude(mgLat);
            bean.setLongitude(mgLon);
            return bean;
        }
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * PI);
        mgLat = wgLat + dLat;
        mgLon = wgLon + dLon;
        bean.setLatitude(mgLat);
        bean.setLongitude(mgLon);
        return bean;
    }
}
