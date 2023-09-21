import actionjs from '../js/action.js';
export default {
    data: {
        onShowTime: null //页面显示时的时间
    },
    back: function () {
        actionjs.back(1)
    },
    onBackPress(){
        this.back()
    },
    //页面显示
    onShow() {
        this.onShowTime = new Date().getTime(); //记录页面显示时的时间
    },
    //页面消失
    onHide() {
        actionjs.addPageViewEvent("联系我们", this.onShowTime); //上报用户浏览页面事件
    }
}
