import actionjs from '../js/action.js';

export default {
    data() {
        return {
            searchText: '', //搜索框的值
            treeR: null, //话题
            treeP: null, //我是
            selectTab: '',
            isNullShow: false, //是否无数据
            isNullText: '本日暂无内容', //暂无数据提示
            info1: '', //弹窗内容
            info2: '', //弹窗内容
            item: null, //预定的议程
            timeIDs: [], //预定议程ID集合
            data: [], //议程数据
            copyData: [], //复制议程数据
            selectCategory: '', //选中类型
            fresh: false, //是否正在刷新
            onShowTime: null //页面显示时的时间
        }
    },
    //初始化
    onInit() {
        //this.getDicP()
        this.getDicR()
        this.initData();

    },
    //下拉刷新
    refresh: function (e) {
        this.fresh = e.refreshing;
        this.updata();
    },
    //页面消失
    onHide() {
        actionjs.addPageViewEvent("议程列表", this.onShowTime); //上报用户浏览页面事件
        this.$element('modal-shaixuan').close()
        this.closeOrderDialog()
        this.closeInforDialog()
        this.cancelDialogBySet()
        this.cancelDialogByLogin()
        this.cancelDialogByLoadiong()
    },
    //页面显示
    onShow() {
        this.onShowTime = new Date().getTime(); //记录页面显示时的时间
        this.updata();
    },
    //初始化数据
    initData: async function () {
        try {
            await this.getAllData();

            if (!this.index_agendaDate) {
                var action = actionjs.initAction(9015);
                var result = await FeatureAbility.callAbility(action);
                var today = JSON.parse(result)
                if (result != null) {
                    this.index_agendaDate = today.data;
                }
            }

            if (this.index_agendaDate == "2023.08.05") {
                this.changeTab(this.data[1]);
            } else if (this.index_agendaDate == "2023.08.06") {
                this.changeTab(this.data[2]);
            } else {
                this.changeTab(this.data[0]);
                this.index_m_meetingTypeCn = "";
            }
            this.index_agendaDate = "";

        } catch (pluginError) {
            console.error("xue服务器时间 : Plugin Error = " + pluginError);
        }
    },
    //获取所有议程数据
    async getAllData() {
        try {
            var action = actionjs.initAction(9011);
            var result = await FeatureAbility.callAbility(action);
            this.data = JSON.parse(result);
            this.copyData = JSON.parse(JSON.stringify(this.data));
            this.filterData();
        } catch (pluginError) {
            console.error("getAllData Error = " + pluginError);
        }
    },
    // 筛选
    showPanel() {
        console.log("筛选")
        this.$element('modal-shaixuan').show()
    },
    //关闭筛选
    closePanel() {
        this.$element('modal-shaixuan').close()
    },
    //过滤数据
    filterData() {
        try {
            this.$element('modal-shaixuan').close();
            var data = JSON.parse(JSON.stringify(this.copyData));
            //我关注的话题
            var crowdArea = [];
            for (let treeRKey in this.treeR) {
                if (this.treeR[treeRKey].isClick) {
                    if (this.treeR[treeRKey].name != "全部") {
                        crowdArea.push(this.treeR[treeRKey].name);
                    }
                }
            }
            //我是
            var category = [];
            for (let treePKey in this.treeP) {
                if (this.treeP[treePKey].isClick) {
                    if (this.treeP[treePKey].name != "全部") {
                        category.push(this.treeP[treePKey].name);
                    }
                }
            }
            data.filter(day => {
                if (this.selectTab && day.date == this.selectTab.date) {
                    day.category.filter(categoryItem => {
                        if (this.selectCategory.name == categoryItem.name) {
                            this.selectCategory.group = categoryItem.group;
                            //搜索框
                            if (this.searchText) {
                                this.selectCategory.group = this.selectCategory.group.filter(group => {
                                    if (group.timeLine) {
                                        group.timeLine = group.timeLine.filter(timeLine => {
                                            var arr = []; //符合条件的嘉宾
                                            if (timeLine.guest && timeLine.guest.length > 0) {
                                                arr = timeLine.guest.filter(guest => {
                                                    return (guest.name && guest.name.toLowerCase().includes(this.searchText.toLowerCase())) || (guest.duty && guest.duty.toLowerCase().includes(this.searchText.toLowerCase()))
                                                });
                                            }
                                            return arr.length > 0 || (timeLine.name && timeLine.name.toLowerCase().includes(this.searchText.toLowerCase())) || (timeLine.hotel && timeLine.hotel.toLowerCase().includes(this.searchText.toLowerCase()));
                                        });
                                        return group.timeLine.length
                                    } else {
                                        return false;
                                    }
                                });
                            }
                            if (day.date != "08/04") {
                                //我关注的话题-我是
                                if (crowdArea.length > 0 || category.length > 0) {
                                    this.selectCategory.group = this.selectCategory.group.filter(group => {
                                        if (group.timeLine) {
                                            group.timeLine = group.timeLine.filter(timeLine => {
                                                var pbool = false;
                                                //我关注的话题
                                                for (var crowdAreaIndex in crowdArea) {
                                                    var str = crowdArea[crowdAreaIndex];
                                                    if (timeLine.m_crowdArea && timeLine.m_crowdArea.length > 0) {
                                                        if (str) {
                                                            for (var areaIndex in timeLine.m_crowdArea) {
                                                                var area = timeLine.m_crowdArea[areaIndex];
                                                                if (area == str) {
                                                                    pbool = true;
                                                                    break;
                                                                }
                                                            }
                                                            if (pbool) {
                                                                break;
                                                            }
                                                        }
                                                    } else {
                                                        pbool = false;
                                                    }
                                                }
                                                if (pbool) {
                                                    //我是
                                                    for (var categoryIndex in category) {
                                                        var categoryStr = category[categoryIndex];
                                                        if (timeLine.m_contentCategory && timeLine.m_contentCategory.length > 0) {
                                                            if (str) {
                                                                for (var cIndex in timeLine.m_contentCategory) {
                                                                    var c = timeLine.m_contentCategory[cIndex];
                                                                    if (c == categoryStr) {
                                                                        pbool = true;
                                                                        break;
                                                                    }
                                                                }
                                                                if (pbool) {
                                                                    break;
                                                                }
                                                            }
                                                        } else {
                                                            pbool = false;
                                                        }
                                                    }
                                                }
                                                return pbool;
                                            });
                                            return group.timeLine.length
                                        } else {
                                            return false;
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            });
        } catch (err) {
            console.error("filterData = " + err);
        }
        if (this.selectCategory.group.length == 0) {
            this.isNullShow = true;
            this.isNullText = '暂无您要搜索的内容';
        } else {
            this.isNullShow = false;
        }
    },
    //关闭添加日历弹窗
    closeOrderDialog() {
        this.$element('orderDialog').close()
    },
    //添加到日历
    addCalendarBycode() {
        this.addCalendar()
    },
    //添加日历
    addCalendar: async function () {
        var this_ = this;
        try {
            var action = actionjs.initAction(8002);
            action.data = {
                title: this.item.name,
                location: this.item.hotel,
                description: this.item.remark,
                startTime: this.item.startTimeLong,
                endTime: this.item.endTimeLong
            };
            var result = await FeatureAbility.callAbility(action);
            if (result == "OK") {
                console.log("addCalendar展示弹窗");
                var timeId = setTimeout(function () {
                    this_.$element('setDialog').show();
                    clearTimeout(timeId);
                }, 500);
            } else if (result == "ERROR") {
                console.log("addCalendar用户拒绝授权");
                //用户禁止授权
                this_.info1 = "请您在设置-服务管理";
                this_.info2 = "打开元服务添加日历权限";
                var timeId = setTimeout(function () {
                    this_.$element('infoDialog').show();
                    clearTimeout(timeId);
                }, 500);
            } else if (result == "ADD_ERROR") {
                console.log("addCalendar添加日历失败");
                this_.info1 = "添加日历失败，请重试";
                var timeId = setTimeout(function () {
                    this_.$element('infoDialog').show();
                    clearTimeout(timeId);
                }, 500);
            } else {
                var timeId = setTimeout(function () {
                    this_.info1 = "已将议程添加到您的日历";
                    this_.$element('infoDialog').show();
                    clearTimeout(timeId);
                }, 500);
                var timeId1 = setTimeout(function () {
                    this_.closeOrderDialog();
                    this_.closeInforDialog();
                    console.log("addCalendar数据更新");
                    this_.updata();
                    clearTimeout(timeId1);
                }, 2500);
            }
        } catch (pluginError) {
            console.error("getBatteryLevel : Plugin Error = " + pluginError);
        }
    },
    //授权日历
    setPermission: async function () {
        var this_ = this;
        try {
            var action = actionjs.initAction(8004);
            var result = await FeatureAbility.callAbility(action);
            var timeId = setTimeout(function () {
                this_.cancelDialogBySet();
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
            console.error("xue授权弹窗 : Plugin Error = " + pluginError);
        }
    },
    //切换TAB
    changeTab: function (e) {
        this.selectTab = {}
        this.selectCategory = {
            group: []
        }
        this.selectTab = e;
        // 按照指定顺序排序
        if (e.date == '08/05') {
            e.category = this.categorySort(e.category)

            if (this.index_m_meetingTypeCn == "鸿蒙生态峰会" ||this.index_m_meetingTypeCn == "HarmonyOS生态峰会" || this.index_m_meetingTypeCn == "技术论坛") {
                this.changeCategory(e.category[1])
            } else if (this.index_m_meetingTypeCn == "Codelabs" || this.index_m_meetingTypeCn == "Tech. Hour") {
                this.changeCategory(e.category[2])
            } else {
                this.changeCategory(e.category[0])
            }
        } else {
            if (this.index_m_meetingTypeCn == "Codelabs" || this.index_m_meetingTypeCn == "Tech. Hour") {
                this.changeCategory(e.category[1])
            } else {
                this.changeCategory(e.category[0])
            }
        }
        this.index_m_meetingTypeCn = "";
        this.filterData();
    },
    //修改类型
    changeCategory: function (e) {
        this.selectCategory = e
        this.filterData();
    },
    //预订
    showDialog(e) {
        if (e.groupAddress) {
            //开发者主题演讲取groupAddress
            e.hotel = e.groupAddress;
        }
        this.createReserve(e)
    },
    //关闭消息弹窗
    closeInforDialog: function () {
        this.$element('infoDialog').close()
        for (let timeIdsKey in this.timeIds) {
            clearTimeout(timeIdsKey);
        }
        this.timeIds = []
    },
    //关闭预定成功弹窗
    cancelDialogByOrder() {
        this.$element('orderDialog').close();
        this.updata();
    },
    //关闭添加日历权限弹窗
    cancelDialogBySet() {
        this.$element('setDialog').close()
    },
    //关闭登录弹窗
    cancelDialogByLogin() {
        this.$element('loginDialog').close()
    },
    //关闭消息弹窗并刷新数据
    cancelDialog(e) {
        this.closeInforDialog()
        this.updata()
    },
    //关闭loading
    cancelDialogByLoadiong(e) {
        this.$element('loading').close()
    },
    //跳转
    go: function (e) {
        actionjs.go(e)
        this.cancelDialog()
    },
    //返回
    back: function () {
        actionjs.back(0)
    },
    //议程详情
    goAgendaDetail: function (group, itemId) {
        if (group.groupName == "主题演讲" || group.groupName == "开发者主题演讲") {
            return
        }
        actionjs.goAgendaDetail(itemId)
    },
    //搜索框内容改变
    searchChange: function (e) {
        this.searchText = e.value
    },
    //更新
    updata: function () {
        console.error("数据更新aaa")
        this.getAllData();
        this.closePanel();
        this.closeOrderDialog();
        this.closeInforDialog();
        this.cancelDialogBySet();
        this.cancelDialogByLogin();
        this.cancelDialogByLoadiong();
        this.fresh = false;
    },
    //预定
    createReserve: async function (e) {
        this.item = e
        this.$element('loading').show()
        var this_ = this
        try {
            this.info1 = ''
            this.info2 = ''
            var action = actionjs.initAction(9013);
            action.data = {
                title: this.item.name,
                subSeminarId: this.item.id,
                startTime: this.item.startTimeLong,
                endTime: this.item.endTimeLong
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
                            e.reserve = true;
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
                            timeId = setTimeout(this_.cancelDialog, 2000)
                            e.reserve = false;
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
    //获取我是
    getDicP: async function () {
        try {
            var action = actionjs.initAction(9009);
            var result = await FeatureAbility.callAbility(action);
            this.treeP = JSON.parse(result)
        } catch (pluginError) {
            console.error("getBatteryLevel : Plugin Error = " + pluginError);
        }
    },
    //获取我关注的话题
    getDicR: async function () {
        try {
            var action = actionjs.initAction(9010);
            var result = await FeatureAbility.callAbility(action);
            this.treeR = JSON.parse(result)
        } catch (pluginError) {
            console.error("getBatteryLevel : Plugin Error = " + pluginError);
        }
    },
    //重置筛选
    closeReset: function () {
        for (let treePKey in this.treeP) {
            this.treeP[treePKey].isClick = false;
        }
        for (let treeRKey in this.treeR) {
            this.treeR[treeRKey].isClick = false;
        }
        if (this.treeP.length > 0) {
            this.treeP[0].isClick = true;
        }
        if (this.treeR.length > 0) {
            this.treeR[0].isClick = true;
        }
    },
    //选择我是
    setSelectP: function (e) {
        var isAll = false;
        var allFalse = false;
        if (this.treeP.length > 0) {
            for (let treePKey in this.treeP) {
                if (this.treeP[treePKey].name == e) {
                    if (treePKey == 0) {
                        isAll = true;
                    }
                    this.treeP[treePKey].isClick = !this.treeP[treePKey].isClick;
                }
            }

            for (var i = 1;i < this.treeP.length; i++) {
                if (this.treeP[i].isClick) {
                    allFalse = true;
                    break;
                }
            }
            if (!isAll) {
                this.treeP[0].isClick = false;
            } else {
                for (let treePKey in this.treeP) {
                    this.treeP[treePKey].isClick = false;
                }
                if (this.treeP.length > 0) {
                    this.treeP[0].isClick = true;
                }
            }
            if (!allFalse) {
                this.treeP[0].isClick = true;
            }
        }
        console.log("xue:" + JSON.stringify(this.treeP))
    },
    //选择话题
    setSelectR: function (e) {
        var isAll = false;
        var allFalse = false;
        if (this.treeR.length > 0) {
            for (let treeRKey in this.treeR) {
                if (this.treeR[treeRKey].name == e) {
                    if (treeRKey == 0) {
                        isAll = true;
                    }
                    this.treeR[treeRKey].isClick = !this.treeR[treeRKey].isClick;
                }
            }
            for (var i = 1;i < this.treeR.length; i++) {
                if (this.treeR[i].isClick) {
                    allFalse = true;
                    break;
                }
            }
            if (!isAll) {
                this.treeR[0].isClick = false;
            } else {
                for (let treeRKey in this.treeR) {
                    this.treeR[treeRKey].isClick = false;
                }
                if (this.treeR.length > 0) {
                    this.treeR[0].isClick = true;
                }
            }
            if (!allFalse) {
                this.treeR[0].isClick = true;
            }
        }
    },
    //类型排序
    categorySort: function (data) {
        let newData = data.sort(function (a, b) {
            var order = ["开发者主题演讲", "论坛活动", "开发者活动"];
            return order.indexOf(a.name) - order.indexOf(b.name);
        });
        return newData
    },
    //添加到桌面
    goCardSelect: function () {
        actionjs.addModularClickEvent("议程列表-添加至桌面"); //上报用户点击功能按钮事件
        var concealAction = actionjs.initAction(5001);
        FeatureAbility.callAbility(concealAction);
    }
}