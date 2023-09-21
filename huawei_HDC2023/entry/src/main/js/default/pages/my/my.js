import app from '@system.app';
import {HuaweiIdAuthManager} from '@hw-hmscore/hms-jsb-account';
import actionjs from '../js/action.js';

export default {
    data: {
        name: '',
        enterprise: '',
        onShowTime: null //页面显示时的时间
    },
    onInit() {
        this.getData()
        //actionjs.getIdData()
        actionjs.addAction('我的')
    },
    onShow(){
        this.onShowTime = new Date().getTime(); //记录页面显示时的时间
        actionjs.ifLogin()
    },
    //页面消失
    onHide() {
        actionjs.addPageViewEvent("我的", this.onShowTime); //上报用户浏览页面事件
    },
    //返回物理键
    onBackPress() {
        actionjs.go(-3)
        return true;
    },
    getData: async function(){
        try {
            var action = actionjs.initAction(8001);
            var result = await FeatureAbility.callAbility(action);
            console.error(JSON.stringify(result))
            if (result != null) {
                var data = JSON.parse(result)
                this.name = data.name
                this.enterprise = data.enterprise
            }

        } catch (pluginError) {
            console.error("getData : Plugin Error = " + pluginError);
        }
    },
    goTicket: function () {
        actionjs.go(13)
    },
    goContract: function () {
        actionjs.go(24)
    },
    go: function (e) {
        actionjs.go(e)
    },
    back: function () {
        actionjs.back(0)
    },
    goAgenda: function () {
        actionjs.go(14)
    },
    goInte(){
        var action = actionjs.initAction(12345);
        //打开网络URL
        action.data = "integralFA*points_detail*我的码力值";
        FeatureAbility.callAbility(action);
    },
    outLogin:  function () {
        var this_ = this
        this_.$element('logOut').show()
    },
    cancelDialog:function (){
        var this_ = this
        this_.$element('logOut').close()
    },
    confirmDialog:async function (){
        try {
            HuaweiIdAuthManager.getAuthApi().cancelAuthorization().then((result)=>{
                //帐号退出成功
                console.info("cancelAuthorization  success");
                console.info(JSON.stringify(result));
                var action = actionjs.initAction(8003);
                FeatureAbility.callAbility(action);
            }).catch((error) => {
                //帐号退出失败
                console.error("cancelAuthorization  fail");
                console.error(JSON.stringify(error));
                var action = actionjs.initAction(8003);
                 FeatureAbility.callAbility(action);
            });
            this.go(16)
        } catch (pluginError) {
            console.error("getData : Plugin Error = " + pluginError);
        }
    },
    goCardSelect: function () {
        var concealAction = actionjs.initAction(5001);
        FeatureAbility.callAbility(concealAction);
    },
}
