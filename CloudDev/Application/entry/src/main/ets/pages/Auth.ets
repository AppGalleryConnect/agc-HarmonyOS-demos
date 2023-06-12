import { AuthDialog } from '../components/AuthDialog';
import { AuthResultView } from '../components/AuthResult';
import { Constants } from '../common/Constants';

@Entry
@Component
struct AuthScreen {
  dialogController: CustomDialogController = new CustomDialogController({
    builder: AuthDialog(),
    autoCancel: true,
  });

  build() {
    Column() {
      Navigation()
        .title(this.NavigationTitle())
        .height(Constants.VIEWPORT_50)
        .width(Constants.PERCENT_100)
        .margin({ bottom: Constants.LENGTH_10_PX })
        .titleMode(NavigationTitleMode.Mini)
      AuthResultView({
        onLoginPressed: () => {
          this.dialogController.open();
        }
      })
    }
  }

  @Builder NavigationTitle() {
    Column() {
      Text($r('app.string.auth_service_title')).fontSize($r('app.float.navigation_font_size'))
    }
  }
}