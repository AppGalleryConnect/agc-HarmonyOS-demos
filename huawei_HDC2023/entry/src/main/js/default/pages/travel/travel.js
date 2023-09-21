import actionjs from '../js/action.js';
export default {
    data(){
        return{
            swiperList:[
                /*{
                    coverImageUrl:'/common/images3/swiper.png'
                },
                {
                    coverImageUrl:'/common/images3/swiper.png'
                },
                {
                    coverImageUrl:'/common/images3/swiper.png'
                }*/
            ],
            menuList:[
                {
                    title:'任务中心',
                    describe:'打卡相关互动获取码力值',
                    img:'/common/images/one-case.png',
                    color:'#EA4A6F',
                    type:1
                },
                {
                    title:'码力商城',
                    describe:'获取码力值可兑换商品',
                    img:'/common/images/two-case.png',
                    color:'#378AF6',
                    type:2
                },
                {
                    title:'美食广场',
                    describe:'可查看更多相关美食',
                    img:'/common/images/three-case.png',
                    color:'#69E9CE',
                    type:3
                },
                {
                    title:'兑换记录',
                    describe:'可查看相关兑换记录',
                    img:'/common/images/four-case.png',
                    color:'#5324C7',
                    type:4
                },
                {
                    title:'我的码力值',
                    describe:'可查看我做任务后获得总码力值',
                    img:'/common/images/five-case.png',
                    color:'#FF794A',
                    type:5
                },
            ],
            cardStatus:1,
            showInteract:false,
            introduce:`又见面了！无所不能的开发者们:\n欢迎来到华为开发者大会2023！\n恭喜你完成签到并获得门票对应的初始码力值，接下来就由你Carry全场了！\n大会期间在各个园区均设有码力互动任务，无论你是萌新还是大神，HDC Together 2023有丰富的宝藏等你解锁，在这里可以和你期待已久的高手过招，有让你乐在其中的“Drama”，“扭一扭”把Buff叠满，还能爆装备哦！\n当然了，获取的码力值最后依然会变成你累计的“财富”, 助力你挖掘更多快乐宝藏，先不说了，游戏开了！\n「码力互动攻略」\n•	签到后登录“华为开发者大会”2023小程序/元服务，即可获得初始码力值\n•	点击小程序/元服务码力互动模块，查看各园区码力任务，可快速获取规则信息\n•	搭载HarmonyOS的手机用户还有专属码力互动任务，打开蓝牙>园区内任务点触发卡片任务>打开小艺建议（Harmonyos3.0及以上）或右滑至负一屏(最新版)，点击卡片参与\n•	完成码力任务将获得相应的码力值，码力值可用于兑换大会主题礼品及美食等\n•	礼品兑换处位于B3咨询处，礼品数量有限，兑完即止\n•	码力值仅在会期内有效，过期作废`,
            hotList:[

            ],
            noSign:false,
            isHW:true,//弹窗内容 默认未签到
            swiperDataIndex:0,
            swiperDataTimer: null, //轮播
            onShowTime: null, //页面显示时的时间
            jumpState : true
        }
    },
    onInit() {

    },
    onShow() {
        this.jumpState = true
        this.onShowTime = new Date().getTime(); //记录页面显示时的时间
        this.getSwiperList()
        this.getTopList()
        if(this.type == 1){
            this.cardStatus = 2;
        }
    },
    //页面消失
    onHide() {
        actionjs.addPageViewEvent("精彩活动列表", this.onShowTime); //上报用户浏览页面事件
    },
    onBackPress(){
        this.back()
    },
    //关闭loading
    cancelDialogByLoadiong(e) {
        this.$element('loadingTravel').close()
    },
    //Swiper轮换事件
    changeSwiper: function (e) {
        this.swiperDataIndex = e.index;
        this.startSwiper();
    },
    //Swiper触摸发生事件
    touchStartSwiper: function (e) {
        this.stopSwiper();
    },
    //Swiper触摸结束事件
    touchEndSwiper: function (e) {
        this.startSwiper();
    },
    //启动Swiper轮换
    startSwiper: function () {
        if (this.swiperList && this.swiperList.length > 0) {
            this.stopSwiper();
            this.swiperDataTimer = setTimeout(() => {
                this.$element('swiper').showNext();
            }, 3000);
        }
    },
    //停止Swiper轮换
    stopSwiper: function () {
        if(this.swiperDataTimer) {
            clearTimeout(this.swiperDataTimer);
        }
    },
    getSwiperList: async function () {
        try {
            this.$element('loadingTravel').show()
            var action = actionjs.initAction(7001);
            var result = await FeatureAbility.callAbility(action);
            if(result){
                var arr = JSON.parse(result)
                if(arr && arr.length > 0){
                    this.swiperList = []
                     for (let i=0;i<arr.length;i++) {
                         let item = arr[i]
                         this.swiperList.push(item)
                     }
                    //启动轮播
                    this.startSwiper();
                }
            }
            this.cancelDialogByLoadiong()
        } catch (pluginError) {
            console.error(" travel getSwiperList Error = " + pluginError);
        }
    },
    getTopList: async function () {
        try {
            var action = actionjs.initAction(7002);
            var result = await FeatureAbility.callAbility(action);
            if(result){
                var arr = JSON.parse(result)
                if(arr && arr.length > 0){
                    this.hotList = []
                    for (let i=0;i<arr.length;i++){
                        let item = arr[i]
                        this.hotList.push(item)
                    }

                }
            }
        } catch (pluginError) {
            console.error(" travel getSwiperList Error = " + pluginError);
        }
    },
    back: function () {
        actionjs.back(0)
    },
    goMore: function (){
        actionjs.go(22)
    },
    choiceCard:function(e){
      this.cardStatus =e
    },
    //跳转导览
    goGuide:function (){
        var action = actionjs.initAction(12345);
        action.data = "index.html";
        FeatureAbility.callAbility(action);
    },
    //添加到桌面
    goCardSelect: function () {
        var concealAction = actionjs.initAction(5001);
        FeatureAbility.callAbility(concealAction);
    },
    go: function (e) {
        actionjs.go(e)
    },
    goDetail: function (e){
        actionjs.goTravelDetail(e.id, false)
    },
    showInteractDialog(){
        this.showInteract = true
        // this.$element('interactDialog').show()
    },
    closeInteractDialog(){
        this.showInteract = false

        // this.$element('interactDialog').close()

    },
    showhintDialog(e) {
        this.$element('hintDialog').show()
    },
    closeDialog(){
        this.$element('hintDialog').close()
    },
    goInte: async function (type){
        try {
            if(!this.jumpState){
                return false
            }
            this.jumpState = false
            var noSign = false;
            var action = actionjs.initAction(8000);
            var result = await FeatureAbility.callAbility(action);
            if (result == 0) {
                var action1 = actionjs.initAction(80000);
                var result1 = await FeatureAbility.callAbility(action1);
                if(result1){
                    if(type == 2){//华为员工不能进入商城
                        if((result1.indexOf("0013")>-1 || result1.indexOf("0014")>-1 || result1.indexOf("0015")>-1) && result1.indexOf("是") == -1){
                            noSign = true;
                            this.isHW = false;
                            this.showhintDialog();
                        }
                        else if(result1.indexOf("已签到") == -1){
                            this.isHW = true;
                            this.showhintDialog();
                            noSign = true;
                        }
                    }else{
                        this.isHW = true;
                        if(result1.indexOf("已签到") == -1){
                            this.showhintDialog();
                            noSign = true;
                        }
                    }
                }

                var adata = ""
                switch (type){
                    case 1:
                        if(!noSign){
                            actionjs.go(26)
                        }
                        break;
                    case 2:
                        adata="integralFA*points_shop*码力商城"
                        break;
                    case 3:
                        adata="integralFA*food_court*美食广场"
                        break;
                    case 4:
                        adata="integralFA*exchange_detail*兑换记录"
                        break;
                    case 5:
                        adata="integralFA*points_detail*我的码力值"
                        break;
                    default :
                        break;
                }

                if(adata && !noSign){
                    var action = actionjs.initAction(12345);
                    //打开网络URL
                    action.data = adata;
                    FeatureAbility.callAbility(action);
                }
                this.jumpState = true
            } else {
                this.go(12)
            }

        } catch (pluginError) {
            console.error("getBatteryLevel : Plugin Error = " + pluginError);
        }
    }
}
