@Component
export default struct Mine {

  build() {
    Row() {
      Text('Welcome to CloudDB Demo!')
    }
    .width('100%')
    .height('100%')
    .alignItems(VerticalAlign.Center)
    .justifyContent(FlexAlign.Center)
  }
}