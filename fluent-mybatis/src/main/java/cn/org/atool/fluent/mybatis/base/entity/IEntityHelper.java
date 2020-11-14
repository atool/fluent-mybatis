package cn.org.atool.fluent.mybatis.base.entity;

import java.util.Map;

/**
 * entity帮助类
 *
 * @author wudarui
 */
public interface IEntityHelper {
    /**
     * entity对象转换为map对象
     * key值为entity的属性字段名
     *
     * @param entity
     * @return
     */
    Map<String, Object> toEntityMap(IEntity entity);

    /**
     * entity对象转换为map对象
     * key值为对应的数据库表字段名
     *
     * @param entity
     * @return
     */
    Map<String, Object> toColumnMap(IEntity entity);

    /**
     * map对应属性值设置到Entity对象中
     *
     * @param map
     * @return
     */
    <E extends IEntity> E toEntity(Map<String, Object> map);

    /**
     * 拷贝一个entity对象
     *
     * @param entity
     * @return
     */
    <E extends IEntity> E copy(IEntity entity);
}