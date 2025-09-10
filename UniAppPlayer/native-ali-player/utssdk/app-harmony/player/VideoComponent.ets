import { AliPlayer, VidAuth } from '@aliyun_video_cloud/aliyun_player';
import { UrlSource } from '@aliyun_video_cloud/aliyun_player/src/main/ets/com/aliyun/player/source/UrlSource';

import { VOD_TYPE } from './Constants';

import VideoController from './VideoController';
import UIController from './UIController';

import { IAuthInfo } from './Modals';

import { ErrorInfoWin } from './ErrorInfoWin';
import { VideoControls } from './VideoControls';
import { BrightnessAdjustPanel } from './BrightnessAdjustPanel';
import { VolumeAdjustPanel } from './VolumeAdjustPanel';
import { FinishScene } from './FinishScene';
import { LoadingScene } from './LoadingScene';
import { HeaderButtons } from './HeaderButtons';
import { WinLock } from './WinLock';
import { RefreshScene } from './RefreshScene';

@Component
export struct VideoComponent {
  playerVodType: string = VOD_TYPE.URL;
  videoUrl?: string = '';
  @ObjectLink uiController: UIController;
  private autoplay: boolean = false;
  @State playerIndex: number = -1;
  private xComponentController = new XComponentController();
  authInfo: IAuthInfo | null = null;
  onFullScreenChanged: Function = () => {};
  onBackButton: Function =() => {};

  private getPlayer(): AliPlayer | undefined {
    return VideoController.getPlayer(this.playerIndex);
  }

  private initVideoVodInfo() {
    let player: AliPlayer | undefined = this.getPlayer();
    if (player) {
      if (this.playerVodType === 'url') {
        let urlSource: UrlSource = new UrlSource()
        if (this.videoUrl) {
          urlSource.setUri(this.videoUrl);
        }
        player.setUrlDataSource(urlSource);
      } else if (this.playerVodType === 'auth' && this.authInfo) {
        const vidAuthSource: VidAuth = new VidAuth();
        vidAuthSource.setVid(this.authInfo.videoId);
        vidAuthSource.setPlayAuth(this.authInfo.playAuth);
        player.setVidAuthDataSource(vidAuthSource);
      }
    }
  }

  private initSurfaceBinding() {
    let player: AliPlayer | undefined = this.getPlayer();
    if (player) {
      player.setSurfaceId(this.playerIndex.toString());
    }
  }

  private initPlayerConfigs() {
    let player: AliPlayer | undefined = this.getPlayer();
    if (player) {
      if (this.autoplay) {
        player.setAutoPlay(true);
        player.prepare();
      }
      player.enableHardwareDecoder(false);
    }
  }

