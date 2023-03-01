import { Logger } from "@hw-agconnect/base-ohos"
import { EmailAuthProvider, VerifyCodeAction, EmailUserBuilder, VerifyCodeSettingBuilder,
  PhoneAuthProvider, AGConnectAuth
} from "@hw-agconnect/auth-ohos"

const TAG = "AGC_AUTH_DEMO";

let auth: AGConnectAuth = globalThis.auth;


let email:string = "your email address";
let password:string = "your password";

@Entry
@Component
struct EmailPage {
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

        Button("requestEmailVerifyCode").width('90%').margin({ top: 20 })
          .onClick(() => {
            let verifyCodeSettings = new VerifyCodeSettingBuilder()
              .setAction(VerifyCodeAction.REGISTER_LOGIN)
              .setLang('zh_CN')
              .setSendInterval(60)
              .build();

            auth.requestEmailVerifyCode(email, verifyCodeSettings)
              .then(verifyCodeResult => {
              })
              .catch(error => {
              })
          })

        Button("createEmailUser").width('90%').margin({ top: 20 })
          .onClick(() => {
            let emailUser = new EmailUserBuilder()
              .setEmail(email)
              .setPassword(password)
              .setVerifyCode(this.verifyCode)
              .build();
            auth.createEmailUser(emailUser)
              .then(result => {
              });
          })

        Button("signInEmail").width('90%').margin({ top: 20 })
          .onClick(() => {
            let credential = EmailAuthProvider.credentialWithPassword(email, password);
            auth.signIn(credential).then(signInResult => {
              Logger.info(TAG, "signInEmail success " + signInResult.getUser().getUid());
            });
          })

        Button("linkPhone").width('90%').margin({ top: 20 })
          .onClick(() => {
            auth.getCurrentUser().then(user => {
              if (user) {
                let credential = PhoneAuthProvider.credentialWithVerifyCode("86", "13812345678", this.verifyCode);
                user.link(credential).then(signInResult => {
                  Logger.info(TAG, "link success " + signInResult.getUser().getUid());
                });
              }
            });
          })

      }
      .width('100%')
    }
    .height('100%')
  }
}