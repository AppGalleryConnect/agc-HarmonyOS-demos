package com.smarket.hdc2023.widget.form.database;

import ohos.app.Context;
import ohos.data.DatabaseHelper;
import ohos.data.orm.OrmContext;
import ohos.data.orm.OrmPredicates;

import java.util.List;

/**
 * 卡片数据库操作
 */
public class DatabaseUtils {
    private static Context mContext;
    private static volatile OrmContext ormContext;

    private DatabaseUtils() {
    }

    /**
     * 初始化数据库
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        if (mContext == null) {
            mContext = context;
        }
        if (ormContext == null) {
            synchronized (DatabaseUtils.class) {
                if (ormContext == null) {
                    ormContext = new DatabaseHelper(context).getOrmContext("CardDatabase", "CardDatabase.db", CardDatabase.class);
                }
            }
        }
    }

    /**
     * 删除卡片
     *
     * @param cardId 卡片id
     */
    public static void deleteCardData(long cardId) {
        OrmPredicates where = ormContext.where(FormData.class);
        where.equalTo("formId", cardId);
        List<FormData> cardDataList = ormContext.query(where);
        if (!cardDataList.isEmpty()) {
            ormContext.delete(cardDataList.get(0));
            ormContext.flush();
        }
    }

    /**
     * 新增卡片信息
     *
     * @param cardData 卡片对象
     */
    public static void insertCardData(FormData cardData) {
        if (queryCardData(cardData.getFormId()) == null) {
            ormContext.insert(cardData);
            ormContext.flush();
        }
    }

    /**
     * 获取所有卡片
     */
    public static List<FormData> queryAllCardData() {
        OrmPredicates where = ormContext.where(FormData.class);
        return ormContext.query(where);
    }

    /**
     * 根据id获取卡片
     */
    public static FormData queryCardData(long cardId) {
        OrmPredicates where = ormContext.where(FormData.class);
        where.equalTo("formId", cardId);
        List<FormData> cardDataList = ormContext.query(where);
        if (!cardDataList.isEmpty()) {
            return cardDataList.get(0);
        }
        return null;
    }

    /**
     * 更新poiId
     *
     * @param cardId 卡片ID
     * @param poiId  PoiId
     */
    public static void updatePoiId(long cardId, String poiId) {
        FormData cardData = queryCardData(cardId);
        if (cardData != null) {
            cardData.setPoiId(poiId);
            ormContext.update(cardData);
            ormContext.flush();
        }
    }
}
