/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.huawei.apms.hos.crash.js;

import ohos.aafwk.content.Intent;
import ohos.ace.ability.AceAbility;

public class JsMainAbility extends AceAbility {

    @Override
    public void onStart(Intent intent) {
        setInstanceName("jscrash");
        super.onStart(intent);
    }
}
