package com.smarket.hdc2023.http;

import com.smarket.hdc2023.MyApplication;
import com.smarket.hdc2023.ResourceTable;
import com.smarket.hdc2023.bean.*;
import com.smarket.hdc2023.common.BASE64;
import com.smarket.hdc2023.common.Config;
import com.smarket.hdc2023.common.MD5;
import com.smarket.hdc2023.common.PageAction;
import com.smarket.hdc2023.help.CeliaCardUtil;
import com.smarket.hdc2023.help.LogUtil;
import com.smarket.hdc2023.help.TimeHelper;
import com.smarket.hdc2023.vo.*;
import ohos.aafwk.ability.Ability;
import ohos.agp.utils.TextTool;
import ohos.rpc.MessageParcel;
import ohos.system.DeviceInfo;
import ohos.system.version.SystemVersion;
import ohos.utils.zson.ZSONArray;
import ohos.utils.zson.ZSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.smarket.hdc2023.common.AES256.randomivValue;
import static com.smarket.hdc2023.common.Config.*;

public class HttpUtil {
    public static final String HTTPS = "https:";

    /*
     * 统计
     * */
    public static void postMatomo(String actionName) {

        List<String> os = new ArrayList<>();
        os.add("OS");
        os.add("HarmonyOS " + SystemVersion.getVersion());
        List<String> mobile = new ArrayList<>();
        mobile.add("Matomo Mobile Version");
        mobile.add(DeviceInfo.getName());
        List<String> locale = new ArrayList<>();
        locale.add("Locale");
        locale.add(DeviceInfo.getLocale());
        Map<String, List<String>> cvar = new HashMap<>();
        cvar.put("1", os);
        cvar.put("2", mobile);
        cvar.put("3", locale);
        String urlString = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_MATOMO) + "matomo.php?rec=1";
        urlString = urlString + "&_cvar=" + ZSONObject.toZSONString(cvar);
        urlString = urlString + "&action_name=" + actionName;
        urlString = urlString + "&idsite=200";
        urlString = urlString + "&rand=" + new Date().getTime();
        urlString = urlString + "&url=https://" + actionName;
        HttpBean httpBean = HttpApi.get(urlString);
        if (httpBean.getStratus() == 0) {
            LogUtil.e("统计上传成功");
        } else {
            LogUtil.e("统计上传失败：" + httpBean.getStratus());
        }

    }

    /*
     * 获取积分列表
     * */
    public static void getInteractionTaskQuery(MessageParcel reply) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_PROJECT) + MyApplication.getApplication().getResourceString(ResourceTable.String_interactionTaskQuery);
        Map<String, Object> map = new HashMap<>();
        map.put("activityId", ACTIVITYID);
        map.put("sess", MyApplication.getSess());
        map.put("groupName", "元服务专属任务");
        map.put("returnStatus", 1);
        String params = ZSONObject.toZSONString(map);
        HttpBean httpBean = HttpApi.post(urlStr, params);
        if (httpBean.getStratus() == 0) {
            TaskHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), TaskHttp.class);
            if (baseHttp.code == 0) {
                List<TaskList> taskList = ZSONArray.stringToZSONArray(ZSONObject.toZSONString(baseHttp.data)).toJavaList(TaskList.class);
                reply.writeString(ZSONObject.toZSONString(taskList));
                LogUtil.e(ZSONObject.toZSONString(taskList));
            } else {
                httpBean.setStratus(-1);
                httpBean.setData(baseHttp.msg);
                reply.writeString(ZSONObject.toZSONString(httpBean));
            }

        } else {
            reply.writeString(ZSONObject.toZSONString(httpBean));
            LogUtil.e("获取积分列表失败：" + httpBean.getStratus());
        }
    }

    /*
     * 获取嘉宾头像列表
     * */
    public static void getGuestimgs(MessageParcel reply) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + Config.TENANT_ID + "/seminar/" + MyApplication.getApplication().getResourceString(ResourceTable.String_SEMINAR_ID) + "/guest.json";
        HttpBean httpBean = HttpApi.get(urlStr);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            List<Guestimgs> guestimgs = ZSONArray.stringToZSONArray(ZSONObject.toZSONString(baseHttp.body.content)).toJavaList(Guestimgs.class);
            List<Guestimgs> guestimgsBack = HttpData.checkGuestImgs(guestimgs);
            LogUtil.e("获取嘉宾头像列表：" + ZSONObject.toZSONString(guestimgsBack));
            reply.writeString(ZSONObject.toZSONString(guestimgsBack));
        } else {
            LogUtil.e("获取嘉宾头像列表失败：" + httpBean.getStratus());

            getGuestimgsByApi(reply, "");


        }
    }

    private static final String regEx_html = "<[^>]+>";

    /*
     * 获取活动体验的地址
     * */
    public static void getHDHotel(MessageParcel reply, String id) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID + "/article/" + id + ".json";
        HttpBean httpBean = HttpApi.get(urlStr);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            HelpContent content = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), HelpContent.class);
            String back = String.valueOf(content.content);
            Pattern p1 = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            Matcher matcher1 = p1.matcher(back);
            back = matcher1.replaceAll(" ").replaceAll("&quot", "");
            LogUtil.e("获取活动体验的地址:" + back);
            reply.writeString(back);
        } else {
            LogUtil.e("获取活动体验的地址失败：" + httpBean.getStratus());
            reply.writeString(null);
        }
    }

    /*
     * 获取嘉宾头像列表
     * */
    public static void getGuestimgsByApi(MessageParcel reply, String guestName) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST) + MyApplication.getApplication().getResourceString(ResourceTable.String_seminarGuestGetList);
        Map<String, Object> map = new HashMap<>();

        map.put("seminarId", SEMINAR_ID);
        map.put("tenantId", TENANT_ID);

        String params = ZSONObject.toZSONString(map);
        HttpBean httpBean = HttpApi.post(urlStr, params);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            List<Guestimgs> guestimgs = ZSONArray.stringToZSONArray(ZSONObject.toZSONString(baseHttp.body.content)).toJavaList(Guestimgs.class);
            List<Guestimgs> guestimgsBack = HttpData.checkGuestImgs(guestimgs);
            List<Guestimgs> result = new ArrayList<>();
            if (!TextTool.isNullOrEmpty(guestName)) {
                result = guestimgsBack.stream().filter(a -> a.name.contains(guestName)).collect(Collectors.toList());
            } else {
                result = guestimgsBack;
            }
            reply.writeString(ZSONObject.toZSONString(result));
        } else {
            LogUtil.e("获取嘉宾头像列表失败：" + httpBean.getStratus());

        }
    }

    /*
     * 获取验证码
     * */
    public static void sendCode(String phone, MessageParcel reply) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_PROJECT) + MyApplication.getApplication().getResourceString(ResourceTable.String_memberSendVerificationCode);
        Map<String, Object> map = new HashMap<>();
        map.put("key", phone);
        map.put("content", "");
        map.put("smsSign", SMS_SIGN);
        map.put("memberFormId", Config.MEMBER_FROM_ID);
        String params = ZSONObject.toZSONString(map);
        HttpBean httpBean = HttpApi.post(urlStr, params);
        if (httpBean.getStratus() == 0) {
            LogUtil.e("获取验证码：" + httpBean.getData());
            reply.writeString("0");
        } else {
            reply.writeString(httpBean.getStratus() + "");
            LogUtil.e("获取验证码失败：" + httpBean.getStratus());
        }
    }

    /*
     * 校验验证码
     * */
    public static void getToken(String phone, String code, MessageParcel reply, Ability ability) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST) + MyApplication.getApplication().getResourceString(ResourceTable.String_memberGetToken);
        Map<String, Object> map = new HashMap<>();
        map.put("unique", phone);
        map.put("checkCode", code);
        map.put("formId", MEMBER_FROM_ID);
        map.put("schemaId", SCHEMA_ID);
        map.put("action", "all");

        String params = ZSONObject.toZSONString(map);
        HttpBean httpBean = HttpApi.post(urlStr, params);
        if (httpBean.getStratus() == 0) {
            LogUtil.e("校验验证码：" + httpBean.getData());
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);

            GetTokenBody getTokenBody = null;
            if (baseHttp.body.content != null && !TextTool.isNullOrEmpty(String.valueOf(baseHttp.body.content))) {
                getTokenBody = ZSONObject.stringToClass(String.valueOf(baseHttp.body.content), GetTokenBody.class);
                login(getTokenBody.token, phone, reply,ability);
            } else {
                reply.writeString(baseHttp.body.desc + "");
            }
        } else {
            reply.writeString("网络繁忙，请稍后重试！");
            LogUtil.e("检验验证码失败：" + httpBean.getStratus());
        }
    }

    public static void loginHW(String serverAuthCode, MessageParcel reply, Ability ability) {
        try {
            String clientId = MyApplication.getApplication().getResourceString(ResourceTable.String_clientId);
            String clientSecret = MyApplication.getApplication().getResourceString(ResourceTable.String_clientSecret);
            String getAccessTokenUrl = MyApplication.getApplication().getResourceString(ResourceTable.String_getAccessTokenUrl);
            String getMobileNumberUrl = MyApplication.getApplication().getResourceString(ResourceTable.String_getMobileNumberUrl);
            StringBuilder getAccessTokenParam = new StringBuilder();
            getAccessTokenParam.append("redirect_uri=" + URLEncoder.encode("hms://redirect_uri", "UTF-8"));
            getAccessTokenParam.append("&code=" + URLEncoder.encode(serverAuthCode, "UTF-8"));
            getAccessTokenParam.append("&client_secret=" + URLEncoder.encode(clientSecret, "UTF-8"));
            getAccessTokenParam.append("&client_id=" + URLEncoder.encode(clientId, "UTF-8"));
            getAccessTokenParam.append("&grant_type=" + URLEncoder.encode("authorization_code", "UTF-8"));
            HttpBean getAccessTokenHttpBean = HttpApi.post2(getAccessTokenUrl, getAccessTokenParam.toString());
            if (getAccessTokenHttpBean.getStratus() == 0) {
                String accessToken = ZSONObject.stringToZSON(getAccessTokenHttpBean.getData()).getString("access_token");
                if (TextTool.isNullOrEmpty(accessToken)) {
                    reply.writeString("请输入您的报名手机号！");
                    LogUtil.e("登录失败：未获取到accessToken");
                } else {
                    HttpBean getMobileNumberHttpBean = HttpApi.post2(getMobileNumberUrl, "access_token=" + URLEncoder.encode(accessToken, "UTF-8"));
                    if (getMobileNumberHttpBean.getStratus() == 0) {
                        String mobileNumber = ZSONObject.stringToZSON(getMobileNumberHttpBean.getData()).getString("mobileNumber");
                        if (TextTool.isNullOrEmpty(mobileNumber)) {
                            reply.writeString("请输入您的报名手机号！");
                            LogUtil.e("登录失败：未获取到mobileNumber");
                        } else {
                            String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_PROJECT) + MyApplication.getApplication().getResourceString(ResourceTable.String_apiLoginHW);
                            Map<String, Object> map = new HashMap<>();
                            map.put("password", MD5.toMd5(mobileNumber + "D3E5A54CF083BBC30439BB519FE60FCA"));
                            map.put("mobile", mobileNumber);
                            String params = ZSONObject.toZSONString(map);
                            HttpBean httpBean = HttpApi.post(urlStr, params);
                            if (httpBean.getStratus() == 0) {
                                BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
                                if (baseHttp.body.result == 0) {
                                    LogUtil.e("登录：" + baseHttp.body.content);
                                    LoginBody loginBody = ZSONObject.stringToClass(String.valueOf(baseHttp.body.content), LoginBody.class);
                                    LogUtil.e(ZSONObject.toZSONString(loginBody));
                                    seminarContact(reply, loginBody.session, ability);
                                } else {
                                    reply.writeString(baseHttp.body.desc);
                                }
                            } else {
                                reply.writeString(httpBean.getStratus() + "");
                                LogUtil.e("登录失败：" + httpBean.getStratus());
                            }
                        }
                    } else {
                        reply.writeString(getMobileNumberHttpBean.getStratus() + "");
                        LogUtil.e("登录失败getMobileNumber：" + getMobileNumberHttpBean.getStratus());
                    }
                }
            } else {
                reply.writeString(getAccessTokenHttpBean.getStratus() + "");
                LogUtil.e("登录失败getAccessToken：" + getAccessTokenHttpBean.getStratus());
            }
        } catch (Exception e) {
            reply.writeString("请输入您的报名手机号！");
            LogUtil.e("loginHW：" + e);
        }
    }

    /*
     * 登录
     * */
    public static void login(String token, String phone, MessageParcel reply, Ability ability) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST) + MyApplication.getApplication().getResourceString(ResourceTable.String_memberLogin);
        Map<String, Object> map = new HashMap<>();
        map.put("tenantId", TENANT_ID);
        map.put("loginType", "hw_unionId");
        map.put("memberFormId", MEMBER_FROM_ID);
        map.put("schemaId", SCHEMA_ID);
        map.put("globalUserId", MyApplication.getGlobalUserId());
        map.put("token", token);
        map.put("verify", "");
        map.put("unique", phone);
        String params = ZSONObject.toZSONString(map);
        HttpBean httpBean = HttpApi.post(urlStr, params);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            if (baseHttp.body.result == 0) {
                LogUtil.e("登录：" + baseHttp.body.content);
//                MyApplication.sess = baseHttp.body.
                LoginBody loginBody = ZSONObject.stringToClass(String.valueOf(baseHttp.body.content), LoginBody.class);
                LogUtil.e(ZSONObject.toZSONString(loginBody));
                seminarContact(reply, loginBody.session,ability);
//                contactUpdate( reply);
            } else {
                reply.writeString(baseHttp.body.desc);
            }

        } else {
            reply.writeString(httpBean.getStratus() + "");
            LogUtil.e("登录失败：" + httpBean.getStratus());
        }
    }

    /*
     * 获取用户报名信息
     * */
    public static void seminarContact(MessageParcel reply, String session, Ability ability) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_PROJECT) + MyApplication.getApplication().getResourceString(ResourceTable.String_apiSeminarContactsGet);
        Map<String, Object> map = new HashMap<>();
        map.put("tenantId", TENANT_ID);
        map.put("seminarId", SEMINAR_ID);
        map.put("sess", session);
        map.put("openId", "");
        map.put("globalUserId", MyApplication.getGlobalUserId());
        String params = ZSONObject.toZSONString(map);
        HttpBean httpBean = HttpApi.post(urlStr, params);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            if (baseHttp.body.result == 0) {
                UserInfo userInfo = ZSONObject.stringToClass(String.valueOf(baseHttp.body.content), UserInfo.class);
                if (userInfo.signUpStatusCode == 2 || userInfo.signUpStatusCode == 3) {
                    String name = "";
                    String enterprise = "";
                    String i_ticketType = "";
                    String isNormalTicket = "";
                    String i_status = "";
                    String qrCode = "";
                    for (UserInfo.item item : userInfo.items) {
                        switch (item.fieldName) {
                            case "name":
                                name = item.value;
                                break;
                            case "enterprise":
                                enterprise = item.value;
                                break;
                            case "i_ticketType":
                                i_ticketType = item.value;
                                break;
                            case "i_status":
                                i_status = item.value;
                                break;
                            case "i_cardNumber":
                                qrCode = item.value;
                                break;
                            case "i_ticketTypeName":
                                isNormalTicket = item.value;
                                break;
                            default:
                                break;

                        }
                    }
                    if (i_status.equals("1")) {
                        MyApplication.setISNormalTicket((TextTool.isNullOrEmpty(isNormalTicket) || "普通票".equals(isNormalTicket)));
                        MyApplication.getPreferences().putString("name", name).flush();
                        MyApplication.getPreferences().putString("qrCode", qrCode).flush();
                        MyApplication.getPreferences().putString("enterprise", enterprise).flush();
                        MyApplication.getPreferences().putString("i_ticketType", i_ticketType).flush();
                        MyApplication.getPreferences().putString("i_ticketName", isNormalTicket).flush();
                        MyApplication.getPreferences().putString("checkInStatus", userInfo.checkInStatus).flush();
                        MyApplication.getPreferences().putString("giftSpecialRight", userInfo.giftSpecialRight).flush();
                        String ticketName = "";
                        if(isNormalTicket.equals("专家票")){
                            ticketName = "内场";
                        }else if(isNormalTicket.contains("VIP")){
                            ticketName = "VIP";
                        }
                        MyApplication.getPreferences().putString("ticketName", ticketName).flush();
                        MyApplication.setSess(session);

                        //登录成功后刷新卡片
                        if(ability!=null){
                            CeliaCardUtil.updateCeliaCard(ability);
                        }

                        if (reply != null) {
                            httpBean.setStratus(0);
                            httpBean.setData("");
                            reply.writeString("success");
                        }
                    } else {
                        if (reply != null) {
                            httpBean.setStratus(-1);
                            httpBean.setData("未购票");
                            reply.writeString("未购票");
                        }
                    }


                } else {
                    if (reply != null) {
                        httpBean.setStratus(-1);
                        httpBean.setData("未购票");
                        reply.writeString("未购票");
                    }
                }
                LogUtil.e(ZSONObject.toZSONString(baseHttp.body.content));
            } else {
                if (reply != null) {
                    httpBean.setStratus(-1);
                    httpBean.setData(baseHttp.body.desc);
                    reply.writeString(ZSONObject.toZSONString(httpBean));
                }
            }

        } else {
            reply.writeString(ZSONObject.toZSONString(httpBean));
            LogUtil.e("登录失败：" + httpBean.getStratus());
        }
    }

    /*
     * 获取预定的分会场ID
     * */
    public static void getReserveIdlist(MessageParcel reply) {
        if (TextTool.isNullOrEmpty(MyApplication.getSess()) || TextTool.isNullOrEmpty(MyApplication.getGlobalUserId())) {
            HttpBean httpBean = new HttpBean();
            httpBean.setStratus(-1);
            reply.writeString(ZSONObject.toZSONString(httpBean));
            return;
        }

        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST) + MyApplication.getApplication().getResourceString(ResourceTable.String_apiSeminarSubseminarReserveGetidlist);
        Map<String, Object> map = new HashMap<>();
        map.put("tenantId", TENANT_ID);
        map.put("seminarId", SEMINAR_ID);
        map.put("sess", MyApplication.getSess());
        map.put("globalUserId", MyApplication.getGlobalUserId());
        String params = ZSONObject.toZSONString(map);
        HttpBean httpBean = HttpApi.post(urlStr, params);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            if (baseHttp.body.result == 0) {
                IdListContent idListContent = ZSONObject.stringToClass(String.valueOf(baseHttp.body.content), IdListContent.class);
                MyApplication.setidlist(ZSONObject.toZSONString(idListContent.items));
                reply.writeString(ZSONObject.toZSONString(idListContent.items));
                LogUtil.e(ZSONObject.toZSONString(idListContent.items));
            } else {
                httpBean.setStratus(-1);
                httpBean.setData(baseHttp.body.desc);
                reply.writeString(ZSONObject.toZSONString(httpBean));
            }

        } else {
            reply.writeString(ZSONObject.toZSONString(httpBean));
            LogUtil.e("获取预定的分会场ID失败：" + httpBean.getStratus());
        }
    }

    /*
     * 获取预定的TechHour ID
     * */
    public static void getReserveTechHourIdlist(MessageParcel reply) {
        if (TextTool.isNullOrEmpty(MyApplication.getSess()) || TextTool.isNullOrEmpty(MyApplication.getGlobalUserId())) {
            HttpBean httpBean = new HttpBean();
            httpBean.setStratus(-1);
            reply.writeString(ZSONObject.toZSONString(httpBean));
            return;
        }

        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_PROJECT) + MyApplication.getApplication().getResourceString(ResourceTable.String_apiSeminarSubseminarReservetechHourGetList);
        Map<String, Object> map = new HashMap<>();
        map.put("seminarId", SEMINAR_ID);
        map.put("sess", MyApplication.getSess());
        String params = ZSONObject.toZSONString(map);
        HttpBean httpBean = HttpApi.post(urlStr, params);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            if (baseHttp.body.result == 0) {
                List<TechHourIdListContent> TechHourIdListContent = ZSONArray.stringToZSONArray(ZSONObject.toZSONString(baseHttp.body.content)).toJavaList(TechHourIdListContent.class);
                List<String> ls = new ArrayList<>();
                for (int i = 0; i < TechHourIdListContent.size(); i++) {
                    ls.add(TechHourIdListContent.get(i).agendaId);
                }
                MyApplication.setTechHouridlist(ZSONObject.toZSONString(ls));
                reply.writeString(ZSONObject.toZSONString(ls));
                LogUtil.e(ZSONObject.toZSONString(ls));
            } else {
                httpBean.setStratus(-1);
                httpBean.setData(baseHttp.body.desc);
                reply.writeString(ZSONObject.toZSONString(httpBean));
            }

        } else {
            reply.writeString(ZSONObject.toZSONString(httpBean));
            LogUtil.e("获取预定的TechHour ID失败：" + httpBean.getStratus());
        }
    }

    public static List<String> getReserveIdlist() {
        if (TextTool.isNullOrEmpty(MyApplication.getSess()) || TextTool.isNullOrEmpty(MyApplication.getGlobalUserId())) {
            MyApplication.setidlist("");
            return null;
        }
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST) + MyApplication.getApplication().getResourceString(ResourceTable.String_apiSeminarSubseminarReserveGetidlist);
        Map<String, Object> map = new HashMap<>();
        map.put("tenantId", TENANT_ID);
        map.put("seminarId", SEMINAR_ID);
        map.put("sess", MyApplication.getSess());
        map.put("globalUserId", MyApplication.getGlobalUserId());
        String params = ZSONObject.toZSONString(map);
        HttpBean httpBean = HttpApi.post(urlStr, params);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            if (baseHttp.body.result == 0) {
                IdListContent idListContent = ZSONObject.stringToClass(String.valueOf(baseHttp.body.content), IdListContent.class);
                MyApplication.setidlist(ZSONObject.toZSONString(idListContent.items));
                LogUtil.e(ZSONObject.toZSONString(baseHttp.body.content));
            } else {
                MyApplication.setidlist("");
            }

        }
        return MyApplication.getidlist();
    }

    /*
     * 获取字典表
     * */
    @SuppressWarnings("unchecked")
    public static void getTree(String id, MessageParcel reply) {

        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST) + MyApplication.getApplication().getResourceString(ResourceTable.String_dicParamsGetTree);
        Map<String, Object> map = new HashMap<>();

        map.put("tenantId", TENANT_ID);
        map.put("dicId", id);
        String params = ZSONObject.toZSONString(map);
        HttpBean httpBean = HttpApi.post(urlStr, params);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            if (baseHttp.body.result == 0) {
                Map<String, Object> mapContent = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), Map.class);
                Object content = mapContent.get(id);
                List<TreeBean> treeBeanList = ZSONArray.stringToZSONArray(ZSONObject.toZSONString(content)).toJavaList(TreeBean.class);
                TreeBean treeBean = new TreeBean();
                treeBean.name = "全部";
                treeBean.isClick = true;
                List<TreeBean> treeBeanListBack = new ArrayList<>();
                treeBeanListBack.add(treeBean);
                treeBeanListBack.addAll(treeBeanList);
                reply.writeString(ZSONObject.toZSONString(treeBeanListBack));
                LogUtil.e(ZSONObject.toZSONString(treeBeanListBack));
            } else {
                httpBean.setStratus(-1);
                httpBean.setData(baseHttp.body.desc);
                reply.writeString(ZSONObject.toZSONString(httpBean));
            }

        } else {
            reply.writeString(ZSONObject.toZSONString(httpBean));
            LogUtil.e("登录失败：" + httpBean.getStratus());
        }
    }

    /*
     * 获取卡片议程数据
     * */
    @SuppressWarnings("unchecked")
    public static List<CardData> getsimplelist() {
        List<CardData> cardDataList = new ArrayList<>();

        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST) + MyApplication.getApplication().getResourceString(ResourceTable.String_projectHwhdcGetSimpleList);
        ServerTime serverTime1 = HttpUtil.getServerTime();
        Map<String, Object> map = new HashMap<>();
        List<Sort> sort = new ArrayList<>();
        sort.add(new Sort());
        map.put("tenantId", TENANT_ID);
        map.put("seminarId", SEMINAR_ID);
        map.put("startTime", serverTime1 != null ? serverTime1.getServerTime() : TimeHelper.getTimeBy10());
