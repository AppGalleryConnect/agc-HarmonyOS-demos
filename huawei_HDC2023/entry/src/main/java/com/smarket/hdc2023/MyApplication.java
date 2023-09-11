/*
 * Copyright 2023. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.smarket.hdc2023;

import com.huawei.agconnect.AGConnectInstance;
import com.huawei.hms.jsb.adapter.har.bridge.HmsBridge;
import com.smarket.hdc2023.bean.*;
import com.smarket.hdc2023.common.AES256;
import com.smarket.hdc2023.common.Config;
import com.smarket.hdc2023.help.LogUtil;
import com.smarket.hdc2023.http.HttpApi;
import com.smarket.hdc2023.http.HttpUtil;
import com.smarket.hdc2023.vo.SubSeminarBeanVO;
import com.smarket.hdc2023.widget.form.PreferencesUtils;
import com.smarket.hdc2023.widget.form.database.DatabaseUtils;
import ohos.aafwk.ability.AbilityPackage;
import ohos.agp.utils.TextTool;
import ohos.data.DatabaseHelper;
import ohos.data.distributed.common.KvManagerConfig;
import ohos.data.distributed.common.KvManagerFactory;
import ohos.data.preferences.Preferences;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.utils.zson.ZSONArray;
import ohos.utils.zson.ZSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.smarket.hdc2023.common.Config.SEMINAR_ID;
import static com.smarket.hdc2023.common.Config.TENANT_ID;

public class MyApplication extends AbilityPackage {
    public static ThreadPoolExecutor executorService = new ThreadPoolExecutor(10, 20,
            60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(20));
    private static Preferences preferences;
    private static MyApplication application;

    public static MyApplication getApplication() {

        if (application == null) {
            application = new MyApplication();
        }

        return application;
    }
    public String getResourceString(int resourceID ){
        try {
            return getResourceManager().getElement(resourceID).getString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String sess = "";
    public static List<SubSeminarBeanVO> searchBackList = new ArrayList<>();
    public static List<SubSeminarBean> backList1 = new ArrayList<>();
    public static List<SubSeminarBean> backList2 = new ArrayList<>();
    public static List<SubSeminarBean> backList3 = new ArrayList<>();
    public static List<HdBean> hdBeans = new ArrayList<>();
    public static List<HdBean> tHBeans = new ArrayList<>();
    public static String pageStr = "";
    public static SubSeminarBean agendaDetail = null;
    public static SubSeminarBean agendaDetailTH = null;
    public static String agendaDetailId = "";
    private static String deviceID = "";

    public static String getDeviceID() {
        if (TextTool.isNullOrEmpty(deviceID)) {
            deviceID = "HarmonyOSHDC202308050607";//KvManagerFactory.getInstance().createKvManager(new KvManagerConfig(getApplication())).getLocalDeviceInfo().getId();

        }
        return deviceID;

    }

    /**
     * 获取session，不校验有效
     * @return session
     */
    public static String getSess2() {
        if (TextTool.isNullOrEmpty(sess)) {
            try {
                String getsess = getPreferences().getString("sess", "");
                if(!TextTool.isNullOrEmpty(getsess)) {
                    sess = AES256.aesDecrypt(getsess, getDeviceID());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sess;
    }

    public static String getSess() {
        if (TextTool.isNullOrEmpty(sess)) {
            try {
                String getsess = getPreferences().getString("sess", "");
                if(!TextTool.isNullOrEmpty(getsess)) {
                    sess = AES256.aesDecrypt(getsess, getDeviceID());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
       /* if(!TextTool.isNullOrEmpty(sess)){
            //检查sess的有效性
            String urlStr = Config.HOST + MyApplication.getApplication().getResourceString(ResourceTable.String_apiSeminarContactsGet);
            Map<String, Object> map = new HashMap<>();
            map.put("tenantId", TENANT_ID);
            map.put("seminarId", SEMINAR_ID);
            map.put("sess", sess);
            map.put("openId", "");
            map.put("globalUserId", MyApplication.getGlobalUserId());
            String params = ZSONObject.toZSONString(map);
            HttpBean httpBean = HttpApi.post(urlStr, params);
            if (httpBean.getStratus() == 0) {

            }else{
                MyApplication.setSess("");
                sess = "";
            }
        }*/
        return sess;
    }

    public static void setSess(String session) {
        try {
            getPreferences().putString("sess", AES256.aesEncrypt(session,getDeviceID())).flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        sess = session;
    }

    public static void outLogin() {
        MyApplication.setSess("");
        MyApplication.setidlist("");
        MyApplication.setTechHouridlist("");
        MyApplication.setISNormalTicket(true);
        MyApplication.getPreferences().putString("name", "").flush();
        MyApplication.getPreferences().putString("qrCode", "").flush();
        MyApplication.getPreferences().putString("enterprise", "").flush();
        MyApplication.getPreferences().putString("i_ticketType", "").flush();
        MyApplication.getPreferences().putString("ticketName", "").flush();
        MyApplication.getPreferences().putString("i_ticketName", "").flush();
        MyApplication.getPreferences().putString("checkInStatus", "").flush();
        MyApplication.getPreferences().putString("giftSpecialRight", "").flush();
    }

    public static Params getPage() {
        String page = getPreferences().getString("page", "{\"page\":\"main\"}");
        return ZSONObject.stringToClass(page, Params.class);
    }

    public static void setPage(Params params) {
        getPreferences().putString("page", ZSONObject.toZSONString(params)).flush();
    }

    public static boolean getISNormalTicket() {
        return getPreferences().getBoolean("isNormalTicket", true);
    }

    public static void setISNormalTicket(boolean isNormalTicket) {
        getPreferences().putBoolean("isNormalTicket", isNormalTicket).flush();
    }

    public static void checkGlobalUserId() {
        String globalUserId = getGlobalUserId();
        if (TextTool.isNullOrEmpty(globalUserId)) {
            MyApplication.executorService.execute(() -> HttpUtil.getUserId());
        }

    }

    public static String getGlobalUserId() {
        return getPreferences().getString("globalUserId", "");
    }

    public static List<String> getidlist() {
        String idList = null;
        try {
            String getidlist = getPreferences().getString("getidlist", "");
            if(!TextTool.isNullOrEmpty(getidlist)){
                idList = AES256.aesDecrypt(getidlist,getDeviceID());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> idListNew = new ArrayList<>();
        if (!TextTool.isNullOrEmpty(idList)) {
            idListNew = ZSONArray.stringToZSONArray(idList).toJavaList(String.class);
        }
        return idListNew;
    }


    public static void setidlist(String idList) {
        try {
            getPreferences().putString("getidlist", AES256.aesEncrypt(idList,getDeviceID())).flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getTechHouridlist() {
        String idList = null;
        try {
            String getTechHouridlist = getPreferences().getString("getTechHouridlist", "");
            if(!TextTool.isNullOrEmpty(getTechHouridlist)) {
                idList = AES256.aesDecrypt(getTechHouridlist, getDeviceID());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> idListNew = new ArrayList<>();
        if (!TextTool.isNullOrEmpty(idList)) {
            idListNew = ZSONArray.stringToZSONArray(idList).toJavaList(String.class);
        }
        return idListNew;
    }


    public static void setTechHouridlist(String idList) {
        try {
            getPreferences().putString("getTechHouridlist", AES256.aesEncrypt(idList,getDeviceID())).flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    public static String getConceal() {
        return getPreferences().getString("hasAgreeConceal", "1");
    }

    public static void setAgreeConceal() {
        getPreferences().putString("hasAgreeConceal", "true").flush();
    }

    public static UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.qrCode = getPreferences().getString("qrCode", "");
        userInfo.name = getPreferences().getString("name", "");
        userInfo.enterprise = getPreferences().getString("enterprise", "");
        userInfo.i_ticketType = getPreferences().getString("i_ticketType", "");
        userInfo.ticketName = getPreferences().getString("ticketName", "");
        userInfo.i_ticketName = getPreferences().getString("i_ticketName", "");
        userInfo.checkInStatus = getPreferences().getString("checkInStatus", "");
        userInfo.giftSpecialRight = getPreferences().getString("giftSpecialRight", "");
        LogUtil.e(ZSONObject.toZSONString(userInfo));
        return userInfo;
    }
    private HmsBridge mHmsBridge;
    @Override
    public void onInitialize() {
        // 初始化JSB
        mHmsBridge = HmsBridge.getInstance();
        mHmsBridge.initBridge(this);
        super.onInitialize();

        //初始化华为统计
        try {
            AGConnectInstance.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DatabaseUtils.init(this);
        PreferencesUtils.init(this);
        application = this;
    }
    @Override
    public void onEnd() {
        // 结束JSB
        mHmsBridge.destoryBridge();
        super.onEnd();
    }
    public static Preferences getPreferences() {
        if (preferences == null) {
            DatabaseHelper databaseHelper = new DatabaseHelper(application);
            preferences = databaseHelper.getPreferences("hdc2023.xml");
        }
        return preferences;
    }

}
