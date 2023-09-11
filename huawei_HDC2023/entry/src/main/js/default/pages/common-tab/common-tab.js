import actionJs from '../js/action.js';

export default {
    props: {
        type: 1 //当前选中
    },
    //跳转页面
    go: function (e) {
        if (this.type != e) {
            switch (e) {
                case 1: //首页
                    e = -3;
                    break;
                case 2: //精彩活动
                    e = 21;
                    break;
                case 3: //议程
                    e = 1;
                    break;
                case 4: //服务
                    e = 2;
                    break;
                case 5: //我的
                    e = 0;
                    break;
                default:
                    e = -3;
                    break;
            }
            actionJs.goIndex(e)
        }
    },
}