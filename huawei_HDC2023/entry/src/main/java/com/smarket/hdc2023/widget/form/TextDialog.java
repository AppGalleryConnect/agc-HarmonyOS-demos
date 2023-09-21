package com.smarket.hdc2023.widget.form;

import com.smarket.hdc2023.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

/**
 * 文本提示框
 */
public class TextDialog extends ToastDialog {
    private final Text dialogText;

    public TextDialog(Context context) {
        super(context);
        Component dialogComponent = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_text_dialog, null, false);
        dialogText = (Text) dialogComponent.findComponentById(ResourceTable.Id_dialogText);
        setTransparent(true);
        setComponent(dialogComponent)
                .setAlignment(LayoutAlignment.CENTER);
    }

    /**
     * 设置提示框文本
     *
     * @param content 文本
     */
    public void setContent(String content) {
        if (dialogText != null) {
            dialogText.setText(content);
            show();
        }
    }
}
