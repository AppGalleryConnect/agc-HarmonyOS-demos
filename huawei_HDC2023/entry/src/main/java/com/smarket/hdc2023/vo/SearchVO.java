package com.smarket.hdc2023.vo;

public class SearchVO {
    private String searchText;
    private String[] category;
    private String[] crowdArea;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String[] getCategory() {
        return category;
    }

    public void setCategory(String[] category) {
        this.category = category;
    }

    public String[] getCrowdArea() {
        return crowdArea;
    }

    public void setCrowdArea(String[] crowdArea) {
        this.crowdArea = crowdArea;
    }
}
