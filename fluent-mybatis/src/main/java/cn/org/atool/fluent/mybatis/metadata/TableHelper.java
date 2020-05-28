package cn.org.atool.fluent.mybatis.metadata;

import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.annotation.TableName;
import cn.org.atool.fluent.mybatis.condition.interfaces.IEntity;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.util.Constants;
import cn.org.atool.fluent.mybatis.util.MybatisUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 实体类反射表辅助类
 * </p>
 *
 * @author darui.wu
 */
@Slf4j
public class TableHelper {
    /**
     * 储存反射类表信息
     * Key: entity class
     */
    private static final Map<Class, TableInfo> TABLE_INFO_CACHE = new ConcurrentHashMap<>();

    /**
     * <p>
     * 获取实体映射表信息
     * </p>
     *
     * @param clazz 反射实体类
     * @return 数据库表反射信息
     */
    public static TableInfo getTableInfo(Class<?> clazz) {
        if (!IEntity.class.isAssignableFrom(clazz)) {
            return null;
        }
        Class<?> currentClass = MybatisUtil.getProxyTargetClass(clazz);
        if (!TABLE_INFO_CACHE.containsKey(currentClass)) {
            initTableInfo(currentClass);
        }
        return TABLE_INFO_CACHE.get(currentClass);
    }

    /**
     * <p>
     * 实体类反射获取表信息【初始化】
     * </p>
     *
     * @param clazz Entity实体类
     * @return 数据库表反射信息
     */
    synchronized static void initTableInfo(Class<?> clazz) {
        if (!TABLE_INFO_CACHE.containsKey(clazz)) {
            TableInfo tableInfo = new TableInfo(clazz);
            /* 初始化表名相关 */
            initTableName(clazz, tableInfo);
            /* 初始化字段相关 */
            initTableFields(clazz, tableInfo);
            TABLE_INFO_CACHE.put(clazz, tableInfo);
        }
    }

    /**
     * <p>
     * 初始化 表数据库类型,表名,resultMap
     * </p>
     *
     * @param clazz     实体类
     * @param tableInfo 数据库表反射信息
     */
    private static void initTableName(Class<?> clazz, TableInfo tableInfo) {
        /* 数据库全局配置 */
        TableName table = clazz.getAnnotation(TableName.class);

        String tableName = table.value();
        if (MybatisUtil.isNotEmpty(table.schema())) {
            tableName = table.schema() + Constants.DOT + tableName;
        }
        tableInfo.setTableName(tableName);
    }

    /**
     * <p>
     * 获取该类的所有属性列表
     * 初始化 表主键,表字段
     * </p>
     *
     * @param clazz     实体类
     * @param tableInfo 数据库表反射信息
     */
    private static void initTableFields(Class<?> clazz, TableInfo tableInfo) {
        List<Field> fields = MybatisUtil.getFieldList(clazz);

        List<FieldInfo> fieldList = new ArrayList<>();
        for (Field field : fields) {
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null) {
                fieldList.add(new FieldInfo(field, tableField));
                continue;
            }
            TableId tableId = field.getAnnotation(TableId.class);
            if (tableId == null) {
                continue;
            }
            if (tableInfo.getPrimary() == null) {
                tableInfo.setPrimary(new PrimaryInfo(field, tableId));
            } else {
                throw FluentMybatisException.instance("There must be only one, Discover multiple @TableId annotation in %s", clazz.getName());
            }
        }

        /* 字段列表,不可变集合 */
        Collections.sort(fieldList);
        tableInfo.setFields(Collections.unmodifiableList(fieldList));

        /* 未发现主键注解，提示警告信息 */
        if (tableInfo.getPrimary() == null) {
            log.warn(String.format("Warn: Could not find @TableId in Class: %s.", clazz.getName()));
        }
    }

    /**
     * 根据mapper接口定义，提前对应的Entity类型
     * 如果是多泛型的时候，需要将Entity泛型放在第一位
     *
     * @param mapperClass mapper 接口
     * @return mapper 泛型
     */
    public static Class<?> extractEntity(Class<?> mapperClass) {
        Type[] types = mapperClass.getGenericInterfaces();
        for (Type type : types) {
            if (!(type instanceof ParameterizedType)) {
                continue;
            }
            // 对第一个泛型进行处理
            Type[] array = ((ParameterizedType) type).getActualTypeArguments();
            if (array == null || array.length == 0 || array[0] instanceof TypeVariable || array[0] instanceof WildcardType) {
                return null;
            } else {
                return (Class<?>) array[0];
            }
        }
        return null;
    }
}