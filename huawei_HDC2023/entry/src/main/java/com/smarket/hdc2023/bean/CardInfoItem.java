package com.smarket.hdc2023.bean;

/**
 * 卡片信息
 */
public class CardInfoItem {
    //主键ID
    public String id;
    //POI-ID
    public String poid;
    //类型：'checkIn’-签到’；ticket’-门票,’eat’-午餐,’landscape’-风景,’desktop’-加桌,’daily’-每日签到,’questionnaire’-问卷,’interact_prompt’-互动提醒,’interact_complete’-互动完成,
    public String type;
    //标题
    public String title;
    public String text_1;
    public String text_2;
    public String text_3;
    public String text_4;
    public String text_5;
    public String text_6;
    //会议ID
    public String seminarId;
}