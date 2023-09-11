package com.smarket.hdc2023;

import com.smarket.hdc2023.bean.*;
import com.smarket.hdc2023.common.Config;
import com.smarket.hdc2023.common.InternalBattery;
import com.smarket.hdc2023.help.*;
import com.smarket.hdc2023.http.HttpUtil;
import com.smarket.hdc2023.vo.CategoryVO;
import com.smarket.hdc2023.vo.SearchVO;
import com.smarket.hdc2023.vo.SubSeminarBeanVO;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.ace.ability.AceAbility;
import ohos.ace.ability.AceInternalAbility;
import ohos.agp.utils.TextTool;
import ohos.bundle.IBundleManager;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.utils.IntentConstants;
import ohos.utils.net.Uri;
import ohos.utils.zson.ZSONObject;

import java.util.List;
import java.util.Map;

import static com.smarket.hdc2023.common.InternalBattery.*;

public class BatteryInternalAbility extends AceInternalAbility {

    private static final String TAG = BatteryInternalAbility.class.getSimpleName();


    private static BatteryInternalAbility instance;
    private static AceAbility ability;

    private static final String BUNDLE_NAME = "com.smarket.hdc2023";

    private static final String ABILITY_NAME = "BatteryInternalAbility";


    private BatteryInternalAbility() {
        super(BUNDLE_NAME, ABILITY_NAME);
    }

