package com.smarket.hdc2023.bean;

import java.util.ArrayList;
import java.util.List;

public class HTMLCarBean {
    public String title;
    public boolean isAir = false;
    public boolean isShowTitle = false;
    public List<Bean> info;


    public class Bean {
        public String title;
        public String content;
        public List<String> imgs;

    }

    public void addBean(String title, String content) {
        Bean bean = new Bean();
        bean.title = title;
        bean.content = content;
        if (info == null) {
            info = new ArrayList<>();
        }
        info.add(bean);
    }

    public void addBean(String title, String content, List<String> imgs) {
        Bean bean = new Bean();
        bean.title = title;
        bean.content = content;

        bean.imgs = imgs;
        if (info == null) {
            info = new ArrayList<>();
        }
        info.add(bean);
    }
}
