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

import { BookInfo } from '../../Service/BookInfo';
import router from '@ohos.router'

@Entry
@Component
export default struct Detail {
  @State book: any = (router.getParams() as any).book;

  build() {
    Column({ space: 20 }) {
      Image(this.book.imageUrl ? this.book.imageUrl : $r('app.media.icon'))
        .width(300)
        .height(300)
        .margin({ left: 5, right: 10 })

      Text(this.book.bookName)
      Text(this.book.author)
      Divider()

      Scroll() {
        Text(this.book.description).fontSize(14)
      }
      .layoutWeight(1)
    }
    .width('100%')
    .height('100%')
    .padding(40)
    .alignItems(HorizontalAlign.Center)
    .justifyContent(FlexAlign.Start)
    .onAppear(() => {
      console.log('detail on appear... book=' + JSON.stringify(this.book));
      console.log((this.book instanceof BookInfo) + '');
      console.log((this.book as any).imageUrl)
    })
  }
}