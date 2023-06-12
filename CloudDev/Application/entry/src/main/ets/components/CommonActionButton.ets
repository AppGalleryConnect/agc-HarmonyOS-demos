import {Constants} from '../common/Constants';

@Component
export struct CommonActionButton {
  public title: Resource;
  public color: ResourceColor;
  public backColor: ResourceColor;
  public borderW: number;
  public borderC: ResourceColor;
  public onAction: Function;
  @Link isEnabled: boolean;

  build() {
    Button({ type: ButtonType.Normal }) {
      Text(this.title)
        .fontColor(this.color)
        .fontSize($r('app.float.body_font_size'))
        .margin({ top: Constants.LENGTH_5_PX, bottom: Constants.LENGTH_5_PX })
    }
    .width(Constants.PERCENT_90)
    .backgroundColor(this.backColor)
    .borderRadius(Constants.BORDER_RADIUS_4_PX)
    .borderWidth(this.borderW)
    .borderColor(this.borderC)
    .enabled(this.isEnabled)
    .alignSelf(ItemAlign.Center)
    .onClick(() => {
      this.onAction();
    })
  }
}