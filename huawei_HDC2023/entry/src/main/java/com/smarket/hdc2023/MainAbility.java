package com.smarket.hdc2023;

import com.smarket.hdc2023.bean.CardInfoContent;
import com.smarket.hdc2023.bean.CeliaCardStyle;
import com.smarket.hdc2023.bean.HdcFormIntentInfo;
import com.smarket.hdc2023.bean.ServerTime;
import com.smarket.hdc2023.help.*;
import com.smarket.hdc2023.http.HttpUtil;
import com.smarket.hdc2023.widget.form.PreferencesUtils;
import com.smarket.hdc2023.widget.form.TextDialogUtil;
import com.smarket.hdc2023.widget.form.database.DatabaseUtils;
import com.smarket.hdc2023.widget.form.database.FormData;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.FormBindingData;
import ohos.aafwk.ability.ProviderFormInfo;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.agp.utils.TextTool;
import ohos.ai.cv.common.ConnectionCallback;
import ohos.ai.cv.common.VisionManager;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.bundle.IBundleManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.audio.SoundPlayer;
import ohos.utils.zson.ZSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 主程序
 */
public class MainAbility extends BaseAbility {
    //默认规格
    private static final int DEFAULT_DIMENSION_2X2 = 2;
    //卡片ID
    private static final int INVALID_FORM_ID = -1;
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP, 0XD000F00, MainAbility.class.getName());

    //大会午餐poi id
    private final String lunchPoiId = "666";

    //延时执行
    private Timer timer;

    private Ability ability;

    /**
     * 服务创建
     */
    @Override
    public void onStart(Intent intent) {
        jumpPage(intent);
        ability = this;
        //初始化二维码生成服务
        if (CeliaCardUtil.iBarcodeDetector == null) {
            VisionManager.init(this, connectionCallback);
        }

        //初始化华为统计
        HiAnalyticsUtil.initInstance(this);
        super.onStart(intent);
    }

    /**
     * 停止
     */
    @Override
    public void onStop() {
        super.onStop();
        try {
            //释放二维码生成服务资源
            if (CeliaCardUtil.iBarcodeDetector != null) {
                CeliaCardUtil.iBarcodeDetector.release();
            }
            VisionManager.destroy();
            CeliaCardUtil.iBarcodeDetector = null;
        }
        catch (Exception ex){

        }
        try {
            //取消延时任务
            if (timer != null) {
                timer.cancel();
            }
        }catch (Exception ex){

        }

    }

    /**
     * 新的Intent
     */
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onStart(intent);
    }

    /**
     * 卡片创建
     */
    @Override
    protected ProviderFormInfo onCreateForm(Intent intent) {
        ability = this;
        //卡片信息
        ProviderFormInfo info = new ProviderFormInfo();
        try {
            if (intent == null) {
                return info;
            }
            // 获取卡片id
            long cardId = INVALID_FORM_ID;
            if (intent.hasParameter(AbilitySlice.PARAM_FORM_IDENTITY_KEY)) {
                cardId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, INVALID_FORM_ID);
            }
            // 获取卡片规格
            int dimension = DEFAULT_DIMENSION_2X2;
            if (intent.hasParameter(AbilitySlice.PARAM_FORM_DIMENSION_KEY)) {
                dimension = intent.getIntParam(AbilitySlice.PARAM_FORM_DIMENSION_KEY, DEFAULT_DIMENSION_2X2);
            }
            // 获取卡片名称
            String name = "widget";
            if (intent.hasParameter(AbilitySlice.PARAM_FORM_NAME_KEY)) {
                name = intent.getStringParam(AbilitySlice.PARAM_FORM_NAME_KEY);
            }

            HiLog.info(TAG, "卡片名称：" + name);
            //小艺建议卡片-获取poiId
            String poiId = "";
            if (name.equals("celia")) {
                try {
                    //初始化二维码生成服务
                    if (CeliaCardUtil.iBarcodeDetector == null) {
                        VisionManager.init(this, connectionCallback);
                    }
                    IntentParams intentParams = intent.getParam(AbilitySlice.PARAM_FORM_CUSTOMIZE_KEY);
                    String formIntentInfo = (String) intentParams.getParam("formIntentInfo");
                    // TODO: 打包需要注释此代码=====START
                    //formIntentInfo = HttpUtil.getTestPoiId();
                    //formIntentInfo = HttpUtil.getTestPoiId_SN();
                    // TODO: 打包需要注释此代码=====END
                    HiLog.info(TAG, "formIntentInfo：" + formIntentInfo);
                    if (formIntentInfo != null && !formIntentInfo.equals("")) {
                        HdcFormIntentInfo hdcFormIntentInfo = ZSONObject.stringToClass(formIntentInfo, HdcFormIntentInfo.class);
                        if (hdcFormIntentInfo != null) {
                            List<HdcFormIntentInfo.PoiInfo> poiInfo = hdcFormIntentInfo.getPoiParams();
                            if (poiInfo != null && poiInfo.size() > 0) {
                                poiId = poiInfo.get(0).getPoiId();
                            }
                        }
                    }
                    HiLog.info(TAG, "小艺建议poiId：" + poiId);
                    //接收负一屏参数
                    if (TextTool.isNullOrEmpty(poiId)) {
                        HiLog.info(TAG, "进入负一屏逻辑");
                        String extFaParam = (String) intentParams.getParam("extFaParam");
                        HiLog.info(TAG, "extFaParam" + extFaParam);
                        if (extFaParam != null && !extFaParam.equals("")) {
                            ZSONObject extraParamJson = ZSONObject.stringToZSON(extFaParam);
                            poiId = extraParamJson.getString("poi_id");
                            HiLog.info(TAG, "负一屏poiId：" + poiId);
                        }
                    }
                } catch (Exception e) {
                    HiLog.error(TAG, "poiIdError" + e);
                }
            }

            //将卡片信息加入缓存
            FormData cardData = new FormData(cardId, dimension, name, poiId);
            DatabaseUtils.insertCardData(cardData);

            if (name.equals("widget")) {
                //大会议程卡片 2x2 刷新议程卡片
                CeliaCardUtil.updateAgendaCard(this);
            } else if (name.equals("card_2x4")) {
                //大会议程卡片 2x4 刷新议程卡片
                CeliaCardUtil.updateAgendaCard(this);
            } else if (name.equals("celia")) {
                //小艺建议卡片 根据PoiId刷新卡片
                CeliaCardUtil.poiIdUpdate(this, poiId, cardId);
            }
            info.setJsBindingData(new FormBindingData());
        } catch (Exception ee) {
            HiLog.error(TAG, "onCreateForm" + ee);
        }
        return info;
    }

    /**
     * 刷新大会午餐卡片
     *
     * @param formId 卡片ID
     */
    private void updateLunchCard(long formId) {
        getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(() -> {
            //判断是否签到
            CardInfoContent cardInfoContent = HttpUtil.getHarmonyCardGetCardByPoid(lunchPoiId);
            if (cardInfoContent != null && cardInfoContent.card != null) {
                //没有值卡片不刷新，还显示原有卡片
                ZSONObject zsonObject = new ZSONObject();
                //卡片样式
                CeliaCardStyle celiaCardStyle = new CeliaCardStyle();
                celiaCardStyle.bgClass1 = "card-bg2";
                celiaCardStyle.bgClass2 = "pr6";
                celiaCardStyle.isSignOrLunchCard = true;
                celiaCardStyle.isLunchCard = true;
                zsonObject.put("style", celiaCardStyle);

                //卡片内容
                zsonObject.put("signOrLunchTitle", cardInfoContent.card.title);
                zsonObject.put("lunchText1", cardInfoContent.card.text_1);
                zsonObject.put("lunchText2", cardInfoContent.card.text_2);
                zsonObject.put("lunchText3", cardInfoContent.card.text_3);

                //修改卡片POI-ID
                DatabaseUtils.updatePoiId(formId, lunchPoiId);

                //刷新卡片
                CommonUtil.updateCardForm(this, formId, zsonObject);
            }
        });
    }

    //卡片刷新
    @Override
    protected void onUpdateForm(long formId) {
        super.onUpdateForm(formId);
        //获取当前卡片
        FormData formData = DatabaseUtils.queryCardData(formId);
        if (formData.getName().equals("widget")) {
            //大会议程卡片 2x2 刷新议程卡片
            CeliaCardUtil.updateAgendaCard(this);
        } else if (formData.getName().equals("card_2x4")) {
            //大会议程卡片 2x4 刷新议程卡片
            CeliaCardUtil.updateAgendaCard(this);
        } else if (formData.getName().equals("celia")) {
            //获取当前时间
            ServerTime serverTime = com.smarket.hdc2023.http.HttpUtil.getServerTime();
            String day = "";//yyyy-MM-dd HH:mm
            if (serverTime != null) {
                day = TimeHelper.getByDateTime(serverTime.getServerTime());
            }
            if (day.equals("2023-08-05 11:30") || day.equals("2023-08-06 11:30")) {
                //自主加桌和未登录不刷新
                getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(() -> {
                    if (!TextTool.isNullOrEmpty(formData.getPoiId()) && !TextTool.isNullOrEmpty(MyApplication.getSess())) {
                        //随机延时刷新大会午餐卡片,减少服务器压力
                        int delay;
                        try {
                            delay = SecureRandom.getInstanceStrong().nextInt(30) * 1000;
                        } catch (NoSuchAlgorithmException e) {
                            delay = 0;
                            HiLog.error(TAG, "delay" + e);
                        }
                        if (delay == 0) {
                            delay = 100;
                        }
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                updateLunchCard(formId);
                            }
                        }, delay);
                    }
                });
            }
        }
    }

    //卡片移除
    @Override
    protected void onDeleteForm(long formId) {
        super.onDeleteForm(formId);
        DatabaseUtils.deleteCardData(formId);
    }

    /**
     * 播放敲击键盘音
     */
    private void playKeyClickSound() {
        //步骤1：实例化对象
        SoundPlayer soundPlayer = new SoundPlayer(getBundleName());
        //步骤2：播放键盘敲击音，音量为1.0
        soundPlayer.playSound(SoundPlayer.SoundType.KEY_CLICK, 1.0f);
        soundPlayer.release();
    }

    /**
     * 卡片点击事件
     *
     * @param formId  卡片ID
     * @param message 参数
     */
    @Override
    protected void onTriggerFormEvent(long formId, String message) {
        super.onTriggerFormEvent(formId, message);

        ZSONObject zsonObject = ZSONObject.stringToZSON(message);
        String action = zsonObject.getString("action");
        String buttonName = "";//按钮名称
        switch (action) {
            case "agendaSwiperDataChange":
                //小艺建议我的议程卡片-下一个
                playKeyClickSound();
                int agendaSwiperIndex = Integer.parseInt(String.valueOf(zsonObject.getString("message")));
                swiperDataChange(formId, agendaSwiperIndex);
                buttonName = "切换议程";
                break;
            case "agendaSwiperDataChange2":
                //议程卡片2x4切换-向下
                playKeyClickSound();
                int agendaSwiperIndex2 = Integer.parseInt(String.valueOf(zsonObject.getString("message")));
                swiperDataChange2(formId, agendaSwiperIndex2, false);
                buttonName = "切换议程";
                break;
            case "agendaSwiperDataChangeUp":
                //议程卡片2x4切换-向上
                playKeyClickSound();
                int agendaSwiperIndexUp = Integer.parseInt(String.valueOf(zsonObject.getString("message")));
                swiperDataChange2(formId, agendaSwiperIndexUp, true);
                buttonName = "切换议程";
                break;
            case "goPlay":
                //播放/暂停
                AudioPlayUtil.goPlay(this, zsonObject.getString("sceneryAudioUrl"), zsonObject.getString("sceneryCardId"), zsonObject.getString("sceneryMessage"), zsonObject.getString("sceneryTaskId"), 0, null);
                buttonName = "语音讲解";
                break;
        }

        //上报用户点击功能按钮事件
        HiAnalyticsUtil.addModularClickEvent(this, CommonUtil.getCardCnName(formId) + "-" + buttonName);
    }

    /**
     * 切换小艺建议卡片当前展示议程
     *
     * @param formId            卡片ID
     * @param agendaSwiperIndex 当前位置
     */
    private void swiperDataChange(long formId, int agendaSwiperIndex) {
        int agendaDataCount = PreferencesUtils.getAgendaDataCount();
        if (agendaDataCount == 0 || agendaDataCount == 1) {
            return;
        }
        if (agendaSwiperIndex == agendaDataCount - 1) {
            agendaSwiperIndex = 0;
        } else {
            agendaSwiperIndex += 1;
        }
        ZSONObject swiperZSON = new ZSONObject();
        swiperZSON.put("agendaSwiperIndex", agendaSwiperIndex);
        CommonUtil.updateCardForm(this, formId, swiperZSON);
    }

    /**
     * 切换议程2x4卡片当前展示议程
     *
     * @param formId            卡片ID
     * @param agendaSwiperIndex 当前位置
     * @param isUp              是否向上
     */
    private void swiperDataChange2(long formId, int agendaSwiperIndex, boolean isUp) {
        int agendaDataCount = PreferencesUtils.getAgendaDataCount2();
        if (agendaDataCount == 0 || agendaDataCount == 1) {
            return;
        }
        if (isUp) {
            if (agendaSwiperIndex == 0) {
                agendaSwiperIndex = agendaDataCount - 1;
            } else {
                agendaSwiperIndex -= 1;
            }
        } else {
            if (agendaSwiperIndex == agendaDataCount - 1) {
                agendaSwiperIndex = 0;
            } else {
                agendaSwiperIndex += 1;
            }
        }
        ZSONObject swiperZSON = new ZSONObject();
        swiperZSON.put("agendaSwiperIndex", agendaSwiperIndex);
        CommonUtil.updateCardForm(this, formId, swiperZSON);
    }

    /**
     * 卡片跳转页面
     *
     * @param intent 意图
     */
    private void jumpPage(Intent intent) {
        try {
            String cardParams = intent.getStringParam("params");
            if (cardParams != null) {
                Boolean isGo = true;
                ZSONObject zsonObject = ZSONObject.stringToZSON(cardParams);
                String message = zsonObject.getString("message");
                String data = zsonObject.getString("data");
                String data2 = zsonObject.getString("data2");
                String data3 = zsonObject.getString("data3");
                IntentParams params = new IntentParams();
                String pageUri = "";
                String buttonName = "";//按钮名称
                switch (message) {
                    case "myOrAllAgenda"://我的议程/全部议程
                        if (data.equals("") || data.equals("我的议程")) {
                            if (TextTool.isNullOrEmpty(MyApplication.getSess2())) {
                                pageUri = "pages/loginAccredit/loginAccredit";
                            } else {
                                pageUri = "pages/my-agenda/my-agenda";
                            }
                            buttonName = "我的议程";
                        } else {
                            pageUri = "pages/agenda/agenda";
                            buttonName = "全部议程";
                        }
                        break;
                    case "agendaDetail"://议程详情
                        if (!TextTool.isNullOrEmpty(data) && "false".equals(data2)) {
                            params.setParam("id", data);
                            pageUri = "pages/agenda-detail/agenda-detail";
                        } else {
                            if (!TextTool.isNullOrEmpty(data3)) {
                                String clickDate = TimeHelper.getDayPattern6ByTime(Long.valueOf(data3));
                                params.setParam("index_agendaDate", clickDate);
                            }
                            pageUri = "pages/agenda/agenda";
                        }
                        buttonName = "议程详情";
                        break;
                    case "goIntegral"://码力互动
                        pageUri = "pages/travel/travel";
                        buttonName = "码力互动";
                        break;
                    case "goTravelList"://精彩活动
                        pageUri = "pages/travel/travel";
                        params.setParam("type", 1);
                        buttonName = "精彩活动";
                        break;
                    case "myTickets"://我的门票
                        if (TextTool.isNullOrEmpty(MyApplication.getSess2())) {
                            pageUri = "pages/loginAccredit/loginAccredit";
                            params.setParam("goUrl", 131);
                        } else {
                            pageUri = "pages/my-ticket/my-ticket";
                        }
                        buttonName = "我的门票";
                        break;
                    case "goLogin"://登录
                        if (TextTool.isNullOrEmpty(MyApplication.getSess2())) {
                            pageUri = "pages/loginAccredit/loginAccredit";
                            buttonName = "登录";
                        } else {
                            isGo = false;
                        }
                        break;
                    case "sceneryDetail"://三丫坡风景详情
                        pageUri = "pages/travel-detail/travel-detail";
                        params.setParam("id", data);
                        buttonName = "三丫坡风景详情";
                        break;
                    default:
                        //首页
                        pageUri = "pages/index/index";
                        buttonName = "首页";
                        break;
                }
                if (intent.hasParameter(AbilitySlice.PARAM_FORM_ID_KEY)) {
                    long formId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, INVALID_FORM_ID);
                    //上报用户点击功能按钮事件
                    HiAnalyticsUtil.addModularClickEvent(this, CommonUtil.getCardCnName(formId) + "-" + buttonName);
                }
                if (isGo) {
                    setPageParams(pageUri, params);
                }
            }
        } catch (Exception e) {
            HiLog.error(TAG, "jumpPage：" + e);
        }
    }

    /**
     * 权限授权回调
     *
     * @param requestCode  equestPermission中传入的requestCode
     * @param permissions  申请的权限名
     * @param grantResults 申请权限的结果
     */
    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1111: {
                //日历权限
                if (grantResults.length > 0 && grantResults[0] == IBundleManager.PERMISSION_GRANTED) {
                    // 权限被授予
                    // 注意：因时间差导致接口权限检查时有无权限，所以对那些因无权限而抛异常的接口进行异常捕获处理
                    TextDialogUtil.show(getContext(), "授权成功");
                } else {
                    // 权限被拒绝
                    TextDialogUtil.show(getContext(), "您已拒绝授权");
                }
                break;
            }
        }
    }

    /**
     * 二维码服务回调
     */
    private ConnectionCallback connectionCallback = new ConnectionCallback() {
        @Override
        public void onServiceConnect() {
            if (CeliaCardUtil.iBarcodeDetector == null) {
                CeliaCardUtil.iBarcodeDetector = VisionManager.getBarcodeDetector(ability);
            }
        }

        @Override
        public void onServiceDisconnect() {
            CeliaCardUtil.iBarcodeDetector = null;
        }
    };
}