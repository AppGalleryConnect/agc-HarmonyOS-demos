package com.smarket.hdc2023.help;

import com.smarket.hdc2023.MyApplication;
import com.smarket.hdc2023.bean.CardInfoContent;
import com.smarket.hdc2023.bean.CeliaCardStyle;
import com.smarket.hdc2023.bean.ServerTime;
import com.smarket.hdc2023.http.HttpUtil;
import com.smarket.hdc2023.widget.form.PreferencesUtils;
import com.smarket.hdc2023.widget.form.TextDialogUtil;
import com.smarket.hdc2023.widget.form.bean.AgendaBean;
import com.smarket.hdc2023.widget.form.database.DatabaseUtils;
import com.smarket.hdc2023.widget.form.database.FormData;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.FormBindingData;
import ohos.aafwk.ability.FormException;
import ohos.agp.utils.TextTool;
import ohos.ai.cv.qrcode.IBarcodeDetector;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.zson.ZSONArray;
import ohos.utils.zson.ZSONObject;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 小艺建议卡片工具类
 */
public class CeliaCardUtil {
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP, 0XD000F00, CeliaCardUtil.class.getName());

    //二维码服务
    public static IBarcodeDetector iBarcodeDetector;

    //poiId中包含此字符串，显示默认议程卡片
    private static String noUpdateStr = "#SINONO";

    /**
     * 刷新小艺建议卡片
     *
     * @param ability ability
     */
    public static void updateCeliaCard(Ability ability) {
        List<FormData> formData = DatabaseUtils.queryAllCardData();
        for (FormData formDatum : formData) {
            if (formDatum.getName().equals("celia") && !TextTool.isNullOrEmpty(formDatum.getPoiId())) {
                String poiId = formDatum.getPoiId();
                if (poiId.contains(noUpdateStr)) {
                    poiId = poiId.replaceAll(noUpdateStr, "");
                    DatabaseUtils.updatePoiId(formDatum.getFormId(), poiId);
                }
                poiIdUpdate(ability, poiId, formDatum.getFormId());
            }
        }
    }

    /**
     * 根据PoiId刷新卡片
     *
     * @param ability ability
     * @param poiId   PoiId
     * @param formId  卡片ID
     */
    public static void poiIdUpdate(Ability ability, String poiId, long formId) {
        if (!TextTool.isNullOrEmpty(poiId)) {
            //获取最后一个字符
            String poiIdEnd = poiId.substring(poiId.length() - 1);
            if (poiIdEnd.equals("1")) {
                //1结尾的为议程卡片 刷新议程卡片
                updateAgendaCard(ability);
            } else if (poiIdEnd.equals("2")) {
                //2结尾的为我的门票卡片
                updateTicketCard(ability, formId);
            } else if (poiIdEnd.equals("3")) {
                //3结尾的为风景类
                updateSceneryCard(ability, poiId, formId);
            } else if (poiIdEnd.equals("4")) {
                //4结尾的为加桌卡片、每日打卡卡片、趣味答题卡片
                updateTaskCard(ability, poiId, formId);
            } else if (poiIdEnd.equals("5")) {
                //5结尾的为签到卡片
                updateSignCard(ability, poiId, formId);
            } else if (poiIdEnd.equals("7")) {
                //7结尾的为积分互动卡片-距离
                updateIntegralCard(ability, poiId, formId);
            }
        } else {
            //自主加桌，显示全部议程卡片 刷新议程卡片
            updateAgendaCard(ability);
        }
    }

    /**
     * 刷新议程卡片
     *
     * @param ability ability
     */
    public static void updateAgendaCard(Ability ability) {
        ability.getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(() -> {
            List<AgendaBean> allAgendaData = new ArrayList<>();//全部议程-不显示已开始的
            List<AgendaBean> myAgendaData = new ArrayList<>();//我的议程-不显示已开始的
            List<AgendaBean> agendaSwiperData = new ArrayList<>();//轮换议程数据
            AgendaBean firstAgenda = new AgendaBean();//8月4日议程
            try {
                //获取当前时间
                ServerTime serverTime = com.smarket.hdc2023.http.HttpUtil.getServerTime();
                String day = "";//yyyy.MM.dd
                if (serverTime != null) {
                    day = TimeHelper.getDayPattern6ByTime(serverTime.getServerTime());
                }
                //获取全部议程
                String result = PreferencesUtils.getAgendaResult();
                Boolean isJk = false;
                if (TextTool.isNullOrEmpty(result)) {
                    result = com.smarket.hdc2023.widget.form.HttpUtil.httpGet(new ZSONObject()).get(15, TimeUnit.SECONDS);
                    isJk = true;
                }
                //我预约的议程
                List<String> subs = MyApplication.getidlist();
                if (!"error".equals(result)) {
                    HiLog.info(TAG, "刷新议程卡片,获取全部议程：" + result);
                    ZSONObject zsonObject = ZSONObject.stringToZSON(result);
                    ZSONArray zsonArray = zsonObject.getZSONObject("body").getZSONObject("content").getZSONArray("items");
                    List<AgendaBean> agendaBeans = zsonArray.toJavaList(AgendaBean.class);//全部议程
                    List<AgendaBean> removeData = new ArrayList<>();//不显示的议程
                    for (AgendaBean agendaBean : agendaBeans) {
                        //格式化议程数据
                        agendaBean.setName(agendaBean.getName().replace("|", ""));
                        String startMonthDay = TimeHelper.getDateByDay(agendaBean.getStartTime());
                        String startHourMinute = TimeHelper.getDateByTime(agendaBean.getStartTime());
                        String endHourMinute = TimeHelper.getDateByTime(agendaBean.getEndTime());
                        agendaBean.setAgendaDate(startMonthDay);
                        agendaBean.setAgendaTime(startHourMinute + "-" + endHourMinute);
                        agendaBean.setHasGroupName(agendaBean.getM_meetingTypeCn().stream().anyMatch(v -> v.contains("主题演讲")));
                        if (agendaBean.getHasGroupName() && startMonthDay.equals("08月04日")) {
                            firstAgenda = ZSONObject.stringToClass(ZSONObject.toZSONString(agendaBean), AgendaBean.class);
                        }
                        //过滤日期
                        if (startMonthDay.equals("08月04日") || startMonthDay.equals("08月05日") || startMonthDay.equals("08月06日")) {
                            //过滤类型
                            if (agendaBean.getM_meetingTypeCn().contains("主题演讲") || agendaBean.getM_meetingTypeCn().contains("开发者主题演讲") || agendaBean.getM_meetingTypeCn().contains("HarmonyOS极客马拉松") || agendaBean.getM_meetingTypeCn().contains("HarmonyOS生态峰会")|| agendaBean.getM_meetingTypeCn().contains("鸿蒙生态峰会") || agendaBean.getM_meetingTypeCn().contains("技术论坛") || agendaBean.getM_meetingTypeCn().contains("Codelabs") || agendaBean.getM_meetingTypeCn().contains("Tech. Hour")) {
                                //只显示没开始的议程
                                if (agendaBean.getStartTime() >= serverTime.getServerTime()) {
                                    allAgendaData.add(agendaBean);
                                    if (subs != null && subs.contains(agendaBean.getSubSeminarId())) {
                                        myAgendaData.add(agendaBean);
                                    }
                                }
                            } else {
                                removeData.add(agendaBean);
                            }
                        } else {
                            removeData.add(agendaBean);
                        }
                    }
                    //没有议程显示全部
                    if (allAgendaData.size() == 0) {
                        if (agendaBeans.size() != removeData.size()) {
                            agendaBeans.removeAll(removeData);
                        }
                        allAgendaData = agendaBeans;
                    }
                    //对议程排序
                    allAgendaData.sort(Comparator.comparing(AgendaBean::getStartTime));
                    myAgendaData.sort(Comparator.comparing(AgendaBean::getStartTime));
                    //如果全部议程超过30个，显示1/3
                    int length = allAgendaData.size();
                    if (length >= 30) {
                        allAgendaData = allAgendaData.subList(0, length / 3);
                    }
                } else {
                    result = "";
                    HiLog.info(TAG, "获取议程请求出错：" + result);
                    ability.getUITaskDispatcher().asyncDispatch(() -> TextDialogUtil.show(ability.getContext(), "数据请求出错"));
                }

                //刷新议程卡片
                List<FormData> formData = DatabaseUtils.queryAllCardData();
                for (FormData formDatum : formData) {
                    int cardType = 0;//议程卡片类型，1为主题演讲卡片，2为我的议程卡片，3为全部议程卡片，4为自主加桌卡片，0为非议程卡片
                    ZSONObject zsonObject = new ZSONObject();
                    if (formDatum.getName().equals("widget")) {
                        //大会议程卡片 2x2
                        cardType = 4;
                        zsonObject.put("agendaSwiperIndex", 0);
                        zsonObject.put("agendaSwiperData", allAgendaData);
                    } else if (formDatum.getName().equals("card_2x4")) {
                        //大会议程卡片 2x4
                        cardType = 4;
                        zsonObject.put("agendaSwiperIndex", 0);
                        //组装数据
                        List<ZSONObject> zsonObjects = new ArrayList<>();
                        ZSONObject zson = new ZSONObject();
                        int n = 1;
                        for (AgendaBean agendaBean : allAgendaData) {
                            if (n == 1) {
                                zson = new ZSONObject();
                                zson.put("agenda1", agendaBean);
                                zson.put("isTwo", false);
                                n = 2;
                            } else {
                                zson.put("agenda2", agendaBean);
                                zson.put("isTwo", true);
                                zsonObjects.add(zson);
                                n = 1;
                            }
                        }
                        if (n == 2) {
                            //单数加上主题演讲的议程
                            zson.put("agenda2", firstAgenda);
                            zson.put("isTwo", true);
                            zsonObjects.add(zson);
                        }
                        PreferencesUtils.saveAgendaDataCount2(zsonObjects.size());
                        zsonObject.put("agendaSwiperData", zsonObjects);
                    } else if (formDatum.getName().equals("celia")) {
                        if (TextTool.isNullOrEmpty(formDatum.getPoiId())) {
                            //小艺建议自主加桌显示全部议程卡片
                            cardType = 3;
                        } else {
                            //小艺建议推荐卡片
                            //获取最后一个字符
                            String poiIdEnd = formDatum.getPoiId().substring(formDatum.getPoiId().length() - 1);
                            if (poiIdEnd.equals("1")) {
                                if (TextTool.isNullOrEmpty(MyApplication.getSess())) {
                                    //未登录显示全部议程卡片
                                    cardType = 3;
                                } else {
                                    if (day.equals("2023.08.05") || day.equals("2023.08.06")) {
                                        if (myAgendaData.size() > 0) {
                                            //有预约显示我的议程卡片
                                            cardType = 2;
                                        } else {
                                            //没有预约显示全部议程卡片
                                            cardType = 3;
                                        }
                                    } else {
                                        //除了8月5日和8月6日，都显示主题演讲
                                        cardType = 1;
                                    }
                                }
                            } else if (formDatum.getPoiId().contains(noUpdateStr)) {
                                //卡片不刷新时显示全部议程卡片
                                cardType = 3;
                            }
                        }
                        //小艺术建议卡片样式
                        CeliaCardStyle celiaCardStyle = new CeliaCardStyle();
                        celiaCardStyle.isAgendaCard = true;
                        celiaCardStyle.bgClass1 = "card-bg";
                        if (cardType == 1) {
                            //主题演讲卡片
                            String ticketName = MyApplication.getUserInfo().ticketName;
                            if (!TextTool.isNullOrEmpty(ticketName)) {
                                //显示席位
                                zsonObject.put("ticketName", ticketName);
                                celiaCardStyle.isFirstAgendaCard = true;
                            }
                            celiaCardStyle.notMyAgendaCardBtn = "我的议程";
                            agendaSwiperData.add(firstAgenda);
                        } else if (cardType == 2) {
                            //我的议程卡片
                            celiaCardStyle.isMyAgendaCard = true;
                            agendaSwiperData = myAgendaData;
                        } else if (cardType == 3) {
                            //全部议程卡片
                            celiaCardStyle.notMyAgendaCardBtn = "全部议程";
                            agendaSwiperData = allAgendaData;
                        }
                        zsonObject.put("agendaSwiperIndex", 0);
                        zsonObject.put("agendaSwiperData", agendaSwiperData);
                        PreferencesUtils.saveAgendaData(agendaSwiperData);
                        zsonObject.put("style", celiaCardStyle);
                    }
                    if (cardType != 0) {
                        if (isJk && !TextTool.isNullOrEmpty(result)) {
                            //将本次接口的返回值缓存，25分钟内不重新加载
                            PreferencesUtils.saveAgendaResult(result, serverTime.getServerTime());
                        }
                        if (zsonObject != null && !TextTool.isNullOrEmpty(result) && zsonObject.containsKey("agendaSwiperData") && zsonObject.getZSONArray("agendaSwiperData").size() > 0) {
                            CommonUtil.updateCardForm(ability, formDatum.getFormId(), zsonObject);
                        }
                    }
                }
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 刷新门票卡片
     *
     * @param ability ability
     * @param formId  卡片ID
     */
    private static void updateTicketCard(Ability ability, long formId) {
        ability.getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(() -> {
            ZSONObject zsonObject = new ZSONObject();
            //卡片样式
            CeliaCardStyle celiaCardStyle = new CeliaCardStyle();
            celiaCardStyle.bgClass1 = "card-bg";
            celiaCardStyle.isTicketCard = true;
            if (TextTool.isNullOrEmpty(MyApplication.getSess())) {
                //未登录样式
                celiaCardStyle.ticketDivClass = "ticket";
                celiaCardStyle.ticketTextClass = "ticket-text";
                zsonObject.put("style", celiaCardStyle);

                //刷新卡片
                CommonUtil.updateCardForm(ability, formId, zsonObject);
            } else {
                //已登录样式
                celiaCardStyle.ticketDivClass = "qrcode";
                celiaCardStyle.ticketTextClass = "qrcode-text";
                celiaCardStyle.isLogin = true;
                zsonObject.put("style", celiaCardStyle);

                //二维码内容
                String qrCode = MyApplication.getUserInfo().qrCode;
                if (TextTool.isNullOrEmpty(qrCode)) {
                    qrCode = "no data";
                }
                String imgName = "qrcode" + System.currentTimeMillis() + ".png";
                zsonObject.put("ticketImg", "memory://" + imgName);
                FormBindingData formBindingData = new FormBindingData(zsonObject);
                formBindingData.addImageData(imgName, getImage(qrCode));
                //刷新卡片
                updateCardForm(ability, formId, formBindingData);
            }
        });
    }

    /**
     * 刷新风景卡片
     *
     * @param ability ability
     * @param poiId   poiId
     * @param formId  卡片ID
     */
    private static void updateSceneryCard(Ability ability, String poiId, long formId) {
        ability.getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(() -> {
            //获取卡片数据
            CardInfoContent cardInfoContent = HttpUtil.getHarmonyCardGetCardByPoid(poiId);
            if (cardInfoContent != null && cardInfoContent.card != null && !TextTool.isNullOrEmpty(cardInfoContent.card.type)) {
                ZSONObject zsonObject = new ZSONObject();
                //卡片样式
                CeliaCardStyle celiaCardStyle = new CeliaCardStyle();
                celiaCardStyle.bgClass1 = "card-bg";
                celiaCardStyle.isSceneryCard = true;
                celiaCardStyle.hasIntegralBtn = true;
                zsonObject.put("style", celiaCardStyle);

                //卡片内容
                String distanceMessage = "恭喜您完成任务";
                if (cardInfoContent.card.nearestCard != null && !TextTool.isNullOrEmpty(cardInfoContent.card.nearestCard.distanceMessage)) {
                    distanceMessage = cardInfoContent.card.nearestCard.distanceMessage;
                }
                zsonObject.put("distanceMessage", distanceMessage);
                zsonObject.put("cardId", String.valueOf(formId));
                zsonObject.put("interactionTaskId", cardInfoContent.card.interaction_task_id);
                zsonObject.put("sceneryTitle", cardInfoContent.card.title);
                zsonObject.put("sceneryText1", cardInfoContent.card.text_1);
                zsonObject.put("sceneryAudioUrl", cardInfoContent.card.text_2);
                zsonObject.put("sceneryText2", cardInfoContent.card.text_3);
                zsonObject.put("sceneryId", cardInfoContent.card.text_4);
                zsonObject.put("imgType", TextTool.isNullOrEmpty(cardInfoContent.card.text_5) ? "/common/sanyapo.png" : "/common/" + cardInfoContent.card.text_5 + "1.png");
                //刷新卡片
                CommonUtil.updateCardForm(ability, formId, zsonObject);
            } else {
                //没有值卡片不更新,将POI-ID清除，显示议程卡片
                DatabaseUtils.updatePoiId(formId, poiId + noUpdateStr);
                updateAgendaCard(ability);
            }
        });
    }

    /**
     * 刷新加桌卡片、每日打卡卡片、趣味答题卡片
     *
     * @param ability ability
     * @param poiId   poiId
     * @param formId  卡片ID
     */
    private static void updateTaskCard(Ability ability, String poiId, long formId) {
        ability.getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(() -> {
            //获取卡片数据
            CardInfoContent cardInfoContent = HttpUtil.getHarmonyCardGetCardByPoid(poiId);
            if (cardInfoContent != null && cardInfoContent.card != null && !TextTool.isNullOrEmpty(cardInfoContent.card.type)) {
                ZSONObject zsonObject = new ZSONObject();
                //卡片样式
                CeliaCardStyle celiaCardStyle = new CeliaCardStyle();
                celiaCardStyle.bgClass1 = "card-bg";
                celiaCardStyle.bgClass2 = "card-bg1";
                celiaCardStyle.hasIntegralBtn = true;
                celiaCardStyle.isTaskCard = true;
                zsonObject.put("style", celiaCardStyle);

                //卡片内容
                String distanceMessage = "恭喜您完成任务";
                if (cardInfoContent.card.nearestCard != null && !TextTool.isNullOrEmpty(cardInfoContent.card.nearestCard.distanceMessage)) {
                    distanceMessage = cardInfoContent.card.nearestCard.distanceMessage;
                }
                zsonObject.put("cardId", String.valueOf(formId));
                zsonObject.put("distanceMessage", distanceMessage);
                zsonObject.put("interactionTaskId", cardInfoContent.card.interaction_task_id);

                if (cardInfoContent.card.type.equals("desktop")) {
                    zsonObject.put("taskCardText", "卡片加桌");
                    zsonObject.put("taskCardText2", "卡片加桌");
                    zsonObject.put("taskCardText3", "赢取码力值");
                    zsonObject.put("taskCardLeftImg", "/common/sign-bg1.png");
                    zsonObject.put("taskCardRightImg", "/common/sign1.png");
                    zsonObject.put("taskCardRightClass", "sign1");
                } else if (cardInfoContent.card.type.equals("daily")) {
                    zsonObject.put("taskCardText", "每日打卡");
                    zsonObject.put("taskCardText2", "每日打卡");
                    zsonObject.put("taskCardText3", "赢取码力值");
                    zsonObject.put("taskCardLeftImg", "/common/sign-bg2.png");
                    zsonObject.put("taskCardRightImg", "/common/sign2.png");
                    zsonObject.put("taskCardRightClass", "sign2");
                } else if (cardInfoContent.card.type.equals("questionnaire")) {
                    zsonObject.put("taskCardText", "趣味答题");
                    zsonObject.put("taskCardText2", "NPC 互动");
                    zsonObject.put("taskCardText3", "赢取礼品");
                    zsonObject.put("taskCardLeftImg", "/common/sign-bg3.png");
                    zsonObject.put("taskCardRightImg", "/common/sign3.png");
                    zsonObject.put("taskCardRightClass", "sign3");
                }

                //刷新卡片
                CommonUtil.updateCardForm(ability, formId, zsonObject);
            } else {
                //没有值卡片不更新,将POI-ID清除，显示议程卡片
                DatabaseUtils.updatePoiId(formId, poiId + noUpdateStr);
                updateAgendaCard(ability);
            }
        });
    }

    /**
     * 刷新签到卡片
     *
     * @param ability ability
     * @param poiId   poiId
     * @param formId  卡片ID
     */
    private static void updateSignCard(Ability ability, String poiId, long formId) {
        ability.getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(() -> {
            //判断是否签到
            CardInfoContent cardInfoContent = HttpUtil.getHarmonyCardGetCardByPoid(poiId);
            if (cardInfoContent != null && cardInfoContent.card != null) {
                ZSONObject zsonObject = new ZSONObject();
                //卡片样式
                CeliaCardStyle celiaCardStyle = new CeliaCardStyle();
                celiaCardStyle.bgClass1 = "card-bg3";
                celiaCardStyle.bgClass2 = "pr6";
                celiaCardStyle.isSignOrLunchCard = true;
                celiaCardStyle.isSignCard = true;
                zsonObject.put("style", celiaCardStyle);

                //卡片内容
                zsonObject.put("signOrLunchTitle", cardInfoContent.card.title);
                zsonObject.put("signText1", cardInfoContent.card.text_1);

                //刷新卡片
                CommonUtil.updateCardForm(ability, formId, zsonObject);
            } else {
                //没有值卡片不更新,将POI-ID清除，显示议程卡片
                DatabaseUtils.updatePoiId(formId, poiId + noUpdateStr);
                updateAgendaCard(ability);
            }
        });
    }

    /**
     * 刷新积分互动卡片
     *
     * @param ability ability
     * @param poiId   poiId
     * @param formId  卡片ID
     */
    private static void updateIntegralCard(Ability ability, String poiId, long formId) {
        ability.getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(() -> {
            //获取卡片数据
            CardInfoContent cardInfoContent = HttpUtil.getHarmonyCardGetCardByPoid(poiId);
            if (cardInfoContent != null && cardInfoContent.card != null) {
                //获取剩余积分
                String totalScore = "";
                if (!TextTool.isNullOrEmpty(MyApplication.getSess())) {
                    totalScore = HttpUtil.getTotalScore();
                }
                ZSONObject zsonObject = new ZSONObject();
                //卡片样式
                CeliaCardStyle celiaCardStyle = new CeliaCardStyle();
                celiaCardStyle.bgClass1 = "card-bg2";
                celiaCardStyle.hasIntegralBtn = true;
                celiaCardStyle.isIntegralCard = true;
                //卡片内容
                zsonObject.put("integralTitle", cardInfoContent.card.title);
                zsonObject.put("integralText", cardInfoContent.card.text_1);
                if (!TextTool.isNullOrEmpty(totalScore)) {
                    zsonObject.put("integralNum", totalScore);
                } else {
                    //没有值显示未登录状态
                    celiaCardStyle.integralText = "登录HDC2023，赢取码力值";
                }
                zsonObject.put("style", celiaCardStyle);
                //刷新卡片
                CommonUtil.updateCardForm(ability, formId, zsonObject);
            } else {
                //没有值卡片不更新,将POI-ID清除，显示议程卡片
                DatabaseUtils.updatePoiId(formId, poiId + noUpdateStr);
                updateAgendaCard(ability);
            }
        });
    }

    /**
     * 更新单个卡片
     *
     * @param ability         ability
     * @param formId          卡片ID
     * @param formBindingData 卡片数据
     */
    private static void updateCardForm(Ability ability, long formId, FormBindingData formBindingData) {
        try {
            ability.updateForm(formId, formBindingData);
        } catch (FormException e) {
            DatabaseUtils.deleteCardData(formId);
            e.printStackTrace();
        }
    }

    /**
     * 获取二维码
     *
     * @param text 二维码内容
     * @return 图像信息
     */
    private static byte[] getImage(String text) {
        final int IMAGE_LENGTH = 100;
        byte[] byteArray = new byte[IMAGE_LENGTH * IMAGE_LENGTH * 4];
        iBarcodeDetector.detect(text, byteArray, IMAGE_LENGTH, IMAGE_LENGTH);
        return byteArray;
    }
}