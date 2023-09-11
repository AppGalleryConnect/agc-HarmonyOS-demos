import actionjs from '../js/action.js';

export default {
    data() {
        return {
            detail: {}, //嘉宾详情
            timeLine: [], //演讲议程
            onShowTime: null //页面显示时的时间
        }
    },
    //页面初始化
    onInit() {
        this.getGuestDetail()
    },
    //页面显示
    onShow() {
        this.onShowTime = new Date().getTime(); //记录页面显示时的时间
    },
    //页面消失
    onHide() {
        actionjs.addPageViewEvent("嘉宾详情", this.onShowTime); //上报用户浏览页面事件
    },
    //返回
    back: function () {
        actionjs.back(0)
    },
    //获取嘉宾详情数据
    getGuestDetail: async function () {
        try {
            var action = actionjs.initAction(90082);
            action.data = this.id
            var result = await FeatureAbility.callAbility(action);
            var data = JSON.parse(result)
            this.detail = data.detail
            this.timeLine = data.subSeminarList;
            for (let i = 0; i < this.timeLine.length; i++) {
                this.timeLine[i].agendaDate = this.timeLine[i].agendaDate.replace(/月/g, "/").replace(/日/g, "");
            }
            console.error("nitamade ===");
            console.error(JSON.stringify(this.timeLine))
        } catch (pluginError) {
            console.error("xuegetSubListData : Plugin Error = " + pluginError);
        }
    }
}