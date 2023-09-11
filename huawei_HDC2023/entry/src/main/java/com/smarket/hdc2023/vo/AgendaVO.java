package com.smarket.hdc2023.vo;

import java.util.List;

public class AgendaVO {
    private String agendaDate;
    private List<AgendasVO> agendas;

    public String getAgendaDate() {
        return agendaDate;
    }

    public void setAgendaDate(String agendaDate) {
        this.agendaDate = agendaDate;
    }

    public List<AgendasVO> getAgendas() {
        return agendas;
    }

    public void setAgendas(List<AgendasVO> agendas) {
        this.agendas = agendas;
    }
}
