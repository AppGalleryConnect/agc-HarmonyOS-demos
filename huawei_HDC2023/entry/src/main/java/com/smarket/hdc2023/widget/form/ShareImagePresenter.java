package com.smarket.hdc2023.widget.form;

import com.smarket.hdc2023.ResourceTable;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Image;
import ohos.app.Context;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.media.image.ImagePacker;
import ohos.media.photokit.metadata.AVStorage;
import ohos.utils.IntentConstants;
import ohos.utils.net.Uri;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ShareImagePresenter {
    private static final String HARMONY_CODE = "HDC-HarmonyCode";

    public ShareImagePresenter() {
    }

    /**
     * 获取鸿蒙码的uri
     *
     * @param context 上下文
     * @return 鸿蒙码的uri
     */
    private static Uri getHarmonyCodeUri(Context context) {
        DataAbilityHelper helper = DataAbilityHelper.creator(context);
        try {
            ResultSet query = helper.query(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, null, null);
            while (query.goToNextRow()) {
                String displayName = query.getString(query.getColumnIndexForName(AVStorage.AVBaseColumns.DISPLAY_NAME));
                String id = query.getString(query.getColumnIndexForName(AVStorage.AVBaseColumns.ID));
                double size = query.getDouble(query.getColumnIndexForName(AVStorage.AVBaseColumns.SIZE));
                double fileSize = size / 1024 / 1024;
                String conversionSize = String.format("%.2f", fileSize) + "M";
                //查询图库中鸿蒙码是否已存在，如果存在直接返回Uri
                if (displayName.equals(HARMONY_CODE + ".jpg")) {
                    return Uri.appendEncodedPathToUri(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, id);
                }
            }
            query.close();
            helper.release();
        } catch (DataAbilityRemoteException e) {
            e.printStackTrace();
        }
        return saveHarmonyCodeUri(context);
    }

    /**
     * 将鸿蒙码保存到图库
     *
     * @param context 上下文
     * @return 鸿蒙码的uri
     */
    private static Uri saveHarmonyCodeUri(Context context) {
        DataAbilityHelper helper = DataAbilityHelper.creator(context);
        try {
            Image image = new Image(context);
            image.setPixelMap(ResourceTable.Media_HarmonyCode);

            ValuesBucket valuesBucket = new ValuesBucket();
            valuesBucket.putString(AVStorage.Images.Media.DISPLAY_NAME, HARMONY_CODE);
            valuesBucket.putString(AVStorage.Images.Media.MIME_TYPE, "image/JPEG");
            //应用独占
            valuesBucket.putInteger("is_pending", 1);
            int id = helper.insert(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, valuesBucket);
            Uri uri = Uri.appendEncodedPathToUri(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, String.valueOf(id));
            //这里需要"w"写权限
            FileDescriptor fd = helper.openFile(uri, "w");
            ImagePacker imagePacker = ImagePacker.create();
            ImagePacker.PackingOptions packingOptions = new ImagePacker.PackingOptions();
            OutputStream outputStream = new FileOutputStream(fd);
            packingOptions.format = "image/jpeg";
            packingOptions.quality = 100;
            boolean result = imagePacker.initializePacking(outputStream, packingOptions);
            if (result) {
                result = imagePacker.addImage(image.getPixelMap());
                if (result) {
                    long dataSize = imagePacker.finalizePacking();
                }
            }
            outputStream.flush();
            outputStream.close();
            valuesBucket.clear();
            //解除独占
            valuesBucket.putInteger("is_pending", 0);
            helper.update(uri, valuesBucket, null);
            helper.release();
            return uri;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
