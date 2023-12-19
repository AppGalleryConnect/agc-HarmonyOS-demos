import cloud from '@hw-agconnect/cloud';

@Entry
@Component
struct Functions {
  year : string = '';
  @State functionResult: string = '';
  build() {
    Column() {
      TextInput({placeholder:"输入年份"})
        .width('90%')
        .onChange((value: string) => {
          this.year = value;
        })

      Button('查询', {type: ButtonType.Capsule, stateEffect: true})
        .margin({ top: '100px', bottom: '100px'})
        .width('90%')
        .onClick(() => {
          this.callFunction();
        })

      Text("生肖: " + this.functionResult)
        .textAlign(TextAlign.Center)
        .fontSize(20)
        .padding(10)
        .width('90%')

    }.justifyContent(FlexAlign.Center)
    .width('100%')
    .height('100%')
  }

  async callFunction() {
    let result  = await cloud.callFunction({
      name: "animal-symbolic",
      params: {
        "year": this.year
      }
    })

    this.functionResult = result.getValue().result
    console.log("animal", this.functionResult)
  }
}