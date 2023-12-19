import cloud, { ProgressEvent, AuthUser, UploadParam } from '@hw-agconnect/cloud';
import { Login, AuthMode } from "@hw-agconnect/auth-component";
import picker from '@ohos.file.picker'
import fs from '@ohos.file.fs';
import image from '@ohos.multimedia.image';



@Entry
@Component
struct CloudStorage {
  @StorageLink('user') user: AuthUser | null | undefined = null;
  @State isShowButton: boolean = true
  @State image: Resource | PixelMap = $r('app.media.empty_image');
  @State progress: string = '';
  @State accessAddress: string = '';
  @State publicAccessAddress: string = '';
  @State imageUploadingProgress: number = 0;
  @State canUpload: boolean = true;
  @State imgUri: string = ""

  aboutToAppear() {
    this.user = AppStorage.Get<AuthUser>('user')
  }

  // photoViewPicker
  initImagPicker(): Promise<string> {
    return new Promise((resolve: Function, reject: Function) => {
      let uri: string;
      const photoSelectOptions = new picker.PhotoSelectOptions();
      photoSelectOptions.MIMEType = picker.PhotoViewMIMETypes.IMAGE_TYPE; // 过滤选择媒体文件类型为IMAGE
      photoSelectOptions.maxSelectNumber = 1; // 选择媒体文件的最大数目
      const photoPicker = new picker.PhotoViewPicker();
      photoPicker.select(photoSelectOptions).then((photoSelectResult: picker.PhotoSelectResult) => {
        uri = photoSelectResult.photoUris[0];
        this.imgUri = uri
        resolve(uri)
      }).catch((err: Object) => {
        reject(err)
      })
    })
  }

  upLoadImage() {
    this.initImagPicker().then((res: string) => {
      this.canUpload = false;
      let cloudPath: string = `testcloud/demo_${new Date().getTime()}.jpg`
      let uploadParmas: UploadParam = {
        localPath: res,
        cloudPath: cloudPath,
        onUploadProgress: (pe: ProgressEvent) => {
          console.log(`onUploadProgress:bytes:${pe.loaded} total:${pe.total}`)
          let percentCompleted = Math.round(
            (pe.loaded * 100) / pe.total
          )
          this.imageUploadingProgress = percentCompleted;
        }
      }
      cloud.storage().upload(uploadParmas)
      this.getDownloadUrl(cloudPath)
    })
  }

  getDownloadUrl(path: string) {
    cloud.storage().getDownloadURL(path).then(async (downUrl: string) => {
      this.accessAddress = downUrl
      this.publicAccessAddress = downUrl
      let file = fs.openSync(this.imgUri, fs.OpenMode.READ_ONLY)
      const imageSource = image.createImageSource(file.fd)
      let options: image.InitializationOptions = {
        alphaType: 0,
        editable: false,
        pixelFormat: 3,
        scaleMode: 1,
        size: { height: 100, width: 100 }
      };
      this.image = await imageSource.createPixelMap(options)
      this.canUpload = true;
    })
  }

  build() {
    Column() {
      Navigation()
        .title(this.NavigationTitle())
        .height('50vp')
        .width('100%')
        .margin({ bottom: 10 })
        .titleMode(NavigationTitleMode.Mini)

      Column() {
        Row() {
          Text($r('app.string.cloudStorage_description')).fontSize($r('app.float.body_font_size'))
        }.margin({ bottom: 15 })

        Row() {
          if (this.user !== null && this.user !== undefined) {
            Button($r('app.string.cloudStorage_uploadButton'), { type: ButtonType.Normal })
              .borderRadius(4)
              .width('100%')
              .backgroundColor($r('app.color.login_button'))
              .opacity(this.canUpload ? 1 : 0.5)
              .enabled(this.canUpload)
              .height(40)
              .onClick(() => {
                // this.readFsBuff();
                this.upLoadImage()
              })
          } else {
            Column() {
              Login({
                modes: [AuthMode.PHONE_VERIFY_CODE],
                onSuccess: (user: AuthUser) => {
                  this.isShowButton = false
                  AppStorage.Set<AuthUser>('user', user);
                }
              }) {
                Button($r('app.string.cloudStorage_uploadButton'), { type: ButtonType.Normal })
                  .borderRadius(4)
                  .width('100%')
                  .backgroundColor($r('app.color.login_button'))
                  .opacity(this.canUpload ? 1 : 0.5)
                  .enabled(this.canUpload)
                  .height(40)
              }
            }
          }
        }

        if (this.imageUploadingProgress !== 0) {
          Row() {
            Text($r('app.string.cloudStorage_progressLabel')).fontSize($r('app.float.body_font_size'))
            Text(': ' + this.imageUploadingProgress.toString().substr(0, 5) + " %")
              .fontSize($r('app.float.body_font_size'))
          }
          .margin({ top: 10 })
        }
      }.alignItems(HorizontalAlign.Start).width('90%').margin({ bottom: 20 })

      Column() {
        Row() {
          Image(this.image).objectFit(ImageFit.Contain).height(250)
            .backgroundColor($r('app.color.black'))
        }
      }.width('90%').margin({ bottom: 15 })

      if (this.accessAddress !== '' && this.publicAccessAddress !== '') {
        Column({ space: 10 }) {
          Flex({ justifyContent: FlexAlign.SpaceBetween }) {
            Column() {
              Text($r('app.string.cloudStorage_accessAddressLabel'))
                .fontSize($r('app.float.list_item_font_size'))
                .fontWeight(FontWeight.Bold)
                .textAlign(TextAlign.Start)
                .width('100%')

              Text(this.accessAddress.split('').join('\u200B'))
                .textOverflow({ overflow: TextOverflow.Ellipsis })
                .textAlign(TextAlign.Start)
                .width('100%')
                .maxLines(1)
                .fontSize($r('app.float.list_item_font_size'))

            }.height(30).layoutWeight(3)
          }

          Flex({ justifyContent: FlexAlign.SpaceBetween }) {
            Column() {
              Text($r('app.string.cloudStorage_publicAccessAddressLabel'))
                .fontSize($r('app.float.list_item_font_size'))
                .fontWeight(FontWeight.Bold)
                .textAlign(TextAlign.Start)
                .width('100%')
              Text(this.publicAccessAddress.split('').join('\u200B'))
                .textOverflow({ overflow: TextOverflow.Ellipsis })
                .textAlign(TextAlign.Start)
                .width('100%')
                .maxLines(1)
                .fontSize($r('app.float.list_item_font_size'))

            }.height(30).layoutWeight(3)
          }
        }.width('90%').margin({ right: 10 })
      }
    }.height('100%')
  }

  @Builder
  NavigationTitle() {
    Column() {
      Text($r('app.string.cloudStorage_label')).fontSize($r('app.float.navigation_font_size'))
    }
  }
}