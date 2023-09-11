package com.smarket.hdc2023.bean;

import com.smarket.hdc2023.vo.AgendaVO;

import java.util.List;

public class SubSeminarBean {

    public String canReserve;
    public String endTime;
    public String hotel;
    public String name;
    public String intro;
    public String reserveEndTime;
    public String reserveStartTime;
    public String startTime;
    public String[] m_meetingTypeCn;
    public String[] m_categoryI;
    public String[] m_isShowHome;
    public String[] m_contentCategory;
    public String[] m_crowdArea;
    public String m_parseIntro;
    public String m_subSeminarOrder;
    public String hmDay;
    public String hmStartTime;
    public String hmEndTime;
    public String hmStartAndEndTime;
    public String hmName;

    public String hmContent;
    public String i_codelabs;
    public Object i_isTechnology;//1松湖对话
    public int hm_isTechnology = -1;//1松湖对话
    public Object i_sortOrder ;//1松湖对话
    public int hm_sortOrder = -1;//1松湖对话
    public Object i_TechHour;//不为空 techHour
    public String hm_TechHour;//不为空 techHour
    public String hm_Hudong;//不为空 techHour
    public Object i_Hudong;//不为空 互动体验
    public String subSeminarId;
    public Object i_type;
    public Object category;//"type": "allsub",
    public List<String> hmCategory;//"type": "allsub",
    public boolean isCode = false;
    public boolean isSelectShow = false;
    public boolean isMain = false;
    public boolean isSub = false;
    public boolean isSelect = false;
    public String hmTitle = "技术论坛";
    public List<AgendaVO> agendaList;
    public List<Agendas> hmAgenda;

    public boolean hmClear = false;

    public List<Guest> hmhdList;

    public String getCanReserve() {
        return canReserve;
    }

