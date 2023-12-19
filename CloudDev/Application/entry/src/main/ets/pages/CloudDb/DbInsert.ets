import router from '@ohos.router';
import { Post } from './Post';
import Long from "long";
import buffer from '@ohos.buffer';
import cloud, { AuthUser, Database, ObjectTypeInfo } from '@hw-agconnect/cloud';

interface RouterParamsType {
  isEditing?: boolean
  checkBoxStatus?: boolean
  recordId?: string
  content?: string
}

@Entry
@Component
struct DbInsert {
  private params = router.getParams() as Record<string, Object>
  private isEditing: boolean = this.params.isEditing as boolean;
  private recordId: string = this.params.recordId as string;
  private contentR: string = this.params.content as string;
  private checkBoxChecked: boolean = this.params.checkBoxStatus as boolean;
  private user: AuthUser | null | undefined = AppStorage.Get('user');
  @State content: string = '';
  agcDataBase: Database | undefined = undefined

  aboutToAppear() {
    this.user = AppStorage.Get<AuthUser>('user');
    this.loadObjectInfo().then((res: ObjectTypeInfo) => {
      this.agcDataBase = cloud.database({ objectTypeInfo: res, zoneName: "Demo" })
    })
  }

  loadObjectInfo(): Promise<ObjectTypeInfo> {
    return new Promise(async (resolve: Function) => {
      const context = getContext(this)
      const value = await context.resourceManager.getRawFileContent('schema.json');
      let json: string = buffer.from(value).toString("utf8");
      let objectTypeInfo: ObjectTypeInfo = JSON.parse(json);
      resolve(objectTypeInfo)
    })
  }

  onPageShow() {
    if (this.recordId?.length) {
      this.content = this.contentR;
    }
  }

  handleUpsert() {
    if (this.content !== '') {
      let post = new Post();
      if (this.isEditing) {
        post.setId(Long.fromString(this.recordId.toString()));
      }
      let uid = this.user ? this.user.getUid() : ''
      post.setContent(this.content);
      post.setInsertDate(new Date());
      post.setUserId(uid);
      try {
        (this.agcDataBase as Database).collection(Post).upsert(post)
        AlertDialog.show({
          title: $r('app.string.dialog_success_title'),
          message: $r('app.string.dialog_success_message'),
          cancel: () => {
            let params: RouterParamsType = { checkBoxStatus: this.checkBoxChecked }
            router.back({ url: 'pages/CloudDb/CloudDb', params });
          }
        });
        this.content = '';
      } catch (err) {
      }
    }
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
        TextArea({ text: this.content })
          .height(100)
          .margin({ top: 20 })
          .backgroundColor($r('app.color.start_window_background'))
          .border({
            color: $r('app.color.body_color'),
            width: 1,
            radius: 4
          })
          .onChange((event) => {
            this.content = event;
          })

        Button({ type: ButtonType.Normal, stateEffect: true }) {
          Text($r('app.string.publishButton_label'))
            .textAlign(TextAlign.Center)
            .fontSize($r('app.float.body_font_size'))
            .fontColor($r('app.color.start_window_background'))
            .width('90%')
            .height(40)
        }
        .margin({ top: 10 })
        .borderRadius(4)
        .width('100%')
        .backgroundColor($r('app.color.login_button'))
        .onClick(() => {
          this.handleUpsert();
        })
      }.margin({
        left: 20,
        right: 20
      })
    }.height('100%')
  }

  @Builder
  NavigationTitle() {
    Column() {
      Text($r('app.string.cloudDB_label'))
        .fontSize($r('app.float.navigation_font_size'))
    }
  }
}