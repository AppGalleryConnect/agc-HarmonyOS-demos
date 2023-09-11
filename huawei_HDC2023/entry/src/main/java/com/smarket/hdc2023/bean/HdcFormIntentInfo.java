package com.smarket.hdc2023.bean;

import java.util.List;

public class HdcFormIntentInfo {
    public List<PoiInfo> poiParams;

    public List<PoiInfo> getPoiParams() {
        return poiParams;
    }

    public class PoiInfo {
        public String poiId;

        public String getPoiId() {
            return poiId;
        }
    }
}