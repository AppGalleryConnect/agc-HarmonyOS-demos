/*
* Copyright 2022. Huawei Technologies Co., Ltd. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import LoadingDialog from './LoadingDialog';
import prompt from '@ohos.prompt';
import { CloudDBService } from '../Service/CloudDBService';

@Component
export default struct UserLogin {
  @Link currentIndexRef: number
  @Link BuildRef: Array<Object>
  @State username: string = ''
  @State password: string = ''
  controller: CustomDialogController = new CustomDialogController({
    builder: LoadingDialog(),
    autoCancel: false,
    customStyle: true,
    alignment: DialogAlignment.Center
  })

  build() {
    Column() {
      Text('Login')

      Row() {
        Text('username').margin({ right: 10 })
        TextInput({ placeholder: 'input phone number' }).layoutWeight(1)
          .onChange((value: string) => {
            this.username = value;
          })
      }
      .padding(20)

      Row() {
        Text('password').margin({ right: 10 })
        TextInput({ placeholder: 'input password' })
          .layoutWeight(1)
          .type(InputType.Password)
          .onChange((value: string) => {
            this.password = value;
          })
      }
      .padding(20)

      Row() {
        Button({ type: ButtonType.Normal }) {
          Text('login')
            .fontSize(20)
            .fontColor(Color.White)
            .padding({ left: 110, top: 5, right: 110, bottom: 5 })
        }
        .margin({ top: 50 })
        .borderRadius(8)
        .backgroundColor('#f68a0f')
        .onClick(async () => {
          this.controller.open();
          const result = await CloudDBService.login(this.username, this.password);
          this.controller.close();
          if (result) {
            prompt.showToast({
              message: 'Login success.',
              duration: 2000,
            });
            this.currentIndexRef = 3;
            this.BuildRef[2]['num'] = 3;
          } else {
            prompt.showToast({
              message: 'Login failed!',
              duration: 2000,
            });
          }
        })
      }
    }
    .justifyContent(FlexAlign.Center)
  }
}