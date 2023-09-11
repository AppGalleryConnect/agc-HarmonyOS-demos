package com.smarket.hdc2023.http;

import com.smarket.hdc2023.MyApplication;
import com.smarket.hdc2023.bean.*;
import com.smarket.hdc2023.common.Config;
import com.smarket.hdc2023.common.PageAction;
import com.smarket.hdc2023.help.LogUtil;
import com.smarket.hdc2023.help.TimeHelper;
import ohos.agp.utils.TextTool;
import ohos.utils.zson.ZSONArray;
import ohos.utils.zson.ZSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.smarket.hdc2023.common.PageAction.*;

public class HttpData {
    private static final String regEx_html = "<[^>]+>";

    public static SeminarInfo checkSeminarInfo(SeminarInfo seminarInfo) {
        /*seminarInfo.values.logoMapId = Config.HOST_IMG + seminarInfo.values.logoMapId;
        seminarInfo.values.logoMapId = HttpApi.get302(seminarInfo.values.logoMapId);*/
        String time = TimeHelper.getDate(seminarInfo.values.startTime) + "-" + TimeHelper.getDate(seminarInfo.values.endTime);
        seminarInfo.values.address = time + "|" + seminarInfo.values.address;
        LogUtil.e("大会介绍：" + seminarInfo.values.introduction);
        seminarInfo.values.introduction = seminarInfo.values.introduction.replace(" ", "")
                .replace("<br/>", "\r\n").replace("&nbsp", "").replace(";", "");
        Pattern p = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(seminarInfo.values.introduction);
        seminarInfo.values.introduction = matcher.replaceAll("");
        return seminarInfo;
    }

//    public static List<HTMLBean> checkHtmlInfo(String info) {
//        List<HTMLBean> htmlBeans = new ArrayList<>();
//        info = info.replace("\\n", "/n").replace("\\", "");
//        LogUtil.e("HTML：" + info);
//        Document doc = Jsoup.parse(info);
//        Elements titleElements = doc.select("p[class=centent_box_title]");
//        for (Element imgElement : titleElements) {
//            HTMLBean htmlBean = new HTMLBean();
//            String title = imgElement.text();
//            LogUtil.e("HTMLtitle：" + title);
//            htmlBean.title = title;
//            htmlBeans.add(htmlBean);
//        }
//        Elements imgElements = doc.select("img[src]");
//        for (int i = 0; i < imgElements.size(); i++) {
//            String img = imgElements.get(i).attr("src");
//            if (!img.startsWith("http")) {
//                img = "https:" + img;
//            }
//            if (img.contains("mappingId=")) {
//                img = HttpApi.get302(img);
//            }
//            HTMLBean htmlBean;
//            if (htmlBeans.size() > i) {
//                htmlBean = htmlBeans.get(i);
//                htmlBean.img = img;
//            } else {
//                htmlBean = new HTMLBean();
//                htmlBean.img = img;
//                htmlBeans.add(htmlBean);
//            }
//            LogUtil.e("HTMLIMG：" + img);
//        }
//
//        Elements miniTitleElements = doc.getElementsByClass("centent_box_sub_title");
//        for (int i = 0; i < miniTitleElements.size(); i++) {
//            String miniTitle = miniTitleElements.get(i).text();
//            LogUtil.e("HTMLminititle：" + miniTitle);
//            if (htmlBeans.size() > i) {
//                htmlBeans.get(i).miniTitle = miniTitle;
//            } else {
//                HTMLBean htmlBean = new HTMLBean();
//                htmlBean.miniTitle = miniTitle;
//                htmlBeans.add(htmlBean);
//            }
//
//        }
//
//        Elements contentElements = doc.select("p[class=centent_box_centent]");
//        for (int i = 0; i < contentElements.size(); i++) {
//            String content = contentElements.get(i).text().replace("/n", "\r\n");
//            if (htmlBeans.size() > i) {
//                htmlBeans.get(i).content = content;
//            } else {
//                HTMLBean htmlBean = new HTMLBean();
//                htmlBean.content = content;
//                htmlBeans.add(htmlBean);
//            }
//        }
//        return htmlBeans;
//    }

//    public static List<HTMLFoodBean> checkHtmlInfoByFood(String info) {
//        List<HTMLFoodBean> htmlBeans = new ArrayList<>();
//        info = info.replace("\\n", "/n").replace("\\", "");
//        LogUtil.e("HTML：" + info);
//        Document doc = Jsoup.parse(info);
//        Elements itemElements = doc.getElementsByClass("box_guide_list");
//        for (Element itemElement : itemElements) {
//            HTMLFoodBean htmlBean = new HTMLFoodBean();
//            Elements titleElements = itemElement.select("p.centent_box_span");
//            for (Element imgElement : titleElements) {
//                String title = imgElement.text();
//                LogUtil.e("HTMLtitle：" + title);
//                if (htmlBean.title == null) {
//                    htmlBean.title = new ArrayList<>();
//                }
//                htmlBean.title.add(title);
//            }
//            Elements pItemElements = itemElement.select("div.p-item");
//            for (int i = 0; i < pItemElements.size(); i++) {
//                String title = "";
//                String content = "";
//                Element element = pItemElements.get(i);
//                LogUtil.e("HTMLinfo:" + element.text());
//                Elements titles = element.select("p.p-left");
//                if (titles.size() > 0) {
//                    title = titles.get(0).text();
//                }
//
//                Elements contents = element.select("p.centent_box_centent");
//                if (contents.size() > 0) {
//                    content = contents.get(0).text().replace("/n", "\r\n");
//                }
//                LogUtil.e(title + ":" + content);
//                htmlBean.addBean(title, content);
//            }
//            Elements imgElements = itemElement.select("img[src]");
//            for (int i = 0; i < imgElements.size(); i++) {
//                String img = imgElements.get(i).attr("src");
//                if (!img.startsWith("http")) {
//                    img = "https:" + img;
//                }
//                if (img.contains("mappingId=")) {
//                    img = HttpApi.get302(img);
//                }
//                if (htmlBean.imgs == null) {
//                    htmlBean.imgs = new ArrayList<>();
//                }
//                htmlBean.imgs.add(img);
//                LogUtil.e("HTMLIMG：" + img);
//            }
//
//            htmlBeans.add(htmlBean);
//        }
//        return htmlBeans;
//    }

//    public static List<HTMLFoodBean> checkHtmlInfoByzhoubian(String info) {
//        List<HTMLFoodBean> htmlBeans = new ArrayList<>();
//        info = info.replace("\\n", "/n").replace("\\", "");
//        LogUtil.e("HTML：" + info);
//        Document doc = Jsoup.parse(info);
//        Elements itemElements = doc.getElementsByClass("zhoubian-content");
//        for (Element itemElement : itemElements) {
//            HTMLFoodBean htmlBean = new HTMLFoodBean();
//            Elements titleElements = itemElement.select("div.centent_box_span02");
//            for (Element imgElement : titleElements) {
//                String title = imgElement.text();
//                LogUtil.e("HTMLtitle：" + title);
//                if (htmlBean.title == null) {
//                    htmlBean.title = new ArrayList<>();
//                }
//                htmlBean.title.add(title);
//            }
//            Elements titItemElements = itemElement.select("p.centent_box_tit");
//            Elements conItemElements = itemElement.select("p.centent_box_centent.mar_t");
//            int forCount = -1;
//            if (titItemElements.size() > conItemElements.size()) {
//                forCount = titItemElements.size();
//            } else {
//                forCount = conItemElements.size();
//            }
//
//            for (int i = 0; i < forCount; i++) {
//                String title = "";
//                String content = "";
//                if (titItemElements.size() > i) {
//                    Element titles = titItemElements.get(i);
//                    title = titles.text();
//
//                }
//                if (conItemElements.size() > i) {
//                    Element contents = conItemElements.get(i);
//                    content = contents.text().replace("/n", "\r\n");
//
//                }
//                LogUtil.e(title + ":" + content);
//                htmlBean.addBean(title, content);
//            }
//            Elements imgElements = itemElement.select("img[src]");
//            for (int i = 0; i < imgElements.size(); i++) {
//                String img = imgElements.get(i).attr("src");
//                if (!img.startsWith("http")) {
//                    img = "https:" + img;
//                }
//                if (img.contains("mappingId=")) {
//                    img = HttpApi.get302(img);
//                }
//                if (htmlBean.imgs == null) {
//                    htmlBean.imgs = new ArrayList<>();
//                }
//                htmlBean.imgs.add(img);
//                LogUtil.e("HTMLIMG：" + img);
//            }
//
//            htmlBeans.add(htmlBean);
//        }
//
//        return htmlBeans;
//    }

//    public static List<HTMLCarBean> checkHtmlInfoByCar(String info) {
//        List<HTMLCarBean> htmlBeans = new ArrayList<>();
//        info = info.replace("\\n", "/n").replace("\\", "");
//        LogUtil.e("HTML：" + info);
//        Document doc = Jsoup.parse(info);
//        Elements itemElements = doc.getElementsByClass("box_guide_list");
//        for (Element itemElement : itemElements) {
//            HTMLCarBean htmlBean = new HTMLCarBean();
//            Elements titleElements = itemElement.select("p.centent_box_span");
//            htmlBean.title = titleElements.text();
//            htmlBean.isAir = htmlBean.title.contains("机场") || htmlBean.title.contains("飞机");
//            htmlBean.isShowTitle = !TextTool.isNullOrEmpty(htmlBean.title);
//            LogUtil.e("HTMLtitle：" + htmlBean.title);
//
//            Elements titItemElements = itemElement.select("p.centent_box_span02");
//            if (titItemElements.size() < 1) {
//                titItemElements = itemElement.select("p.centent_box_tit");
//            }
//            Elements conItemElements = itemElement.select("p.centent_box_centent.mar_t");
//            int forCount = -1;
//            if (titItemElements.size() > conItemElements.size()) {
//                forCount = titItemElements.size();
//            } else {
//                forCount = conItemElements.size();
//            }
//
//            for (int i = 0; i < forCount; i++) {
//                String title = "";
//                String content = "";
//                if (titItemElements.size() > i) {
//                    Element titles = titItemElements.get(i);
//                    title = titles.text();
//
//                }
//                if (conItemElements.size() > i) {
//                    Element contents = conItemElements.get(i);
//                    content = contents.text().replace("/n", "\r\n");
//
//                }
//                LogUtil.e(title + ":" + content);
//                if (!TextTool.isNullOrEmpty(title)) {
//                    List<String> imgs = new ArrayList<>();
//                    Elements imgElements = itemElement.select("img[src]");
//                    for (Element imgElement : imgElements) {
//                        String img = imgElement.attr("src");
//                        if (!img.startsWith("http")) {
//                            img = "https:" + img;
//                        }
//                        if (img.contains("mappingId=")) {
//                            img = HttpApi.get302(img);
//                        }
//                        imgs.add(img);
//                        LogUtil.e("图片：" + img);
//                    }
//                    if (imgs.size() > 0) {
//                        htmlBean.addBean(title, content, imgs);
//                    } else {
//                        htmlBean.addBean(title, content);
//                    }
//
//
//                }
//
//            }
//            htmlBeans.add(htmlBean);
//        }
//
//
//        return htmlBeans;
//    }

//    public static List<HTMLTraffic> checkHtmlInfoByTraffic(String info) {
//        List<HTMLTraffic> htmlBeans = new ArrayList<>();
//        try {
//            info = info.replace("\\n", "/n").replace("\\t", "/t").replace("\\", "");
//            LogUtil.e("HTML：" + info);
//            Document doc = Jsoup.parse(info);
//            Elements itemElements = doc.getElementsByClass("box_guide_list");
//            for (Element itemElement : itemElements) {
//                HTMLTraffic htmlBean = new HTMLTraffic();
//                Elements titleElements = itemElement.select("p.centent_box_span");
//                htmlBean.title = titleElements.text().replace("接驳巴士线", "").replace("/t", "").replace("/n", "");
//                htmlBean.isShow = titleElements.text().contains("接驳巴士线");
//                LogUtil.e("HTMLtitle：" + htmlBean.title);
//                if (!htmlBean.isShow) {
//                    Elements contentElements = itemElement.select("p[class=centent_box_tit]");
//                    LogUtil.e("HTMLcontent：" + contentElements.text());
//                    htmlBean.setTimeBean(contentElements.text().replace("/n", "\r\n"), "", false, false, new ArrayList<>());
//                } else {
//                    Elements titleItemElements = itemElement.select("p.centent_box_tit");
//                    Elements conElements = itemElement.select("p.centent_box_centent.mar_t");
//                    int forCount = -1;
//                    if (titleItemElements.size() > conElements.size()) {
//                        forCount = titleItemElements.size();
//                    } else {
//                        forCount = conElements.size();
//                    }
//                    for (int i = 0; i < forCount; i++) {
//                        String title = "";
//                        String content = "";
//                        boolean isShowTime = false;
//                        List<HTMLTrafficImg> imgs = new ArrayList<>();
//
//                        if (titleItemElements.size() > i) {
//                            Element titles = titleItemElements.get(i);
//                            title = titles.text();
//                            if (title.contains("时间")) {
//                                isShowTime = true;
//                            }
//                        }
//                        if (conElements.size() > i) {
//                            Element contents = conElements.get(i);
//
//                            Elements imgElements = contents.select("img[src]");
//                            if (imgElements.size() > 0) {
//                                content = "";
//                                String img = contents.html().replace("<img", "@#<img");
//                                Document docImg = Jsoup.parse(img);
//
//                                for (int i1 = 0; i1 < docImg.text().split("@#").length; i1++) {
//                                    String s = docImg.text().split("@#")[i1];
//                                    if (!TextTool.isNullOrEmpty(s)) {
//                                        if (i1 == docImg.text().split("@#").length - 1) {
//                                            imgs.add(new HTMLTrafficImg(s, false, false));
//                                        } else {
//                                            imgs.add(new HTMLTrafficImg(s, !title.contains("往返"), title.contains("往返")));
//                                        }
//
//                                    }
//                                }
//
//                                LogUtil.e("路线:" + ZSONObject.toZSONString(imgs));
//
//
//                            } else {
//                                content = contents.text();
//                                LogUtil.e("HTMLcontent：" + content);
//                            }
//
//
//                        }
//                        LogUtil.e(title + ":" + content);
//                        if (!TextTool.isNullOrEmpty(title)) {
//
//                            htmlBean.setTimeBean(title, content, true, isShowTime, imgs);
//                        }
//
//                    }
//
//                }
//
////            if (titItemElements.size() < 1) {
////                titItemElements = itemElement.select("p.centent_box_tit");
////            }
////            Elements conItemElements = itemElement.select("p.centent_box_centent.mar_t");
////            int forCount = -1;
////            if (titItemElements.size() > conItemElements.size()) {
////                forCount = titItemElements.size();
////            } else {
////                forCount = conItemElements.size();
////            }
////
////            for (int i = 0; i < forCount; i++) {
////                String title = "";
////                String content = "";
////                if (titItemElements.size() > i) {
////                    Element titles = titItemElements.get(i);
////                    title = titles.text();
////
////                }
////                if (conItemElements.size() > i) {
////                    Element contents = conItemElements.get(i);
////                    content = contents.text().replace("/n", "\r\n");
////
////                }
////                LogUtil.e(title + ":" + content);
////                if (!TextTool.isNullOrEmpty(title)) {
////                    List<String> imgs = new ArrayList<>();
////                    Elements imgElements = itemElement.select("img[src]");
////                    for (Element imgElement : imgElements) {
////                        String img = imgElement.attr("src");
////                        if (!img.startsWith("http")) {
////                            img = "https:" + img;
////                        }
////                        if (img.contains("mappingId=")) {
////                            img = HttpApi.get302(img);
////                        }
////                        imgs.add(img);
////                        LogUtil.e("图片：" + img);
////                    }
////                    if (imgs.size() > 0) {
////                        htmlBean.addBean(title, content, imgs);
////                    } else {
////                        htmlBean.addBean(title, content);
////                    }
////
////
////                }
////
////            }
//                htmlBeans.add(htmlBean);
//            }
//        } catch (Exception e) {
//            LogUtil.e("班车信息：" + e.getMessage());
//        }
//
//        return htmlBeans;
//    }

//    public static List<HTMLQABean> checkHtmlInfoByQA(String info) {
//        List<HTMLQABean> htmlBeans = new ArrayList<>();
//        info = info.replace("\\n", "/n").replace("\\t", "/t").replace("\\", "");
//        LogUtil.e("HTML：" + info);
//        try {
//
//            Document doc = Jsoup.parse(info);
//            Elements itemElements = doc.getElementsByClass("box_guide_list");
//
//            for (int i = 0; i < itemElements.size(); i++) {
//                Element itemElement = itemElements.get(i);
//                HTMLQABean htmlBean = new HTMLQABean();
//                Elements titleElements = itemElement.select("div.box_shuttleBus_text");
//                for (Element imgElement : titleElements) {
//                    String title = "";
//                    String content = "";
//                    List<HTMLImg> imgs = new ArrayList<>();
//                    if (imgElement.text().startsWith("A")) {
//                        Elements titElement = imgElement.select("p[class=centent_box_centen]");
//                        Elements conElement = imgElement.select("p.centent_box_centen.qa_text_right");
//                        title = titElement.text();
//                        content = conElement.text().replace("/n", "\r\n").replace("/t", "\t");
//                        Elements imgElements = itemElement.select("img[src]");
//                        List<String> imgInfos = new ArrayList<>();
//                        if (imgElements.size() > 0) {
//                            if (content.contains("(")) {
//                                content = content.replace("(", "@#");
//                                for (String s : content.split("@#")) {
//                                    if (!TextTool.isNullOrEmpty(s)) {
//                                        imgInfos.add("(" + s);
//                                    }
//                                }
//                            }
//                            content = "";
//                        }
//
//                        for (int i1 = 0; i1 < imgElements.size(); i1++) {
//                            String img = imgElements.get(i1).attr("src");
//                            if (!img.startsWith("http")) {
//                                img = "https:" + img;
//                            }
//                            if (img.contains("mappingId=")) {
//                                img = HttpApi.get302(img);
//                            }
//                            String imgInfo = "";
//                            if (imgInfos.size() > i1) {
//                                imgInfo = imgInfos.get(i1);
//                            }
//                            imgs.add(new HTMLImg(imgInfo, img));
//                        }
//                        htmlBean.imgs = imgs;
//                        htmlBean.num = title;
//                        htmlBean.content = content;
//                    } else {
//                        Elements conElement = imgElement.select("p[class=centent_box_q]");
//                        title = (i + 1) + ".Q：";
//                        content = conElement.text();
//                        LogUtil.e("Q::::" + title + ":" + content);
//                        htmlBean.QNum = title + "\r\n\t";
//                        htmlBean.QContent = content + "\r\n\t";
//                    }
//
//
//                }
//                LogUtil.e("QA:" + ZSONObject.toZSONString(htmlBean));
//                htmlBeans.add(htmlBean);
//            }
//        } catch (Exception e) {
//            LogUtil.e("QA:" + e.getMessage());
//        } finally {
//            return htmlBeans;
//        }
//
//
//    }

