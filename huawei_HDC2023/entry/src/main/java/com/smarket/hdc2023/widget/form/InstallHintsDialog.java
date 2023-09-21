package com.smarket.hdc2023.widget.form;

import com.smarket.hdc2023.ResourceTable;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.utils.IntentConstants;
import ohos.utils.net.Uri;

/**
 * 应用安装提示弹框
 */
public class InstallHintsDialog extends CommonDialog implements Component.ClickedListener {
    private final Context context;
    private final String packageName;

    public InstallHintsDialog(Context context, String packageName) {
        super(context);
        this.context = context;
        this.packageName = packageName;
        Component container = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_install_hints_dialog, null, false);
        Text title = (Text) container.findComponentById(ResourceTable.Id_title);
        Button cancels = (Button) container.findComponentById(ResourceTable.Id_cancels);
        Button confirms = (Button) container.findComponentById(ResourceTable.Id_confirms);
        title.setText("您未安装" + getAppName());
        cancels.setClickedListener(this);
        confirms.setClickedListener(this);
        title.setFont(FontUtil.medium());
        cancels.setFont(FontUtil.medium());
        confirms.setFont(FontUtil.medium());
        setContentCustomComponent(container);
        setSize(DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT);
        setCornerRadius(50);
        setTransparent(true);
        setAlignment(LayoutAlignment.CENTER);
        setAutoClosable(true);
        show();
    }

    @Override
    public void onClick(Component component) {
        if (component.getId() == ResourceTable.Id_confirms) {
            Intent installIntent = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withAction(IntentConstants.ACTION_VIEW_DATA)
                    .withUri(Uri.parse("market://details?id=" + packageName))
                    .withBundleName("com.huawei.appmarket")
                    .withFlags(Intent.FLAG_NOT_OHOS_COMPONENT)
                    .build();
            installIntent.setOperation(operation);
            context.startAbility(installIntent, 0);
        }
        destroy();
    }

    private String getAppName() {
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
