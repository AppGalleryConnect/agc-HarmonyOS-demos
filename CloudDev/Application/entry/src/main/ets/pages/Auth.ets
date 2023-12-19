import cloud, { AuthUser } from '@hw-agconnect/cloud'
import { Login, AuthMode } from "@hw-agconnect/auth-component";

@Styles
function buttonFancy() {
  .width('90%')
  .backgroundColor($r('app.color.login_button'))
  .borderRadius('8vp')
  .height('30vp')
}

@Entry
@Component
struct AuthScreen {
  @StorageLink('user') currentUser: AuthUser | null | undefined = null
  @State isShowButton: boolean = true;
  @State creatTime: string = '';
  @State lastLogin: string = ''
  @State userId: string = ''

  @Builder
  NavigationTitle() {
    Column() {
      Text($r('app.string.auth_service_title')).fontSize($r('app.float.navigation_font_size'))
    }
  }

  async aboutToAppear() {
    this.currentUser = AppStorage.Get<AuthUser>('user');
    if (this.currentUser) {
      let agcUserExtra = await this.currentUser.getUserExtra();
      this.creatTime = agcUserExtra.getCreateTime()
      this.lastLogin = agcUserExtra.getLastSignInTime()
    }
  }

  async logout() {
    cloud.auth().signOut().then(() => {
      this.isShowButton = true
      this.currentUser = null
    }).catch((error: Object) => {
    });
  }

  deleteUser() {
    cloud.auth().deleteUser().then(() => {
      this.isShowButton = true
      this.currentUser = null
      this.creatTime = ''
      this.lastLogin = ''
    }).catch((err: Object) => {
    })
  }

  formatDate(date: string): string {
    if (!date) {
      return ''
    }
    let d = new Date(Number.parseInt(date.toString()));
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

  build() {
    Column() {
      Navigation()
        .title(this.NavigationTitle())
        .height('50vp')
        .width('100%')
        .margin({ bottom: 10 })
        .titleMode(NavigationTitleMode.Mini)
      Flex({ direction: FlexDirection.Column, justifyContent: FlexAlign.Start, alignItems: ItemAlign.Center }) {
        Column() {
          if (this.currentUser !== null && this.currentUser !== undefined) {
            Column() {
              if (this.currentUser?.getPhotoUrl() !== null && this.currentUser?.getPhotoUrl() !== undefined && this.currentUser?.getPhotoUrl() !== '') {
                Image(this.currentUser?.getPhotoUrl().toString())
                  .width(70)
                  .height(70)
              } else {
                Image($r("app.media.user_dark")).width(70).height(70)
              }
              Flex({ justifyContent: FlexAlign.SpaceBetween }) {
                Text($r('app.string.auth_result_mobile_number')).fontSize($r('app.float.body_font_size'))
                Text(this.currentUser?.getPhone()?.toString()).fontSize($r('app.float.body_font_size'))
              }
              .margin({ top: 10, bottom: 10 })

              Divider().color($r('app.color.placeholder_background'))
              Flex({ justifyContent: FlexAlign.SpaceBetween }) {
                Text($r('app.string.auth_result_registered'))
                  .fontSize($r('app.float.body_font_size'))
                if (this.creatTime) {
                  Text(this.formatDate(this.creatTime))
                    .fontSize($r('app.float.body_font_size'))
                }
              }
              .margin({ top: 10, bottom: 10 })

              Divider().color($r('app.color.placeholder_background'))
              Flex({ justifyContent: FlexAlign.SpaceBetween }) {
                Text($r('app.string.auth_result_last_login'))
                  .fontSize($r('app.float.body_font_size'))
                if (this.lastLogin) {
                  Text(this.formatDate(this.lastLogin))
                    .fontSize($r('app.float.body_font_size'))
                }
              }
              .margin({ top: 10, bottom: 10 })
            }
            .margin({ left: 30, right: 30, top: 20 })
            .padding(15)
            .border({ width: 1, color: $r('app.color.placeholder_background') })

            Column() {
              Button({ type: ButtonType.Normal }) {
                Text($r('app.string.auth_service_delete_user_button_text'))
                  .fontColor($r('app.color.white'))
                  .margin({ top: 5, bottom: 5 })
              }
              .buttonFancy()
              .margin({ top: 10 })
              .onClick(() => {
                this.deleteUser()
              })

              Button({ type: ButtonType.Normal }) {
                Text($r('app.string.auth_service_logout_button_text'))
                  .fontColor($r('app.color.white'))
                  .margin({ top: 5, bottom: 5 })
              }
              .buttonFancy()
              .margin({ top: 10 })
              .onClick(() => {
                this.logout();
              })
            }
          } else {
            Column() {
              Login({
                modes: [AuthMode.PHONE_VERIFY_CODE],
                onSuccess: async (user: AuthUser) => {
                  this.isShowButton = false
                  AppStorage.Set<AuthUser>('user', user);
                  this.currentUser=AppStorage.Get<AuthUser>('user');
                  if (this.currentUser != null && this.currentUser != undefined) {
                    let agcUserExtra = await this.currentUser.getUserExtra();
                    this.creatTime = agcUserExtra.getCreateTime()
                    this.lastLogin = agcUserExtra.getLastSignInTime()
                  }
                }
              }) {
                if (this.isShowButton) {
                  Button({ type: ButtonType.Normal }) {
                    Text($r('app.string.auth_service_login_button_text'))
                      .fontColor($r('app.color.white'))
                  }
                  .buttonFancy()
                }
              }
            }
          }

        }.height('90%')
        .borderRadius('4vp')
      }
      .height('90%')
    }
  }
}