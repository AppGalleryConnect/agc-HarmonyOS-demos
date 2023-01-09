/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.huawei.apms.hos.crash.java;

import com.huawei.apms.hos.crash.TestCrash;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class ExceptionAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ExceptionAbilitySlice.class.getName());
        TestCrash.createJvmException();
    }
}
