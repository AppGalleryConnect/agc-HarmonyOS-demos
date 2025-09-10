import { window } from '@kit.ArkUI';

@Component
export struct BrightnessAdjustPanel {
  @Prop isShow: boolean
  @Prop panelIsShow: boolean
  @Prop displayBrightness: number;
  @Prop currentBrightness: number
  updateBrightnessStartCallback: Function = () => {}
  updateBrightnessCallback: Function = () => {}
  updateBrightnessEndCallback: Function = () => {}
  onDoubleClick: Function = () => {}

  private async gestureStartHandler() {
    try {
      const lastWindow = await window.getLastWindow(getContext(this));
      let brightness = lastWindow.getWindowProperties().brightness;
      if (brightness === -1) { // -1 表示跟随系统亮度 实际亮度未知 设置默认0.5
        brightness = 0.5;
      }
      this.updateBrightnessStartCallback(brightness);
    } catch (error) {
      console.log('Start update brightness error happened', error);
    }
  }

  private gestureMoveHandler(event?: GestureEvent) {
    const offset = event ? -event.offsetY : 0;
    let newBrightness = this.currentBrightness + (offset / 100);
    if (newBrightness <= 0) {
      newBrightness = 0;
    }
    if (newBrightness >= 1) {
      newBrightness = 1;
    }
    this.updateBrightness(newBrightness);
  }

  private async gestureEndHandler() {
    try {
      const lastWindow = await window.getLastWindow(getContext(this));
      let brightness = lastWindow.getWindowProperties().brightness;
      if (brightness === -1) {
        brightness = 0.5;
      }
      this.updateBrightnessEndCallback(brightness);
    } catch (error) {
      console.log('End update brightness error happened', error);
    }
  }

  private async updateBrightness(newBrightness: number) {
    try {
      const lastWindow = await window.getLastWindow(getContext(this));
      lastWindow.setWindowBrightness(newBrightness);
      this.updateBrightnessCallback(newBrightness);
    } catch (error) {
      console.error("Update brightness error happened", error)
    }
  }

  build() {
    if (this.isShow) {
      Column() {
        if (this.panelIsShow) {
          Row() {
            Column() {
              Text("亮度")
                .fontSize(12)
                .fontColor('#FFFFFF')
                .margin({ bottom: 10 })
              Text((this.displayBrightness * 100).toFixed(0) + "%")
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
          .height('100%')
          .alignItems(VerticalAlign.Center)
          .justifyContent(FlexAlign.Center)
        }
      }
      .width('50%')
      .height("100%")
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
