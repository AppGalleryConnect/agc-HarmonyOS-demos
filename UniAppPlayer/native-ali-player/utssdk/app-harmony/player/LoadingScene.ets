@Component
export struct LoadingScene {
  @Prop isShow: boolean
  @Prop loadingProgress: number
  @Prop loadingSpeed: number

  build() {
    if (this.isShow) {
      Column() {
        Row() {
          Column() {
            Stack() {
              Progress({ value: this.loadingProgress, total: 100, type: ProgressType.ScaleRing })
                .color(Color.Grey).width(45)
                .style({ strokeWidth: 8, scaleCount: 20, scaleWidth: 3 })
              Text(this.loadingProgress + '%').fontSize(12).fontColor('#FFFFFF');
            }.margin({ top: 5 })

            Text("Loading... " + this.loadingSpeed + "Kb/s")
              .fontSize(12)
              .fontColor('#FFFFFF')
              .margin({ bottom: 10 })

          }
          .width('100%')
          .justifyContent(FlexAlign.Center)
          .alignItems(HorizontalAlign.Center)
          .height('100%')
          .backgroundColor('rgba(0, 0, 0, 0.7)')
          .borderRadius(5)
        }
        .width('30%')
        .aspectRatio(1)
        .alignItems(VerticalAlign.Center)
        .justifyContent(FlexAlign.Center)
        .margin({ left: '35%', right: '35%' })
      }
      .width('100%')
      .height('100%')
      .justifyContent(FlexAlign.Center)
      .alignItems(HorizontalAlign.Center)
    }
  }
}
