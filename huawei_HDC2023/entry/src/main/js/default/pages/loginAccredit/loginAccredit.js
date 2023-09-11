import prompt from '@system.prompt'
import {HuaweiIdAuthParamsHelper, HuaweiIdAuthManager, Scope} from '@hw-hmscore/hms-jsb-account';
import actionjs from '../js/action.js';

export default {
    data: {
        title: "",
        getCodeInfo: "获取验证码",
        time: 60,
        phoneText: "",
        codeText: "",
        isSelect: false,
        conceal: true,
        onShowTime: null //页面显示时的时间
    },
    onInit() {
        this.title = this.$t('strings.world');
        this.$set()
        actionjs.addAction('登录')
        this.checkConceal()
    },
    //页面显示
    onShow() {
        this.onShowTime = new Date().getTime(); //记录页面显示时的时间
    },
    //页面消失
    onHide() {
        actionjs.addPageViewEvent("登录", this.onShowTime); //上报用户浏览页面事件
    },
    showLoading() {
        this.$element('loading').show()
    },
    closeLoading() {
        this.$element('loading').close()
    },
    go: function (e) {
        actionjs.go(e)
    },
    back: function () {
        this.menu = false
        actionjs.back(0)
    },
    onBackPress(){
        this.back()
    },
    showTime: function () {
        const _this = this;
        _this.time = _this.time - 1;
        _this.getCodeInfo = _this.time + "秒后重新获取"
        var timer = setInterval(function () {
            _this.time = _this.time - 1;
            if (_this.time == 0) {
                _this.time = 60;
                _this.getCodeInfo = "重新获取验证码";
                clearInterval(timer);
            } else {
                _this.getCodeInfo = _this.time + "秒后重新获取"
            }

        }, 1000);
    },
    phoneChange: function (e) {
        console.log("xue::" + e.value)
        this.phoneText = e.value

    },
    codeChange: function (e) {
        console.log("xue::" + e.value)
        this.codeText = e.value

    },
    login: function () {
        actionjs.goLoginNew(this.goUrl)
    },
    loginHW: function (){
        if (!this.isSelect) {
            this.showToast('您尚未同意隐私声明');
        } else {
            var signInOption = new HuaweiIdAuthParamsHelper().setAuthorizationCode().setScopeList([new Scope("https://www.huawei.com/auth/account/mobile.number")]).build();
            console.info(signInOption);
            // HuaweiIdAuthManager.getAuthApi方法返回huaweiIdAuth对象，在huaweiIdAuth对象中调用huaweiIdAuth.getSignInIntent方法
            HuaweiIdAuthManager.getAuthApi().getSignInIntent(signInOption).then(async (result) => {
                this.showLoading();
                var action = actionjs.initAction(99);
                action.data = {
                    phone: result.serverAuthCode
                }
                var cResult = await FeatureAbility.callAbility(action);
                if (cResult == 'success') {
                    if (this.goUrl) {
                        this.go(this.goUrl);
                    } else {
                        this.go(11);
                    }
                } else if (cResult == '未购票') {
                    setTimeout(this.showBuyTicketDialog, 500)
                } else {
                    this.showToast(cResult);
                }
                // 登录成功，获取用户的华为帐号信息
                console.info("signIn success");
                console.info(JSON.stringify(result));
                this.closeLoading();
            }).catch((error) => {
                // 登录失败
                console.error("signIn fail");
                console.error(JSON.stringify(error));
                this.closeLoading();
            });
        }
    },
    showToast: function (msg) {
        this.closeLoading();
        prompt.showToast({
            message: msg
        });
    },
    checkConceal: async function () {
        var concealAction = actionjs.initAction(6000);
        var concealResult = await FeatureAbility.callAbility(concealAction);
        if (concealResult != 'true') {
            this.conceal =false
            this.showDilog();
        }


    },
    agreeConceal:async function() {
        this.isSelect = true
        this.closeConcealDialog()
        if (!this.conceal) {
            var action = actionjs.initAction(6001);
            FeatureAbility.callAbility(action);
            this.conceal = true
        }
    },
    disagreeConceal:async function() {
        this.isSelect = false
        this.closeConcealDialog()
        if (!this.conceal) {
            var action = actionjs.initAction(6002);
            FeatureAbility.callAbility(action);
        }
    },
    showDilog: function (e) {
        this.$element('concealDialog').show()
    },
    closeConcealDialog() {
        this.$element('concealDialog').close()
    },
    closeBuyTicketDialog() {
        this.$element('noBuyTicket').close()
        HuaweiIdAuthManager.getAuthApi().cancelAuthorization().then((result)=>{
            // 帐号取消授权成功
            console.info("cancelAuthorization success");
            console.info(JSON.stringify(result));
        }).catch((error) => {
            // 帐号取消授权失败
            console.error("cancelAuthorization fail");
            console.error(JSON.stringify(error));
        });
    },
    showBuyTicketDialog() {
        this.$element('noBuyTicket').show()
    },
    setSelect() {
        this.isSelect = !this.isSelect
    },
}
