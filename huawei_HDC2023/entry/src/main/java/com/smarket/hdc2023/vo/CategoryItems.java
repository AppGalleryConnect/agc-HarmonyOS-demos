package com.smarket.hdc2023.vo;

import java.util.List;

public class CategoryItems {

    private String name;
    public List<CategoryVO> group;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryVO> getGroup() {
        return group;
    }

    public void setGroup(List<CategoryVO> group) {
        this.group = group;
    }
}
