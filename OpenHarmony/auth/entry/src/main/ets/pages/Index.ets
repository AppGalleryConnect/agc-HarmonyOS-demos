import { AGConnectAuth } from "@hw-agconnect/auth-ohos"
import router from '@system.router';
import { Logger } from '@hw-agconnect/base-ohos';

let auth: AGConnectAuth = globalThis.agconnect.auth();
globalThis.auth = auth;

const TAG = "AuthTestPage";

@Entry
@Component
struct Index {

  build() {
    Row() {
      Column() {

        Button("getUser").width('90%').margin({ top: 20 })
          .onClick(() => {
            auth.getCurrentUser().then((user) => {
              if (user == null) {
                Logger.info(TAG, "no user login in ");
              } else {
                Logger.info(TAG, "getCurrentUser getUid " + user.getUid());
                Logger.info(TAG, "getCurrentUser getDisplayName " + user.getDisplayName());
                let array = user.getProviderInfo();
                for (var i = 0; i < array.length; i++) {
                  let map = array[i];
                  map.forEach((value, key) => {
                    Logger.info(TAG, "getCurrentUser  getProviderInfo " + key + ", " + value);
                  })
                }
                user.getToken(false).then(tokenResult => {
                }).catch(err => {
                })
              }
            }).catch(error => {
              Logger.error(TAG, "getCurrentUser error " + error);
            })
          })

        Button("signOut").width('90%').margin({ top: 20 })
          .onClick(() => {
            auth.signOut().then(() => {
              Logger.info(TAG, "signOut success ");
            }).catch(error => {
              Logger.error(TAG, "signOut error " + error);
            })
          })

        Button("deleteUser").width('90%').margin({ top: 20 })
          .onClick(() => {
            auth.deleteUser().then(() => {
              Logger.info(TAG, "deleteUser success ");
            }).catch(error => {
              Logger.error(TAG, "deleteUser error " + error);
            })
          })

        Button("gotoEmail").width('90%').margin({ top: 20 })
          .onClick(() => {
            router.push({
              uri: 'pages/emailPage'
            })
          })

        Button("gotoPhone").width('90%').margin({ top: 20 })
          .onClick(() => {
            router.push({
              uri: 'pages/phonePage'
            })
          })
      }
      .width('100%')
    }
    .height('100%')
  }
}