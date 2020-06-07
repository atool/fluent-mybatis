package com.baomidou.mybatisplus.core.metadata;

import cn.org.atool.fluent.mybatis.method.metadata.TableMetaHelper;

/**
 * TableInfoHelper
 *
 * @author darui.wu
 * @create 2020/6/3 11:55 上午
 */
@Deprecated
public class TableInfoHelper {
    public static TableInfo getTableInfo(Class<?> clazz) {
        return (TableInfo) TableMetaHelper.getTableInfo(clazz);
    }
}