//        map.put("startTime", "1632751878");
//        map.put("startTime", "1635060954");
        map.put("canReserve", 1);
        map.put("articleCategoryId", new String[]{TECH_ID, HUDONG_ID});
        map.put("sort", sort);
        map.put("start", 0);
        map.put("num", 4);

        String params = ZSONObject.toZSONString(map);
        HttpBean httpBean = HttpApi.post(urlStr, params);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            Map<String, Object> mapContent = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), Map.class);
            Object content = mapContent.get("items");
            byte[] converttoBytes = String.valueOf(content).replace("\\\\", "\\").getBytes(StandardCharsets.UTF_8);
            String itemsStr = new String(converttoBytes, StandardCharsets.UTF_8);
            List<SubSeminarBean> subSeminarBeans = ZSONArray.stringToZSONArray(itemsStr).toJavaList(SubSeminarBean.class);
            subSeminarBeans = HttpData.checkSubSeminarBeanByCard(subSeminarBeans);
            for (SubSeminarBean subSeminarBean : subSeminarBeans) {
                CardData cardData = new CardData();
                cardData.subSeminarId = subSeminarBean.subSeminarId;
                cardData.name = subSeminarBean.hmName.contains("|") ? subSeminarBean.hmName.substring(0, subSeminarBean.hmName.indexOf("|")) : subSeminarBean.hmName;
                cardData.hmDay = subSeminarBean.hmDay;
                cardData.hmStartAndEndTime = subSeminarBean.hmStartAndEndTime;
                cardData.Agendatype = subSeminarBean.hmTitle;
                cardData.startTime = subSeminarBean.startTime;
                cardData.endTime = subSeminarBean.endTime;
                cardData.isSelect = !(cardData.Agendatype.contains(PageAction.INTERACTION) || cardData.Agendatype.contains(PageAction.MAIN) || cardData.Agendatype.contains(PageAction.TECHOUR) || cardData.Agendatype.contains(PageAction.BREAK_FAST) || cardData.Agendatype.contains(PageAction.CODE));


                LogUtil.e("卡片数据：" + ZSONObject.toZSONString(cardData));
                cardDataList.add(cardData);
            }
            cardDataList = HttpData.sortCardData(cardDataList);
            for (int i = 0; i < cardDataList.size(); i++) {
                MyApplication.getPreferences().putString("card" + i, ZSONObject.toZSONString(cardDataList.get(i))).flush();
            }
            LogUtil.e(ZSONObject.toZSONString(content));

        }
        return cardDataList;
    }

    @SuppressWarnings("unchecked")
    public static List<SubSeminarBeanVO> queryNewSubSeminarByJson(MessageParcel reply) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID + "/seminar/" + MyApplication.getApplication().getResourceString(ResourceTable.String_SEMINAR_ID) + "/subSeminar/list.json";
        HttpBean httpBean = HttpApi.get(urlStr);
        List<SubSeminarBeanVO> subSeminarBeanVOS = new ArrayList<>();
        if (httpBean.getStratus() == 0) {
            LogUtil.e("1数据解析------");
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            LogUtil.e("2数据解析------");
            if (baseHttp.body.result == 0) {
                try {
                    //刷新我预约的议程ID
                    getReserveIdlist(MessageParcel.create());

                    Map<String, Object> mapContent = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), Map.class);
                    LogUtil.e("3数据解析------");
                    Object content = mapContent.get("items");
                    List<SubSeminarBean> treeBeanList = ZSONArray.stringToZSONArray(ZSONObject.toZSONString(content)).toJavaList(SubSeminarBean.class);
                    //按日期分组
                    Map<Object, List<SubSeminarBean>> groupData = treeBeanList.stream().collect(Collectors.groupingBy(i -> (new SimpleDateFormat("MM/dd")).format(Long.parseLong(i.getStartTime() + "000"))));

                    Map<String, Map<Object, List<SubSeminarBean>>> _groupData = new HashMap<>();

                    for (Map.Entry<Object, List<SubSeminarBean>> data : groupData.entrySet()) {
                        if (data != null) {
                            Map<Object, List<SubSeminarBean>> subGroupData = data.getValue().stream().collect(Collectors.groupingBy(i -> i.getM_categoryI()[0]));
                            //最外层的时间
                            _groupData.put(data.getKey().toString(), subGroupData);
                            SubSeminarBeanVO subSeminarBeanVO = new SubSeminarBeanVO();
                            subSeminarBeanVO.setDate(data.getKey().toString());
                            List<CategoryItems> categoryItemsList = new ArrayList<>();
                            subGroupData.entrySet().stream().forEach(_data -> {
                                CategoryItems categoryItems = new CategoryItems();
                                if (_data.getValue().size() > 0) {
                                    //获取sub item
                                    categoryItems.setName(_data.getKey().toString());
                                    Map<Object, List<SubSeminarBean>> subS = _data.getValue().stream().collect(Collectors.groupingBy(sub -> sub.getM_meetingTypeCn()[0]));
                                    List<CategoryVO> categoryVOList = new ArrayList<>();
                                    //获取已预约的议程ID
                                    List<String> idList = MyApplication.getidlist();
                                    for (Map.Entry<Object, List<SubSeminarBean>> subSeminarBean : subS.entrySet()) {
                                        CategoryVO categoryVO = new CategoryVO();
                                        for (int i = 0; i < subSeminarBean.getValue().size(); i++) {
                                            Boolean isTheme = false;
                                            if (subSeminarBean.getValue().get(i).getM_meetingTypeCn()[0].contains("主题演讲")) {
                                                isTheme = true;
                                            }
                                            //判断是否是主题演讲
                                            String[] canReserveNames = {"开发者主题演讲", "Codelabs", "技术论坛"};//Tech Hour列表不约
                                            List<String> canReserveNameList = Arrays.stream(canReserveNames).collect(Collectors.toList());
                                            categoryVO.setM_contentCategory(subSeminarBean.getValue().get(i).getM_contentCategory());
                                            categoryVO.setM_crowdArea(subSeminarBean.getValue().get(i).getM_crowdArea());
                                            if (isTheme) {

                                                String name = subSeminarBean.getValue().get(i).getM_meetingTypeCn()[0];
                                                categoryVO.setGroupName(name);

                                                //给开发者主题演讲预约议程使用
                                                SubSeminarBean ssb = subSeminarBean.getValue().get(i);
                                                if (canReserveNameList.contains(name)) {
                                                    categoryVO.setCanReserve(1);
                                                }
                                                if (idList.contains(ssb.getSubSeminarId())) {
                                                    categoryVO.setReserve(true);
                                                }
                                                categoryVO.setId(ssb.getSubSeminarId());
                                                categoryVO.setName(ssb.getName());
                                                categoryVO.setStartTimeLong(ssb.getStartTime());
                                                categoryVO.setEndTimeLong(ssb.getEndTime());

                                                List<AgendasVO> voList = new ArrayList<>();
                                                if(subSeminarBean.getValue().get(i).getAgendaList().size()>0){
                                                    voList = subSeminarBean.getValue().get(i).getAgendaList().get(0).getAgendas();
                                                }
                                                if (voList.size() > 0) {
                                                    List<TimeLineVO> timeLineVOS = new ArrayList<>();
                                                    for (int j = 0; j < voList.size(); j++) {
                                                        TimeLineVO timeLineVO = new TimeLineVO();
                                                        AgendasVO ss = voList.get(j);
                                                        timeLineVO.setId(ss.getAgendaId());
                                                        timeLineVO.setRemark(ss.getRemark());
                                                        timeLineVO.setName(ss.getName());
                                                        timeLineVO.setStartTime((new SimpleDateFormat("HH:mm")).format(Long.parseLong(ss.getStartTime() + "000")));
                                                        timeLineVO.setEndTime((new SimpleDateFormat("HH:mm")).format(Long.parseLong(ss.getEndTime() + "000")));
                                                        timeLineVO.setEndTimeLong(ss.getEndTime());
                                                        timeLineVO.setStartTimeLong(ss.getStartTime());
                                                        timeLineVO.setGuest(ss.getGuests());
                                                        timeLineVO.setHotel(ss.getAddress());
                                                        timeLineVO.setM_crowdArea(subSeminarBean.getValue().get(i).getM_crowdArea());
                                                        timeLineVO.setM_contentCategory(subSeminarBean.getValue().get(i).getM_contentCategory());
                                                        timeLineVOS.add(timeLineVO);

                                                    }
                                                    categoryVO.setTimeLine(timeLineVOS);
                                                }
                                                String startTime = (new SimpleDateFormat("HH:mm")).format(Long.parseLong(subSeminarBean.getValue().get(i).getStartTime() + "000"));
                                                String endTime = (new SimpleDateFormat("HH:mm")).format(Long.parseLong(subSeminarBean.getValue().get(i).getEndTime() + "000"));
                                                categoryVO.setGroupTitle(subSeminarBean.getValue().get(i).getName());
                                                categoryVO.setGroupDesc(subSeminarBean.getValue().get(i).getM_parseIntro());
                                                categoryVO.setGroupTime(startTime + " - " + endTime);
                                                categoryVO.setGroupAddress(subSeminarBean.getValue().get(i).getHotel());
                                            } else {
                                                String name = subSeminarBean.getValue().get(i).getM_meetingTypeCn()[0];
                                                if (name.equals("Codelabs")) {
                                                    categoryVO.setGroupName(name);
                                                    categoryVO.setGroupAddress("B3教学楼一楼中庭南侧（彼陂咖啡旁）");
                                                    categoryVO.setGroupDesc("Codelabs提供技术专家指导、教程和动手编码体验，在这里你能了解和体验到华为的尖端技术，并与华为技术专家&全国各地的Coder分享知识和见解，一起探索代码的独特魅力，更有丰富的奖励等您来赢取！");
                                                    categoryVO.setGroupTime("09:00-17:00");
                                                    categoryVO.setGroupTips("Codelabs 需提前预约后方可参加");
                                                    SubSeminarBean ssb = subSeminarBean.getValue().get(0);
                                                    if (idList.contains(ssb.getSubSeminarId())) {
                                                        categoryVO.setReserve(true);
                                                    }
                                                    categoryVO.setId(ssb.getSubSeminarId());
                                                    categoryVO.setName(ssb.getName());
                                                    categoryVO.setStartTimeLong(ssb.getStartTime());
                                                    categoryVO.setEndTimeLong(ssb.getEndTime());
                                                    categoryVO.setCanReserve(1);
                                                } else {
                                                    categoryVO.setGroupName(name);
                                                }
                                                if (subSeminarBean.getValue().size() > 0) {// && !name.equals("Codelabs")
                                                    List<TimeLineVO> timeLineVOS = new ArrayList<>();
                                                    for (int j = 0; j < subSeminarBean.getValue().size(); j++) {
                                                        TimeLineVO timeLineVO = new TimeLineVO();
                                                        SubSeminarBean ss = subSeminarBean.getValue().get(j);
                                                        if (canReserveNameList.contains(name)) {
                                                            timeLineVO.setCanReserve(1);
                                                        }
                                                        if (idList.contains(ss.getSubSeminarId())) {
                                                            timeLineVO.setReserve(true);
                                                        }
                                                        timeLineVO.setId(ss.getSubSeminarId());
                                                        timeLineVO.setRemark(ss.getM_parseIntro());
                                                        timeLineVO.setName(ss.getName());
                                                        timeLineVO.setStartTimeLong(ss.getStartTime());
                                                        timeLineVO.setEndTimeLong(ss.getEndTime());
                                                        timeLineVO.setStartTime((new SimpleDateFormat("HH:mm")).format(Long.parseLong(ss.getStartTime() + "000")));
                                                        timeLineVO.setEndTime((new SimpleDateFormat("HH:mm")).format(Long.parseLong(ss.getEndTime() + "000")));
                                                        timeLineVO.setEndTimeLong(ss.getEndTime());
                                                        timeLineVO.setStartTimeLong(ss.getStartTime());
                                                        timeLineVO.setOrder(ss.getM_subSeminarOrder());
                                                        timeLineVO.setHotel(ss.getHotel());
                                                        timeLineVO.setM_crowdArea(ss.getM_crowdArea());
                                                        timeLineVO.setM_contentCategory(ss.getM_contentCategory());
                                                        List<AgendasVO> list = new ArrayList<>();
                                                        if (ss.getAgendaList().size() > 0) {
                                                            for (int k = 0; k < ss.getAgendaList().get(0).getAgendas().size(); k++) {
                                                                list.add(ss.getAgendaList().get(0).getAgendas().get(k));
                                                            }
                                                        }
                                                        timeLineVO.setAgendaVO(list);
                                                        timeLineVOS.add(timeLineVO);
                                                    }

                                                    /*Collections.sort(timeLineVOS, new Comparator<TimeLineVO>() {
                                                        @Override
                                                        public int compare(TimeLineVO t1, TimeLineVO t2) {
                                                            int result = t2.getOrder().compareTo(t1.getOrder());
                                                            if(result==0){
                                                                result = t1.getStartTimeLong().compareTo(t2.getStartTimeLong());
                                                                if(result==0){
                                                                    result = t1.getEndTimeLong().compareTo(t2.getEndTimeLong());
                                                                }
                                                            }
                                                            return result;

                                                        }
                                                    });*/
                                                    timeLineVOS.sort(Comparator.comparing(TimeLineVO::getOrder).reversed().thenComparing(TimeLineVO::getStartTimeLong).thenComparing(TimeLineVO::getEndTimeLong).thenComparing(TimeLineVO::getId));
                                                    categoryVO.setTimeLine(timeLineVOS);
                                                }
                                            }
                                        }

                                        if (categoryVO.getGroupName() != null && !categoryVO.getGroupName().equals("早餐会") && !categoryVO.getGroupName().equals("音乐节")) {
                                            categoryVOList.add(categoryVO);

                                            //自定义排序
                                            String[] timeLineNames = {"主题演讲", "开发者主题演讲", "技术论坛", "HarmonyOS生态峰会", "鸿蒙生态峰会", "Codelabs", "Tech. Hour", "互动体验", "HarmonyOS极客马拉松"};
                                            Collections.sort(categoryVOList, new Comparator<CategoryVO>() {
                                                @Override
                                                public int compare(CategoryVO t1, CategoryVO t2) {
                                                    return Arrays.stream(timeLineNames).collect(Collectors.toList()).indexOf(t1.getGroupName()) - Arrays.stream(timeLineNames).collect(Collectors.toList()).indexOf(t2.getGroupName());
                                                }
                                            });
                                            categoryItems.setGroup(categoryVOList);
                                        }
                                    }
                                }
                                categoryItemsList.add(categoryItems);
                            });
                            //自定义排序
                            String[] categoryNames = {"主题演讲", "开发者主题演讲", "论坛活动", "开发者活动"};
                            Collections.sort(categoryItemsList, new Comparator<CategoryItems>() {
                                @Override
                                public int compare(CategoryItems t1, CategoryItems t2) {
                                    return Arrays.stream(categoryNames).collect(Collectors.toList()).indexOf(t1.getName()) - Arrays.stream(categoryNames).collect(Collectors.toList()).indexOf(t2.getName());
                                }
                            });
                            subSeminarBeanVO.setCategory(categoryItemsList);
                            subSeminarBeanVOS.add(subSeminarBeanVO);
                        }
                    }
                    subSeminarBeanVOS.sort(Comparator.comparing(SubSeminarBeanVO::getDate));

                    LogUtil.e("4数据解析------" + subSeminarBeanVOS.toString());


                    MyApplication.searchBackList = subSeminarBeanVOS;

                } catch (Exception e) {
                    LogUtil.e("分会场解析：" + e.getMessage());
                }

            } else {
                if (reply != null) {
                    httpBean.setStratus(-1);
                    httpBean.setData(baseHttp.body.desc);
                    reply.writeString("");
                }
            }
        } else {
//            querySubSeminar(day, reply);
            LogUtil.e("获取分会场列表失败：" + httpBean.getStratus());
        }
        return subSeminarBeanVOS;
    }

    /**
     * @param reply
     * @param searchVO
     */
    public static void querySearchNewSubSeminarByJson(MessageParcel reply, SearchVO searchVO) {
//        List<SubSeminarBeanVO> backList =MyApplication.backList;
        List<SubSeminarBeanVO> backList = queryNewSubSeminarByJson(reply);
        String searchText = searchVO.getSearchText().trim();
        String[] category = searchVO.getCategory();
        String[] crowdArea = searchVO.getCrowdArea();
        List<String> categoryList = Arrays.asList(category);
        List<String> crowdAreaList = Arrays.asList(crowdArea);

        for (SubSeminarBeanVO seminarBeanVO : backList) {
            if ((categoryList.size() > 0 || crowdAreaList.size() > 0) && seminarBeanVO.getDate().equals("08/04")) {
                continue;
            }
            for (CategoryItems categoryItem : seminarBeanVO.getCategory()) {
                List<CategoryVO> categoryVOS = categoryItem.getGroup();
                for (CategoryVO categoryVO : categoryVOS) {
                    List<TimeLineVO> oldTimeLineVO = categoryVO.getTimeLine();
                    List<TimeLineVO> timeLineVOList = new ArrayList<>(oldTimeLineVO);
                    timeLineVOList = timeLineVOList.stream().filter(s -> {
                        boolean search = true;
                        if (!searchText.isEmpty()) {
                            search = s.getHotel().contains(searchText) || s.getName().contains(searchText) || s.getRemark().contains(searchText);
                            if (!search) {
                                if (s.getGuest() != null) {
                                    List<Guest> guests = s.getGuest().stream().filter(g -> g.name.contains(searchText)).collect(Collectors.toList());
                                    s.setGuest(guests);
                                    search = true;
                                }
                            }
                        }
                        return search;
                    }).collect(Collectors.toList());
                    timeLineVOList = timeLineVOList.stream().filter(s -> {
                        boolean pbool = true;
                        for (String str : crowdAreaList) {
                            if (s.getM_crowdArea() != null && s.getM_crowdArea().length > 0) {
                                if (str != "") {
                                    pbool = Arrays.asList(s.getM_crowdArea()).contains(str);
                                    if (pbool) {
                                        break;
                                    }
                                }

                            } else {
                                pbool = false;
                            }
                        }
                        return pbool;
                    }).collect(Collectors.toList());

                    timeLineVOList = timeLineVOList.stream().filter(s -> {
                        boolean pbool = true;
                        for (String str : categoryList) {
                            if (s.getM_contentCategory() != null && s.getM_contentCategory().length > 0) {
                                if (str != "") {
                                    pbool = Arrays.asList(s.getM_contentCategory()).contains(str);
                                    if (pbool) {
                                        break;
                                    }
                                }

                            } else {
                                pbool = false;
                            }
                        }
                        return pbool;
                    }).collect(Collectors.toList());
                    categoryVO.setTimeLine(timeLineVOList);
                }
            }
        }
        MyApplication.searchBackList = backList;

    }

    public static Map<String, List<CategoryVO>> querySubSeminarListForMySeminar() {
        Map<String, List<CategoryVO>> responseMap = new HashMap<>();
        List<String> idList = MyApplication.getidlist();
        if (idList == null) {
            idList = new ArrayList<>();
        }
        List<String> HidList = MyApplication.getTechHouridlist();
        if (HidList == null) {
            HidList = new ArrayList<>();
        }
        if (MyApplication.searchBackList == null || MyApplication.searchBackList.size() == 0) {
            queryNewSubSeminarByJson(null);
        }
        if (MyApplication.searchBackList != null) {
            for (SubSeminarBeanVO subSeminarBeanVO : MyApplication.searchBackList) {
                List<CategoryVO> groupList = new ArrayList<>();
                responseMap.put(subSeminarBeanVO.getDate(), groupList);
                if (subSeminarBeanVO.getCategory() != null) {
                    for (CategoryItems categoryItems : subSeminarBeanVO.getCategory()) {
                        if (subSeminarBeanVO.getCategory() != null) {
                            for (CategoryVO categoryVO : categoryItems.getGroup()) {
                                List<TimeLineVO> list = new ArrayList<>();
                                if (categoryVO.getTimeLine() != null) {
                                    for (TimeLineVO timeLineVO : categoryVO.getTimeLine()) {
                                        if (idList.contains(timeLineVO.getId()) || categoryVO.getGroupName().equals("主题演讲")) {
                                            if (categoryVO.getGroupName().equals("Tech. Hour")) {
                                                List<AgendasVO> la = new ArrayList<>();
                                                for (int ii = 0; ii < timeLineVO.getAgendaVO().size(); ii++) {
                                                    if (HidList.contains(timeLineVO.getAgendaVO().get(ii).getAgendaId())) {
                                                        la.add(timeLineVO.getAgendaVO().get(ii));
                                                    }
                                                }
                                                timeLineVO.setAgendaVO(la);
                                            }
                                            list.add(timeLineVO);
                                        }
                                        if (idList.contains(categoryVO.getId()) && categoryVO.getGroupName().equals("开发者主题演讲")) {
                                            list.add(timeLineVO);
                                        }
                                    }
                                    if (list.size() > 0) {
                                        CategoryVO item = new CategoryVO();
                                        item.setGroupName(categoryVO.getGroupName());
                                        item.setGroupId(categoryVO.getGroupId());
                                        item.setId(categoryVO.getId());
                                        item.setName(categoryVO.getName());
                                        item.setEndTimeLong(categoryVO.getEndTimeLong());
                                        item.setStartTimeLong(categoryVO.getStartTimeLong());
                                        item.setTimeLine(list);
                                        groupList.add(item);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return responseMap;
    }

    public static boolean enableClick(String id) {
        boolean enable = true;
        if (MyApplication.searchBackList == null || MyApplication.searchBackList.size() == 0) {
            queryNewSubSeminarByJson(null);
        }
        if (MyApplication.searchBackList != null) {
            for (SubSeminarBeanVO subSeminarBeanVO : MyApplication.searchBackList) {
                if (subSeminarBeanVO.getCategory() != null) {
                    for (CategoryItems categoryItems : subSeminarBeanVO.getCategory()) {
                        if (subSeminarBeanVO.getCategory() != null) {
                            for (CategoryVO categoryVO : categoryItems.getGroup()) {
                                if (categoryVO.getTimeLine() != null) {
                                    for (TimeLineVO timeLineVO : categoryVO.getTimeLine()) {
                                        if (timeLineVO.getId().equals(id) && categoryVO.getGroupName().contains("主题演讲")) {
                                            enable = false;
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
        return enable;
    }


    /*
     * 获取分会场列表
     * */
    @SuppressWarnings("unchecked")
    public static void querySubSeminarByJson(MessageParcel reply) {
        MyApplication.backList1 = new ArrayList<>();
        MyApplication.backList2 = new ArrayList<>();
        MyApplication.backList3 = new ArrayList<>();
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID + "/seminar/" + MyApplication.getApplication().getResourceString(ResourceTable.String_SEMINAR_ID) + "/subSeminar/list.json";
        HttpBean httpBean = HttpApi.get(urlStr);
        if (httpBean.getStratus() == 0) {
            LogUtil.e("1数据解析------");
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            LogUtil.e("2数据解析------");
            if (baseHttp.body.result == 0) {
                try {


                    Map<String, Object> mapContent = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), Map.class);
                    LogUtil.e("3数据解析------");
                    Object content = mapContent.get("items");
                    List<SubSeminarBean> treeBeanList = ZSONArray.stringToZSONArray(ZSONObject.toZSONString(content)).toJavaList(SubSeminarBean.class);
                    LogUtil.e("4数据解析------");
                    List<SubSeminarBean> backList1 = new ArrayList<>();
                    List<SubSeminarBean> backList2 = new ArrayList<>();
                    List<SubSeminarBean> backList3 = new ArrayList<>();
                    for (SubSeminarBean subSeminarBean : treeBeanList) {
                        if (TimeHelper.isDay(Long.valueOf(subSeminarBean.startTime), "2021.10.22", "2021.10.23")) {
                            backList1.add(subSeminarBean);
                        }
                        if (TimeHelper.isDay(Long.valueOf(subSeminarBean.startTime), "2021.10.23", "2021.10.24")) {
                            backList2.add(subSeminarBean);
                        }
                        if (TimeHelper.isDay(Long.valueOf(subSeminarBean.startTime), "2021.10.24", "2021.11.25")) {
                            backList3.add(subSeminarBean);
                        }
                    }


                    MyApplication.backList1 = HttpData.checkSubSeminarBean(backList1);
                    MyApplication.backList2 = HttpData.checkSubSeminarBean(backList2);
                    MyApplication.backList3 = HttpData.checkSubSeminarBean(backList3);
                    LogUtil.e("4数据解析------");
                } catch (Exception e) {
                    LogUtil.e("分会场解析：" + e.getMessage());
                }
                httpBean.setStratus(0);
                httpBean.setData("");
                reply.writeString("");
            } else {
                httpBean.setStratus(-1);
                httpBean.setData(baseHttp.body.desc);
                reply.writeString("");
            }

        } else {
//            querySubSeminar(day, reply);
            LogUtil.e("获取分会场列表失败：" + httpBean.getStratus());
        }
    }

    /*
     * 获取分会场列表
     * */
    @SuppressWarnings("unchecked")
    public static void querySubSeminar(int day, MessageParcel reply) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST) + MyApplication.getApplication().getResourceString(ResourceTable.String_apiSeminarSubseminarInformationQuery);
        Map<String, Object> map = new HashMap<>();
        List<String> expandInfo = new ArrayList<>();
        expandInfo.add("agenda");
        map.put("tenantId", TENANT_ID);
        map.put("seminarId", SEMINAR_ID);
        map.put("expandInfo", expandInfo);
        map.put("start", 0);
        map.put("num", 50);

        String params = ZSONObject.toZSONString(map);
        HttpBean httpBean = HttpApi.post(urlStr, params);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            if (baseHttp.body.result == 0) {
                Map<String, Object> mapContent = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), Map.class);
                Object content = mapContent.get("items");
                List<SubSeminarBean> treeBeanList = ZSONArray.stringToZSONArray(ZSONObject.toZSONString(content)).toJavaList(SubSeminarBean.class);
                List<SubSeminarBean> backList = new ArrayList<>();
                for (SubSeminarBean subSeminarBean : treeBeanList) {
                    if (TimeHelper.isDay(Long.valueOf(subSeminarBean.startTime), "2021.10." + day, "2021.10." + (day + 1))) {
                        backList.add(subSeminarBean);
                    }
                }
                reply.writeString(ZSONObject.toZSONString(backList));
                LogUtil.e(ZSONObject.toZSONString(backList));
            } else {
                httpBean.setStratus(-1);
                httpBean.setData(baseHttp.body.desc);
                reply.writeString(ZSONObject.toZSONString(httpBean));
            }

        } else {
            reply.writeString(ZSONObject.toZSONString(httpBean));
            LogUtil.e("获取分会场列表失败：" + httpBean.getStratus());
        }
    }

    /*
     * 获取分会场详情
     * */
    public static String subSeminarGetByJSON(String id, MessageParcel reply) {
//        id = id.replaceAll("\"", "");
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID + "/seminar/" + MyApplication.getApplication().getResourceString(ResourceTable.String_SEMINAR_ID) + "/subSeminar/" + Long.valueOf(id) + ".json";
        HttpBean httpBean = HttpApi.get(urlStr);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            if (baseHttp.body.result == 0) {
                LogUtil.e(ZSONObject.toZSONString(baseHttp.body.content));
                AgendaDetail agendaDetail = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), AgendaDetail.class);
                LogUtil.e("111111111111111");
                LogUtil.e(ZSONObject.toZSONString(agendaDetail.detail));
                httpBean.setStratus(0);
                try {
                    //刷新我预约的议程ID
                    if (agendaDetail.detail.m_meetingTypeCn.equals("Tech. Hour")) {
                        getReserveTechHourIdlist(MessageParcel.create());
                    } else {
                        getReserveIdlist(MessageParcel.create());
                    }

                    AgendaDetailBack agendaDetailBack = HttpData.checkAgendaDetailBack(agendaDetail);

                    LogUtil.e(ZSONObject.toZSONString(agendaDetailBack));
                    return ZSONObject.toZSONString(agendaDetailBack);

                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e("转换异常：" + e.getMessage());
                }
            } else {
                httpBean.setStratus(-1);
                httpBean.setData(baseHttp.body.desc);
            }
            reply.writeString(null);
        } else {
            LogUtil.e("xue2222获取分会场详情失败：" + httpBean.getStratus());
            subSeminarGet(id, reply);
        }
        return null;
    }

    /*
     * 获取分会场详情
     * */
    public static void subSeminarGet(String id, MessageParcel reply) {

        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST) + MyApplication.getApplication().getResourceString(ResourceTable.String_apiSeminarSubseminarGet);
        Map<String, Object> map = new HashMap<>();
        List<String> expandInfo = new ArrayList<>();
        expandInfo.add("agenda");
        expandInfo.add("guest");
        map.put("tenantId", TENANT_ID);
        map.put("subSeminarId", Long.valueOf(id));
        map.put("expandInfo", expandInfo);
        String params = ZSONObject.toZSONString(map);
        HttpBean httpBean = HttpApi.post(urlStr, params);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            if (baseHttp.body.result == 0) {
//                Map<String, Object> mapContent = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), Map.class);
//                Object content = mapContent.get("items");
//                List<SubSeminarBean> treeBeanList = ZSONArray.stringToZSONArray(ZSONObject.toZSONString(content)).toJavaList(SubSeminarBean.class);
//                List<SubSeminarBean> backList = new ArrayList<>();
//                for (SubSeminarBean subSeminarBean : treeBeanList) {
//                    if (TimeHelper.isDay(Long.valueOf(subSeminarBean.startTime), "2021.10."+day, "2021.10."+(day+1))) {
//                        backList.add(subSeminarBean);
//                    }
//                }
//                reply.writeString(ZSONObject.toZSONString(backList));
                LogUtil.e(ZSONObject.toZSONString(baseHttp.body.content));
            } else {
                httpBean.setStratus(-1);
                httpBean.setData(baseHttp.body.desc);
                reply.writeString(ZSONObject.toZSONString(httpBean));
            }

        } else {
            reply.writeString(ZSONObject.toZSONString(httpBean));
            LogUtil.e("登录失败：" + httpBean.getStratus());
        }
    }


    /*
     * 获取用户ID
     * */
    public static void getUserId() {

        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST) + MyApplication.getApplication().getResourceString(ResourceTable.String_anonymousGetId);
        Map<String, Object> map = new HashMap<>();
        map.put("clientVersion", DeviceInfo.getModel());
        map.put("clientBrand", "hw_unionId");
        map.put("clientType", DeviceInfo.getName());
        map.put("clientIP", "");
        String params = ZSONObject.toZSONString(map);
        HttpBean httpBean = HttpApi.post(urlStr, params);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            MyApplication.getApplication().getPreferences().putString("globalUserId", String.valueOf(baseHttp.body.content)).flush();
        } else {
            LogUtil.e("获取用户ID失败：" + httpBean.getStratus());
        }
    }

    /*
     * 获取文章列表
     * */
    public static void getsubArticleListByJson(MessageParcel reply, String id) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID + "/seminar/" + MyApplication.getApplication().getResourceString(ResourceTable.String_SEMINAR_ID) + "/article/list/" + id + ".json";
        HttpBean httpBean = HttpApi.get(urlStr);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            HomeAgendasContent homeAgendasContent = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), HomeAgendasContent.class);
            List<SubSeminar> subSeminars = new ArrayList<>();
            if (homeAgendasContent.items != null && homeAgendasContent.items.size() > 0) {
                if (homeAgendasContent.items.size() > 10) {
                    subSeminars.addAll(homeAgendasContent.items.subList(0, 9));
                } else {
                    subSeminars.addAll(homeAgendasContent.items);
                }

                String back = ZSONObject.toZSONString(HttpData.checkSubSeminars(subSeminars));
                LogUtil.e("getsubArticleListByJson:" + back);
                reply.writeString(back);
            }

        } else {
            LogUtil.e("获取用户ID失败：" + httpBean.getStratus());
//            getSeminarInfo(reply);
        }
    }

    /*
     * 获取互动文章列表
     * */
    public static void gethdsubArticleListByJson(String id) {
        LogUtil.e("获取互动文章列表:");
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID + "/article/list/" + id + ".json";
        HttpBean httpBean = HttpApi.get(urlStr);
        BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
        try {
            HdBeanList hdBeanList = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), HdBeanList.class);
            MyApplication.hdBeans = new ArrayList<>();
            MyApplication.hdBeans.addAll(hdBeanList.items);
        } catch (Exception e) {
            LogUtil.e("获取互动文章列表:" + e.getMessage());
        }
    }

    /*
     * 获取Techour文章列表
     * */
    public static void getTHsubArticleListByJson(String id) {
        LogUtil.e("获取Techour列表:");
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID + "/article/list/" + id + ".json";
        HttpBean httpBean = HttpApi.get(urlStr);
        BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
        try {
            HdBeanList hdBeanList = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), HdBeanList.class);
            MyApplication.tHBeans = new ArrayList<>();
            MyApplication.tHBeans.addAll(hdBeanList.items);
        } catch (Exception e) {
            LogUtil.e("获取Techour列表:" + e.getMessage());
        }
    }

    /*
     * 获取文章详情
     * */
