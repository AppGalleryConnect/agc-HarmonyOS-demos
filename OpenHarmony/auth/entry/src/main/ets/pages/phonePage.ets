import { Logger } from "@hw-agconnect/base-ohos"
import "@hw-agconnect/auth-ohos"
import { EmailAuthProvider, VerifyCodeAction, VerifyCodeSettingBuilder, PhoneUserBuilder,
  PhoneAuthProvider, AGConnectAuth, AGConnectAuthCredentialProvider
} from "@hw-agconnect/auth-ohos"

const TAG = "AGC_AUTH_DEMO";

let auth: AGConnectAuth = globalThis.auth;
let phone:string = "your email address";
let password:string = "your password";

@Entry
@Component
struct PhonePage {
  @State verifyCode: string = ''

  build() {
    Row() {
      Column() {
        TextInput({ placeholder: 'input your verifyCode' })
          .width('90%')
          .margin({ top: 20 })
          .onChange((value: string) => {
            this.verifyCode = value;
          })

        Button("requestPhoneVerifyCode").width('90%').margin({ top: 20 })
          .onClick(() => {
            let verifyCodeSettings = new VerifyCodeSettingBuilder()
              .setAction(VerifyCodeAction.REGISTER_LOGIN)
              .setLang('zh_CN')
              .setSendInterval(60)
              .build();

            auth.requestPhoneVerifyCode("86", phone, verifyCodeSettings)
              .then(verifyCodeResult => {
              Logger.info(TAG, "requestPhoneVerifyCode success " + JSON.stringify(verifyCodeResult));
            }).catch(error => {
              Logger.error(TAG, "requestPhoneVerifyCode error " + JSON.stringify(error));
            })
          })

        Button("createPhoneUser").width('90%').margin({ top: 20 })
          .onClick(() => {
          let user = new PhoneUserBuilder()
            .setCountryCode("86")
            .setPhoneNumber(phone)
            .setPassword(password)
            .setVerifyCode( this.verifyCode)
            .build();
          auth.createPhoneUser(user)
            .then(result => {
              Logger.info(TAG, "createPhoneUser success getUid:" + result.getUser().getUid());
            })
        })

        Button("signInPhone").width('90%').margin({ top: 20 })
          .onClick(() => {
            let credential = PhoneAuthProvider.credentialWithPassword("86",phone, password);
            auth.signIn(credential).then(signInResult => {
              Logger.info(TAG, "signInPhone success " + signInResult.getUser().getUid());
            });
          })

        Button("linkEmail").width('90%').margin({ top: 20 })
          .onClick(() => {
            auth.getCurrentUser().then(user => {
              if (user) {
                let credential = EmailAuthProvider.credentialWithVerifyCode("your email address",  this.verifyCode);
                user.link(credential).then(signInResult => {
                  Logger.info(TAG, "link success " + signInResult.getUser().getUid());
                })
              }
            });
          })

        Button("updatePasswordByPhone").width('90%').margin({ top: 20 })
          .onClick(() => {
            auth.getCurrentUser().then(user => {
              if (user) {
                user.updatePassword("new password", this.verifyCode,AGConnectAuthCredentialProvider.Phone_Provider).then(() => {
                  Logger.info(TAG, "updatePassword success " );
                })
              }
            });
          })

      }
      .width('100%')
    }
    .height('100%')
  }
}