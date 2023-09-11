import actionjs from '../js/action.js';

export default {
    data: {
        title: '',
        onShowTime: null //页面显示时的时间
    },
    onInit() {
        actionjs.addAction('服务')
    },
    //页面显示
    onShow() {
        this.onShowTime = new Date().getTime(); //记录页面显示时的时间
    },
    //页面消失
    onHide() {
        actionjs.addPageViewEvent("更多服务", this.onShowTime); //上报用户浏览页面事件
    },
    go: function (e) {
        actionjs.go(e)
    },
    back: function () {
        actionjs.back(0)
    },
    onBackPress(){
        this.back()
    },

    //添加到桌面
    goCardSelect: function () {
        var concealAction = actionjs.initAction(5001);
        FeatureAbility.callAbility(concealAction);
    },
    //跳转导览
    goGuide:function (){
        var action = actionjs.initAction(12345);
        action.data = "index.html";
        FeatureAbility.callAbility(action);
    },
}
