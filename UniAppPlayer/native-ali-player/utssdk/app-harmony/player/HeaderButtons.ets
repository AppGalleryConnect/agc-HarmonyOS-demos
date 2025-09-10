const speedIcons: Map<number, string> = new Map([
  [0.75, 'speed_75percent'],
  [1, 'speed_100percent'],
  [1.25, 'speed_125percent'],
  [1.5, 'speed_150percent'],
  [2.0, 'speed_200percent'],
])

function numberFormat(num: number) {
  return num % 1 === 0 ? `${num}.0` : num
}

interface MoreButtonParams {
  speed: number
  onSpeedClick: Function
}

@Component
export struct HeaderButtons {
  @State moreButtonsIsShow: boolean = false
  @Prop isShow: boolean
  @Prop speed: number
  onSpeedButtonClickCallback: Function = () => {}
  onMoreSpeedClickCallback: Function = () => {}
  onBackClick: Function = () => {}
  onMoreButtonShow: Function = () => {}
  onMoreButtonHide: Function = () => {}

  getSpeedIcon(): string {
    const speedFormat = speedIcons.get(this.speed) ?? 'speed_100percent';
    return `sys.symbol.${speedFormat}`;
  }

  build() {
    if (this.isShow) {
      Column() {
        Row() {
          Column() {
            Row() {
              Column() {
                SymbolGlyph($r('sys.symbol.chevron_backward'))
                  .fontSize(24)
                  .fontColor(['rgba(255, 255, 255, 0.9)'])
                  .margin(8)
              }
              .width(40)
              .height(40)
              .borderRadius(20)
              .backgroundColor("rgba(255, 255, 255, 0.1)")
              .onClick(() => {
                this.onBackClick()
              })
            }
          }
          .alignItems(HorizontalAlign.Start)
          .layoutWeight(1)

          Column() {
            Row() {
              Column() {
                SymbolGlyph($r(this.getSpeedIcon()))
                  .fontSize(24)
                  .fontColor(['rgba(255, 255, 255, 0.9)'])
                  .margin(8)
              }
              .width(40)
              .height(40)
              .borderRadius(20)
              .backgroundColor("rgba(255, 255, 255, 0.1)")
              .onClick(() => {
                this.onSpeedButtonClickCallback()
              })

              Column() {
                SymbolGlyph($r('sys.symbol.dot_grid_2x2'))
                  .fontSize(24)
                  .fontColor(['rgba(255, 255, 255, 0.9)'])
                  .margin(8)
              }
              .width(40)
              .height(40)
              .borderRadius(20)
              .margin({
                left: 8
              })
              .backgroundColor("rgba(255, 255, 255, 0.1)")
              .bindSheet($$this.moreButtonsIsShow, MoreButtonModal({
                speed: this.speed,
                onSpeedClick: this.onMoreSpeedClickCallback
              }), {
                title: {
                  title: '更多'
                },
                height: 220,
				onWillDismiss: (action: DismissSheetAction) => {
				  this.onMoreButtonHide()
				  action.dismiss()
				},
				onWillSpringBackWhenDismiss: () => {}
              } as SheetOptions)
              .onClick(() => {
                this.moreButtonsIsShow = true
				this.onMoreButtonShow()
              })
            }
          }
        }
        .width("100%")
      }
      .width("100%")
      .height(56)
      .position({
        top: 0
      })
      .padding({
        top: 8, right: 16, bottom: 8, left: 8
      })
    }
  }
}

@Builder
function MoreButtonModal($$: MoreButtonParams) {
  Column() {
    Row() {
      Column() {
        SymbolGlyph($r('sys.symbol.speed_multiple'))
          .fontSize(24)
          .fontColor(['rgba(0, 0, 0, 0.9)'])
      }
      Column() {
        Text('倍速')
          .fontSize(16)
      }
      .margin({
        right: 24,
        left: 9
      })
      Column() {
        Flex({
          direction: FlexDirection.Row,
          justifyContent: FlexAlign.SpaceBetween
        }) {
          ForEach(Array.from(speedIcons.keys()), (item: number) => {
            Text(numberFormat(item).toString())
              .fontSize(14)
              .fontColor(item === $$.speed ? 'rgb(255, 117, 0)' : 'rgba(0, 0, 0, 0.9)')
              .maxLines(1)
              .padding(5)
              .onClick(() => {
                $$.onSpeedClick(item)
              })
          })
        }
        .width('100%')
        .margin({
          right: 5
        })
      }
      .layoutWeight(1)
    }
    .width('100%')
    .padding({
      top: 12, bottom: 12
    })
  }
  .height('100%')
  .width('100%')
  .padding(16)
}
