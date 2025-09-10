import { AliPlayer } from '@aliyun_video_cloud/aliyun_player'
import { SEEK_TYPE } from './Constants'

@Component
export struct VideoControls {
  @Prop isShow: boolean
  @Prop player: AliPlayer | undefined
  // 播放进度
  @Prop videoProgress: number
  // 总时长
  @Prop duration: number
  // 正在播放
  @Prop isPlaying: boolean
  // 总时长显示
  @Prop durationFormat: string
  // 播放进度显示
  @Prop progressFormat: string
  // 是否全屏
  @Prop isFullscreen: boolean
  progressChangeCallback: Function = (value: number) => {}
  fullscreenCallback: Function = () => {}

  build() {
    if (this.isShow) {
      Column() {
        Row() {
          // 开始暂停
          Column() {
            SymbolGlyph(this.isPlaying? $r('sys.symbol.pause') : $r('sys.symbol.play_fill'))
              .fontSize(24)
              .fontColor(['rgba(255, 255, 255, 0.9)'])
              .onClick(() => {
                if (this.isPlaying) {
                  this.player?.pause()
                } else {
                  this.player?.start()
                }
              })
          }

          // 进度
          Column() {
            Text(this.progressFormat)
              .fontSize(10)
              .fontColor('#ffffff')
              .textAlign(TextAlign.Center)
              .maxLines(1)
              .clip(false)
          }
          .margin({
            left: 16, right: 8
          })

          // 进度条
          Column() {
            Stack({ alignContent: Alignment.Start }) {
              Slider({
                value: this.videoProgress,
                min: 0,
                max: 100,
              })
                .trackColor("rgba(255, 255, 255, 0.4)")
                .selectedColor("rgb(255, 117, 0)")
                .showTips(true)
                .onChange((value: number, mode: SliderChangeMode) => {
                  this.progressChangeCallback?.(value)
                  if (mode === SliderChangeMode.End || mode === SliderChangeMode.Click) {
                    if (this.duration <= 0) {
                      return;
                    }
                    // this.uiController.showFinishScene = false;
                    let seekValue: number = Math.floor(this.duration / 100 * value);
                    this.player?.seekTo(seekValue, SEEK_TYPE.ACCURATE);
                  }
                })
            }
          }
          .layoutWeight(1)

          // 总时长
          Column() {
            Text(this.durationFormat)
              .fontSize(10)
              .fontColor('#ffffff')
              .textAlign(TextAlign.Center)
              .maxLines(1)
              .clip(false)
          }
          .margin({
            left: 8, right: 16
          })

          // 全屏按钮
          if (!this.isFullscreen) {
            Column() {
              SymbolGlyph($r('sys.symbol.arrow_up_left_and_arrow_down_right'))
                .fontSize(24)
                .fontColor(['rgba(255, 255, 255, 0.9)'])
                .onClick(() => {
                  this.fullscreenCallback?.()
                })
            }
          }
        }
        .width("100%")
      }
      .padding({
        top: 12,
        right: 16,
        bottom: 12,
        left: 16
      })
      .margin({
        bottom: this.isFullscreen ? 10 : 0
      })
      .width("100%")
      .height(48)
    }
  }
}