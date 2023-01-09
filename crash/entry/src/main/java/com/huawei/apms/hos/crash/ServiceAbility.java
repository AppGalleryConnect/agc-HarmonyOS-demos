/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.huawei.apms.hos.crash;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteObject;
import ohos.utils.zson.ZSONObject;

import java.util.HashMap;
import java.util.Map;

public class ServiceAbility extends Ability {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "ServiceAbility");

    private MyRemote remote = new MyRemote();

    @Override
    public void onStart(Intent intent) {
        HiLog.error(LABEL_LOG, "ServiceAbility::onStart");
        super.onStart(intent);
    }

    @Override
    public void onBackground() {
        super.onBackground();
        HiLog.info(LABEL_LOG, "ServiceAbility::onBackground");
    }

    @Override
    public void onStop() {
        super.onStop();
        HiLog.info(LABEL_LOG, "ServiceAbility::onStop");
    }

    @Override
    public void onCommand(Intent intent, boolean restart, int startId) {
    }

    @Override
    public IRemoteObject onConnect(Intent intent) {
        super.onConnect(intent);
        return remote.asObject();
    }

    @Override
    public void onDisconnect(Intent intent) {
    }

    static class MyRemote extends RemoteObject implements IRemoteBroker {
        private static final int SUCCESS = 0;

        private static final int ERROR = 1;

        private static final int PLUS = 1001;

        MyRemote() {
            super("MyService_MyRemote");
        }

        @Override
        public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) {
            HiLog.info(LABEL_LOG, "ServiceAbility::create java fatal");
            switch (code) {
                case PLUS: {
                    String dataStr = data.readString();
                    int create = 3/0;

                    // 返回结果当前仅支持String，对于复杂结构可以序列化为ZSON字符串上报
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("code", SUCCESS);
                    result.put("abilityResult", dataStr);
                    reply.writeString(ZSONObject.toZSONString(result));
                    break;
                }
                default: {
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("abilityError", ERROR);
                    reply.writeString(ZSONObject.toZSONString(result));
                    return false;
                }
            }
            return true;
        }

        @Override
        public IRemoteObject asObject() {
            return this;
        }
    }
}