    public static List<SubSeminarBean> checkSubSeminarBean(List<SubSeminarBean> subSeminarBeans) {
        if (subSeminarBeans != null && subSeminarBeans.size() > 0) {
            for (int i = 0; i < subSeminarBeans.size(); i++) {

                if (!TextTool.isNullOrEmpty(subSeminarBeans.get(i).i_codelabs) || !("").equals(subSeminarBeans.get(i).i_codelabs)) {
                    subSeminarBeans.get(i).hmClear = true;
                    continue;
                }
                try {
                    if (subSeminarBeans.get(i).i_isTechnology != null && !TextTool.isNullOrEmpty(subSeminarBeans.get(i).i_isTechnology.toString())) {

                        subSeminarBeans.get(i).hm_isTechnology = Integer.valueOf(subSeminarBeans.get(i).i_isTechnology.toString());


                    }
                } catch (Exception e) {
                    LogUtil.e(e.getMessage());
                }
                if (subSeminarBeans.get(i).i_sortOrder != null) {
                    try {
                        subSeminarBeans.get(i).hm_sortOrder = Integer.valueOf(subSeminarBeans.get(i).i_sortOrder.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                subSeminarBeans.get(i).hmDay = TimeHelper.getDateByDay(Long.valueOf(subSeminarBeans.get(i).startTime));
                subSeminarBeans.get(i).hmStartTime = TimeHelper.getDateByTime(Long.valueOf(subSeminarBeans.get(i).startTime));
                subSeminarBeans.get(i).hmEndTime = TimeHelper.getDateByTime(Long.valueOf(subSeminarBeans.get(i).endTime));
                subSeminarBeans.get(i).hmStartAndEndTime = subSeminarBeans.get(i).hmStartTime + "-"
                        + subSeminarBeans.get(i).hmEndTime;
                subSeminarBeans.get(i).hmName = subSeminarBeans.get(i).name.contains("|") ?
                        subSeminarBeans.get(i).name.substring(0, subSeminarBeans.get(i).name.indexOf("|")) : subSeminarBeans.get(i).name;
                subSeminarBeans.get(i).hmContent = subSeminarBeans.get(i).hotel;
                subSeminarBeans.get(i).hmCategory = new ArrayList<>();
                String category = String.valueOf(ZSONObject.toZSONString(subSeminarBeans.get(i).category)).replace("\\", "");
                if (category.startsWith("\"")) {
                    category = category.substring(1, category.length() - 1);
                }
                try {
                    if (!TextTool.isNullOrEmpty(category)) {
                        List<Category> categorys = ZSONArray.stringToClassList(category, Category.class);
                        if (categorys != null && categorys.size() > 0) {
                            for (Category categoryBean : categorys) {
                                if (categoryBean.type.equals("allsub")) {
                                    String nodes = ZSONObject.toZSONString(categoryBean.nodes);
                                    try {
                                        if (!TextTool.isNullOrEmpty(nodes)) {
                                            List<String> nodesList = ZSONArray.stringToClassList(nodes, String.class);
                                            if (nodesList != null && nodesList.size() > 0) {
                                                subSeminarBeans.get(i).hmCategory.addAll(nodesList);
                                            }
                                        }
                                    } catch (Exception e) {
                                        LogUtil.e("nodes处理异常" + e.getMessage());
                                        LogUtil.e("nodes处理异常" + category);
                                    }

                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    LogUtil.e("category处理异常" + e.getMessage());
                    LogUtil.e("category处理异常" + category);
                }
                String i_typeStr = ZSONObject.toZSONString(subSeminarBeans.get(i).i_type);
                if (!TextTool.isNullOrEmpty(i_typeStr)) {
                    if (i_typeStr.contains("主题演讲")) {
                        subSeminarBeans.get(i).isMain = true;
                        subSeminarBeans.get(i).hmTitle = PageAction.MAIN;
                    } else if (i_typeStr.contains("开发者活动")) {
                        subSeminarBeans.get(i).hmTitle = subSeminarBeans.get(i).hm_isTechnology == 1 ? PageAction.BREAK_FAST : CODE;

                        if (subSeminarBeans.get(i).hmTitle.equals(CODE)) {
                            subSeminarBeans.get(i).isCode = true;
                            LogUtil.e("1111111code议程：" + subSeminarBeans.get(i).name);

                        } else {
                            subSeminarBeans.get(i).isSub = true;
                        }

                    } else {
                        subSeminarBeans.get(i).isSub = true;
                        subSeminarBeans.get(i).hmTitle = subSeminarBeans.get(i).hm_isTechnology == 1 ? PageAction.SONG_HU : PageAction.TECHNOLOGY;
                    }

                    String i_TechHour = ZSONObject.toZSONString(subSeminarBeans.get(i).i_TechHour).replaceAll("&quot", "");
                    if (i_TechHour.startsWith("\"")) {
                        i_TechHour = i_TechHour.substring(1, i_TechHour.length() - 1);
                    }
                    try {
                        if (!TextTool.isNullOrEmpty(i_TechHour)) {
                            List<String> techHours = ZSONArray.stringToClassList(i_TechHour, String.class);
                            if (techHours != null && techHours.size() > 0) {
                                LogUtil.e("Tech:" + techHours.get(0));
                                subSeminarBeans.get(i).hm_TechHour = techHours.get(0);
                                subSeminarBeans.get(i).hmTitle = TECHOUR;
                                subSeminarBeans.get(i).isSub = true;
                                subSeminarBeans.get(i).hmName = techHours.get(0).contains("|") ? techHours.get(0).substring(0, techHours.get(0).indexOf("|")) : techHours.get(0);
                                Pattern p = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
                                Matcher matcher = p.matcher(subSeminarBeans.get(i).intro);
                                subSeminarBeans.get(i).hmContent = matcher.replaceAll(" ").replaceAll("&quot", "");
                                LogUtil.e(subSeminarBeans.get(i).hmContent);
                            }
                        }
                    } catch (Exception e) {
                        LogUtil.e("techhour处理异常" + e.getMessage());
                        LogUtil.e("techhour处理异常" + i_TechHour);
                    }
                    String i_Hudong = ZSONObject.toZSONString(subSeminarBeans.get(i).i_Hudong).replaceAll("&quot", "");
                    if (i_Hudong.startsWith("\"")) {
                        i_Hudong = i_Hudong.substring(1, i_Hudong.length() - 1);
                    }
                    try {
                        if (!TextTool.isNullOrEmpty(i_Hudong) && i_Hudong.startsWith("[")) {
                            String hudong = "";
                            if (i_Hudong.startsWith("[")) {
                                List<String> hudongs = ZSONArray.stringToClassList(i_Hudong, String.class);
                                if (hudongs != null && hudongs.size() > 0) {
                                    hudong = hudongs.get(0);
                                }
                            } else {
                                hudong = i_Hudong;
                            }
                            subSeminarBeans.get(i).hm_Hudong = hudong;
                            LogUtil.e("Tech:" + hudong);
                            subSeminarBeans.get(i).hmTitle = PageAction.INTERACTION;
                            subSeminarBeans.get(i).isSub = true;
                            subSeminarBeans.get(i).hmName = hudong.contains("|") ? hudong.substring(0, hudong.indexOf("|")) : hudong;
                            Pattern p = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
                            Matcher matcher = p.matcher(subSeminarBeans.get(i).intro);
                            subSeminarBeans.get(i).hmContent = matcher.replaceAll(" ").replaceAll("&quot", "");
                            LogUtil.e(subSeminarBeans.get(i).hmContent);
                        }
                    } catch (Exception e) {
                        LogUtil.e("i_Hudong处理异常" + e.getMessage());
                        LogUtil.e("i_Hudong处理异常" + i_Hudong);
                    }

                }

                if ((subSeminarBeans.get(i).isMain || subSeminarBeans.get(i).isCode) && subSeminarBeans.get(i).agendaList != null && subSeminarBeans.get(i).agendaList.size() > 0) {
                    String subSeminarStr = ZSONObject.toZSONString(subSeminarBeans.get(i).agendaList);
                    List<AgendasBean> agendasBeans = null;
                    try {
                        agendasBeans = ZSONArray.stringToClassList(subSeminarStr, AgendasBean.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    subSeminarBeans.get(i).hmAgenda = new ArrayList<>();
                    if (agendasBeans != null) {
                        for (AgendasBean agendasBean : agendasBeans) {
                            for (AgendasBean.Agendas agenda : agendasBean.agendas) {
                                Agendas agendas = new Agendas();
                                agendas.name = agenda.name;
                                agendas.startTime = TimeHelper.getDateByTime(Long.valueOf(agenda.startTime));
                                agendas.endTime = TimeHelper.getDateByTime(Long.valueOf(agenda.endTime));
                                agendas.subSeminarId = subSeminarBeans.get(i).subSeminarId;
                                if (agenda.guests != null && agenda.guests.size() > 0) {
                                    List<Guest> guests = null;
                                    try {
                                        guests = ZSONArray.stringToClassList(ZSONObject.toZSONString(agenda.guests), Guest.class);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (guests != null && guests.size() > 0) {

                                        for (Guest guest : guests) {
//                                            if (agendas.allAgendaName == null) {
//                                                agendas.allAgendaName = new ArrayList<>();
//                                            }
//                                            agendas.allAgendaName.add(guest.name + " | " + guest.duty+"\r\n");
                                            if (TextTool.isNullOrEmpty(agendas.agendaName)) {
                                                agendas.agendaName = "";
                                            }

                                            agendas.agendaName = agendas.agendaName + guest.name + " | " + guest.duty + "\r\n";
                                        }


//                                        agendas.agendaName = guests.get(0).name + " | " + guests.get(0).duty;

                                    }
                                }

                                subSeminarBeans.get(i).hmAgenda.add(agendas);
                            }
                        }
                    }
                    subSeminarBeans.get(i).agendaList = new ArrayList<>();
                    LogUtil.e("日程数据：" + ZSONObject.toZSONString(subSeminarBeans.get(i).hmAgenda));
                }

                if (!TextTool.isNullOrEmpty(subSeminarBeans.get(i).intro)) {
                    if (subSeminarBeans.get(i).hmTitle.equals(PageAction.BREAK_FAST)) {
                        Pattern p = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
                        Matcher matcher = p.matcher(subSeminarBeans.get(i).intro);
                        subSeminarBeans.get(i).intro = matcher.replaceAll(" ");
                        if (subSeminarBeans.get(i).intro.contains("|")) {
                            subSeminarBeans.get(i).hmContent = subSeminarBeans.get(i).intro.substring(0, subSeminarBeans.get(i).intro.indexOf("|"));
                        }
                        LogUtil.e("hmContent:" + subSeminarBeans.get(i).hmContent);
                    } else if (subSeminarBeans.get(i).hmTitle.equals(CODE)) {
                        Pattern p = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
                        Matcher matcher = p.matcher(subSeminarBeans.get(i).intro);
                        subSeminarBeans.get(i).intro = matcher.replaceAll(" ");
                        if (subSeminarBeans.get(i).intro.contains("|")) {
                            subSeminarBeans.get(i).intro = subSeminarBeans.get(i).intro.substring(0, subSeminarBeans.get(i).intro.indexOf("|"));
                        }
                    } else {
                        Pattern p = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
                        Matcher matcher = p.matcher(subSeminarBeans.get(i).intro);
                        subSeminarBeans.get(i).intro = matcher.replaceAll(" ").replaceAll("&quot", "").replaceAll("&amp;", "&");
                        LogUtil.e("intro:" + subSeminarBeans.get(i).intro);
                    }


                }

            }
            Iterator<SubSeminarBean> it = subSeminarBeans.iterator();
            while (it.hasNext()) {
                SubSeminarBean subSeminarBean = it.next();
                if (subSeminarBean.hmClear) {
                    it.remove();
                }
            }


        }

        return subSeminarBeans;
    }

    public static List<SubSeminarBean> checkSubSeminarBeanByCard(List<SubSeminarBean> subSeminarBeans) {
        if (subSeminarBeans != null && subSeminarBeans.size() > 0) {
            for (int i = 0; i < subSeminarBeans.size(); i++) {
                if (subSeminarBeans.get(i).i_isTechnology != null) {
                    try {
                        subSeminarBeans.get(i).hm_isTechnology = Integer.valueOf(subSeminarBeans.get(i).i_isTechnology.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (subSeminarBeans.get(i).i_sortOrder != null) {
                    try {
                        subSeminarBeans.get(i).hm_sortOrder = Integer.valueOf(subSeminarBeans.get(i).i_sortOrder.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                subSeminarBeans.get(i).hmDay = TimeHelper.getDateByDay(Long.valueOf(subSeminarBeans.get(i).startTime));
                subSeminarBeans.get(i).hmStartTime = TimeHelper.getDateByTime(Long.valueOf(subSeminarBeans.get(i).startTime));
                subSeminarBeans.get(i).hmEndTime = TimeHelper.getDateByTime(Long.valueOf(subSeminarBeans.get(i).endTime));
                subSeminarBeans.get(i).hmStartAndEndTime = subSeminarBeans.get(i).hmStartTime + "-"
                        + subSeminarBeans.get(i).hmEndTime;
                subSeminarBeans.get(i).hmName = subSeminarBeans.get(i).name;
                subSeminarBeans.get(i).hmContent = subSeminarBeans.get(i).hotel;
                subSeminarBeans.get(i).hmCategory = new ArrayList<>();
                if (!TextTool.isNullOrEmpty(subSeminarBeans.get(i).intro)) {
                    Pattern p = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = p.matcher(subSeminarBeans.get(i).intro);
                    subSeminarBeans.get(i).intro = matcher.replaceAll(" ").replaceAll("&quot", "").replaceAll("&amp;", "&");
                    LogUtil.e("intro:" + subSeminarBeans.get(i).intro);
                }
                String category = String.valueOf(ZSONObject.toZSONString(subSeminarBeans.get(i).category)).replace("\\", "");
                if (category.startsWith("\"")) {
                    category = category.substring(1, category.length() - 1);
                }
                try {
                    if (!TextTool.isNullOrEmpty(category)) {
                        List<Category> categorys = ZSONArray.stringToClassList(category, Category.class);
                        if (categorys != null && categorys.size() > 0) {
                            for (Category categoryBean : categorys) {
                                if (categoryBean.type.equals("allsub")) {
                                    String nodes = ZSONObject.toZSONString(categoryBean.nodes);
                                    try {
                                        if (!TextTool.isNullOrEmpty(nodes)) {
                                            List<String> nodesList = ZSONArray.stringToClassList(nodes, String.class);
                                            if (nodesList != null && nodesList.size() > 0) {
                                                subSeminarBeans.get(i).hmCategory.addAll(nodesList);
                                            }
                                        }
                                    } catch (Exception e) {
                                        LogUtil.e("nodes处理异常" + e.getMessage());
                                        LogUtil.e("nodes处理异常" + category);
                                    }

                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    LogUtil.e("category处理异常" + e.getMessage());
                    LogUtil.e("category处理异常" + category);
                }
                String i_typeStr = ZSONObject.toZSONString(subSeminarBeans.get(i).i_type);
                if (!TextTool.isNullOrEmpty(i_typeStr)) {
                    if (i_typeStr.contains("主题演讲")) {
                        subSeminarBeans.get(i).isMain = true;
                        subSeminarBeans.get(i).hmTitle = PageAction.MAIN;
                    } else if (i_typeStr.contains("开发者活动")) {
                        subSeminarBeans.get(i).hmTitle = subSeminarBeans.get(i).hm_isTechnology == 1 ? PageAction.BREAK_FAST : CODE;

                        if (subSeminarBeans.get(i).hmTitle.equals(CODE)) {
                            subSeminarBeans.get(i).isCode = true;
                            LogUtil.e("code议程：" + subSeminarBeans.get(i).name);
//                            if (!TextTool.isNullOrEmpty(subSeminarBeans.get(i).i_codelabs) || !("").equals(subSeminarBeans.get(i).i_codelabs)) {
//                                subSeminarBeans.get(i).hmName = subSeminarBeans.get(i).i_codelabs.contains("|") ?
//                                        subSeminarBeans.get(i).i_codelabs.substring(0, subSeminarBeans.get(i).i_codelabs.indexOf("|")) : subSeminarBeans.get(i).i_codelabs;
//                            }

                        } else {
                            subSeminarBeans.get(i).isSub = true;
                        }

                    } else {
                        subSeminarBeans.get(i).isSub = true;
                        subSeminarBeans.get(i).hmTitle = subSeminarBeans.get(i).hm_isTechnology == 1 ? PageAction.SONG_HU : PageAction.TECHNOLOGY;
                    }

                    String i_TechHour = ZSONObject.toZSONString(subSeminarBeans.get(i).i_TechHour).replaceAll("&quot", "");
                    if (i_TechHour.startsWith("\"")) {
                        i_TechHour = i_TechHour.substring(1, i_TechHour.length() - 1);
                    }
                    try {
                        if (!TextTool.isNullOrEmpty(i_TechHour)) {
                            List<String> techHours = ZSONArray.stringToClassList(i_TechHour, String.class);
                            if (techHours != null && techHours.size() > 0) {
                                LogUtil.e("Tech:" + techHours.get(0));
                                subSeminarBeans.get(i).hmTitle = TECHOUR;
                                subSeminarBeans.get(i).isSub = true;
                                subSeminarBeans.get(i).hmName = techHours.get(0).contains("|") ? techHours.get(0).substring(0, techHours.get(0).indexOf("|")) : techHours.get(0);
                                Pattern p = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
                                Matcher matcher = p.matcher(subSeminarBeans.get(i).intro);
                                subSeminarBeans.get(i).hmContent = matcher.replaceAll(" ").replaceAll("&quot", "");
                                LogUtil.e(subSeminarBeans.get(i).hmContent);
                            }
                        }
                    } catch (Exception e) {
                        LogUtil.e("techhour处理异常" + e.getMessage());
                        LogUtil.e("techhour处理异常" + i_TechHour);
                    }
                    String i_Hudong = ZSONObject.toZSONString(subSeminarBeans.get(i).i_Hudong).replaceAll("&quot", "");
                    if (i_Hudong.startsWith("\"")) {
                        i_Hudong = i_Hudong.substring(1, i_Hudong.length() - 1);
                    }
                    try {
                        if (!TextTool.isNullOrEmpty(i_Hudong) && i_Hudong.startsWith("[")) {
                            String hudong = "";
                            if (i_Hudong.startsWith("[")) {
                                List<String> hudongs = ZSONArray.stringToClassList(i_Hudong, String.class);
                                if (hudongs != null && hudongs.size() > 0) {
                                    hudong = hudongs.get(0);
                                }
                            } else {
                                hudong = i_Hudong;
                            }
                            LogUtil.e("Tech:" + hudong);
                            subSeminarBeans.get(i).hmTitle = PageAction.INTERACTION;
                            subSeminarBeans.get(i).isSub = true;
                            subSeminarBeans.get(i).hmName = hudong.contains("|") ? hudong.substring(0, hudong.indexOf("|")) : hudong;
                            Pattern p = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
                            Matcher matcher = p.matcher(subSeminarBeans.get(i).intro);
                            subSeminarBeans.get(i).hmContent = matcher.replaceAll(" ").replaceAll("&quot", "");
                            LogUtil.e(subSeminarBeans.get(i).hmContent);
                        }
                    } catch (Exception e) {
                        LogUtil.e("i_Hudong处理异常" + e.getMessage());
                        LogUtil.e("i_Hudong处理异常" + i_Hudong);
                    }
//                    if (subSeminarBeans.get(i).hmTitle .equals(CODE)) {
//                        if (!TextTool.isNullOrEmpty(subSeminarBeans.get(i).i_codelabs) || !("").equals(subSeminarBeans.get(i).i_codelabs)) {
//                            subSeminarBeans.get(i).hmName = subSeminarBeans.get(i).i_codelabs.contains("|") ?
//                        subSeminarBeans.get(i).i_codelabs.substring(0, subSeminarBeans.get(i).i_codelabs.indexOf("|")) : subSeminarBeans.get(i).i_codelabs;
//                        }
//                    }


                }

                if ((subSeminarBeans.get(i).isMain || subSeminarBeans.get(i).isCode) && subSeminarBeans.get(i).agendaList != null && subSeminarBeans.get(i).agendaList.size() > 0) {
                    String subSeminarStr = ZSONObject.toZSONString(subSeminarBeans.get(i).agendaList);
                    List<AgendasBean> agendasBeans = null;
                    try {
                        agendasBeans = ZSONArray.stringToClassList(subSeminarStr, AgendasBean.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    subSeminarBeans.get(i).hmAgenda = new ArrayList<>();
                    if (agendasBeans != null) {
                        for (AgendasBean agendasBean : agendasBeans) {
                            for (AgendasBean.Agendas agenda : agendasBean.agendas) {
                                Agendas agendas = new Agendas();
                                agendas.name = agenda.name;
                                agendas.startTime = TimeHelper.getDateByTime(Long.valueOf(agenda.agendaDate));
                                agendas.endTime = TimeHelper.getDateByTime(Long.valueOf(agenda.endTime));
                                if (agenda.guests != null && agenda.guests.size() > 0) {
                                    List<Guest> guests = null;
                                    try {
                                        guests = ZSONArray.stringToClassList(ZSONObject.toZSONString(agenda.guests), Guest.class);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (guests != null && guests.size() > 0) {
                                        agendas.agendaName = guests.get(0).name + "|" + guests.get(0).duty;

                                    }
                                }
                                subSeminarBeans.get(i).hmAgenda.add(agendas);
                            }
                        }
                    }
                    subSeminarBeans.get(i).agendaList = new ArrayList<>();
                    LogUtil.e("日程数据：" + ZSONObject.toZSONString(subSeminarBeans.get(i).hmAgenda));
                }

            }

        }
        return subSeminarBeans;
    }

    public static List<SubSeminarBean> search(List<SubSeminarBean> subSeminarBeans, String
            key, List<String> tab) {
        List<SubSeminarBean> backSubSeminarBeans = new ArrayList<>();
        List<String> subs = MyApplication.getidlist();
        if (subSeminarBeans != null && subSeminarBeans.size() > 0) {
            for (int i = 0; i < subSeminarBeans.size(); i++) {
                if ((!TextTool.isNullOrEmpty(key) && !(subSeminarBeans.get(i).hmName.contains(key)) || !subSeminarBeans.get(i).hmName.contains(key))) {
                    continue;
                }

                if (tab != null && tab.size() > 0) {
                    boolean bn = false;
                    for (String s : tab) {
                        if (subSeminarBeans.get(i).hmCategory.contains(s)) {
                            bn = true;
                        }

                    }


                    if (!bn) {
                        continue;
                    }

                }
                if (subs != null && subs.size() > 0) {
                    subSeminarBeans.get(i).isSelect = subs.contains(subSeminarBeans.get(i).subSeminarId);
                }
                backSubSeminarBeans.add(subSeminarBeans.get(i));
            }
        }
        return backSubSeminarBeans;
    }

    public static SubSeminarList searchSubSeminarBean(List<SubSeminarBean> subSeminarBeans, String
            key, List<String> tab) {

        SubSeminarList subSeminarList = new SubSeminarList();
        boolean isSelectShow = MyApplication.getISNormalTicket();
        if (subSeminarBeans != null && subSeminarBeans.size() > 0) {
            for (SubSeminarBean subSeminarBean : subSeminarBeans) {
                switch (subSeminarBean.hmTitle) {
                    case PageAction.MAIN:
                        if (subSeminarList.main == null) {
                            subSeminarList.main = new ArrayList<>();
                        }
                        subSeminarList.main.add(subSeminarBean);
                        break;
                    case PageAction.BREAK_FAST:
                        if (subSeminarList.breakfast == null) {
                            subSeminarList.breakfast = new ArrayList<>();
                        }
                        subSeminarList.breakfast.add(subSeminarBean);
                        break;
                    case CODE:
                        if (subSeminarList.code == null) {
                            subSeminarList.code = new ArrayList<>();
                        }
                        subSeminarList.code.add(subSeminarBean);
                        break;
                    case PageAction.INTERACTION:
                        if (subSeminarList.interaction == null) {
                            subSeminarList.interaction = new ArrayList<>();
                        }
                        subSeminarList.interaction.add(subSeminarBean);
                        break;
                    case PageAction.SONG_HU:
                        if (subSeminarList.songhu == null) {
                            subSeminarList.songhu = new ArrayList<>();
                        }
                        subSeminarBean.isSelectShow = !isSelectShow;

                        subSeminarList.songhu.add(subSeminarBean);
                        break;
                    case PageAction.TECHNOLOGY:
                        if (subSeminarList.technology == null) {
                            subSeminarList.technology = new ArrayList<>();
                        }
                        subSeminarList.technology.add(subSeminarBean);
                        break;
                    case TECHOUR:
                        if (subSeminarList.techour == null) {
                            subSeminarList.techour = new ArrayList<>();
                        }
                        subSeminarList.techour.add(subSeminarBean);
                        break;
                }

            }
            if (subSeminarList.interaction != null && subSeminarList.interaction.size() > 0) {
                subSeminarList.interaction = checkInteraction(subSeminarList.interaction);
            }
            if (subSeminarList.techour != null && subSeminarList.techour.size() > 0) {
                subSeminarList.techour = checkTechour(subSeminarList.techour);
            }


        }
//
//        subSeminarList.songhu =null;
//        subSeminarList.main =null;
//        subSeminarList.breakfast =null;
//        subSeminarList.code =null;
//        subSeminarList.interaction =null;
//        subSeminarList.technology =null;


        //        松湖论坛倒序
        if (subSeminarList.songhu != null && subSeminarList.songhu.size() > 0) {
            subSeminarList.songhu = search(subSeminarList.songhu, key, tab);
            subSeminarList.songhu = sortSubSeminarBeanBySort(subSeminarList.songhu);
        } else {
            subSeminarList.songhu = null;
        }
        if (subSeminarList.techour != null && subSeminarList.techour.size() > 0) {
            subSeminarList.techour = search(subSeminarList.techour, key, tab);
            subSeminarList.techour = sortSubSeminarBeanBySort(subSeminarList.techour);
        } else {
            subSeminarList.techour = null;
        }
//        主题演讲倒序
        if (subSeminarList.main != null && subSeminarList.main.size() > 0) {
            subSeminarList.main = search(subSeminarList.main, key, tab);
            subSeminarList.main = sortSubSeminarBeanBySort(subSeminarList.main);
        } else {
            subSeminarList.main = null;
        }
//        早餐倒序
        if (subSeminarList.breakfast != null && subSeminarList.breakfast.size() > 0) {
            subSeminarList.breakfast = search(subSeminarList.breakfast, key, tab);
            subSeminarList.breakfast = sortSubSeminarBeanBySort(subSeminarList.breakfast);
        } else {
            subSeminarList.breakfast = null;
        }

//        code倒序
        if (subSeminarList.code != null && subSeminarList.code.size() > 0) {
            subSeminarList.code = search(subSeminarList.code, key, tab);
            subSeminarList.code = sortSubSeminarBeanBySort(subSeminarList.code);
        } else {
            subSeminarList.code = null;
        }

//        code倒序
        if (subSeminarList.technology != null && subSeminarList.technology.size() > 0) {
            subSeminarList.technology = search(subSeminarList.technology, key, tab);
            subSeminarList.technology = sortSubSeminarBeanBySort(subSeminarList.technology);
        } else {
            subSeminarList.technology = null;
        }
//        code倒序
        if (subSeminarList.interaction != null && subSeminarList.interaction.size() > 0) {
            subSeminarList.interaction = search(subSeminarList.interaction, key, tab);
        } else {
            subSeminarList.interaction = null;
        }


        //        松湖论坛倒序
        if (subSeminarList.songhu != null && subSeminarList.songhu.size() < 1) {

            subSeminarList.songhu = null;
        }
        if (subSeminarList.techour != null && subSeminarList.techour.size() < 1) {

            subSeminarList.techour = null;
        }
//        主题演讲倒序
        if (subSeminarList.main != null && subSeminarList.main.size() < 1) {

            subSeminarList.main = null;
        }
//        早餐倒序
        if (subSeminarList.breakfast != null && subSeminarList.breakfast.size() < 1) {

            subSeminarList.breakfast = null;
        }

//        code倒序
        if (subSeminarList.code != null && subSeminarList.code.size() < 1) {
            subSeminarList.code = null;
        }

//        code倒序
        if (subSeminarList.technology != null && subSeminarList.technology.size() < 1) {

            subSeminarList.technology = null;
        }
//        code倒序
        if (subSeminarList.interaction != null && subSeminarList.interaction.size() < 1) {

            subSeminarList.interaction = null;
        }


        LogUtil.e("main搜索到议程：" + (subSeminarList.main != null ? subSeminarList.main.size() : 0));
        LogUtil.e("technology搜索到议程：" + (subSeminarList.technology != null ? subSeminarList.technology.size() : 0));
        LogUtil.e("songhu搜索到议程：" + (subSeminarList.songhu != null ? subSeminarList.songhu.size() : 0));
        LogUtil.e("breakfast搜索到议程：" + (subSeminarList.breakfast != null ? subSeminarList.breakfast.size() : 0));
        LogUtil.e("code搜索到议程：" + (subSeminarList.code != null ? subSeminarList.code.size() : 0));
        LogUtil.e("techour搜索到议程：" + (subSeminarList.techour != null ? subSeminarList.techour.size() : 0));
        LogUtil.e("interaction搜索到议程：" + (subSeminarList.interaction != null ? subSeminarList.interaction.size() : 0));


        return subSeminarList;
    }

    public static List<SubSeminarBean> checkInteraction(List<SubSeminarBean> subSeminarBeans) {
        List<SubSeminarBean> back = new ArrayList<>();
        subSeminarBeans = sortSubSeminarBeanByA(subSeminarBeans);
        if (MyApplication.hdBeans != null && MyApplication.hdBeans.size() > 0) {
            for (HdBean hdBean : MyApplication.hdBeans) {
                SubSeminarBean newSub = new SubSeminarBean();
                newSub.isSub = true;
                newSub.subSeminarId = hdBean.id;
                for (SubSeminarBean subSeminarBean : subSeminarBeans) {
                    if (hdBean.title.equals(subSeminarBean.hm_Hudong)) {
                        if (newSub.hmhdList == null) {
                            newSub.hmhdList = new ArrayList<>();
                        }
                        Guest guest = new Guest();
                        guest.name = subSeminarBean.name.contains("|") ? subSeminarBean.name.substring(0, subSeminarBean.name.indexOf("|")) : subSeminarBean.name;
                        guest.startTime = subSeminarBean.startTime;
                        LogUtil.e("互动体验：" + guest.name + guest.startTime);
                        LogUtil.e("互动体验：" + guest.name + subSeminarBean.hm_sortOrder);
                        LogUtil.e("互动体验1111：" + subSeminarBean.intro);
                        Pattern p = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
                        Matcher matcher = p.matcher(subSeminarBean.intro);
                        guest.duty = matcher.replaceAll(" ").replaceAll("&quot", "").replace("|", "\r\n\t");
                        newSub.hmhdList.add(guest);
                    }
                }
                if (newSub.hmhdList != null && newSub.hmhdList.size() > 0) {
                    newSub.hmDay = TimeHelper.getDateByDay(Long.valueOf(hdBean.extra.get(0).customText_58.get(0).value));
                    newSub.hmStartTime = TimeHelper.getDateByTime(Long.valueOf(hdBean.extra.get(0).customText_58.get(0).value));
                    newSub.hmEndTime = TimeHelper.getDateByTime(Long.valueOf(hdBean.extra.get(0).customText_59.get(0).value));
                    newSub.hmStartAndEndTime = newSub.hmStartTime + "-"
                            + newSub.hmEndTime;
                    newSub.hmName = hdBean.title.contains("|") ? hdBean.title.substring(0, hdBean.title.indexOf("|")) : hdBean.title;
                    newSub.hmContent = hdBean.summary.contains("#") ? hdBean.summary.substring(0, hdBean.summary.indexOf("#")) :
                            hdBean.summary;
                    newSub.hmCategory = new ArrayList<>();
                    String tabs = hdBean.summary.contains("#") ? hdBean.summary.substring(hdBean.summary.indexOf("#")) : "";
                    List<String> tab = Arrays.asList(tabs.split("|"));
                    newSub.hmCategory.addAll(tab);
                    back.add(newSub);
                }
            }
        }
        return back;
    }

    public static List<SubSeminarBean> checkTechour(List<SubSeminarBean> subSeminarBeans) {
        List<SubSeminarBean> back = new ArrayList<>();
        subSeminarBeans = sortSubSeminarBeanByA(subSeminarBeans);

        if (MyApplication.tHBeans != null && MyApplication.tHBeans.size() > 0) {
            for (HdBean hdBean : MyApplication.tHBeans) {
                SubSeminarBean newSub = new SubSeminarBean();
                newSub.isSub = true;
                newSub.subSeminarId = hdBean.id;
                newSub.hm_sortOrder = hdBean.order;
                for (SubSeminarBean subSeminarBean : subSeminarBeans) {
                    if (hdBean.title.equals(subSeminarBean.hm_TechHour)) {
                        if (newSub.hmhdList == null) {
                            newSub.hmhdList = new ArrayList<>();
                        }
                        Guest guest = new Guest();
                        guest.name = subSeminarBean.name;
                        guest.startTime = subSeminarBean.startTime;
                        LogUtil.e("Th：" + guest.name + guest.startTime);
//                        if (!TextTool.isNullOrEmpty(subSeminarBean.intro)) {
//                            Pattern p = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
//                            Matcher matcher = p.matcher(subSeminarBean.intro);
//                            guest.duty = matcher.replaceAll(" ").replaceAll("&quot", "").replace("|", "\r\n\t");
//                        } else {
//                            guest.duty = "";
//                        }

                        newSub.hmhdList.add(guest);
                    }
                }
//                if (newSub.hmhdList != null && newSub.hmhdList.size() > 0) {
//                    newSub.hmhdList = sortGuest(newSub.hmhdList);
//                }


                if (newSub.hmhdList != null && newSub.hmhdList.size() > 0) {
                    LogUtil.e("Techour排序" + ZSONObject.toZSONString((hdBean.extra)));
                    if (hdBean.extra != null && hdBean.extra.size() > 0 && hdBean.extra.get(0) != null && hdBean.extra.get(0).customText_58 != null && hdBean.extra.get(0).customText_58.size() > 0) {
                        try {
                            newSub.hmDay = TimeHelper.getDateByDay(Long.valueOf(hdBean.extra.get(0).customText_58.get(0).value));
                            newSub.hmStartTime = TimeHelper.getDateByTime(Long.valueOf(hdBean.extra.get(0).customText_58.get(0).value));
                        } catch (Exception e) {
                            LogUtil.e("Techour时间转换异常：" + e.getMessage());
                        }
                    }
                    if (hdBean.extra != null && hdBean.extra.size() > 0 && hdBean.extra.get(0) != null && hdBean.extra.get(0).customText_59 != null && hdBean.extra.get(0).customText_59.size() > 0) {
                        try {
                            newSub.hmEndTime = TimeHelper.getDateByTime(Long.valueOf(hdBean.extra.get(0).customText_59.get(0).value));
                        } catch (Exception e) {
                            LogUtil.e("Techour结束时间转换异常：" + e.getMessage());
                        }
                    }
//                    if (hdBean.extra != null && hdBean.extra.size() > 0 && hdBean.extra.get(0) != null && hdBean.extra.get(0).customText_57 != null
//                            && hdBean.extra.get(0).customText_57.size() > 0) {
//                        try {
//                            newSub.hm_sortOrder = Integer.valueOf(hdBean.extra.get(0).customText_57.get(0).value);
//                        } catch (Exception e) {
//                            LogUtil.e("排序转换异常：" + e.getMessage());
//                        }
//
//                    }
                    newSub.hmStartAndEndTime = newSub.hmStartTime + "-"
                            + newSub.hmEndTime;
                    newSub.hmName = hdBean.title.contains("|") ? hdBean.title.substring(0, hdBean.title.indexOf("|")) : hdBean.title;
                    newSub.hmContent = hdBean.summary.contains("#") ? hdBean.summary.substring(0, hdBean.summary.indexOf("#")) :
                            hdBean.summary;
                    newSub.intro = hdBean.summary.contains("#") ? hdBean.summary.substring(0, hdBean.summary.indexOf("#")) :
                            hdBean.summary;

                    newSub.hmCategory = new ArrayList<>();
                    String tabs = hdBean.summary.contains("#") ? hdBean.summary.substring(hdBean.summary.indexOf("#") + 1) : "";
                    LogUtil.e("TH:" + tabs);
                    List<String> tab = Arrays.asList(tabs.split("\\|"));
                    LogUtil.e("TH:" + ZSONObject.toZSONString(tab));
                    newSub.hmCategory.addAll(tab);
                    back.add(newSub);
                }
            }
        }
        back = sortSubSeminarBeanBySort(back);
        return back;
    }

    public static List<SubSeminarBean> reserveSubSeminarBean(List<SubSeminarBean> subSeminarBeans) {

        List<SubSeminarBean> backSubSeminarBeans = new ArrayList<>();
        List<String> subs = MyApplication.getidlist();

        if (subSeminarBeans != null && subSeminarBeans.size() > 0 && subs != null && subs.size() > 0) {
            for (int i = 0; i < subSeminarBeans.size(); i++) {
                LogUtil.e("预定查询：" + ZSONObject.toZSONString(subSeminarBeans.get(i)));
//                if (subs.contains(subSeminarBeans.get(i).subSeminarId)
//                ) {
//                    LogUtil.e("我的预定查询：" + subSeminarBeans.get(i).hmTitle);
//                    backSubSeminarBeans.add(subSeminarBeans.get(i));
//                }
                if (subs.contains(subSeminarBeans.get(i).subSeminarId)
                        && !subSeminarBeans.get(i).hmTitle.equals(CODE)
                        && !subSeminarBeans.get(i).hmTitle.equals(TECHOUR)) {
                    LogUtil.e("我的预定查询：" + subSeminarBeans.get(0).hmTitle);
                    backSubSeminarBeans.add(subSeminarBeans.get(i));
                }
            }

        }
        return backSubSeminarBeans;
    }

    public static List<SubSeminarBean> reserveSubSeminarBeanBy22(List<SubSeminarBean> subSeminarBeans) {

        List<SubSeminarBean> backSubSeminarBeans = new ArrayList<>();
        List<String> subs = MyApplication.getidlist();
        if (subs == null) {
            subs = new ArrayList<>();
        }
        if (subSeminarBeans != null && subSeminarBeans.size() > 0) {
            for (int i = 0; i < subSeminarBeans.size(); i++) {
                if (subs.contains(subSeminarBeans.get(i).subSeminarId)) {
                    backSubSeminarBeans.add(subSeminarBeans.get(i));
                } else if (subSeminarBeans.get(i).hmTitle.equals(MAIN)) {
                    backSubSeminarBeans.add(subSeminarBeans.get(i));
                }


            }

        }
        return backSubSeminarBeans;
    }


    public static List<Guestimgs> checkGuestImgs(List<Guestimgs> guestimgs) {
        List<Guestimgs> guestimgsBack = new ArrayList<>();
        for (Guestimgs entry : guestimgs) {
            if (!TextTool.isNullOrEmpty(entry.guestType)) {
                LogUtil.e("嘉宾显示：" + entry.guestType);
            }
            if (!TextTool.isNullOrEmpty(entry.guestType) && !entry.guestType.contains("不显示") && !entry.guestType.contains("否")) {
                entry.guestType = "";
                if (!TextTool.isNullOrEmpty(entry.realPath) && !entry.realPath.startsWith("https")) {
                    entry.realPath = "https:" + entry.realPath;
                }
                if (guestimgsBack.size() > 15) {
                    break;
                }
                guestimgsBack.add(entry);
            }
        }
        LogUtil.e("获取嘉宾头像列表：" + ZSONObject.toZSONString(guestimgsBack));
        return guestimgsBack;
    }

    public static List<SubSeminar> checkSubSeminars(List<SubSeminar> subSeminars) {

        for (int i = 0; i < subSeminars.size(); i++) {
            LogUtil.e("summary:" + subSeminars.get(i).summary);
            if (subSeminars.get(i).title.contains("|")) {
                subSeminars.get(i).title = subSeminars.get(i).title.replace("|", "\r\n");
            }
            String[] summarys = subSeminars.get(i).summary.split("\\|");
            LogUtil.e("获取首页议程title：" + subSeminars.get(i).title);
            LogUtil.e("获取首页议程：" + ZSONObject.toZSONString(subSeminars.get(i)));
            if (summarys.length > 3) {
                subSeminars.get(i).summary = summarys[0];
                subSeminars.get(i).summaryAddress = summarys[1];
                subSeminars.get(i).summaryTime = summarys[2];
                subSeminars.get(i).summaryType = summarys[3];
            }
        }
        return subSeminars;
    }

    public static List<CardData> sortCardData(List<CardData> cardDatas) {
        //正序排序
        Collections.sort(cardDatas, new Comparator<CardData>() {
            @Override
            public int compare(CardData o1, CardData o2) {
                int ii = 0;
                try {
                    String o1Str = o1.startTime;
                    if (o1Str.startsWith("\"")) {
                        o1Str.replace("\"", "");
                    }
                    String o2Str = o2.startTime;
                    if (o2Str.startsWith("\"")) {
                        o2Str.replace("\"", "");
                    }
                    int i1 = Integer.valueOf(o1Str);
                    int i2 = Integer.valueOf(o2Str);
                    ii = i1 - i2;

                    if (ii == 0) {
                        String c1Str = o1.endTime;
                        if (c1Str.startsWith("\"")) {
                            c1Str.replace("\"", "");
                        }
                        String c2Str = o2.endTime;
                        if (c2Str.startsWith("\"")) {
                            c2Str.replace("\"", "");
                        }
                        int c1 = Integer.valueOf(c1Str);
                        int c2 = Integer.valueOf(c2Str);
                        ii = c1 - c2;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    ii = 0;
                }
                return ii;
            }
        });
        return cardDatas;

    }


    public static List<SubSeminarBean> sortSubSeminarBeanBySort(List<SubSeminarBean> subSeminarBeans) {
        Collections.sort(subSeminarBeans, (o1, o2) -> {
            int ii = 0;
            try {
                int i1 = o1.hm_sortOrder;
                int i2 = o2.hm_sortOrder;
                ii = i2 - i1;
                if (ii == 0) {
                    String c1Str = o1.startTime;
                    if (c1Str.startsWith("\"")) {
                        c1Str.replace("\"", "");
                    }
                    String c2Str = o2.startTime;
                    if (c2Str.startsWith("\"")) {
                        c2Str.replace("\"", "");
                    }
                    int c1 = Integer.valueOf(c1Str);
                    int c2 = Integer.valueOf(c2Str);
                    ii = c1 - c2;
                    if (ii == 0) {
                        String c3Str = o1.endTime;
                        if (c3Str.startsWith("\"")) {
                            c3Str.replace("\"", "");
                        }
                        String c4Str = o2.endTime;
                        if (c4Str.startsWith("\"")) {
                            c4Str.replace("\"", "");
                        }
                        int c3 = Integer.valueOf(c3Str);
                        int c4 = Integer.valueOf(c4Str);
                        ii = c3 - c4;
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                ii = 0;
            }
            return ii;
        });
        return subSeminarBeans;

    }

    public static List<SubSeminarBean> sortSubSeminarBeanByA(List<SubSeminarBean> subSeminarBeans) {
        Collections.sort(subSeminarBeans, (o1, o2) -> {
            int ii = 0;
            try {
                int i1 = o1.hm_sortOrder;
                int i2 = o2.hm_sortOrder;
                ii = i1 - i2;
                if (ii == 0) {
                    String c1Str = o1.startTime;
                    if (c1Str.startsWith("\"")) {
                        c1Str.replace("\"", "");
                    }
                    String c2Str = o2.startTime;
                    if (c2Str.startsWith("\"")) {
                        c2Str.replace("\"", "");
                    }
                    int c1 = Integer.valueOf(c1Str);
                    int c2 = Integer.valueOf(c2Str);
                    ii = c1 - c2;
                }

            } catch (Exception e) {
                e.printStackTrace();
                ii = 0;
            }
            return ii;
        });
        return subSeminarBeans;

    }


    public static AgendaDetailBack checkAgendaDetailBack(AgendaDetail agendaDetail) {
        AgendaDetailBack agendaDetailBack = new AgendaDetailBack();
        if (agendaDetail.detail != null) {
            agendaDetailBack.hmDay = TimeHelper.getDateByDay2(Long.valueOf(agendaDetail.detail.startTime));
            agendaDetailBack.hmName = agendaDetail.detail.name.replace("|", "");
            agendaDetailBack.hotel = agendaDetail.detail.hotel;
            agendaDetailBack.startTime = agendaDetail.detail.startTime;
            agendaDetailBack.endTime = agendaDetail.detail.endTime;
            agendaDetailBack.subSeminarId = agendaDetail.detail.subSeminarId;
            agendaDetailBack.intro = agendaDetail.detail.m_parseIntro;
            agendaDetailBack.hmStartAndEndTime = TimeHelper.getDateByTime(Long.valueOf(agendaDetail.detail.startTime)) + "-"
                    + TimeHelper.getDateByTime(Long.valueOf(agendaDetail.detail.endTime));

            //详情1：松湖对话 技术论坛，其中松湖对话不可预约
            //详情2：codelabs
            //详情3：techhour
            switch (agendaDetail.detail.m_meetingTypeCn) {
                case "Codelabs":
                    agendaDetailBack.type = 2;//详情2
                    agendaDetailBack.isShowSelect = true;//可预约
                    agendaDetailBack.techhourType=false;
                    break;
                case "技术论坛":
                    agendaDetailBack.type = 1;//详情1
                    agendaDetailBack.isShowSelect = true;//可预约
                    agendaDetailBack.techhourType=false;
                    break;
                case "Tech. Hour":
                    agendaDetailBack.type = 3;//详情3
                    agendaDetailBack.isShowSelect = false;//议程不可预约，需要预约日程
                    agendaDetailBack.techhourType=true;
                    break;
                case "互动体验":
                    agendaDetailBack.type = 4;//详情3
                    agendaDetailBack.isShowSelect = false;//议程不可预约，需要预约日程
                    agendaDetailBack.techhourType=false;
                    break;
                default:
                    agendaDetailBack.type = 1;//详情1
                    agendaDetailBack.isShowSelect = false;//不可预约
                    agendaDetailBack.techhourType=false;
                    break;
            }

            String subSeminarStr = ZSONObject.toZSONString(agendaDetail.detail.agendaList);
            List<AgendasBean> agendasBeans = null;
            try {
                agendasBeans = ZSONArray.stringToClassList(subSeminarStr, AgendasBean.class);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("议程解析开始异常：" + e.getMessage());
            }
            agendaDetailBack.hmAgenda = new ArrayList<>();

            List<String> techHouridlist = MyApplication.getTechHouridlist();//TechHour已预约议程ID

            if (agendasBeans != null) {
                for (AgendasBean agendasBean : agendasBeans) {
                    for (AgendasBean.Agendas agenda : agendasBean.agendas) {
                        Agendas agendas = new Agendas();
                        agendas.name = agenda.name;
                        agendas.remark = agenda.remark.replace("\\n", "\r\n").replace("<br>", "").replace("<br/>", "");
                        if (agendas.remark.contains("<img")) {
                            String[] remarkArray = agendas.remark.split("<img");
                            agendas.remark = remarkArray[0];
                            if(remarkArray[1].contains("src=")) {
                                agendas.img = remarkArray[1].split("src=")[1];
                                if(agendas.img.contains("png")){
                                    agendas.img= agendas.img.split("png")[0].replaceAll("\"","")+"png";
                                }
                            }
                        }

                        agendas.startTime = TimeHelper.getDateByTime(Long.valueOf(agenda.startTime));
                        agendas.endTime = TimeHelper.getDateByTime(Long.valueOf(agenda.endTime));
                        agendas.subSeminarId = agendaDetail.detail.subSeminarId;

                        //判断Tech Hour日程是否预约
                        if(agendaDetailBack.techhourType) {
                            agendas.agendaId = agenda.agendaId;
                            agendas.startTimeLong = agenda.startTime;
                            agendas.endTimeLong = agenda.endTime;
                            agendas.canReserve = 1;
                            if (techHouridlist != null && techHouridlist.size() > 0) {
                                agendas.isReserve = techHouridlist.contains(agendas.agendaId);
                            }
                        }

                        if (agenda.guests != null && agenda.guests.size() > 0) {
                            List<Guest> guests = null;
                            try {
                                guests = ZSONArray.stringToClassList(ZSONObject.toZSONString(agenda.guests), Guest.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            agendas.guests=guests;
                        }
                        agendaDetailBack.hmAgenda.add(agendas);
                    }
                }
            }
            if (agendaDetailBack.hmAgenda.size() < 1) {
                agendaDetailBack.hmAgenda = null;
            }
            List<String> subs = MyApplication.getidlist();
            if (subs != null && subs.size() > 0) {
                agendaDetailBack.isSelect = subs.contains(agendaDetailBack.subSeminarId);
            }

//            LogUtil.e(ZSONObject.toZSONString(agendaDetail.detail.guestList));
//            agendaDetailBack.guestList = new ArrayList<>();
//
//
//            if (agendaDetailBack.type == 3) {
//                Guest guest = new Guest();
//                guest.name = agendaDetail.detail.name;
//                guest.duty = agendaDetail.detail.hotel;
//                agendaDetailBack.guestList.add(guest);
//                agendaDetailBack.hotel = "";
//                if (MyApplication.tHBeans != null && MyApplication.tHBeans.size() > 0) {
//                    for (HdBean tHBean : MyApplication.tHBeans) {
//                        String titlee = tHBean.title.contains("|") ? tHBean.title.substring(0, tHBean.title.indexOf("|")) : tHBean.title;
//                        if (titlee.equals(agendaDetailBack.hmName)) {
//                            agendaDetailBack.thId = tHBean.id;
//                        }
//                    }
//                }
//
//            } else {
//                if (agendaDetail.detail.guestList != null && agendaDetail.detail.guestList.size() > 0) {
//                    agendaDetailBack.guestList = new ArrayList<>();
//                    if (agendaDetail.detail.guestList != null && agendaDetail.detail.guestList.size() > 0) {
//                        if (agendaDetail.detail.guestList.get(0).guests != null && agendaDetail.detail.guestList.get(0).guests.size() > 0) {
//                            agendaDetailBack.guestList.addAll(agendaDetail.detail.guestList.get(0).guests);
//                        }
//                    }
//                    LogUtil.e("有嘉宾：" + ZSONObject.toZSONString(agendaDetailBack.guestList));
//                }
//            }
//            if (agendaDetailBack.type == 5) {
//                Pattern p1 = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
//                Matcher matcher1 = p1.matcher(agendaDetail.detail.m_parseIntro);
//                agendaDetailBack.intro = matcher1.replaceAll(" ").replaceAll("&quot", "");
//                if (agendaDetailBack.intro.contains("|")) {
//                    agendaDetailBack.intro = agendaDetailBack.intro.substring(agendaDetailBack.intro.indexOf("|") + 1);
//                }
//            }
//            if (agendaDetailBack.type == 2) {
//                Pattern p1 = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
//                Matcher matcher1 = p1.matcher(agendaDetail.detail.m_parseIntro);
//                agendaDetailBack.intro = matcher1.replaceAll(" ").replaceAll("&quot", "");
//                if (agendaDetailBack.intro.contains("|")) {
//                    agendaDetailBack.intro = agendaDetailBack.intro.substring(agendaDetailBack.intro.indexOf("|") + 1);
//                }
//            }
        }
//        if (agendaDetailBack.type == 0) {
//            LogUtil.e("松湖论坛有嘉宾：" + ZSONObject.toZSONString(agendaDetailBack.guestList));
//        }
        return agendaDetailBack;
    }


    /**
     * 将源List按照指定元素数量拆分为多个List
     *
     * @param source       源List
     * @param splitItemNum 每个List中元素数量
     */
    public static <T> List<List<T>> splitArray(List<T> source, int splitItemNum) {
        List<List<T>> result = new ArrayList<List<T>>();
        if (source != null && source.size() > 0 && splitItemNum > 0) {
            if (source.size() <= splitItemNum) {
                // 源List元素数量小于等于目标分组数量
                result.add(source);
            } else {
                // 计算拆分后list数量
                int splitNum = (source.size() % splitItemNum == 0) ? (source.size() / splitItemNum) : (source.size() / splitItemNum + 1);

                List<T> value = null;
                for (int i = 0; i < splitNum; i++) {
                    if (i < splitNum - 1) {
                        value = source.subList(i * splitItemNum, (i + 1) * splitItemNum);
                    } else {
                        // 最后一组
                        value = source.subList(i * splitItemNum, source.size());
                    }
                    result.add(value);
                }
            }
        }
        return result;
    }

}
