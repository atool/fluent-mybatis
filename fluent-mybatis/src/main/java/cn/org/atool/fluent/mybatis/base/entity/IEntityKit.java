package cn.org.atool.fluent.mybatis.base.entity;

import cn.org.atool.fluent.mybatis.base.IEntity;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;

import java.util.Map;

/**
 * entity帮助类
 *
 * @author wudarui
 */
@SuppressWarnings({ "UnusedReturnValue", "rawtypes" })
public interface IEntityKit {
    /**
     * new Entity
     *
     * @param <E> 实体类型
     * @return Entity
     */
    <E extends IEntity> E newEntity();

    /**
     * entity对象转换为map对象
     * key值为entity的属性字段名
     *
     * @param entity      实例
     * @param allowedNull is allowed null, true: 所有字段, false: 只允许非空值
     * @param fields      指定字段
     * @return 实例属性名称:属性值
     */
    Map<String, Object> toEntityMap(IEntity entity, boolean allowedNull, FieldMapping... fields);

    /**
     * entity对象转换为map对象
     * key值为对应的数据库表字段名
     *
     * @param entity      实例
     * @param allowedNull is allowed null, true: 所有字段, false: 只允许非空值
     * @param fields      指定字段
     * @return 实例数据库字段名: 属性值
     */
    Map<String, Object> toColumnMap(IEntity entity, boolean allowedNull, FieldMapping... fields);

    /**
     * map对应属性值设置到Entity对象中, 同JSON反序列化
     *
     * @param map map
     * @param <E> 实体类型
     * @return map转对象
     */
    <E extends IEntity> E toEntity(Map<String, Object> map);

    /**
     * 拷贝一个entity对象
     *
     * @param entity 实例
     * @param <E>    实体类型
     * @return 拷贝
     */
    <E extends IEntity> E copy(IEntity entity);

    /**
     * 根据实体属性名称返回属性值
     *
     * @param entity    Entity instance
     * @param fieldName 实体属性名称
     * @param <E>       值类型
     * @return 属性值
     */
    <E> E valueByField(IEntity entity, String fieldName);

    /**
     * 设置实体属性值
     *
     * @param entity    Entity instance
     * @param fieldName 实体属性名称
     * @param value     属性值
     * @param <E>       实体类型
     * @return ignore
     */
    <E extends IEntity> E valueByField(E entity, String fieldName, Object value);

    /**
     * 根据数据库字段名称返回属性值
     *
     * @param entity Entity instance
     * @param column 数据库字段名称
     * @param <E>    值类型
     * @return 属性值
     */
    <E> E valueByColumn(IEntity entity, String column);

    /**
     * 设置实体属性值
     *
     * @param entity     Entity instance
     * @param columnName 数据库字段名称
     * @param value      属性值
     * @param <E>        实体类型
     * @return ignore
     */
    <E extends IEntity> E valueByColumn(E entity, String columnName, Object value);
}