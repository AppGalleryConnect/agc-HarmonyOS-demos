import { AGConnectAuth } from "@hw-agconnect/auth-ohos"
import { StorageReference } from '@hw-agconnect/cloudstorage-ohos'
import { Logger } from "@hw-agconnect/base-ohos"

let agconnect = globalThis.agconnect;
let auth: AGConnectAuth = agconnect.auth();

const TAG = "AGC_CLOUDSTORAGE_DEMO";


function stringToUint8Array(str){
  var arr = [];
  for (var i = 0, j = str.length; i < j; ++i) {
    arr.push(str.charCodeAt(i));
  }

  var tmpUint8Array = new Uint8Array(arr);
  return tmpUint8Array
}

@Entry
@Component
struct Index {
  @State message: string = 'Hello World'
  reference: StorageReference;

  build() {
    Row() {
      Column() {
        Button("init instance with sign")
          .width('90%')
          .margin({ top: 20 })
          .onClick(async () => {
            try {
              let user = await agconnect.auth().getCurrentUser();
              if (user) {
                let storageManagement = agconnect.cloudStorage(agconnect.instance());
                this.reference = await storageManagement.storageReference();
                Logger.info(TAG, "cloudstorage init ok");
              } else {
                Logger.info(TAG, "no user login in");
              }
            } catch (e) {
              Logger.info(TAG, "cloudstorage init failed" + e);
            }
          })

        Button("list")
          .width('90%')
          .margin({ top: 20 })
          .onClick(async () => {
            const child = this.reference.child('path');
            child.list({ maxResults: 10 }).then((res) => {
              Logger.info(TAG, "getList1 success" + JSON.stringify(res));
            }).catch(e => {
              Logger.info(TAG, "getList1 fail" + JSON.stringify(e));
            })
          })

        Button("getDownLoadUrl")
          .width('90%')
          .margin({ top: 20 })
          .onClick(async () => {
            const child = this.reference.child('path');
            child.getDownloadURL().then(async (res) => {

            });
          })

        Button("uploadTask")
          .width('90%')
          .margin({ top: 20 })
          .onClick(async () => {
            var raw = 'Test String 22'
            let array = stringToUint8Array(raw)
            let path = '123.txt';
            const child = this.reference.child(path);
              var uploadTask = child.putData(array);
              uploadTask.then((UploadResult) => {
              }).catch(error => {

              })

            });
      }
      .width('100%')
    }
    .height('100%')
  }
}