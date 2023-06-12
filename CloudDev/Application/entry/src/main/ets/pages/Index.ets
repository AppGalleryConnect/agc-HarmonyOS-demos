import { HomeButton } from '../components/HomeButton';
import { Constants } from '../common/Constants';
import { AGCAuth, AgUser } from '../services/Auth';
import { AGConnectUser } from '@hw-agconnect/auth-ohos';

@Entry
@Component
struct Index {
  @StorageLink('user') currentUser: AgUser = new AgUser();
  @State agcUser: AGConnectUser = null;

  async aboutToAppear() {
    // 杀掉进程重启情况
    this.agcUser = await new AGCAuth(getContext(this)).getCurrentUser();
    if (this.agcUser) {
      let agcUserExtra = await this.agcUser.getUserExtra();
      this.currentUser = new AgUser(
      this.agcUser.getUid(),
      this.agcUser.getPhotoUrl(),
      this.agcUser.getPhone(),
      this.agcUser.getDisplayName(),
      agcUserExtra.getCreateTime(),
      agcUserExtra.getLastSignInTime());
      AppStorage.Set<AgUser>('user', this.currentUser);
    }
  }

  build() {
    Flex({ direction: FlexDirection.Column, justifyContent: FlexAlign.Center }) {
      Text($r('app.string.main_page_title'))
        .width(Constants.PERCENT_100)
        .fontSize($r('app.float.main_title_font_size'))
        .textAlign(TextAlign.Center)
        .fontColor($r('app.color.body_color'))
        .margin({ bottom: Constants.LENGTH_10_PX })

      HomeButton({
        title: $r('app.string.auth_service_title'),
        description: $r('app.string.auth_service_description'),
        routeName: 'pages/AuthService'
      })

      HomeButton({
        title: $r('app.string.cloud_function_title'),
        description: $r('app.string.cloud_function_description_main'),
        routeName: "pages/Functions"
      })

      HomeButton({
        title: $r('app.string.cloudStorage_label'),
        description: $r('app.string.cloud_storage_description_main'),
        routeName: Constants.STORAGE_ROUTE
      })
    }
    .height(Constants.PERCENT_100)
    .width(Constants.PERCENT_100)
    .padding(Constants.LENGTH_10_PX)
    .backgroundColor($r('app.color.start_window_background'))
  }
}