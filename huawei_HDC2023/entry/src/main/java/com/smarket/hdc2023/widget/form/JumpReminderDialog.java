package com.smarket.hdc2023.widget.form;

import com.smarket.hdc2023.ResourceTable;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.event.notification.NotificationHelper;
import ohos.event.notification.NotificationRequest;
import ohos.event.notification.NotificationSlot;
import ohos.multimodalinput.event.KeyEvent;
import ohos.rpc.RemoteException;
import ohos.system.version.SystemVersion;

/**
 * 应用跳转第三方时提示弹框
 */
public class JumpReminderDialog extends CommonDialog {
    public JumpReminderDialog(Context context) {
        super(context);
        Component container = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_jump_reminder_dialog, null, false);
        Button cancels = (Button) container.findComponentById(ResourceTable.Id_cancels);
        Button confirms = (Button) container.findComponentById(ResourceTable.Id_confirms);
        setContentCustomComponent(container);
        setSize(DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT);
        setCornerRadius(90);
        setAlignment(LayoutAlignment.CENTER);
        cancels.setFont(FontUtil.medium());
        confirms.setFont(FontUtil.medium());
        siteRemovable(false);
        setSwipeToDismiss(false);
        siteKeyboardCallback((iDialog, keyEvent) -> keyEvent.getKeyCode() == KeyEvent.KEY_BACK);
        show();
        cancels.setClickedListener(component -> {
            if (context instanceof TransparentAbility) {
                context.terminateAbility();
            }
            destroy();
        });
        confirms.setClickedListener(component -> {
            startWeChatSweep(context);
            destroy();
            if (context instanceof TransparentAbility) {
                context.terminateAbility();
            }
        });
    }

    /**
     * 拉起微信扫一扫
     *
     * @param context 上下文
     */
    public static void startWeChatSweep(Context context) {
        if (AppIsInstall.isInstall(context, "com.tencent.mm")) {
            Intent intent = new Intent();
            intent.setParam("LauncherUI.From.Scaner.Shortcut", true);
            Operation operation = new Intent.OperationBuilder()
                    .withBundleName("com.tencent.mm")
                    .withAbilityName("com.tencent.mm.ui.LauncherUI")
                    .withFlags(Intent.FLAG_NOT_OHOS_COMPONENT
                            | Intent.FLAG_ABILITY_NEW_MISSION
                            | Intent.FLAG_ABILITY_CLEAR_MISSION)
                    .build();
            intent.setOperation(operation);
            context.startAbility(intent, 0);
        } else {
            if (SystemVersion.getApiVersion() > 6) { 
                TextDialogUtil.show(context, "您未安装微信");
            } else {
                NotificationSlot notificationSlot = new NotificationSlot("high", "Order notification", NotificationSlot.LEVEL_HIGH);
                notificationSlot.setEnableVibration(true);
                notificationSlot.enableBypassDnd(true);
                notificationSlot.setLockscreenVisibleness(NotificationRequest.VISIBLENESS_TYPE_PUBLIC);
                try {
                    NotificationHelper.addNotificationSlot(notificationSlot);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                NotificationRequest request = new NotificationRequest(0x1000001).setSlotId("high")
                        .setTapDismissed(true);
                String title = "您未安装微信";
                String text = "请前往应用市场安装";
                NotificationRequest.NotificationNormalContent content = new NotificationRequest.NotificationNormalContent();
                content.setTitle(title)
                        .setText(text);
                NotificationRequest.NotificationContent notificationContent = new NotificationRequest.NotificationContent(content);
                request.setContent(notificationContent); // 设置通知的内容
                try {
                    NotificationHelper.publishNotification(request);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
