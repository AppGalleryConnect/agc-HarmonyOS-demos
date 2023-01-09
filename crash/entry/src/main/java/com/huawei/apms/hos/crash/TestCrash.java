/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.huawei.apms.hos.crash;

import com.huawei.apm.crash.APMCrash;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class TestCrash {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "TestCrash");

    private TestCrash() {
    }

    public static void createJvmNormalFatal(boolean runInNewThread) {
        if (runInNewThread) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    throw new UnsupportedOperationException("test java exception in new thread");
                }
            };

            thread.setName("xcrash_test_java_thread");
            thread.start();
        } else {
            throw new UnsupportedOperationException("test java exception in origin thread");
        }
    }

    public static void createJvmFatal(boolean runInNewThread) {
        if (runInNewThread) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    throw new UnsupportedOperationException("test java exception");
                }
            };

            thread.setName("xcrash_test_java_thread");
            thread.start();
        } else {
            Object[] o = null;
            while (true) {
                o = new Object[] {o};
            }
        }
    }

    public static void createJvmException() {
        try {
            int[] testArray = {1};
            testArray[3] = 2;
        } catch (Exception exception) {
            HiLog.warn(LABEL_LOG, "createJvmException " + exception.getMessage());
            APMCrash.getInstance().recordException(exception);
        }
    }

    static {
        System.loadLibrary("hello");
    }

    public static native void createNativeException(int i);
}
