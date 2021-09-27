package cn.org.atool.fluent.mybatis.metadata;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.org.atool.fluent.mybatis.If.isBlank;

/**
 * <p>
 * 实体类反射表辅助类
 * </p>
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes", "UnusedReturnValue"})
@Slf4j
public class TableMetaHelper {
    /**
     * 储存反射类表信息
     * Key: entity class
     */
    private static final KeyMap<TableMeta> TABLE_INFO_CACHE = new KeyMap<>();

    /**
     * <p>
     * 获取实体映射表信息
     * </p>
     *
     * @param entityClass 反射实体类
     * @return 数据库表反射信息
     */
    public static TableMeta getTableInfo(Class entityClass) {
        if (!TABLE_INFO_CACHE.containsKey(entityClass)) {
            initTableInfo(entityClass);
        }
        return TABLE_INFO_CACHE.get(entityClass);
    }

    /**
     * <p>
     * 实体类反射获取表信息【初始化】
     * </p>
     *
     * @param clazz Entity实体类
     */
    synchronized static void initTableInfo(Class clazz) {
        if (!TABLE_INFO_CACHE.containsKey(clazz)) {
            TableMeta tableMeta = new TableMeta(clazz);
            /* 初始化表名相关 */
            initTableName(clazz, tableMeta);
            /* 初始化字段相关 */
            initTableFields(clazz, tableMeta);

            TABLE_INFO_CACHE.put(clazz, tableMeta);
        }
    }

    /**
     * <p>
     * 初始化 表数据库类型,表名,resultMap
     * </p>
     *
     * @param clazz     实体类
     * @param tableMeta 数据库表反射信息
     */
    private static boolean initTableName(Class<?> clazz, TableMeta tableMeta) {
        /* 数据库全局配置 */
        FluentMybatis fluentMyBatis = clazz.getAnnotation(FluentMybatis.class);
        if (fluentMyBatis == null) {
            return false;
        }
        String tableName = fluentMyBatis.table();
        if (isBlank(tableName)) {
            tableName = MybatisUtil.tableName(clazz.getSimpleName(), fluentMyBatis.prefix(), fluentMyBatis.suffix());
        }
        tableMeta.setTableName(tableName);
        return true;
    }

    /**
     * <p>
     * 获取该类的所有属性列表
     * 初始化 表主键,表字段
     * </p>
     *
     * @param clazz     实体类
     * @param tableMeta 数据库表反射信息
     */
    private static void initTableFields(Class clazz, TableMeta tableMeta) {
        List<Field> fields = MybatisUtil.getFieldList(clazz);

        List<TableFieldMeta> fieldList = new ArrayList<>();
        for (Field field : fields) {
            TableId tableId = field.getAnnotation(TableId.class);
            if (tableId == null) {
                TableField tableField = field.getAnnotation(TableField.class);
                fieldList.add(new TableFieldMeta(field, tableField));
            } else if (tableMeta.getPrimary() == null) {
                tableMeta.setPrimary(new TablePrimaryMeta(field, tableId));
            } else {
                throw FluentMybatisException.instance("There must be only one, Discover multiple @TableId annotation in %s", clazz.getName());
            }
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