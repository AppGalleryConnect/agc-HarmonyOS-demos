/*
 * Copyright 2023. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smarket.hdc2023.bean;

import java.util.List;

public class AgendaDetail {
    public agenda detail;

    public class agenda {
        /**
         * canReserve : 0
         * i_Hudong : 互动体验|10月24日
         * reserveMaxCount : -1
         * i_sortOrder : 2
         * name : 获取能力和工具
         * subSeminarId : 2108301584413243
         * i_questionId :
         * i_type :
         * needVerify : 0
         * seminarId : 2108303597884786
         * category : [{"type":"main","id":"258295","mainId":"258263","condition":["258263"],"nodes":["\u9886\u57df","\u5546\u52a1\u5408\u4f5c"]},{"type":"allsub","nodes":["\u5546\u52a1\u5408\u4f5c"]}]
         * permissionType : disabled
         * hotel :
         * customMessage :
         * instanceId : 19860
         * startTime : 1635040800
         * intro : <p>E:开发者自由|F：选择3个HiAI体验馆的demo进行体验|G：体验完毕后在答题卡填写demo名字并勾选demo使用的能力</p>
         * reserveEndTime : 0
         * createTime :
         * i_TechHour :
         * endTime : 1635044400
         * i_trackAddress :
         * modifyTime :
         * i_codelabs :
         * seatNum :
         * reserveStartTime : 0
         * i_isTechnology :
         * i_liveUrl :
         * tenantId : 558
         * i_trackName :
         * typeDetails :
         * agendaList : []
         * guestList : [{"seminarGuestType":"演讲嘉宾","isDefault":0,"seminarGuestTypeId":"7889","guests":[]}]
         */


        public String name;
        public String subSeminarId;
        public String hotel;
        public String startTime;
        public String endTime;
        public String m_parseIntro;
        public String m_meetingTypeCn;
        public String i_Hudong;
        public String i_TechHour;
        public List<Object> agendaList;
        public List<GuestList> guestList;
        public int i_isTechnology;

    }

}
