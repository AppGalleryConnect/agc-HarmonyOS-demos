import { Log } from '../common/Log';
import { mockData } from '../common/CountryViewModel';
import { AGCAuth, AgUser } from '../services/Auth';
import { Constants } from '../common/Constants';

const TAG = "[AuthPage]";

@CustomDialog
export struct AuthDialog {
  agcAuth: AGCAuth;
  controller: CustomDialogController;
  @State countryCode: string = '+86';
  @State phoneNumber: string = '';
  @State verificationCode: string = '';
  @State intervalId: number = -123456;
  @State timer: number = 60;
  @State canLogin: boolean = true;

  aboutToAppear() {
    this.agcAuth = new AGCAuth(getContext(this));
  }

  canAuthorize(): boolean {
    return this.countryCode !== '' && this.phoneNumber !== '' && this.verificationCode !== '';
  }

  canGetCode(): boolean {
    return this.phoneNumber.length > 5 && this.phoneNumber.length <= 20;
  }

  startTimer(): void {
    this.timer = 60;
    this.intervalId = setInterval(() => {
      this.timer--;
      if (this.timer === 0) {
        clearInterval(this.intervalId);
        this.timer = 60;
      }
    }, 1000);
  }

  async onGetCodeButtonClicked() {
    if (this.phoneNumber !== '' && this.timer === 60) {
      this.startTimer();
      this.agcAuth.requestPhoneVerifyCode(this.countryCode, this.phoneNumber);
    }
  }

  onAuthButtonClicked() {
    this.canLogin = false;
    this.agcAuth.login(this.countryCode, this.phoneNumber, this.verificationCode).then(user => {
      AppStorage.Set<AgUser>('user', user);
      Log.info(TAG, "Logged in succesfully.");
      this.canLogin = true;
      this.controller?.close();
    }).catch((err) => {
      this.canLogin = true;
      Log.error(TAG, "Logged in failed " + JSON.stringify(err));
      AlertDialog.show({
        title: $r('app.string.common_prompt'),
        message: $r('app.string.common_login_fail'),
      });
    });
  }

  build() {
    Flex({ direction: FlexDirection.Column, alignItems: ItemAlign.Center, justifyContent: FlexAlign.Center }) {
      Text($r('app.string.auth_dialog_title'))
        .fontSize($r('app.float.navigation_font_size'))
        .margin({ bottom: Constants.LENGTH_20_PX })

      Flex({ direction: FlexDirection.Row }) {

        Select(mockData)
          .font({ size: $r('app.float.body_font_size') })
          .selectedOptionFont({ size: $r('app.float.body_font_size') })
          .optionFont({ size: $r('app.float.body_font_size') })
          .value(this.countryCode)
          .layoutWeight(Constants.LENGTH_1_PX)
          .backgroundColor($r('app.color.placeholder_background'))
          .borderRadius(Constants.BORDER_RADIUS_4_PX)
          .height(Constants.HEIGHT_40)
          .onSelect((_, val) => {
            this.countryCode = val.substring(0, val.length - 4);
          })


        TextInput({ placeholder: $r('app.string.auth_dialog_number_placeholder') })
          .layoutWeight(Constants.LENGTH_3_PX)
          .margin({ left: Constants.LENGTH_5_PX })
          .borderRadius(Constants.BORDER_RADIUS_4_PX)
          .maxLength(Constants.LENGTH_20_PX)
          .height(Constants.HEIGHT_40)
          .enabled(this.timer === 60)
          .onChange((val) => {
            this.phoneNumber = val;
          })

      }
      .margin({ bottom: Constants.LENGTH_20_PX })

      Flex({ direction: FlexDirection.Row }) {
        TextInput({ placeholder: $r('app.string.auth_dialog_code_placeholder'), text: this.verificationCode })
          .layoutWeight(Constants.LENGTH_3_PX)
          .margin({ right: Constants.LENGTH_5_PX })
          .borderRadius(Constants.BORDER_RADIUS_4_PX)
          .maxLength(Constants.LENGTH_6_PX)
          .height(Constants.HEIGHT_40)
          .onChange((val) => {
            this.verificationCode = val;
          })

        Button(this.timer === 60 ? $r('app.string.auth_dialog_get_code_button_text') : this.timer.toString(), {
          type: ButtonType.Normal
        })
          .backgroundColor($r('app.color.start_window_background'))
          .layoutWeight(Constants.LENGTH_2_PX)
          .borderColor($r('app.color.action_button_background'))
          .borderWidth(Constants.LENGTH_1_PX)
          .fontColor($r('app.color.action_button_background'))
          .borderRadius(Constants.BORDER_RADIUS_4_PX)
          .margin({ left: Constants.LENGTH_5_PX })
          .height(Constants.HEIGHT_40)
          .enabled(this.canGetCode() && this.timer === 60)
          .onClick(() => this.onGetCodeButtonClicked())
      }
      .margin({ bottom: Constants.LENGTH_20_PX })


      Button($r('app.string.auth_dialog_auth_button_text'), { type: ButtonType.Normal })
        .width(Constants.PERCENT_100)
        .borderRadius(Constants.BORDER_RADIUS_4_PX)
        .backgroundColor($r('app.color.action_button_background'))
        .enabled(this.canAuthorize() && this.verificationCode.length > 5 && this.canLogin)
        .opacity(this.canLogin ? 1 : 0.5)
        .height(Constants.HEIGHT_40)
        .onClick(() => this.onAuthButtonClicked())

    }
    .height(Constants.PERCENT_50)
    .padding({ right: Constants.LENGTH_20_PX, left: Constants.LENGTH_20_PX })
  }
}