import { AliPlayer } from '@aliyun_video_cloud/aliyun_player'
import { LOCAL_PROP_KEY_VOLUME } from './Constants'

@Component
export struct VolumeAdjustPanel {
  @Prop isShow: boolean
  @Prop panelIsShow: boolean
  @Prop player: AliPlayer | undefined
  @Prop displayVolume: number
  @Prop currentVolume: number
  updateVolumeStartCallback: Function = () => {}
  updateVolumeCallback: Function = () => {}
  updateVolumeEndCallback: Function = () => {}
  onDoubleClick: Function = () => {}

  private async gestureStartHandler() {
    this.updateVolumeStartCallback();
  }

  private gestureMoveHandler(event?: GestureEvent) {
    const offset = event ? -event.offsetY : 0;
    let newVolume = (this.currentVolume / 50) + (offset / 100);
    if (newVolume <= 0) {
      newVolume = 0;
    }
    if (newVolume >= 2) { // 最大音量 200%
      newVolume = 2;
    }
    this.updateVolume(newVolume);
  }

  private async gestureEndHandler() {
    this.updateVolumeEndCallback();
  }

  private updateVolume(newVolume: number) {
    this.player?.setVolume(newVolume);
    AppStorage.setOrCreate<number>(LOCAL_PROP_KEY_VOLUME, newVolume * 50);
    this.updateVolumeCallback(newVolume / 2)
  }

  build() {
    if (this.isShow) {
      Column() {
        if (this.panelIsShow) {
          Row() {
            Column() {
              Text("音量")
                .fontSize(12)
                .fontColor('#FFFFFF')
                .margin({ bottom: 10 })
              Text((this.displayVolume * 100).toFixed(0) + "%")
                .fontSize(12)
                .fontColor('#FFFFFF')
                .margin({ bottom: 10 })
            }
            .justifyContent(FlexAlign.Center)
            .alignItems(HorizontalAlign.Center)
            .backgroundColor('rgba(0, 0, 0, 0.7)')
            .borderRadius(5)
            .padding({
              top: 10, right: 10, left: 10
            })
          }
          .width('100%')
          .height('100%')
          .alignItems(VerticalAlign.Center)
          .justifyContent(FlexAlign.Center)
        }
      }
      .width('50%')
      .height("100%")
      .position({
        right: 0
      })
      .alignItems(HorizontalAlign.Center)
      .gesture(GestureGroup(GestureMode.Parallel,
        PanGesture({ direction: PanDirection.Vertical })
          .onActionStart(() => {
            this.gestureStartHandler();
          })
          .onActionUpdate((event?: GestureEvent) => {
            this.gestureMoveHandler(event);
          })
          .onActionEnd(() => {
            this.gestureEndHandler();
          }),
        TapGesture({ count: 2 })
          .onAction(() => {
            this.onDoubleClick()
          })
      )
      )
    }
  }
}