import { AGConnectFunction } from "@hw-agconnect/function-ohos";

let agcFunction:AGConnectFunction = globalThis.agconnect.function();

@Entry
@Component
struct Index {

  build() {
    Row() {
      Column() {
        Button("function call")
          .width('90%')
          .margin({ top: 20 })
          .onClick(() => {
            //define your request body.
            let reqBody = {};

            //define your httpTriggerURI
            let httpTriggerURI = "***-$latest";
            let callableFunc = agcFunction.wrap(httpTriggerURI);
            callableFunc.call(reqBody).then((result)=> {
              console.info(JSON.stringify(result));
            }).catch((error)=> {
              console.info(JSON.stringify(error));
            })

          })
      }
      .width('100%')
    }
    .height('100%')
  }
}