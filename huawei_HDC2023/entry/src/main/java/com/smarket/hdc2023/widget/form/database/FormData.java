package com.smarket.hdc2023.widget.form.database;

import ohos.data.orm.OrmObject;
import ohos.data.orm.annotation.Entity;
import ohos.data.orm.annotation.PrimaryKey;

/**
 * 桌面卡片数据表
 */
@Entity(tableName = "form_data")
public class FormData extends OrmObject {
    //卡片Id
    @PrimaryKey()
    private long formId;

    //卡片规格
    private int dimensions;

    //卡片名称
    private String name;

    //poiId
    private String poiId;

    public FormData() {
        super();
    }

    public FormData(long formId, int dimensions, String name, String poiId) {
        this.formId = formId;
        this.dimensions = dimensions;
        this.name = name;
        this.poiId = poiId;
    }

    public long getFormId() {
        return formId;
    }

    public void setFormId(long formId) {
        this.formId = formId;
    }

    public int getDimensions() {
        return dimensions;
    }

    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }
}