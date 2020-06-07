package com.baomidou.mybatisplus.core.metadata;

import cn.org.atool.fluent.mybatis.condition.base.PredicateField;
import cn.org.atool.fluent.mybatis.method.metadata.TableMeta;

/**
 * TableInfo
 *
 * @author darui.wu
 * @create 2020/6/3 2:06 下午
 */
@Deprecated
public class TableInfo extends TableMeta {
    public TableInfo() {
    }

    public TableInfo(Class<?> entityType) {
        super(entityType);
    }

    public String chooseSelect(PredicateField predicate) {
        return super.filter(predicate);
    }
}