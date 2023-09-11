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

package com.smarket.hdc2023.vo;

import java.util.List;

public class AgendaListVO {

    private String agendaDate;
    private List<Object> agendas;

    public String getAgendaDate() {
        return agendaDate;
    }

    public void setAgendaDate(String agendaDate) {
        this.agendaDate = agendaDate;
    }

    public List<Object> getAgendas() {
        return agendas;
    }

    public void setAgendas(List<Object> agendas) {
        this.agendas = agendas;
    }
}
