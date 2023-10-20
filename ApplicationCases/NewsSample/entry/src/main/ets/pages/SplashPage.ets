import common from '@ohos.app.ability.common';
import UserPrivacyDialog from '../view/UserPrivacyDialog';
import data_preferences from '@ohos.data.preferences';
import router from '@ohos.router';
import { DELAY_TIME } from '../common/constant/CommonConstant';
import Logger from '../common/utils/Logger';

const H_STORE: string = 'NewsStore';
const IS_PRIVACY: string = 'isPrivacy';

@Entry
@Component
struct SplashPage {
  context: common.UIAbilityContext = getContext(this) as common.UIAbilityContext;
  dialogController: CustomDialogController = new CustomDialogController({
    builder: UserPrivacyDialog(
      {
        cancel: () => { this.exitApp(this) },
        confirm: () => { this.onConfirm(this) }
      }),
    cancel: () => { this.exitApp(this) },
    autoCancel: false,
    alignment: DialogAlignment.Center,
  });

  onConfirm(that) {
    let preferences = data_preferences.getPreferences(that.context, H_STORE);
    preferences.then((res) => {
      res.put(IS_PRIVACY, true).then(() => {
        res.flush();
        Logger.info('SplashPage','isPrivacy is put success');
      }).catch((err: Error) => {
        Logger.info('SplashPage','isPrivacy put failed. Cause:' + err);
      });
    })
    that.jumpMainPage();
  }

  exitApp(that) {
    that.context.terminateSelf();
  }

  aboutToAppear() {
    let preferences = data_preferences.getPreferences(this.context, H_STORE);
    preferences.then((res) => {
      res.get(IS_PRIVACY, false).then((isPrivate) => {
        if (isPrivate === true) {//已同意协议，跳转广告页
          this.jumpMainPage();
        } else {//未同意协议，打开协议弹窗页面
          this.dialogController.open();
        }
      });
    });
  }

  jumpMainPage() {
    setTimeout(() => {
      router.replaceUrl({ url: 'pages/Index' });
    }, DELAY_TIME);
  }

  aboutToDisappear() {
    clearTimeout();
  }

  build() {
    Row() {
      Column() {
        Image($r('app.media.icon_news'))
          .width($r('app.float.default_120'))
      }
      .width('100%')
      .alignItems(HorizontalAlign.Center)
      .margin({ bottom: $r('app.float.default_120') });
    }
    .height('100%')
    .alignItems(VerticalAlign.Center)
    .backgroundColor($r('app.color.color_background'))
    .backgroundImageSize({ width: '100%', height: '100%' })
  }
}