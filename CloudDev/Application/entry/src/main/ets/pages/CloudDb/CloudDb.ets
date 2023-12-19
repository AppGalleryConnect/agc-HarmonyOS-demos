import cloud, { AuthUser, Database, ObjectTypeInfo } from '@hw-agconnect/cloud';
import { Login, AuthMode } from "@hw-agconnect/auth-component";
import { Post } from './Post';
import router from '@ohos.router';
import buffer from '@ohos.buffer';
import Long from 'long';
import Logger from '@ohos.hilog';

const TAG = "[CloudDBPage]";

interface RouterParamsType {
  isEditing?: boolean
  checkBoxStatus?: boolean
  recordId?: string
  content?: string
}

@Entry
@Component
struct CloudDb {
  private scroller: Scroller = new Scroller()
  @StorageLink('user') user: AuthUser | null | undefined = null;
  @State isSelected: boolean = false;
  @State isShowButton: boolean = true;
  @State allRecords: Post[] = [];
  @State likeCount: number = 0
  agcDataBase: Database | undefined = undefined

  @Builder
  NavigationTitle() {
    Column() {
      Text($r('app.string.cloudDB_label'))
        .fontSize($r('app.float.navigation_font_size'))
    }
  }

  onPageShow() {
    this.orderByDate()
  }

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

  async getPostList() {
    try {
      const resultArray = await (this.agcDataBase as Database)
        .collection(Post)
        .query()
        .orderByDesc("insertDate")
        .limit(100, 0)
        .get();
      this.allRecords = resultArray
      console.log("query success : " + JSON.stringify(resultArray));
    } catch (err) {
      console.log("query err------------", JSON.stringify(err));
    }
  }

  async orderByMostLiked() {
    try {
      const resultArray = await (this.agcDataBase as Database)
        .collection(Post)
        .query()
        .orderByDesc("likeCount")
        .limit(100, 0)
        .get();
      this.allRecords = resultArray
      console.log("query success : " + JSON.stringify(resultArray));
    } catch (err) {
      console.log("query err------------" + JSON.stringify(err));
    }
  }

  async getMyPosts(uid: string) {
    try {
      const resultArray = await (this.agcDataBase as Database)
        .collection(Post)
        .query()
        .equalTo("userId", uid)
        .orderByDesc("insertDate")
        .get();
      this.allRecords = resultArray
    } catch (err) {
    }
  }

  async getMyPostsByLike(uid: string) {
    try {
      const resultArray = await (this.agcDataBase as Database)
        .collection(Post)
        .query()
        .equalTo("userId", uid)
        .orderByDesc("likeCount")
        .get();
      this.allRecords = resultArray
    } catch (err) {
    }
  }

  async deleteRecord(post: Post, uid: string) {
    if (post.userId === uid) {
      try {
        const record = await (this.agcDataBase as Database)
          .collection(Post)
          .delete(post);
        this.onCheckboxToggle()
      } catch (err) {
      }
    }
  }

  async updateLikeCount(item: Post) {
    let likes: string[] = JSON.parse(item.likes)
    let userId: string = this.user ? this.user.getUid() : '';
    let index: number = likes.indexOf(userId);
    if (index >= 0) {
      likes = likes.filter((item: string) => item !== userId)
      item.likeCount = likes.length
      item.setLikes(JSON.stringify(likes))
    } else {
      likes.push(userId);
      item.likeCount = likes.length
      item.setLikes(JSON.stringify(likes))
    }
    try {
      await (this.agcDataBase as Database).collection(Post).upsert(item)
    } catch (err) {
    }
    this.allRecords = this.allRecords.map((record: Post) => {
      if ((record.id as Long).toString() == (item.id as Long).toString()) {
        console.log('record' + JSON.stringify(record))
        record.likeCount = item.likeCount
        record.likes = item.likes
      }
      return record
    })
  }

  async orderByDate() {
    if (this.isSelected) {
      let userId: string = this.user ? this.user.getUid() : '';
      this.getMyPosts(userId)
    } else {
      this.getPostList()
    }
  }

  async orderByLikes() {
    if (this.isSelected) {
      let userId: string = this.user ? this.user.getUid() : '';
      this.getMyPostsByLike(userId)
    } else {
      this.orderByMostLiked();
    }
  }

  async onCheckboxToggle() {
    if (this.isSelected) {
      let userId: string = this.user ? this.user.getUid() : '';
      this.getMyPosts(userId)
    } else {
      this.getPostList()
    }

  }

  onCancel() {
    Logger.info(0x00, TAG, 'Cancel button is clicked');
  }

  onAccept() {
    Logger.info(0x00, TAG, 'Confirm button is clicked');
  }

  existApp() {
    Logger.info(0x00, TAG, 'Click the callback in the blank area');
  }

  pad(val: number) {
    let str = val.toString();
    return str.padStart(2, '0');
  }

