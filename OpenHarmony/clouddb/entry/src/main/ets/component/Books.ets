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
import { CloudDBService as CloudDB } from '../Service/CloudDBService';
import { BookInfo } from '../Service/BookInfo';

@Component
export default struct Books {
  @State bookList: Array<BookInfo> = []
  @State searchStr: string = ''
  controller: SearchController = new SearchController()
  loading: CustomDialogController = new CustomDialogController({
    builder: LoadingDialog(),
    autoCancel: false,
    customStyle: true,
    alignment: DialogAlignment.Center
  })

  build() {
    Column() {
      Search({ value: this.searchStr, placeholder: "Search book name or author...", controller: this.controller })
        .width('85%')
        .margin(26)
        .onSubmit(async (value: string) => {
          this.bookList = await CloudDB.queryBooks(value);
          this.searchStr = value;
        })
        .onChange((value: string) => {
          this.searchStr = value;
        })

      List({ space: 5, initialIndex: 0 }) {
        ForEach(this.bookList, (item: BookInfo) => {
          ListItem() {
            Row() {
              Navigator({ target: 'pages/book/detail' }) {
                Image(item.getImageUrl() ? item.getImageUrl() : $r('app.media.icon'))
                  .width(100)
                  .height(100)
                  .margin({ left: 5, right: 10 })
              }
              .params({ book: item })

              Column() {
                Row() {
                  Text(item.getBookName()).fontSize(16).margin({ right: 5 })
                }

                Row() {
                  Text(item.getAuthor()).fontSize(14)
                  Divider().vertical(true).margin(5).height(15)
                  Text('￥' + item.getPrice()).fontSize(14)
                }

                Text(item.getDescription())
                  .maxLines(3)
                  .textOverflow({ overflow: TextOverflow.Ellipsis })
                  .fontSize(14)
              }
              .height(100)
              .alignItems(HorizontalAlign.Start)
              .layoutWeight(3)

              Column() {
                Button('Del')
                  .opacity(0.8)
                  .backgroundColor(0xF55A42)
                  .onClick(async () => {
                    AlertDialog.show({
                      title: 'Warning',
                      message: 'Delete this record?',
                      primaryButton: {
                        value: 'cancel',
                        action: () => {
                          console.info('Cancel delete record.')
                        }
                      },
                      secondaryButton: {
                        value: 'ok',
                        action: async () => {
                          this.loading.open();
                          await CloudDB.deleteRecord(item);
                          this.loading.close();
                          prompt.showToast({
                            message: 'Delete success.',
                            duration: 2000,
                          });
                          this.bookList = await CloudDB.queryBooks(this.searchStr);
                        }
                      },
                      cancel: () => {
                        console.info('Closed callbacks')
                      }
                    })
                  })
              }
              .width(50)
              .alignItems(HorizontalAlign.Center)
              .justifyContent(FlexAlign.Center)
              .margin({ right: 10 })
              .layoutWeight(1)
            }
            .height(150)
            .width('100%')
          }.editable(true)
        }, (item: BookInfo) => item.getId())
      }
      .listDirection(Axis.Vertical)
      .height('70%')
      .margin({ top: 10, left: 18 })
      .width('100%')
      .layoutWeight(2)
    }
    .onAppear(async () => {
      await CloudDB.init(getContext(this));
      this.bookList = await CloudDB.queryBooks();
    })
  }
}