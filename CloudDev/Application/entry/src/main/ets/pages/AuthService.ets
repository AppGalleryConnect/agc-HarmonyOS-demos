import {Login, AuthMode} from "@hw-agconnect/auth-component"
import router from '@ohos.router';

@Entry
@Component
struct AuthService {
  build() {
    Column(){
      Text('点击按钮登录')
        .fontSize(30)
        .padding({bottom:50})

      Login({
        modes: [AuthMode.PASSWORD, AuthMode.PHONE_VERIFY_CODE, AuthMode.MAIL_VERIFY_CODE],
        onSuccess: (user) => {
          router.pushUrl({ url: "pages/Functions", params: { user: user } });
        },
        onError: (err) => {
          console.error('error: ', err && err.message);
        }
      })
      {
        Button('login')
          .width('90%')
          .align(Alignment.Center);
      }
    }.width('100%')
    .height('100%')
    .justifyContent(FlexAlign.Center);
  }
}