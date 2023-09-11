package com.smarket.hdc2023.widget.form;

import ohos.agp.utils.Point;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.global.configuration.DeviceCapability;

/**
 * 获取屏幕宽高和像素转换
 *
 * @since 2023/6/1 17:08
 */
public class DevicesUtil {
    public static int getScreenWidth(Context context) {
        Point point = new Point();
        DisplayManager.getInstance().getDefaultDisplay(context).get().getSize(point);
        return point.getPointXToInt();
    }

    public static int getScreenHeight(Context context) {
        Point point = new Point();
        DisplayManager.getInstance().getDefaultDisplay(context).get().getSize(point);
        return point.getPointYToInt();
    }

    public static int px2vp(Context context, float size) {
        float density = (float) context.getResourceManager().getDeviceCapability().screenDensity / DeviceCapability.SCREEN_MDPI;
        return (int) (size / density);
    }

    public static int vp2px(Context context, int size) {
        float density = (float) context.getResourceManager().getDeviceCapability().screenDensity / DeviceCapability.SCREEN_MDPI;
        return (int) (size * density);
    }
}
