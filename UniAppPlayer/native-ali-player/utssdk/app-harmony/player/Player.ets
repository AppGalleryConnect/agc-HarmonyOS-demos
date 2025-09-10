import { VideoComponent } from './VideoComponent';
import VideoController from './VideoController';
import { IAuthInfo } from './Modals'

import { VOD_TYPE } from './Constants';
import window from '@ohos.window';

import UIController from './UIController';

import { AliPlayer, InfoBean, InfoCode, PlayerConfig } from '@aliyun_video_cloud/aliyun_player';
import {
  OnCompletionListener,
  OnInfoListener,
  OnPreparedListener,
  OnStateChangedListener,
  OnErrorListener
} from '@aliyun_video_cloud/aliyun_player/src/main/ets/com/aliyun/player/IPlayer';
import { InvokeMethod, ProgressCallback, StateCallback } from './Modals';

@Component
export struct PlayerPage {
  @Prop videoUrl: string;
  @Prop authInfo: IAuthInfo;
  @Prop @Watch("methodInvoke") invokeMethod: InvokeMethod
  @Prop vodType: VOD_TYPE = VOD_TYPE.URL;
  private aliPlayerIndex: number = VideoController.createAliPlayer(getContext(), null);
  @State uiController: UIController = new UIController({
    isFullScreen: false
  })
  onFullScreen?: Function
  onProgress?: Function
  onState?: Function
  onBackButton?: Function
  lastInvokeMethodId: number = -1;

  methodInvoke() {
    if (this.lastInvokeMethodId === this.invokeMethod.versionId) {
      return;
    }
    if (this.invokeMethod.method === 'prepare') {
      VideoController.getPlayer(this.aliPlayerIndex)?.prepare();
    } else if (this.invokeMethod.method === 'start') {
      VideoController.getPlayer(this.aliPlayerIndex)?.start();
    } else if (this.invokeMethod.method === 'pause') {
      VideoController.getPlayer(this.aliPlayerIndex)?.pause();
    } else if (this.invokeMethod.method === 'stop') {
      VideoController.getPlayer(this.aliPlayerIndex)?.stop();
    } else if (this.invokeMethod.method === 'seekTo') {
      const params = this.invokeMethod.params
      const value = parseInt(params['value']);
      VideoController.getPlayer(this.aliPlayerIndex)?.seekTo(value, 1);
    } else if (this.invokeMethod.method === 'speed') {
      const params = this.invokeMethod.params;
      const value = parseInt(params['value']);
      if (this.uiController.speedSelects.indexOf(value) === -1) {
        return;
      }
      this.uiController.speed = value;
      VideoController.getPlayer(this.aliPlayerIndex)?.setSpeed(value);
    } else if (this.invokeMethod.method === 'fullscreenChange') {
      this.onFullScreenListen();
    }
	this.lastInvokeMethodId = this.invokeMethod.versionId;
  }

  async fullScreenChanged() {
    try {
      const lastWindow = await window.getLastWindow(getContext(this));
      this.uiController.isFullScreen = !this.uiController.isFullScreen;
      lastWindow.setPreferredOrientation(this.uiController.isFullScreen ? window.Orientation.LANDSCAPE : window.Orientation.PORTRAIT);
      lastWindow.setWindowSystemBarEnable(this.uiController.isFullScreen ? [] : ['status']);
    } catch (error) {
      console.error('On fullscreen changed error happened', error);
    }
  }

  onFullScreenListen = () => {
    this.fullScreenChanged();
    this.onFullScreen?.()
  }

  mPrepared: OnPreparedListener = {
    onPrepared: () => {
      let player: AliPlayer | undefined = this.getPlayer();
      if (player) {
        this.uiController.onPrepared(player);
      }
    }
  };

  mOnVideoInfo: OnInfoListener = {
    onInfo: (bean: InfoBean) => {
      this.uiController.onVideoInfo(bean);
      if (bean.getCode() === InfoCode.CurrentPosition) {
        let videoProgress = bean.getExtraValue();
        this.onProgress?.({ detail: { value: videoProgress } } as ProgressCallback)
      }
    }
  }

  mOnCompletion: OnCompletionListener = {
    onCompletion: () => {
      this.uiController.onCompletion();
    }
  }

  mOnStatusChangedListener: OnStateChangedListener = {
    onStateChanged: (status: number) => {
      this.uiController.onStatusChanged(status)
      this.onState?.({ detail: { value: status } } as StateCallback)
    }
  }

  mOnErrorEventListener: OnErrorListener = {
    onError: (errorInfo) => {
      VideoController.getPlayer(this.aliPlayerIndex)?.stop();
      this.uiController.onError(errorInfo);
    }
  }

  getPlayer(): AliPlayer | undefined {
    return VideoController.getPlayer(this.aliPlayerIndex);
  }

  initPlayerEventListeners() {
    let player: AliPlayer | undefined = this.getPlayer();
    if (player) {
      player.setOnPreparedListener(this.mPrepared);
      player.setOnCompletionListener(this.mOnCompletion);
      player.setOnInfoListener(this.mOnVideoInfo);
      player.setOnStateChangedListener(this.mOnStatusChangedListener);
      player.setOnErrorListener(this.mOnErrorEventListener);
	  const config = new PlayerConfig();
	  config.mNetworkTimeout = 2000
	  player.setConfig(config);
    }
  }

  aboutToAppear() {
    this.initPlayerEventListeners();
  }

  async aboutToDisappear() {
    try {
      this.onFullScreen = undefined
      this.onProgress = undefined
      this.onState = undefined
	  this.onBackButton = undefined
      VideoController.getPlayer(this.aliPlayerIndex)?.stop();
      VideoController.dispose(this.aliPlayerIndex);
      const lastWindow = await window.getLastWindow(getContext(this));
      lastWindow.setWindowBrightness(-1);
    } catch (error) {
      console.error('Disappear handle error happened', error);
    }
  }

  build() {
    Row() {
      Column() {
        VideoComponent({
          videoUrl: this.videoUrl,
          playerVodType: this.vodType,
          playerIndex: this.aliPlayerIndex,
          onFullScreenChanged: this.onFullScreenListen,
		  onBackButton: this.onBackButton,
          uiController: this.uiController,
          authInfo: this.authInfo
        });
      }
      .width('100%')
    }
    .height('100%')
    .alignItems(VerticalAlign.Top)
  }
}
