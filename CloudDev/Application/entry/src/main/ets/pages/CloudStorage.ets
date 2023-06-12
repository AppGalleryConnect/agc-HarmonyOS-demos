import mediaLibrary from '@ohos.multimedia.mediaLibrary';
// @ts-ignore
import pasteboard from '@ohos.pasteboard';
import { Log } from '../common/Log';
import ImagePicker from '../components/image-picker/ImagePicker';
import ImageModel, { PictureItem } from '../components/image-picker/Image';
import { Constants } from '../common/Constants';
import { AGCStorageReference } from '../services/Storage';
import { AGCAuth, AgUser } from '../services/Auth';
import { AuthDialog } from '../components/AuthDialog';
import { AGConnectUser } from '@hw-agconnect/auth-ohos';

const TAG = "[CloudStoragePage]"

@Entry
@Component
struct CloudStorage {
  private ImageModel: ImageModel = new ImageModel(getContext(this));
  @State @Watch("onImageChange") index: number = -1;
  @State image: Resource | PixelMap = $r('app.media.empty_image');
  @State imgDatas: Array<mediaLibrary.FileAsset> = [];
  @State numArray: PictureItem[] = [];
  @State progress: string = '';
  @State accessAddress: string = '';
  @State publicAccessAddress: string = '';
  @State imageUploadingProgress: number = 0;
  @State canUpload: boolean = true;
  @State imageUploadingText: Resource = $r('app.string.cloudStorage_progressLabel');
  ref: AGCStorageReference;
  @StorageLink('user') user: AGConnectUser = AppStorage.Get<AGConnectUser>('user');
  context: any;

  aboutToAppear() {
    this.context = getContext(this);
    this.ref = new AGCStorageReference(this.context);
    this.ImageModel.getAllImg().then((images) => {
      this.imgDatas = images;
    }).catch(e => {
      Log.error(TAG, "get image error " + JSON.stringify(e));
    });
  }

  dialogController: CustomDialogController = new CustomDialogController({
    builder: AuthDialog(),
    autoCancel: true,
  });

  checkUserIsLoggedIn() {
    this.index = -1;

    if (this.user.getUid()) {
      this.imageDialogController.open();
    } else {
      this.dialogController.open();
    }
  }

  imageDialogController: CustomDialogController = new CustomDialogController({
    builder: ImagePicker({
      imageDatas: $imgDatas,
      index: $index,
    }),
    autoCancel: true,
    gridCount: 12
  });

  async onImageChange() {
    if (this.index !== -1) {
      this.imageDialogController.close();
      this.uploadImage(this.user, this.imgDatas[this.index]);
    }
  }

  async uploadImage(user, file) {
    this.canUpload = false;
    this.ref.uploadImage(
      user, file,
      (progress) => {
        this.imageUploadingText = $r('app.string.cloudStorage_progressLabel');
        this.imageUploadingProgress = progress;
        console.log('%' + this.imageUploadingProgress);
      },
      async (url: string) => {
        if (url === 'app.media.window') {
          this.image = $r('app.media.window');
          this.accessAddress = this.ref.randomizeImageName(false);
          this.publicAccessAddress = this.ref.randomizeImageName(true);
        }
        else {
          this.accessAddress = url.split('token')[0];
          this.publicAccessAddress = url;
          this.image = await this.ref.fetchNetworkImage(this.publicAccessAddress);
          Log.info(TAG, "Public Access: ", this.publicAccessAddress);
        }
        this.canUpload = true;
      },
      (error, showDialog=false) => {
        console.error("upload error" + error.message);
        this.canUpload = true;
        if (showDialog)
          AlertDialog.show({
            title: $r('app.string.common_prompt'),
            message: error.message,
          });
      });
  }

  build() {
    Column() {
      Navigation()
        .title(this.NavigationTitle())
        .height(Constants.VIEWPORT_50)
        .width(Constants.PERCENT_100)
        .margin({ bottom: Constants.LENGTH_10_PX })
        .titleMode(NavigationTitleMode.Mini)

      Column() {
        Row() {
          Text($r('app.string.cloudStorage_description')).fontSize($r('app.float.body_font_size'))
        }.margin({ bottom: Constants.LENGTH_15_PX })

        Row() {
          Button($r('app.string.cloudStorage_uploadButton'), { type: ButtonType.Normal })
            .borderRadius(Constants.BORDER_RADIUS_4_PX)
            .width(Constants.PERCENT_100)
            .backgroundColor($r('app.color.action_button_background'))
            .opacity(this.canUpload ? 1 : 0.5)
            .enabled(this.canUpload)
            .height(Constants.HEIGHT_40)
            .onClick(() => {
              this.checkUserIsLoggedIn();
            })
        }

        if (this.imageUploadingProgress !== 0) {
          Row() {
            Text(this.imageUploadingText).fontSize($r('app.float.body_font_size'))
            Text(': ' + this.imageUploadingProgress.toString().substr(0, 5) + " %")
              .fontSize($r('app.float.body_font_size'))
          }
          .margin({ top: Constants.LENGTH_10_PX })
        }

      }.alignItems(HorizontalAlign.Start).width(Constants.PERCENT_90).margin({ bottom: Constants.LENGTH_20_PX })

      Column() {
        Row() {
          Image(this.image).objectFit(ImageFit.Contain).height(Constants.LENGTH_250_PX)
            .backgroundColor($r('app.color.black'))
        }

      }.width(Constants.PERCENT_90).margin({ bottom: Constants.LENGTH_15_PX })

      if (this.accessAddress !== '' && this.publicAccessAddress !== '') {
        Column({ space: Constants.LENGTH_10_PX }) {


          Flex({ justifyContent: FlexAlign.SpaceBetween }) {
            Column() {
              Text($r('app.string.cloudStorage_accessAddressLabel'))
                .fontSize($r('app.float.list_item_font_size'))
                .fontWeight(FontWeight.Bold)
                .textAlign(TextAlign.Start)
                .width(Constants.PERCENT_100)

              Text(this.accessAddress.split('').join('\u200B'))
                .textOverflow({ overflow: TextOverflow.Ellipsis })
                .textAlign(TextAlign.Start)
                .width(Constants.PERCENT_100)
                .maxLines(1)
                .fontSize($r('app.float.list_item_font_size'))

            }.height(Constants.LENGTH_30_PX).layoutWeight(3)
          }


          Flex({ justifyContent: FlexAlign.SpaceBetween }) {
            Column() {
              Text($r('app.string.cloudStorage_publicAccessAddressLabel'))
                .fontSize($r('app.float.list_item_font_size'))
                .fontWeight(FontWeight.Bold)
                .textAlign(TextAlign.Start)
                .width(Constants.PERCENT_100)

              Text(this.publicAccessAddress.split('').join('\u200B'))
                .textOverflow({ overflow: TextOverflow.Ellipsis })
                .textAlign(TextAlign.Start)
                .width(Constants.PERCENT_100)
                .maxLines(1)
                .fontSize($r('app.float.list_item_font_size'))

            }.height(Constants.LENGTH_30_PX).layoutWeight(3)
          }
        }.width(Constants.PERCENT_90).margin({ right: Constants.LENGTH_10_PX })
      }
    }.height(Constants.PERCENT_100)
  }

  @Builder NavigationTitle() {
    Column() {
      Text($r('app.string.cloudStorage_label')).fontSize($r('app.float.navigation_font_size'))
    }
  }
}