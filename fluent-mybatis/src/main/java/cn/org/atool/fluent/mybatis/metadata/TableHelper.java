package cn.org.atool.fluent.mybatis.metadata;

import cn.org.atool.fluent.mybatis.annotation.*;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.util.Constants;
import cn.org.atool.fluent.mybatis.util.ReflectUtils;
import cn.org.atool.fluent.mybatis.util.StringUtils;
import cn.org.atool.fluent.mybatis.util.GlobalConfig;
import cn.org.atool.fluent.mybatis.util.GlobalConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.MapperBuilderAssistant;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

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
     */
    private static final Map<Class<?>, TableInfo> TABLE_INFO_CACHE = new ConcurrentHashMap<>();

    /**
     * 默认表主键名称
     */
    private static final String DEFAULT_ID_NAME = "id";

    /**
     * <p>
     * 获取实体映射表信息
     * </p>
     *
     * @param clazz 反射实体类
     * @return 数据库表反射信息
     */
    public static TableInfo getTableInfo(Class<?> clazz) {
        if (clazz == null
            || ReflectUtils.isPrimitiveOrWrapper(clazz)
            || clazz == String.class) {
            return null;
        }
        // https://github.com/baomidou/mybatis-plus/issues/299
        TableInfo tableInfo = TABLE_INFO_CACHE.get(ReflectUtils.getProxyTargetClass(clazz));
        if (null != tableInfo) {
            return tableInfo;
        }
        //尝试获取父类缓存
        Class<?> currentClass = clazz;
        while (null == tableInfo && Object.class != currentClass) {
            currentClass = currentClass.getSuperclass();
            tableInfo = TABLE_INFO_CACHE.get(ReflectUtils.getProxyTargetClass(currentClass));
        }
        if (tableInfo != null) {
            TABLE_INFO_CACHE.put(ReflectUtils.getProxyTargetClass(clazz), tableInfo);
        }
        return tableInfo;
    }

    /**
     * <p>
     * 获取所有实体映射表信息
     * </p>
     *
     * @return 数据库表反射信息集合
     */
    @SuppressWarnings("unused")
    public static List<TableInfo> getTableInfos() {
        return new ArrayList<>(TABLE_INFO_CACHE.values());
    }

    /**
     * <p>
     * 实体类反射获取表信息【初始化】
     * </p>
     *
     * @param clazz 反射实体类
     * @return 数据库表反射信息
     */
    public synchronized static TableInfo initTableInfo(MapperBuilderAssistant builderAssistant, Class<?> clazz) {
        TableInfo tableInfo = TABLE_INFO_CACHE.get(clazz);
        if (tableInfo != null) {
            if (builderAssistant != null) {
                tableInfo.setConfiguration(builderAssistant.getConfiguration());
            }
            return tableInfo;
        }

        /* 没有获取到缓存信息,则初始化 */
        tableInfo = new TableInfo(clazz);
        GlobalConfig globalConfig;
        if (null != builderAssistant) {
            tableInfo.setCurrentNamespace(builderAssistant.getCurrentNamespace());
            tableInfo.setConfiguration(builderAssistant.getConfiguration());
            globalConfig = GlobalConfigUtils.getGlobalConfig(builderAssistant.getConfiguration());
        } else {
            // 兼容测试场景
            globalConfig = GlobalConfigUtils.defaults();
        }

        /* 初始化表名相关 */
        initTableName(clazz, globalConfig, tableInfo);

        /* 初始化字段相关 */
        initTableFields(clazz, globalConfig, tableInfo);

        /* 放入缓存 */
        TABLE_INFO_CACHE.put(clazz, tableInfo);

        /* 自动构建 resultMap */
        tableInfo.initResultMapIfNeed();

        return tableInfo;
    }

    /**
     * <p>
     * 初始化 表数据库类型,表名,resultMap
     * </p>
     *
     * @param clazz        实体类
     * @param globalConfig 全局配置
     * @param tableInfo    数据库表反射信息
     */
    private static void initTableName(Class<?> clazz, GlobalConfig globalConfig, TableInfo tableInfo) {
        /* 数据库全局配置 */
        GlobalConfig.DbConfig dbConfig = globalConfig.getDbConfig();
        TableName table = clazz.getAnnotation(TableName.class);

        String tableName = table.value();

        String schema = dbConfig.getSchema();

        if (StringUtils.isNotEmpty(table.schema())) {
            schema = table.schema();
        }
        /* 表结果集映射 */
        if (StringUtils.isNotEmpty(table.resultMap())) {
            tableInfo.setResultMap(table.resultMap());
        }
        tableInfo.setAutoInitResultMap(table.autoResultMap());


        String targetTableName = tableName;

        if (StringUtils.isNotEmpty(schema)) {
            targetTableName = schema + Constants.DOT + targetTableName;
        }

        tableInfo.setTableName(targetTableName);

        /* 开启了自定义 KEY 生成器 */
        if (null != dbConfig.getKeyGenerator()) {
            tableInfo.setKeySequence(clazz.getAnnotation(KeySequence.class));
        }
    }

    /**
     * <p>
     * 初始化 表主键,表字段
     * </p>
     *
     * @param clazz        实体类
     * @param globalConfig 全局配置
     * @param tableInfo    数据库表反射信息
     */
    public static void initTableFields(Class<?> clazz, GlobalConfig globalConfig, TableInfo tableInfo) {
        /* 数据库全局配置 */
        GlobalConfig.DbConfig dbConfig = globalConfig.getDbConfig();
        List<Field> list = getAllFields(clazz);
        // 标记是否读取到主键
        boolean isReadPK = false;
        // 是否存在 @TableId 注解
        boolean existTableId = isExistTableId(list);
        List<FieldInfo> fieldList = new ArrayList<>();
        for (Field field : list) {
            /*
             * 主键ID 初始化
             */
            if (!isReadPK) {
                if (existTableId) {
                    isReadPK = initTableIdWithAnnotation(dbConfig, tableInfo, field, clazz);
                } else {
                    isReadPK = initTableIdWithoutAnnotation(dbConfig, tableInfo, field, clazz);
                }
                if (isReadPK) {
                    continue;
                }
            }
            /* 有 @TableField 注解的字段初始化 */
            TableField tableField = field.getAnnotation(TableField.class);
            fieldList.add(new FieldInfo(field, tableField));
        }

        /* 字段列表,不可变集合 */
        tableInfo.setFieldList(Collections.unmodifiableList(fieldList));

        /* 未发现主键注解，提示警告信息 */
        if (StringUtils.isEmpty(tableInfo.getKeyColumn())) {
            log.warn(String.format("Warn: Could not find @TableId in Class: %s.", clazz.getName()));
        }
    }

    /**
     * <p>
     * 判断主键注解是否存在
     * </p>
     *
     * @param list 字段列表
     * @return true 为存在 @TableId 注解;
     */
    public static boolean isExistTableId(List<Field> list) {
        for (Field field : list) {
            TableId tableId = field.getAnnotation(TableId.class);
            if (tableId != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * 主键属性初始化
     * </p>
     *
     * @param dbConfig  全局配置信息
     * @param tableInfo 表信息
     * @param field     字段
     * @param clazz     实体类
     * @return true 继续下一个属性判断，返回 continue;
     */
    private static boolean initTableIdWithAnnotation(GlobalConfig.DbConfig dbConfig, TableInfo tableInfo,
                                                     Field field, Class<?> clazz) {
        TableId tableId = field.getAnnotation(TableId.class);
        if (tableId == null) {
            return false;
        }
        if (StringUtils.isEmpty(tableInfo.getKeyColumn())) {
            /* 主键策略（ 注解 > 全局 ） */
            // 设置 Sequence 其他策略无效
            if (IdType.NONE == tableId.type()) {
                tableInfo.setIdType(dbConfig.getIdType());
            } else {
                tableInfo.setIdType(tableId.type());
            }

            /* 字段 */
            String column = tableId.value();

            tableInfo.setKeyRelated(checkRelated(field.getName(), column))
                .setKeyColumn(column)
                .setKeyProperty(field.getName())
                .setKeyType(field.getType());
            return true;
        } else {
            throw throwExceptionId(clazz);
        }
    }

    /**
     * <p>
     * 主键属性初始化
     * </p>
     *
     * @param tableInfo 表信息
     * @param field     字段
     * @param clazz     实体类
     * @return true 继续下一个属性判断，返回 continue;
     */
    private static boolean initTableIdWithoutAnnotation(GlobalConfig.DbConfig dbConfig, TableInfo tableInfo,
                                                        Field field, Class<?> clazz) {
        String column = field.getName();
        if (dbConfig.isCapitalMode()) {
            column = column.toUpperCase();
        }
        if (DEFAULT_ID_NAME.equalsIgnoreCase(column)) {
            if (StringUtils.isEmpty(tableInfo.getKeyColumn())) {
                tableInfo.setKeyRelated(checkRelated(field.getName(), column))
                    .setIdType(dbConfig.getIdType())
                    .setKeyColumn(column)
                    .setKeyProperty(field.getName())
                    .setKeyType(field.getType());
                return true;
            } else {
                throw throwExceptionId(clazz);
            }
        }
        return false;
    }

    /**
     * <p>
     * 判定 related 的值
     * </p>
     *
     * @param property 属性名
     * @param column   字段名
     * @return related
     */
    public static boolean checkRelated(String property, String column) {
        column = StringUtils.getTargetColumn(column);
        String propertyUpper = property.toUpperCase(Locale.ENGLISH);
        String columnUpper = column.toUpperCase(Locale.ENGLISH);

        // 开启了驼峰并且 column 包含下划线
        return !(propertyUpper.equals(columnUpper) ||
            propertyUpper.equals(columnUpper.replace(Constants.UNDERSCORE, Constants.EMPTY)));
    }

    /**
     * 发现设置多个主键注解抛出异常
     */
    private static FluentMybatisException throwExceptionId(Class<?> clazz) {
        return FluentMybatisException.instance("There must be only one, Discover multiple @TableId annotation in %s", clazz.getName());
    }

    /**
     * <p>
     * 获取该类的所有属性列表
     * </p>
     *
     * @param clazz 反射类
     * @return 属性集合
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = ReflectUtils.getFieldList(ReflectUtils.getProxyTargetClass(clazz));
        return fields.stream().filter(TableHelper::isTableField).collect(toList());
    }

    /**
     * 过滤注解非表字段属性
     *
     * @param field
     * @return
     */
    private static boolean isTableField(Field field) {
        TableField tableField = field.getAnnotation(TableField.class);
        if (tableField != null) {
            return true;
        } else {
            TableId tableId = field.getAnnotation(TableId.class);
            return tableId != null;
        }
    }
}