import actionjs from '../js/action.js';
export default {
    data: {
        introduction: {},
        onShowTime: null //页面显示时的时间
    },
    onInit() {
        this.getMoreServices()
        actionjs.addAction('大会介绍')
    },
    //页面显示
    onShow() {
        this.onShowTime = new Date().getTime(); //记录页面显示时的时间
    },
    //页面消失
    onHide() {
        actionjs.addPageViewEvent("大会介绍", this.onShowTime); //上报用户浏览页面事件
    },
    go: function (e) {
    actionjs.go(e)
    },
    back: function () {
        actionjs.back(2)
    },
    onBackPress(){
        this.back()
    },
    /**
     * 获取大会介绍
     */
    getMoreServices: async function () {
        try {
            var action = actionjs.initAction(90200);
            var result = await FeatureAbility.callAbility(action);
            var moreServices = JSON.parse(result);
            this.introduction = moreServices.introduction

        } catch (pluginError) {
            console.error("getMoreServices : Plugin Error = " + pluginError);
        }
    }
}
