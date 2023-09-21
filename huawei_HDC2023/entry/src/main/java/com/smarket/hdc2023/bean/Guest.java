package com.smarket.hdc2023.bean;

public class Guest {

    /**
     * attendedCount : 0
     * demartment :
     * duty : 公司供应链总裁、公司CFO、流程与IT管理部总裁、全球技术服务部总裁、首席供应官、审计委员会主任、监事会主席
     * email :
     * enterprise :
     * gender :
     * global : 0
     * guestId : 0
     * guestType : 演讲嘉宾
     * guestTypeId : 7866
     * imageMapId : 83863956ab58fef23647ca3bf88c9b5e
     * intro :
     * isPublic : 是
     * modifyTime : 1631781673
     * name : 嘉宾名字
     * phone :
     * realPath : //hwuat.smarket.net.cn/content/hw/sFile/tenantId558/2021-09-16/image/1631781670129248.jpg
     * seminarGuestId : 2109160739522190
     * seminarGuestTypeId : 7866
     * seminarId : 2108303597884786
     * showOnHome : 是
     * tenantId : 558
     */
    public String duty;
    public String name;
    public String startTime;
    public String realPath;
    public String showOnHome;
    public String isPublic;
    public String intro;
    public String seminarGuestId;
    public String enterprise;

    public String imageMapIdHarmony;
    public String guestId;

    public Integer getGuestsOrder() {
        if(this.guestsOrder==null){
            return 0;
        }
        return guestsOrder;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setGuestsOrder(Integer guestsOrder) {
        this.guestsOrder = guestsOrder;
    }
    public Integer guestsOrder;
}
