package com.smarket.hdc2023;

import com.smarket.hdc2023.help.LogUtil;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.ace.ability.AceAbility;
import ohos.agp.utils.TextTool;
import ohos.agp.window.service.WindowManager;
import ohos.global.configuration.Configuration;

public class BaseAbility extends AceAbility {
    private static String page = "";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        getWindow().addFlags(WindowManager.LayoutConfig.MARK_TRANSLUCENT_STATUS);
        BatteryInternalAbility.getInstance().register(this);
        MyApplication.checkGlobalUserId();
    }


    @Override
    public void onStop() {
        super.onStop();
        BatteryInternalAbility.getInstance().deregister();
    }

    @Override
    public boolean onSaveData(IntentParams saveData) {

        return super.onSaveData(saveData);
    }

    @Override
    public void onConfigurationUpdated(Configuration newConfig) {
        switch (MyApplication.pageStr) {
            case "嘉宾列表":
                page = "pages/guest/guest-list";
                break;
            case "嘉宾详情":
                page = "pages/guest/guest-detail";
                break;
            case "议程列表":
                page = "pages/agenda/agenda";
                break;
            case "议程详情":
                page = "pages/agenda-detail/agenda-detail";
                break;
            case "大会介绍":
                page = "pages/help-about/help-about";
                break;
            case "参会助手":
                page = "pages/help/help";
                break;
            case "登录":
                page = "pages/loginAccredit/loginAccredit";
                break;
            case "我的议程":
                page = "pages/my-agenda/my-agenda";
                break;
            case "我的门票":
                page = "pages/my-ticket/my-ticket";
                break;
            case "我的":
                page = "pages/my/my";
                break;
            case "班车信息":
            case "自助交通":
            case "大会餐饮":
            case "酒店推荐":
            case "周边推荐":
            case "Q&A":
            case "大会地址":
                page = "pages/help-detail/help-detail";
                break;
            default:
                page = "pages/index/index";
                break;

        }
        this.restart();

        super.onConfigurationUpdated(newConfig);
    }

    public void initView(String pageStr, IntentParams params) {
        setInstanceName("default");
        switch (MyApplication.pageStr) {
            case "班车信息":
                params = new IntentParams();
                params.setParam("isSelect", 1);
                break;
            case "自助交通":
                params = new IntentParams();
                params.setParam("isSelect", 2);
                break;
            case "大会餐饮":
                params = new IntentParams();
                params.setParam("isSelect", 3);
                break;
            case "酒店推荐":
                params = new IntentParams();
                params.setParam("isSelect", 4);
                break;
            case "周边推荐":
                params = new IntentParams();
                params.setParam("isSelect", 5);
                break;
            case "Q&A":
                params = new IntentParams();
                params.setParam("isSelect", 6);
                break;
            case "大会地址":
                params = new IntentParams();
                params.setParam("isSelect", 0);
                break;
            case "议程详情":
                String idStr = "";
                if (params != null) {
                    try {
                        idStr = params.getParam("id").toString();
                        MyApplication.agendaDetailId = idStr;
                        LogUtil.e("详情页面：" + idStr);
                    } catch (Exception e) {
                        LogUtil.e("详情页面：" + e.getMessage());
                    }
                }
                switch (idStr) {
                    case "0":
                    case "1":
                    case "2":
                    case "3":
                        break;
                    default:
                        params = new IntentParams();
                        if (MyApplication.agendaDetailId != null) {
                            params.setParam("id", MyApplication.agendaDetailId);
                        }
                        if (MyApplication.agendaDetail != null) {
                            params.setParam("bean", MyApplication.agendaDetail);
                        }
                        if (MyApplication.agendaDetailTH != null) {
                            params.setParam("tHbean", MyApplication.agendaDetailTH);
                        }


                        break;
                }
                break;


        }
        pageStr = TextTool.isNullOrEmpty(page) ? pageStr : page;
        page = null;
        setPageParams(pageStr, params);
    }
}
