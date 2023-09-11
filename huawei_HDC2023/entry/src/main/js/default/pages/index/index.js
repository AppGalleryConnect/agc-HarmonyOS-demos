import app from '@system.app';
import actionjs from '../js/action.js';

export default {
    data() {
        return {
            indexImgs:[],
            indexImgsTimer:null,
            guestImgs: [], //嘉宾列表
            subArtivleList: [], //议程列表
            cooperativePartner: [], //合作伙伴
            currentIndex: 0, //嘉宾索引
            agendaDataIndex: 0, //议程索引
            cooperativePartnerIndex: 0, //合作伙伴索引
            agendaDataTimer: null, //议程轮播
            cooperativePartnerTimer: null, //合作伙伴轮播
            currentTimer: null, //嘉宾轮播
            onShowTime: null, //页面显示时的时间
            ifLogin:false
        }
    },
    //页面初始化
    onInit() {
        this.checkLogin();
        this.getIndexImgs();
        this.getGuestimgs();
        this.getsubArticleList();
        actionjs.addAction('首页');
        this.getCooperativePartner();
    },
    //页面显示
    onShow() {
        this.onShowTime = new Date().getTime(); //记录页面显示时的时间
    },
    //页面消失
    onHide() {
        actionjs.addPageViewEvent("首页", this.onShowTime); //上报用户浏览页面事件
    },
    //获取顶部轮播图
    getIndexImgs: async function () {
        try {
            var action = actionjs.initAction(17007);
            var result = await FeatureAbility.callAbility(action);
            if(result){
                var arr = JSON.parse(result)
                if(arr && arr.length > 0){
                    this.indexImgs = []
                    for (let i=0;i<arr.length;i++) {
                        let item = arr[i]
                        this.indexImgs.push(item)
                    }
                    //启动KV-Swiper轮换
                    this.startKvSwiper();
                }
            }
        } catch (pluginError) {
            console.error(" index getIndexImgs Error = " + pluginError);
        }
    },
    goImageDetail:function(e){
        try {
            if(e.isUseLinkUrl == 1) {
                var action = actionjs.initAction(12345);
                //打开网络URL
                action.data = "indexImg*"+e.title+"*"+e.url;
                FeatureAbility.callAbility(action);
            }
        } catch (pluginError) {
            console.error("goImageDetail : Plugin Error = " + pluginError);
        }
    },
    //获取合作伙伴数据
    getCooperativePartner: async function () {
        try {

            var action = actionjs.initAction(5000);
            action.data = "是";
            var result = await FeatureAbility.callAbility(action);
            this.cooperativePartner = JSON.parse(result);

            //启动合作伙伴轮播
            this.startCooperativePartnerSwiper();

            console.info(result);
        } catch (pluginError) {
            console.error("getGuestimgs : Plugin Error = " + pluginError);
        }
    },
    //打开隐私协议弹窗
    showConcealDialog() {
        this.$element('concealDialog').show()
    },
    //关闭隐私协议弹窗
    closeConcealDialog() {
        this.$element('concealDialog').close()
    },
    //同意隐私协议弹窗
    agreeConceal() {
        var action = actionjs.initAction(6001);
        FeatureAbility.callAbility(action);
        this.closeConcealDialog();
    },
    //拒绝隐私协议弹窗
    disagreeConceal() {
        var action = actionjs.initAction(6002);
        FeatureAbility.callAbility(action);
    },
    //返回物理键
    onBackPress() {
        app.terminate();
        return true;
    },
    checkLogin: async function(){
        try {
            var action = actionjs.initAction(8000);

            var result = await FeatureAbility.callAbility(action);

            if (result == 0) {

            }else{
                this.ifLogin = true
            }
        } catch (pluginError) {
            console.error("checkLogin  index : Plugin Error = " + pluginError);
        }
    },
    //获取嘉宾列表
    getGuestimgs: async function () {
        try {
            console.error("xue获取嘉宾")
            var action = actionjs.initAction(90081);
            action.data = {
                isPublic: "是"
            }
            var result = await FeatureAbility.callAbility(action);
            this.guestImgs = JSON.parse(result);
            console.error("fuck =======");
            console.error(JSON.stringify(this.guestImgs));
            console.info(result);

            //启动嘉宾轮播
            this.startGuestSwiper();
        } catch (pluginError) {
            console.error("getGuestimgs : Plugin Error = " + pluginError);
        }
    },
    //KV-Swiper轮换事件
    changeKv: function (e) {
        this.startKvSwiper();
    },
    //KV-Swiper触摸发生事件
    touchStartKv: function (e) {
        this.stopKvSwiper();
    },
    //KV-Swiper触摸结束事件
    touchEndKv: function (e) {
        this.startKvSwiper();
    },
    //启动KV-Swiper轮换
    startKvSwiper: function () {
        if (this.indexImgs && this.indexImgs.length > 0) {
            this.stopKvSwiper();
            this.indexImgsTimer = setTimeout(() => {
                this.$element('swiper2').showNext();
            }, 3000);
        }
    },
    //停止KV-Swiper轮换
    stopKvSwiper: function () {
        if (this.indexImgsTimer) {
            clearTimeout(this.indexImgsTimer);
        }
    },
    //议程Swiper轮换事件
    changeAgenda: function (e) {
        this.agendaDataIndex = e.index;
        this.startAgendaSwiper();
    },
    //议程Swiper触摸发生事件
    touchStartAgenda: function (e) {
        this.stopAgendaSwiper();
    },
    //议程Swiper触摸结束事件
    touchEndAgenda: function (e) {
        this.startAgendaSwiper();
    },
    //启动议程Swiper轮换
    startAgendaSwiper: function () {
        if (this.subArtivleList && this.subArtivleList.length > 0) {
            this.stopAgendaSwiper();
            this.agendaDataTimer = setTimeout(() => {
                this.$element('swiper').showNext();
            }, 5000);
        }
    },
    //停止议程Swiper轮换
    stopAgendaSwiper: function () {
        if (this.agendaDataTimer) {
            clearTimeout(this.agendaDataTimer);
        }
    },
    //嘉宾Swiper轮换事件
    changeGuest: function (e) {
        this.currentIndex = e.index;
        this.startGuestSwiper();
    },
    //嘉宾Swiper触摸发生事件
    touchStartGuest: function (e) {
        this.stopGuestSwiper();
    },
    //嘉宾Swiper触摸结束事件
    touchEndGuest: function (e) {
        this.startGuestSwiper();
    },
    //启动嘉宾Swiper轮换
    startGuestSwiper: function () {
        if (this.guestImgs && this.guestImgs.length > 0) {
            this.stopGuestSwiper();
            this.currentTimer = setTimeout(() => {
                this.$element('swiper3').showNext();
            }, 4000);
        }
    },
    //停止嘉宾Swiper轮换
    stopGuestSwiper: function () {
        if (this.currentTimer) {
            clearTimeout(this.currentTimer);
        }
    },
    //合作伙伴Swiper轮换事件
    changeCooperativePartner: function (e) {
        this.cooperativePartnerIndex = e.index;
        this.startCooperativePartnerSwiper();
    },
    //合作伙伴Swiper触摸发生事件
    touchStartCooperativePartner: function (e) {
        this.stopCooperativePartnerSwiper();
    },
    //合作伙伴Swiper触摸结束事件
    touchEndCooperativePartner: function (e) {
        this.startCooperativePartnerSwiper();
    },
    //启动合作伙伴Swiper轮换
    startCooperativePartnerSwiper: function () {
        if (this.cooperativePartner && this.cooperativePartner.length > 0) {
            this.stopCooperativePartnerSwiper();
            this.cooperativePartnerTimer = setTimeout(() => {
                this.$element('swiper4').showNext();
            }, 3000);
        }
    },
    //停止合作伙伴Swiper轮换
    stopCooperativePartnerSwiper: function () {
        if (this.cooperativePartnerTimer) {
            clearTimeout(this.cooperativePartnerTimer);
        }
    },
    //打开嘉宾详情
    goGuestDetail: function (e, num) {
        actionjs.goGuestDetail(e, num);
    },
    //获取议程列表
    getsubArticleList: async function () {
        try {
            var action = actionjs.initAction(9005);
            var result = await FeatureAbility.callAbility(action);
            this.subArtivleList = JSON.parse(result);

            //不同类型赋值不同样式
            if (this.subArtivleList) {
                for (var i = 0;i < this.subArtivleList.length; i++) {
                    switch (this.subArtivleList[i].m_meetingTypeCn) {
                        case "主题演讲":
                            this.subArtivleList[i].m_meetingTypeNum = 0;
                            break;
                        case "开发者主题演讲":
                            this.subArtivleList[i].m_meetingTypeNum = 1;
                            break;
                        case "技术论坛":
                            this.subArtivleList[i].m_meetingTypeNum = 2;
                            break;
                        case "HarmonyOS生态峰会":
                        case "鸿蒙生态峰会":
                            this.subArtivleList[i].m_meetingTypeNum = 3;
                            this.subArtivleList[i].m_meetingTypeCn="鸿蒙生态峰会";
                            break;
                        case "Codelabs":
                            this.subArtivleList[i].m_meetingTypeNum = 4;
                            break;
                        case "Tech. Hour":
                            this.subArtivleList[i].m_meetingTypeNum = 5;
                            break;
                        case "HarmonyOS极客马拉松":
                            this.subArtivleList[i].m_meetingTypeNum = 7;
                            break;
                        default:
                            this.subArtivleList[i].m_meetingTypeNum = 6;
                            break;
                    }
                }
            }

            //启动议程轮播
            this.startAgendaSwiper();

            console.log("getsubArticleListxue: = " + JSON.stringify(this.subArtivleList));
        } catch (pluginError) {
            console.error("getsubArticleList : Plugin Error = " + pluginError);
        }

        //检查隐私协议
        var concealAction = actionjs.initAction(6000);
        var concealResult = await FeatureAbility.callAbility(concealAction);
        if (concealResult != 'true') {
            this.showConcealDialog();
        }
    },
    //跳转页面
    go: function (e) {
        if(e==21){
            actionjs.addModularClickEvent("首页-三丫坡风光-查看详情"); //上报用户点击功能按钮事件
        }
        actionjs.go(e)
    },
    //议程详情
    goAgendaDetail: function (e) {
        if (e.m_meetingTypeCn == "主题演讲" || e.m_meetingTypeCn == "开发者主题演讲") {
            actionjs.goAgendaList(e);
        }else{
            actionjs.goAgendaDetail(e.subSeminarId)
        }
    },
    //添加到桌面
    goCardSelect: function () {
        actionjs.addModularClickEvent("首页-添加至桌面"); //上报用户点击功能按钮事件
        var concealAction = actionjs.initAction(5001);
        FeatureAbility.callAbility(concealAction);
    },
    //打开Petal出行
    goPetal: function () {
        var action = actionjs.initAction(12346);
        FeatureAbility.callAbility(action);
    },
    //打开我的门票
    goMyTicket: function () {
        if (this.ifLogin) {
            actionjs.goLogin(131);
        } else {
            actionjs.go(13);
        }
    }
}