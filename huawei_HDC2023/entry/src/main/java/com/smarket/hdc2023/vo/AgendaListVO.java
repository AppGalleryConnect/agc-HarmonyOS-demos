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
