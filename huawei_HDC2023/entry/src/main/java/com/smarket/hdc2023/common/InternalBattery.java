package com.smarket.hdc2023.common;

public class InternalBattery {

    public static final int LOGIN_HW = 99;
    public static final int CHECK_USER = 80000;
    public static final int CHECK_LOGIN = 8000;
    public static final int USER_INFO = 8001;

    /*
     * 添加到日历
     * */
    public static final int ADD_CALENDAR = 8002;
    /*
     * 退出登录
     * */
    public static final int LOGIN_OUT = 8003;
    /*
     * 日历授权
     * */
    public static final int SET_CALENDAR = 8004;
    /*
     * 检测普通票
     * */
    public static final int CHECK_ = 8005;
    /*
     * 统计addAction
     * */
    public static final int ADD_ACTION = 9000;
    /*
     * 统计addAction(议程详情专用)
     * */
    public static final int ADD_ACTION_ = 90001;
    /*
     * 发送验证码
     * */
    public static final int BATTERY_SEND_CODE = 9001;
    /*
     * 校验验证码
     * */
    public static final int BATTERY_GET_TOKEN = 9002;
    /*
     * 登录
     * */
    public static final int BATTERY_LOGIN = 9003;
    /*
     * 获取会议信息 getSeminarInfoByjson
     * */
    public static final int SEMINAR_INFO = 9004;
    /*
     * 获取首页议程数据
     * */
    public static final int SEMINAR_BANNER = 9005;
    /*
     * 获取参会指南数据
     * */
    public static final int SEMINAR_HELP = 9006;
    /*
     * 获取预订列表
     * */
    public static final int RESERVE_ID_LIST = 9007;
    /*
     * 获取预订TechHour列表
     * */
    public static final int RESERVE_TECHHOUR_ID_LIST = 90070;
    /*
     * 获取预订列表(按照日期返回)
     * */
    public static final int RESERVE_ID_LIST_1 = 90071;
    /*
     * 获取首页嘉宾
     * */
    public static final int GUEST_IMGS = 9008;
    /*
     * 获取嘉宾列表
     * */
    public static final int GUEST_LIST = 90081;
    /*
     * 获取嘉宾明细
     * */
    public static final int GUEST_INFO = 90082;
    /*
     * 获取议程筛选（合作伙伴）
     * */
    public static final int FILTER_DIC_P = 9009;
    /*
     * 获取议程筛选（领域）
     * */
    public static final int FILTER_DIC_R = 9010;
    /*
     * 获取议程列表querySubSeminar
     * */
    public static final int QUERY_SUB_SEMINAR = 9011;
    /*
     * 获取议程列表(按照日程返回)
     * */
    public static final int QUERY_SUB_SEMINAR_1 = 90111;
    /*
     * 获取议程详情
     * */
    public static final int QUERY_SUB_SEMINAR_GET = 9012;
    /*
     * 预定议程
     * */
    public static final int CREATE_RESERVE = 9013;
    /*
     * 获取活动体验的地址
     * */
    public static final int GET_HDHOTEL = 9014;
    /*
     * 获取服务器时间
     * */
    public static final int GET_TIME = 9015;

    /*
     * 检查是否已同意隐私政策
     * */
    public static final int HAS_AGREE_CONCEAL = 6000;
    /*
     * 同意隐私政策
     * */
    public static final int AGREE_CONCEAL = 6001;
    /*
     * 不同意隐私政策
     * */
    public static final int DIS_AGREE_CONCEAL = 6002;
    /*
     * 获取文章数据
     * */
    public static final int ARTICLE_DETAIL = 6003;
    /*
     * 跳转浏览器
     * */
    public static final int OPEN_SYSTEM_BROWSER = 6004;
    /*
     * 收起键盘
     * */
    public static final int CLOSE_KEY_BOARD = 6005;

    /*
     * 合作伙伴列表
     * */
    public static final int PARTNER_LIST = 5000;
    /*
     * 打开添加到桌面
     * */
    public static final int ADD_TO_HOME = 5001;
    /*
     * 打开直播
     * */
    public static final int OPEN_SHOW = 5002;
    /*
     * 打开WebVIew
     * */
    public static final int OPEN_WEBVIEW = 12345;
    /*
     * 打开petal出行
     * */
    public static final int OPEN_PETAL = 12346;
    /*
     * 更多服务
     * */
    public static final int MORE_SERVICES = 90200;

    /*
     *  三丫坡风光 轮播图
     * */
    public static final int TRAVEL_SWIPER = 7001;

    /*
     *  三丫坡风光 全部列表
     * */
    public static final int TRAVEL_LIST = 7002;

    /*
     *  三丫坡风光 详情
     * */
    public static final int TRAVEL_DETAIL = 7003;
    /*
     *  活动列表
     * */
    public static final int INTER_TASKQUERY = 7004;

    /**
     *  播放语音
     */
    public static final int PLAY_AUDIO = 7005;

    /**
     *  结束语音服务
     */
    public static final int PLAY_STOP = 7006;

    /**
     *  查询语音播放状态
     */
    public static final int PLAY_STATUS = 7007;
    /**
     *  首页顶部轮播图
     */
    public static final int INDEX_IMGS = 17007;
    /**
     * 上报用户浏览页面事件
     */
    public static final int ADD_PAGE_VIEW_EVENT = 10001;
    /**
     * 上报用户点击功能按钮事件
     */
    public static final int ADD_MODULAR_CLICK_EVENT = 10002;
}
