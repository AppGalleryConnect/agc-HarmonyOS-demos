package com.smarket.hdc2023.bean;

public class AgendaSummary {
    public String subSeminarId;
    public String name;
    public String hotel;
    public String startTime;
    public String endTime;
    public String agendaDate;
    public String m_meetingTypeCn;
    public String m_isShowHome;
    public String m_subSeminarOrder;

    public String getM_subSeminarOrder() {
        return m_subSeminarOrder;
    }

    public void setM_subSeminarOrder(String m_subSeminarOrder) {
        this.m_subSeminarOrder = m_subSeminarOrder;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAgendaDate() {
        return agendaDate;
    }

    public void setAgendaDate(String agendaDate) {
        this.agendaDate = agendaDate;
    }

    public String getM_meetingTypeCn() {
        return m_meetingTypeCn;
    }

    public void setM_meetingTypeCn(String m_meetingTypeCn) {
        this.m_meetingTypeCn = m_meetingTypeCn;
    }

    public String getM_isShowHome() {
        return m_isShowHome;
    }

    public void setM_isShowHome(String m_isShowHome) {
        this.m_isShowHome = m_isShowHome;
    }
}
