package com.smarket.hdc2023.bean;

import java.util.List;

public class AgendaDetailBack {
    public String hmName;
    public String subSeminarId;
    public String thId;
    public String hmDay;
    public String intro;
    public String startTime;
    public String endTime;
    public String hmStartAndEndTime;
    public String hotel;
    public boolean isSelect = false;
    public boolean isShowSelect = false;
    public int type = 1;//1，松湖对话/论坛活动；2，codelabs；3，tech hour；4.互动体验 ,5早餐会
    public boolean techhourType = true;
    public List<Agendas> hmAgenda;
    public List<Guest> guestList;
}
