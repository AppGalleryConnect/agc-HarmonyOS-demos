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

@CustomDialog
export default struct LoadingDialog {
  private controller: CustomDialogController;

  build() {
    Column({ space: 10 }) {
      LoadingProgress()
        .size({ width: 60, height: 60 })

      Text("Loading...")
        .fontSize(25)
    }
    .width("60%")
    .padding({ top: 10, bottom: 10 })
    .backgroundColor(Color.White)
    .borderRadius(10)
  }
}