package com.smarket.hdc2023.widget.form;

import ohos.app.Context;

/**
 * 文本提示框工具类
 */
public class TextDialogUtil {
    private static TextDialog dialog;

    /**
     * 显示文本提示框
     *
     * @param context 上下文
     * @param content 文本
     */
    public static void show(Context context, String content) {
        context.getUITaskDispatcher().asyncDispatch(() -> {
            if (dialog != null) {
                dialog.destroy();
                dialog = null;
            }
            dialog = new TextDialog(context);
            dialog.setContent(content);
        });
    }
}
