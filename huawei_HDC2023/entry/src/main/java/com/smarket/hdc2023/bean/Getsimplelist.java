package com.smarket.hdc2023.bean;

import java.util.List;

import static com.smarket.hdc2023.common.Config.SEMINAR_ID;
import static com.smarket.hdc2023.common.Config.TENANT_ID;

public class Getsimplelist {
    public String seminarId = SEMINAR_ID;
    public String tenantId= TENANT_ID;
    public long startTime;
    public int canReserve = 1;
    public List<Sort> sort;
    public int start = 0;
    public int num = 4;
}
