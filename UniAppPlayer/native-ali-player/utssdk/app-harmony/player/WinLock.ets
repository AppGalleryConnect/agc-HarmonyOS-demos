
@Component
export struct WinLock {
  @Prop isShow: boolean = false
  @Prop isLock: boolean = false
  onLockClick: Function = () => {}
  build() {
    if (this.isShow) {
      Column() {
        Row() {
          Column() {
            SymbolGlyph($r(this.isLock ? 'sys.symbol.lock_fill': 'sys.symbol.lock_open_fill'))
              .fontSize(24)
              .fontColor(['rgba(255, 255, 255, 0.9)'])
              .margin(8)
          }
          .width(40)
          .height(40)
          .borderRadius(20)
          .margin({
            left: 30
          })
          .backgroundColor("rgba(255, 255, 255, 0.1)")
          .onClick(() => {
            this.onLockClick()
          })
        }
        .height('100%')
        .alignItems(VerticalAlign.Center)
      }
      .height('100%')
      .alignItems(HorizontalAlign.Start)
    }
  }
}