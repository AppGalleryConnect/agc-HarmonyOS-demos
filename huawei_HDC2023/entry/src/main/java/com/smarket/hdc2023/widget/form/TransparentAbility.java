package com.smarket.hdc2023.widget.form;

import com.smarket.hdc2023.MyApplication;
import com.smarket.hdc2023.ResourceTable;
import com.smarket.hdc2023.help.CommonUtil;
import com.smarket.hdc2023.help.HiAnalyticsUtil;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.utils.TextTool;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.utils.IntentConstants;
import ohos.utils.net.Uri;
import ohos.utils.zson.ZSONObject;

/**
 * 一个透明页面，用来添加本地提醒、请求权限和第三方拉端
 */
public class TransparentAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        jumpPage(intent);
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_transparent);
        getWindow().setTransparent(true);
    }

    /**
     * 新的Intent
     */
    @Override
    public void onNewIntent(Intent intent) {
        jumpPage(intent);
        super.onNewIntent(intent);
    }

    /**
     * 点击事件
     *
     * @param intent 意图
     */
    private void jumpPage(Intent intent) {
        String cardParams = intent.getStringParam("params");
        if (cardParams != null) {
            ZSONObject zsonObject = ZSONObject.stringToZSON(cardParams);
            String message = zsonObject.getString("message");
            String data = zsonObject.getString("data");
            String data2 = zsonObject.getString("data2");
            String data3 = zsonObject.getString("data3");
            String data4 = zsonObject.getString("data4");
            getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(() -> {
                switch (message) {
                    case "placeCode":
                        new JumpReminderDialog(this);
                        break;
                    case "openBrowser":
                        Intent browserIntent = new Intent();
                        Operation browserOperation = new Intent.OperationBuilder().withAction(IntentConstants.ACTION_VIEW_DATA).withBundleName("com.huawei.browser").withUri(Uri.parse(MyApplication.getApplication().getResourceString(ResourceTable.String_aruri))).build();
                        browserIntent.setOperation(browserOperation);
                        startAbility(browserIntent);
                        terminateAbility();
                        break;
                    case "goTask":
                        //做任务
                        if (intent.hasParameter(AbilitySlice.PARAM_FORM_ID_KEY)) {
                            long formId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, -1);
                            //上报用户点击功能按钮事件
                            HiAnalyticsUtil.addModularClickEvent(this, CommonUtil.getCardCnName(formId) + "-" + data);
                        }
                        if (isLogin()) {
                            if (data.equals("卡片加桌")) {
                                CommonUtil.AddToHome(this);
                                CommonUtil.completeFATask(this, Long.valueOf(data2), data3, "TASK_ID", data4);
                            } else if (data.equals("每日打卡")) {
                                CommonUtil.completeFATask(this, Long.valueOf(data2), data3, "TASK_ID", data4);
                            } else if (data.equals("趣味答题")) {
                                //打开问卷
                                CommonUtil.completeFATask(this, Long.valueOf(data2), data3, "TASK_ID", data4);
                                CommonUtil.OpenWebview("questionFA*" + data2 + "*" + data3 + "*" + data4, this);
                            }
                        }
                        terminateAbility();
                        break;
                    case "goPointsShop":
                        if (intent.hasParameter(AbilitySlice.PARAM_FORM_ID_KEY)) {
                            long formId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, -1);
                            //上报用户点击功能按钮事件
                            HiAnalyticsUtil.addModularClickEvent(this, CommonUtil.getCardCnName(formId) + "-码力商城");
                        }
                        //打开积分商城/兑换礼品
                        if (isLogin()) {
                            CommonUtil.OpenWebview("integralFA*points_shop*码力商城", this);
                        }
                        terminateAbility();
                        break;
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsFromUserResult(requestCode, permissions, grantResults);
        terminateAbility();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 判断是否登录，没登录跳转登录页
     *
     * @return 是否登录
     */
    private Boolean isLogin() {
        if (TextTool.isNullOrEmpty(MyApplication.getSess())) {
            Intent intent = new Intent();
            Operation operation = new Intent.OperationBuilder().withDeviceId("").withBundleName("com.smarket.hdc2023").withAbilityName("com.smarket.hdc2023.MainAbility").build();
            intent.setOperation(operation);
            intent.setParam("params", "{\"message\":\"goLogin\"}");
            startAbility(intent);
            return false;
        } else {
            return true;
        }
    }
}
