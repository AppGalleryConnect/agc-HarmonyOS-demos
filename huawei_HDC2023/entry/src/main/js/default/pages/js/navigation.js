import router from '@system.router'
export default {
    data: {
        title: 'World',
        menu:false
    },
    menuClick:function(){
        this.menu = !this.menu
    },
    go:function(e){
        this.menu = false
        switch (e) {
            case 0:
            router.push({
                uri: 'pages/loginAccredit/loginAccredit',
            });
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
            case 3 :
            router.replace({
                uri: 'pages/help-about/help-about'
            });
            break
            case 4 :
            router.replace({
                uri: 'pages/help-detail/help-detail'
            });
            break
            case 5 :
            router.replace({
                uri: 'pages/help-traffic/help-traffic'
            });
            break
            case 6 :
            router.replace({
                uri: 'pages/help-car/help-car'
            });
            break
            case 7 :
            router.replace({
                uri: 'pages/help-food/help-food'
            });
            break
            case 8 :
            router.replace({
                uri: 'pages/help-hotel/help-hotel'
            });
            break
            case 9 :
            router.replace({
                uri: 'pages/help-recomand/help-recomand'
            });
            break
            case 10 :
            router.replace({
                uri: 'pages/help-qa/help-qa'
            });
            break
            default:
                router.push ({
                    uri: 'pages/index/index',
                });
                break
        }
    },


}
