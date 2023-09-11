package com.smarket.hdc2023.widget.form.database;

import ohos.data.orm.OrmDatabase;
import ohos.data.orm.annotation.Database;

/**
 * 数据库
 */
@Database(entities = {FormData.class}, version = 1)
public abstract class CardDatabase extends OrmDatabase {
}
