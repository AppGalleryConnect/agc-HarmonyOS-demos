import router from '@system.router'

export default {
    go: function (e) {
        this.menu = false
        switch (e) {
            case -3:
                router.clear();
                router.replace({
                    uri: 'pages/index/index',
                });
                break
            case -2:
                router.back({
                    uri: 'pages/agenda/agenda',
                });
                break
            case -1:
                router.back();
                break
            case 0:
                this.checkLogin()
                break
            case 1:
                router.push({
                    uri: 'pages/agenda/agenda',
                });
                break
            case 2:
                router.push({
                    uri: 'pages/help/help',
                });
                break
            case 3:
                router.push({
                    uri: 'pages/help-about/help-about'
                });
                break
            case 4:
                router.push({
                    uri: 'pages/help-detail/help-detail',
                    params: {
                        isSelect: 0,
                        pageType: 0
                    }
                });
                break
            case 5:
                router.push({
                    uri: 'pages/help-detail/help-detail',
                    params: {
                        isSelect: 1
                    }
                });
                break
            case 6:
                router.push({
                    uri: 'pages/help-detail/help-detail',
                    params: {
                        isSelect: 2,
                        pageType: 1
                    }
                });
                break
            case 7:
                router.push({
                    uri: 'pages/help-detail/help-detail',
                    params: {
                        isSelect: 3,
                        pageType: 2
                    }
                });
                break
            case 8:
                router.push({
                    uri: 'pages/help-detail/help-detail',
                    params: {
                        isSelect: 4,
                        pageType: 3
                    }
                });
                break
            case 9:
                router.push({
                    uri: 'pages/help-detail/help-detail',
                    params: {
                        isSelect: 5
                    }
                });
                break
            case 10:
                router.push({
                    uri: 'pages/help-detail/help-detail',
                    params: {
                        isSelect: 6
                    }
                });
                break
            case 11:
                router.replace({
                    uri: 'pages/my/my',
                });
                break
            case 12:
                router.push({
                    uri: 'pages/loginAccredit/loginAccredit',
                });
                break
            case 13:
                router.push({
                    uri: 'pages/my-ticket/my-ticket'
                });
                break
            case 131:
                router.push({
                    uri: 'pages/my-ticket/my-ticket',
                    params: {
                        isLogin: true
                    }
                });
                break
            case 14:
                router.push({
                    uri: 'pages/my-agenda/my-agenda'
                });
                break
            case 15:
                router.push({
                    uri: 'pages/help/help',
                });
                break
            case 16:
                router.clear();
                router.push({
                    uri: 'pages/index/index',
                });
                break
            case 17:
                router.clear();
                router.push({
                    uri: 'pages/index/index',
                });
                break
            case 18:
                router.push({
                    uri: 'pages/guest/guest-list',
                });
                break
            case 20:
                router.push({
                    uri: 'pages/partner/partner-list',
                });
                break
            case 21:
                //三丫坡风景
                router.push({
                    uri: 'pages/travel/travel',
                });
                break
            case 22:
            //三丫坡风景更多
                router.push({
                    uri: 'pages/travel-more/travel-more',
                });
                break
            case 23:
            //三丫坡风景详情
                router.push({
                    uri: 'pages/travel-detail/travel-detail',
                });
                break
            case 24:
            //联系我们
            router.push({
                uri: 'pages/contact-us/contact-us',
            });
            break
            case 25:
            //积分互动
                this.checkLogin_1();
                break
            case 26:
            //积分互动
                router.push({
                    uri: 'pages/taskCenter/taskCenter',
                });
                break
            default:
                router.back();
                break
        }
    },
    goIndex:function (e){
        router.clear();
        switch (e) {
            case -3:
                router.replace({
                    uri: 'pages/index/index',
                });
                break
            case 0:
                this.checkLogin()
                break
            case 1:
                router.replace({
                    uri: 'pages/agenda/agenda',
                });
                break
            case 2:
                router.replace({
                    uri: 'pages/help/help',
                });
                break
            case 21:
            //三丫坡风景
                router.replace({
                    uri: 'pages/travel/travel',
                });
                break
            default:
                router.back();
                break
        }
    },
    back: function (e) {
        this.menu = false
        switch (e) {
            default:
                router.back();
                break
        }
    },
    goAgendaDetail: function (e) {

        router.push({
            uri: 'pages/agenda-detail/agenda-detail',
            params: {
                id: e
            }
        });

    },
    goGuestDetail: function (e,num) {

        router.push({
            uri: 'pages/guest/guest-detail',
            params: {
                id: e,
                num: num
            }
        });

    },
    goAgendaDetailByhd: function (e) {

        router.push({
            uri: 'pages/agenda-detail/agenda-detail',
            params: {
                bean: e
            }
        });

    },
    goAgendaDetailByTh: function (e) {

        router.push({
            uri: 'pages/agenda-detail/agenda-detail',
            params: {
                tHbean: e
            }
        });

    },
    checkLogin: async function(){
        try {
            var action = this.initAction(8000);

            var result = await FeatureAbility.callAbility(action);
            console.log("1111111xue:" + result)
            if (result == 0) {
                this.go(11)
            } else {
                this.go(12)
            }

        } catch (pluginError) {
            console.error("getBatteryLevel : Plugin Error = " + pluginError);
        }
    },
    checkLogin_1: async function(){
        try {
            var action = this.initAction(8000);
            var result = await FeatureAbility.callAbility(action);
            if (result == 0) {
                router.push({
                    uri: 'pages/travel/travel',
                });
            } else {
                this.go(12)
            }

        } catch (pluginError) {
            console.error("getBatteryLevel : Plugin Error = " + pluginError);
        }
    },
    ifLogin: async function(){
        try {
            var action = this.initAction(8000);

            var result = await FeatureAbility.callAbility(action);
            console.log("1111111xue:" + result)
            if (result == 0) {

            } else {
                router.back({
                    uri: 'pages/index/index',
                });
            }

        } catch (pluginError) {
            console.error("getBatteryLevel : Plugin Error = " + pluginError);
        }
    },

    getIdData: async function(){
        try {
            var action = this.initAction(9007);
            var result = await FeatureAbility.callAbility(action);
        } catch (pluginError) {
            console.error("getBatteryLevel : Plugin Error = " + pluginError);
        }
    },
    addAction: async function(e){
        try {
            var action = this.initAction(9000);
            action.data = e
            var result = await FeatureAbility.callAbility(action);
        } catch (pluginError) {
            console.error("getBatteryLevel : Plugin Error = " + pluginError);
        }
    },
    addAgendaDetailAction: async function(e){
        try {
            var action = this.initAction(90001);
            action.data = e
            var result = await FeatureAbility.callAbility(action);
        } catch (pluginError) {
            console.error("getBatteryLevel : Plugin Error = " + pluginError);
        }
    },
    initAction: function (code) {
        var actionData = {};
        var action = {};
        action.bundleName = "com.smarket.hdc2023";
        action.abilityName = "BatteryInternalAbility";
        action.messageCode = code;
        action.data = actionData;
        action.abilityType = 1;
        action.syncOption = 0;
        return action;
    },
    goAgendaList: function (e) {
        router.push({
            uri: 'pages/agenda/agenda',
            params: {
                index_m_meetingTypeCn: e.m_meetingTypeCn,
                index_agendaDate: e.agendaDate
            }
        });
    },
    goTravelDetail: function (id, isMore) {
        router.push({
            uri: 'pages/travel-detail/travel-detail',
            params: {
                id: id,
                isMore: isMore
            }
        });

    },
    //上报用户浏览页面事件
    addPageViewEvent: async function(pageName,onShowTime) {
        try {
            var onHideTime = new Date().getTime();
            var duration = onHideTime - onShowTime;
            var action = this.initAction(10001);
            action.data = {
                pageName: pageName,
                duration: duration
            };
            await FeatureAbility.callAbility(action);
        } catch (pluginError) {
            console.error("addPageViewEvent : Plugin Error = " + pluginError);
        }
    },
    //上报用户点击功能按钮事件
    addModularClickEvent: async function(buttonName) {
        try {
            var action = this.initAction(10002);
            action.data = buttonName;
            await FeatureAbility.callAbility(action);
        } catch (pluginError) {
            console.error("addModularClickEvent : Plugin Error = " + pluginError);
        }
    },
    goLogin: function (goUrl) {
        router.push({
            uri: 'pages/loginAccredit/loginAccredit',
            params: {
                goUrl: goUrl
            }
        });
    },
    goLoginNew: function (goUrl) {
        router.push({
            uri: 'pages/login/login',
            params: {
                goUrl: goUrl
            }
        });
    }
}
