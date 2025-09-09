/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2025-2025. All rights reserved.
 */

package com.demo.rs;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * 功能：本Demo展示了如何调用ReportApi获取目标数据
 * 原理：通过getToken()获取验证token，将token、过滤条件传入getReport()中获取目标数据
 */
public class ReportApiDemo {
    private static final Logger LOG = LoggerFactory.getLogger(ReportApiDemo.class);

    private String clientId = "174*****992"; // clientId值需要替换

    private String clientSecret = "1AC*****EAB"; // clientSecret值需要替换

    public static void main(String[] args) {
        ReportApiDemo demo = new ReportApiDemo();
        String appId = "576*****661";  // appId值需要替换
        HashMap<String, String> params = new HashMap<>();
        params.put("startTime", "20250813");
        params.put("endTime", "20250819");
        params.put("chainStartTime", "20250806");
        params.put("chainEndTime", "20250812");
        params.put("language", "zh-CN");
        params.put("groupBy", "date");
        params.put("timeType", "day");
        params.put("exportType", "EXCEL");
        String result = demo.getReportFileUrl(demo.getToken(), appId, params);
        System.out.println(result);
    }

    /**
     * 构造http post请求内容
     *
     * @return http post请求
     */
    public HttpPost buildHttpPost() {
        String url = "https://" + Domain.CHINA.getDomainName() + "/api/oauth2/v1/token"; // domain按需替换
        JSONObject keyString = new JSONObject();
        keyString.put("client_id", clientId);
        keyString.put("client_secret", clientSecret);
        keyString.put("grant_type", "client_credentials");
        StringEntity posEntity = new StringEntity(keyString.toString(), StandardCharsets.UTF_8);
        posEntity.setContentEncoding("UTF-8");
        posEntity.setContentType("application/json");
        HttpPost post = new HttpPost(url);
        post.setEntity(posEntity);
        return post;
    }

    /**
     * 获取用户鉴权token
     *
     * @return 返回获得的token
     */
    public String getToken() {
        HttpPost post = buildHttpPost();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpResponse response = httpClient.execute(post);
            if (response == null || response.getStatusLine() == null) {
                LOG.error("Authorization service response null or statusLine is null");
                return null;
            }
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                LOG.error("Authorization Service getStatusCode is not 200.");
                return null;
            }

            String entity = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (entity == null) {
                LOG.error("Authorization Service entity is null");
                return null;
            }
            return JSON.parseObject(entity).getString("access_token");
        } catch (Exception e) {
            LOG.error("Authorization Service request error", e);
            return null;
        } finally {
            post.releaseConnection();
        }
    }

    /**
     * 设置http get请求内容
     *
     * @param token accessToken
     * @param appId 应用ID
     * @param params 请求的其他参数
     * @return 返回一个http get请求
     */
    public HttpGet buildHttpGet(String token, String appId, HashMap<String, String> params) {
        StringBuilder url = new StringBuilder();
        url.append("https://").append(Domain.CHINA.getDomainName()).append(
            "/api/report/harmony-report/v1/harmony/appDownloadAnalysisExport/").append(appId).append("?");
        params.forEach((k, v) -> url.append("&").append(k).append("=").append(v));
        HttpGet httpGet = new HttpGet(url.toString());
        httpGet.setHeader("Authorization", "Bearer " + token);
        httpGet.setHeader("client_id", clientId); // client_id值需要替换
        httpGet.setHeader("appId", appId);
        return httpGet;
    }

    /**
     * 获取报表数据
     *
     * @param token accessToken
     * @param appId 应用ID
     * @param params 请求的其他参数
     * @return 返回报表数据文件的下载url
     */
    public String getReportFileUrl(String token, String appId, HashMap<String, String> params) {
        HttpGet httpGet = buildHttpGet(token, appId, params);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse == null || httpResponse.getStatusLine() == null) {
                LOG.error("AppDownloadAnalysisExport service response or statusLine is null");
                return null;
            }
            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                LOG.error("AppDownloadAnalysisExport Service getStatusCode is not 200.");
                return null;
            }

            String entity = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            if (entity == null) {
                LOG.error("AppDownloadAnalysisExport Service entity is null");
                return null;
            }
            return JSON.parseObject(entity).getString("fileURL");
        } catch (Exception e) {
            LOG.error("Failed to get Report");
            return null;
        } finally {
            httpGet.releaseConnection();
        }
    }

    /**
     * Domain包含四个站点的域值字段
     */
    private enum Domain {
        CHINA("connect-api.cloud.huawei.com"),
        EUROPE("connect-api-dre.cloud"),
        ASIA("connect-api-dra.cloud"),
        RUSSIA("connect-api-drru.cloud");

        private final String domainName;

        Domain(String domainName) {
            this.domainName = domainName;
        }

        public String getDomainName() {
            return domainName;
        }
    }
}
