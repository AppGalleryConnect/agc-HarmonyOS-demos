import { CommonActionButton } from '../components/CommonActionButton';
import { Constants } from '../common/Constants';
import { uuid } from '../services/Function';

@Entry
@Component
export struct CloudFunction {
  @State globalId: string = '';
  @State functionButtonEnabled: boolean = true;
  @State year: string = '';
  getFunctionResult = () => {
    uuid(getContext(this), this.year).then((id) => {
      this.globalId = id;
    }).catch((err) => {
      console.error('error: ', err && err.message);
    });
  }

  build() {
    Column() {
      Navigation()
        .title(this.NavigationTitle())
        .height(Constants.VIEWPORT_50)
        .width(Constants.PERCENT_100)
        .margin({ bottom: Constants.LENGTH_10_PX })
        .titleMode(NavigationTitleMode.Mini)

      TextInput({placeholder:"输入年份"})
        .width(Constants.PERCENT_90)
        .onChange((value: string) => {
          this.year = value;
        })

      Text($r('app.string.cloud_function_description'))
        .width(Constants.PERCENT_90)
        .textAlign(TextAlign.Center)
        .margin({ top: Constants.LENGTH_20_PX, bottom: Constants.LENGTH_20_PX })
        .fontSize($r('app.float.body_font_size'))

      CommonActionButton({
        title: $r('app.string.cloud_function_button_text'),
        backColor: $r('app.color.action_button_background'),
        color: $r('app.color.start_window_background'),
        onAction: this.getFunctionResult,
        isEnabled: $functionButtonEnabled
      })

      Column() {
        Text(this.globalId).fontSize($r('app.float.body_font_size'))
      }
      .width(Constants.PERCENT_90)
      .padding({ top: Constants.LENGTH_20_PX, bottom: Constants.LENGTH_20_PX })
      .margin({ top: Constants.LENGTH_20_PX })
      .backgroundColor($r('app.color.placeholder_background'))
    }.height(Constants.PERCENT_100)
  }

  @Builder NavigationTitle() {
    Column() {
      Text($r('app.string.cloud_function_title')).fontSize($r('app.float.navigation_font_size'))
    }
  }
}