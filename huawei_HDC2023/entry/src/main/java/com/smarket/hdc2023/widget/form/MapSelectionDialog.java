package com.smarket.hdc2023.widget.form;

import com.smarket.hdc2023.ResourceTable;
import com.smarket.hdc2023.widget.form.bean.LocationDataBean;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.components.element.StateElement;
import ohos.agp.text.Font;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;

import java.util.List;

/**
 * 地图选择弹框
 */
public class MapSelectionDialog extends CommonDialog implements Component.ClickedListener {
    private final double latitude;
    private final double longitude;
    private final Context context;

    public MapSelectionDialog(Context context, LocationDataBean location, List<String> mapInstallList) {
        super(context);
        this.context = context;
        Component container = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_map_select_dialog, null, false);
        DirectionalLayout mapList = (DirectionalLayout) container.findComponentById(ResourceTable.Id_mapList);
        StateElement stateElement = new StateElement();
        stateElement.addState(new int[]{ComponentState.COMPONENT_STATE_PRESSED},
                new ShapeElement(context, ResourceTable.Graphic_rectangle_pressed));
        stateElement.addState(new int[]{ComponentState.COMPONENT_STATE_EMPTY},
                new ShapeElement(context, ResourceTable.Graphic_rectangle_normal));

        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setRgbColor(RgbColor.fromArgbInt(Color.BLACK.getValue()));
        int size = mapInstallList.size();
        for (int i = 0; i < size; i++) {
            String map = mapInstallList.get(i);
            Button mapButton = new Button(context);
            mapButton.setName(map);
            mapButton.setWidth(ComponentContainer.LayoutConfig.MATCH_PARENT);
            mapButton.setHeight(ComponentContainer.LayoutConfig.MATCH_CONTENT);
            mapButton.setText(getAppName(map));
            mapButton.setTextSize(DevicesUtil.vp2px(context, 16));
            mapButton.setPaddingTop(DevicesUtil.vp2px(context, 12));
            mapButton.setPaddingBottom(DevicesUtil.vp2px(context, 12));
            mapButton.setTextAlignment(TextAlignment.CENTER);
            mapButton.setBackground(stateElement);
            mapButton.setClickedListener(this);
            mapList.addComponent(mapButton);
            if (i == size - 1) {
                break;
            }

            //添加分割线
            Text text = new Text(context);
            text.setWidth(ComponentContainer.LayoutConfig.MATCH_PARENT);
            text.setHeight(2);
            text.setAlpha(0.2f);
            text.setBackground(shapeElement);
            mapList.addComponent(text);
        }
        Button close = (Button) container.findComponentById(ResourceTable.Id_close);
        //自定义字体
        Font font = new Font.Builder("myfont")
                .setWeight(Font.BOLD)
                .build();
        close.setFont(font);
        close.setClickedListener(this);

        setContentCustomComponent(container);
        setSize(DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT);
        setCornerRadius(50);
        setAlignment(LayoutAlignment.BOTTOM);
        setAutoClosable(true);
        setTransparent(true);
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        setDestroyedListener(() -> {
            if (context instanceof TransparentAbility) {
                context.terminateAbility();
            }
        });
        show();
    }

    @Override
    public void onClick(Component component) {
        switch (component.getName()) {
            case NavigationPresenter.BAIDU_MAP_PACKAGE_NAME:
                NavigationPresenter.baiduMap(context, latitude, longitude);
                break;
            case NavigationPresenter.GAUD_MAP_PACKAGE_NAME:
                NavigationPresenter.gaudMap(context, latitude, longitude);
                break;
            case NavigationPresenter.TENCENT_MAP_PACKAGE_NAME:
                NavigationPresenter.tencentMap(context, latitude, longitude);
                break;
        }
        if (context instanceof TransparentAbility) {
            context.terminateAbility();
        }
        destroy();
    }

    private String getAppName(String packageName) {
        String appName;
        switch (packageName) {
            case NavigationPresenter.BAIDU_MAP_PACKAGE_NAME:
                appName = "百度地图";
                break;
            case NavigationPresenter.GAUD_MAP_PACKAGE_NAME:
                appName = "高德地图";
                break;
            case NavigationPresenter.TENCENT_MAP_PACKAGE_NAME:
                appName = "腾讯地图";
                break;
            default:
                appName = "";
                break;
        }
        return appName;
    }
}
