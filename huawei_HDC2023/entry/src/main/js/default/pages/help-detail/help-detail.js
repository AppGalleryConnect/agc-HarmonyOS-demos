import actionjs from '../js/action.js';

export default {
    data: {
        title: '',
        image: '',

        ifDetail: false,
        ifTraffic: true,
        ifCar: false,
        ifFood: false,
        ifHotel: false,
        ifRecomand: false,
        ifQa: false,

        pageType:1,
        carTraffic:'car',
        trafficChoice:[
            {
                id:1,
                name:'机场/高铁'
            },
            {
                id:2,
                name:'酒店 - 东莞篮球中心'
             },
            {
                id:3,
                name:'酒店 - 华为三丫坡'
            },
            {
                id:4,
                name:'自驾',
                name2:'接驳班车'
            },
        ],
        trafficChoiceIndex:0,
        isShow:false,
        address: [],
        regularBus: [],
        traffic: [],
        regularBusAll: [],
        food: [],
        qa: [],
        onShowTime: null //页面显示时的时间
    },
    onInit() {
        this.getMoreServices()
    },
    //页面显示
    onShow() {
        this.onShowTime = new Date().getTime(); //记录页面显示时的时间
    },
    //页面消失
    onHide() {
        var pageName = "";
        switch (this.pageType) {
            case 0:
                pageName = "大会地址";
                break;
            case 1:
                pageName = "大会交通";
                break;
            case 2:
                pageName = "大会餐饮";
                break;
            case 3:
                pageName = "Q&A";
                break;
        }
        actionjs.addPageViewEvent(pageName, this.onShowTime); //上报用户浏览页面事件
    },
    getMoreServices: async function () {
        try {
            console.log("获取更多服务")
            var action = actionjs.initAction(90200);
            var result = await FeatureAbility.callAbility(action);
            var moreServices = JSON.parse(result);
            this.address = moreServices.address
            this.regularBusAll = moreServices.selfService.regularBus
            this.regularBus = moreServices.selfService.regularBus.filter(item => item.Stype == 1)
            this.traffic = moreServices.selfService.traffic
            this.food = moreServices.food
            this.qa = moreServices.qa

        } catch (pluginError) {
            console.error("getMoreServices : Plugin Error = " + pluginError);
        }
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
    showQA(i){
        var arr = []
        for (let index = 0; index < this.qa.length; index++) {
            const element = this.qa[index];
            if (element.index == i) {
                if (element.isFold == 'true') {
                    element.isFold = 'false'
                } else {
                    element.isFold = 'true'
                }
            }else{
                element.isFold = 'true'
            }
            arr.push(element)
        }
        this.qa = arr
    },
    switchTab(pageType) {
        this.pageType = pageType
    },
    choiceTraffic(index){
        this.trafficChoiceIndex = index
        this.regularBus = this.regularBusAll.filter(item => item.Stype ==  (index+1))
    },
    switchTraffic(carTraffic) {
        this.choiceTraffic(0);
        this.carTraffic = carTraffic
    },
    goCar(name) {
        actionjs.addModularClickEvent(name + "-出行（打车）"); //上报用户点击功能按钮事件
        //打开Petal出行
        var action = actionjs.initAction(12346);
        FeatureAbility.callAbility(action);
    }
}
