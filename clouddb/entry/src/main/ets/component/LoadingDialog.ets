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