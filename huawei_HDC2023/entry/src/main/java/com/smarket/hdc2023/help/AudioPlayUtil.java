package com.smarket.hdc2023.help;

import com.smarket.hdc2023.service.AudioServiceAbility;
import com.smarket.hdc2023.widget.form.database.DatabaseUtils;
import com.smarket.hdc2023.widget.form.database.FormData;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.webengine.WebView;
import ohos.agp.utils.TextTool;
import ohos.app.Context;
import ohos.bundle.ElementName;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageParcel;
import ohos.utils.zson.ZSONObject;

import java.util.List;

/**
 * 播放音频工具类
 */
public class AudioPlayUtil {
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.DEBUG, 0x0, AudioPlayUtil.class.getName());

    //音频播放对象
    private static AudioServiceAbility.AudioRemoteObject audioRemoteObject;

    //音频播放连接
    private static IAbilityConnection connection;

    //音频播放服务
    private static Intent audioIntent;

    //当前ability
    private static Ability ability;

    //当前播放音频URL
    private static String sceneryAudioUrl = "";

    //当前播放音频卡片ID
    private static String sceneryCardId = "";

    //当前播放类型 0卡片 1详情 2Webview
    private static int sceneryType = 999;

    /**
     * 播放/暂停音频
     *
     * @param _ability         ability
     * @param _sceneryAudioUrl 播放音频URL
     * @param _sceneryCardId   播放音频卡片ID
     * @param sceneryMessage   播放音频任务完成提示
     * @param sceneryTaskId    播放音频任务ID
     * @param _sceneryType     当前播放类型 0卡片 1详情 2Webview
     * @param webView          webView
     */
    public static void goPlay(Ability _ability, String _sceneryAudioUrl, String _sceneryCardId, String sceneryMessage, String sceneryTaskId, int _sceneryType, WebView webView) {
        try {
            //返回主界面后audioRemoteObject就会为空，此时操作卡片就需要重新获取audioRemoteObject对象
            if (audioRemoteObject == null) {
                ability = _ability;
                sceneryAudioUrl = _sceneryAudioUrl;
                sceneryCardId = _sceneryCardId;
                sceneryType = _sceneryType;
                startService(sceneryMessage, sceneryTaskId, webView);
            } else {
                if (ability.getClass() != _ability.getClass() || sceneryType != _sceneryType || !TextTool.isEqual(sceneryAudioUrl, _sceneryAudioUrl) || !TextTool.isEqual(sceneryCardId, _sceneryCardId) || audioRemoteObject.getAudioService() == null || audioRemoteObject.getAudioService().getNowPlaying() == null) {
                    try {
                        ability.disconnectAbility(connection);
                    } catch (Exception ignored) {
                    }
                    try {
                        ability.stopAbility(audioIntent);
                    } catch (Exception ignored) {
                    }
                    audioRemoteObject = null;
                    audioIntent = null;
                    connection = null;
                    ability = _ability;
                    sceneryAudioUrl = _sceneryAudioUrl;
                    sceneryCardId = _sceneryCardId;
                    sceneryType = _sceneryType;
                    startService(sceneryMessage, sceneryTaskId, webView);
                } else {
                    audioRemoteObject.getAudioService().playAudio(sceneryAudioUrl, sceneryCardId, sceneryMessage, sceneryTaskId, sceneryType, webView);
                }
            }
        } catch (Exception e) {
            HiLog.error(TAG, "goPlayErr：" + e.toString());
        }
    }

    /**
     * 启动并连接服务
     *
     * @param sceneryMessage 播放音频任务完成提示
     * @param sceneryTaskId  播放音频任务ID
     * @param webView        webView
     */
    private static void startService(String sceneryMessage, String sceneryTaskId, WebView webView) {
        connection = new IAbilityConnection() {
            @Override
            public void onAbilityConnectDone(ElementName elementName, IRemoteObject remoteObject, int i) {
                if (remoteObject instanceof AudioServiceAbility.AudioRemoteObject) {
                    audioRemoteObject = (AudioServiceAbility.AudioRemoteObject) remoteObject;
                    audioRemoteObject.getAudioService().playAudio(sceneryAudioUrl, sceneryCardId, sceneryMessage, sceneryTaskId, sceneryType, webView);
                }
            }

            @Override
            public void onAbilityDisconnectDone(ElementName elementName, int i) {
                audioRemoteObject = null;
                //停止所有小艺建议音频播放卡片
                List<FormData> formData = DatabaseUtils.queryAllCardData();
                ZSONObject zsonObject = new ZSONObject();
                zsonObject.put("sceneryPlayImg", "/common/pause.png");
                for (FormData formDatum : formData) {
                    if (formDatum.getName().equals("celia") && !TextTool.isNullOrEmpty(formDatum.getPoiId())) {
                        String poiIdEnd = formDatum.getPoiId().substring(formDatum.getPoiId().length() - 1);
                        if (poiIdEnd.equals("3")) {
                            //刷新卡片
                            CommonUtil.updateCardForm(ability, formDatum.getFormId(), zsonObject);
                        }
                    }
                }
                updateWebViewPlayImg(ability, webView, false);
            }
        };
        audioIntent = new Intent();
        Operation operation = new Intent.OperationBuilder().withDeviceId("").withBundleName(ability.getBundleName()).withAbilityName(AudioServiceAbility.class.getName()).build();
        audioIntent.setOperation(operation);
        ability.startAbility(audioIntent);
        ability.connectAbility(audioIntent, connection);
    }

    /**
     * 刷新Webview播放状态
     *
     * @param context      context
     * @param webView      webView
     * @param isNowPlaying 播放状态
     */
    public static void updateWebViewPlayImg(Context context, WebView webView, Boolean isNowPlaying) {
        if (webView != null) {
            context.getUITaskDispatcher().asyncDispatch(() -> {
                try {
                    webView.executeJs("javascript:voicePlayCallback(" + isNowPlaying.toString() + ")", msg -> {
                        // 在此确认返回结果
                    });
                } catch (Exception ignored) {
                }
            });
        }
    }

    /**
     * 断开音频连接
     *
     * @param _ability     ability
     * @param _sceneryType 当前播放类型 0卡片 1详情 2Webview
     */
    public static void stopConnect(Ability _ability, int _sceneryType) {
        if (ability != null && (_ability == null || ability.getClass() == _ability.getClass()) && (_sceneryType == 999 || sceneryType == _sceneryType)) {
            try {
                ability.disconnectAbility(connection);
            } catch (Exception ignored) {
            }
            try {
                ability.stopAbility(audioIntent);
            } catch (Exception ignored) {
            }
            audioRemoteObject = null;
            audioIntent = null;
            connection = null;
        }
    }

    /**
     * 查询JS语音播放状态
     *
     * @param _ability     ability
     * @param reply        JS对象
     * @param _sceneryType 当前播放类型 0卡片 1详情 2Webview
     */
    public static void queryJsPlayStatus(Ability _ability, MessageParcel reply, int _sceneryType) {
        if (ability != null && _ability != null && ability.getClass() == _ability.getClass() && sceneryType == _sceneryType && audioRemoteObject != null && audioRemoteObject.getAudioService().getNowPlaying() != null) {
            reply.writeString(audioRemoteObject.getAudioService().getNowPlaying().toString());
        } else {
            reply.writeString("false");
        }
    }
}