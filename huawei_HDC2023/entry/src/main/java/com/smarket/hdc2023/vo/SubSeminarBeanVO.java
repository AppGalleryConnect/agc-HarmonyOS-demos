package com.smarket.hdc2023.vo;

import java.util.List;

public class SubSeminarBeanVO {
    private String date;
    public List<CategoryItems> category;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<CategoryItems> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryItems> category) {
        this.category = category;
    }
}