    /**
     * Business execution
     *
     * @param code   Request Code.
     * @param data   Receives MessageParcel object.
     * @param reply  The MessageParcel object is returned.
     * @param option Indicates whether the operation is synchronous or asynchronous.
     * @return If the operation is successful, true is returned. Otherwise, false is returned.
     */
    public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) {
        LogUtil.e(code + "");
        String dataStr = data.readString();
        LogUtil.e(dataStr);
        LogUtil.e("更改消息容量：" + reply.setCapacity(Integer.MAX_VALUE));
        switch (code) {
            case INTER_TASKQUERY:
                HttpUtil.getInteractionTaskQuery(reply);
                break;
            case GUEST_IMGS:
                HttpUtil.getGuestimgs(reply);
                break;
            case CHECK_:
                boolean bn = MyApplication.getISNormalTicket();
                reply.writeString(bn ? "0" : "1");
                break;
            case GET_TIME:
                ServerTime serverTime1 = HttpUtil.getServerTime();
                HttpBean httpBean1 = new HttpBean();
                String day = "";
                if (serverTime1 != null) {
                    day = TimeHelper.getDayPattern6ByTime(serverTime1.getServerTime());
                }
                httpBean1.setData(day);
                reply.writeString(ZSONObject.toZSONString(httpBean1));
                break;
            case GET_HDHOTEL:
                HttpUtil.getHDHotel(reply, dataStr);
                break;
            case CREATE_RESERVE:
                ServerTime serverTime = HttpUtil.getServerTime();
                if (serverTime != null) {
                    if (TextTool.isNullOrEmpty(MyApplication.getSess())) {
                        HttpBean httpBean = new HttpBean();
                        httpBean.setStratus(-2);
                        reply.writeString(ZSONObject.toZSONString(httpBean));
                        break;
                    }
                    CalendarInfo calendarInfo = ZSONObject.stringToClass(dataStr, CalendarInfo.class);
                    long currentTime = serverTime.getServerTime() * 1000;
                    long startTime = Long.parseLong(calendarInfo.startTime) * 1000;
                    long endTime = Long.parseLong(calendarInfo.endTime) * 1000;
                    HttpBean httpBean = new HttpBean();
                    if (currentTime > endTime) {
                        httpBean.setStratus(-6);
                        reply.writeString(ZSONObject.toZSONString(httpBean));
                    } else {
                        List<String> subs;//已预约ID
                        String ReserveId;//本次预约ID
                        if(calendarInfo.isDay) {
                            subs = MyApplication.getTechHouridlist();
                            ReserveId = calendarInfo.agendaId;
                        }else {
                            subs = MyApplication.getidlist();
                            ReserveId = calendarInfo.subSeminarId;
                        }
                        if (subs != null && subs.size() > 0 && subs.contains(ReserveId)) {
                            if (currentTime > startTime) {
                                httpBean.setStratus(-4);
                                reply.writeString(ZSONObject.toZSONString(httpBean));
                            } else {
                                try {
                                    CalendarReminderUtils.deleteCalendarEvent(ability, calendarInfo.title);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (calendarInfo.isDay) {
                                    HttpUtil.deleteReserveByTechHour(reply, calendarInfo.subSeminarId, calendarInfo.agendaId);
                                } else {
                                    HttpUtil.deleteReserve(reply, calendarInfo.subSeminarId);
                                }
                            }
                        } else {
                            if (currentTime < startTime && currentTime > (startTime - 3600000)) {
                                httpBean.setStratus(-3);
                                reply.writeString(ZSONObject.toZSONString(httpBean));
                            } else if (currentTime > startTime) {
                                httpBean.setStratus(-5);
                                reply.writeString(ZSONObject.toZSONString(httpBean));
                            } else {
                                if (calendarInfo.isDay) {
                                    HttpUtil.createReserveByTechHour(reply, calendarInfo.subSeminarId, calendarInfo.agendaId);
                                } else {
                                    HttpUtil.createReserve(reply, calendarInfo.subSeminarId);
                                }
                            }
                        }
                    }
                } else {
                    HttpBean httpBean = new HttpBean();
                    httpBean.setStratus(-1);
                    httpBean.setData("网络繁忙，请稍后重试！");
                    reply.writeString(ZSONObject.toZSONString(httpBean));
                }
                break;
            case ADD_CALENDAR:
                LogUtil.e(ability.verifySelfPermission("ohos.permission.WRITE_CALENDAR") + "");
                if (ability.verifySelfPermission("ohos.permission.WRITE_CALENDAR") != IBundleManager.PERMISSION_GRANTED) {
                    // 应用未被授予权限
                    if (ability.canRequestPermission("ohos.permission.WRITE_CALENDAR")) {
                        reply.writeString("OK");
                    } else {
                        // 显示应用需要权限的理由，提示用户进入设置授权
                        reply.writeString("ERROR");
                    }
                }else {
                    //添加日历
                    CalendarInfo calendarInfo1 = ZSONObject.stringToClass(dataStr, CalendarInfo.class);
                    int r = CalendarReminderUtils.addCalendarEvent(ability, calendarInfo1.title, calendarInfo1.location, calendarInfo1.description,
                            Long.valueOf(calendarInfo1.startTime), Long.valueOf(calendarInfo1.endTime));
                    if (r == 0) {
                        reply.writeString("SUCCESS");
                    } else {
                        reply.writeString("ADD_ERROR");
                    }
                }
                break;
            case SET_CALENDAR:
                if (ability.verifySelfPermission("ohos.permission.WRITE_CALENDAR") != IBundleManager.PERMISSION_GRANTED) {
                    // 应用未被授予权限
                    if (ability.canRequestPermission("ohos.permission.WRITE_CALENDAR")) {
                        // 是否可以申请弹框授权(首次申请或者用户未选择禁止且不再提示)
                        ability.requestPermissionsFromUser(
                                new String[]{"ohos.permission.WRITE_CALENDAR", "ohos.permission.READ_CALENDAR"}, 1111);
                    } else {
                        // 显示应用需要权限的理由，提示用户进入设置授权
                        reply.writeString("ERROR");
                    }
                }else {
                    // 权限已被授予
                    reply.writeString("OK");
                }
                break;
            case LOGIN_OUT:
                MyApplication.outLogin();
                reply.writeString("");
                break;
            case LOGIN_HW:
                LoginInternalBean param2 = new LoginInternalBean();
                try {
                    param2 = ZSONObject.stringToClass(dataStr, LoginInternalBean.class);
                } catch (RuntimeException e) {
                    LogUtil.e("convert failed." + e.getMessage());
                }
                HttpUtil.loginHW(param2.phone, reply,ability);
                break;
            case ADD_ACTION:
                HttpUtil.postMatomo(dataStr);
                MyApplication.pageStr = dataStr;
                break;
            case ADD_ACTION_:
                HttpUtil.postMatomo("议程详情");
                MyApplication.pageStr = "议程详情";
                AgendaDetailPage agendaDetailPage = ZSONObject.stringToClass(dataStr, AgendaDetailPage.class);
                MyApplication.agendaDetailId = null;
                MyApplication.agendaDetail = null;
                MyApplication.agendaDetailTH = null;
                if (agendaDetailPage.type == 0) {
                    MyApplication.agendaDetailId = agendaDetailPage.id;
                } else if (agendaDetailPage.type == 1) {
                    MyApplication.agendaDetail = agendaDetailPage.bean;
                } else {
                    MyApplication.agendaDetailTH = agendaDetailPage.bean;
                }
                break;
            case InternalBattery.CHECK_LOGIN:
                HttpUtil.checkSess(reply);
                //reply.writeString(TextTool.isNullOrEmpty(MyApplication.getSess()) ? "1" : "0");
//                try {
//                    FormControllerManager formControllerManager = FormControllerManager.getInstance(context);
//                    List<Long> formId = formControllerManager.getAllFormIdFromSharePreference();
//                    for (Long each : formId) {
//                        FormController formController = formControllerManager.getController(each);
//                        formController.updateFormData(each);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                break;
            case InternalBattery.CHECK_USER:
                HttpUtil.checkUser(reply);
                break;
            case InternalBattery.RESERVE_ID_LIST:
                //HttpUtil.gethdsubArticleListByJson(HUDONG_ID);
                //HttpUtil.getTHsubArticleListByJson(TECH_ID);
                HttpUtil.getReserveIdlist(reply);
                break;
            case InternalBattery.RESERVE_TECHHOUR_ID_LIST:
                HttpUtil.getReserveTechHourIdlist(reply);
                break;
            case InternalBattery.RESERVE_ID_LIST_1:
                List<CategoryVO> response = null;
                AgendasSearch agendasSearchs = ZSONObject.stringToClass(dataStr, AgendasSearch.class);
                Map<String, List<CategoryVO>> resultMap = HttpUtil.querySubSeminarListForMySeminar();
                switch (agendasSearchs.day) {
                    case 1:
                    default:
                        if(!MyApplication.getUserInfo().i_ticketName.contains("单日")){
                            response = resultMap.get("08/04");
                        }
                        break;
                    case 2:
                        response = resultMap.get("08/05");
                        break;
                    case 3:
                        response = resultMap.get("08/06");
                        break;
                }
                if (response != null && response.size() < 1) {
                    reply.writeString(null);
                } else {
                    reply.writeString(ZSONObject.toZSONString(response));
                }
                break;
            case InternalBattery.FILTER_DIC_P:
                HttpUtil.getTree(Config.FILTER_DIC_P, reply);
                break;
            case InternalBattery.FILTER_DIC_R:
                HttpUtil.getTree(Config.FILTER_DIC_R, reply);
                break;
            case InternalBattery.QUERY_SUB_SEMINAR:
                List<SubSeminarBeanVO> list = HttpUtil.queryNewSubSeminarByJson(reply);
                reply.writeString(ZSONObject.toZSONString(list));
                break;
            case InternalBattery.QUERY_SUB_SEMINAR_1:
                SearchVO searchVO = ZSONObject.stringToClass(dataStr, SearchVO.class);
                HttpUtil.querySearchNewSubSeminarByJson(reply, searchVO);
                LogUtil.e("查询结果：" + ZSONObject.toZSONString(MyApplication.searchBackList));
                reply.writeString(ZSONObject.toZSONString(MyApplication.searchBackList));
                break;
            case InternalBattery.QUERY_SUB_SEMINAR_GET:
                String back9012 = HttpUtil.subSeminarGetByJSON(dataStr, reply);
                reply.writeString(back9012);
                LogUtil.e("back9012:" + back9012);

                break;
            case InternalBattery.USER_INFO:
                HttpUtil.seminarContact(null, MyApplication.getSess(),null);
                UserInfo userInfo = MyApplication.getUserInfo();
                SeminarInfo seminarInfo = HttpUtil.getSeminarInfoByjsonAndCard();
                UserInfoBack userInfoBack = new UserInfoBack();
                userInfoBack.name = userInfo.name;
                userInfoBack.qrCode = userInfo.qrCode;
                userInfoBack.enterprise = userInfo.enterprise;
                userInfoBack.ticketType = userInfo.i_ticketType;
                userInfoBack.time = TimeHelper.getDate(seminarInfo.values.startTime) + "-" + TimeHelper.getDayByDate(seminarInfo.values.endTime);
                userInfoBack.add = seminarInfo.values.address;
                userInfoBack.ticketName = userInfo.i_ticketName;
                reply.writeString(ZSONObject.toZSONString(userInfoBack));
                break;
            case InternalBattery.BATTERY_SEND_CODE:
                LoginInternalBean param = new LoginInternalBean();
                try {
                    param = ZSONObject.stringToClass(dataStr, LoginInternalBean.class);
                } catch (RuntimeException e) {
                    LogUtil.e("convert failed." + e.getMessage());
                }
                String phone = param.phone;
                LogUtil.e(phone);
                HttpUtil.sendCode(phone, reply);
                break;
            case InternalBattery.BATTERY_GET_TOKEN:
                LoginInternalBean param1 = new LoginInternalBean();
                try {
                    param1 = ZSONObject.stringToClass(dataStr, LoginInternalBean.class);
                } catch (RuntimeException e) {
                    LogUtil.e("convert failed." + e.getMessage());
                }
                HttpUtil.getToken(param1.phone, param1.code, reply,ability);
                break;
            case InternalBattery.SEMINAR_INFO:
//                ActionHelper.getSeminarInfo(reply);
                HttpUtil.getSeminarInfoByjson(reply);
                break;
            case InternalBattery.SEMINAR_BANNER:
                HttpUtil.getSeminarBanner(reply);
                break;
            case InternalBattery.HAS_AGREE_CONCEAL:
                String concealResult = MyApplication.getConceal();
                reply.writeString(concealResult);
                LogUtil.e("xue隐私:" + concealResult);
                break;
            case InternalBattery.AGREE_CONCEAL:
                MyApplication.setAgreeConceal();
                break;
            case InternalBattery.DIS_AGREE_CONCEAL:
//                System.exit(0);
                ability.terminateAbility();
                break;
            case InternalBattery.ARTICLE_DETAIL:
//                HttpUtil.getsubArticleList(reply, dataStr);
                break;
            case InternalBattery.OPEN_SYSTEM_BROWSER:
                Intent intent = new Intent();
                Operation mOperation = new Intent.OperationBuilder()
                        .withUri(Uri.parse(String.format(dataStr)))
                        .build();
                intent.setOperation(mOperation);
                ability.startAbility(intent);
                break;
            case InternalBattery.ADD_TO_HOME:
                //判断卡片任务是否完成
                //页面内加桌不给积分了
                //HttpUtil.completeTask("DESKTOP_ID");
                CommonUtil.AddToHome(ability);
                break;
            case OPEN_SHOW:
                String url = "";
                if (dataStr.contains("openShow1")) {
                    url = MyApplication.getApplication().getResourceString(ResourceTable.String_openShow1);
                } else {
                    url = MyApplication.getApplication().getResourceString(ResourceTable.String_openShow2);
                }
                Intent jsIntent = new Intent();
                Operation jsOperation = new Intent.OperationBuilder()
                        .withUri(Uri.parse(url))
                        .withBundleName("com.huawei.himovie")
                        .withFlags(Intent.FLAG_ABILITY_NEW_MISSION)
                        .build();
                jsIntent.setOperation(jsOperation);
                ability.startAbility(jsIntent);
                break;
            case InternalBattery.CLOSE_KEY_BOARD:
                break;
            case GUEST_LIST:
                GuestSearch guestSearch = ZSONObject.stringToClass(dataStr, GuestSearch.class);
                HttpUtil.getGuestList(reply, guestSearch);
                break;
            case MORE_SERVICES:
                HttpUtil.getMoreServices(reply);
                break;
            case GUEST_INFO:
                HttpUtil.getGuestInfo(reply, dataStr);
                break;
            case PARTNER_LIST:
                HttpUtil.PartnerList(reply, dataStr);
                break;
            case InternalBattery.OPEN_WEBVIEW:
                //打开WEBVIEW
                CommonUtil.OpenWebview(dataStr, ability);
                break;
            case InternalBattery.OPEN_PETAL:
                Intent petalIntent = new Intent();
                Operation petalOperation = new Intent.OperationBuilder()
                        .withDeviceId("")
                        .withBundleName("com.huawei.fastapp")
                        .withUri(Uri.parse("hap://app/com.hw.map.petaltravels/Hello?start_utm_source=hm&start_utm_campaign=HDC"))
                        .withAction(IntentConstants.ACTION_VIEW_DATA)
                        .withFlags(Intent.FLAG_NOT_OHOS_COMPONENT)
                        .build();
                petalIntent.setOperation(petalOperation);
                ability.startAbility(petalIntent);
                break;
            case INDEX_IMGS:
                HttpUtil.getIndexSwiperList(reply);
                break;
            case TRAVEL_SWIPER:
                HttpUtil.getTravelSwiperList(reply);
                break;
            case TRAVEL_LIST:
                HttpUtil.getTravelList(reply);
                break;
            case TRAVEL_DETAIL:
                HttpUtil.getTravelDetail(dataStr,reply);
                break;
            case PLAY_AUDIO:
                //播放语音
                AudioPlayUtil.goPlay(ability, dataStr, null, null, null, 1, null);
                break;
            case PLAY_STOP:
                //结束语音服务
                AudioPlayUtil.stopConnect(ability,1);
                break;
            case PLAY_STATUS:
                //查询语音播放状态
                AudioPlayUtil.queryJsPlayStatus(ability,reply,1);
                break;
            case ADD_PAGE_VIEW_EVENT:
                //上报用户浏览页面事件
                ZSONObject pageViewJson = ZSONObject.stringToZSON(dataStr);
                HiAnalyticsUtil.addPageViewEvent(ability,pageViewJson.getString("pageName"),pageViewJson.getIntValue("duration"));
                break;
            case ADD_MODULAR_CLICK_EVENT:
                //上报用户点击功能按钮事件
                HiAnalyticsUtil.addModularClickEvent(ability,dataStr);
                break;
            default:
                reply.writeString("service not defined");
                return false;
        }
        return true;
    }

    /**
     * BatteryInternalAbility
     *
     * @return If the instance is NULL, Get new instance. Otherwise, instance is returned.
     */
    public static BatteryInternalAbility getInstance() {
        if (instance == null) {
            synchronized (BatteryInternalAbility.class) {
                if (instance == null) {
                    instance = new BatteryInternalAbility();
                }
            }
        }
        return instance;
    }

    /**
     * init Internal ability
     */
    public void register(AceAbility abilitys) {
        ability = abilitys;
        this.setInternalAbilityHandler(this::onRemoteRequest, abilitys);
    }

    /**
     * Internal ability release
     */
    public void deregister() {
        this.setInternalAbilityHandler(null);
    }
}