  build() {
    Stack({ alignContent: Alignment.BottomStart }) {
      XComponent({
        id: this.playerIndex.toString(),
        type: XComponentType.SURFACE,
        libraryname: 'premierlibrary',
        controller: this.xComponentController
      })
        .onLoad(async () => {
          this.initVideoVodInfo();
          this.initSurfaceBinding();
          this.initPlayerConfigs();

          // 视频准备
          this.getPlayer()?.prepare();
        })
        .width('100%')
        .height('100%')

      // 亮度操作界面
      BrightnessAdjustPanel({
        isShow: !this.uiController.isLock && !this.uiController.isHidden && !(this.uiController.videoStatus === 6),
        panelIsShow: this.uiController.showBrightnessScene,
        displayBrightness: this.uiController.displayBrightness,
        currentBrightness: this.uiController.currentBrightness,
        updateBrightnessStartCallback: (value: number) => {
          this.uiController.showBrightnessScene = true;
          this.uiController.currentBrightness = value;
        },
        updateBrightnessCallback: (value: number) => {
          this.uiController.displayBrightness = value
        },
        updateBrightnessEndCallback: (value: number) => {
          this.uiController.showBrightnessScene = false
          this.uiController.currentBrightness = value;
        },
		onDoubleClick: () => {
		  if (this.uiController.isPlaying()) {
			this.getPlayer()?.pause()
		  } else {
			this.getPlayer()?.start()
		  }
		}
      });

      // 音量操作界面
      VolumeAdjustPanel({
        isShow: !this.uiController.isLock && !this.uiController.isHidden && !(this.uiController.videoStatus === 6),
        panelIsShow: this.uiController.showVolumeScene,
        player: this.getPlayer(),
        displayVolume: this.uiController.displayVolume,
        currentVolume: this.uiController.currentVolume,
        updateVolumeStartCallback: () => {
          this.uiController.showVolumeScene = true
        },
        updateVolumeCallback: (value: number) => {
          this.uiController.displayVolume = value;
        },
        updateVolumeEndCallback: () => {
          this.uiController.showVolumeScene = false;
          this.uiController.storedVolume = this.uiController.displayVolume * 100;
          this.uiController.currentVolume = this.uiController.storedVolume;
        },
		onDoubleClick: () => {
		  if (this.uiController.isPlaying()) {
			this.getPlayer()?.pause()
		  } else {
			this.getPlayer()?.start()
		  }
		}
      });

      // 锁屏操作
      WinLock({
        isShow: this.uiController.isFullScreen && !this.uiController.isHidden && !(this.uiController.videoStatus === 6),
        isLock: this.uiController.isLock,
        onLockClick: () => {
          this.uiController.isLock = !this.uiController.isLock
        }
      })

      // 头部操作按钮展示
      HeaderButtons({
        isShow: !this.uiController.isLock && !this.uiController.isHidden && !this.uiController.isError && !(this.uiController.videoStatus === 6),
        speed: this.uiController.speed,
        onSpeedButtonClickCallback: (() => {
          const nextSpeed = this.uiController.nextSpeed(this.uiController.speed)
          this.getPlayer()?.setSpeed(nextSpeed);
          this.uiController.speed = nextSpeed;
        }),
        onMoreSpeedClickCallback: (value: number) => {
          this.getPlayer()?.setSpeed(value);
          this.uiController.speed = value;
        },
        onBackClick: () => {
          if (this.uiController.isFullScreen) {
            this.onFullScreenChanged?.()
          }
		  this.onBackButton?.();
        },
		onMoreButtonShow: () => {
		  this.uiController.showWidget();
		},
		onMoreButtonHide: () => {
		  this.uiController.showAndHideWidget();
		}
      })

      // 视频控制部件
      VideoControls({
        isShow: !this.uiController.isLock && !this.uiController.isHidden && !this.uiController.isError && !(this.uiController.videoStatus === 6),
        player: this.getPlayer(),
        isPlaying: this.uiController.isPlaying(),
        durationFormat: this.uiController.durationFormat,
        progressFormat: this.uiController.progressFormat,
        videoProgress: this.uiController.videoProgress,
        duration: this.uiController.mVideoDuration,
        isFullscreen: this.uiController.isFullScreen,
        progressChangeCallback: (value: number) => {
          this.uiController.videoProgress = value;
        },
        fullscreenCallback: () => {
          this.onFullScreenChanged?.()
        }
      })

      // 进度加载界面
      LoadingScene({
        isShow: this.uiController.showLoadingScene,
        loadingProgress: this.uiController.loadingProgress,
        loadingSpeed: this.uiController.loadingSpeed
      });

      // 播放完成界面
      FinishScene({
        isShow: this.uiController.showFinishScene,
        player: this.getPlayer(),
        replayCallback: () => {
          this.uiController.showFinishScene = false;
          this.uiController.replaying = true;
          this.getPlayer()?.stop();
          this.getPlayer()?.prepare();
        }
      });

      // 错误信息展示
      ErrorInfoWin({
        isShow: this.uiController.isError,
		errInfo: this.uiController.errInfo,
        onRefreshClick: () => {
          this.uiController.isError = false
          this.uiController.refreshLoading = true
          setTimeout(() => {
            this.getPlayer()?.prepare();
          }, 1000)
        }
      })
	  
	  // 刷新展示框
	  RefreshScene({
	    isShow: this.uiController.refreshLoading
	  })

    }.width('100%')
    .backgroundColor('#00ff00')
    .height('100%')
    .gesture(GestureGroup(GestureMode.Exclusive,
	  TapGesture({count:2})
		.onAction(()=>{
		  
		  if (this.uiController.isPlaying()) {
			this.getPlayer()?.pause()
		  }
		  if (this.uiController.isPause()) {
			this.getPlayer()?.start()
		  }
		}),
	  TapGesture({count:1})
	  .onAction(()=>{
		this.uiController.showAndHideWidget();
	  })
	))
    .onTouch(() => {
      this.uiController.showAndHideWidget();
    })
  }
}