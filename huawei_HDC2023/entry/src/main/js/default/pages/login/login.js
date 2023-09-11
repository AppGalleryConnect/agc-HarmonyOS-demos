import prompt from '@system.prompt'
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
        this.goLogin()

    },
    sendCode: async function(){
        try {
            console.error("xue获取验证码")
            if (this.getCodeInfo.indexOf("秒后") != -1) {
                return
            }

            if (!(/^1[3456789]\d{9}$/.test(this.phoneText))) {
                this.showToast('请输入正确格式的手机号');
            } else {
                this.showLoading();

                this.showTime();
                var action = actionjs.initAction(9001);
                action.data = {
                    phone: this.phoneText
                }
                var result = await FeatureAbility.callAbility(action);

            }
        } catch (pluginError) {
            console.error("getBatteryLevel : Plugin Error = " + pluginError);
        } finally {
            this.closeLoading();
        }
    },
    goLogin: async function(){
        this.showLoading();
        if (!(/^1[23456789]\d{9}$/.test(this.phoneText))) {
            this.showToast('请输入正确格式的手机号');
        } else if (this.codeText == "") {
            this.showToast('验证码不得为空');
        } else if (!this.isSelect) {
            this.showToast('您尚未同意隐私声明');
        } else {
            var hasClose = false;
            try {
                var action = actionjs.initAction(9002);
                action.data = {
                    phone: this.phoneText,
                    code: this.codeText
                }
                var result = await FeatureAbility.callAbility(action);
                if (result == 'success') {
                    if(this.goUrl){
                        this.go(this.goUrl);
                    }else{
                        this.go(11);
                    }
                } else if (result == '未购票') {
                    setTimeout(this.showBuyTicketDialog, 500)
                } else {
                    this.showToast(result);
                }
            } catch (pluginError) {
                console.error("getBatteryLevel : Plugin Error = " + pluginError);
            } finally {
                console.error("######" + hasClose);
                this.closeLoading();
            }
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
    },
    showBuyTicketDialog() {
        this.$element('noBuyTicket').show()
    },
    setSelect() {
        this.isSelect = !this.isSelect
    },
}
