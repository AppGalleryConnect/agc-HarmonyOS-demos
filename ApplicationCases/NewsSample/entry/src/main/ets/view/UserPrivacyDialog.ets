/*
 * Copyright (c) 2022 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { FONT_WEIGHT_400, FULL_WIDTH, OPACITY_4, OPACITY_6 } from '../common/constant/CommonConstant';


@Extend(Text) function descStyle () {
  .fontSize($r('app.float.default_14'))
  .fontWeight(FONT_WEIGHT_400)
  .fontFamily($r('app.string.HarmonyHeiTi'))
  .fontColor($r(`app.element.color.titleColor`))
  .width(FULL_WIDTH)
  .lineHeight($r('app.float.default_20'))
  .margin({ top: $r('app.float.default_8') })
}

@CustomDialog
export default struct UserPrivacyDialog {
  controller: CustomDialogController = new CustomDialogController({ builder: '' });;
  cancel: Function =  () => {};
  confirm: Function  = () => {};

  build() {
    Column() {
      Text($r('app.string.privacy_title'))
        .descStyle()
      Text($r('app.string.privacy_desc'))
        .descStyle()
        .opacity(OPACITY_6)
      Row() {
        Button($r('app.string.cancel'))
          .backgroundColor(Color.White)
          .fontColor($r('app.color.blueColor'))
          .onClick(() => {
            this.controller.close();
            this.cancel();
          })
        Divider()
          .vertical(true)
          .height($r('app.float.default_22'))
          .opacity(OPACITY_4)
        Button($r('app.string.sure'))
          .backgroundColor(Color.White)
          .fontColor($r('app.color.blueColor'))
          .onClick(() => {
            this.controller.close();
            this.confirm();
          })
      }
      .width(FULL_WIDTH)
      .margin({ top: $r('app.float.default_22') })
      .justifyContent(FlexAlign.SpaceEvenly)
    }
    .padding($r('app.float.default_16'))
  }
}