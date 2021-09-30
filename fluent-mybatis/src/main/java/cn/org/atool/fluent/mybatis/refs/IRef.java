package cn.org.atool.fluent.mybatis.refs;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.metadata.SetterMeta;
import cn.org.atool.fluent.mybatis.spring.IConvertor;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.refs.ARef.instance;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.*;
import static java.util.stream.Collectors.toSet;

/**
 * 常用工具方法入口
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused"})
public interface IRef {

    /**
     * 返回clazz实体对应的默认Query实例
     *
     * @param eClass Entity类类型
     * @return IQuery
     */
    static IQuery query(Class<? extends IEntity> eClass) {
        return RefKit.byEntity(entityClass(eClass)).query();
    }

    /**
     * 返回clazz实体对应的空Query实例
     *
     * @param eClass Entity类类型
     * @return IQuery
     */
    static IQuery emptyQuery(Class<? extends IEntity> eClass) {
        return RefKit.byEntity(entityClass(eClass)).emptyQuery();
    }

    /**
     * 返回clazz实体对应的默认Updater实例
     *
     * @param eClass Entity类类型
     * @return IUpdate
     */
    static IUpdate updater(Class<? extends IEntity> eClass) {
        return RefKit.byEntity(entityClass(eClass)).updater();
    }

    /**
     * 返回clazz实体对应的空Updater实例
     *
     * @param eClass Entity类类型
     * @return IUpdate
     */
    static IUpdate emptyUpdater(Class<? extends IEntity> eClass) {
        return RefKit.byEntity(entityClass(eClass)).emptyUpdater();
    }

    /**
     * 返回clazz属性field对应的数据库字段名称
     *
     * @param eClass Entity类类型
     * @param field  entity属性名
     * @return 数据库字段名称
     */
    static String columnOfField(Class<? extends IEntity> eClass, String field) {
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
     * @param clazz Entity类类型
     * @return 主键字段
     */
    static String primaryColumn(Class<? extends IEntity> eClass) {
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
     * @param entities 如果为空, 变更应用中所有的实体类对应数据库类型; 如果不为空, 变更指定类
     */
    static void changeDbType(DbType dbType, IEntity... entities) {
        Set<String> list = Stream.of(entities).map(e -> e.getClass().getName()).collect(toSet());
        if (If.isEmpty(entities)) {
            list = instance().allEntityClass();
        }
        for (String klass : list) {
            instance().byEntity(klass).db(dbType);
        }
    }

    static void register(Type type, IConvertor convertor) {
        SetterMeta.register(type, convertor);
    }

    static void register(String typeName, IConvertor convertor) {
        SetterMeta.register(typeName, convertor);
    }

    static <T> T valueByField(IEntity entity, String field) {
        assertNotNull("entity", entity);
        return RefKit.entityKit(entity.entityClass()).valueByField(entity, field);
    }

    static <T> T valueByColumn(IEntity entity, String field) {
        assertNotNull("entity", entity);
        return RefKit.entityKit(entity.entityClass()).valueByColumn(entity, field);
    }

    /**
     * 拷贝一个entity对象
     *
     * @param entity 实例
     * @return 拷贝
     */
    static <E extends IEntity> E copy(E entity) {
        return entity == null ? null : RefKit.entityKit(entity.entityClass()).copy(entity);
    }

    /**
     * map对应属性值设置到Entity对象中
     *
     * @param map map
     * @return map转对象
     */
    static <E extends IEntity> E toEntity(Class<E> eClass, Map<String, Object> map) {
        return map == null ? null : RefKit.entityKit(eClass).toEntity(map);
    }

    /**
     * entity对象转换为map对象
     * key值为entity的属性字段名
     *
     * @param entity 实例
     * @return 实例属性名称:属性值
     */
    static Map<String, Object> toMap(IEntity entity) {
        if (entity == null) {
            return new HashMap<>();
        } else {
            return RefKit.entityKit(entity.entityClass()).toEntityMap(entity, false);
        }
    }
}