import actionjs from '../js/action.js';

export default {
    data: {
        closeMore:false,
        agendaDetailShow:false,
        showMore: true, //是否显示更多按钮
        lastIntro: '', //上次议程简介
        isClickShowMore: false, //是否显点击更多按钮
        title: '', //议程标题
        day: '', //议程日期
        time: '', //议程时间
        hmHotel: '', //议程地点
        intro: '', //议程简介
        subSeminarId: '', //会议ID
        startTime: '', //开始时间
        endTime: '', //结束时间
        bean: null,
        hmAgenda:[], //日程列表
        isSelect: false, //是否预约
        isShowSelect: true, //是否显示预约
        showStyle1: false, //详情页样式1
        showStyle2: false, //详情页样式2
        showStyle3: true, //详情页样式3
        showStyle4: false, //详情页样式3
        info1: '', //弹窗提示文字
        info2: '', //弹窗提示文字
        timeIDs: [], //预定议程ID集合
        techhourType: false, //是否techhour
        item: {}, //当前预约
        onShowTime: null //页面显示时的时间
    },
    //页面显示
    onShow() {
        this.onShowTime = new Date().getTime(); //记录页面显示时的时间
        //this.initShowMore();
        if (this.id != null) {
            console.error("xue议程详情ID:" + this.id);
            this.queryDetail();
        }
    },
    //页面消失
    onHide() {
        actionjs.addPageViewEvent("议程详情", this.onShowTime); //上报用户浏览页面事件
    },
    //初始化议程简介展开按钮
    initShowMore() {
        if (!this.isClickShowMore) {
            var introText = this.$element('introText');
            if (introText) {
                if (introText.attr && introText.attr.value == this.intro) {
                    var introBoundingClientRect = introText.getBoundingClientRect();
                    if (introBoundingClientRect) {
                        console.info(`introText height is ${introBoundingClientRect.height}`);
                        if (introBoundingClientRect.height > 78) {
                            this.showMore = true;
                        }
                    } else {
                        if (!this.showMore) {
                            setTimeout(this.initShowMore, 100)
                        }
                    }
                } else {
                    setTimeout(this.initShowMore, 100)
                }
            } else {
                if (!this.showMore) {
                    setTimeout(this.initShowMore, 100)
                }
            }
        } else {
            this.showMore = false;
        }
    },
    //初始化
    onInit() {
        if (this.id != null) {
            actionjs.addAgendaDetailAction({
                type: 0,
                id: this.id
            })
        }
    },
    //获取详情数据
    queryDetail: async function () {
        this.$element('loading').show()
        try {
            var action = actionjs.initAction(9012);
            action.data = this.id
            var result = await FeatureAbility.callAbility(action);
            if (result != null) {
                this.bean = JSON.parse(result);
                console.log("xue211111111type:" + this.bean.type)
                this.title = this.bean.hmName
                this.day = this.bean.hmDay
                this.time = this.bean.hmStartAndEndTime
                this.hmHotel = this.bean.hotel
                this.lastIntro = this.intro;
                this.intro = this.bean.intro;
                if (this.intro != this.lastIntro) {
                    this.showMore = false;
                    //this.initShowMore();
                }

                if(this.bean.hmAgenda){
                    this.hmAgenda = this.bean.hmAgenda.map((item)=>{
                        if(item.guests&&item.guests.length!=0){
                            return {...item,guestsHeight:item.guests.length*24+81+'px'}
                        }else{
                            return {...item,guestsHeight:'81px'}
                        }
                    })
                }else{
                    this.bean.hmAgenda=[];
                }


                this.guestList = this.bean.guestList
                this.isSelect = this.bean.isSelect
                this.subSeminarId = this.bean.subSeminarId
                this.startTime = this.bean.startTime
                this.endTime = this.bean.endTime
                this.isShowSelect = this.bean.isShowSelect;
                this.techhourType = this.bean.techhourType;

                switch (this.bean.type) {
                    case 1: //codelabs
                        this.showStyle1 = true
                        this.showStyle2 = false
                        this.showStyle3 = false
                        this.showStyle4 = false
                        break
                    case 2: //互动体验
                        this.showStyle1 = false
                        this.showStyle2 = true
                        this.showStyle3 = false
                        this.showStyle4 = false
                        break
                    case 3: //其它
                        this.showStyle1 = false
                        this.showStyle2 = false
                        this.showStyle3 = true
                        this.showStyle4 = false
                        break
                    case 4: //互动体验
                        this.showStyle1 = false
                        this.showStyle2 = false
                        this.showStyle3 = false
                        this.showStyle4 = true
                        break
                }
            }
            this.agendaDetailShow = true

        } catch (pluginError) {
            console.error("xue2222 : Plugin Error = " + pluginError);
            this.agendaDetailShow = true
        }
        var that=this;
        setTimeout(that.$element('loading').close(),500);
    },
    //跳转
    go: function (e) {
        actionjs.go(e)
    },
    //展开
    openIntro: function (e) {
        this.isClickShowMore = true;
        this.showMore = false;
        this.closeMore = true;
    },
    closeIntro: function (e) {
        this.isClickShowMore = false;
        this.showMore = true;
        this.closeMore = false;
    },
    //预订
    showDialog: function (e) {
        console.error("xue:去预订")
        this.item.title = this.title; //标题
        this.item.subSeminarId = this.subSeminarId; //会议ID
        this.item.startTime = this.startTime; //开始时间
        this.item.endTime = this.endTime; //结束时间
        this.item.isDay = false; //是否日程
        this.item.hotel = this.hmHotel; //地点
        this.item.intro = this.intro; //议程简介
        this.createReserve(e);
    },
    //关闭消息弹窗
    closeInforDialog: function () {
        this.$element('infoDialog').close()
        for (let timeIdsKey in this.timeIds) {
            clearTimeout(timeIdsKey);
        }
        this.timeIds = []
    },
    //关闭弹窗
    cancelDialog(e) {
        this.$element('orderDialog').close()
        this.$element('loginDialog').close()
        this.$element('infoDialog').close()
        this.cancelDialogByLoadiong()
    },
    //设置权限
    setPermission: async function () {
        var this_ = this;
        try {
            var action = actionjs.initAction(8004);
            var result = await FeatureAbility.callAbility(action);
            var timeId = setTimeout(function () {
                this_.$element('setDialog').close()
                clearTimeout(timeId);
            }, 500);
            if (result == "OK") {
                //已授权
                this_.info1 = "您已授权，无需再次授权";
                this_.$element('infoDialog').show();
            }
            if (result == "ERROR") {
                //用户禁止授权
                this_.info1 = "请您在设置-服务管理";
                this_.info2 = "打开元服务添加日历权限";
                this_.$element('infoDialog').show();
            }
        } catch (pluginError) {
            console.error("getBatteryLevel : Plugin Error = " + pluginError);
        }
    },
    //添加日历
    addCalendar: async function () {
        var this_ = this;
        try {
            var action = actionjs.initAction(8002);
            action.data = {
                title: this.item.title,
                location: this.item.hotel,
                description: this.item.intro,
                startTime: this.item.startTime,
                endTime: this.item.endTime
            }
            var result = await FeatureAbility.callAbility(action);
            if (result == "OK") {
                console.log("addCalendar展示弹窗");
                this.$element('setDialog').show()
            } else if (result == "ERROR") {
                console.log("addCalendar用户拒绝授权");
                //用户禁止授权
                this_.info1 = "请您在设置-服务管理";
                this_.info2 = "打开元服务添加日历权限";
                this_.$element('infoDialog').show();
            } else if (result == "ADD_ERROR") {
                console.log("addCalendar添加日历失败");
                this_.info1 = "添加日历失败，请重试";
                this_.$element('infoDialog').show();
            } else {
                this_.info1 = "已将议程添加到您的日历";
                this_.$element('infoDialog').show();
                this_.closeOrderDialog();
                var timeId1 = setTimeout(function () {
                    this_.closeInforDialog();
                    clearTimeout(timeId1);
                }, 2500);
            }
            console.log("xue:" + result)
        } catch (pluginError) {
            console.error("getBatteryLevel : Plugin Error = " + pluginError);
        }
    },
    //预约
    createReserve: async function (e) {
        this.$element('loading').show()
        var this_ = this
        try {
            this.info1 = ''
            this.info2 = ''
            console.error("xue:去预订1")
            var action = actionjs.initAction(9013);
            action.data = {
                title: this.item.title,
                subSeminarId: this.item.subSeminarId,
                startTime: this.item.startTime,
                endTime: this.item.endTime,
                isDay: this.item.isDay, //是否日程
                agendaId: this.item.agendaId //议程ID
            }

            var result = await FeatureAbility.callAbility(action);

            if (result != null) {
                var back = JSON.parse(result);
                this.cancelDialogByLoadiong()
                var timeIdNew = setTimeout(function () {
                    clearTimeout(timeIdNew);
                    var timeId = null
                    switch (back.stratus) {
                        case 0:
                            this_.$element('orderDialog').show()
                            this_.queryDetail()
                            break
                        case -2:
                            this_.$element('loginDialog').show()
                            break
                        case -3:
                            this_.info1 = '议程即将开始，'
                            this_.info2 = '如您感兴趣请直接前往会场参加'
                            this_.$element('infoDialog').show()
                            break
                        case -4:
                            this_.info1 = '议程正在进行，无法取消'
                            this_.info2 = ''
                            this_.$element('infoDialog').show()
                            break
                        case -5:
                            this_.info1 = '议程正在进行，'
                            this_.info2 = '如您感兴趣请直接前往会场参加'
                            this_.$element('infoDialog').show()
                            break
                        case -6:
                            this_.info1 = '该议程已结束'
                            this_.info2 = '请关注其他议程'
                            this_.$element('infoDialog').show()
                            break
                        case -97:
                            this_.info1 = back.data
                            this_.$element('infoDialog').show()
                            this_.queryDetail()
                            timeId = setTimeout(this_.cancelDialog, 2000)
                            break
                        default:
                            this_.info1 = back.data
                            this_.$element('infoDialog').show()
                            this_.queryDetail()
                            timeId = setTimeout(this_.closeInforDialog, 2000)
                            break
                    }

                    if (timeId != null) {
                        this_.timeIds.put(timeId)
                    }
                }, 500)
            }
        } catch (pluginError) {
            console.error("createReserve : Plugin Error = " + pluginError);
        }
    },
    //取消Loadiong
    cancelDialogByLoadiong(e) {
        this.$element('loading').close()
    },
    //添加到桌面
    goCardSelect: function () {
        actionjs.addModularClickEvent("议程详情-添加至桌面"); //上报用户点击功能按钮事件
        var concealAction = actionjs.initAction(5001);
        FeatureAbility.callAbility(concealAction);
    },
    //预订日程
    showDialogItem: function (e) {
        console.error("xue:去预订日程")
        this.item.subSeminarId = this.subSeminarId; //会议ID
        this.item.isDay = true; //是否日程
        this.item.agendaId = e.agendaId; //议程ID
        this.item.startTime = e.startTimeLong; //开始时间
        this.item.endTime = e.endTimeLong; //结束时间
        this.item.title = e.name; //标题
        this.item.hotel = this.hmHotel; //地点
        this.item.intro = this.intro; //议程简介
        this.createReserve(e);
    }
}