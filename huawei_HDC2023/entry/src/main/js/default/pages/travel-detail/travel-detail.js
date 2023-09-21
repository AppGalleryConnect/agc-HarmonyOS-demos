import actionjs from '../js/action.js';
export default {
    data(){
        return{
            swiperList:[
                /*{
                    coverImageUrl:'/common/images3/swiper.png'
                }, {
                    coverImageUrl:'/common/images3/swiper.png'
                }, {
                    coverImageUrl:'/common/images3/swiper.png'
                }*/
            ],
            //语音讲解列表
            voiceList:[
                /*{
                    title:'三丫坡风景001',
                    tag:'语音讲解'
                }, {
                    title:'三丫坡风景001',
                    tag:'语音讲解'
                }*/
            ],
            //风景简介
            introduce:'',//'HUAWEI HiAI是面向智能终端的AI能力开放平台，基于 “芯、端、云”三层开放架构，即芯片能力开放、应用能力开放、服务能力开放，构筑全面开放的智慧生态，让开发者能够快速地利用华为强大的AI处理能力，为用户提供更好的智慧应用体验。',
            title: '',
            address: '',
            videoList: [],
            swiperDataIndex:0,
            swiperDataTimer: null, //轮播
            playStatusTimer:null,//播放状态查询定时器
            onShowTime: null //页面显示时的时间
        }
    },
    onInit() {

    },
    onShow(){
        this.onShowTime = new Date().getTime(); //记录页面显示时的时间
        this.getDetailInfo()
    },
    //页面消失
    onHide() {
        actionjs.addPageViewEvent("精彩活动详情", this.onShowTime); //上报用户浏览页面事件
    },
    onBackPress(){
        this.back()
    },
    //关闭loading
    cancelDialogByLoadiong(e) {
        this.$element('loading').close()
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
    getDetailInfo: async function() {
        if(this.id) {
            this.$element('loading').show()
            try {
                var action = actionjs.initAction(7003);
                action.data = this.id + ''
                var result = await FeatureAbility.callAbility(action);
                if(result){
                    var detailInfo = JSON.parse(result)
                    if(detailInfo.articleCategoryId) {
                        // 轮播
                        var imgArr=detailInfo.showImagesRealPath
                        this.swiperList = []
                        if(imgArr.length > 0) {
                            this.swiperList= imgArr
                        }
                        // 标题
                        if(detailInfo.title){
                            this.title = detailInfo.title.split('|')[0] || ''
                            this.address = detailInfo.title.split('|')[1] || ''
                        }
                        // 介绍
                        this.introduce = detailInfo.content.replace(/<\/?.+?\/?>/g, "").replace("&amp;","&")
                        // 语音/视频
                        if(detailInfo.summary) {
                            let voiceArr = detailInfo.summary.split('|')
                            this.voiceList = []
                            for (let i = 0; i < voiceArr.length; i++) {
                                let voiceStr = voiceArr[i]
                                this.voiceList.push({title: voiceStr.split('#')[0], tag:'语音讲解', url: voiceStr.split('#')[1],isNowPlaying:""})
                            }
                            /*let videoArr  = detailInfo.summary.split('$')[1].split('|')
                            for (let i = 0; i < videoArr.length; i++) {
                                let item = videoArr[i]
                                this.videoList.push(item)
                            }*/
                        }
                        //启动轮播
                        this.startSwiper();
                    }
                }
                this.cancelDialogByLoadiong()
            } catch (pluginError) {
                console.error(" travel getSwiperList Error = " + pluginError);
            }
        }
    },
    //返回
    back: function () {
        if(this.isMore) {
            actionjs.back(22)
        } else {
            actionjs.back(21)
        }
    },
    //添加到桌面
    goCardSelect: function () {
        var concealAction = actionjs.initAction(5001);
        FeatureAbility.callAbility(concealAction);
    },
    //播放语音
    playAudio: function(item) {
        actionjs.addModularClickEvent("精彩活动详情-语音讲解"); //上报用户点击功能按钮事件
        var action = actionjs.initAction(7005);
        action.data = item.url;
        FeatureAbility.callAbility(action);
        //启动播放状态查询定时器
        if (this.playStatusTimer) {
            clearInterval(this.playStatusTimer);
        }
        this.playStatusTimer = setInterval(() => {
            this.queryPlayStatus(item);
        }, 100);
        for (let i = 0; i < this.voiceList.length; i++) {
            if (item.title == this.voiceList[i].title  && item.url == this.voiceList[i].url) {
                this.voiceList[i].isNowPlaying = "1";
            } else {
                this.voiceList[i].isNowPlaying = "";
            }
        }
    },
    onDestroy() {
        //页面销毁结束语音服务
        var action = actionjs.initAction(7006);
        FeatureAbility.callAbility(action);
    },
    //查询语音播放状态
    queryPlayStatus: async function(item) {
        var action = actionjs.initAction(7007);
        var result = await FeatureAbility.callAbility(action);
        if (result) {
            for (let i = 0; i < this.voiceList.length; i++) {
                if (item.title == this.voiceList[i].title  && item.url == this.voiceList[i].url) {
                    this.voiceList[i].isNowPlaying = result == "true"?"1":"";
                } else {
                    this.voiceList[i].isNowPlaying = "";
                }
            }
        }
    }
}