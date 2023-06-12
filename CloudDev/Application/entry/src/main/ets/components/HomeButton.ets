import router from '@ohos.router';

import { AGCAuth } from '../services/Auth';
import { Constants } from '../common/Constants';


@Component
export struct HomeButton {
  public title: Resource;
  public description: Resource;
  public routeName: string;

  onNavButtonClick() {
    if (new AGCAuth(getContext(this)).getCurrentUser() === null) {
      AlertDialog.show({
        title: $r('app.string.login_warning_title'),
        message: $r('app.string.login_warning_message')
      });
    } else {
      router.pushUrl({ url: this.routeName, params: { title: this.title } });
    }
  }

  build() {
    Button({ type: ButtonType.Normal, stateEffect: true }) {
      Column() {
        Text(this.title)
          .fontSize($r('app.float.body_font_size'))
          .margin({ bottom: Constants.LENGTH_5_PX, top: Constants.LENGTH_5_PX })
          .fontWeight(FontWeight.Bold)
        Text(this.description)
          .textAlign(TextAlign.Center)
          .fontSize($r('app.float.body_font_size'))
          .margin({ bottom: Constants.LENGTH_5_PX })
      }
      .width(Constants.PERCENT_100)
    }
    .borderRadius(Constants.BORDER_RADIUS_4_PX)
    .margin(Constants.LENGTH_10_PX)
    .borderWidth(Constants.LENGTH_1_PX)
    .borderColor($r('app.color.body_color'))
    .fontColor($r('app.color.body_color'))
    .backgroundColor($r('app.color.start_window_background'))
    .width(Constants.PERCENT_96)
    .height(Constants.HEIGHT_50VP)
    .onClick(() => this.onNavButtonClick())
  }
}