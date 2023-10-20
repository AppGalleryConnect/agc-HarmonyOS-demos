import TabBar from '../view/TabBar';
import { FULL_WIDTH } from '../common/constant/CommonConstant';

@Entry
@Component
struct Index {
  build() {
    Column() {
      TabBar()
    }
    .width(FULL_WIDTH)
    .backgroundColor($r('app.color.listColor'))
    .justifyContent(FlexAlign.Center)
  }
}