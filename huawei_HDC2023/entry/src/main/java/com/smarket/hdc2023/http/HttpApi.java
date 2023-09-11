package com.smarket.hdc2023.http;

import com.smarket.hdc2023.bean.HttpBean;
import com.smarket.hdc2023.help.CommonUtil;
import com.smarket.hdc2023.help.LogUtil;
import ohos.utils.zson.ZSONObject;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpApi {

    public static HttpBean get(String urlString) {
// 通过openConnection来获取URLConnection
        HttpsURLConnection connection = null;
        HttpBean httpBean = new HttpBean();
        LogUtil.e("请求URL：" + urlString);
        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            if (urlConnection instanceof HttpsURLConnection) {
                connection = (HttpsURLConnection) urlConnection;
            }
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                byte[] b = CommonUtil.getBytesFromInputStream(connection.getInputStream());
                String back = new String(b).replace("\r", "").replace("\n", "");
                httpBean.setStratus(0);
                httpBean.setData(back);
//                LogUtil.i("GET返回结果：" + back);
            } else {
                httpBean.setStratus(connection.getResponseCode());
                LogUtil.e("GET请求状态码：" + connection.getResponseCode());
            }
            // 之后可进行url的其他操作
        } catch (IOException e) {
            LogUtil.e("exception happened." + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            return httpBean;
        }

    }

    public static String get302(String urlString) {
// 通过openConnection来获取URLConnection
        HttpsURLConnection connection = null;
        String newUri = "";
        LogUtil.e("302请求URL：" + urlString);
        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            if (urlConnection instanceof HttpsURLConnection) {
                connection = (HttpsURLConnection) urlConnection;
            }
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_MOVED_TEMP) {
                newUri = connection.getHeaderField("location");
                if (newUri.startsWith("http://")) {
                    newUri =   newUri.replace("http://","https://");
                }
            }
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                newUri = connection.getURL().toString();
                if (newUri.startsWith("http://")) {
                    newUri =   newUri.replace("http://","https://");
                }
            }


            // 之后可进行url的其他操作
        } catch (IOException e) {
            LogUtil.e("exception happened." + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            return newUri;
        }

    }

    public static HttpBean post(String urlString, String params) {
        HttpsURLConnection connection = null;
        HttpBean httpBean = new HttpBean();
        LogUtil.e("请求URL：" + urlString);
        LogUtil.e("POST请求：" + params);
        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            if (urlConnection instanceof HttpsURLConnection) {
                connection = (HttpsURLConnection) urlConnection;
            }
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();

            CommonUtil.bytesToOutputStream(params.getBytes(), connection.getOutputStream());
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                byte[] b = CommonUtil.getBytesFromInputStream(connection.getInputStream());
                String back = new String(b);
                httpBean.setData(back);
                httpBean.setStratus(0);
                LogUtil.i("POST返回结果：" + ZSONObject.toZSONString(back));
            } else {
                httpBean.setStratus(connection.getResponseCode());
                LogUtil.e("POST请求状态码：" + connection.getResponseCode());
            }
            // 之后可进行url的其他操作
        } catch (IOException e) {
            LogUtil.e("exception happened." + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            return httpBean;
        }
    }

    public static HttpBean post2(String urlString,  String params) {
        HttpsURLConnection connection = null;
        HttpBean httpBean = new HttpBean();
        LogUtil.e("请求URL：" + urlString);
        LogUtil.e("POST请求：" + params);
        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            if (urlConnection instanceof HttpsURLConnection) {
                connection = (HttpsURLConnection) urlConnection;
            }
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            connection.connect();

            CommonUtil.bytesToOutputStream(params.getBytes(), connection.getOutputStream());
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                byte[] b = CommonUtil.getBytesFromInputStream(connection.getInputStream());
                String back = new String(b);
                httpBean.setData(back);
                httpBean.setStratus(0);
                LogUtil.i("POST返回结果：" + ZSONObject.toZSONString(back));
            } else {
                httpBean.setStratus(connection.getResponseCode());
                LogUtil.e("POST请求状态码：" + connection.getResponseCode());
            }
            // 之后可进行url的其他操作
        } catch (IOException e) {
            LogUtil.e("exception happened." + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            return httpBean;
        }
    }
}
