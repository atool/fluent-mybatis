package com.baomidou.mybatisplus.core;


import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.method.metadata.FieldMeta;
import cn.org.atool.fluent.mybatis.method.metadata.PrimaryMeta;
import cn.org.atool.fluent.mybatis.method.metadata.TableInfoCompatible;
import cn.org.atool.fluent.mybatis.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.util.Constants;
import cn.org.atool.fluent.mybatis.util.MybatisUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TableInfoCompatible
 *
 * @author darui.wu
 * @create 2020/6/3 7:51 下午
 */
@Slf4j
@Deprecated
public class TableInfoHelper extends TableInfoCompatible {
    @Override
    public boolean initTableName(Class<?> clazz, TableMeta tableMeta) {
        /* 数据库全局配置 */
        TableName table = clazz.getAnnotation(TableName.class);
        if (table == null) {
            return false;
        }

        String tableName = table.value();
        if (MybatisUtil.isNotEmpty(table.schema())) {
            tableName = table.schema() + Constants.DOT + table.value();
        }
        tableMeta.setTableName(tableName);
        return true;
    }

    @Override
    public void initTableFields(Class<?> clazz, TableMeta tableMeta) {
        List<Field> fields = MybatisUtil.getFieldList(clazz);

        List<FieldMeta> fieldList = new ArrayList<>();
        for (Field field : fields) {
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null) {
                fieldList.add(new TableFieldInfo(field, tableField));
                continue;
            }
            TableId tableId = field.getAnnotation(TableId.class);
            if (tableId == null) {
                continue;
            }
            if (tableMeta.getPrimary() != null) {
                throw FluentMybatisException.instance("There must be only one, Discover multiple @TableId annotation in %s", clazz.getName());
            }
            PrimaryMeta primaryMeta = new PrimaryMeta(tableId.value(), field);
            primaryMeta.setAutoIncrease(tableId.type() == IdType.AUTO);
            tableMeta.setPrimary(primaryMeta);
        }

        /* 字段列表,不可变集合 */
        Collections.sort(fieldList);
        tableMeta.setFields(Collections.unmodifiableList(fieldList));

        /* 未发现主键注解，提示警告信息 */
        if (tableMeta.getPrimary() == null) {
            log.warn(String.format("Warn: Could not find @TableId in Class: %s.", clazz.getName()));
        }
    }
}