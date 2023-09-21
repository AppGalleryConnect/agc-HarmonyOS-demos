package com.smarket.hdc2023.bean;

import java.util.List;

public class AgendasBean  {
    /**
     * agendaDate : 1634918400
     * agendas : [{"agendaId":"8965","tenantId":"558","name":"code日程1","agendaDate":"1634918400","startTime":"1634947200","endTime":"1634947260","createTime":"1631856995","address":"","remark":"2222","status":"notStarted","guests":[]},{"agendaId":"8966","tenantId":"558","name":"code议程二","agendaDate":"1634918400","startTime":"1634947200","endTime":"1634947260","createTime":"1631857016","address":"","remark":"月","status":"notStarted","guests":[]}]
     */
    public long agendaDate;
    public List<Agendas> agendas;
    public class Agendas {
        /**
         * agendaId : 8965
         * tenantId : 558
         * name : code日程1
         * agendaDate : 1634918400
         * startTime : 1634947200
         * endTime : 1634947260
         * createTime : 1631856995
         * address : 
         * remark : 2222
         * status : notStarted
         * guests : []
         */
        public String agendaId;
        public String tenantId;
        public String name;
        public String agendaDate;
        public String startTime;
        public String endTime;
        public String createTime;
        public String address;
        public String remark;
        public String status;
        public List<Object> guests;
    }
}

