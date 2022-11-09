import LoadingDialog from './LoadingDialog';
import prompt from '@ohos.prompt';
import { BookInfo } from '../Service/BookInfo';
import { CloudDBService as CloudDB } from '../Service/CloudDBService';

@Component
export default struct Add {
  @State bookId: number = 1
  @State imageUrl: string = ''
  @State bookName: string = ''
  @State author: string = ''
  @State price: number = undefined
  @State description: string = ''
  loading: CustomDialogController = new CustomDialogController({
    builder: LoadingDialog(),
    autoCancel: false,
    customStyle: true,
    alignment: DialogAlignment.Center
  })

  build() {
    Column({ space: 20 }) {
      Row() {
        Text("id").width(90).fontSize(14)
        TextInput({ placeholder: 'id' }).layoutWeight(1)
          .type(InputType.Number)
          .onChange((value: string) => {
            this.bookId = Number(value);
          })
      }

      Row() {
        Text("BookName").width(90).fontSize(14)
        TextInput({ placeholder: 'input book name' }).layoutWeight(1)
          .onChange((value: string) => {
            this.bookName = value;
          })
      }

      Row() {
        Text("Author").width(90).fontSize(14)
        TextInput({ placeholder: 'input author' }).layoutWeight(1)
          .onChange((value: string) => {
            this.author = value;
          })
      }

      Row() {
        Text("Image url").width(90).fontSize(14)
        TextInput({ placeholder: 'input image url' }).layoutWeight(1)
          .onChange((value: string) => {
            this.imageUrl = value;
          })
      }

      Row() {
        Text("Preview").width(90).fontSize(14)
        Image(this.imageUrl ? this.imageUrl : $r('app.media.app_icon'))
          .width(100)
          .height(100)
      }
      .alignItems(VerticalAlign.Center)
      .justifyContent(FlexAlign.Start)

      Row() {
        Text("Price").width(90).fontSize(14)
        TextInput({ placeholder: 'input price' }).layoutWeight(1)
          .type(InputType.Number)
          .onChange((value: string) => {
            this.price = Number(value);
          })
      }

      Row() {
        Text("Description").width(90).fontSize(14)
        TextInput({ placeholder: 'input description' }).layoutWeight(1)
          .onChange((value: string) => {
            this.description = value;
          })
      }

      Row() {
        Button('Add', { type: ButtonType.Capsule, stateEffect: true }).backgroundColor(0x317aff).width(90)
          .onClick(async () => {
            if (!CloudDB.getLogin()) {
              prompt.showToast({
                message: 'Please login first.',
                duration: 2000,
              });
              return;
            }

            const book = new BookInfo();
            book.setId(this.bookId);
            book.setBookName(this.bookName);
            book.setAuthor(this.author);
            book.setImageUrl(this.imageUrl);
            book.setPrice(this.price);
            book.setDescription(this.description);
            this.loading.open();
            const upsertNum = await CloudDB.upsertRecord(book);
            this.loading.close();
            if (upsertNum > 0) {
              prompt.showToast({
                message: 'Add success.',
                duration: 2000,
              });
            } else {
              prompt.showToast({
                message: 'Add failed.',
                duration: 5000,
              });
            }
            console.log('upsertNum=' + upsertNum);
          })
      }
    }
    .margin({ top: 20 })
    .padding({ left: 10, right: 10 })
  }
}