package com.smarket.hdc2023.widget.form.bean;

import java.util.List;

/**
 * 卡片议程数据实体类
 */
public class AgendaBean {
    //会议ID
    private String subSeminarId;
    //名称
    private String name;
    //地点
    private String hotel;
    //开始时间
    private long startTime;
    //结束时间
    private long endTime;
    //会议类型
    private List<String> m_meetingTypeCn;
    //会议日期
    private String agendaDate;
    //会议时间
    private String agendaTime;
    //是否包含主题演讲
    private Boolean hasGroupName;

    public String getSubSeminarId() {
        return subSeminarId;
    }

    public void setSubSeminarId(String subSeminarId) {
        this.subSeminarId = subSeminarId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public List<String> getM_meetingTypeCn() {
        return m_meetingTypeCn;
    }

    public void setM_meetingTypeCn(List<String> m_meetingTypeCn) {
        this.m_meetingTypeCn = m_meetingTypeCn;
    }

    public String getAgendaDate() {
        return agendaDate;
    }

    public void setAgendaDate(String agendaDate) {
        this.agendaDate = agendaDate;
    }

    public String getAgendaTime() {
        return agendaTime;
    }

    public void setAgendaTime(String agendaTime) {
        this.agendaTime = agendaTime;
    }

    public Boolean getHasGroupName() {
        return hasGroupName;
    }

    public void setHasGroupName(Boolean hasGroupName) {
        this.hasGroupName = hasGroupName;
    }

    public AgendaBean() {
    }
}