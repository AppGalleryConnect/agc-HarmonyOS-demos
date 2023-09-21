package com.smarket.hdc2023.bean;

/**
 * 卡片信息
 */
public class CardInfoContent {
    //卡片信息
    public Object cardInfo;
    public cardInfo card;

    //卡片信息
    public static class cardInfo {
        //主键
        public String id;
        //poid
        public String poid;
        //类型：'checkIn’-签到’；ticket’-门票,’eat’-午餐,’landscape’-风景,’desktop’-加桌,’daily’-每日签到,’questionnaire’-问卷,’interact_prompt’-互动提醒,’interact_complete’-互动完成,
        public String type;
        //卡片标题
        public String title;
        //任务ID
        public String interaction_task_id;
        public String text_1;
        public String text_2;
        public String text_3;
        public String text_4;
        public String text_5;
        public String text_6;
        //会议ID
        public String seminarId;
        //附近卡片的poiId 1-4|100,2-4|200,3-4|300
        public String nearest_poids;
        //最近的卡片信息
        public Object nearestCardInfo;
        public nearestCardInfo nearestCard;
    }

    //最近的卡片信息
    public static class nearestCardInfo {
        public String poid;
        //卡片标题
        public String title;
        //距离
        public String distance;
        //任务完成的文字提示
        public String distanceMessage;
    }
}