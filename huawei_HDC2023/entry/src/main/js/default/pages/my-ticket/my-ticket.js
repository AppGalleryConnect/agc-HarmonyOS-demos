import actionjs from '../js/action.js';
export default {
    data: {
        i_ticketType: '',
        qrCode:'',
        time: '',
        address: '',
        onShowTime: null //页面显示时的时间
    },
    onInit() {
        this.getData()
        actionjs.addAction('我的门票')
    },
    onShow(){
        this.onShowTime = new Date().getTime(); //记录页面显示时的时间
        actionjs.ifLogin()
    },
    //页面消失
    onHide() {
        actionjs.addPageViewEvent("我的门票", this.onShowTime); //上报用户浏览页面事件
    },
    getData: async function(){
        try {
            var action = actionjs.initAction(8001);
            var result = await FeatureAbility.callAbility(action);
            var data = JSON.parse(result)
            this.qrCode = data.qrCode
            this.i_ticketType =data.ticketName
            this.address = data.add
            this.time = data.time
        } catch (pluginError) {
            console.info("getData : Plugin Error = " + pluginError);
        }
    },
    back: function () {
        if (this.isLogin) {
            actionjs.go(11);
        } else {
            actionjs.back(1);
        }
    },
    onBackPress() {
        this.back();
        return true;
    },
    goCardSelect: function () {
        var concealAction = actionjs.initAction(5001);
        FeatureAbility.callAbility(concealAction);
    },
}
