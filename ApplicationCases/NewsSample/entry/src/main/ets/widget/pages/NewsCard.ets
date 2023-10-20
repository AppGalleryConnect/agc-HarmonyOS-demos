let newsStorage = new LocalStorage();

@Entry(newsStorage)
@Component
struct NewsCard {
  @LocalStorageProp('id') newsId:number=0;
  @LocalStorageProp('title') newsTitle:string="入春来，百花香";
  @LocalStorageProp('image') newsImage:string="ic_news_1_1.png";

  build() {
    Row() {
      Stack({ alignContent: Alignment.BottomStart })  {
        Image($rawfile(this.newsImage))
          .width('100%')
          .height('100%')
          .objectFit(ImageFit.Fill)
          .borderRadius($r('app.float.image_border_radius'))
        Text(this.newsTitle)
          .fontSize($r('app.float.mini_title_font_size'))
          .fontWeight(FontWeight.Bolder)
          .fontColor($r('app.color.mini_text_font_color'))
          .padding({left:$r('app.float.row_padding'),bottom:$r('app.float.row_padding')})
      }
      .width('100%')
      .height('100%')
      .backgroundColor($r('app.color.start_window_background'))
    }
    .height('100%')
    .alignItems(VerticalAlign.Top)
    .onClick(() => {
      postCardAction(this, {
        "action": 'router',
        "abilityName": 'EntryAbility',
        "params": {
          "from":"WidgetCard",
          "targetPage": "pages/DetailPage",
          "id": this.newsId
        }
      });
    })
  }
}