import router from '@ohos.router';
import { NewsData } from '../common/bean/NewsData';
import { DEFAULT_48, FULL_HEIGHT, FULL_WIDTH, NewsDetailContent, NewsDetailScrollItem, NewsDetailSource, NewsDetailTitle } from '../common/constant/CommonConstant';
import NewsViewModel from '../viewmodel/NewsViewModel';
@Entry
@Component
struct DetailPage {
  private newsId: number
  private newsData: NewsData
  @State title: string=''
  private scroller: Scroller = new Scroller();

  parseRouterParams() {
    let params = router.getParams() as Record<string, Object>;
    if(params==null){
      this.newsId=AppStorage.Get('newsId')
      return
    }
    console.error("params"+params.id)
    this.newsId=(params.id as number);
  }

  getDetails() {
    this.newsData = NewsViewModel.getDetailNews(this.newsId);
  }

  aboutToAppear() {
    this.parseRouterParams()
    // Request news data.
    this.getDetails()
  }

  onScrollAction(that) {
    that.yOffset = that.scroller.currentOffset().yOffset;
    if (that.yOffset > DEFAULT_48) {
      this.title=this.newsData.title;
    } else {
      this.title=""
    }
  }

  build() {
    Navigation() {
      Scroll(this.scroller) {
        Column() {
          Text(this.newsData.title)
            .fontSize(NewsDetailTitle.TEXT_FONT_SIZE)
            .fontColor($r('app.color.fontColor_text'))
            .width(FULL_WIDTH)
            .maxLines(NewsDetailTitle.TEXT_MAX_LINES)
            .margin({top: NewsDetailTitle.TEXT_MARGIN_TOP })
            .padding({ left: NewsDetailTitle.TEXT_MARGIN_LEFT})
            .textOverflow({ overflow: TextOverflow.Ellipsis })
            .fontWeight(NewsDetailTitle.TEXT_FONT_WEIGHT)
            .textAlign(TextAlign.Start)
          Text(this.newsData.source)
            .fontSize(NewsDetailSource.FONT_SIZE)
            .fontColor($r('app.color.fontColor_text2'))
            .height(NewsDetailSource.HEIGHT)
            .width(NewsDetailSource.WIDTH)
            .maxLines(NewsDetailSource.MAX_LINES)
            .margin({ left: NewsDetailSource.MARGIN_LEFT, top: NewsDetailSource.MARGIN_TOP })
            .textOverflow({ overflow: TextOverflow.None })
          Text(this.newsData.content)
            .fontSize(NewsDetailContent.FONT_SIZE)
            .fontColor($r('app.color.fontColor_text'))
            .width(NewsDetailContent.WIDTH)
            .margin({ left: NewsDetailContent.MARGIN_LEFT, top: NewsDetailContent.MARGIN_TOP })
            .textOverflow({ overflow: TextOverflow.Ellipsis })
          ForEach(this.newsData.imagesUrl, itemImg => {
            Image($rawfile(itemImg.url))
              .objectFit(ImageFit.Cover)
              .width(NewsDetailScrollItem.WIDTH)
              .height(NewsDetailScrollItem.HEIGHT)
              .margin({ left: NewsDetailScrollItem.MARGIN_LEFT, top: NewsDetailScrollItem.MARGIN_TOP,
                right: NewsDetailScrollItem.MARGIN_RIGHT })
          }, (itemImg, index) => JSON.stringify(itemImg) + index.toString())
        }
        .margin({ bottom: NewsDetailScrollItem.MARGIN_BOTTOM })
      } .onScroll(() => { this.onScrollAction(this) })
    }
    .size({ width: FULL_WIDTH, height: FULL_HEIGHT })
    .title(this.title)
    .titleMode(NavigationTitleMode.Mini)
  }
}