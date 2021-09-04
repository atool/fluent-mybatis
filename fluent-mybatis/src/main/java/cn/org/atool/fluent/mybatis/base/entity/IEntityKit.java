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
     * entity对象转换为map对象
     * key值为entity的属性字段名
     *
     * @param entity 实例
     * @param isNoN  true:仅仅非空字段; false: 所有字段
     * @return 实例属性名称:属性值
     */
    Map<String, Object> toEntityMap(IEntity entity, boolean isNoN);

    /**
     * entity对象转换为map对象
     * key值为对应的数据库表字段名
     *
     * @param entity 实例
     * @param isNoN  true:仅仅非空字段; false: 所有字段
     * @return 实例数据库字段名: 属性值
     */
    Map<String, Object> toColumnMap(IEntity entity, boolean isNoN);

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
     * 返回属性值
     *
     * @param fieldName 字段名称
     * @return 属性值
     */
    Object getFieldValue(IEntity entity, String fieldName);
}