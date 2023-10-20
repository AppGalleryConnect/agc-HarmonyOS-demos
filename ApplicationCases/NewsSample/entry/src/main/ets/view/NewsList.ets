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

import {
  NewsListConstant,
  FULL_WIDTH,
  FULL_HEIGHT,
  PAGE_SIZE
} from '../common/constant/CommonConstant';
import NewsItem from './NewsItem';

import NewsViewModel from '../viewmodel/NewsViewModel';
import router from '@ohos.router';

/**
 * The news list component.
 */
@Component
export default struct NewsList {
  @Link currentIndex: number;
  @State newsData: Array<any> = [];
  @State pageSize: number = PAGE_SIZE;

  changeCategory() {
    this.newsData = NewsViewModel.getNewsList(this.currentIndex,this.pageSize);
  }

  aboutToAppear() {
    // Request news data.
    this.changeCategory();
  }

  build() {
    Column() {
        this.ListLayout()
    }
    .width(FULL_WIDTH)
    .height(FULL_HEIGHT)
    .justifyContent(FlexAlign.Center)
  }

  @Builder ListLayout() {
    List() {
      ForEach(this.newsData, item => {
        ListItem() {
          NewsItem({ newsData: item })
        }
        .height(NewsListConstant.ITEM_HEIGHT)
        .backgroundColor($r('app.color.white'))
        .margin({ top: NewsListConstant.ITEM_MARGIN_TOP})
        .borderRadius(NewsListConstant.ITEM_BORDER_RADIUS)
        .onClick(() => {
          router.pushUrl({
            url: 'pages/DetailPage',
            params: {
              id:item.id
            }
          })
        })
      }, (item, index) => JSON.stringify(item) + index.toString())
    }
    .width(NewsListConstant.LIST_WIDTH)
    .height(FULL_HEIGHT)
    .margin({ left: NewsListConstant.LIST_MARGIN_LEFT, right: NewsListConstant.LIST_MARGIN_RIGHT })
    .backgroundColor($r('app.color.listColor'))
    .divider({
      color: $r('app.color.dividerColor'),
      strokeWidth: NewsListConstant.LIST_DIVIDER_STROKE_WIDTH,
      endMargin: NewsListConstant.LIST_MARGIN_RIGHT
    })
  }

}