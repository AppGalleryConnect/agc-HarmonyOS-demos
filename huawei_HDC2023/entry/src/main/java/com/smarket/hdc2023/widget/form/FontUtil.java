package com.smarket.hdc2023.widget.form;

import ohos.agp.text.Font;
import ohos.agp.text.RichText;
import ohos.agp.text.RichTextBuilder;
import ohos.agp.text.TextForm;
import ohos.agp.utils.Color;

public class FontUtil {
    /**
     * 自定义字体,粗体
     *
     * @return 字体
     */
    public static Font bold() {
        return new Font.Builder("myfont")
                .setWeight(Font.BOLD)
                .build();
    }

    /**
     * 自定义字体,微粗
     *
     * @return 字体
     */
    public static Font medium() {
        return new Font.Builder("myfont")
                .setWeight(Font.MEDIUM)
                .build();
    }


    /**
     * 字体下划线
     *
     * @param data 文本数据
     * @return 富文本
     */
    public static RichText underline(String data) {
        TextForm textForm = new TextForm();
        textForm.setUnderline(true); // 设置下划线
        textForm.setTextColor(Color.getIntColor("#32528A"));
        textForm.setTextFont(medium());
        textForm.setTextSize(50);
        RichTextBuilder richTextBuilder = new RichTextBuilder(textForm);
        richTextBuilder.addText(data);
        return richTextBuilder.build();
    }
}
