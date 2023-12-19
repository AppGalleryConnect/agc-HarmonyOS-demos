import router from '@ohos.router';
import cloud, { AuthUser } from '@hw-agconnect/cloud';


@Entry
@Component
struct Index {
  @StorageLink('user') currentUser: AuthUser | null | undefined = null
  @State createTime: string = ''
  @State lastSignInTime: string = ''

  aboutToAppear() {
    cloud.auth().getCurrentUser().then((user: AuthUser | null) => {
      if (user) {
        this.currentUser = user
        AppStorage.Set<AuthUser>('user', this.currentUser);
      }
    })
  }

  @Builder
  homeButton(title: Resource, description: Resource, routeName: string) {
    Button({ type: ButtonType.Normal, stateEffect: true }) {
      Column() {
        Text(title)
          .fontSize($r('app.float.body_font_size'))
          .margin({ bottom: 5, top: 5 })
          .fontWeight(FontWeight.Bold)
        Text(description)
          .textAlign(TextAlign.Center)
          .fontSize($r('app.float.body_font_size'))
          .margin({ bottom: 5 })
      }
      .width('100%')
    }
    .borderRadius(4)
    .margin(10)
    .borderWidth(1)
    .borderColor($r('app.color.body_color'))
    .fontColor($r('app.color.body_color'))
    .backgroundColor($r('app.color.start_window_background'))
    .width('96%')
    .onClick(async () => {
      let params: Record<string, Resource> = { 'title': title }
      router.pushUrl({ url: routeName, params });
    })
  }

  build() {
    Flex({ direction: FlexDirection.Column, justifyContent: FlexAlign.Center }) {
      Text($r('app.string.main_page_title'))
        .width('100%')
        .fontSize($r('app.float.main_title_font_size'))
        .textAlign(TextAlign.Center)
        .fontColor($r('app.color.body_color'))
        .margin({ bottom: 10 })
      this.homeButton($r('app.string.auth_service_title'), $r('app.string.auth_service_description'), 'pages/Auth')
      this.homeButton($r('app.string.cloud_function_title'), $r('app.string.cloud_function_description_main'), 'pages/CloudFunction')
      this.homeButton($r('app.string.cloudDB_label'), $r('app.string.cloud_db_description_main'), "pages/CloudDb/CloudDb",)
      this.homeButton($r('app.string.cloudStorage_label'), $r('app.string.cloud_storage_description_main'), 'pages/CloudStorage')
    }
    .height('100%')
    .width('100%')
    .padding(10)
    .backgroundColor($r('app.color.start_window_background'))
  }
}