package com.smarket.hdc2023.widget.form;

import ohos.app.Context;

/**
 * 判断应用是否安装
 */
public class AppIsInstall {
    public static boolean isInstall(Context context, String packageName) {
        boolean isInstall = false;
        try {
            isInstall = context.getBundleManager().isApplicationEnabled(packageName);
        } catch (IllegalArgumentException exception) {
            return false;
        }
        return isInstall;
    }
}
