/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.huawei.apms.hos.crash.widget.widget;

import com.huawei.apms.hos.crash.widget.controller.FormController;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.FormBindingData;
import ohos.aafwk.ability.ProviderFormInfo;
import ohos.aafwk.content.Intent;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.zson.ZSONObject;

/**
 * Form controller implementation.
 */
public class WidgetImpl extends FormController {
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.DEBUG, 0x0, WidgetImpl.class.getName());
    private static final int DEFAULT_DIMENSION_2X2 = 2;

    public WidgetImpl(Context context, String formName, Integer dimension) {
        super(context, formName, dimension);
    }

    @Override
    public ProviderFormInfo bindFormData(long formId) {
        HiLog.info(TAG, "bind form data");
        ProviderFormInfo providerFormInfo = new ProviderFormInfo();
        if (dimension == DEFAULT_DIMENSION_2X2) {
            ZSONObject zsonObject = new ZSONObject();
            zsonObject.put("", "");
            providerFormInfo.setJsBindingData(new FormBindingData(zsonObject));
        }
        return providerFormInfo;
    }

    @Override
    public void updateFormData(long formId, Object... vars) {
        HiLog.info(TAG, "update form data: formId" + formId);
    }

    @Override
    public void onTriggerFormEvent(long formId, String message) {
        HiLog.info(TAG, "onTriggerFormEvent.");
    }

    @Override
    public Class<? extends AbilitySlice> getRoutePageSlice(Intent intent) {
        HiLog.info(TAG, "set route page slice.");
        Class<? extends AbilitySlice> abilitySlice;
        ZSONObject zsonObject = ZSONObject.stringToZSON(intent.getStringParam("params"));
        switch (zsonObject.getString("message")) {
            case "page1":
                abilitySlice = Widget1Slice.class;
                break;
            case "page2":
                abilitySlice = Widget2Slice.class;
                break;
            case "page3":
                abilitySlice = Widget3Slice.class;
                break;
            case "page4":
                abilitySlice = Widget4Slice.class;
                break;
            default:
                abilitySlice = null;
                break;
        }

        return abilitySlice;
    }


}

