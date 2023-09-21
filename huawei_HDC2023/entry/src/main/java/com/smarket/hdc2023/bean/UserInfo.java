package com.smarket.hdc2023.bean;

import java.util.List;

public class UserInfo {
    public int signUpStatusCode;
    public String qrCode;
    public String name;
    public String i_ticketType;
    public String ticketName;//席位名称
    public String i_ticketName;//门票名称
    public String enterprise;
    public String checkInStatus;
    public String giftSpecialRight;
    public List<item> items;
//    报名状态（1未审核，2人工通过， 3自动通过， 4已拒绝）

    public class item {
        public String fieldName;
        public String value;
    }
}
