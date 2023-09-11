package com.smarket.hdc2023.widget.form;

import com.smarket.hdc2023.MyApplication;
import com.smarket.hdc2023.ResourceTable;
import com.smarket.hdc2023.common.Config;
import ohos.rpc.MessageParcel;
import ohos.utils.zson.ZSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import static com.smarket.hdc2023.common.Config.*;

public class HttpUtil {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String URI = "uri";
    public static final String CONTENT_TYPE_ENCODED = "application/x-www-form-urlencoded;charset=UTF-8";
    public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
    public static final String REQUEST_METHOD_KEY = "requestMethod";
    public static final String BODY = "body";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";

    public static CompletableFuture<String> httpGet(ZSONObject zsonObject) {
        zsonObject.put(REQUEST_METHOD_KEY, GET);
        return httpRequest(zsonObject);
    }

    public static CompletableFuture<String> httpPost(ZSONObject zsonObject) {
        zsonObject.put(REQUEST_METHOD_KEY, POST);
        return httpRequest(zsonObject);
    }

    public static CompletableFuture<String> httpRequest(ZSONObject zsonObject) {
        return CompletableFuture.supplyAsync(() -> {
            InputStream stream;
            HttpURLConnection conn = null;
            try {
                URL url = new URL(MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID + "/seminar/" + MyApplication.getApplication().getResourceString(ResourceTable.String_SEMINAR_ID) + "/subSeminar/list.json");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(6000);
                conn.addRequestProperty(CONTENT_TYPE, zsonObject.getString(CONTENT_TYPE));
                String requestType = zsonObject.getString(REQUEST_METHOD_KEY);
                conn.setRequestMethod(requestType);
                boolean isPost = requestType.equals("POST");
                conn.setDoInput(true);
                conn.setDoOutput(isPost);
                conn.setUseCaches(false);
                conn.connect();
                if (isPost) {
                    String body = zsonObject.getString("body");
                    OutputStream out = conn.getOutputStream();
                    out.write(body.getBytes());
                    out.flush();
                    out.close();
                }
                int responseCode = conn.getResponseCode();
                String data;
                if (responseCode == 200) {
                    stream = conn.getInputStream();
                    data = StreamUtil.readInputStreams(stream);
                    //刷新我预约的议程ID
                    com.smarket.hdc2023.http.HttpUtil.getReserveIdlist(MessageParcel.create());
                } else {
                    stream = conn.getErrorStream();
                    data = "error";
                }
                return data;
            } catch (Exception e) {
                return "error";
            } finally {
                assert conn != null;
                conn.disconnect();
            }
        });
    }
}
