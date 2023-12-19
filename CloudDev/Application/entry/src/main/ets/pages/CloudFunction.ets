import cloud, { FunctionResult } from '@hw-agconnect/cloud';

@Entry
@Component
struct CloudFunction {
  @State globalId: string = '';

  build() {
    Column() {
      Navigation()
        .title(this.NavigationTitle())
        .height('50vp')
        .width('100%')
        .margin({ bottom: 10 })
        .titleMode(NavigationTitleMode.Mini)

      Text($r('app.string.cloud_function_description'))
        .width('90%')
        .textAlign(TextAlign.Center)
        .margin({ top: 20, bottom: 20 })
        .fontSize($r('app.float.body_font_size'))
      Button({ type: ButtonType.Normal }) {
        Text($r('app.string.cloud_function_button_text'))
          .fontColor($r('app.color.white'))
          .margin({ top: 5, bottom: 5 })
      }
      .width('90%')
      .backgroundColor($r('app.color.login_button'))
      .borderRadius('8vp')
      .height('30vp')
      .margin({ top: 10 })
      .onClick(() => {
        cloud.callFunction({
          name: 'id-generator',
          version: '$latest'
        }).then((res: FunctionResult) => {
          this.globalId = res.getValue()?.uuid
        }).catch((err: Object) => {
        })
      })

      Column() {
        Text(this.globalId).fontSize($r('app.float.body_font_size'))
      }
      .width('90%')
      .padding({ top: 20, bottom: 20 })
      .margin({ top: 20 })
      .backgroundColor($r('app.color.placeholder_background'))
    }.height('100%')
  }

  @Builder
  NavigationTitle() {
    Column() {
      Text($r('app.string.cloud_function_title')).fontSize($r('app.float.navigation_font_size'))
    }
  }
}