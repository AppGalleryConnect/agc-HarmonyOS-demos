/*
 * Copyright 2023. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smarket.hdc2023.service;

import com.smarket.hdc2023.MainAbility;
import com.smarket.hdc2023.MyApplication;
import com.smarket.hdc2023.bean.CeliaCardStyle;
import com.smarket.hdc2023.bean.TaskComplate;
import com.smarket.hdc2023.help.AudioPlayUtil;
import com.smarket.hdc2023.help.CommonUtil;
import com.smarket.hdc2023.http.HttpUtil;
import com.smarket.hdc2023.widget.form.database.DatabaseUtils;
import com.smarket.hdc2023.widget.form.database.FormData;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.LocalRemoteObject;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.webengine.WebView;
import ohos.agp.utils.TextTool;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.event.intentagent.IntentAgent;
import ohos.event.intentagent.IntentAgentConstant;
import ohos.event.intentagent.IntentAgentHelper;
import ohos.event.intentagent.IntentAgentInfo;
import ohos.event.notification.NotificationRequest;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.common.Source;
import ohos.media.player.Player;
import ohos.rpc.IRemoteObject;
import ohos.utils.zson.ZSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 播放音频服务
 */
public class AudioServiceAbility extends Ability {
    //完成时长(毫秒)
    private final int completeDuration = 15000;
    //播放音频URL
    private String sceneryAudioUrl = "";
    //播放音频卡片ID
    private String sceneryCardId = "";
    //播放音频任务完成提示
    private String sceneryMessage = "";
    //播放音频任务ID
    private String sceneryTaskId = "";
    //当前播放类型 0卡片 1详情 2Webview
    private static int sceneryType;

    //当前播放时长
    private int currentTime = 0;
    //任务是否达标
    private boolean isCompletePlay = false;
    //任务是否完成
    private boolean isCompletTTask = false;
    //是否刷新卡片
    private boolean isUpdateCard = false;
    //任务是否需要执行
    private boolean isGoTask = true;

    //本人分数
    private String totalScore = "";
    //当前任务分数
    private String increaseScore = "";

    //当前WebView
    private WebView webView;

    //播放回调
    private PlayerCallback myPlayerCallback;
    //播放对象
    private AudioRemoteObject remoteObject;
    //播放器
    private Player player;

    //播放源
    private Source source;

    public Boolean getNowPlaying() {
        return isNowPlaying;
    }

    //播放状态
    private Boolean isNowPlaying = null;

    //定时器
    Timer timer;

    private static final HiLogLabel TAG = new HiLogLabel(HiLog.DEBUG, 0x0, AudioServiceAbility.class.getName());

    @Override
    public IRemoteObject onConnect(Intent intent) {
        notice();
        if (remoteObject == null) {
            remoteObject = new AudioRemoteObject(this);
        }
        return remoteObject;
    }

