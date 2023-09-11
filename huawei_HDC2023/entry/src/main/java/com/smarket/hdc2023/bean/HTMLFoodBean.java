package com.smarket.hdc2023.bean;

import java.util.ArrayList;
import java.util.List;

public class HTMLFoodBean {
    public List<String> imgs;
    public List<String> title;
    public List<Bean> info;

    public void addBean(String miniTitle, String content) {
        Bean bean = new Bean();
        bean.miniTitle = miniTitle;
        bean.content = content;
        if (info == null) {
            info = new ArrayList<>();
        }
        info.add(bean);
    }

    public class Bean {
        public String miniTitle;
        public String content;
    }
}
