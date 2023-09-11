package com.smarket.hdc2023.help;

import com.smarket.hdc2023.MyApplication;
import com.smarket.hdc2023.ResourceTable;
import com.smarket.hdc2023.bean.CeliaCardStyle;
import com.smarket.hdc2023.bean.TaskComplate;
import com.smarket.hdc2023.http.HttpUtil;
import com.smarket.hdc2023.widget.form.database.DatabaseUtils;
import com.smarket.hdc2023.widget.form.database.FormData;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.FormBindingData;
import ohos.aafwk.ability.FormException;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.utils.TextTool;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.utils.net.Uri;
import ohos.utils.zson.ZSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CommonUtil {
    /**
     * 从输入流获取数据
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] getBytesFromInputStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len;
        while ((len = in.read(b)) != -1) {
            out.write(b, 0, len);
        }
        byte[] bytes = out.toByteArray();
        out.close();
        return bytes;
    }

    public static void bytesToOutputStream(byte[] bytes, OutputStream out) throws IOException {
        out.write(bytes);
        out.flush();
        out.close();
    }

    public static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer retBuf = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U'))) try {
                    retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                    i += 5;
                } catch (NumberFormatException localNumberFormatException) {
                    retBuf.append(unicodeStr.charAt(i));
                }
                else retBuf.append(unicodeStr.charAt(i));
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }

    /**
     * 打开Webview
     *
     * @param url     打开的地址
     * @param ability ability
     */
    public static void OpenWebview(String url, Ability ability) {
        //打开WEBVIEW
        Intent webViewIntent = new Intent();
        Operation webViewOperation = new Intent.OperationBuilder().withDeviceId("").withBundleName("com.smarket.hdc2023").withAbilityName("com.smarket.hdc2023.widget.form.WebViewAbility").build();
        webViewIntent.setOperation(webViewOperation);
        if (url.contains("integralFA"))//如果是积分
        {
            String typeName = url.split("\\*")[1];
            String title = url.split("\\*")[2];
            webViewIntent.setParam("title", title);
            url = MyApplication.getApplication().getResourceString(ResourceTable.String_integraluri) + typeName + "?sess=" + MyApplication.getSess2();

        }else if(url.equals("index.html")){
            webViewIntent.setParam("title", "导览");
        } else if(url.contains("questionFA")) {
            String data2 = url.split("\\*")[1];
            String data3 = url.split("\\*")[2];
            String data4 = url.split("\\*")[3];
            url = MyApplication.getApplication().getResourceString(ResourceTable.String_question);
            webViewIntent.setParam("data2", data2);
            webViewIntent.setParam("data3", data3);
            webViewIntent.setParam("data4", data4);
            webViewIntent.setParam("title", "趣味答题");
        }else if(url.contains("indexImg")){
            String title = url.split("\\*")[1];
            webViewIntent.setParam("title", title);
            url = url.split("\\*")[2];
        }
        webViewIntent.setParam("url", url);

        ability.startAbility(webViewIntent);
    }

    /**
     * 卡片加桌
     *
     * @param ability ability
     */
    public static void AddToHome(Ability ability) {
        String uriPath = "store://appgallery.huawei.com/oper/addhome?id=1690603011653&installType=7470&s=70EAA2D3C49D1E43D2247AEE4112526A3B572038E2F1DA6397F19E9C61F9832B";
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder().withUri(Uri.parse(uriPath)).build();
        intent.setAction("android.intent.action.VIEW");
        intent.setOperation(operation);
        ability.startAbility(intent);
    }

    /**
     * 积分互动卡片-完成任务
     *
     * @param ability             ability
     * @param formId              卡片ID
     * @param integralText        卡片文字
     * @param interactionTaskType 任务类型
     * @param interactionTaskId   任务ID
     */
    public static void completeFATask(Ability ability, long formId, String integralText, String interactionTaskType, String interactionTaskId) {
        ability.getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(() -> {
            TaskComplate taskComplate = null;
            if (interactionTaskType.equals("TASK_ID")) {
                taskComplate = HttpUtil.completeFATask(interactionTaskId);
            } else {
                taskComplate = HttpUtil.completeTask(interactionTaskType);
            }
            if (taskComplate != null && taskComplate.totalScore != null && taskComplate.increaseScore != null) {
                ZSONObject zsonObject = new ZSONObject();
                //卡片样式
                CeliaCardStyle celiaCardStyle = new CeliaCardStyle();
                celiaCardStyle.bgClass1 = "card-bg2";
                celiaCardStyle.hasIntegralBtn = true;
                celiaCardStyle.isIntegralCard = true;
                celiaCardStyle.hasAddIntegral = true;
                zsonObject.put("style", celiaCardStyle);

                //卡片内容
                zsonObject.put("integralTitle", "码力互动赢大奖");
                zsonObject.put("integralText", integralText);
                zsonObject.put("integralNum", taskComplate.totalScore.toString());
                zsonObject.put("integralAddNum", taskComplate.increaseScore.toString());

                //刷新卡片
                updateCardForm(ability, formId, zsonObject);
            }
        });
    }

    /**
     * 更新单个卡片
     *
     * @param ability    ability
     * @param formId     卡片ID
     * @param zsonObject 卡片数据
     */
    public static void updateCardForm(Ability ability, long formId, ZSONObject zsonObject) {
        try {
            if (DatabaseUtils.queryCardData(formId) != null) {
                ability.updateForm(formId, new FormBindingData(zsonObject));
            }
        } catch (FormException e) {
            DatabaseUtils.deleteCardData(formId);
            e.printStackTrace();
        }
    }

    /**
     * 获取卡片中文名称
     * @param formId 卡片ID
     * @return 卡片中文名称
     */
    public static String getCardCnName(long formId) {
        FormData formData = DatabaseUtils.queryCardData(formId);
        String cardName = "卡片";//卡片中文名称
        if (formData != null) {
            switch (formData.getName()) {
                case "widget":
                    cardName = "大会议程卡片2x2";
                    break;
                case "card_1x2":
                    cardName = "首页卡片";
                    break;
                case "card_2x4":
                    cardName = "大会议程卡片2x4";
                    break;
                case "celia":
                    if (TextTool.isNullOrEmpty(formData.getPoiId())) {
                        cardName = "自主加桌小艺建议卡片";
                    } else {
                        cardName = "小艺建议卡片";
                    }
                    break;
            }
        }
        return cardName;
    }
}