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

import mediaLibrary from '@ohos.multimedia.mediaLibrary';
import { Constants } from '../../common/Constants';

@CustomDialog
export default struct ImagePicker {
  @Link imageDatas: Array<mediaLibrary.FileAsset>;
  private selected: number = 0;
  public controller: CustomDialogController;
  @Link index: number;

  aboutToAppear() {
    this.selected = this.index;
  }

  build() {
    Column() {
      List({ space: Constants.LENGTH_5_PX }) {
        ForEach(this.imageDatas, (item, index) => {
          ListItem() {
            if (this.imageDatas.length === 0) {
              Text($r('app.string.no_photo'))
                .fontSize($r('app.float.navigation_font_size'))
                .margin({ bottom: Constants.LENGTH_20_PX })
            } else {
              Stack({ alignContent: Alignment.TopEnd }) {
                Image(item?.uri)
                  .width(Constants.LENGTH_180_PX)
                  .height(Constants.LENGTH_150_PX)
                  .objectFit(ImageFit.Contain)
                  .margin({
                    left: Constants.LENGTH_2_PX,
                    right: Constants.LENGTH_2_PX,
                    top: Constants.LENGTH_5_PX,
                    bottom: Constants.LENGTH_5_PX
                  })
                Radio({ value: 'Radio3', group: 'radioGroup' })
                  .checked(index === this.index ? true : false)
                  .border({ width: 1, color: Color.Blue, radius: 10 })
                  .onChange(() => {
                    this.selected = index;
                  })
              }
            }
          }
        })
      }
      .width(Constants.PERCENT_90)
      .height(Constants.LENGTH_160_PX)
      .listDirection(Axis.Horizontal)

      Row() {
        Button($r('app.string.confirmButton_label'))
          .margin({ right: Constants.PERCENT_25 })
          .onClick(() => {
            this.index = this.selected;
          })

        Button($r('app.string.cancelButton_label'))
          .backgroundColor('red')
          .onClick(() => {
            this.controller.close();
          })
      }
      .margin({ bottom: Constants.LENGTH_10_PX })
    }
    .width(Constants.PERCENT_100)
  }
}