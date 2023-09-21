package com.smarket.hdc2023.vo;

import com.smarket.hdc2023.bean.Guest;

import java.util.List;

public class TimeLineVO {
    private String id;
    private String name;
    private String remark;
    private String startTime;
    private String startTimeLong;
    private String endTime;
    private String endTimeLong;
    private List<Guest> guest;
    private String hotel;
    private String order;
    private String[] m_crowdArea;
    private String[] m_contentCategory;
    //是否可以预约
    private Integer canReserve=0;
    //是否预约
    private Boolean isReserve=false;

    private List<AgendasVO> agendaVO;

    public String getStartTimeLong() {
        return startTimeLong;
    }

    public void setStartTimeLong(String startTimeLong) {
        this.startTimeLong = startTimeLong;
    }

    public String getEndTimeLong() {
        return endTimeLong;
    }

    public void setEndTimeLong(String endTimeLong) {
        this.endTimeLong = endTimeLong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Integer getCanReserve() {
        return canReserve;
    }

    public void setCanReserve(Integer canReserve) {
        this.canReserve = canReserve;
    }

    public Boolean getReserve() {
        return isReserve;
    }

    public void setReserve(Boolean reserve) {
        isReserve = reserve;
    }

    public List<Guest> getGuest() {
        return guest;
    }

    public void setGuest(List<Guest> guest) {
        this.guest = guest;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String[] getM_crowdArea() {
        return m_crowdArea;
    }

    public void setM_crowdArea(String[] m_crowdArea) {
        this.m_crowdArea = m_crowdArea;
    }

    public String[] getM_contentCategory() {
        return m_contentCategory;
    }

    public void setM_contentCategory(String[] m_contentCategory) {
        this.m_contentCategory = m_contentCategory;
    }

    public List<AgendasVO> getAgendaVO() {
        return agendaVO;
    }

    public void setAgendaVO(List<AgendasVO> agendaVO) {
        this.agendaVO = agendaVO;
    }
}