//    public static void getsubArticleList(MessageParcel reply, String id) {
//        String jsonId = "";
//        switch (id) {
//            case "班车信息":
//                jsonId = HELP_TRAFFIC_ID;
//                break;
//            case "自助交通":
//                jsonId = HELP_CAR_ID;
//                break;
//            case "大会餐饮":
//                jsonId = HELP_FOOD_ID;
//                break;
//            case "酒店推荐":
//                jsonId = HELP_HOTEL_ID;
//                break;
//            case "周边推荐":
//                jsonId = HELP_RECOMAND_ID;
//                break;
//            case "QA":
//                jsonId = HELP_QA_ID;
//                break;
//            case "大会地址":
//                jsonId = HELP_DETAIL_ID;
//                break;
//        }
//        if (TextTool.isNullOrEmpty(jsonId)) {
//            return;
//        }
//
//        String urlStr = HOST_STATIC + TENANT_ID + "/seminar/" + MyApplication.getApplication().getResourceString(ResourceTable.String_SEMINAR_ID) + "/article/" + jsonId + ".json";
//        HttpBean httpBean = HttpApi.get(urlStr);
//        if (httpBean.getStratus() == 0) {
//            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
//            HelpContent content = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), HelpContent.class);
//            String back = ZSONObject.toZSONString(content.content);
//            switch (jsonId) {
//                case HELP_DETAIL_ID:
////                    大会地址
//                    List<HTMLBean> htmlBeans = HttpData.checkHtmlInfo(back);
//                    LogUtil.e("HTML:" + ZSONObject.toZSONString(htmlBeans));
//                    reply.writeString(ZSONObject.toZSONString(htmlBeans));
//                    break;
//                case HELP_FOOD_ID:
////                    大会餐饮
//                    List<HTMLFoodBean> htmlFoodBeans = HttpData.checkHtmlInfoByFood(back);
//                    LogUtil.e("HTML:" + ZSONObject.toZSONString(htmlFoodBeans));
//                    reply.writeString(ZSONObject.toZSONString(htmlFoodBeans));
//                    break;
//                case HELP_HOTEL_ID:
////                    酒店推荐
//                    List<HTMLFoodBean> htmlHotelBeans = HttpData.checkHtmlInfoByFood(back);
//                    LogUtil.e("HTML:" + ZSONObject.toZSONString(htmlHotelBeans));
//                    reply.writeString(ZSONObject.toZSONString(htmlHotelBeans));
//                    break;
//                case HELP_RECOMAND_ID:
////                    周边推荐
//                    List<HTMLFoodBean> htmlRecomandBeans = HttpData.checkHtmlInfoByzhoubian(back);
//                    LogUtil.e("HTML:" + ZSONObject.toZSONString(htmlRecomandBeans));
//                    reply.writeString(ZSONObject.toZSONString(htmlRecomandBeans));
//                    break;
//                case HELP_QA_ID:
////                    QA
//                    List<HTMLQABean> htmlQABeans = HttpData.checkHtmlInfoByQA(back);
//                    LogUtil.e("HTML:" + ZSONObject.toZSONString(htmlQABeans));
//                    reply.writeString(ZSONObject.toZSONString(htmlQABeans));
//                    break;
//                case HELP_CAR_ID:
////                    自助交通
//                    List<HTMLCarBean> htmlCarBeans = HttpData.checkHtmlInfoByCar(back);
//                    LogUtil.e("HTML:" + ZSONObject.toZSONString(htmlCarBeans));
//                    reply.writeString(ZSONObject.toZSONString(htmlCarBeans));
//
//                    break;
//                case HELP_TRAFFIC_ID:
////                    班车信息
//                    List<HTMLTraffic> htmlTrafficBeans = HttpData.checkHtmlInfoByTraffic(back);
//                    LogUtil.e("HTML:" + ZSONObject.toZSONString(htmlTrafficBeans));
//                    reply.writeString(ZSONObject.toZSONString(htmlTrafficBeans));
//
//                    break;
//
//            }
//
//        } else {
//            LogUtil.e("获取用户ID失败：" + httpBean.getStratus());
//            reply.writeString(null);
////            getSeminarInfo(reply);
//        }
//    }

    /*
     * 获取文章详情JSON
     * */
    public static void getArticleDetailJSON(MessageParcel reply, String articleId) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID + "/custom/hdc2021/" + articleId + ".json";
        HttpBean httpBean = HttpApi.get(urlStr);
        reply.writeString(httpBean.getData());
    }

    /*
     * 获取会议信息
     * */
    public static SeminarInfo getSeminarInfoByjson(MessageParcel reply) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID + "/seminar/" + MyApplication.getApplication().getResourceString(ResourceTable.String_SEMINAR_ID) + "/another/info.json";
        SeminarInfo seminarInfo = null;
        HttpBean httpBean = HttpApi.get(urlStr);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            seminarInfo = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), SeminarInfo.class);
            seminarInfo = HttpData.checkSeminarInfo(seminarInfo);
            LogUtil.e("getSeminarInfoByjsonxue:" + ZSONObject.toZSONString(seminarInfo.values));
            reply.writeString(ZSONObject.toZSONString(seminarInfo.values));
        } else {
            LogUtil.e("获取用户ID失败：" + httpBean.getStratus());
            getSeminarInfo(reply);
        }

        return seminarInfo;
    }

    /*
     * 获取会议信息
     * */
    public static SeminarInfo getSeminarInfoByjsonAndCard() {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID + "/seminar/" + MyApplication.getApplication().getResourceString(ResourceTable.String_SEMINAR_ID) + "/another/info.json";
        SeminarInfo seminarInfo = null;
        HttpBean httpBean = HttpApi.get(urlStr);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            seminarInfo = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), SeminarInfo.class);
        } else {
            LogUtil.e("获取用户ID失败：" + httpBean.getStratus());
        }

        return seminarInfo;
    }

    /*
     * 获取会议信息
     * */
    public static void getSeminarInfo(MessageParcel reply) {

        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST) + MyApplication.getApplication().getResourceString(ResourceTable.String_seminarGet);
        Map<String, Object> map = new HashMap<>();
        map.put("tenantId", TENANT_ID);
        map.put("seminarId", SEMINAR_ID);
        String params = ZSONObject.toZSONString(map);
        HttpBean httpBean = HttpApi.post(urlStr, params);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            SeminarInfo seminarInfo = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), SeminarInfo.class);
            seminarInfo = HttpData.checkSeminarInfo(seminarInfo);
            LogUtil.e("getSeminarInfoByjsonxue:" + ZSONObject.toZSONString(seminarInfo.values));
            reply.writeString(ZSONObject.toZSONString(seminarInfo.values));
        } else {
            LogUtil.e("获取用户ID失败：" + httpBean.getStratus());
        }
    }

    /*
     * 预定议程
     * */
    public static void createReserve(MessageParcel reply, String subSeminarId) {

        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST) + MyApplication.getApplication().getResourceString(ResourceTable.String_apiSeminarSubseminarReserveCreate);
        Map<String, Object> map = new HashMap<>();
        map.put("tenantId", TENANT_ID);
        map.put("seminarId", SEMINAR_ID);
        map.put("globalUserId", MyApplication.getGlobalUserId());
        map.put("sess", MyApplication.getSess());
        map.put("ignoreConflict", 1);
        map.put("subSeminarId", subSeminarId);
        String params = ZSONObject.toZSONString(map);
        HttpBean httpBean = HttpApi.post(urlStr, params);
        if (httpBean.getStratus() == 0) {
            //完成预约任务
            completeTask("RESERVATION_ID");
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            httpBean.setStratus(baseHttp.body.result);
            httpBean.setData(baseHttp.body.desc);
            LogUtil.e("getSeminarInfoByjsonxue:" + ZSONObject.toZSONString(httpBean));
            getReserveIdlist(MessageParcel.create());
        } else {
            httpBean.setStratus(-1);
            httpBean.setData("网络繁忙，请稍后重试！");
        }
        reply.writeString(ZSONObject.toZSONString(httpBean));
    }

    /*
     * 取消议程
     * */
    public static void deleteReserve(MessageParcel reply, String subSeminarId) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST) + MyApplication.getApplication().getResourceString(ResourceTable.String_apiSeminarSubseminarReserveDelete);
        Map<String, Object> map = new HashMap<>();
        map.put("tenantId", TENANT_ID);
        map.put("seminarId", SEMINAR_ID);
        map.put("globalUserId", MyApplication.getGlobalUserId());
        map.put("sess", MyApplication.getSess());
        map.put("subSeminarId", subSeminarId);

        String params = ZSONObject.toZSONString(map);
        HttpBean httpBean = HttpApi.post(urlStr, params);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            httpBean.setStratus(baseHttp.body.result == 0 ? -97 : baseHttp.body.result);
            httpBean.setData(baseHttp.body.result == 0 ? "取消议程成功" : baseHttp.body.desc);
            LogUtil.e("deleteReserve:" + ZSONObject.toZSONString(httpBean));
            getReserveIdlist(MessageParcel.create());
        } else {
            httpBean.setStratus(-1);
            httpBean.setData("网络繁忙，请稍后重试！");
        }
        reply.writeString(ZSONObject.toZSONString(httpBean));
    }

    /**
     * 预定Tech Hour议程
     *
     * @param reply        JS发送的对象
     * @param subSeminarId 议程ID
     * @param agendaId     日程ID
     */
    public static void createReserveByTechHour(MessageParcel reply, String subSeminarId, String agendaId) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_PROJECT) + MyApplication.getApplication().getResourceString(ResourceTable.String_apiCbgTechHourCreate);
        Map<String, Object> map = new HashMap<>();
        map.put("tenantId", TENANT_ID);
        map.put("seminarId", SEMINAR_ID);
        map.put("globalUserId", MyApplication.getGlobalUserId());
        map.put("sess", MyApplication.getSess());
        map.put("ignoreConflict", 1);
        map.put("subSeminarId", subSeminarId);
        map.put("agendaId", agendaId);

        String params = ZSONObject.toZSONString(map);
        HttpBean httpBean = HttpApi.post(urlStr, params);
        if (httpBean.getStratus() == 0) {
            //完成预约任务
            completeTask("RESERVATION_ID");
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            httpBean.setStratus(baseHttp.body.result);
            httpBean.setData(baseHttp.body.desc);
            LogUtil.e("createReserveByTechHour:" + ZSONObject.toZSONString(httpBean));
        } else {
            httpBean.setStratus(-1);
            httpBean.setData("网络繁忙，请稍后重试！");
        }
        reply.writeString(ZSONObject.toZSONString(httpBean));
    }

    /**
     * 取消Tech Hour议程
     *
     * @param reply        JS发送的对象
     * @param subSeminarId 议程ID
     * @param agendaId     日程ID
     */
    public static void deleteReserveByTechHour(MessageParcel reply, String subSeminarId, String agendaId) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_PROJECT) + MyApplication.getApplication().getResourceString(ResourceTable.String_apiCbgTechHourDelete);
        Map<String, Object> map = new HashMap<>();
        map.put("tenantId", TENANT_ID);
        map.put("seminarId", SEMINAR_ID);
        map.put("globalUserId", MyApplication.getGlobalUserId());
        map.put("sess", MyApplication.getSess());
        map.put("subSeminarId", subSeminarId);
        map.put("agendaId", agendaId);

        String params = ZSONObject.toZSONString(map);
        HttpBean httpBean = HttpApi.post(urlStr, params);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            httpBean.setStratus(baseHttp.body.result == 0 ? -97 : baseHttp.body.result);
            httpBean.setData(baseHttp.body.result == 0 ? "取消议程成功" : baseHttp.body.desc);
            LogUtil.e("deleteReserveByTechHour:" + ZSONObject.toZSONString(httpBean));
        } else {
            httpBean.setStratus(-1);
            httpBean.setData("网络繁忙，请稍后重试！");
        }
        reply.writeString(ZSONObject.toZSONString(httpBean));
    }

    /*
     * 获取服务器时间
     * */
    public static ServerTime getServerTime() {
        // ServerTime serverTime = null;
        // String urlStr = HOST_PROJECT + "get_time";
        // Map<String, Object> map = new HashMap<>();
        // String params = ZSONObject.toZSONString(map);
        // HttpBean httpBean = HttpApi.post(urlStr, params);
        // if (httpBean.getStratus() == 0 && !TextTool.isNullOrEmpty(httpBean.getData())) {
        //     serverTime = new ServerTime();
        //     serverTime.setServerTime(Long.valueOf(httpBean.getData().substring(0, 10)));
        //     return serverTime;
        // }
        // return serverTime;
        ServerTime serverTime = new ServerTime();
        serverTime.setServerTime(System.currentTimeMillis() / 1000);
        return serverTime;
    }

    /**
     * 获取合作伙伴
     *
     * @param reply
     */
    @SuppressWarnings("unchecked")
    public static void PartnerList(MessageParcel reply, String showHome) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID + "/seminar/" + MyApplication.getApplication().getResourceString(ResourceTable.String_SEMINAR_ID) + "/article/list/" + PARTNER_ID + ".json";
        HttpBean httpBean = HttpApi.get(urlStr);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            Map<String, Object> mapContent = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), Map.class);
            Object items = mapContent.get("items");
            List<Partner> PartnerList = ZSONArray.stringToZSONArray(ZSONObject.toZSONString(items)).toJavaList(Partner.class);
            PartnerList.sort(Comparator.comparingInt(Partner::getOrder).reversed());
            List<String> images = PartnerList.stream().map(a -> a.coverImageRealPath.startsWith(HTTPS) ? a.coverImageRealPath.replace(MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG_OLD),MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG)) : HTTPS + a.coverImageRealPath.replace(MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG_OLD),MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG))).collect(Collectors.toList());
            reply.writeString(ZSONObject.toZSONString(showHome.equals("是") ? HttpData.splitArray(images, 9) : images));
        } else {
            LogUtil.e("获取合作伙伴：" + httpBean.getStratus());
            reply.writeString(null);
        }
    }

    /**
     * 获取嘉宾列表
     *
     * @param reply
     * @param guestSearch
     */
    public static void getGuestList(MessageParcel reply, GuestSearch guestSearch) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID + "/seminar/" + MyApplication.getApplication().getResourceString(ResourceTable.String_SEMINAR_ID) + "/guest.json";
        HttpBean httpBean = HttpApi.get(urlStr);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            List<Guest> guests = ZSONArray.stringToZSONArray(ZSONObject.toZSONString(baseHttp.body.content)).toJavaList(Guest.class);
            //排序
            guests = guests.stream().sorted(Comparator.comparing(Guest::getGuestsOrder).reversed()).collect(Collectors.toList());
            //处理图片
            guests.forEach(a -> {
                a.realPath = !TextTool.isNullOrEmpty(a.realPath) && a.realPath.startsWith(HTTPS) ? a.realPath : HTTPS + a.realPath;
                a.intro = !TextTool.isNullOrEmpty(a.intro) && a.intro.startsWith(HTTPS) ? a.intro : HTTPS + a.intro;
            });
            List<Guest> result = guests;
            //搜索和首页显示
            if (!TextTool.isNullOrEmpty(guestSearch.key)) {
                result = guests.stream().filter(a -> a.name.contains(guestSearch.key) && a.isPublic.contains("是")).collect(Collectors.toList());
            } else {
//            else if (!TextTool.isNullOrEmpty(guestSearch.isPublic) && guestSearch.isPublic.equals("是")) {
                result = guests.stream().filter(a -> a.isPublic.contains("是")).collect(Collectors.toList());
//            }
            }
            reply.writeString(ZSONObject.toZSONString(result));
        } else {
            LogUtil.e("获取嘉宾列表失败：" + httpBean.getStratus());
        }
    }

    /**
     * 完成积分任务
     */

    public static TaskComplate completeTask(String type) {
        TaskComplate taskComplate = null;
        try {
            String param_type = type.equals("DESKTOP_ID") ? DESKTOP_ID : RESERVATION_ID;//interactionTaskComplete
            String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_PROJECT) + MyApplication.getApplication().getResourceString(ResourceTable.String_interactionTaskQuery);
            Map<String, Object> map = new HashMap<>();
            map.put("activityId", ACTIVITYID);
            map.put("sess", MyApplication.getSess());
            map.put("groupId", param_type);
            map.put("returnStatus", 1);
            String params = ZSONObject.toZSONString(map);
            HttpBean httpBean = HttpApi.post(urlStr, params);
            if (httpBean.getStratus() == 0) {
                TaskHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), TaskHttp.class);
                if (baseHttp.code == 0) {
                    List<TaskList> taskList = ZSONArray.stringToZSONArray(ZSONObject.toZSONString(baseHttp.data)).toJavaList(TaskList.class);
                    List<TaskList> collect = taskList.stream().filter(i -> i.isComplete.equals(0)).collect(Collectors.toList());
                    if (collect.size() > 0) {
                        //完成任务
                        String urlStr1 = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_PROJECT) + MyApplication.getApplication().getResourceString(ResourceTable.String_interactionTaskComplete);
                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("activityId", ACTIVITYID);
                        map1.put("sess", MyApplication.getSess());
                        map1.put("taskId", collect.get(0).id);
                        map1.put("qrCode", BASE64.encode(collect.get(0).id.toString().getBytes(StandardCharsets.UTF_8)) + randomivValue(5));
                        map1.put("returnScore", 1);
                        String params1 = ZSONObject.toZSONString(map1);
                        HttpBean httpBean1 = HttpApi.post(urlStr1, params1);
                        if (httpBean1.getStratus() == 0) {
                            TaskHttp baseHttp1 = ZSONObject.stringToClass(httpBean1.getData(), TaskHttp.class);
                            LogUtil.e(ZSONObject.toZSONString(baseHttp1));
                            if (baseHttp1.code == 0) {
                                taskComplate = ZSONObject.stringToClass(baseHttp1.data.toString(), TaskComplate.class);
                            } else {
                                LogUtil.e("完成任务失败：" + baseHttp1.code);
                            }
                        } else {
                            LogUtil.e("完成任务失败：" + httpBean1.getStratus());
                        }
                    }
                    LogUtil.e(ZSONObject.toZSONString(taskList));
                } else {
                    httpBean.setStratus(-1);
                    httpBean.setData(baseHttp.msg);
                }

            } else {
                LogUtil.e("完成任务失败：" + httpBean.getStratus());
            }
        } catch (Exception ex) {
            LogUtil.e("完成任务失败：" + ex.toString());
        }
        return taskComplate;
    }

    /**
     * 完成积分任务-卡片
     */
    public static TaskComplate completeFATask(String taskId) {
        TaskComplate taskComplate = null;
        try {
            //完成任务
            String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_PROJECT) + MyApplication.getApplication().getResourceString(ResourceTable.String_interactionTaskComplete);
            Map<String, Object> map = new HashMap<>();
            map.put("activityId", ACTIVITYID);
            map.put("sess", MyApplication.getSess());
            map.put("taskId", taskId);
            map.put("qrCode", BASE64.encode(taskId.getBytes(StandardCharsets.UTF_8)) + randomivValue(5));
            map.put("returnScore", 1);
            String params = ZSONObject.toZSONString(map);
            HttpBean httpBean = HttpApi.post(urlStr, params);
            if (httpBean.getStratus() == 0) {
                TaskHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), TaskHttp.class);
                if (baseHttp.code == 0) {
                    taskComplate = ZSONObject.stringToClass(baseHttp.data.toString(), TaskComplate.class);
                    LogUtil.e(ZSONObject.toZSONString(taskComplate));
                } else {
                    LogUtil.e("完成任务失败：" + baseHttp.code);
                }
            } else {
                LogUtil.e("完成任务失败：" + httpBean.getStratus());
            }
        } catch (Exception ex) {
            LogUtil.e("完成任务失败：" + ex);
        }
        return taskComplate;
    }

    /**
     * 获取嘉宾明细
     *
     * @param reply
     * @param guestId
     */
    public static void getGuestInfo(MessageParcel reply, String guestId) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID + "/seminar/" + MyApplication.getApplication().getResourceString(ResourceTable.String_SEMINAR_ID) + "/" + guestId + ".json";
        HttpBean httpBean = HttpApi.get(urlStr);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            GuestInfo guestInfo = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), GuestInfo.class);
            guestInfo.detail.realPath = !TextTool.isNullOrEmpty(guestInfo.detail.realPath) && guestInfo.detail.realPath.startsWith(HTTPS) ? guestInfo.detail.realPath : HTTPS + guestInfo.detail.realPath;
            //按照开始时间排序
            guestInfo.subSeminarList.sort(Comparator.comparing(AgendaSummary::getStartTime).thenComparing(AgendaSummary::getEndTime));
            guestInfo.subSeminarList.forEach(b -> {
                b.agendaDate = TimeHelper.getDateByDay(Long.valueOf(b.startTime));
                b.startTime = TimeHelper.getDateByTime(Long.valueOf(b.startTime));
                b.endTime = TimeHelper.getDateByTime(Long.valueOf(b.endTime));
            });
            reply.writeString(ZSONObject.toZSONString(guestInfo));
        } else {
            LogUtil.e("获取嘉宾明细失败：" + httpBean.getStratus());
        }
    }

    /**
     * 获取首页会议日程
     *
     * @param reply
     */
    @SuppressWarnings("unchecked")
    public static void getSeminarBanner(MessageParcel reply) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID + "/seminar/" + MyApplication.getApplication().getResourceString(ResourceTable.String_SEMINAR_ID) + "/subSeminar/list.json";
        HttpBean httpBean = HttpApi.get(urlStr);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            Map<String, Object> mapContent = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), Map.class);
            Object items = mapContent.get("items");
            List<AgendaSummary> list = ZSONArray.stringToZSONArray(ZSONObject.toZSONString(items)).toJavaList(AgendaSummary.class);
            list.sort(Comparator.comparing(AgendaSummary::getM_subSeminarOrder).reversed().thenComparing(AgendaSummary::getStartTime).thenComparing(AgendaSummary::getEndTime));
            List<AgendaSummary> showHomeList = list.stream().filter(a -> a.m_isShowHome.toString().contains("是")).collect(Collectors.toList());
            showHomeList.forEach(b -> {
                b.agendaDate = TimeHelper.getDayPattern6ByTime(Long.valueOf(b.startTime));
                b.startTime = TimeHelper.getDateByTime(Long.valueOf(b.startTime));
                b.endTime = TimeHelper.getDateByTime(Long.valueOf(b.endTime));
                b.m_meetingTypeCn = b.m_meetingTypeCn.replace("[\"", "").replace("\"]", "");
            });
            reply.writeString(ZSONObject.toZSONString(showHomeList));
        } else {
            LogUtil.e("获取首页会议日程失败：" + httpBean.getStratus());
        }
    }

    /**
     * 获取更多服务
     *
     * @param reply
     */
    public static void getMoreServices(MessageParcel reply) {
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_MORE_SERVICES) + "moreServices.json";
        HttpBean httpBean = HttpApi.get(urlStr);
        reply.writeString(httpBean.getData());
    }

    /**
     * 首页轮播图
     *
     * @param reply
     */
    public static void getIndexSwiperList(MessageParcel reply) {
        LogUtil.e("获取风景首页轮播图");
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID +"/seminar/" + SEMINAR_ID + "/article/list/" + INDEXIMG_ID + ".json";
        HttpBean httpBean = HttpApi.get(urlStr);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            Map<String, Object> mapContent = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), Map.class);
            if (!mapContent.isEmpty()) {
                Object items = mapContent.get("items");
                ZSONArray arr = ZSONArray.stringToZSONArray(items.toString());
                for(int i=0;i<arr.size();i++){
                    ZSONObject jo = arr.getZSONObject(i);
                    String coverImageRealPath = jo.get("coverImageRealPath").toString().startsWith(HTTPS) ? jo.get("coverImageRealPath").toString().replace(MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG_OLD),MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG)) : HTTPS + jo.get("coverImageRealPath").toString().replace(MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG_OLD),MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG));
                    jo.remove("coverImageRealPath");
                    jo.put("coverImageRealPath",coverImageRealPath);
                }
                if (items != null) {
                    reply.writeString(ZSONObject.toZSONString(arr));
                }
            }
        } else {
            LogUtil.e("获取首页轮播图失败：" + httpBean.getStratus());
        }
    }

    /**
     * 风景首页轮播图
     *
     * @param reply
     */
    public static void getTravelSwiperList(MessageParcel reply) {
        LogUtil.e("获取风景首页轮播图");
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID +"/seminar/" + SEMINAR_ID + "/article/list/" + FENGJINGIMG_ID + ".json";
        HttpBean httpBean = HttpApi.get(urlStr);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            Map<String, Object> mapContent = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), Map.class);
            if (!mapContent.isEmpty()) {
                Object items = mapContent.get("items");
                ZSONArray arr = ZSONArray.stringToZSONArray(items.toString());
                for(int i=0;i<arr.size();i++){
                    ZSONObject jo = arr.getZSONObject(i);
                    String coverImageRealPath = jo.get("coverImageRealPath").toString().startsWith(HTTPS) ? jo.get("coverImageRealPath").toString().replace(MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG_OLD),MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG)) : HTTPS + jo.get("coverImageRealPath").toString().replace(MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG_OLD),MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG));
                    jo.remove("coverImageRealPath");
                    jo.put("coverImageRealPath",coverImageRealPath);
                }
                if (items != null) {
                    reply.writeString(ZSONObject.toZSONString(arr));
                }
            }
        } else {
            LogUtil.e("获取风景首页轮播图失败：" + httpBean.getStratus());
        }
    }

    /**
     * 风景列表
     *
     * @param reply
     */
    public static void getTravelList(MessageParcel reply) {
        LogUtil.e("获取风景列表");
        String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID +"/seminar/" + SEMINAR_ID + "/article/list/" + FENGJINGLISTIMG_ID + ".json";
        HttpBean httpBean = HttpApi.get(urlStr);
        if (httpBean.getStratus() == 0) {
            BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
            Map<String, Object> mapContent = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), Map.class);
            if (!mapContent.isEmpty()) {
                Object items = mapContent.get("items");
                ZSONArray arr = ZSONArray.stringToZSONArray(items.toString());
                for(int i=0;i<arr.size();i++){
                    ZSONObject jo = arr.getZSONObject(i);
                    String coverImageRealPath = jo.get("coverImageRealPath").toString().startsWith(HTTPS) ? jo.get("coverImageRealPath").toString().replace(MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG_OLD),MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG)) : HTTPS + jo.get("coverImageRealPath").toString().replace(MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG_OLD),MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG));
                    jo.remove("coverImageRealPath");
                    jo.put("coverImageRealPath",coverImageRealPath);
                }
                if (items != null) {
                    reply.writeString(ZSONObject.toZSONString(arr));
                }
            }
        } else {
            LogUtil.e("获取风景列表失败：" + httpBean.getStratus());
        }
    }

    /**
     * 风景详情
     *
     * @param reply
     */
    public static void getTravelDetail(String detailId, MessageParcel reply) {
        if (!TextTool.isNullOrEmpty(detailId)) {
            LogUtil.e("获取风景详情");
            String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID + "/seminar/" + MyApplication.getApplication().getResourceString(ResourceTable.String_SEMINAR_ID) + "/article/" + detailId + ".json";
            HttpBean httpBean = HttpApi.get(urlStr);
            if (httpBean.getStratus() == 0) {
                BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
                Map<String, Object> mapContent = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), Map.class);
                if (!mapContent.isEmpty()) {
                    Object items = mapContent.get("showImagesRealPath");
                    ZSONArray arr = ZSONArray.stringToZSONArray(items.toString());
                    ZSONArray arr_new = new ZSONArray();
                    for(int i=0;i<arr.size();i++){
                        String jo = arr.get(i).toString();
                        String coverImageRealPath = jo.startsWith(HTTPS) ? jo.replace(MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG_OLD),MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG)) : HTTPS + jo.replace(MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG_OLD),MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_IMG));
                        arr_new.add(coverImageRealPath);
                    }
                    mapContent.remove("showImagesRealPath");
                    mapContent.put("showImagesRealPath",arr_new);
                }
                reply.writeString(ZSONObject.toZSONString(mapContent));
            } else {
                LogUtil.e("获取风景详情失败：" + httpBean.getStratus());
            }
        }
    }

    /**
     * 获取小艺建议卡片内容
     *
     * @param poiId poiId
     */
    public static CardInfoContent getHarmonyCardGetCardByPoid(String poiId) {
        CardInfoContent cardInfoContent = null;
        try {
            String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_PROJECT) + MyApplication.getApplication().getResourceString(ResourceTable.String_apiHarmonyCardGetCardByPoid);
            Map<String, Object> map = new HashMap<>();
            map.put("seminarId", SEMINAR_ID);
            map.put("sess", MyApplication.getSess());
            map.put("poid", poiId);
            String params = ZSONObject.toZSONString(map);
            HttpBean httpBean = HttpApi.post(urlStr, params);
            if (httpBean.getStratus() == 0) {
                BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
                if (baseHttp.body.result == 0) {
                    cardInfoContent = ZSONObject.stringToClass(String.valueOf(baseHttp.body.content), CardInfoContent.class);
                    if (cardInfoContent.cardInfo != null) {
                        if (!cardInfoContent.cardInfo.getClass().isArray()) {
                            cardInfoContent.card = ZSONObject.stringToClass(String.valueOf(cardInfoContent.cardInfo), CardInfoContent.cardInfo.class);
                            if (cardInfoContent.card.nearestCardInfo != null) {
                                if (!cardInfoContent.card.nearestCardInfo.getClass().isArray()) {
                                    cardInfoContent.card.nearestCard = ZSONObject.stringToClass(String.valueOf(cardInfoContent.card.nearestCardInfo), CardInfoContent.nearestCardInfo.class);
                                }
                            }
                        }
                    }
                }
                LogUtil.e("getHarmonyCardGetCardByPoid:" + ZSONObject.toZSONString(httpBean));
            }
        } catch (Exception e) {
            LogUtil.e("getHarmonyCardGetCardByPoid:" + e);
        }
        return cardInfoContent;
    }

    /**
     * 获取小艺建议卡片内容
     *
     * @param poiId poiId
     */
    public static CardInfoItem getHarmonyCardListJson(String poiId) {
        CardInfoItem cardInfoItem = null;
        try {
            String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID + "/custom/hdc2023/harmony-card-list.json";
            HttpBean httpBean = HttpApi.get(urlStr);
            if (httpBean.getStratus() == 0) {
                BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
                if (baseHttp.body.result == 0) {
                    Map<String, Object> mapContent = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), Map.class);
                    Object items = mapContent.get("items");
                    List<CardInfoItem> list = ZSONArray.stringToZSONArray(ZSONObject.toZSONString(items)).toJavaList(CardInfoItem.class);
                    for (CardInfoItem item : list) {
                        if (poiId.equals(item.poid)) {
                            cardInfoItem = item;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e("getHarmonyCardListJson:" + e);
        }
        return cardInfoItem;
    }

    /**
     * 获取测试poiId
     *
     * @return poiId
     */
    public static String getTestPoiId() {
        String poiId = null;
        try {
            String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_TestPoiId);
            HttpBean httpBean = HttpApi.get(urlStr);
            if (httpBean.getStratus() == 0) {
                poiId = httpBean.getData();
            }
        } catch (Exception e) {
            LogUtil.e("getTestPoiId:" + e);
        }
        return poiId;
    }
    public static String getTestPoiId_SN() {
        String poiId = null;
        try {
            String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_POIID_CESHI);
            HttpBean httpBean = HttpApi.post(urlStr,"");
            if (httpBean.getStratus() == 0) {
                ZSONObject zsonHttp = ZSONObject.stringToZSON(httpBean.getData());
                ZSONObject body = ZSONObject.stringToZSON(zsonHttp.get("body").toString());
                poiId = body.get("content").toString();
            }
        } catch (Exception e) {
            LogUtil.e("getTestPoiId:" + e);
        }
        return poiId;
    }

    /**
     * 获取我的积分
     */
    public static String getTotalScore() {
        String totalScore = null;
        try {
            String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_PROJECT) + MyApplication.getApplication().getResourceString(ResourceTable.String_interactionApiUserScoreQuery);
            Map<String, Object> map = new HashMap<>();
            map.put("sess", MyApplication.getSess());
            map.put("type", 0);
            map.put("activityId", ACTIVITYID);
            map.put("start", 0);
            map.put("num", 1);
            String params = ZSONObject.toZSONString(map);
            HttpBean httpBean = HttpApi.post(urlStr, params);
            if (httpBean.getStratus() == 0) {
                ZSONObject zsonHttp = ZSONObject.stringToZSON(httpBean.getData());
                if (zsonHttp.getInteger("code") == 0) {
                    totalScore = zsonHttp.getZSONObject("data").getString("totalScore");
                }
                LogUtil.e("getTotalScore:" + ZSONObject.toZSONString(httpBean));
            }
        } catch (Exception e) {
            LogUtil.e("getTotalScore:" + e);
        }
        return totalScore;
    }
    /**
     * 校验sess有效性
     */
    public static void checkSess(){
        String sess = MyApplication.getSess();
        if(!TextTool.isNullOrEmpty(sess)) {
            //检查sess的有效性
            String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_PROJECT) + MyApplication.getApplication().getResourceString(ResourceTable.String_apiSeminarContactsGet);
            Map<String, Object> map = new HashMap<>();
            map.put("tenantId", TENANT_ID);
            map.put("seminarId", SEMINAR_ID);
            map.put("sess", sess);
            map.put("openId", "");
            map.put("globalUserId", MyApplication.getGlobalUserId());
            String params = ZSONObject.toZSONString(map);
            HttpBean httpBean = HttpApi.post(urlStr, params);
            if (httpBean.getStratus() == 401) {
                MyApplication.setSess("");
            }
        }
    }
    public static void checkSess(MessageParcel reply){
        String sess = MyApplication.getSess();
        if(!TextTool.isNullOrEmpty(sess)) {
            //检查sess的有效性
            String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_PROJECT) + MyApplication.getApplication().getResourceString(ResourceTable.String_apiSeminarContactsGet);
            Map<String, Object> map = new HashMap<>();
            map.put("tenantId", TENANT_ID);
            map.put("seminarId", SEMINAR_ID);
            map.put("sess", sess);
            map.put("openId", "");
            map.put("globalUserId", MyApplication.getGlobalUserId());
            String params = ZSONObject.toZSONString(map);
            HttpBean httpBean = HttpApi.post(urlStr, params);
            if (httpBean.getStratus() == 401) {
                MyApplication.setSess("");
            }
        }
        reply.writeString(TextTool.isNullOrEmpty(MyApplication.getSess()) ? "1" : "0");
    }
    public static void checkUser(MessageParcel reply){
        seminarContact(null, MyApplication.getSess(),null);
        String checkInStatus = MyApplication.getUserInfo().checkInStatus;
        String i_ticketType = MyApplication.getUserInfo().i_ticketType;
        String m_giftSpecialRight = MyApplication.getUserInfo().giftSpecialRight;
        reply.writeString(checkInStatus+"|"+i_ticketType+"|"+m_giftSpecialRight);
    }
}