package com.smarket.hdc2023.widget.form.bean;

/**
 * 位置信息实体类
 */
public class LocationDataBean {
    //地址
    private String address;

    //经度
    private double longitude;

    //纬度
    private double latitude;

    public LocationDataBean() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
