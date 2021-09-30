package cn.org.atool.fluent.mybatis.refs;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.functions.TableDynamic;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.metadata.SetterMeta;
import cn.org.atool.fluent.mybatis.spring.IConvertor;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static cn.org.atool.fluent.mybatis.refs.ARef.instance;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.*;

/**
 * 常用工具方法入口
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public abstract class IRef {

    /**
     * 返回clazz实体对应的默认Query实例
     *
     * @param eClass Entity类类型
     * @return IQuery
     */
    public static <E extends IEntity> IQuery query(Class<E> eClass) {
        return RefKit.byEntity(entityClass(eClass)).query();
    }

    /**
     * 返回clazz实体对应的空Query实例
     *
     * @param eClass Entity类类型
     * @return IQuery
     */
    public static <E extends IEntity> IQuery emptyQuery(Class<E> eClass) {
        return RefKit.byEntity(entityClass(eClass)).emptyQuery();
    }

    /**
     * 返回clazz实体对应的默认Updater实例
     *
     * @param eClass Entity类类型
     * @return IUpdate
     */
    public static <E extends IEntity> IUpdate updater(Class<E> eClass) {
        return RefKit.byEntity(entityClass(eClass)).updater();
    }

    /**
     * 返回clazz实体对应的空Updater实例
     *
     * @param eClass Entity类类型
     * @return IUpdate
     */
    public static <E extends IEntity> IUpdate emptyUpdater(Class<E> eClass) {
        return RefKit.byEntity(entityClass(eClass)).emptyUpdater();
    }

    /**
     * 返回clazz属性field对应的数据库字段名称
     *
     * @param eClass Entity类类型
     * @param field  entity属性名
     * @return 数据库字段名称
     */
    public static <E extends IEntity> String columnOfField(Class<E> eClass, String field) {
        IMapping mapping = RefKit.byEntity(eClass);
        if (mapping == null) {
            throw notFluentMybatisException(eClass);
        } else {
            return mapping.columnOfField(field);
        }
    }

    /**
     * 返回clazz实体的主键字段
     *
     * @param eClass Entity类类型
     * @return 主键字段
     */
    public static <E extends IEntity> String primaryColumn(Class<E> eClass) {
        IMapping mapping = RefKit.byEntity(eClass);
        if (mapping == null) {
            throw notFluentMybatisException(eClass);
        } else {
            return mapping.primaryId(false);
        }
    }

    /**
     * 设置对应的实体类对应的数据库类型
     *
     * @param dbType   要变更成的数据库类型
     * @param eClasses 如果为空, 变更应用中所有的实体类对应数据库类型; 如果不为空, 变更指定类
     */
    public static void changeDbType(DbType dbType, Class<? extends IEntity>... eClasses) {
        Set<String> list = RefKit.getEntityClass(eClasses);
        for (String klass : list) {
            instance().byEntity(klass).db(dbType);
        }
    }

    /**
     * 设置对应表的命名策略
     *
     * @param tableSupplier 表的命名策略
     * @param eClasses      如果为空, 变更应用中所有的实体类对应数据库类型; 如果不为空, 变更指定类
     */
    public static void tableSupplier(TableDynamic tableSupplier, Class<? extends IEntity>... eClasses) {
        Set<String> list = RefKit.getEntityClass(eClasses);
        for (String klass : list) {
            instance().byEntity(klass).setTableSupplier(tableSupplier);
        }
    }


    /**
     * 注册PoJoHelper.toPoJo时特定类型的转换器
     *
     * @param type      类型
     * @param convertor 类型转换器
     */
    public static void register(Type type, IConvertor convertor) {
        SetterMeta.register(type, convertor);
    }

    /**
     * 注册PoJoHelper.toPoJo时特定类型的转换器
     *
     * @param typeName  类型, 比如 java.util.List&lt;java.lang.String&gt;
     * @param convertor 类型转换器
     */
    public static void register(String typeName, IConvertor convertor) {
        SetterMeta.register(typeName, convertor);
    }

    /**
     * 获取entity的属性field值
     *
     * @param entity 实体类
     * @param field  属性名称
     * @param <T>    属性值类型
     * @return 属性值
     */
    public static <T> T valueByField(IEntity entity, String field) {
        assertNotNull("entity", entity);
        return RefKit.entityKit(entity.entityClass()).valueByField(entity, field);
    }

    /**
     * 获取entity的对应数据库字段的属性值
     *
     * @param entity 实体类
     * @param column 数据库字段名称
     * @param <T>    属性值类型
     * @return 属性值
     */
    public static <T> T valueByColumn(IEntity entity, String column) {
        assertNotNull("entity", entity);
        return RefKit.entityKit(entity.entityClass()).valueByColumn(entity, column);
    }

    /**
     * 拷贝一个entity对象
     *
     * @param entity 实例
     * @return 拷贝
     */
    public static <E extends IEntity> E copy(E entity) {
        return entity == null ? null : RefKit.entityKit(entity.entityClass()).copy(entity);
    }

    /**
     * map对应属性值设置到Entity对象中
     *
     * @param map map
     * @return map转对象
     */
    public static <E extends IEntity> E toEntity(Class<E> eClass, Map<String, Object> map) {
        return map == null ? null : RefKit.entityKit(eClass).toEntity(map);
    }

    /**
     * entity对象转换为map对象
     * key值为entity的属性字段名
     *
     * @param entity 实例
     * @return 实例属性名称:属性值
     */
    public static Map<String, Object> toMap(IEntity entity) {
        if (entity == null) {
            return new HashMap<>();
        } else {
            return RefKit.entityKit(entity.entityClass()).toEntityMap(entity, false);
        }
    }
}