    @Override
    public void onStop() {
        super.onStop();
        closePlay();
        isNowPlaying = null;
        cancelBackgroundRunning();
        //取消定时任务
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onDisconnect(Intent intent) {
    }

    /**
     * 前台服务
     */
    private void notice() {
        // 创建通知
        NotificationRequest request = new NotificationRequest(1005);
        NotificationRequest.NotificationNormalContent content = new NotificationRequest.NotificationNormalContent();
        content.setTitle("华为开发者大会2023");
        NotificationRequest.NotificationContent notificationContent = new NotificationRequest.NotificationContent(content);
        request.setContent(notificationContent);
        request.setIntentAgent(startAbilityIntentAgent());
        // 绑定通知
        keepBackgroundRunning(1005, request);
    }

    // 前台服务点击跳转
    private IntentAgent startAbilityIntentAgent() {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder().withDeviceId("").withBundleName(getBundleName()).withAbilityName(MainAbility.class.getName()).build();
        intent.setOperation(operation);
        //intent.setParam("params", "{\"message\":\"goLogin\"}");
        List<Intent> intentList = new ArrayList<>();
        intentList.add(intent);
        List<IntentAgentConstant.Flags> flags = new ArrayList<>();
        flags.add(IntentAgentConstant.Flags.UPDATE_PRESENT_FLAG);
        IntentAgentInfo paramsInfo = new IntentAgentInfo(200, IntentAgentConstant.OperationType.START_ABILITY, flags, intentList, null);
        IntentAgent intentAgent = IntentAgentHelper.getIntentAgent(this, paramsInfo);
        return intentAgent;
    }

    /**
     * 初始化player
     */
    public void initPlayer() {
        if (getPlayer() == null) {
            myPlayerCallback = null;
            myPlayerCallback = new PlayerCallback();
            player = new Player(this);
            player.setPlayerCallback(myPlayerCallback);
        }
    }

    /**
     * 获取player对象
     *
     * @return player对象
     */
    public Player getPlayer() {
        return player;
    }

    //停止播放
    public void stopPlay() {
        isUpdateCard = true;
        completeFATask();
        if (getPlayer() != null) {
            if (getIsPlay()) {
                player.stop();
            }
            player.release();
            player = null;
            source = null;
        }
    }

    //停止播放
    public void closePlay() {
        stopPlay();
        isNowPlaying = false;
        updateCardData();
    }

    /**
     * 获取是否在播放
     *
     * @return 是否播放
     */
    public boolean getIsPlay() {
        if (player != null) {
            return player.isNowPlaying();
        }
        return false;
    }

    /**
     * 更新播放按钮
     */
    public void updateCardData() {
        if (!TextTool.isNullOrEmpty(sceneryCardId)) {
            ZSONObject zsonObject = new ZSONObject();
            if (isNowPlaying) {
                zsonObject.put("sceneryPlayImg", "/common/parse.png");
            } else {
                zsonObject.put("sceneryPlayImg", "/common/play.png");
            }
            //刷新卡片
            CommonUtil.updateCardForm(this, Long.valueOf(sceneryCardId), zsonObject);
        }
        if (sceneryType == 2) {
            AudioPlayUtil.updateWebViewPlayImg(this, webView, isNowPlaying);
        }
    }

    /**
     * 播放/暂停音频
     *
     * @param _sceneryAudioUrl 播放音频URL
     * @param _sceneryCardId   播放音频卡片ID
     * @param _sceneryMessage  播放音频任务完成提示
     * @param _sceneryTaskId   播放音频任务ID
     * @param _sceneryType     当前播放类型 0卡片 1详情 2Webview
     * @param _webView         当前WebView
     */
    public void playAudio(String _sceneryAudioUrl, String _sceneryCardId, String _sceneryMessage, String _sceneryTaskId, int _sceneryType, WebView _webView) {
        if (isNowPlaying == null) {
            isNowPlaying = getIsPlay();
        }
        HiLog.debug(TAG, "音频地址：" + _sceneryAudioUrl);
        Boolean isUpdate = false;//是否更换播放源
        if (_sceneryType != sceneryType) {
            //播放类型不一样
            isUpdate = true;
        } else if (!TextTool.isNullOrEmpty(_sceneryAudioUrl) && !sceneryAudioUrl.equals(_sceneryAudioUrl)) {
            //播放地址发生变化
            isUpdate = true;
        } else if (!TextTool.isNullOrEmpty(_sceneryCardId) && !sceneryCardId.equals(_sceneryCardId)) {
            //卡片发生变化
            isUpdate = true;
        }
        if (isUpdate) {
            //停止当前播放
            isNowPlaying = false;
            closePlay();
            sceneryAudioUrl = _sceneryAudioUrl;
            sceneryType = _sceneryType;
            if (sceneryType == 0) {
                //卡片
                if (!TextTool.isNullOrEmpty(_sceneryCardId)) {
                    sceneryCardId = _sceneryCardId;
                }
                if (!TextTool.isNullOrEmpty(_sceneryMessage)) {
                    sceneryMessage = _sceneryMessage;
                }
                if (!TextTool.isNullOrEmpty(_sceneryTaskId)) {
                    sceneryTaskId = _sceneryTaskId;
                }
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (getPlayer() != null && getPlayer().isNowPlaying()) {
                            currentTime = getPlayer().getCurrentTime();
                            completeFATask();
                        }
                    }
                }, 0, 1000);
            } else if (sceneryType == 2) {
                if (_webView != null) {
                    webView = _webView;
                }
            }
            currentTime = 0;
            isCompletePlay = false;
            isCompletTTask = false;
            isUpdateCard = false;
            isGoTask = true;
        }

        isNowPlaying = !isNowPlaying;
        updateCardData();

        if (source == null) {
            //一个Player对象多次设置资源后,callback回调就不走了,此处每次设置资源都使用了新的Player对象
            stopPlay();
            initPlayer();
            source = new Source(sceneryAudioUrl);
            player.setSource(source);
            player.prepare();
        } else {
            //当用户杀死应用时，player就会为null，此时点击卡片播放，需做判空处理
            if (getPlayer() == null) {
                initPlayer();
            }
        }
        if (isNowPlaying) {
            isUpdateCard = false;
            if (!getIsPlay()) {
                player.play();
            }
        } else {
            if (getIsPlay()) {
                player.pause();
            }
            isUpdateCard = true;
            completeFATask();
        }
    }

    /**
     * player状态回掉
     */
    class PlayerCallback implements Player.IPlayerCallback {
        @Override
        public void onPrepared() {
        }

        @Override
        public void onMessage(int type, int extra) {
        }

        @Override
        public void onError(int errorType, int errorCode) {
            //出错了，停止播放
            AudioPlayUtil.stopConnect(null, 999);
        }

        @Override
        public void onResolutionChanged(int i, int i1) {
        }

        @Override
        public void onPlayBackComplete() {
            //播放完成
            AudioPlayUtil.stopConnect(null, 999);
        }

        @Override
        public void onRewindToComplete() {
        }

        @Override
        public void onBufferingChange(int i) {
        }

        @Override
        public void onNewTimedMetaData(Player.MediaTimedMetaData mediaTimedMetaData) {
        }

        @Override
        public void onMediaTimeIncontinuity(Player.MediaTimeInfo mediaTimeInfo) {
        }
    }

    public class AudioRemoteObject extends LocalRemoteObject {
        private AudioServiceAbility audioService;

        /**
         * 构造方法
         *
         * @param audioService 服务类对象
         */
        AudioRemoteObject(AudioServiceAbility audioService) {
            this.audioService = audioService;
        }

        public AudioServiceAbility getAudioService() {
            return audioService;
        }
    }

    /**
     * 完成卡片任务
     */
    private void completeFATask() {
        if (isGoTask) {
            getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(() -> {
                if (!TextTool.isNullOrEmpty(MyApplication.getSess())) {
                    if (getPlayer() != null && getPlayer().isNowPlaying()) {
                        currentTime = getPlayer().getCurrentTime();
                        HiLog.debug(TAG, "当前播放时长(毫秒)：" + currentTime);
                    }
                    if (!isCompletTTask) {
                        if (!isCompletePlay) {
                            if (currentTime >= completeDuration && !TextTool.isNullOrEmpty(sceneryTaskId)) {
                                //任务已完成
                                isCompletePlay = true;
                                //调用接口完成任务
                                TaskComplate taskComplate = HttpUtil.completeFATask(sceneryTaskId);
                                if (taskComplate != null && taskComplate.totalScore != null && taskComplate.increaseScore != null) {
                                    isCompletTTask = true;
                                    totalScore = taskComplate.totalScore.toString();
                                    increaseScore = taskComplate.increaseScore.toString();
                                    completeFATask();
                                } else {
                                    //该任务不在执行
                                    isGoTask = false;
                                    //取消定时器
                                    if (timer != null) {
                                        timer.cancel();
                                        timer = null;
                                    }
                                }
                            }
                        } else {
                            //取消定时器
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                            }
                            //等待接口调用完成
                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    completeFATask();
                                }
                            }, 500);
                        }
                    } else {
                        //判断是否满足刷新卡片条件，满足则刷新
                        if (isUpdateCard && !TextTool.isNullOrEmpty(sceneryCardId)) {
                            FormData formData = DatabaseUtils.queryCardData(Long.valueOf(sceneryCardId));
                            if (formData != null && !TextTool.isNullOrEmpty(formData.getPoiId())) {
                                String poiIdEnd = formData.getPoiId().substring(formData.getPoiId().length() - 1);
                                if (poiIdEnd.equals("3")) {
                                    ZSONObject zsonObject = new ZSONObject();
                                    //卡片样式
                                    CeliaCardStyle celiaCardStyle = new CeliaCardStyle();
                                    celiaCardStyle.bgClass1 = "card-bg2";
                                    celiaCardStyle.hasIntegralBtn = true;
                                    celiaCardStyle.isIntegralCard = true;
                                    celiaCardStyle.hasAddIntegral = true;
                                    zsonObject.put("style", celiaCardStyle);

                                    //卡片内容
                                    zsonObject.put("integralTitle", "码力互动赢大奖");
                                    zsonObject.put("integralText", sceneryMessage);
                                    zsonObject.put("integralNum", totalScore);
                                    zsonObject.put("integralAddNum", increaseScore);

                                    //刷新卡片
                                    CommonUtil.updateCardForm(this, Long.valueOf(sceneryCardId), zsonObject);
                                }
                            }
                        }
                    }
                }
            });
        }
    }
}