    public void setCanReserve(String canReserve) {
        this.canReserve = canReserve;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getReserveEndTime() {
        return reserveEndTime;
    }

    public void setReserveEndTime(String reserveEndTime) {
        this.reserveEndTime = reserveEndTime;
    }

    public String getReserveStartTime() {
        return reserveStartTime;
    }

    public void setReserveStartTime(String reserveStartTime) {
        this.reserveStartTime = reserveStartTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String[] getM_meetingTypeCn() {
        return m_meetingTypeCn;
    }

    public void setM_meetingTypeCn(String[] m_meetingTypeCn) {
        this.m_meetingTypeCn = m_meetingTypeCn;
    }

    public String[] getM_categoryI() {
        return m_categoryI;
    }

    public void setM_categoryI(String[] m_categoryI) {
        this.m_categoryI = m_categoryI;
    }

    public String[] getM_isShowHome() {
        return m_isShowHome;
    }

    public void setM_isShowHome(String[] m_isShowHome) {
        this.m_isShowHome = m_isShowHome;
    }

    public String[] getM_contentCategory() {
        return m_contentCategory;
    }

    public void setM_contentCategory(String[] m_contentCategory) {
        this.m_contentCategory = m_contentCategory;
    }

    public String getHmDay() {
        return hmDay;
    }

    public void setHmDay(String hmDay) {
        this.hmDay = hmDay;
    }

    public String getHmStartTime() {
        return hmStartTime;
    }

    public void setHmStartTime(String hmStartTime) {
        this.hmStartTime = hmStartTime;
    }

    public String getHmEndTime() {
        return hmEndTime;
    }

    public void setHmEndTime(String hmEndTime) {
        this.hmEndTime = hmEndTime;
    }

    public String getHmStartAndEndTime() {
        return hmStartAndEndTime;
    }

    public void setHmStartAndEndTime(String hmStartAndEndTime) {
        this.hmStartAndEndTime = hmStartAndEndTime;
    }

    public String getHmName() {
        return hmName;
    }

    public void setHmName(String hmName) {
        this.hmName = hmName;
    }

    public String getHmContent() {
        return hmContent;
    }

    public void setHmContent(String hmContent) {
        this.hmContent = hmContent;
    }

    public String getI_codelabs() {
        return i_codelabs;
    }

    public void setI_codelabs(String i_codelabs) {
        this.i_codelabs = i_codelabs;
    }

    public Object getI_isTechnology() {
        return i_isTechnology;
    }

    public void setI_isTechnology(Object i_isTechnology) {
        this.i_isTechnology = i_isTechnology;
    }

    public int getHm_isTechnology() {
        return hm_isTechnology;
    }

    public void setHm_isTechnology(int hm_isTechnology) {
        this.hm_isTechnology = hm_isTechnology;
    }

    public Object getI_sortOrder() {
        return i_sortOrder;
    }

    public void setI_sortOrder(Object i_sortOrder) {
        this.i_sortOrder = i_sortOrder;
    }

    public int getHm_sortOrder() {
        return hm_sortOrder;
    }

    public void setHm_sortOrder(int hm_sortOrder) {
        this.hm_sortOrder = hm_sortOrder;
    }

    public Object getI_TechHour() {
        return i_TechHour;
    }

    public void setI_TechHour(Object i_TechHour) {
        this.i_TechHour = i_TechHour;
    }

    public String getHm_TechHour() {
        return hm_TechHour;
    }

    public void setHm_TechHour(String hm_TechHour) {
        this.hm_TechHour = hm_TechHour;
    }

    public String getHm_Hudong() {
        return hm_Hudong;
    }

    public void setHm_Hudong(String hm_Hudong) {
        this.hm_Hudong = hm_Hudong;
    }

    public Object getI_Hudong() {
        return i_Hudong;
    }

    public void setI_Hudong(Object i_Hudong) {
        this.i_Hudong = i_Hudong;
    }

    public String getSubSeminarId() {
        return subSeminarId;
    }

    public void setSubSeminarId(String subSeminarId) {
        this.subSeminarId = subSeminarId;
    }

    public Object getI_type() {
        return i_type;
    }

    public void setI_type(Object i_type) {
        this.i_type = i_type;
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public List<String> getHmCategory() {
        return hmCategory;
    }

    public void setHmCategory(List<String> hmCategory) {
        this.hmCategory = hmCategory;
    }

    public boolean isCode() {
        return isCode;
    }

    public void setCode(boolean code) {
        isCode = code;
    }

    public boolean isSelectShow() {
        return isSelectShow;
    }

    public void setSelectShow(boolean selectShow) {
        isSelectShow = selectShow;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public boolean isSub() {
        return isSub;
    }

    public void setSub(boolean sub) {
        isSub = sub;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getHmTitle() {
        return hmTitle;
    }

    public void setHmTitle(String hmTitle) {
        this.hmTitle = hmTitle;
    }

    public List<AgendaVO> getAgendaList() {
        return agendaList;
    }

    public void setAgendaList(List<AgendaVO> agendaList) {
        this.agendaList = agendaList;
    }

    public List<Agendas> getHmAgenda() {
        return hmAgenda;
    }

    public void setHmAgenda(List<Agendas> hmAgenda) {
        this.hmAgenda = hmAgenda;
    }

    public boolean isHmClear() {
        return hmClear;
    }

    public void setHmClear(boolean hmClear) {
        this.hmClear = hmClear;
    }

    public List<Guest> getHmhdList() {
        return hmhdList;
    }

    public void setHmhdList(List<Guest> hmhdList) {
        this.hmhdList = hmhdList;
    }

    public String getM_parseIntro() {
        return m_parseIntro;
    }

    public void setM_parseIntro(String m_parseIntro) {
        this.m_parseIntro = m_parseIntro;
    }

    public String[] getM_crowdArea() {
        return m_crowdArea;
    }

    public void setM_crowdArea(String[] m_crowdArea) {
        this.m_crowdArea = m_crowdArea;
    }

    public String getM_subSeminarOrder() {
        return m_subSeminarOrder;
    }

    public void setM_subSeminarOrder(String m_subSeminarOrder) {
        this.m_subSeminarOrder = m_subSeminarOrder;
    }
}
