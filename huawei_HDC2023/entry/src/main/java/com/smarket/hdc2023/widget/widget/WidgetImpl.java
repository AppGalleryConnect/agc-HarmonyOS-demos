package com.smarket.hdc2023.widget.widget;

import com.smarket.hdc2023.widget.controller.FormController;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.ProviderFormInfo;
import ohos.aafwk.content.Intent;
import ohos.app.Context;
import ohos.utils.zson.ZSONObject;

public class WidgetImpl extends FormController {

    public WidgetImpl(Context context, String formName, Integer dimension) {
        super(context, formName, dimension);
    }

    @Override
    public ProviderFormInfo bindFormData() {
        return null;
    }

    @Override
    public void updateFormData(long formId, Object... vars) {
    }

    @Override
    public void onTriggerFormEvent(long formId, String message) {
    }
    public ZSONObject getData() {
        ZSONObject zsonObject = new ZSONObject();
        return zsonObject;
    }
    @Override
    public Class<? extends AbilitySlice> getRoutePageSlice(Intent intent) {
        return null;
    }
}
