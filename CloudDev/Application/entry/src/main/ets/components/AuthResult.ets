import { CommonActionButton } from '../components/CommonActionButton';
import '@hw-agconnect/auth-ohos';
import { Constants } from '../common/Constants';
import { AGCAuth, AgUser } from '../services/Auth';

@Component
export struct AuthResultView {
  public onLoginPressed: Function;
  @StorageLink('user') currentUser: AgUser = AppStorage.Get<AgUser>('user');
  @State isEditingDisplayName: boolean = false;
  @State displayNameText: string = ''
  @State canLogOut: boolean = true;
  @State loginButtonEnableState: boolean = true;

  aboutToAppear() {
    this.currentUser = AppStorage.Get<AgUser>('user');
  }

  async logout() {
    this.canLogOut = false
    await new AGCAuth(getContext(this)).logout().then((res) => {
      if (res) {
        AppStorage.Set<AgUser>('user', new AgUser());
        this.canLogOut = true
      }
    })
  }

  async deleteUser() {
    this.canLogOut = false
    await new AGCAuth(getContext(this)).deleteUser().then((res) => {
      if (res) {
        AppStorage.Set<AgUser>('user', new AgUser());
        this.canLogOut = true
      }
    })
  }

  build() {
    Flex({ direction: FlexDirection.Column, justifyContent: FlexAlign.Start, alignItems: ItemAlign.Center }) {
      if (!this.currentUser?.getUid()) {
        CommonActionButton({
          title: $r('app.string.auth_service_login_button_text'),
          color: $r('app.color.start_window_background'),
          backColor: $r('app.color.action_button_background'),
          onAction: () => {
            this.onLoginPressed();
          },
          isEnabled: $loginButtonEnableState
        })
      } else {
        Column() {
          if (this.currentUser?.getPhotoUrl() !== null && this.currentUser?.getPhotoUrl() !== undefined && this.currentUser?.getPhotoUrl() !== '') {
            Image(this.currentUser?.getPhotoUrl().toString())
              .width(Constants.LENGTH_70_PX)
              .height(Constants.LENGTH_70_PX)
          } else {
            Image($r("app.media.user_dark")).width(Constants.LENGTH_70_PX).height(Constants.LENGTH_70_PX)
          }

          Flex({ justifyContent: FlexAlign.SpaceBetween }) {
            Text($r('app.string.auth_result_mobile_number')).fontSize($r('app.float.body_font_size'))
            Text(this.currentUser?.getPhone()?.toString()).fontSize($r('app.float.body_font_size'))
          }
          .margin({ top: Constants.LENGTH_10_PX, bottom: Constants.LENGTH_10_PX })

          Divider().color($r('app.color.placeholder_background'))

          Flex({ justifyContent: FlexAlign.SpaceBetween }) {
            Text($r('app.string.auth_result_registered'))
              .fontSize($r('app.float.body_font_size'))
            if (this.currentUser?.getRegisterDate()) {
              Text(this.formatDate(this.currentUser?.getRegisterDate()))
                .fontSize($r('app.float.body_font_size'))
            }
          }
          .margin({ top: Constants.LENGTH_10_PX, bottom: Constants.LENGTH_10_PX })

          Divider().color($r('app.color.placeholder_background'))

          Flex({ justifyContent: FlexAlign.SpaceBetween }) {
            Text($r('app.string.auth_result_last_login'))
              .fontSize($r('app.float.body_font_size'))
            if (this.currentUser?.getLastLogin()) {
              Text(this.formatDate(this.currentUser?.getLastLogin()))
                .fontSize($r('app.float.body_font_size'))
            }
          }
          .margin({ top: Constants.LENGTH_10_PX, bottom: Constants.LENGTH_10_PX })
        }
        .margin({ left: Constants.LENGTH_30_PX, right: Constants.LENGTH_30_PX, top: Constants.LENGTH_20_PX })
        .padding(Constants.LENGTH_15_PX)
        .border({ width: Constants.LENGTH_1_PX, color: $r('app.color.placeholder_background') })

        Column() {
          CommonActionButton({
            title: $r('app.string.auth_service_delete_user_button_text'),
            color: $r('app.color.black'),
            backColor: $r('app.color.start_window_background'),
            borderW: Constants.LENGTH_1_PX,
            borderC: $r('app.color.black'),
            onAction: () => {
              this.deleteUser();
            },
            isEnabled: $canLogOut
          }).margin({ top: Constants.LENGTH_10_PX })

          CommonActionButton({
            title: $r('app.string.auth_service_logout_button_text'),
            color: $r('app.color.black'),
            backColor: $r('app.color.start_window_background'),
            borderW: Constants.LENGTH_1_PX,
            borderC: $r('app.color.black'),
            onAction: () => {
              this.logout();
            },
            isEnabled: $canLogOut
          }).margin({ top: Constants.LENGTH_10_PX })
        }
      }
    }.height(Constants.PERCENT_90)
  }

  formatDate(date: String): string {
    if (date) {
      let d = new Date(parseInt(date.toString()));

      let ymd = d
        .toISOString()
        .replace('-', '/')
        .split('T')[0]
        .replace('-', '/');

      let hours = d.getHours().toString().length < 2 ? "0" + d.getHours() : d.getHours();

      let minutes = d.getMinutes().toString().length < 2 ? "0" + d.getMinutes() : d.getMinutes();

      let seconds = d.getSeconds().toString().length < 2 ? "0" + d.getSeconds() : d.getSeconds();

      let hm = hours + ':' + minutes + ':' + seconds;

      return ymd + ' ' + hm;
    }
  }
}