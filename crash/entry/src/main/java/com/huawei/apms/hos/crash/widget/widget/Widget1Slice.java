/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.huawei.apms.hos.crash.widget.widget;

import com.huawei.apms.hos.crash.ResourceTable;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.utils.zson.ZSONObject;

public class Widget1Slice extends AbilitySlice {
    private static final String PARAMS = "params";
    private static final String MESSAGE = "message";

    @Override
    public void onStart(Intent intent) {
        Component component = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_multiple_contact_widget1, null, false);
        Text text = (Text) component.findComponentById(ResourceTable.Id_page1);
        ZSONObject zsonObject = ZSONObject.stringToZSON(intent.getStringParam(PARAMS));
        text.setText(zsonObject.getString(MESSAGE));
        super.setUIContent((ComponentContainer) component);
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
