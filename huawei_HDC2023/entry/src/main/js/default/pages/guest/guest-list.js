import actionjs from '../js/action.js';

export default {
    data() {
        return {
            searchText: '', //搜索框内容
            main: [], //嘉宾数据
            isNullShow: false, //是否没有值
            fresh: false, //是否正在刷新
            onShowTime: null //页面显示时的时间
        }
    },
    //页面初始化
    onInit() {
        this.getSubListData();
    },
    //页面显示
    onShow() {
        this.onShowTime = new Date().getTime(); //记录页面显示时的时间
    },
    //页面消失
    onHide() {
        actionjs.addPageViewEvent("嘉宾列表", this.onShowTime); //上报用户浏览页面事件
    },
    //下拉刷新
    refresh: function (e) {
        this.fresh = e.refreshing;
        this.getSubListData();
    },
    //搜索
    closePanel() {
        this.getSubListData()
    },
    //返回
    back: function () {
        actionjs.back(0)
    },
    //打开嘉宾详情
    goGuestDetail: function (e, num) {
        actionjs.goGuestDetail(e, num)
    },
    //搜索框内容改变
    searchChange: function (e) {
        this.searchText = e.value
    },
    //获取嘉宾数据
    getSubListData: async function () {
        try {
            var action = actionjs.initAction(90081);
            action.data = {
                key: this.searchText
            }
            var result = await FeatureAbility.callAbility(action);
            this.main = JSON.parse(result);
            this.fresh = false;
            if (this.main && this.main.length > 0) {
                this.isNullShow = false;
            } else {
                this.isNullShow = true;
            }
        } catch (pluginError) {
            console.error("xuegetSubListData : Plugin Error = " + pluginError);
        }
    },
    //添加至桌面
    goCardSelect: function () {
        var concealAction = actionjs.initAction(5001);
        FeatureAbility.callAbility(concealAction);
    }
}