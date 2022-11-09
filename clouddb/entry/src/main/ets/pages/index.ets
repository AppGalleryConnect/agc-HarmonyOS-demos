import Mine from '../component/Mine';
import UserLogin from '../component/UserLogin';
import Add from '../component/Add';
import Books from '../component/Books';

@Entry
@Component
struct Index {
  @State currentIndex: number = 0
  @State Build: Array<Object> = [
    {
      icon: $r('app.media.app_icon'),
      icon_after: $r('app.media.app_icon'),
      text: 'list',
      num: 0
    },
    {
      icon: $rawfile('addMore.svg'),
      icon_after: $rawfile('addMore.svg'),
      text: 'add',
      num: 1
    },
    {
      icon: $rawfile('mine.svg'),
      icon_after: $rawfile('mine.svg'),
      text: 'mine',
      num: 2
    }
  ]

  @Builder NavigationTitle() {
    Column() {
      Text('Demo')
        .width(80)
        .height(60)
        .fontColor(Color.Blue)
        .fontSize(20)
    }
    .onClick(() => {
      console.log("title")
    })
  }

  @Builder NavigationToolbar() {
    Row() {
      ForEach(this.Build, item => {
        Column() {
          Image(this.currentIndex == item.num ? item.icon_after : item.icon)
            .width(25)
            .height(25)
          Text(item.text)
            .fontColor(this.currentIndex == item.num ? "#ff7500" : "#000000")
        }
        .onClick(async () => {
          this.currentIndex = item.num;
        })
        .margin({ left: 40, right: 40 })
      })
    }
    .alignItems(VerticalAlign.Center)
    .justifyContent(FlexAlign.Center)
    .width('100%')
  }

  build() {
    Column() {
      Navigation() {
        if (this.currentIndex === 1) {
          Add()
        } else if (this.currentIndex === 2) {
          UserLogin({ currentIndexRef: $currentIndex, BuildRef: $Build })
        } else if (this.currentIndex === 3) {
          Mine()
        } else {
          Books()
        }
      }
      .title(this.NavigationTitle)
      .titleMode(NavigationTitleMode.Mini)
      .hideTitleBar(false)
      .hideBackButton(true)
      .toolBar(this.NavigationToolbar)
    }
    .height('100%')
  }
}