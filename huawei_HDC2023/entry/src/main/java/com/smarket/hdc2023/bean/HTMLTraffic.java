package com.smarket.hdc2023.bean;

import java.util.ArrayList;
import java.util.List;

public class HTMLTraffic {
    public String title;
    public boolean isShow = false;
    public List<Bean> timeBean = new ArrayList<>();
    public List<Bean> siteBean = new ArrayList<>();

    public void setTimeBean(String title, String content,boolean isShow,boolean isShowTime, List<HTMLTrafficImg> imgs) {
        Bean bean = new Bean();
        bean.title = title;
        bean.content = content.replace("/n", "\r\n").replace("/t", "");
        if (imgs!=null&&imgs.size()>0) {
            for (int i = 0; i < imgs.size(); i++) {
                switch (i+1) {
                    case 1:
                        bean.info1 = imgs.get(i).info;
                        bean.isOne1 = imgs.get(i).isOne;
                        bean.isTwo1 = imgs.get(i).isTwo;
                        break;
                    case 2:
                        bean.info2 = imgs.get(i).info;
                        bean.isOne2 = imgs.get(i).isOne;
                        bean.isTwo2 = imgs.get(i).isTwo;
                        break;
                    case 3:
                        bean.info3 = imgs.get(i).info;
                        bean.isOne3 = imgs.get(i).isOne;
                        bean.isTwo3 = imgs.get(i).isTwo;
                        break;
                    case 4:
                        bean.info4 = imgs.get(i).info;
                        bean.isOne4 = imgs.get(i).isOne;
                        bean.isTwo4 = imgs.get(i).isTwo;
                        break;
                    case 5:
                        bean.info5 = imgs.get(i).info;
                        bean.isOne5 = imgs.get(i).isOne;
                        bean.isTwo5 = imgs.get(i).isTwo;
                        break;
                    case 6:
                        bean.info6 = imgs.get(i).info;
                        bean.isOne6 = imgs.get(i).isOne;
                        bean.isTwo6 = imgs.get(i).isTwo;
                        break;
                    case 7:
                        bean.info7 = imgs.get(i).info;
                        bean.isOne7 = imgs.get(i).isOne;
                        bean.isTwo7 = imgs.get(i).isTwo;
                        break;
                    case 8:
                        bean.info8 = imgs.get(i).info;
                        bean.isOne8 = imgs.get(i).isOne;
                        bean.isTwo8 = imgs.get(i).isTwo;
                        break;
                }
            }
        }
        bean.isShow = isShow;
        bean.isShowTime = isShowTime;
        if (isShowTime) {
            timeBean.add(bean);
        }else {
            siteBean.add(bean);
        }


    }
    public class Bean {
        public String title;
        public String content;
        public boolean isShow = false;
        public boolean isShowTime = false;


        public String info1;
        public boolean isOne1 = false;
        public boolean isTwo1 = false;

        public String info2;
        public boolean isOne2 = false;
        public boolean isTwo2 = false;

        public String info3;
        public boolean isOne3 = false;
        public boolean isTwo3 = false;

        public String info4;
        public boolean isOne4 = false;
        public boolean isTwo4 = false;

        public String info5;
        public boolean isOne5 = false;
        public boolean isTwo5 = false;

        public String info6;
        public boolean isOne6 = false;
        public boolean isTwo6 = false;

        public String info7;
        public boolean isOne7 = false;
        public boolean isTwo7 = false;
        public String info8;
        public boolean isOne8 = false;
        public boolean isTwo8 = false;


    }
}