  getFormattedDate(date: Date) {
    return (
      [date.getFullYear(), this.pad(date.getMonth() + 1), this.pad(date.getDate())].join('-')
        +
        ' '
        +
      [this.pad(date.getHours()), this.pad(date.getMinutes()), this.pad(date.getSeconds()),].join(':')
    );
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
        Flex({ justifyContent: FlexAlign.SpaceBetween }) {
          Row() {
            Row() {
              Text($r('app.string.cloudDB_Publications')).fontSize($r('app.float.list_item_font_size')).onClick(() => {
                // order by date
                this.orderByDate();
              })
              Text('|').fontSize($r('app.float.list_item_font_size')).margin({ left: 5 })
              Text($r('app.string.cloudDB_HighestHit'))
                .fontSize($r('app.float.list_item_font_size'))
                .margin({ left: 5 })
                .onClick(() => {
                  this.orderByLikes();
                })
            }.border({ radius: 4, width: 1 }).padding(5)

            Row() {
              if (this.user !== null && this.user !== undefined) {
                Checkbox()
                  .width(8)
                  .height(8)
                  .onChange((value: boolean) => {
                    this.isSelected = value;
                    this.onCheckboxToggle();
                  })
                  .onAppear(() => {
                    this.onCheckboxToggle();
                  })
                  .select(this.isSelected)
                Text($r('app.string.cloudDB_SeeOnly')).fontSize($r('app.float.list_item_font_size'))
              }
            }.margin({ left: 5 })
          }

          Row() {
            if (this.user != null && this.user != undefined) {
              Button({ type: ButtonType.Normal, stateEffect: true }) {
                Text($r('app.string.cloudDB_new_record'))
                  .fontSize($r('app.float.list_item_font_size'))
                  .textAlign(TextAlign.Center)
                  .margin(5)
                  .fontColor($r('app.color.white'))
              }
              .width(70)
              .height(24)
              .borderRadius(4)
              .backgroundColor($r('app.color.login_button'))
              .onClick(() => {
                let params: RouterParamsType = {
                  isEditing: false,
                  checkBoxStatus: this.isSelected,
                }
                router.pushUrl({
                  url: 'pages/CloudDb/DbInsert',
                  params
                });
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
                  Button({ type: ButtonType.Normal, stateEffect: true }) {
                    Text($r('app.string.cloudDB_new_record'))
                      .fontSize($r('app.float.list_item_font_size'))
                      .textAlign(TextAlign.Center)
                      .margin(5)
                      .fontColor($r('app.color.white'))
                  }
                  .width(70)
                  .height(24)
                  .borderRadius(4)
                  .backgroundColor($r('app.color.login_button'))
                }
              }
            }
          }
        }.margin({ top: 10, bottom: 10 })

        Column() {
          List({ initialIndex: 0, scroller: this.scroller }) {
            ForEach(this.allRecords, (item: Post) => {
              ListItem() {
                Column() {
                  Column() {
                    Flex({ justifyContent: FlexAlign.SpaceBetween }) {
                      Image($r('app.media.window')).width(20).height(20)
                      Text(item.insertDate ? this.getFormattedDate(new Date(item.insertDate)) : '')
                        .fontSize($r('app.float.list_item_font_size'))
                    }.margin({ bottom: 15 })

                    Row() {
                      Text(item.content)
                        .fontSize($r('app.float.list_item_font_size'))
                        .width('100%')
                        .maxLines(2)
                    }.margin({ bottom: 15 })

                    Flex({ justifyContent: FlexAlign.SpaceBetween }) {
                      if (this.user != null && this.user != undefined && this.user?.getUid() === item?.userId) {
                        Row() {
                          Row() {
                            Image($r("app.media.ic_public_delete_filled_red"))
                              .width(20)
                              .height(20)
                              .margin({ right: 10 })
                              .onClick(() => {
                                AlertDialog.show({
                                  title: $r('app.string.deleteButton_label'),
                                  message: $r('app.string.popUpDesc_label'),
                                  primaryButton: {
                                    value: $r('app.string.deleteButton_label'),
                                    action: () => {
                                      let userId: string = this.user ? this.user.getUid() : '';
                                      this.deleteRecord(item, userId)
                                    },
                                    backgroundColor: $r('app.color.dialog_cancel_background'),
                                    fontColor: $r('app.color.white')
                                  },
                                  secondaryButton: {
                                    value: $r('app.string.cancelButton_label'),
                                    action: () => {
                                      this.onCancel();
                                    },
                                    fontColor: $r('app.color.action_button_background')
                                  },
                                  autoCancel: true
                                });
                              })
                            Image($r("app.media.ic_public_edit_outline"))
                              .width(20)
                              .height(20)
                              .onClick(() => {
                                let recordId = ''
                                if (item.id !== undefined) {
                                  recordId = item.id.toString()
                                }
                                let params: RouterParamsType = {
                                  isEditing: true,
                                  recordId: recordId,
                                  content: item.content,
                                  checkBoxStatus: this.isSelected,
                                }
                                router.pushUrl({
                                  url: 'pages/CloudDb/DbInsert',
                                  params
                                });
                              })
                          }
                        }
                      }
                      if (this.user != null && this.user != undefined) {
                        Row() {
                          Text(item.likeCount.toString()).fontSize($r('app.float.list_item_font_size'))
                          Image(item.likes.includes(this.user?.getUid()) && this.user?.getUid() ? $r('app.media.ic_public_thumbsup_filled') : $r('app.media.ic_public_thumbsup'))
                            .width(20)
                            .height(20)
                            .margin({ left: 5 })
                            .onClick(() => {
                              this.updateLikeCount(item);
                            })
                        }

                      } else {
                        Row() {
                          Login({
                            modes: [AuthMode.PHONE_VERIFY_CODE],
                            onSuccess: (user: AuthUser) => {
                              this.isShowButton = false
                              AppStorage.Set<AuthUser>('user', user);
                            }
                          }) {
                            Row() {
                              Text(item.likeCount.toString()).fontSize($r('app.float.list_item_font_size'))
                              Image($r('app.media.ic_public_thumbsup'))
                                .width(20)
                                .height(20)
                                .margin({ left: 5 })
                            }

                          }
                        }
                      }

                    }
                  }
                }
              }
              .padding(10)
              .border({ color: $r('app.color.black'), width: 1, radius: 4 })
              .margin({ top: 10 })
            })
          }
          .height('85%')
          .scrollBar(BarState.Off)
        }

      }.alignItems(HorizontalAlign.Start).margin({
        left: 20,
        right: 20
      })
    }

  }
}