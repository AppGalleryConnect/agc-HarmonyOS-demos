package com.smarket.hdc2023.bean;

import java.util.List;

public class Agendas {
    public String subSeminarId;
    public String name;
    public String endTime;
    public String remark;
    public String img;
    public String startTime;
    public String agendaName;
    public List<Guest> guests;

    //是否可以预约
    public Integer canReserve=0;
    //是否预约
    public Boolean isReserve=false;
    //议程ID
    public String agendaId;
    //议程开始时间
    public String startTimeLong;
    //议程结束时间
    public String endTimeLong;
}
