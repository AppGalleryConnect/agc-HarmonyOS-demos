import { AliPlayer } from '@aliyun_video_cloud/aliyun_player'

@Component
export struct FinishScene {
  @Prop isShow: boolean
  @Prop player: AliPlayer | undefined
  replayCallback: Function = () => {}

  build() {
    if (this.isShow) {
      Column() {
        Row() {
          Column() {
            Text("播放结束")
              .fontSize(12)
              .fontColor('#FFFFFF')
              .margin({ bottom: 15 })
            Button('重播', { type: ButtonType.Normal, stateEffect: true })
              .fontColor('#4488CC')
              .backgroundColor('rgba(0, 0, 0, 0)')
              .borderRadius(3)
              .borderWidth(1)
              .borderColor('#4488CC')
              .fontSize(12)
              .height(28)
              .margin({ bottom: 5 })
              .onClick(() => {
                this.replayCallback?.()
              })
          }
          .width('100%')
          .justifyContent(FlexAlign.Center)
          .alignItems(HorizontalAlign.Center)
          .height('100%')
          .backgroundColor('rgba(0, 0, 0, 0.7)')
          .borderRadius(5)
        }
        .width('60%')
        .alignItems(VerticalAlign.Center)
        .justifyContent(FlexAlign.Center)
        .margin({ left: '20%', right: '20%' })
        .aspectRatio(5 / 3)
      }
      .width('100%')
      .height('100%')
      .justifyContent(FlexAlign.Center)
      .alignItems(HorizontalAlign.Center)
    }
  }
}