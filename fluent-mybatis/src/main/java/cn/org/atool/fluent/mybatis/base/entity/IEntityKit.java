package cn.org.atool.fluent.mybatis.base.entity;

import cn.org.atool.fluent.mybatis.base.IEntity;

import java.util.Map;

/**
 * entity帮助类
 *
 * @author wudarui
 */
public interface IEntityKit {
    /**
     * new Entity
     *
     * @return Entity
     */
    <E extends IEntity> E newEntity();

    /**
     * entity对象转换为map对象
     * key值为entity的属性字段名
     *
     * @param entity      实例
     * @param allowedNull is allowed null, true: 所有字段, false: 只允许非空值
     * @return 实例属性名称:属性值
     */
    Map<String, Object> toEntityMap(IEntity entity, boolean allowedNull);

    /**
     * entity对象转换为map对象
     * key值为对应的数据库表字段名
     *
     * @param entity      实例
     * @param allowedNull is allowed null, true: 所有字段, false: 只允许非空值
     * @return 实例数据库字段名: 属性值
     */
    Map<String, Object> toColumnMap(IEntity entity, boolean allowedNull);

    /**
     * map对应属性值设置到Entity对象中, 同JSON反序列化
     *
     * @param map map
     * @return map转对象
     */
    <E extends IEntity> E toEntity(Map<String, Object> map);

    /**
     * 拷贝一个entity对象
     *
     * @param entity 实例
     * @return 拷贝
     */
    <E extends IEntity> E copy(IEntity entity);

    /**
     * 根据实体属性名称返回属性值
     *
     * @param entity    Entity instance
     * @param fieldName 实体属性名称
     * @return 属性值
     */
    <T> T valueByField(IEntity entity, String fieldName);

    /**
     * 设置实体属性值
     *
     * @param entity    Entity instance
     * @param fieldName 实体属性名称
     * @param value     属性值
     * @return ignore
     */
    <E extends IEntity> E valueByField(E entity, String fieldName, Object value);

    /**
     * 根据数据库字段名称返回属性值
     *
     * @param entity Entity instance
     * @param column 数据库字段名称
     * @return 属性值
     */
    <T> T valueByColumn(IEntity entity, String column);


}