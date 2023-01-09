/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.huawei.apms.hos.crash.nativecrash;

import com.huawei.apms.hos.crash.TestCrash;
import com.huawei.apms.hos.crash.java.JavaMainAbility;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class NativeMainAbility extends Ability {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "NativeMainAbility");

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(JavaMainAbility.class.getName());
        HiLog.info(LABEL_LOG, "create native exception by variable 0");
        TestCrash.createNativeException(0);
    }
}
