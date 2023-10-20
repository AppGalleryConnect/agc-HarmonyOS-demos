/*
 * Copyright (c) 2023 Huawei Device Co., Ltd.
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
import {TabBars } from '../common/constant/CommonConstant';
import { NewsData } from '../common/bean/NewsData';
import NewsTypeBean from '../common/bean/NewsTypeBean';
import { newsDataArray,newsType } from '../datasource/NewsDataSource';

class NewsViewModel {
  /**
   * Get news type list from server.
   *
   * @return NewsTypeBean[] newsTypeList
   */
  getNewsTypeList(): NewsTypeBean[] {
    return newsType;
  }

  /**
   * Get default news type list.
   *
   * @return NewsTypeBean[] newsTypeList
   */
  getDefaultTypeList(): NewsTypeBean[] {
    return TabBars.DEFAULT_NEWS_TYPES;
  }

  /**
   * Get news type list from server.
   *
   * @return NewsData[] newsDataList
   */
  getNewsList(currentIndex:number,pageSize: number): NewsData[] {
    return newsDataArray.slice(0,  pageSize);
  }

  getRandomNews(): NewsData {
    var index=Math.floor((Math.random()*newsDataArray.length))
    return newsDataArray[index];
  }

  getDetailNews(id:number): NewsData {
    return newsDataArray[id];
  }

  formatParams = (params: NewsData) => {
    return JSON.stringify(params);
  }
}

let newsViewModel = new NewsViewModel();

export default newsViewModel as NewsViewModel;