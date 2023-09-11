package com.smarket.hdc2023.widget.form;

import com.smarket.hdc2023.MyApplication;
import com.smarket.hdc2023.ResourceTable;
import com.smarket.hdc2023.bean.ArticalInfo;
import com.smarket.hdc2023.bean.ArticalList;
import com.smarket.hdc2023.bean.BaseHttp;
import com.smarket.hdc2023.bean.HttpBean;
import com.smarket.hdc2023.common.Config;
import com.smarket.hdc2023.help.AudioPlayUtil;
import com.smarket.hdc2023.help.HiAnalyticsUtil;
import com.smarket.hdc2023.help.LogUtil;
import com.smarket.hdc2023.http.HttpApi;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.agp.components.webengine.*;
import ohos.agp.utils.TextTool;
import ohos.global.resource.Resource;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;
import ohos.utils.net.Uri;
import ohos.utils.zson.ZSONArray;
import ohos.utils.zson.ZSONObject;

import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.smarket.hdc2023.common.Config.*;

/**
 * WebView页面
 */
public class WebViewAbility extends Ability {

    //日志标签
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.DEBUG, 0x0, WebViewAbility.class.getName());

    //webview显示时的时间
    private Long onActiveTime;

    //返回键
    private Image backBtn;

    private WebView webView;

    private Text titleText;

    //请求地址
    private String url;

    //本地资源域名前缀
    private String resourceUrl = "com.huawei.rawfile";
    private Ability ability;

    //webview标题
    private String webviewTitle = "";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_webview);

        //获取参数
        url = intent.getStringParam("url");
        if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + resourceUrl + "/" + url;
        }
        ability = this;

        webviewTitle = intent.getStringParam("title");
        if (TextTool.isNullOrEmpty(webviewTitle)) {
            webviewTitle = "浏览器";
        }
        titleText = (Text) findComponentById(ResourceTable.Id_title);
        titleText.setText(webviewTitle);

        //webView初始化
        webView = (WebView) findComponentById(ResourceTable.Id_webview);
        WebConfig webConfig = webView.getWebConfig();
        webConfig.setJavaScriptPermit(true);
        webConfig.setWebStoragePermit(true);
        webConfig.setLoadsImagesPermit(true);
        webConfig.setMediaAutoReplay(true);
        webConfig.setMediaAutoReplay(true);
        webConfig.setDataAbilityPermit(true);
        webConfig.setTextAutoSizing(true);
        webConfig.setSecurityMode(WebConfig.SECURITY_NOT_ALLOW);
        webConfig.setViewPortFitScreen(true);

        //监控Web状态
        webView.setWebAgent(new WebAgent() {
            @Override
            public void onLoadingPage(WebView webview, String url, PixelMap favicon) {
                super.onLoadingPage(webview, url, favicon);
                // 页面开始加载时自定义处理
            }

            @Override
            public void onPageLoaded(WebView webview, String url) {
                super.onPageLoaded(webview, url);
                // 页面加载结束后自定义处理
            }

            @Override
            public void onLoadingContent(WebView webview, String url) {
                super.onLoadingContent(webview, url);
                // 加载资源时自定义处理
            }

            @Override
            public void onError(WebView webview, ResourceRequest request, ResourceError error) {
                super.onError(webview, request, error);
                // 发生错误时自定义处理
            }

            @Override
            public boolean isNeedLoadUrl(WebView webView, ResourceRequest request) {
                //定制网址加载行为
                return super.isNeedLoadUrl(webView, request);
            }

            @Override
            public ResourceResponse processResourceRequest(WebView webview, ResourceRequest request) {
                Uri requestUri = request.getRequestUrl();
                HiLog.info(TAG, "请求URL:" + requestUri.toString());
                //定制返回值
                if (resourceUrl.equals(requestUri.getDecodedAuthority())) {
                    String path = requestUri.getDecodedPath();
                    if (TextTool.isNullOrEmpty(path)) {
                        return super.processResourceRequest(webview, request);
                    }
                    // 根据自定义规则访问资源文件
                    String rawFilePath = "entry/resources/rawfile" + path;
                    String mimeType = URLConnection.guessContentTypeFromName(rawFilePath);
                    try {
                        Resource resource = getResourceManager().getRawFileEntry(rawFilePath).openRawFile();
                        ResourceResponse response = new ResourceResponse(mimeType, resource, null);
                        return response;
                    } catch (IOException e) {
                        HiLog.error(TAG, "open raw file failed(" + path + ")：" + e.toString());
                    }
                }
                return super.processResourceRequest(webview, request);
            }
        });

        //监控浏览事件
        webView.setBrowserAgent(new BrowserAgent(this) {
            @Override
            public void onTitleUpdated(WebView webview, String title) {
                super.onTitleUpdated(webview, title);
                // 标题变更时自定义处理
            }

            @Override
            public void onProgressUpdated(WebView webview, int newProgress) {
                super.onProgressUpdated(webview, newProgress);
                // 加载进度变更时自定义处理
            }
        });

        //播放音频函数
        webView.addJsCallback("goPlay", playUrl -> {
            HiLog.info(TAG, "goPlay接收参数:" + playUrl);
            AudioPlayUtil.goPlay(ability, playUrl, null, null, null, 2, webView);
            //返回JSON串
            String res = "{\"status\":\"OK\"}";
            HiLog.info(TAG, "goPlay接返回值:" + res);
            return res;
        });

        //导览 跳转详情页面
        webView.addJsCallback("goDetail", id -> {
            HiLog.info(TAG, "goDetail接收参数:" + id);
            Operation operation = new Intent.OperationBuilder().withDeviceId("").withBundleName("com.smarket.hdc2023").withAbilityName("com.smarket.hdc2023.MainAbility").build();
            intent.setOperation(operation);
            intent.setParam("params", "{\"message\":\"sceneryDetail\",\"data\":\"" + id + "\"}");
            startAbility(intent);
            //返回JSON串
            String res = "{\"status\":\"OK\"}";
            HiLog.info(TAG, "goDetail接返回值:" + res);
            return res;
        });

        //获取地图定位点信息
        webView.addJsCallback("getMapPointList", json -> {
            HiLog.info(TAG, "getMapPointList接收参数:" + json);
            String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID +"/seminar/" + SEMINAR_ID + "/article/list/" + FENGJINGLISTIMG_ID + ".json";
            HttpBean httpBean = HttpApi.get(urlStr);
            List<ArticalList> articalListsF = new ArrayList<>();
            if (httpBean.getStratus() == 0) {
                BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
                Map<String, Object> mapContent = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), Map.class);
                if (!mapContent.isEmpty()) {
                    List<ArticalList> articalLists1 = ZSONArray.stringToZSONArray(ZSONObject.toZSONString(mapContent.get("items"))).toJavaList(ArticalList.class);
                    List<ArticalList> articalLists = articalLists1.stream().filter(g -> g.isTop.equals(1)).collect(Collectors.toList());
                    for (ArticalList articalList : articalLists) {
                        ArticalList at = new ArticalList();
                        String title1 = articalList.title;
                        String[] titleArr = title1.split("\\|");
                        String[] titleArr1 = titleArr[2].split("\\*");
                        at.point = new Integer[]{Integer.valueOf(titleArr1[0]), Integer.valueOf(titleArr1[1])};
                        at.width = Integer.valueOf(titleArr1[2]);
                        at.height = Integer.valueOf(titleArr1[3]);
                        at.id = articalList.id;
                        articalListsF.add(at);
                    }
                }
            } else {
                LogUtil.e("获取导览列表失败：" + httpBean.getStratus());
            }
            //返回JSON串
            String res = ZSONObject.toZSONString(articalListsF);
            HiLog.info(TAG, "getMapPointList接返回值:" + res);
            return res;
        });

        //根据id 返回导览弹窗详情
        webView.addJsCallback("getMapPointInfo", json -> {
            //上报用户点击功能按钮事件
            HiAnalyticsUtil.addModularClickEvent(ability,"展区-导览图点击精彩活动");
            HiLog.info(TAG, "getMapPointInfo接收参数:" + json);
            String urlStr = MyApplication.getApplication().getResourceString(ResourceTable.String_HOST_STATIC) + TENANT_ID + "/seminar/" + MyApplication.getApplication().getResourceString(ResourceTable.String_SEMINAR_ID) + "/article/" + json + ".json";
            HttpBean httpBean = HttpApi.get(urlStr);
            ArticalInfo articalInfo = new ArticalInfo();
            if (httpBean.getStratus() == 0) {
                BaseHttp baseHttp = ZSONObject.stringToClass(httpBean.getData(), BaseHttp.class);
                articalInfo = ZSONObject.stringToClass(ZSONObject.toZSONString(baseHttp.body.content), ArticalInfo.class);
            } else {
                LogUtil.e("获取导览弹窗信息失败：" + httpBean.getStratus());
            }
            //返回JSON串
            String res = ZSONObject.toZSONString(articalInfo);
            HiLog.info(TAG, "getMapPointInfo接返回值:" + res);
            return res;
        });

        //请求地址
        webView.load(url);

        //返回事件
        backBtn = (Image) findComponentById(ResourceTable.Id_back);
        backBtn.setClickedListener(component -> onBackPressed());
    }

    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsFromUserResult(requestCode, permissions, grantResults);
        terminateAbility();
    }

    @Override
    protected void onStop() {
        super.onStop();
        AudioPlayUtil.stopConnect(ability, 2);
    }

    /**
     * webview显示
     */
    @Override
    protected void onActive() {
        //记录页面显示时的时间
        onActiveTime = System.currentTimeMillis();
    }

    /**
     * webview隐藏
     */
    @Override
    protected void onInactive() {
        Long onInactiveTime = System.currentTimeMillis();
        //浏览时长(毫秒)
        Long duration = onInactiveTime - onActiveTime;
        //上报用户浏览页面事件
        HiAnalyticsUtil.addPageViewEvent(ability, webviewTitle, duration.intValue());
    }
}