/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.huawei.apms.hos.crash;

import com.huawei.apm.crash.APMCrash;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.window.dialog.ToastDialog;
import ohos.bundle.ElementName;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;

public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        APMCrash.getInstance().enableDebugMode(true);
        findComponentById(ResourceTable.Id_btn_init).setClickedListener(component -> {
            initCrash();
            new EventHandler(EventRunner.getMainEventRunner()).postTask(() -> {
                new ToastDialog(this).setText("init").show();
            });
        });
        findComponentById(ResourceTable.Id_btn_java_crash_0).setClickedListener(component -> {
            TestCrash.createJvmNormalFatal(true);
        });
        findComponentById(ResourceTable.Id_btn_java_crash).setClickedListener(component -> {
            Intent target = new Intent();
            ElementName elementName = new ElementName("", getBundleName(),
                "com.huawei.hag.jsapplication.java.JavaMainAbility");
            target.setElement(elementName);
            startAbility(target);
        });
        findComponentById(ResourceTable.Id_btn_java_exception).setClickedListener(component -> {
            Intent target = new Intent();
            ElementName elementName = new ElementName("", getBundleName(),
                "com.huawei.hag.jsapplication.java.ExceptionAbility");
            target.setElement(elementName);
            startAbility(target);
        });
        findComponentById(ResourceTable.Id_btn_native_crash).setClickedListener(component -> {
            Intent target = new Intent();
            ElementName elementName = new ElementName("", getBundleName(),
                "com.huawei.hag.jsapplication.nativecrash.NativeMainAbility");
            target.setElement(elementName);
            startAbility(target);
        });
        findComponentById(ResourceTable.Id_btn_js_crash).setClickedListener(component -> {
            Intent target = new Intent();
            ElementName elementName = new ElementName("", getBundleName(),
                "com.huawei.hag.jsapplication.js.JsMainAbility");
            target.setElement(elementName);
            startAbility(target);
        });
    }

    private void initCrash() {
        APMCrash.getInstance().enableCrashReport(true);
        APMCrash.getInstance().setUserId("xyz_abc");
        APMCrash.getInstance().setCustomLogInfo(4, "log 1");
        APMCrash.getInstance().setCustomLogInfo(3, "log 3");
        APMCrash.getInstance().setCustomLogInfo(4, "log 4");
        APMCrash.getInstance().setCustomLogInfo("log 5");
        APMCrash.getInstance().setCustomLogInfo(6, "log 6");
        APMCrash.getInstance().setCustomLogInfo(7, "log 7");
        APMCrash.getInstance().setCustomLogInfo(5, "log 8");
        APMCrash.getInstance().setCustomLogInfo(7, "log 9");
        APMCrash.getInstance().setCustomKeyValue("test key 1", 100);
        APMCrash.getInstance().setCustomKeyValue("test key 2", "test value 2");
        APMCrash.getInstance().setCustomKeyValue("test key 3", true);
        APMCrash.getInstance().setCustomKeyValue("test key 4", "test value 4");
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
