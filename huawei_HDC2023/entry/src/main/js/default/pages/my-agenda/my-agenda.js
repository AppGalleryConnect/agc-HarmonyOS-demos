import actionjs from '../js/action.js';

export default {
    data: {
        agendas: null,
        topBg: 'bg-1',
        selectTab: 0,
        fresh: false,
        info1: '',
        info2: '',
        isNullShow: false,
        sub: null,
        timeIDs: [],
        subTest:[
            {
                groupName:'开发者主题演讲',
                name:'全场景智慧化 迎接产业历史性相遇',
                timeLine:[
                    {
                        rank:0,
                        startTime:'14:40',
                        endTime:'15:10',
                        name:'全场景智慧化 迎接产业历史性相遇',
                        guest:[
                            {
                                name:'David',
                                duty:'华为消费者业务首席战略官'
                            }
                        ]
                    },
                    {
                        rank:1,
                        startTime:'14:40',
                        endTime:'15:10',
                        name:'全场景智慧化 迎接产业历史性相遇',
                        guest:[
                            {
                                name:'David',
                                duty:'华为消费者业务首席战略官'
                            }
                        ]
                    }
                ]
            },
            {
                groupName:'技术论坛',
                timeLine:[
                    { rank:0,
                        startTime:'14:40',
                        endTime:'15:10',
                        name:'全场景智慧化 迎接产业历史性相遇',
                        guest:[
                            {
                                name:'David',
                                duty:'华为消费者业务首席战略官'
                            }
                        ]
                    }
                ]
            },
            {
                groupName:'开发者主题演讲',
                timeLine:[
                    { rank:0,
                        startTime:'14:40',
                        endTime:'15:10',
                        name:'全场景智慧化 迎接产业历史性相遇',
                        guest:[
                            {
                                name:'David',
                                duty:'华为消费者业务首席战略官'
                            }
                        ]
                    }
                ]
            }
        ],
        onShowTime: null //页面显示时的时间
    },
    onInit() {
        actionjs.addAction('我的议程')
        this.getData()
        //this.getTechHourData()
    },
    onShow() {
        this.onShowTime = new Date().getTime(); //记录页面显示时的时间
        this.getSubList()
        actionjs.ifLogin()
    },
    closeInforDialog: function () {
        this.$element('infoDialog').close()
        for (let timeIdsKey in this.timeIds) {
            clearTimeout(timeIdsKey);
        }
        this.timeIds = []
    },
    //页面消失
    onHide() {
        actionjs.addPageViewEvent("我的议程", this.onShowTime); //上报用户浏览页面事件
    },
    //    预订
    showDialog(e) {
        this.createReserve(e)
    },
    cancelDialog(e) {
        this.$element('infoDialog').close()
    },
    goAgendaDetail: function (group,itemId) {
        if (group.groupName == "主题演讲" || group.groupName == "开发者主题演讲") {
            return
        }
        actionjs.goAgendaDetail(itemId)
    },
    refresh: function (e) {
        var that = this;
        that.fresh = e.refreshing;
        this.updata()
    },
    back: function () {
        actionjs.back(1)
    },
    onBackPress() {
        this.back()
    },
    go: function (e) {
        actionjs.go(e)
    },
    changeTab: function (e) {
        this.selectTab = e
        this.sub = null
        switch (this.selectTab) {
            case 1:
                this.topBg = 'bg-2'
                break
            case 2:
                this.topBg = 'bg-3'
                break
            default:
                this.topBg = 'bg-1'
                break
        }
        this.getSubListData(e)
    },
    getSubListData: async function (e) {
        try {
            var action = actionjs.initAction(90071);
            var day = 22

            switch (e) {
                case 1:
                    day = 2
                    break
                case 2:
                    day = 3
                    break
                default:
                    day = 1
                    break
            }

            action.data = {
                day: day
            }
            var result = await FeatureAbility.callAbility(action);
            if (result != null) {
                this.sub = JSON.parse(result)
            }
            this.isNullShow = (this.sub == null)
        } catch (pluginError) {
            console.error("getSubListData : Plugin Error = " + pluginError);
        }


    },
    getData: async function () {
        try {
            var action = actionjs.initAction(9007);
            var result = await FeatureAbility.callAbility(action);
            console.log("1111111xue:" + result)

        } catch (pluginError) {
            console.error("getBatteryLevel : Plugin Error = " + pluginError);
        }
    },
    getTechHourData: async function () {
        try {
            var action = actionjs.initAction(90070);
            var result = await FeatureAbility.callAbility(action);
            console.log("1111111xue:" + result)

        } catch (pluginError) {
            console.error("getBatteryLevel : Plugin Error = " + pluginError);
        }
    },
    updata: function () {
        actionjs.getIdData()
        this.getSubList()
    },
    cancelDialogByLoadiong(e) {
        this.$element('loading').close()
    },
    createReserve: async function (e) {
        this.info1 = ''
        this.info2 = ''
        this.item = e
//        this.$element('loading').show()
        var this_ = this
        try {
            var action = actionjs.initAction(9013);
            action.data = {
                title: this.item.name,
                subSeminarId: this.item.id,
                startTime: this.item.startTimeLong,
                endTime: this.item.endTimeLong
            }

            var result = await FeatureAbility.callAbility(action);
            if (result != null) {
                console.log("xue:" + JSON.stringify(result))
                var back = JSON.parse(result);
                this.updata()
                this.cancelDialogByLoadiong()
                var timeIdNew = setTimeout(function(){
                    clearTimeout(timeIdNew);
                    var timeId = null
                    switch (back.stratus) {
                        case 0:
                            break
                        case -2:
                            break
                        case -3:
                            this_.info1 = '议程即将开始'
                            this_.info2 = '如您感兴趣请直接前往会场参加'
                            this_.$element('infoDialog').show()
                            timeId = setTimeout(this_.closeInforDialog, 2000)
                            break
                        case -4:
                            this_.info1 = '议程正在进行，无法取消'
                            this_.info2 = ''
                            this_.$element('infoDialog').show()
                            timeId = setTimeout(this_.closeInforDialog, 2000)
                            break
                        case -5:
                            this_.info1 = '议程正在进行'
                            this_.info2 = '如您感兴趣请直接前往会场参加'
                            this_.$element('infoDialog').show()
                            timeId = setTimeout(this_.closeInforDialog, 2000)
                            break
                        case -6:
                            this_.info1 = '议程已结束'
                            this_.$element('infoDialog').show()
                            timeId = setTimeout(this_.closeInforDialog, 2000)
                            break
                        case -97:
                            this_.info1 = back.data
                            this_.$element('infoDialog').show()
                            timeId = setTimeout(this_.cancelDialog, 2000)
                            break
                        default:
                            this_.info1 = back.data
                            this_.$element('infoDialog').show()
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
    getSubList: async function () {
        try {
            this.$element('loading').show()
            var action = actionjs.initAction(9011);
            var result = await FeatureAbility.callAbility(action);
            if (result != null) {
                this.cancelDialogByLoadiong()
                this.changeTab(this.selectTab)
                this.fresh = false
            }
        } catch (pluginError) {
            console.error("getBatteryLevel : Plugin Error = " + pluginError);
        }
    },
    //添加到桌面
    goCardSelect: function () {
        var concealAction = actionjs.initAction(5001);
        FeatureAbility.callAbility(concealAction);
    },
}
