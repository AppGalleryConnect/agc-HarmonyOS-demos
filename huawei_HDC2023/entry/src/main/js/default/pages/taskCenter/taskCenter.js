import actionjs from '../js/action.js';
export  default {
    data(){
        return{
            isShow:false,
            taskList:[
                /*{
                    title:'积分互动每日签到',
                    describe:'积分互动每日签到积分互动每日签到积分',
                    isSign:0,//0未签到 1已签到
                    type:'sign',//签到
                },
                {
                    title:'预约大会议程',
                    describe:'预约大会议程预约大会议程',
                    isFinish:0,//0未完成 1已完成
                    type:'task',//任务
                    progress:3,//当前进度
                    totalProgress:5,//总进度
                },
                {
                    title:'浏览三丫坡风光',
                    describe:'浏览三丫坡风光浏览三丫坡风光',
                    isFinish:1,//0未完成 1已完成
                    type:'task',//任务
                    progress:3,//当前进度
                    totalProgress:5,//总进度
                },
                {
                    title:'添加元服务卡片至桌面',
                    describe:'加元服务卡片至桌面',
                    isAdd:0,//0已添加 1未添加
                    type:'card',//添加卡片
                    progress:1,//当前进度
                    totalProgress:2,//总进度
                },
                {
                    title:'问卷答题',
                    describe:'参与技术论坛调查问卷，获得积分',
                    isJoin:0,//0已参加 1未参加
                    type:'joinQues',//参加问卷答题
                    progress:2,//当前进度
                    totalProgress:2,//总进度
                },*/
            ],
            isHW:true,
            text:"活动未开始，敬请期待",
            onShowTime: null //页面显示时的时间
        }
    },
    back: function () {
        actionjs.back(0)
    },
    onShow(){
        this.onShowTime = new Date().getTime(); //记录页面显示时的时间
        this.getTask()
    },
    //页面消失
    onHide() {
        actionjs.addPageViewEvent("任务中心", this.onShowTime); //上报用户浏览页面事件
    },
    //关闭loading
    cancelDialogByLoadiong(e) {
        this.$element('loading').close()
    },
    async getTask(){
        try {
            this.$element('loading').show()
            var action = actionjs.initAction(7004);
            var result = await FeatureAbility.callAbility(action);
            var taskList = JSON.parse(result);
            for(var i=0;i<taskList.length;i++){
                taskList[i].explanation = taskList[i].explanation.replace(/<\/?.+?\/?>/g, "").replace("&ldquo;","“").replace("&rdquo;","”").replace("&nbsp;","");
                var nameArr = taskList[i].name.split("^");
                taskList[i].name1 = nameArr[0]
                taskList[i].name2 = ""
                if(nameArr.length>1){
                    taskList[i].name2 = nameArr[1]
                }
            }
            this.taskList = taskList;
            this.isShow = true
            this.cancelDialogByLoadiong()
        } catch (pluginError) {
            console.error("getInteractionTaskQuery : Plugin Error = " + pluginError);
        }
    },
    goCardSelect: function () {
        var concealAction = actionjs.initAction(5001);
        FeatureAbility.callAbility(concealAction);
    },
    go: function (e) {
        actionjs.go(e)
    },
    showhintDialog() {
        this.$element('hintDialog').show()
    },
    closeDialog(){
        this.$element('hintDialog').close()
    },
    async goTask(){
        var action = actionjs.initAction(9015);
        var result = await FeatureAbility.callAbility(action);
        var today = JSON.parse(result)
        if (result != null) {
            var datArr = today.data.split('.');
            if(datArr.length == 3){
                var month = datArr[1]*1
                var day = datArr[2]*1
                if(month<8 || (month==8 && day<=4) ){
                    this.showhintDialog();
                }else{
                    actionjs.go(1)
                }
            }else{
                actionjs.go(1)
            }
        }else{
            actionjs.go(1)
        }
    }
}
