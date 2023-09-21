import actionjs from '../js/action.js';

export default {
    data() {
        return {
            title: 'World',
            cooperativePartner: [],
            onShowTime: null //页面显示时的时间
        }
    },
    onInit() {
        this.getPartnerList()
    },
    //页面显示
    onShow() {
        this.onShowTime = new Date().getTime(); //记录页面显示时的时间
    },
    //页面消失
    onHide() {
        actionjs.addPageViewEvent("合作伙伴", this.onShowTime); //上报用户浏览页面事件
    },
    go: function (e) {
        actionjs.go(e)
    },
    back: function () {
        actionjs.back(0)
    },
    onBackPress() {
        this.back()
    },
    getPartnerList: async function () {
        try {
            var action = actionjs.initAction(5000);
            action.data = "否"
            var result = await FeatureAbility.callAbility(action);
            this.cooperativePartner = JSON.parse(result);
        } catch (pluginError) {
            console.error("getPartnerList Error = " + pluginError);
        }
    }
}