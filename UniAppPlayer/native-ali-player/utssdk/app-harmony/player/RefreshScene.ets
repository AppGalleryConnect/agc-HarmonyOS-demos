
@Component
export struct RefreshScene {
  @Prop isShow: boolean
  build() {
    if (this.isShow) {
      Column() {
        Row() {
          Column() {
            LoadingProgress()
              .width(32)
              .height(32)
              .color('rgb(216, 216, 216)')
          }
          .width('100%')
          .alignItems(HorizontalAlign.Center)
        }
        .width('100%')
        .height('100%')
        .alignItems(VerticalAlign.Center)
      }
      .width("100%")
      .height("100%")
      .backgroundColor('rgba(0, 0, 0, 0.5)')
    }
  }
}
