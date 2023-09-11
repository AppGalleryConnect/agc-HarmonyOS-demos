package com.smarket.hdc2023.bean;

/**
 * 小艺建议卡片样式实体
 */
public class CeliaCardStyle {
    public String bgClass1 = "";//卡片背景Class1
    public String bgClass2 = "";//卡片背景Class2
    public Boolean isTicketCard = false;//是否为我的门票卡片
    public Boolean isSignOrLunchCard = false;//是否为签到或大会午餐卡片
    public Boolean isLunchCard = false;//是否为大会午餐卡片
    public Boolean isSignCard = false;//是否为签到卡片
    public Boolean isSceneryCard = false;//是否为风景卡片
    public Boolean hasIntegralBtn = false;//是否显示积分互动图标按钮
    public Boolean isIntegralCard = false;//是否为积分互动卡片
    public Boolean hasAddIntegral = false;//是否显示增加的积分
    public Boolean isTaskCard = false;//是否为加桌卡片、每日打卡卡片、趣味答题卡片
    public Boolean isAgendaCard = false;//是否为议程卡片
    public Boolean isFirstAgendaCard = false;//是否为主题演讲卡片
    public Boolean isMyAgendaCard = false;//是否为我的议程卡片
    public String notMyAgendaCardBtn = "";//主题演讲卡片或全部议程卡片的按钮名称
    public String ticketDivClass = "";//我的门票DIV Class
    public String ticketTextClass = "";//我的门票Text Class
    public Boolean isLogin = false;//是否为已登录样式
    public String integralText = "您的码力值";//积分互动卡片文字
}