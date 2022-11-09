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