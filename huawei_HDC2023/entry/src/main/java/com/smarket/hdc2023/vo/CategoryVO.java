package com.smarket.hdc2023.vo;

import java.util.List;

public class CategoryVO {
    private String groupName;
    private String groupId;
    private String groupTime;
    private String groupAddress;
    private String groupTips;
    private String groupTitle;
    private String groupDesc;
//    private List<AgendasVO> timeLine;
    private List<TimeLineVO> timeLine;
    private String[] m_crowdArea;
    private String[] m_contentCategory;

    //是否可以预约
    private Integer canReserve=0;
    //是否预约
    private Boolean isReserve=false;
    //议程ID
    private String id;
    //议程名称
    private String name;
    //议程开始时间
    private String startTimeLong;
    //议程结束时间
    private String endTimeLong;

    public List<TimeLineVO> getTimeLine() {
        return timeLine;
    }

    public void setTimeLine(List<TimeLineVO> timeLine) {
        this.timeLine = timeLine;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupTime() {
        return groupTime;
    }

    public void setGroupTime(String groupTime) {
        this.groupTime = groupTime;
    }

    public String getGroupAddress() {
        return groupAddress;
    }

    public void setGroupAddress(String groupAddress) {
        this.groupAddress = groupAddress;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getGroupTips() {
        return groupTips;
    }

    public void setGroupTips(String groupTips) {
        this.groupTips = groupTips;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
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

}
