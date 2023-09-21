package com.smarket.hdc2023.help;

import com.smarket.hdc2023.BuildConfig;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class LogUtil {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.LOG_APP, 0x0, "xue:" + BuildConfig.BUNDLE_NAME);
    private static final String TAG = "xue:";
    private static final boolean isShow = !BuildConfig.DEBUG;
    private static final int max_str_length = 3001 - TAG.length();

    public static void e(String msg) {
        if (isShow) {
            return;
        }
        //大于4000时
        while (msg.length() > max_str_length) {
            HiLog.error(LABEL_LOG, "xue:" + msg.substring(0, max_str_length), "");
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        HiLog.error(LABEL_LOG, "xue:" + msg, "");

    }

    public static void i(String msg) {
        if (isShow) {
            return;
        }
        while (msg.length() > max_str_length) {
            HiLog.info(LABEL_LOG, "xue:" + msg.substring(0, max_str_length), "");
            msg = msg.substring(max_str_length);
        }
        HiLog.info(LABEL_LOG, "xue:" + msg, "");
    }
}
