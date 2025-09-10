import { AliPlayer, ErrorInfo, InfoBean, InfoCode } from '@aliyun_video_cloud/aliyun_player';
import { OnLoadingStatusListener } from '@aliyun_video_cloud/aliyun_player/src/main/ets/com/aliyun/player/IPlayer';
import { LOCAL_PROP_KEY_VOLUME, TIP_MODE } from './Constants';
import { CommonUtils } from './CommonUtils';
import { display } from '@kit.ArkUI';

export interface IUIControllerAlphaLayout {
  alphaWidth: number;
  alphaHeight: number;
  alphaX: number;
  alphaY: number;
}

export interface IUIControllerInitParams {
  ratio?: number;
  isFullScreen?: boolean;
  containerRatio?: number;
}

@Observed
export default class UIController {
  /**
   * 画面尺寸比例
   */
  public ratio: number = 16 / 9;
  /**
   * 容器尺寸比例（默认采用设备屏幕比例）
   */
  public containerRatio: number = -1;
  /**
   * 播放进度
   */
  public videoProgress: number = 0;
  /**
   * 缓存进度显示文本
   */
  public buffer: string = '00:00';
  /**
   * 缓存进度
   */
  public bufferedProgress: number = 0;
  /**
   * 时长进度文本
   */
  public mDuration: string = '00:00/00:00';

  /**
   * 播放时常显示
   */
  public progressFormat: string = '00:00:00'

  /**
   * 视频时长显示
   */
  public durationFormat: string = '00:00:00'
  /**
   * 总时长
   */
  public mVideoDuration: number = 0;
  /**
   * 全屏状态
   */
  public isFullScreen: boolean = false;
  /**
   * 结束界面开关
   */
  public showFinishScene: boolean = false;
  /**
   * 卡顿、加载界面开关
   */
  public showLoadingScene: boolean = false;
  /**
   * 手势亮度开关
   */
  public showBrightnessScene: boolean = false;
  public currentBrightness: number = 0.5; // 记录最后一次手势拖动前的亮度值
  public displayBrightness: number = 0.5; // 实时显示的亮度值
  /**
   * 手势音量开关
   */
  public showVolumeScene: boolean = false;
  public currentVolume: number = 50; // 记录最后一次手势拖动前的音量
  public displayVolume: number = 0.5; // 实时显示的音量
  public storedVolume: number = 50; // localStorage保存的值
  /**
   * 音量、亮度识别手势前 但touch已发生的标志
   */
  public potentialScene: TIP_MODE|undefined = undefined;
  /**
   * 加载进度
   */
  public loadingProgress: number = 0;
  /**
   * 加载速度
   */
  public loadingSpeed: number = 0;
  /**
   * 重播标记 prepare后 主动调用播放（防止autoplay为false时，重播没有自动播放）
   */
  public replaying: boolean = false;
  /**
   * 发生错误
   */
  public isError: boolean = false;
  public errInfo: ErrorInfo|undefined = undefined; // 错误信息

  /**
   * 设备屏幕尺寸比例
   */
  private screenRatio: number = 1; // 初始化时aboutToAppear 赋值更新

  public videoStatus: number = -1;

  public speed: number = 1;

  public speedSelects: Array<number> = [0.75, 1, 1.25, 1.5, 2.0];

  public isLock: boolean = false;

  public isHidden: boolean = false;

  public timerList: Array<number> = []
  
  public refreshLoading: boolean = false

  constructor(config?: IUIControllerInitParams) {
    try {
      let screen = display.getDefaultDisplaySync();
      this.screenRatio = screen.width / screen.height;
      if (config) {
        if (typeof config.ratio === 'number' && config.ratio === config.ratio && config.ratio !== 0) {
          this.ratio = config.ratio;
        }
        config.isFullScreen !== undefined && (this.isFullScreen = !!config.isFullScreen);
        if (typeof config.containerRatio === 'number' && config.containerRatio === config.containerRatio && config.containerRatio !== 0) {
          this.containerRatio = config.containerRatio;
        }
      }
      if (this.containerRatio === -1) {
        this.containerRatio = this.screenRatio;
      }
      const volume: number|undefined = AppStorage.get(LOCAL_PROP_KEY_VOLUME);
      if (volume) {
        this.storedVolume = volume;
      }
    } catch (error) {

    }
  }

  public onPrepared(player: AliPlayer) {
    if (player) {
      // 初始化完成的回调 1、存音量，2、存视频时长
      let volume: number = player.getVolume();
      this.currentVolume = volume * 100 / 2; // volume [0, 2]
      this.displayVolume = volume / 2;
      this.mVideoDuration = player.getDuration();
      if (this.replaying) {
        player.start();
        this.replaying = false;
      }
    }
  }

  public onCompletion() {
    this.showFinishScene = true;
  }

  public onVideoInfo(bean: InfoBean) {
    if (bean.getCode() === InfoCode.CurrentPosition) {
      this.videoProgress = bean.getExtraValue() / this.mVideoDuration * 100;
      this.mDuration = CommonUtils.getDurationString(this.mVideoDuration, bean.getExtraValue());

      this.durationFormat = CommonUtils.secondToTime(Math.floor(this.mVideoDuration / 1000))
      this.progressFormat = CommonUtils.secondToTime(Math.floor(bean.getExtraValue() / 1000))

    } else if (bean.getCode() === InfoCode.BufferedPosition) {
      this.buffer = CommonUtils.secondToTime(Math.floor(bean.getExtraValue() / 1000));
      this.bufferedProgress = bean.getExtraValue() / this.mVideoDuration * 100;
    }
  }

  public onStatusChanged(status: number) {
    this.videoStatus = status
    // 延时隐藏操作部件
    this.showAndHideWidget()
	
	if (this.videoStatus === 1) {
	  this.refreshLoading = true
	} else {
	  this.refreshLoading = false
	}
  }

  public onError(errorInfo: ErrorInfo) {
    this.errInfo = errorInfo;
    this.isError = true;
  }

  public onLoadingProgress: OnLoadingStatusListener = {
    onLoadingBegin: () => {
      this.showLoadingScene = true;
    },
    onLoadingProgress: (percent: number, netSpeed: number) => {
      this.loadingProgress = percent;
      this.loadingSpeed = netSpeed;
    },
    onLoadingEnd: () => {
      this.showLoadingScene = false;
      this.loadingProgress = 0;
      this.loadingSpeed = 0;
    }
  }

  public isPlaying(): boolean {
    return this.videoStatus === 3;
  }
  
  public isPause(): boolean {
    return this.videoStatus === 4;
  }

  public nextSpeed(currentSpeed: number): number {
    for (let i = 0; i < this.speedSelects.length; i++) {
      if (this.speedSelects[i] === currentSpeed) {
        return this.speedSelects[(i + 1) % this.speedSelects.length]
      }
    }
    return this.speedSelects[1];
  }

  public showAndHideWidget() {
    this.isHidden = false;
    this.timerList.forEach(timer => {
      clearTimeout(timer);
    })
    if (this.isPlaying()) {
      const timer = setTimeout(() => {
        this.isHidden = true;
      }, 3000)
      this.timerList.push(timer);
    }
  }
  
  public showWidget() {
	this.isHidden = false;
	this.timerList.forEach(timer => {
	  clearTimeout(timer);
	})
  }
}
