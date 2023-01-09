/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.huawei.apms.hos.crash.widget.controller;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.ProviderFormInfo;
import ohos.aafwk.content.Intent;
import ohos.app.Context;

/**
 * The api set for form controller.
 */
public abstract class FormController {
    /**
     * Context of ability
     */
    protected final Context context;

    /**
     * The name of current form service widget
     */
    protected final String formName;

    /**
     * The dimension of current form service widget
     */
    protected final int dimension;

    public FormController(Context context, String formName, Integer dimension) {
        this.context = context;
        this.formName = formName;
        this.dimension = dimension;
    }

    /**
     * Bind data for a form
     *
     * @param formId id
     * @return ProviderFormInfo
     */
    public abstract ProviderFormInfo bindFormData(long formId);

    /**
     * Update form data
     *
     * @param formId the id of service widget to be updated
     * @param vars   the data to update for service widget, this parameter is optional
     */
    public abstract void updateFormData(long formId, Object... vars);

    /**
     * Update form data on time, this update time is set in config.json by scheduledUpdateTime property.
     * If you want to update form data on time, implement this method in you own FormController implementation class.
     *
     * @param formId the id of service widget to be updated
     */
    public void onUpdateFormData(long formId) {
    }

    /**
     * Called when receive service widget message event
     *
     * @param formId  form id
     * @param message the message context sent by service widget message event
     */
    public abstract void onTriggerFormEvent(long formId, String message);

    /**
     * Get the destination ability slice to route
     *
     * @param intent intent of current page slice
     * @return the destination ability slice name to route
     */
    public abstract Class<? extends AbilitySlice> getRoutePageSlice(Intent intent);

    /**
     * Delete the resource related to current service widget.
     * If you have saved something when create service widget, you can implement this method in you own FormController
     * implementation class to delete them.
     *
     * @param formId form id
     */
    public void onDeleteForm(long formId) {
    }

    /**
     * Get the dimension of current service widget
     *
     * @return the dimension of current service widget
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * Get the name of current service widget
     *
     * @return the name of current service widget
     */
    public String getFormName() {
        return formName;
    }
}
