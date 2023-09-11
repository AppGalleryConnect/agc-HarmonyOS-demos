package com.smarket.hdc2023.bean;

import java.util.List;

public class HdBean {
    public String id;
    public String title;
    public String summary;
    public int order;
    public List<Bean> extra;

    public class Bean {
        public List<Data> customText_57;
        public List<Data> customText_58;
        public List<Data> customText_59;

        public class Data {
            public String name;
            public String value;
        }

    }

}
